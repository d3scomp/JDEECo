package cz.cuni.mff.d3s.deeco.annotations.processor;

import java.lang.reflect.Type;
import java.util.Collection;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;

/**
 * Checks whether all component's processes use only parameters from the component's knowledge
 * 
 * @author Zbyněk Jiráček
 *
 *//*
public class ComponentProcessChecker implements AnnotationChecker {

	@Override
	public void validateComponent(Object componentObj, ComponentInstance componentInstance)
			throws AnnotationCheckerException {
		if (componentObj == null) {
			throw new AnnotationCheckerException("The input component cannot be null");
		}
		if (componentInstance == null) {
			throw new AnnotationCheckerException("The input component instance cannot be null.");
		}
		
		for (ComponentProcess process : componentInstance.getComponentProcesses()) {
			for (Parameter parameter : process.getParameters()) {
				checkKnowledgePath(parameter.getGenericType(), parameter.getKnowledgePath(), componentObj.getClass());
			}
		}
	}
	
	void checkKnowledgePath(Type type, KnowledgePath knowledgePath, Class<?> componentClass)
			throws AnnotationCheckerException {
		
	}

	@Override
	public void validateEnsemble(Class<?> ensembleClass, EnsembleDefinition ensembleDefinition)
			throws AnnotationCheckerException {
		// ensembles have no processes, ergo they're always correct
	}

}
*/