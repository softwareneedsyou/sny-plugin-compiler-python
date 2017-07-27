/**
 * 
 */
package fr.sny.plugins.compile.python;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.IOUtils;

import fr.esgi.projet.softwareneedsyou.SharedParams;
import fr.esgi.projet.softwareneedsyou.api.ConsoleOutput;
import fr.esgi.projet.softwareneedsyou.api.compiler.CompilerException;
import fr.esgi.projet.softwareneedsyou.api.compiler.PluginCompiler;
import fr.esgi.projet.softwareneedsyou.api.compiler.ResultCompiler;
import fr.esgi.projet.softwareneedsyou.api.compiler.ResultTest;
import fr.esgi.projet.softwareneedsyou.api.compiler.TestState;

/**
 * @author Tristan
 *
 */
public class CompilerPython implements PluginCompiler {
	private final static Path folder = SharedParams.AppPluginsFolder.resolve("PythonCompiler");
	private final static Path tmp = folder.resolve("tmp");
	private final static Path conf = folder.resolve("conf_python.properties");
	static {
		if(Files.notExists(folder))
			try {
				Files.createDirectory(folder);
			} catch (IOException e) {
				e.printStackTrace();
			}
		if(Files.notExists(tmp))
			try {
				Files.createDirectories(tmp);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	private final Properties params = getParams();

	/**
	 * 
	 */
	public CompilerPython() {
	}

	/* (non-Javadoc)
	 * @see java.lang.AutoCloseable#close()
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void close() throws Exception {
		try(OutputStream out = new FileOutputStream(conf.toFile())) {
			this.params.save(out, "");
		}
	}

	/* (non-Javadoc)
	 * @see fr.esgi.projet.softwareneedsyou.api.compiler.PluginCompiler#compileAndTest()
	 */
	@Override
	public ResultCompiler compileAndTest(final String code, final InputStream testsDef, final ConsoleOutput console) throws CompilerException {
		try {
			console.getOutput().println("Compilation ...");
			try {
				Files.write(tmp.resolve("__init__.py"), "\n".getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
			} catch (final IOException e1) {
				//e1.printStackTrace();
			}
			final Path dest = tmp.resolve("tmp.py");
			try {
				Files.write(dest, code.getBytes(StandardCharsets.UTF_8), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
				;//.redirectErrorStream(true);
				Process p = new ProcessBuilder(this.params.getProperty("exe", "python"), "-m", "compileall", "-f", ".")
						.inheritIO().directory(tmp.toFile()).start();
				final int exitCode = p.waitFor();
				System.out.println("exit code = " + exitCode);
				IOUtils.copy(p.getInputStream(), console.getOutput());
				IOUtils.copy(p.getErrorStream(), console.getError());
				if(exitCode == 0) { //p.exitValue
					System.out.println("ok!");
					ResultCompiler result = new ResultCompiler(true);
					final Path test = tmp.resolve("test.py");
					Files.copy(testsDef, test, StandardCopyOption.REPLACE_EXISTING);
					p = new ProcessBuilder(this.params.getProperty("exe", "python"), "-m", "compileall", "-f", ".")
							.inheritIO().directory(tmp.toFile()).start();
					result.addResultTest(0, new ResultTest((p.waitFor()==0) ? TestState.SUCCESS : TestState.FAIL, "..."));
					p = new ProcessBuilder(this.params.getProperty("exe", "python"), "-m", "unittest", "test.py")
							.inheritIO().directory(tmp.toFile()).start();
					result.addResultTest(1, new ResultTest((p.waitFor()==0) ? TestState.SUCCESS : TestState.FAIL, "..."));
					return result;
				} else
					return new ResultCompiler(false);
			} catch (IOException | InterruptedException e) {
				throw new CompilerException(e);
			}
		} catch(RuntimeException r) {
			r.printStackTrace();
			throw new CompilerException(r);
		}
	}

	private final static Set<String> languages = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("Python")));
	/* (non-Javadoc)
	 * @see fr.esgi.projet.softwareneedsyou.api.compiler.PluginCompiler#getLngSupported()
	 */
	@Override
	public Set<String> getLngSupported() {
		return languages;
	}

	private final static Properties getParams() {
		Properties props = new Properties();
		try(FileInputStream in = new FileInputStream(conf.toFile())) {
			props.load(in);
		} catch (IOException e) {
			//e.printStackTrace();
		}
		return props;
	}
	
	public final static void main(final String[] args) {
		CompilerPython comp = new CompilerPython();
		try {
			comp.compileAndTest("", null, new ConsoleOutput() {
				private final PrintWriter writer = new PrintWriter(System.out);
				@Override
				public PrintWriter getWriter(final boolean isErr) {
					return writer;
				}
			});
		} catch (CompilerException e) {
			e.printStackTrace();
		}
	}
}
