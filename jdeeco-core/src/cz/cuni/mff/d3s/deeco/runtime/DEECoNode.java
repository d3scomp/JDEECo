package cz.cuni.mff.d3s.deeco.runtime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessor;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.executor.Executor;
import cz.cuni.mff.d3s.deeco.executor.SameThreadExecutor;
import cz.cuni.mff.d3s.deeco.knowledge.CloningKnowledgeManagerFactory;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerContainer;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerFactory;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.runtimelog.RuntimeLogWriters;
import cz.cuni.mff.d3s.deeco.runtimelog.RuntimeLogger;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.scheduler.SingleThreadedScheduler;
import cz.cuni.mff.d3s.deeco.timer.Timer;

/**
 * Main container for a DEECo application.
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 * @author Filip Krijt <krijt@d3s.mff.cuni.cz>
 */
public class DEECoNode implements DEECoContainer {
	/** 
	 * Node identification in the network
	 */
	final int nodeId;
	
	/**
	 * The metadata model corresponding to the running application.
	 */
	RuntimeMetadata model;
	
	/**
	 * The {@link RuntimeLogger} dedicated to the instance of {@link DEECoNode} and to all that it contains.
	 */
	RuntimeLogger runtimeLogger;
	
	/**
	 * The core plugin of the architecture.
	 */
	RuntimeFramework runtime;
	
	/**
	 * To be used to process the annotated Java classes and populate the RuntimeMetadata model.  
	 */
	AnnotationProcessor processor;
	
	/**
	 * To be used to create knowledgeManager objects. 
	 */
	KnowledgeManagerFactory knowledgeManagerFactory;
	
	/**
	 * Contains the initialized plugins for this deeco object.
	 */
	Set<DEECoPlugin> pluginsSet;
	
	/**
	 * Creates new instance of {@link DEECoNode}.
	 * @param timer is the {@link Timer} that will be used in the created {@link DEECoNode} instance.
	 * @param plugins are the plugins that will be loaded into the {@link DEECoNode} instance.
	 * @throws DEECoException Thrown if the construction of {@link DEECoNode} fails. In such case
	 * please see the error output and log file for further information about the failure.
	 */
	public DEECoNode(int id, Timer timer, DEECoPlugin... plugins) throws DEECoException {
		this(id, timer, new CloningKnowledgeManagerFactory(), plugins);
	}

	public DEECoNode(int id, Timer timer, String logPath, DEECoPlugin... plugins) throws DEECoException {
		this(id, timer, new CloningKnowledgeManagerFactory(), plugins);
	}
	
	public DEECoNode(int id, Timer timer, KnowledgeManagerFactory factory, DEECoPlugin... plugins) throws DEECoException {
		this.nodeId = id;
		model = RuntimeMetadataFactoryExt.eINSTANCE.createRuntimeMetadata();
		knowledgeManagerFactory = factory;
		processor = new AnnotationProcessor(RuntimeMetadataFactoryExt.eINSTANCE, model, knowledgeManagerFactory);
				
		initializeRuntime(timer, null);
		initializePlugins(plugins);
	}

	public DEECoNode(int id, Timer timer, KnowledgeManagerFactory factory, RuntimeLogWriters runtimeLogWriters, DEECoPlugin... plugins) throws DEECoException {
		this.nodeId = id;
		model = RuntimeMetadataFactoryExt.eINSTANCE.createRuntimeMetadata();
		knowledgeManagerFactory = factory;
		processor = new AnnotationProcessor(RuntimeMetadataFactoryExt.eINSTANCE, model, knowledgeManagerFactory);
				
		initializeRuntime(timer, runtimeLogWriters);
		initializePlugins(plugins);
	}

	/**
	 * Creates new instance of {@link DEECoNode} with the specified instance of {@link RuntimeLogger}.
	 * Make sure that the {@link RuntimeLogger#init} method is called after this constructor returns.
	 * @param timer is the {@link Timer} that will be used in the created {@link DEECoNode} instance.
	 * @param runtimeLogWriters is the {@link RuntimeLogWriters} specifically provided for the instance
	 * of {@link DEECoNode} being created.
	 * @param plugins are the plugins that will be loaded into the {@link DEECoNode} instance.
	 * @throws DEECoException Thrown if the construction of {@link DEECoNode} fails. In such case
	 * please see the error output and log file for further information about the failure.
	 */
	public DEECoNode(int id, Timer timer, RuntimeLogWriters runtimeLogWriters, DEECoPlugin... plugins) throws DEECoException {
		this.nodeId = id;
		model = RuntimeMetadataFactoryExt.eINSTANCE.createRuntimeMetadata();
		knowledgeManagerFactory = new CloningKnowledgeManagerFactory();
		processor = new AnnotationProcessor(RuntimeMetadataFactoryExt.eINSTANCE, model, knowledgeManagerFactory);
		
		initializeRuntime(timer, runtimeLogWriters);		
		initializePlugins(plugins);
	}
	
	/**
	 * Internal constructor with dependency injection for testing purposes. 
	 */
	DEECoNode(int id, Timer timer, RuntimeMetadata model,
			KnowledgeManagerFactory factory, AnnotationProcessor processor,
			RuntimeLogWriters runtimeLogWriters, DEECoPlugin... plugins) throws DEECoException {
		this.nodeId = id;
		this.model = model;
		this.knowledgeManagerFactory = factory;
		this.processor = processor;

		initializeRuntime(timer, runtimeLogWriters);
		initializePlugins(plugins);
	}
	
	/**
	 * Initialize the runtime contained in the instance of {@link DEECoNode}.
	 * @param timer is the {@link Timer} that will be used in the {@link RuntimeFramework}
	 * specific to the instance of {@link DEECoNode}. 
	 * @throws DEECoException Thrown if the construction of {@link DEECoNode} fails. In such case
	 * please see the error output and log file for further information about the failure.
	 */
	private void initializeRuntime(Timer timer, RuntimeLogWriters writers) throws DEECoException {
		Executor executor = new SameThreadExecutor();
		Scheduler scheduler = new SingleThreadedScheduler(executor, timer, this);
		try {
			if(writers != null){
				runtimeLogger = new RuntimeLogger(timer, scheduler, writers);
			} else {
				runtimeLogger = new RuntimeLogger(timer, scheduler);
			}
		} catch (IOException e) {
			throw new DEECoException(e);
		}
		KnowledgeManagerContainer kmContainer = new KnowledgeManagerContainer(knowledgeManagerFactory, model);
		scheduler.setExecutor(executor);
		executor.setExecutionListener(scheduler);
		runtime = new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, runtimeLogger, null);
		runtime.init(this);
	}
	
	/**
	 * After the simulation finish the {@link RuntimeLogger} needs to be closed.
	 */
	public void finalize(){
		try {
			runtimeLogger.close();
		} catch (IOException e) {
			Log.e("Unable to close Runtime logger.");
			e.printStackTrace();
		}
	}

	/**
	 * Initialize the plugins for the {@link DEECoNode} instance.
	 * @param plugins are the plugins that will be loaded into the {@link DEECoNode} instance.
	 * @throws DEECoException Thrown if the construction of {@link DEECoNode} fails. In such case
	 * please see the error output and log file for further information about the failure.
	 */
	void initializePlugins(DEECoPlugin[] plugins) throws PluginDependencyException {
		pluginsSet = new HashSet<>();
		
		List<DependencyNode> nodes = constructDependencyNodes(plugins);
		Queue<DependencyNode> queue = new PriorityQueue<DEECoNode.DependencyNode>(new DependencyNodeComparator());
		
		for(DependencyNode n : nodes)
		{
			queue.add(n);
		}

		while (!queue.isEmpty()) {
			DependencyNode n = queue.remove();				

			if (n.dependencyCount == 0) {
				n.plugin.init(this);
				pluginsSet.add(n.plugin);				

				for (DependencyNode dependantPlugin : n.dependantPlugins) {
					queue.remove(dependantPlugin);
					dependantPlugin.dependencyCount--;
					queue.add(dependantPlugin);					
				}
				
			} else {
				throw new CycleDetectedException();
			}
		}	
	}
	
	@Override
	public int getId() {
		return nodeId;
	}
	
	public ComponentInstance deployComponent(Object component) throws AnnotationProcessorException {
		return processor.processComponent(component);
	}
	
	@SuppressWarnings("rawtypes")
	public EnsembleDefinition deployEnsemble(Class ensemble) throws AnnotationProcessorException, DuplicateEnsembleDefinitionException {
		return processor.processEnsemble(ensemble);
	}
	
	public void undeployEnsemble(String ensembleName) throws AnnotationProcessorException, DuplicateEnsembleDefinitionException {
		processor.removeEnsemble(ensembleName);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T extends DEECoPlugin> T getPluginInstance(Class<T> pluginClass) {
		for(DEECoPlugin n: pluginsSet) {
			if(pluginClass.isAssignableFrom(n.getClass())) {
				return (T) n; 
			}
		}
		
		return null;
	}
	
	@Override
	public RuntimeFramework getRuntimeFramework()
	{
		return runtime;
	}
	
	@Override
	public AnnotationProcessor getProcessor() {
		return processor;
	}	
	
	@Override
	public RuntimeMetadata getRuntimeMetadata() {
		return model;
	}

	/**
	 * Provides the {@link RuntimeLogger} of this DEECo container.
	 * @return The {@link RuntimeLogger} of this DEECo container.
	 */
	@Override
	public RuntimeLogger getRuntimeLogger()
	{
		return runtimeLogger;
	}
	
	class DependencyNode {
		DEECoPlugin plugin; 
		List<DependencyNode> dependantPlugins = new ArrayList<>();
		int dependencyCount;
		
		DependencyNode(DEECoPlugin plugin) {
			this.plugin = plugin;
			this.dependencyCount = plugin.getDependencies().size();
		}
		
		DependencyNode() {
			dependencyCount = 0;
		}
	}
	
	class DependencyNodeComparator implements Comparator<DependencyNode>
	{
		@Override
		public int compare(DependencyNode o1, DependencyNode o2) {			
			return Integer.compare(o1.dependencyCount, o2.dependencyCount);
		}		
	}
	
	List<DependencyNode> constructDependencyNodes(DEECoPlugin[] plugins) throws PluginDependencyException {		
		List<DependencyNode> dependencyNodes = new ArrayList<>();		
		Set<DependencyNode> knownPlugins = new HashSet<>();
		
		for (DEECoPlugin p: plugins) {
			DependencyNode node = new DependencyNode(p);
			
			knownPlugins.add(node);
			dependencyNodes.add(node);
		}
		
		for (DependencyNode node: dependencyNodes) {
			for (Class<? extends DEECoPlugin> pluginClass : node.plugin.getDependencies()) {
				DependencyNode found = null;
				for(DependencyNode n: knownPlugins) {
					if(pluginClass.isAssignableFrom(n.plugin.getClass())) {
						found = n;
					}
				}
				if (found != null) {
					found.dependantPlugins.add(node);
				} else {
					throw new MissingDependencyException(node.plugin.getClass(), pluginClass);
				}
			}
		}
		
		return dependencyNodes;
	}		
}
