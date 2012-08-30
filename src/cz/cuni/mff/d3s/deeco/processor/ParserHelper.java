package cz.cuni.mff.d3s.deeco.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.exceptions.ComponentEnsembleParseException;
import cz.cuni.mff.d3s.deeco.invokable.AnnotationHelper;
import cz.cuni.mff.d3s.deeco.invokable.Parameter;
import cz.cuni.mff.d3s.deeco.knowledge.KPBuilder;
import cz.cuni.mff.d3s.deeco.path.grammar.KnowledgePath;
import cz.cuni.mff.d3s.deeco.path.grammar.ParseException;

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
	public static List<Parameter> getParameters(Method method,
			Class<?> annotationClass, String root) throws ParseException, ComponentEnsembleParseException {
		List<Parameter> result = new ArrayList<Parameter>();
		Annotation[][] allAnnotations = method.getParameterAnnotations();
		Type[] parameterTypes = method.getGenericParameterTypes();
		Annotation currentAnnotation;
		Parameter currentParameter;
		Annotation[] parameterAnnotations;

		for (int i = 0; i < parameterTypes.length; i++) {
			parameterAnnotations = allAnnotations[i];
			currentAnnotation = AnnotationHelper.getAnnotation(annotationClass,
					parameterAnnotations);
			if (currentAnnotation != null) {
				currentParameter = parseNamedAnnotation(currentAnnotation,
						parameterTypes[i], i, root);
				if (currentParameter != null)
					result.add(currentParameter);
			}
		}
		return result;
	}
	
	private static Parameter parseNamedAnnotation(Annotation annotation,
			Type type, int index, String root) throws ParseException, ComponentEnsembleParseException {

		String path = (String) AnnotationHelper.getAnnotationValue(annotation);
		
		// Adding prefix (the Component name which holds the "root") to path from annotations
		path = KPBuilder.prependToRoot(path, root);

		KnowledgePath kPath = new KnowledgePath(path);
		
		return new Parameter(kPath, type, index);
	}
	
}
