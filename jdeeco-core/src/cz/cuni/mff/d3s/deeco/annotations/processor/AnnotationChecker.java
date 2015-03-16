package cz.cuni.mff.d3s.deeco.annotations.processor;

import java.util.Arrays;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;

/**
 * Common interface for classes that check components and ensembles. Used by {@link AnnotationProcessor}.
 * 
 * @author Zbyněk Jiráček
 *
 */
public interface AnnotationChecker {
	
	static TypeComparer typeComparer = new GenericTypeComparer();
	static KnowledgePathChecker knowledgePathChecker = new KnowledgePathCheckerImpl(typeComparer);
	
	public static AnnotationChecker[] standardCheckers = new AnnotationChecker[] 
			{
				new RolesAnnotationChecker(knowledgePathChecker, typeComparer),
				//new ComponentProcessChecker()
			};
	
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
