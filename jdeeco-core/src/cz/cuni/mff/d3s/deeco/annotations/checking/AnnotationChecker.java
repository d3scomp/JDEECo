package cz.cuni.mff.d3s.deeco.annotations.checking;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessor;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;

/**
 * Common interface for classes that check components and ensembles. Used by {@link AnnotationProcessor}.
 * 
 * @author Zbyněk Jiráček
 *
 * @see AnnotationProcessor
 */
public interface AnnotationChecker {
	
	static TypeComparer typeComparer = new GenericTypeComparer();
	static KnowledgePathChecker knowledgePathChecker = new KnowledgePathCheckerImpl(typeComparer);
	
	/**
	 * List of Annotation Checkers that are by-default used in {@link AnnotationProcessor}.
	 * 
	 * @see RolesAnnotationChecker
	 * @see ComponentProcessChecker
	 */
	public static AnnotationChecker[] standardCheckers = new AnnotationChecker[] 
			{
				new RolesAnnotationChecker(knowledgePathChecker, typeComparer),
				new ComponentProcessChecker(knowledgePathChecker),
			};
	
	/**
	 * Checks that a given component instance is valid according to the rules of the implementing class.
	 * If the check finds any violation, {@link AnnotationCheckerException} is thrown. On success, the function
	 * just normally returns.
	 * @param componentObj The component instance object
	 * @param componentInstance The processed component instance
	 * @throws AnnotationCheckerException
	 */
	public void validateComponent(Object componentObj, ComponentInstance componentInstance) throws AnnotationCheckerException;
	
	/**
	 * Checks that a given ensemble is valid according to the rules of the implementing class.
	 * If the check finds any violation, {@link AnnotationCheckerException} is thrown. On success, the function
	 * just normally returns.
	 * @param ensembleClass The ensemble class
	 * @param ensembleDefinition The processed ensemble definition
	 * @throws AnnotationCheckerException
	 */
	public void validateEnsemble(Class<?> ensembleClass, EnsembleDefinition ensembleDefinition) throws AnnotationCheckerException;
}
