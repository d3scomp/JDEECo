package cz.cuni.mff.d3s.deeco.runtime;

import java.util.Arrays;

import cz.cuni.mff.d3s.deeco.executor.Executor;
import cz.cuni.mff.d3s.deeco.executor.SameThreadExecutor;
import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.CloningKnowledgeManagerContainer;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerViewImpl;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.scheduler.SingleThreadedScheduler;

/**
 * A builder for assembling a {@link RuntimeFramework} based on a {@link RuntimeConfiguration}.
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
			scheduler = new SingleThreadedScheduler();
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
	
	protected void buildKnowledgeManagerContainer() {		
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
		buildKnowledgeManagerContainer();				
		
		bindKnowledgeManagerContainer(model);
		
		//TODO: buildNetworkContainer() - by default does nothing = no network is used
		//TODO: buildSynchronizer() - by default does nothing = only one runtime in only one JVM
		
		connect();
		buildRuntime(model);
		return runtime;
	}

	/**
	 * Replace the KMs in the model with KMs created via {@link #kmContainer}. 
	 */
	protected void bindKnowledgeManagerContainer(RuntimeMetadata model) {
		for (ComponentInstance ci: model.getComponentInstances()) {
			KnowledgeManager km = kmContainer.createLocal(ci.getKnowledgeManager().getId());
			
			ValueSet initialKnowledge;
			try {
				// get all the knowledge (corresponding to an empty knowledge path)
				initialKnowledge = ci.getKnowledgeManager().get(
						Arrays.asList(RuntimeMetadataFactoryExt.eINSTANCE.createKnowledgePath()));
			} catch (KnowledgeNotFoundException e) {
				continue;
			}
			
			// copy all the values into a ChangeSet
			ChangeSet cs = new ChangeSet();
			for (KnowledgePath p: initialKnowledge.getKnowledgePaths()) {
				cs.setValue(p, initialKnowledge.getValue(p));
			}
			
			km.update(cs);
			ci.setKnowledgeManager(km);	
			ci.setOtherKnowledgeManagersAccess(new KnowledgeManagerViewImpl(km, kmContainer));
		}		
	}
	
}
