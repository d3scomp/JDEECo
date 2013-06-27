package cz.cuni.mff.d3s.deeco.processor;

import java.lang.reflect.Method;

import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.ensemble.Ensemble;
import cz.cuni.mff.d3s.deeco.invokable.BooleanMembership;
import cz.cuni.mff.d3s.deeco.invokable.CandidateMembership;
import cz.cuni.mff.d3s.deeco.invokable.MembershipMethod;
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
	 * @return a {@link SchedulableEnsembleProcess} instance extracted from the
	 *         class definition
	 */
	public static SchedulableEnsembleProcess extractEnsembleProcess(Class<?> c)
			throws ParseException {
		// TODO: put names into the exception strings

		if (!isEnsembleDefinition(c)) {
			throw new ParseException("The class " + c.getName()
					+ " is not an ensemble definition.");
		}

		assert (c != null);

		final Method methodEnsMembership = AnnotationHelper.getAnnotatedMethod(
				c, Membership.class);

		if (methodEnsMembership == null) {
			throw new ParseException(
					"The ensemble definition does not define a membership function");
		}

		final ParameterizedMethod pm = ParserHelper
				.extractParametrizedMethod(methodEnsMembership);

		if (pm == null) {
			throw new ParseException(
					"Malformed membership function definition.");
		}

		MembershipMethod membership = null;
		// Look up MembershipMethod
		//if (methodEnsMembership.getClass().isAssignableFrom(CandidateMembership.class)){
		if (methodEnsMembership.getReturnType().isAssignableFrom(String.class)){
			if (methodEnsMembership.getAnnotation(Membership.class).candidateRange() <= 1){
				throw new ParseException(
						"Candidate MembershipMethod annotation needs to have a set size higher or equal to 2");
			}
			// no other possible exception then we assign a new candidate membership
			membership =  new CandidateMembership(pm);
		} else if (methodEnsMembership.getReturnType().isAssignableFrom(Boolean.class)) {
			membership =  new BooleanMembership(pm);
		} else {
			throw new ParseException(
					"MembershipMethod function has an unknown membership return type");
		}

		final Method knowledgeExchangeMethod = AnnotationHelper
				.getAnnotatedMethod(c, KnowledgeExchange.class);

		if (knowledgeExchangeMethod == null) {
			throw new ParseException(
					"The ensemble definition does not define a knowledge exchange function");
		}

		final ParameterizedMethod knowledgeExchange = ParserHelper
				.extractParametrizedMethod(knowledgeExchangeMethod);
		if (knowledgeExchange == null) {
			throw new ParseException(
					"Malformed knowledge exchange function definition.");
		}

		// Look up scheduling
		ProcessSchedule scheduling = null;

		final ProcessSchedule periodicSchedule = ScheduleHelper
				.getPeriodicSchedule(AnnotationHelper.getAnnotation(
						PeriodicScheduling.class, knowledgeExchangeMethod.getAnnotations()));
		if (periodicSchedule != null) {
			scheduling = periodicSchedule;
		}

		if (scheduling == null) {
			// not periodic
			final ProcessSchedule triggeredSchedule = ScheduleHelper
					.getTriggeredSchedule(
							methodEnsMembership.getParameterAnnotations(),
							membership.method.in, membership.method.inOut);

			if (triggeredSchedule != null) {
				scheduling = triggeredSchedule;
			}
		}

		if (scheduling == null) {
			// No scheduling specified by annotations, using defaults
			scheduling = new ProcessPeriodicSchedule();
		}

		return new SchedulableEnsembleProcess(null, scheduling, membership,
				knowledgeExchange, null);
	}

	public static boolean isEnsembleDefinition(Class<?> clazz) {
		return clazz != null && Ensemble.class.isAssignableFrom(clazz);
	}

}
