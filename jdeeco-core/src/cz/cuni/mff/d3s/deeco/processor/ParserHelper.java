package cz.cuni.mff.d3s.deeco.processor;

import static cz.cuni.mff.d3s.deeco.processor.AnnotationHelper.getAnnotation;
import static cz.cuni.mff.d3s.deeco.processor.AnnotationHelper.getAnnotationValue;
import static cz.cuni.mff.d3s.deeco.processor.ParameterKnowledgeTypeParser.extractKnowledgeType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.exceptions.ComponentEnsembleParseException;
import cz.cuni.mff.d3s.deeco.path.grammar.ParseException;
import cz.cuni.mff.d3s.deeco.path.grammar.PathParser;
import cz.cuni.mff.d3s.deeco.runtime.model.KnowledgePath;
import cz.cuni.mff.d3s.deeco.runtime.model.Parameter;
import cz.cuni.mff.d3s.deeco.runtime.model.ParameterDirection;

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
	public static List<Parameter> getParameterList(Method method) throws ParseException,
			ComponentEnsembleParseException {

		List<Parameter> result = new LinkedList<Parameter>();		
		Annotation[][] allAnnotations = method.getParameterAnnotations();
		Type[] parameterTypes = method.getGenericParameterTypes();
		KnowledgePath knowledgePath;
		Annotation annotation;
		ParameterDirection pDirection;
		Annotation[] parameterAnnotations;

		for (int i = 0; i < parameterTypes.length; i++) {
			parameterAnnotations = allAnnotations[i];
			annotation = getAnnotation(In.class, parameterAnnotations);
			if (annotation == null) {
				annotation = getAnnotation(Out.class, parameterAnnotations);
				if (annotation == null) {
					annotation = getAnnotation(InOut.class, parameterAnnotations);
					if (annotation == null)
						return null;
					else
						pDirection = ParameterDirection.INOUT;
				} else {
					pDirection = ParameterDirection.OUT;
				}
			} else {
				pDirection = ParameterDirection.IN;
			}
			knowledgePath = PathParser.parse((String) getAnnotationValue(annotation));
			result.add(new Parameter(pDirection, knowledgePath, extractKnowledgeType(parameterTypes[i])));
		}
		return result;
	}
}
