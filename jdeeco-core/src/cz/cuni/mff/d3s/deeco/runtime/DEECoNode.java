package cz.cuni.mff.d3s.deeco.runtime;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessor;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.executor.Executor;
import cz.cuni.mff.d3s.deeco.executor.SameThreadExecutor;
import cz.cuni.mff.d3s.deeco.knowledge.CloningKnowledgeManagerFactory;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerContainer;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerFactory;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.scheduler.NoExecutorAvailableException;
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
	 * TODO find a way to inject this field from the DEECoRealm (probably via the constructor here?)
	 */
	int id;
	/**
	 * The metadata model corresponding to the running application.
	 */
	RuntimeMetadata model;
	
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
	Map<Class<? extends DEECoPlugin>, DEECoPlugin> pluginsMap;
	
	public DEECoNode(Timer Timer, DEECoPlugin... plugins) throws DEECoException {			
		pluginsMap= new HashMap<>();
		model = RuntimeMetadataFactoryExt.eINSTANCE.createRuntimeMetadata();
		knowledgeManagerFactory = new CloningKnowledgeManagerFactory();
		processor = new AnnotationProcessor(RuntimeMetadataFactoryExt.eINSTANCE, model, knowledgeManagerFactory);		
		
		createRuntime(Timer);
		runtime.init(this);
		initializePlugins(plugins);
	}
	
	@Override
	public int getId() {
		return id;
	}
	
	public ComponentInstance deployComponent(Object component) throws AnnotationProcessorException {
		return processor.processComponent(component);
	}
	
	@SuppressWarnings("rawtypes")
	public EnsembleDefinition deployEnsemble(Class ensemble) throws AnnotationProcessorException, DuplicateEnsembleDefinitionException {
		return processor.processEnsemble(ensemble);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T extends DEECoPlugin> T getPluginInstance(Class<T> pluginClass) {
		return (T) pluginsMap.get(pluginClass);
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
	
	@SuppressWarnings("rawtypes")
	List<DependencyNode> constructDependencyNodes(DEECoPlugin[] plugins) throws PluginDependencyException {		
		List<DependencyNode> dependencyNodes = new ArrayList<>();		
		Map<Class, DependencyNode> knownPlugins = new HashMap<>();
		
		for (DEECoPlugin p: plugins) {
			DependencyNode node = new DependencyNode(p);
			
			knownPlugins.put(p.getClass(),node);
			dependencyNodes.add(node);
		}
		
		for (DependencyNode node: dependencyNodes) {
			for (Class<? extends DEECoPlugin> pluginClass : node.plugin.getDependencies()) {					
				if (knownPlugins.containsKey(pluginClass)) {
					knownPlugins.get(pluginClass).dependantPlugins.add(node);						
				} else {
					throw new MissingDependencyException(node.plugin.getClass(), pluginClass);
				}
			}
		}	
		
		return dependencyNodes;
	}		

	void initializePlugins(DEECoPlugin[] plugins) throws PluginDependencyException {
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
				pluginsMap.put(n.plugin.getClass(), n.plugin);				

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
	
	private void createRuntime(Timer timer) throws NoExecutorAvailableException {
		Executor executor = new SameThreadExecutor();
		Scheduler scheduler = new SingleThreadedScheduler(executor, timer);
		KnowledgeManagerContainer kmContainer = new KnowledgeManagerContainer(knowledgeManagerFactory, model);
		scheduler.setExecutor(executor);
		executor.setExecutionListener(scheduler);
		runtime = new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer);		
	}

}
