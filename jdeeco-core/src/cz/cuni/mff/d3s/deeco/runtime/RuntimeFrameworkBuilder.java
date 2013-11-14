package cz.cuni.mff.d3s.deeco.runtime;

import cz.cuni.mff.d3s.deeco.executor.Executor;
import cz.cuni.mff.d3s.deeco.executor.SameThreadExecutor;
import cz.cuni.mff.d3s.deeco.knowledge.CloningKnowledgeManagerContainer;
import cz.cuni.mff.d3s.deeco.knowledge.CloningKnowledgeManagerContainer;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.scheduler.LocalTimeScheduler;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;

/**
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 *
 */
public class RuntimeFrameworkBuilder {
	
	
	Scheduler scheduler;
	Executor executor;
	CloningKnowledgeManagerContainer kmContainer;	
	RuntimeConfiguration configuration;	
	RuntimeFramework runtime;
	

	public RuntimeFrameworkBuilder(RuntimeConfiguration configuration) {
		if (configuration == null) {
			throw new IllegalArgumentException("Configuration must not be null");
		}		
		
		this.configuration = configuration;
	}
	
	protected void buildScheduler() {		
		if (configuration.scheduling == null) {
			String msg = "No Executor available for " + configuration.toString();
			Log.w(msg);
			throw new UnsupportedOperationException(msg);
		}
			
		switch (configuration.scheduling) {
		case WALL_TIME:
			scheduler = new LocalTimeScheduler();
			break;
		default:
			String msg = "No Scheduler available for " + configuration.toString();
			Log.w(msg);
			throw new UnsupportedOperationException(msg);
		}
	}
	
	protected void buildExecutor() {		
		if (configuration.execution == null) {
			String msg = "No Executor available for " + configuration.toString();
			Log.w(msg);
			throw new UnsupportedOperationException(msg);
		}
		
		switch (configuration.execution) {
		case SINGLE_THREADED:
			executor = new SameThreadExecutor();
			break;
		default:
			String msg = "No Executor available for " + configuration.toString();
			Log.w(msg);
			throw new UnsupportedOperationException(msg);
		}
	}
	
	protected void buildKnowledgeManagerRegistry() {		
		kmContainer = new CloningKnowledgeManagerContainer();
	}
	
	protected void connect() {
		// wire things together
		scheduler.setExecutor(executor);
		executor.setExecutionListener(scheduler);
	}
	
	protected void buildRuntime(RuntimeMetadata model) {	
		// FIXME if we add support for sharing scheduler/synchronizer among
		// multiple runtimes (for simulation), we will probably override/extend
		// this method 
		runtime = new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer);
	}
	
	
	
	public RuntimeFramework build(RuntimeMetadata model) {
		if (model == null) {
			throw new IllegalArgumentException("Model must not be null");
		}	
		
		buildScheduler();
		buildExecutor();
		buildKnowledgeManagerRegistry();
		//buildNetworkContainer() - by default does nothing = no network is used
		//buildSynchronizer() - by default does nothing = only one runtime in only one JVM
		connect();
		buildRuntime(model);
		return runtime;
	}
	
}
