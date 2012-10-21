package cz.cuni.mff.d3s.deeco.processor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cz.cuni.mff.d3s.deeco.annotations.DEECoComponent;
import cz.cuni.mff.d3s.deeco.annotations.DEECoInitialize;
import cz.cuni.mff.d3s.deeco.annotations.DEECoPeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.DEECoProcess;
import cz.cuni.mff.d3s.deeco.annotations.DEECoStrongLocking;
import cz.cuni.mff.d3s.deeco.annotations.ELockingMode;
import cz.cuni.mff.d3s.deeco.invokable.AnnotationHelper;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableComponentProcess;
import cz.cuni.mff.d3s.deeco.invokable.creators.ParametrizedMethodCreator;
import cz.cuni.mff.d3s.deeco.invokable.creators.SchedulableComponentProcessCreator;
import cz.cuni.mff.d3s.deeco.knowledge.ComponentKnowledge;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.scheduling.ProcessPeriodicSchedule;
import cz.cuni.mff.d3s.deeco.scheduling.ProcessSchedule;
import cz.cuni.mff.d3s.deeco.scheduling.ScheduleHelper;

public class ComponentParser {

	/**
	 * Static function used to extract {@link SchedulableComponentProcess}
	 * instance from the class definition
	 * 
	 * @param c
	 *            class to be parsed for extraction
	 * @param root
	 *            component id for which process executes
	 * @param km
	 *            {@link KnowledgeManager} instance that is used for knowledge
	 *            repository communication
	 * @return list of {@link SchedulableComponentProcess} instances extracted
	 *         from the class definition
	 */
	public List<SchedulableComponentProcessCreator> extractComponentProcess(
			Class<?> c, String root) {
		if (c == null) {
			return null;
		}

		List<Method> methods = AnnotationHelper.getAnnotatedMethods(c,
				DEECoProcess.class);

		if (methods == null || methods.size() == 0) {
			return null;
		}

		final List<SchedulableComponentProcessCreator> result = new ArrayList<SchedulableComponentProcessCreator>();

		for (Method m : methods) {
			final ParametrizedMethodCreator currentMethodCreator = ParametrizedMethodCreator
					.extractParametrizedMethodCreator(m, root);
			if (currentMethodCreator == null) {
				// Not a process method
				continue;
			}

			ProcessSchedule ps = null;
			final ProcessSchedule periodicSchedule = ScheduleHelper
					.getPeriodicSchedule(AnnotationHelper.getAnnotation(
							DEECoPeriodicScheduling.class, m.getAnnotations()));
			if (periodicSchedule != null) {
				ps = periodicSchedule;
			}

			if (ps == null) {
				final ProcessSchedule triggeredSchedule = ScheduleHelper
						.getTriggeredSchedule(m.getParameterAnnotations(),
								currentMethodCreator.in,
								currentMethodCreator.inOut);
				if (triggeredSchedule != null) {
					ps = triggeredSchedule;
				}
			}

			if (ps == null) {
				// No scheduling specified by annotations, using defaults
				ps = new ProcessPeriodicSchedule();
			}

			ELockingMode lm;
			if (AnnotationHelper.getAnnotation(DEECoStrongLocking.class,
					m.getAnnotations()) == null) {
				lm = (ps instanceof ProcessPeriodicSchedule) ? ELockingMode.WEAK
						: ELockingMode.STRONG;
			} else {
				lm = ELockingMode.STRONG;
			}

			final SchedulableComponentProcessCreator skp = new SchedulableComponentProcessCreator(
					ps, currentMethodCreator, lm, root);
			result.add(skp);
		}

		return result;
	}

	/**
	 * Retrieves init method from the <code>ComponentKnowledge</code> class.
	 * 
	 * @param c
	 *            class to be parsed
	 * @return init method or null in case no matching found
	 */
	public ComponentKnowledge extractInitialKnowledge(Class<?> c) {
		List<Method> initMethods = AnnotationHelper.getAnnotatedMethods(c,
				DEECoInitialize.class);
		if (initMethods.size() == 1) {
			try {
				ComponentKnowledge ck = (ComponentKnowledge) initMethods.get(0)
						.invoke(null, new Object[] {});
				if (ck.id == null || ck.id.equals(""))
					ck.id = UUID.randomUUID().toString();
				return ck;
			} catch (Exception e) {
				System.out
						.println("Component Knowledge Initialization exception!");
			}
		}
		return null;
	}

	public boolean isComponentDefinition(Class<?> clazz) {
		return clazz != null
				&& ComponentKnowledge.class.isAssignableFrom(clazz)
				&& clazz.getAnnotation(DEECoComponent.class) != null;
	}

}
