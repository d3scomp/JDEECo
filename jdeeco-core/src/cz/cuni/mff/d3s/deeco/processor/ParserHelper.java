package cz.cuni.mff.d3s.deeco.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.Selector;
import cz.cuni.mff.d3s.deeco.exceptions.ComponentEnsembleParseException;
import cz.cuni.mff.d3s.deeco.invokable.ParameterizedMethod;
import cz.cuni.mff.d3s.deeco.invokable.ParameterizedSelectorMethod;
import cz.cuni.mff.d3s.deeco.invokable.parameters.GenericParameter;
import cz.cuni.mff.d3s.deeco.invokable.parameters.Parameter;
import cz.cuni.mff.d3s.deeco.invokable.parameters.SelectorParameter;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgePathHelper;
import cz.cuni.mff.d3s.deeco.path.grammar.KnowledgePath;
import cz.cuni.mff.d3s.deeco.path.grammar.ParseException;

/**
 * A helper class used for parsing both component and ensemble definitions.
 * 
 * @author Michal Kit
 * 
 */
public class ParserHelper {
	/**
	 * Returns list of method parameterTypes, which are annotated with the given
	 * annotation class.
	 * 
	 * @param method
	 *            Method object that needs to be parsed
	 * @param annotationClass
	 *            class of annotation that should be considered during the
	 *            parsing
	 * @param root
	 *            process owner id
	 * @return list of {@link Parameter} instances which fulfills search
	 *         criteria.
	 * @throws GrammarParserException
	 * @throws ComponentEnsembleParseException 
	 * 
	 * @see Parameter
	 */
	private static <T extends GenericParameter> List<T> getParameters(Class<T> parameterClass, Method method,
			Class<?> annotationClass, String root, String groupId, boolean hasGroup) throws ParseException, ComponentEnsembleParseException {
		List<T> result = new ArrayList<T>();
		Annotation[][] allAnnotations = method.getParameterAnnotations();
		Type[] parameterTypes = method.getGenericParameterTypes();
		Annotation currentAnnotation;
		T currentParameter;
		Annotation[] parameterAnnotations;

		for (int i = 0; i < parameterTypes.length; i++) {
			parameterAnnotations = allAnnotations[i];
			currentAnnotation = AnnotationHelper.getAnnotation(annotationClass,
					parameterAnnotations);
			if (currentAnnotation != null) {
				if (parameterClass.equals(Parameter.class) && groupId == null){
					currentParameter = (T) parseNamedAnnotation(currentAnnotation,
							parameterTypes[i], i, root, hasGroup);
				}else if (parameterClass.equals(Parameter.class) && groupId != null){
					currentParameter = (T) parseNamedGroupAnnotation(currentAnnotation, annotationClass,
							parameterTypes[i], i, root, groupId);
				}else if (parameterClass.equals(SelectorParameter.class)){
					currentParameter = (T) parseNamedSelectorAnnotation(method, currentAnnotation,
							parameterTypes[i], i, root);
				}else{
					throw new ComponentEnsembleParseException("The pair of groupId and parameterClass is not supported yet");
				}
				if (currentParameter != null)
					result.add(currentParameter);
			}
		}
		return result;
	}
	
	/**
	 * Apply the retrieval of parameters from the membership's explicit selectors defined the context
	 * Loop over the selector parameters to find a match in the group ids
	 * @param method
	 * @param explicitSelectorParameters
	 * @param root
	 * @return
	 * @throws ParseException
	 * @throws ComponentEnsembleParseException
	 */
	private static List<SelectorParameter> getImplicitSelectorParameters(Method method, List<SelectorParameter> explicitSelectorParameters, String root) throws ParseException, ComponentEnsembleParseException {
		List<SelectorParameter> implicitSelectorParameters = new ArrayList<SelectorParameter>();
		SelectorParameter currentParameter = null;
		// Loop over the selector parameters to find a match in the group ids
		for (int i = 0; i < explicitSelectorParameters.size(); i++) {
			String groupId = explicitSelectorParameters.get(i).groupId;
			List<Parameter> inParameters = getParameters(Parameter.class, method, In.class, root, groupId, true);
			List<Parameter> inOutParameters = getParameters(Parameter.class, method, InOut.class, root, groupId, true);
			List<Parameter> outParameters = getParameters(Parameter.class, method, Out.class, root, groupId, true);
			// if there are some parameters identified by the group id
			if (inParameters.size() + inOutParameters.size() + outParameters.size() > 0){
				// construct the final selector parameter from the parameters and groupId (sufficient)
				currentParameter = new SelectorParameter(groupId, inParameters, inOutParameters, outParameters, null, -1);
			}
			// add it to the implicit selectors list
			if (currentParameter != null)
				implicitSelectorParameters.add(currentParameter);
		}
		return implicitSelectorParameters;
	}
	
	private static Parameter parseNamedAnnotation(Annotation annotation,
			Type type, int index, String root, boolean hasGroup) throws ParseException, ComponentEnsembleParseException {

		String path = (String) AnnotationHelper.getAnnotationValue(annotation);
		
		// Adding prefix (the Component name which holds the "root") to path from annotations
		path = KnowledgePathHelper.prependToRoot(path, root);
			
		KnowledgePath kPath = new KnowledgePath(path);
		String groupId = kPath.getGroupIdentifier();
		if ((groupId == null && !hasGroup) || (groupId != null && hasGroup)){
			return new Parameter(kPath, type, index);
		}
		return null;
	}
	
	
	private static Parameter parseNamedGroupAnnotation(Annotation annotation, Class<?> annotationType,
			Type type, int index, String root, String groupId) throws ParseException, ComponentEnsembleParseException {
		// if having the right type of parameter
		if (annotation.annotationType().equals(annotationType)){
			String path = (String) AnnotationHelper.getAnnotationValue(annotation);
			
			// Adding prefix (the Component name which holds the "root") to path from annotations
			path = KnowledgePathHelper.prependToRoot(path, root);
				
			KnowledgePath kPath = new KnowledgePath(path);
			// if the group is the same as specified
			if (kPath.hasGroupId(groupId))
				return new Parameter(kPath, type, index);
		}
		return null;
	}
	
	
	private static SelectorParameter parseNamedSelectorAnnotation(Method method, Annotation annotation,
			Type type, int index, String root) throws ParseException, ComponentEnsembleParseException {
		// get the group identifier from the path
		String groupId = (String) AnnotationHelper.getAnnotationValue(annotation);
		// search for the group id in all parameters to get them by parameter type
		List<Parameter> inParameters = getParameters(Parameter.class, method, In.class, root, groupId, true);
		List<Parameter> inOutParameters = getParameters(Parameter.class, method, InOut.class, root, groupId, true);
		List<Parameter> outParameters = getParameters(Parameter.class, method, Out.class, root, groupId, true);
		// construct the final selector parameter
		return new SelectorParameter(groupId, inParameters, inOutParameters, outParameters, type, index);
	}
	
	/**
	 *  Extract an instance of ParameterizedMethod from the Java method instance.
	 *  If the method does not represent a DEECo annotated method or parsing exception occurs return null. 
	 */
	public static ParameterizedMethod extractParametrizedMethod(Method method) {
		return extractParametrizedMethod(method, null);
	}
	
	/**
	 *  Extract an instance of ParameterizedMethod from the Java method instance for the given component id (root).
	 *  If the method does not represent a DEECo annotated method or parsing exception occurs return null. 
	 */
	public static ParameterizedMethod extractParametrizedMethod(Method method, String root) {
		try {
			if (method != null) {
				List<Parameter> in = getParameters(Parameter.class, method, In.class, root, null, false);
				List<Parameter> out = getParameters(Parameter.class, method, Out.class, root, null, false);
				List<Parameter> inOut = getParameters(Parameter.class, method, InOut.class, root, null, false);
				return new ParameterizedMethod(in, inOut, out, method);
			}
		} catch (ComponentEnsembleParseException pe) {
		} catch (ParseException pe) {
		}
		return null;
	}
	
	/**
	 *  Extract an instance of ParameterizedMethod from the Java method instance.
	 *  If the method does not represent a DEECo annotated method or parsing exception occurs return null. 
	 */
	public static ParameterizedSelectorMethod extractParametrizedSelectorMethod(Method method) {
		return extractParametrizedSelectorMethod(method, null);
	}

	public static ParameterizedSelectorMethod extractParametrizedSelectorMethod(Method method, String root) {
		try {
			if (method != null) {
				List<SelectorParameter> selectors = getParameters(SelectorParameter.class, method, Selector.class, root, null, true);
				List<Parameter> in = getParameters(Parameter.class, method, In.class, root, null, false);
				List<Parameter> out = getParameters(Parameter.class, method, Out.class, root, null, false);
				List<Parameter> inOut = getParameters(Parameter.class, method, InOut.class, root, null, false);
				return new ParameterizedSelectorMethod(in, inOut, out, selectors, method);
			}
		} catch (ComponentEnsembleParseException pe) {
		} catch (ParseException pe) {
		}
		return null;
	}
	
	/**
	 *  Extract an instance of ParameterizedMethod from the Java method instance.
	 *  If the method does not represent a DEECo annotated method or parsing exception occurs return null. 
	 */
	public static ParameterizedSelectorMethod extractImplicitParametrizedSelectorMethod(Method method, List<SelectorParameter> explicitSelectorParameters) {
		return extractImplicitParametrizedSelectorMethod(method, explicitSelectorParameters, null);
	}
	
	public static ParameterizedSelectorMethod extractImplicitParametrizedSelectorMethod(Method method, List<SelectorParameter> explicitSelectorParameters, String root) {
		try {
			if (method != null) {
				List<SelectorParameter> selectors = getImplicitSelectorParameters(method, explicitSelectorParameters, root);
				List<Parameter> in = getParameters(Parameter.class, method, In.class, root, null, false);
				List<Parameter> out = getParameters(Parameter.class, method, Out.class, root, null, false);
				List<Parameter> inOut = getParameters(Parameter.class, method, InOut.class, root, null, false);
				return new ParameterizedSelectorMethod(in, inOut, out, selectors, method);
			}
		} catch (ComponentEnsembleParseException pe) {
		} catch (ParseException pe) {
		}
		return null;
	}
}
