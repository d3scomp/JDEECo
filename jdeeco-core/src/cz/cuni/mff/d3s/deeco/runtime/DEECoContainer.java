package cz.cuni.mff.d3s.deeco.runtime;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessor;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;

/**
 * Specifies the entry points of the main DEECo container available to DEECo plugins. 
 * 
 * @author Filip Krijt <krijt@d3s.mff.cuni.cz>
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 */
public interface DEECoContainer {
	
	/**
	 * Returns the instance of the initialized plugin of type <code>pluginClass</code>.
	 * To be called from the <code>init()</code> method of a plugin that depends on a plugin of type <code>pluginClass</code>.
	 */
	public <T extends DEECoPlugin> T getPluginInstance(Class<T> pluginClass) ;
	
	/**
	 * Returns the "core" plugin object (always present) of this DEECo container. 
	 * Shorthand for <code>getPluginInstance(RuntimeFramework.class)</code>
	 */
	public RuntimeFramework getRuntimeFramework();
	
	/**
	 * Returns the annotations processor of this DEECo container.  
	 * To be used by plugins that need to extend the annotation processor so that it can process new, plugin-related annotations.  
	 */
	public AnnotationProcessor getProcessor();
	
	/**
	 * Returns the runtime metadata model (EMF model) of this DEECo container.
	 * To be used by plugins to connect the main EMF model with its plugin-related extensions.
	 */
	public RuntimeMetadata getRuntimeMetadata();
	
	/**
	 * Deploys components to the DEECo runtime by parsing them and adding them to the metadata model.
	 * As soon as they are added to the model, components are dynamically deployed (relevant tasks are created and scheduled).
	 * To be used by plugins to deploy "system components" that specify system processes to be scheduled along with application processes.
	 * 
	 * @param components initialized objects of component-annotated classes
	 * @throws AnnotationProcessorException 
	 */
	public void deployComponents(Object... components) throws AnnotationProcessorException;
	
	/**
	 * Deploys ensembles to the DEECo rutime by parsing them and adding them to the metadata model.
	 * As soon as they are added to the model, ensembles are dynamically deployed (relevant tasks are created and scheduled).
	 * To be used by plugins to deploy "system ensembles" that specify knowledge exchange between "system components" and are scheduled along with application ensembles.
	 * 
	 * @param ensembles ensemble-annotated classes
	 * @throws AnnotationProcessorException
	 */
	@SuppressWarnings("rawtypes")
	public void deployEnsembles(Class... ensembles) throws AnnotationProcessorException;
	
}
