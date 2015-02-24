package cz.cuni.mff.d3s.deeco.annotations.processor;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;

public interface AnnotationChecker {
	
	public void validateComponent(Object componentObj, ComponentInstance componentInstance) throws AnnotationCheckerException;
	
	public void validateEnsemble(Class<?> ensembleClass, EnsembleDefinition ensembleDefinition) throws AnnotationCheckerException;
}
