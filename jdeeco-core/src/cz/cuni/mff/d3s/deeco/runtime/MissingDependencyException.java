package cz.cuni.mff.d3s.deeco.runtime;

/**
 * Thrown when a plugin specifies a dependency on another plugin that is not provided. 
 * @author Filip Krijt <krijt@d3s.mff.cuni.cz>
 *
 */
public class MissingDependencyException extends PluginDependencyException {	
	private static final long serialVersionUID = 1L;

	public MissingDependencyException(Class pluginWithMissingDependency, Class missingDependency) {
		super("Missing dependency" + missingDependency + " for plugin "+ pluginWithMissingDependency + ".");
	}
}
