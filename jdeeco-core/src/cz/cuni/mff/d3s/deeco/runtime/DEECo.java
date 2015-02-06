package cz.cuni.mff.d3s.deeco.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Plugin;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessor;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.executor.Executor;
import cz.cuni.mff.d3s.deeco.executor.SameThreadExecutor;
import cz.cuni.mff.d3s.deeco.knowledge.CloningKnowledgeManagerFactory;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerContainer;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerFactory;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.scheduler.SingleThreadedScheduler;

/**
 * Main container for a DEECo application.
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 * @author Filip Krijt <krijt@d3s.mff.cuni.cz>
 */
public class DEECo {

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
	
	public DEECo(DEECoPlugin... plugins) throws PluginDependencyException {			
		pluginsMap= new HashMap<>();
		model = RuntimeMetadataFactoryExt.eINSTANCE.createRuntimeMetadata();
		knowledgeManagerFactory = new CloningKnowledgeManagerFactory();
		processor = new AnnotationProcessor(RuntimeMetadataFactoryExt.eINSTANCE, model, knowledgeManagerFactory);		
		
		createRuntime();
		runtime.init();
		initializePlugins(plugins);
	}
	
	public void deploy(Object... objs) throws AnnotationProcessorException {
		processor.process(objs);
	}
	
	public void start() {
		runtime.start();
	}

	public void stop() {
		runtime.stop();
	}
	
	@SuppressWarnings("unchecked")
	public <T extends DEECoPlugin> T getPluginInstance(Class<T> clazz) {
		return (T) pluginsMap.get(clazz);
	}
	
	
	class GraphNode {

		DEECoPlugin plugin; 
		List<GraphNode> children;
		int incomingEdges;
		
		GraphNode(DEECoPlugin plugin) {
			this.plugin = plugin;
			incomingEdges = 0;
		}
		GraphNode() {
			incomingEdges = 0;
		}
	}
	
	@SuppressWarnings("rawtypes")
	List<GraphNode> constructNodes(DEECoPlugin[] plugins) throws PluginDependencyException {		
		List<GraphNode> nodes = new ArrayList<>();		
		Map<Class, GraphNode> dict = new HashMap<>();
		
		for (DEECoPlugin p: plugins) {
			GraphNode node = new GraphNode(p);
			node.incomingEdges = p.getDependencies().size();
			dict.put(p.getClass(),node);
			nodes.add(node);
		}
		for (GraphNode node: dict.values()) {
			for (Class clazz: node.plugin.getDependencies()) {					
				if (dict.containsKey(clazz)) {
					dict.get(clazz).children.add(node);						
				} else {
					throw new PluginDependencyException("Missing Dependency for " + node.plugin.getClass());
				}
			}
		}	
		return nodes;
	}		

	void initializePlugins(DEECoPlugin[] plugins) throws PluginDependencyException {
		List<GraphNode> nodes = constructNodes(plugins);		

		while (!nodes.isEmpty()) {
			boolean foundZeroNode = false;

			for (int i = 0; i < nodes.size(); ++i) {
				GraphNode n = nodes.get(i);

				if (n.incomingEdges == 0) {
					n.plugin.init();
					pluginsMap.put(n.plugin.getClass(), n.plugin);

					foundZeroNode = true;

					for (GraphNode successor : n.children) {
						successor.incomingEdges--;
					}

					nodes.remove(i);
					break;
				}
			}

			if (!foundZeroNode) {
				throw new PluginDependencyException("Found a cycle.");
			}
		}
	
	}
	
	private void createRuntime() {
		Scheduler scheduler = new SingleThreadedScheduler();
		Executor executor = new SameThreadExecutor();
		KnowledgeManagerContainer kmContainer = new KnowledgeManagerContainer(knowledgeManagerFactory, model);
		scheduler.setExecutor(executor);
		executor.setExecutionListener(scheduler);
		runtime = new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer);
		
	}
	
}
