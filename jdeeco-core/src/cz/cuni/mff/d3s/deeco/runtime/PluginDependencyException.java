package cz.cuni.mff.d3s.deeco.runtime;

/**
 * Thrown in case of error (missing dependency, cyclic dependency, etc.) when resolving the dependencies of DEECo plugins. 
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 *
 */
public class PluginDependencyException extends Exception {

	private static final long serialVersionUID = 1L;

	public PluginDependencyException(final String msg) {
		super(msg);
	}

}
