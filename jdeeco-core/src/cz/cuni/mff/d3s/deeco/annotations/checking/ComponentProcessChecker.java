package cz.cuni.mff.d3s.deeco.annotations.checking;

import java.lang.reflect.Type;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.checking.ParameterKnowledgePathExtractor.KnowledgePathAndType;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNode;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMapKey;

/**
 * Checks whether all component's processes use only parameters from the component's knowledge
 * (eg. that the parameters correspond to fields in the component class).
 * 
 * Example: A class has a process with following parameters:
 * 	@@In("x") Integer x,
 *  @@Out("y") ParamHolder<<String>> y.
 * 
 * The class extracts all processes from the given {@link ComponentInstance}, then for each process, each
 * parameter's knowledge path is tested.
 * The class uses {@link ParameterKnowledgePathExtractor} class to extract all referenced knowledge paths
 * from the parameter's definitions. Then, a {@link KnowledgePathChecker} is used to verify the existence
 * of the respective field of the correct type in the component class.
 *  
 * A component is valid only if it contains field x of type Integer, and field y of type String. If one
 * of the fields is missing, or has a different type, the component is declared to be invalid. 
 * 
 * @author Zbyněk Jiráček
 *
 * @see AnnotationChecker
 * @see ParameterKnowledgePathExtractor
 * @see KnowledgePathChecker
 */
public class ComponentProcessChecker implements AnnotationChecker {

	private ParameterKnowledgePathExtractor parameterExtractor;
	private KnowledgePathChecker knowledgePathChecker;
	
	/**
	 * Creates a new instance of the {@link ComponentProcessChecker} class.
	 * @param kpChecker A {@link KnowledgePathChecker} that should be used to verify knowledge path's existence in a component class.
	 */
	public ComponentProcessChecker(KnowledgePathChecker kpChecker) {
		this(kpChecker, new ParameterKnowledgePathExtractor());
	}
	
	/**
	 * Internal constructor for testing. Allows replacing the default {@link ParameterKnowledgePathExtractor} with a mock
	 * @param kpChecker A {@link KnowledgePathChecker} that should be used to verify knowledge path's existence in a component class.
	 * @param parameterExtractor A {@link ParameterKnowledgePathExtractor} instance used to acquire all knowledge paths from process parameters.
	 */
	ComponentProcessChecker(KnowledgePathChecker kpChecker, ParameterKnowledgePathExtractor parameterExtractor) {
		this.knowledgePathChecker = kpChecker;
		this.parameterExtractor = parameterExtractor;
	}
	
	/*
	 * (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.annotations.checking.AnnotationChecker#validateComponent(java.lang.Object, cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance)
	 */
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
	
	/**
	 * Checks whether a knowledge path of the given parameter of a component process exists in the component class.
	 * If the knowledge path does not exist in the component class (or types do not match),
	 * {@link ParameterException} is thrown.
	 * @param parameter The parameter of a component process.
	 * @param componentClass The component class.
	 * @throws ParameterException
	 */
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
	
	/**
	 * Checks whether a knowledge path that consists only of field names (no {@link PathNodeMapKey}s allowed)
	 * corresponds to a field in the component class. If not, the method throws {@link KnowledgePathException}.
	 * 
	 * The method uses {@link KnowledgePathChecker} class to validation, see it for more details.
	 * 
	 * @param type The desired type of the knowledge path expression (can be null if unknown).
	 * @param pathNodes Sequence of field names.
	 * @param componentClass The component class.
	 * @throws KnowledgePathCheckException
	 */
	private void checkKnowledgePath(Type type, List<PathNode> pathNodes, Class<?> componentClass)
			throws KnowledgePathCheckException {
		if (!knowledgePathChecker.isFieldInClass(type, pathNodes, componentClass)) {
				throw new KnowledgePathCheckException("The knowledge path is not valid for the component: "
						+ componentClass.getSimpleName() + ". "
						+ "Check whether the field (or sequence of fields) exists in the component and that it has correct type(s) and is public, nonstatic and non@Local");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.annotations.checking.AnnotationChecker#validateEnsemble(java.lang.Class, cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition)
	 */
	@Override
	public void validateEnsemble(Class<?> ensembleClass, EnsembleDefinition ensembleDefinition)
			throws AnnotationCheckerException {
		// ensembles have no processes, ergo they're always correct
	}

}
