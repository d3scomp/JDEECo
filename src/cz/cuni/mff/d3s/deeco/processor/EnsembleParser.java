package cz.cuni.mff.d3s.deeco.processor;

import java.lang.reflect.Method;

import cz.cuni.mff.d3s.deeco.annotations.DEECoEnsemble;
import cz.cuni.mff.d3s.deeco.annotations.DEECoEnsembleMapper;
import cz.cuni.mff.d3s.deeco.annotations.DEECoEnsembleMembership;
import cz.cuni.mff.d3s.deeco.annotations.DEECoPeriodicScheduling;
import cz.cuni.mff.d3s.deeco.ensemble.Ensemble;
import cz.cuni.mff.d3s.deeco.invokable.AnnotationHelper;
import cz.cuni.mff.d3s.deeco.invokable.BooleanMembership;
import cz.cuni.mff.d3s.deeco.invokable.FuzzyMembership;
import cz.cuni.mff.d3s.deeco.invokable.Membership;
import cz.cuni.mff.d3s.deeco.invokable.ParameterizedMethod;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcess;
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
	public SchedulableEnsembleProcess extractEnsembleProcess(Class<?> c) {
		if (c == null) {
			return null;
		}

		final Method methodEnsMembership = AnnotationHelper.getAnnotatedMethod(c,
				DEECoEnsembleMembership.class);
		if (methodEnsMembership == null) {
			return null;
		}

		final ParameterizedMethod pm = ParameterizedMethod
				.extractParametrizedMethod(methodEnsMembership);
		if (pm == null) {
			return null;
		}

		// Look up Membership
		Membership membership;
		if (methodEnsMembership.getReturnType().isAssignableFrom(double.class)) {
					membership = new FuzzyMembership(
							pm,
							(Double) AnnotationHelper.getAnnotationValue(methodEnsMembership
									.getAnnotation(DEECoEnsembleMembership.class)));
		} else {
					membership = new BooleanMembership(pm);
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
			final ProcessSchedule triggeredSchedule = ScheduleHelper.getTriggeredSchedule(
					methodEnsMembership.getParameterAnnotations(),
					membership.getIn(),
					membership.getInOut());
			
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

		final ParameterizedMethod mapper = ParameterizedMethod
						.extractParametrizedMethod(mapperMethod);
		if (mapper == null) {
			return null;
		} else

		return new SchedulableEnsembleProcess(scheduling, membership, mapper);
	}
	
	public boolean isEnsembleDefinition(Class<?> clazz) {
		return clazz != null
				&& Ensemble.class.isAssignableFrom(clazz)
				&& clazz.getAnnotation(DEECoEnsemble.class) != null;
	}

}
