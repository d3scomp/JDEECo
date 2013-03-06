package cz.cuni.mff.d3s.deeco.processor;

import java.lang.reflect.Method;

import cz.cuni.mff.d3s.deeco.annotations.DEECoEnsemble;
import cz.cuni.mff.d3s.deeco.annotations.DEECoEnsembleMapper;
import cz.cuni.mff.d3s.deeco.annotations.DEECoEnsembleMembership;
import cz.cuni.mff.d3s.deeco.annotations.DEECoPeriodicScheduling;
import cz.cuni.mff.d3s.deeco.ensemble.Ensemble;
import cz.cuni.mff.d3s.deeco.invokable.BooleanMembership;
import cz.cuni.mff.d3s.deeco.invokable.Membership;
import cz.cuni.mff.d3s.deeco.invokable.ParameterizedMethod;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcess;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.path.grammar.ParseException;
import cz.cuni.mff.d3s.deeco.scheduling.ProcessPeriodicSchedule;
import cz.cuni.mff.d3s.deeco.scheduling.ProcessSchedule;

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
	 * @return a {@link SchedulableEnsembleProcess} instance extracted
	 *         from the class definition
	 */
	public static SchedulableEnsembleProcess extractEnsembleProcess(
			Class<?> c) throws ParseException {
		// TODO: put names into the exception strings
		
		if (!isEnsembleDefinition(c)) {
			throw new ParseException("The class " + c.getName() + " is not an ensemble definition.");
		}
		
		assert(c != null);
		
		final Method methodEnsMembership = AnnotationHelper.getAnnotatedMethod(
				c, DEECoEnsembleMembership.class);
		
		if (methodEnsMembership == null) {
			throw new ParseException("The ensemble definition does not define a membership function");
		}

		final ParameterizedMethod pm = ParserHelper.extractParametrizedMethod(methodEnsMembership);
		
		if (pm == null) {
			throw new ParseException("Malformed membership function definition.");
		}

		// Look up Membership
		if (!methodEnsMembership.getReturnType().isAssignableFrom(boolean.class)) {
			throw new ParseException("Membership function needs to return boolean");
		}
				
		Membership membership = new BooleanMembership(pm);
		

		// Look up scheduling
		ProcessSchedule scheduling = null;

		final ProcessSchedule periodicSchedule = ScheduleHelper
				.getPeriodicSchedule(AnnotationHelper.getAnnotation(
						DEECoPeriodicScheduling.class, c.getAnnotations()));
		if (periodicSchedule != null) {
			scheduling = periodicSchedule;
		}

		if (scheduling == null) {
			// not periodic
			final ProcessSchedule triggeredSchedule = ScheduleHelper
					.getTriggeredSchedule(
							methodEnsMembership.getParameterAnnotations(),
							membership.method.in,
							membership.method.inOut);

			if (triggeredSchedule != null) {
				scheduling = triggeredSchedule;
			}
		}

		if (scheduling == null) {
			// No scheduling specified by annotations, using defaults
			scheduling = new ProcessPeriodicSchedule();
		}

		final Method knowledgeExchangeMethod = AnnotationHelper.getAnnotatedMethod(c,
				DEECoEnsembleMapper.class);
		
		if (knowledgeExchangeMethod == null) {
			throw new ParseException("The ensemble definition does not define a knowledge exchange function");
		}

		final ParameterizedMethod knowledgeExchange = ParserHelper.extractParametrizedMethod(knowledgeExchangeMethod);
		if (knowledgeExchange == null) {
			throw new ParseException("Malformed knowledge exchange function definition.");
		} 
		
		return new SchedulableEnsembleProcess(null, scheduling,
				membership, knowledgeExchange, null);
	}

	public static boolean isEnsembleDefinition(Class<?> clazz) {
		return clazz != null && Ensemble.class.isAssignableFrom(clazz)
				&& clazz.getAnnotation(DEECoEnsemble.class) != null;
	}

}
