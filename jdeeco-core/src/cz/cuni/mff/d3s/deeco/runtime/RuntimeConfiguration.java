package cz.cuni.mff.d3s.deeco.runtime;

import cz.cuni.mff.d3s.deeco.executor.Executor;
import cz.cuni.mff.d3s.deeco.executor.SameThreadedExecutor;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerRegistry;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.scheduler.LocalTimeScheduler;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;

/**
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 *
 */
public class RuntimeConfiguration {
	public enum Scheduling { WALL_TIME } // future: discrete
	public enum Distribution { LOCAL } // future: BEE, IP
	public enum Execution { SINGLE_THREADED } // future: multi-threaded, actor-based
	
	Scheduling scheduling;
	Distribution distribution;
	Execution execution;

	
	public RuntimeConfiguration(Scheduling scheduling,
			Distribution distribution, Execution execution) {
		this.scheduling = scheduling;
		this.distribution = distribution;
		this.execution = execution;
		
	}

	public Scheduling getScheduling() {
		return scheduling;
	}

	public Distribution getDistribution() {
		return distribution;
	}

	public Execution getExecution() {
		return execution;
	}
	
	

	@Override
	public String toString() {
		return "RuntimeConfiguration [scheduling=" + scheduling
				+ ", distribution=" + distribution + ", execution=" + execution
				+ "]";
	}
	
	
	
	
	
	
}
