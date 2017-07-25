/**
 * 
 */
package fr.sny.plugins.compile.python;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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
		Path dest = tmp.resolve("tmp.py");
		try {
			Files.write(dest, code.getBytes(StandardCharsets.UTF_8), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
			ProcessBuilder pb = new ProcessBuilder(this.params.getProperty("exe", "python"), "-m", "compileall", "-f", ".");
			pb.directory(tmp.toFile());
			Process p = pb.start();
			IOUtils.copy(p.getInputStream(), console.getOutput());
			IOUtils.copy(p.getErrorStream(), console.getError());
			if(p.waitFor() == 0) { //p.exitValue
				ResultCompiler result = new ResultCompiler(true);
				/*//TODO: run tests
				List<Integer> tests = Arrays.asList(1, 2, 3, 4);
				for(Integer i : tests) {
					//if(file.test[i].run())
					ResultTest res = new ResultTest();
					if(i%2 == 0) {
						res.setState(TestState.SUCCESS);
						result.addResultTest(i, res);
					} else {
						res.setState(i%3==0 ? TestState.FAIL : TestState.WARNING);
						res.setDetail("details of error");
						result.addResultTest(i, res);
					}
				}*/
				return result;
			} else
				return new ResultCompiler(false);
		} catch (IOException | InterruptedException e) {
			throw new CompilerException(e);
		}
	}

	private final static Set<String> languages = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("MyLanguage", "OtherLanguage")));
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
			e.printStackTrace();
		}
		return props;
	}
	
	public final static void main(final String[] args) {
		CompilerPython comp = new CompilerPython();
		try {
			comp.compileAndTest("", null, new ConsoleOutput() {
				private final PrintWriter writer = new PrintWriter(System.out);
				@Override
				public PrintWriter getWriter(boolean arg0) {
					return writer;
				}
			});
		} catch (CompilerException e) {
			e.printStackTrace();
		}
	}
}
