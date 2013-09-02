package cz.cuni.mff.d3s.deeco.processor;

import static cz.cuni.mff.d3s.deeco.processor.AnnotationHelper.getAnnotatedMethod;
import static cz.cuni.mff.d3s.deeco.processor.ConditionParser.parseBooleanConditions;
import static cz.cuni.mff.d3s.deeco.processor.ParserHelper.getParameterList;
import static cz.cuni.mff.d3s.deeco.processor.ScheduleHelper.getPeriodicSchedule;
import static cz.cuni.mff.d3s.deeco.processor.ScheduleHelper.getTriggeredSchedule;

import java.lang.reflect.Method;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.definitions.EnsembleDefinition;
import cz.cuni.mff.d3s.deeco.exceptions.ComponentEnsembleParseException;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.path.grammar.ParseException;
import cz.cuni.mff.d3s.deeco.runtime.model.BooleanCondition;
import cz.cuni.mff.d3s.deeco.runtime.model.Ensemble;
import cz.cuni.mff.d3s.deeco.runtime.model.Exchange;
import cz.cuni.mff.d3s.deeco.runtime.model.Parameter;
import cz.cuni.mff.d3s.deeco.runtime.model.PeriodicSchedule;
import cz.cuni.mff.d3s.deeco.runtime.model.Schedule;

/**
 * Parser class for ensemble definitions.
 * 
 * @author Michal Kit
 * 
 */
public class EnsembleParser {

	/**
	 * Static function used to extract {@link SchedulableEnsembleProcess}
	 * instance from the class definition
	 * 
	 * @param c
	 *            class to be parsed for extraction
	 * @param km
	 *            {@link KnowledgeManager} instance that is used for knowledge
	 *            repository communication
	 * @return a {@link SchedulableEnsembleProcess} instance extracted from the
	 *         class definition
	 */
	public static Ensemble extractEnsembleProcess(Class<?> c)
			throws ParseException {
		assert (c != null);
		if (!isEnsembleDefinition(c)) {
			throw new ParseException("The class " + c.getName()
					+ " is not an ensemble definition.");
		}
		List<BooleanCondition> conditions = parseBooleanConditions(c);
		if (conditions.size() != 1) {
			throw new ParseException("The class " + c.getName()
					+ " should contain exactly one membership condition.");
		}
		BooleanCondition mc = conditions.get(0);
		List<Parameter> parameters;
		Method m = getAnnotatedMethod(c, KnowledgeExchange.class);
		if (m == null) {
			throw new ParseException(
					"The ensemble definition does not define a knowledge exchange function");
		}
		try {
			parameters = getParameterList(m);
		} catch (ComponentEnsembleParseException cepe) {
			throw new ParseException(c.getName()
					+ ": Parameters for the method " + m.getName()
					+ " cannot be parsed.");
		}
		Exchange ke = new Exchange(parameters, m);
		Schedule schedule = getPeriodicSchedule(AnnotationHelper
				.getAnnotation(PeriodicScheduling.class, m.getAnnotations()));
		if (schedule == null) {
			schedule = getTriggeredSchedule(
					m.getParameterAnnotations(), parameters);

			if (schedule == null) {
				schedule = new PeriodicSchedule();
			}
		}
		return new Ensemble(c.getName(), mc, ke, schedule);
	}

	/**
	 * Checkes whether the given class is an ensemble definitions.
	 * 
	 * @param clazz
	 *            class to be checked
	 * @return True if the given class is an ensemble definition. False
	 *         otherwise.
	 */
	public static boolean isEnsembleDefinition(Class<?> clazz) {
		return clazz != null
				&& EnsembleDefinition.class.isAssignableFrom(clazz);
	}

}
