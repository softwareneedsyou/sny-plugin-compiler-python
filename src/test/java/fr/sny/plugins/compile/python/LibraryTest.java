package fr.sny.plugins.compile.python;
/*
 * This Java source file was generated by the Gradle 'init' task.
 */
import org.junit.Test;

import fr.esgi.projet.softwareneedsyou.api.ConsoleOutput;
import fr.esgi.projet.softwareneedsyou.api.compiler.CompilerException;
import fr.esgi.projet.softwareneedsyou.api.compiler.ResultCompiler;
import static org.junit.Assert.*;

import java.io.PrintWriter;

public class LibraryTest {
    @Test
    public void testLibraryCompiler() throws CompilerException, Exception {
        CompilerPython compiler = new CompilerPython();
        ResultCompiler result = compiler.compileAndTest("# test\ndef fn():\n\tprint('test')\n", null, new ConsoleOutput() {
			@Override
			public PrintWriter getWriter(final boolean isErr) {
				return new PrintWriter(isErr ? System.err : System.out);
			}
		});
        assertNotNull(result);
        assertTrue(result.isCompileSuccess());
        compiler.close();
    }
}
