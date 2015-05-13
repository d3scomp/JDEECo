package cz.cuni.mff.d3s.deeco.runtime;

import cz.cuni.mff.d3s.deeco.executor.Executor;
import cz.cuni.mff.d3s.deeco.executor.SameThreadExecutor;
import cz.cuni.mff.d3s.deeco.integrity.RatingsManager;
import cz.cuni.mff.d3s.deeco.integrity.RatingsManagerImpl;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerContainer;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerFactory;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeConfiguration.Execution;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeConfiguration.Scheduling;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.scheduler.SingleThreadedScheduler;

/**
 * A builder for assembling a {@link RuntimeFramework} based on a
 * {@link RuntimeConfiguration}.
 * 
 * <p>
 * Based on the {@link RuntimeConfiguration} the class creates instances of
 * internal jDEECo services, sets them up, and returns a handle on a
 * {@link RuntimeFramework} that controls them. 
 * The builder might change the model.
 * </p>
 * 
 * <p>
 * Intended use:
 * 
 * <pre>
 * RuntimeConfiguration conf = ...;
 * RuntimeMetadata model = ...;
 * RuntimeFrameworkBuilder = new RuntimeFrameworkBuilder(conf);
 * RuntimeFramework rf = builder.build(model);
 * </pre>
 * 
 * </p>
 * 
 * @see RuntimeFramework
 * @see RuntimeConfiguration
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 * 
 */
public class RuntimeFrameworkBuilder {
	
	/**
	 * The scheduler corresponding to the {@link #configuration}.
	 */
	protected Scheduler scheduler;
	
	/**
	 * The executor corresponding to the {@link #configuration}.
	 */
	protected Executor executor;
	
	/**
	 * The knowledge manager container corresponding to the {@link #configuration}.
	 */
	protected KnowledgeManagerContainer kmContainer;	
	
	/**
	 * The configuration for which a new runtime framework has to be created and set up.
	 */
	protected RuntimeConfiguration configuration;	
	
	/**
	 * The runtime framework instance managing all the internal services corresponding to the {@link #configuration}.
	 */
	protected RuntimeFramework runtime;

	/**
	 * The runtime metadata for the runtime.
	 */
	protected RuntimeMetadata model;
	
	protected KnowledgeManagerFactory knowledgeManagerFactory;
	
	protected RatingsManager ratingsManager;
	
	/**
	 * Creates a builder capable of creating runtime framework instances that
	 * are set up according to the {@code configuration}.
	 * 
	 * @throws IllegalArgumentException if {@code configuration} is {@code null}.
	 */
	public RuntimeFrameworkBuilder(RuntimeConfiguration configuration, KnowledgeManagerFactory knowledgeManagerFactory) {
		if (configuration == null) {
			throw new IllegalArgumentException("Configuration must not be null");
		}		
		
		this.configuration = configuration;
		this.knowledgeManagerFactory = knowledgeManagerFactory;
	}
	
	/**
	 * Creates an instance of {@link Scheduler} based on the
	 * {@link RuntimeConfiguration.Scheduling}.
	 * 
	 * @returns an instance of {@link SingleThreadedScheduler} for WALL_TIME
	 *          scheduling.
	 * @throws UnsupportedOperationException
	 *             if the configuration's {@link Scheduling} is not supported or
	 *             {@code null}.
	 */
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
	
	/**
	 * Creates an instance of {@link Executor} based on the
	 * {@link RuntimeConfiguration.Execution}.
	 * 
	 * @returns an instance of {@link SameThreadExecutor} for SINGLE_THREADED
	 *          execution.
	 * @throws UnsupportedOperationException
	 *             if the configuration's {@link Execution} is not supported or
	 *             {@code null}.
	 */
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
	
	/**
	 * Creates an instance of {@link KnowledgeManagerContainer} based on
	 * the {@link #configuration}.
	 */
	protected void buildKnowledgeManagerContainer() {		
		kmContainer = new KnowledgeManagerContainer(knowledgeManagerFactory, model);
	}
	
	/**
	 * Inter-connects the previously-built building blocks.
	 * <p>Connects scheduler and executor.</p>
	 */
	protected void connect() {
		scheduler.setExecutor(executor);
		executor.setExecutionListener(scheduler);
	}

	/**
	 * Creates a new runtime framework from the previously-built building blocks
	 * and the given model.
	 * 
	 * @see RuntimeFrameworkImpl
	 */
	protected void buildRuntime() {
		runtime = new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, ratingsManager);
	}
	
	/**
	 * Creates instance of the ratings manager
	 */
	protected void buildRatingsManager() {
		ratingsManager = new RatingsManagerImpl();	
	}
	
	/**
	 * Builds an instance of {@link RuntimeFramework} based on the corresponding
	 * {@link RuntimeConfiguration} and the given model.
	 * 
	 * @throws IllegalArgumentException
	 *             if the model is null
	 * @throws UnsupportedOperationException
	 *             if the combination of options in the corresponding
	 *             {@link RuntimeConfiguration} is not supported.
	 */
	public RuntimeFramework build(RuntimeMetadata model) {
		if (model == null) {
			throw new IllegalArgumentException("Model must not be null");
		}	
		
		this.model = model;
		
		// build all the building blocks
		buildScheduler();
		buildExecutor();
		buildKnowledgeManagerContainer();				
		buildRatingsManager();		
		
		//in the future: buildNetworkContainer() 
		//in the future: buildSynchronizer()
		
		// connect the building blocks
		connect();
		
		// create a runtime using the connected building blocks and the given model
		buildRuntime();
		return runtime;
	}

	
	
}
