package cz.cuni.mff.d3s.deeco.processor;

import java.lang.reflect.Method;

import cz.cuni.mff.d3s.deeco.annotations.DEECoEnsemble;
import cz.cuni.mff.d3s.deeco.annotations.DEECoEnsembleMapper;
import cz.cuni.mff.d3s.deeco.annotations.DEECoEnsembleMembership;
import cz.cuni.mff.d3s.deeco.annotations.DEECoPeriodicScheduling;
import cz.cuni.mff.d3s.deeco.ensemble.Ensemble;
import cz.cuni.mff.d3s.deeco.invokable.AnnotationHelper;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcess;
import cz.cuni.mff.d3s.deeco.invokable.creators.BooleanMembershipCreator;
import cz.cuni.mff.d3s.deeco.invokable.creators.FuzzyMembershipCreator;
import cz.cuni.mff.d3s.deeco.invokable.creators.MembershipCreator;
import cz.cuni.mff.d3s.deeco.invokable.creators.ParametrizedMethodCreator;
import cz.cuni.mff.d3s.deeco.invokable.creators.SchedulableEnsembleProcessCreator;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.scheduling.ProcessPeriodicSchedule;
import cz.cuni.mff.d3s.deeco.scheduling.ProcessSchedule;
import cz.cuni.mff.d3s.deeco.scheduling.ScheduleHelper;

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
	 * @return list of {@link SchedulableEnsembleProcess} instances extracted
	 *         from the class definition
	 */
	public static SchedulableEnsembleProcessCreator extractEnsembleProcess(
			Class<?> c) {
		if (c == null) {
			return null;
		}

		final Method methodEnsMembership = AnnotationHelper.getAnnotatedMethod(
				c, DEECoEnsembleMembership.class);
		if (methodEnsMembership == null) {
			return null;
		}

		final ParametrizedMethodCreator pmc = ParametrizedMethodCreator
				.extractParametrizedMethodCreator(methodEnsMembership);
		if (pmc == null) {
			return null;
		}

		// Look up Membership
		MembershipCreator membershipCreator;
		if (methodEnsMembership.getReturnType().isAssignableFrom(double.class)) {
			membershipCreator = new FuzzyMembershipCreator(
					pmc,
					(Double) AnnotationHelper.getAnnotationValue(methodEnsMembership
							.getAnnotation(DEECoEnsembleMembership.class)));
		} else {
			membershipCreator = new BooleanMembershipCreator(pmc);
		}

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
							membershipCreator.method.in,
							membershipCreator.method.inOut);

			if (triggeredSchedule != null) {
				scheduling = triggeredSchedule;
			}
		}

		if (scheduling == null) {
			// No scheduling specified by annotations, using defaults
			scheduling = new ProcessPeriodicSchedule();
		}

		final Method mapperMethod = AnnotationHelper.getAnnotatedMethod(c,
				DEECoEnsembleMapper.class);
		if (mapperMethod == null) {
			return null;
		}

		final ParametrizedMethodCreator mapperCreator = ParametrizedMethodCreator
				.extractParametrizedMethodCreator(mapperMethod);
		if (mapperCreator == null) {
			return null;
		} else
			return new SchedulableEnsembleProcessCreator(scheduling,
					mapperCreator, membershipCreator);
	}

	public static boolean isEnsembleDefinition(Class<?> clazz) {
		return clazz != null && Ensemble.class.isAssignableFrom(clazz)
				&& clazz.getAnnotation(DEECoEnsemble.class) != null;
	}

}
