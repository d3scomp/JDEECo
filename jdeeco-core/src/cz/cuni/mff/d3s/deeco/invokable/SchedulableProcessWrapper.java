package cz.cuni.mff.d3s.deeco.invokable;

import java.util.List;

/**
 * Base class for wrapping a
 * {@link cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess SchedulableProcess}
 * with the sole purpose of keeping track of the the knowledge paths that get
 * updated. To be used by the
 * {@link cz.cuni.mff.d3s.deeco.scheduling.discrete.DiscreteScheduler
 * DiscreteScheduler} in order to implement its triggering mechanism.
 * 
 * @author Ilias Gerostathopoulos
 * 
 */
public abstract class SchedulableProcessWrapper {

	// wrapped process:
	private final SchedulableProcess sp;
	// structure to keep the updated knowledge paths on each execution:
	protected List<String> changedKnowledgePaths;

	public SchedulableProcessWrapper(SchedulableProcess sp) {
		this.sp = sp;
	}

	public abstract void invoke();

	/**
	 * Retrieves a list of the knowledge paths that were updated during last
	 * execution
	 * 
	 * @return list of strings
	 */
	public List<String> getChangedKnowledgePaths() {
		return changedKnowledgePaths;
	}
	
	public SchedulableProcess getProcess() {
		return sp;
	}
}
