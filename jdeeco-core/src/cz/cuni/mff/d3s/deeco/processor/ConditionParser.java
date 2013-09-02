package cz.cuni.mff.d3s.deeco.processor;

import static cz.cuni.mff.d3s.deeco.processor.AnnotationHelper.getAnnotatedMethods;
import static cz.cuni.mff.d3s.deeco.processor.ParserHelper.getParameterList;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.Condition;
import cz.cuni.mff.d3s.deeco.exceptions.ComponentEnsembleParseException;
import cz.cuni.mff.d3s.deeco.path.grammar.ParseException;
import cz.cuni.mff.d3s.deeco.runtime.model.BooleanCondition;
import cz.cuni.mff.d3s.deeco.runtime.model.Parameter;

public class ConditionParser {

	public static List<BooleanCondition> parseBooleanConditions(Class<?> c) {
		assert (c != null);
		List<BooleanCondition> result = new LinkedList<>();
		List<Parameter> parameters;
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
			} catch (ComponentEnsembleParseException cepe) {
				continue;
			}
			result.add(new BooleanCondition(parameters, m));
		}
		return result;
	}
}
