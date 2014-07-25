package cz.cuni.mff.d3s.deeco.annotations.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;

/**
 * <p>
 * Provides the extension points offered by the main jDEECo annotation processor
 * ({@link AnnotationProcessor}). These extension points can be implemented by
 * other processor(s) in order to augment the main processor's functionality
 * (parse additional annotations, create additional model elements, etc.).
 * </p>
 * <p>
 * The additional processor(s) are passed to the main processor via an
 * extension-aware constructor (at the main processor).
 * </p>
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 * @see AnnotationProcessor#callExtensions
 */
public abstract class AnnotationProcessorExtensionPoint {

	public void onComponentInstanceCreation(ComponentInstance componentInstance, List<Annotation> unknownAnnotations) {
		
	}

	public void onComponentProcessCreation(ComponentProcess componentProcess, List<Annotation> unknownAnnotations) {
		
	}
	
	public void onEnsembleDefinitionCreation(EnsembleDefinition ensembleDefinition, List<Annotation> unknownAnnotations) {
		
	}
	
	public void onUnknownMethodAnnotation(AnnotationProcessor caller, boolean inComponent, Method m, List<Annotation> unknownAnnotations) throws AnnotationProcessorException {
		
	}

	public void onComponentInstanceCreation(ComponentInstance componentInstance, Annotation unknownAnnotation) {
		
	}

	public void onComponentProcessCreation(ComponentProcess componentProcess, Annotation unknownAnnotation) {
		
	}

	public void onEnsembleDefinitionCreation(EnsembleDefinition ensembleDefinition, Annotation unknownAnnotation) {
		
	}
	
	public void onUnknownMethodAnnotation(AnnotationProcessor caller, boolean inComponent, Method m, Annotation unknownAnnotation) throws AnnotationProcessorException {
		
	}
	
}
