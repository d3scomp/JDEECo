package cz.cuni.mff.d3s.deeco.task;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessor;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.scheduler.CurrentTimeProvider;

/**
 * A class providing reflective capabilities to a component process. 
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 *
 */
public class ProcessContext {
	private static ThreadLocal<ProcessContext> context = new ThreadLocal<>();
	
	private ComponentProcess process;
	private CurrentTimeProvider currentTimeProvider;
	
	private ProcessContext(ComponentProcess process, CurrentTimeProvider currentTimeProvider) {
		this.process = process;		
		this.currentTimeProvider = currentTimeProvider;
	}
	
	static void addContext(ComponentProcess process, CurrentTimeProvider currentTimeProvider) {
		context.set(new ProcessContext(process, currentTimeProvider));		
	}	
	
	public static ComponentProcess getCurrentProcess() {
		if (context.get() != null)
			return context.get().process;
		else 
			return null;
	}	
	
	
	private static RuntimeMetadata getModel() {
		ComponentProcess p = getCurrentProcess();
		if (p == null)
			return null;
		
		return (RuntimeMetadata) p.getComponentInstance().eContainer();		
	}
	
	
	public static String startComponent(Object componentDefinition) throws Exception {
		// TODO: figure somehow how to get the factory used to create the model
		AnnotationProcessor processor = new AnnotationProcessor(RuntimeMetadataFactoryExt.eINSTANCE);
		try {
			ComponentInstance ci = processor.processComponentInstance(getModel(), componentDefinition);
			
			// TODO: check that the KM created by the processor has been already replaced by the runtime framework
			if (ci != null)
				return ci.getKnowledgeManager().getId();
			else
				return null;
		} catch (AnnotationProcessorException e) {
			Log.e("Component start failed",  e);
			throw new Exception("Component start failed",  e);
		}
	}
}
