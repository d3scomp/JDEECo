package cz.cuni.mff.d3s.deeco.annotations.processor;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;

/**
 * Common interface for classes that check components and ensembles. Used by {@link AnnotationProcessor}.
 * 
 * @author Zbyněk Jiráček
 *
 */
public interface AnnotationChecker {
	
	/**
	 * Checks that a given component instance is valid.
	 * @param componentObj The component instance object
	 * @param componentInstance The processed component instance
	 * @throws AnnotationCheckerException
	 */
	public void validateComponent(Object componentObj, ComponentInstance componentInstance) throws AnnotationCheckerException;
	
	/**
	 * Checks that a given ensemble is valid.
	 * @param ensembleClass The ensemble class
	 * @param ensembleDefinition The processed ensemble definition
	 * @throws AnnotationCheckerException
	 */
	public void validateEnsemble(Class<?> ensembleClass, EnsembleDefinition ensembleDefinition) throws AnnotationCheckerException;
}
