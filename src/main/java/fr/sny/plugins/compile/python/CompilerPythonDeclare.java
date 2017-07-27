/**
 * 
 */
package fr.sny.plugins.compile.python;

import java.util.Map;
import java.util.UUID;

import org.kohsuke.MetaInfServices;

import fr.esgi.projet.softwareneedsyou.api.compiler.PluginCompiler;
import fr.esgi.projet.softwareneedsyou.api.compiler.PluginCompilerDeclare;
import fr.esgi.projet.softwareneedsyou.api.spi.InitialisationException;

/**
 * @author Tristan
 *
 */
@MetaInfServices
public class CompilerPythonDeclare implements PluginCompilerDeclare {
	private final static UUID uid = UUID.fromString("d40a12f4-5c80-42f4-8b05-5a5b466c620c");

	/**
	 * 
	 */
	public CompilerPythonDeclare() {
	}

	/* (non-Javadoc)
	 * @see fr.esgi.projet.softwareneedsyou.api.spi.PluginDescriptor#getDescription()
	 */
	@Override
	public String getDescription() {
		return "A basic Python Compiler.\nTests are not yet supported (future version).";
	}

	/* (non-Javadoc)
	 * @see fr.esgi.projet.softwareneedsyou.api.spi.PluginDescriptor#getID()
	 */
	@Override
	public UUID getID() {
		return uid;
	}

	/* (non-Javadoc)
	 * @see fr.esgi.projet.softwareneedsyou.api.spi.PluginDescriptor#getInstance(java.util.Map)
	 */
	@Override
	public PluginCompiler getInstance(final Map<String, Object> params) throws InitialisationException {
		return new CompilerPython();
	}

	/* (non-Javadoc)
	 * @see fr.esgi.projet.softwareneedsyou.api.spi.PluginDescriptor#getResume()
	 */
	@Override
	public String getResume() {
		return "A simple Python Compiler";
	}

}
