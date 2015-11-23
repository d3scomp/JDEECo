package cz.cuni.mff.d3s.deeco.task;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessor;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerFactory;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.architecture.api.Architecture;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.runtimelog.RuntimeLogger;
import cz.cuni.mff.d3s.deeco.timer.CurrentTimeProvider;

/**
 * A class providing reflective capabilities to a component process. 
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 *
 */
public class ProcessContext {
	private static ThreadLocal<ProcessContext> context = new ThreadLocal<>();
	
	private Architecture architecture;
	private ComponentProcess process;
	private CurrentTimeProvider currentTimeProvider;
	private RuntimeLogger runtimeLogger;
	
	private ProcessContext(ComponentProcess process, CurrentTimeProvider currentTimeProvider, Architecture architecture, RuntimeLogger runtimeLogger) {
		this.architecture = architecture;
		this.process = process;		
		this.currentTimeProvider = currentTimeProvider;
		this.runtimeLogger = runtimeLogger;
	}
	
	static void addContext(ComponentProcess process, CurrentTimeProvider currentTimeProvider, Architecture architecture, RuntimeLogger runtimeLogger) {
		context.set(new ProcessContext(process, currentTimeProvider, architecture, runtimeLogger));		
	}	
	
	public static ComponentProcess getCurrentProcess() {
		if (context.get() != null)
			return context.get().process;
		else 
			return null;
	}	
	
	public static Architecture getArchitecture() {
		if (context.get() != null)
			return context.get().architecture;
		else 
			return null;
	}
	
	public static CurrentTimeProvider getTimeProvider() {
		if (context.get() != null)
			return context.get().currentTimeProvider;
		else 
			return null;
	}
	
	private static RuntimeMetadata getModel() {
		ComponentProcess p = getCurrentProcess();
		if (p == null)
			return null;
		
		return (RuntimeMetadata) p.getComponentInstance().eContainer();		
	}
	
	public static RuntimeLogger getRuntimeLogger() {
		if (context.get() != null)
			return context.get().runtimeLogger;
		else 
			return null;
	}
	
	public void startComponent(Object componentDefinition, KnowledgeManagerFactory knowledgeManagerFactory) throws Exception {
		// TODO: figure somehow how to get the factory used to create the model
		RuntimeMetadata model = getModel();
		AnnotationProcessor processor = new AnnotationProcessor(RuntimeMetadataFactoryExt.eINSTANCE,model,knowledgeManagerFactory);
		try {
			processor.processComponent(componentDefinition);
			
			// TODO: check that the KM created by the processor has been already replaced by the runtime framework
		} catch (AnnotationProcessorException e) {
			Log.e("Component start failed",  e);
			throw new Exception("Component start failed",  e);
		}
	}
}
