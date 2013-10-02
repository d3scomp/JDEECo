package cz.cuni.mff.d3s.deeco.processor;

import static cz.cuni.mff.d3s.deeco.processor.AnnotationHelper.getAnnotatedMethods;
import static cz.cuni.mff.d3s.deeco.processor.ParserHelper.getParameterList;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.Condition;
import cz.cuni.mff.d3s.deeco.exceptions.ParametersParseException;
import cz.cuni.mff.d3s.deeco.model.BooleanCondition;
import cz.cuni.mff.d3s.deeco.model.Parameter;
import cz.cuni.mff.d3s.deeco.path.grammar.ParseException;

public class ConditionParser {

	public static List<BooleanCondition> parseBooleanConditions(Class<?> c) {
		assert (c != null);
		List<BooleanCondition> result = new LinkedList<>();
		List<Parameter> parameters;
		String id;
		for (Method m : getAnnotatedMethods(c, Condition.class)) {
			if (m == null) {
				continue;
			}
			if (!m.getReturnType().isAssignableFrom(boolean.class)) {
				continue;
			}
			try {
				parameters = getParameterList(m);
			} catch (ParseException pe) {
				continue;
			} catch (ParametersParseException cepe) {
				continue;
			}
			id = m.getAnnotation(Condition.class).value();
			result.add(new BooleanCondition(id, parameters, m));
		}
		return result;
	}
}
