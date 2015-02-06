package cz.cuni.mff.d3s.deeco.runtime;

/**
 * Thrown when the dependency graph of plugins contains a cycle.
 * @author Filip Krijt <krijt@d3s.mff.cuni.cz>
 *
 */
public class CycleDetectedException extends PluginDependencyException {
	private static final long serialVersionUID = 1L;

	public CycleDetectedException() {
		super("Found a cycle.");		
	}
}
