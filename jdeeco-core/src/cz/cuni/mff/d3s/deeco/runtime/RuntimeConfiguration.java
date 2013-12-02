package cz.cuni.mff.d3s.deeco.runtime;


/**
 * A container for {@link RuntimeFramework} configuration options.
 * <p>
 * Hides the details of different options for runtime framework setup by providing a set of intuitive enums.
 * The enums are then translated to actual runtime framework setup by {@link RuntimeFrameworkBuilder}. 
 * </p>
 * 
 * @see RuntimeFrameworkBuilder
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 * 
 */
public class RuntimeConfiguration {
	/**
	 * The enumeration of supported options for scheduling setup.
	 */
	public enum Scheduling { WALL_TIME } // future: discrete
	
	/**
	 * The enumeration of supported options for distribution setup.
	 */
	public enum Distribution { LOCAL } // future: BEE, IP
	
	/**
	 * The enumeration of supported options for execution/threading setup.
	 */
	public enum Execution { SINGLE_THREADED } // future: multi-threaded, actor-based
	
	
	
	/**
	 * @see RuntimeConfiguration#getScheduling()
	 */
	
	Scheduling scheduling;
	/**
	 * @see RuntimeConfiguration#getDistribution()
	 */
	Distribution distribution;
	
	/**
	 * @see RuntimeConfiguration#getExecution()
	 */
	Execution execution;

	
	/**
	 * Convenience constructor.
	 */
	public RuntimeConfiguration(Scheduling scheduling,
			Distribution distribution, Execution execution) {
		this.scheduling = scheduling;
		this.distribution = distribution;
		this.execution = execution;
		
	}

	/**
	 * The selected {@link Scheduling}.
	 */
	public Scheduling getScheduling() {
		return scheduling;
	}

	/**
	 * The selected {@link Distribution}.
	 */
	public Distribution getDistribution() {
		return distribution;
	}

	/**
	 * The selected {@link Execution}.
	 */
	public Execution getExecution() {
		return execution;
	}	
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RuntimeConfiguration [scheduling=" + scheduling
				+ ", distribution=" + distribution + ", execution=" + execution
				+ "]";
	}
	
	
	
	
	
	
}
