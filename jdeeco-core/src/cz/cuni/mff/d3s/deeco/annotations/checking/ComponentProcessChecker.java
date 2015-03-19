package cz.cuni.mff.d3s.deeco.annotations.checking;

import java.lang.reflect.Type;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.checking.ParameterKnowledgePathExtractor.KnowledgePathAndType;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNode;

/**
 * Checks whether all component's processes use only parameters from the component's knowledge
 * 
 * @author Zbyněk Jiráček
 *
 */
public class ComponentProcessChecker implements AnnotationChecker {

	private ParameterKnowledgePathExtractor parameterExtractor;
	private KnowledgePathChecker knowledgePathChecker;
	
	public ComponentProcessChecker(KnowledgePathChecker kpChecker) {
		this(kpChecker, new ParameterKnowledgePathExtractor());
	}
	
	ComponentProcessChecker(KnowledgePathChecker kpChecker, ParameterKnowledgePathExtractor parameterExtractor) {
		this.knowledgePathChecker = kpChecker;
		this.parameterExtractor = parameterExtractor;
	}
	
	@Override
	public void validateComponent(Object componentObj, ComponentInstance componentInstance)
			throws AnnotationCheckerException {
		if (componentObj == null) {
			throw new AnnotationCheckerException("The input component cannot be null.");
		}
		if (componentInstance == null) {
			throw new AnnotationCheckerException("The input component instance cannot be null.");
		}
		
		for (ComponentProcess process : componentInstance.getComponentProcesses()) {
			int i = 0;
			for (Parameter parameter : process.getParameters()) {
				i++;
				try {
					checkParameter(parameter, componentObj.getClass());
				} catch (ParameterException e) {
					throw new AnnotationCheckerException("Process " + process.getName() + ": Parameter " + i + ": " + e.getMessage(), e);
				}
			}
		}
	}
	
	void checkParameter(Parameter parameter, Class<?> componentClass) throws ParameterException {
		
		List<KnowledgePathAndType> knowledgePaths = parameterExtractor.extractAllKnowledgePaths(parameter);
		
		for (KnowledgePathAndType knowledgePathAndType : knowledgePaths) {
			try {
				checkKnowledgePath(knowledgePathAndType.type, knowledgePathAndType.knowledgePath, componentClass);
			} catch (KnowledgePathCheckException ex) {
				String knowledgePathStr = KnowledgePathCheckerImpl.pathNodeSequenceToString(
						knowledgePathAndType.type, knowledgePathAndType.knowledgePath);
				throw new ParameterException("Knowledge path " + knowledgePathStr + ": " 
						+ ex.getMessage(), ex);
			}
		}
	}
	
	private void checkKnowledgePath(Type type, List<PathNode> pathNodes, Class<?> componentClass)
			throws KnowledgePathCheckException {
		if (!knowledgePathChecker.isFieldInClass(type, pathNodes, componentClass)) {
				throw new KnowledgePathCheckException("The knowledge path is not valid for the component: "
						+ componentClass.getSimpleName() + ". "
						+ "Check whether the field (or sequence of fields) exists in the component and that it has correct type(s) and is public, nonstatic and non@Local");
		}
	}

	@Override
	public void validateEnsemble(Class<?> ensembleClass, EnsembleDefinition ensembleDefinition)
			throws AnnotationCheckerException {
		// ensembles have no processes, ergo they're always correct
	}

}
