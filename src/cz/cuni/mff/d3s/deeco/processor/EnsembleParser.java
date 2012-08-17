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
		SchedulableEnsembleProcess result = null;
		if (c != null) {
			ProcessSchedule pSchedule = ScheduleHelper
					.getPeriodicSchedule(AnnotationHelper.getAnnotation(
							DEECoPeriodicScheduling.class, c.getAnnotations()));
			result = new SchedulableEnsembleProcess();
			Method method = AnnotationHelper.getAnnotatedMethod(c,
					DEECoEnsembleMembership.class);
			if (method != null) {
				ParameterizedMethod pm = ParameterizedMethod
						.extractParametrizedMethod(method);
				if (pm == null)
					return null;
				if (method.getReturnType().isAssignableFrom(double.class))
					result.membership = new FuzzyMembership(
							pm,
							(Double) AnnotationHelper.getAnnotationValue(method
									.getAnnotation(DEECoEnsembleMembership.class)));
				else
					result.membership = new BooleanMembership(pm);
				if (pSchedule == null) {// not periodic
					pSchedule = ScheduleHelper.getTriggeredSchedule(
							method.getParameterAnnotations(),
							result.membership.getIn(),
							result.membership.getInOut());
					if (pSchedule == null)
						result.scheduling = new ProcessPeriodicSchedule();
					else
						result.scheduling = pSchedule;
				} else
					result.scheduling = pSchedule;
			} else
				return null;
			method = AnnotationHelper.getAnnotatedMethod(c,
					DEECoEnsembleMapper.class);
			if (method != null) {
				result.mapper = ParameterizedMethod
						.extractParametrizedMethod(method);
				if (result.mapper == null)
					return null;
			} else
				return null;
		}
		return result;
	}
	
	public boolean isEnsembleDefinition(Class<?> clazz) {
		return clazz != null
				&& Ensemble.class.isAssignableFrom(clazz)
				&& clazz.getAnnotation(DEECoEnsemble.class) != null;
	}

}
