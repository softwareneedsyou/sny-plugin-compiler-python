/**
 * 
 */
package fr.sny.plugins.compile.python;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
public class CompilerEx implements PluginCompiler {

	/**
	 * 
	 */
	public CompilerEx() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see java.lang.AutoCloseable#close()
	 */
	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see fr.esgi.projet.softwareneedsyou.api.compiler.PluginCompiler#compileAndTest()
	 */
	@Override
	public ResultCompiler compileAndTest(final String code, final InputStream testsDef, final ConsoleOutput console) throws CompilerException {
		//TODO: compile the code
		if(true) {
			ResultCompiler result = new ResultCompiler(true);
			//TODO: run tests
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
			}
			return result;
		} else
			return new ResultCompiler(false);
	}

	private final static Set<String> languages = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("MyLanguage", "OtherLanguage")));
	/* (non-Javadoc)
	 * @see fr.esgi.projet.softwareneedsyou.api.compiler.PluginCompiler#getLngSupported()
	 */
	@Override
	public Set<String> getLngSupported() {
		return languages;
	}

}
