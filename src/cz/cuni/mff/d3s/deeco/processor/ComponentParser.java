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
import cz.cuni.mff.d3s.deeco.invokable.ParameterizedMethod;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableComponentProcess;
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
	public List<SchedulableComponentProcess> extractComponentProcess(
			Class<?> c, String root) {
		List<SchedulableComponentProcess> result = null;
		if (c != null) {
			result = new ArrayList<SchedulableComponentProcess>();
			List<Method> methods = AnnotationHelper.getAnnotatedMethods(c,
					DEECoProcess.class);
			ParameterizedMethod currentMethod;
			ProcessSchedule ps;
			SchedulableComponentProcess skp;
			ELockingMode lm;
			if (methods != null && methods.size() > 0) {
				for (Method m : methods) {
					currentMethod = ParameterizedMethod
							.extractParametrizedMethod(m, root);
					if (currentMethod != null) {
						ps = ScheduleHelper
								.getPeriodicSchedule(AnnotationHelper
										.getAnnotation(
												DEECoPeriodicScheduling.class,
												m.getAnnotations()));
						if (ps == null) {// not periodic
							ps = ScheduleHelper.getTriggeredSchedule(
									m.getParameterAnnotations(),
									currentMethod.in, currentMethod.inOut);
							if (ps == null)
								ps = new ProcessPeriodicSchedule();
						}
						if (AnnotationHelper.getAnnotation(
								DEECoStrongLocking.class, m.getAnnotations()) == null)
							lm = (ps instanceof ProcessPeriodicSchedule) ? ELockingMode.WEAK
									: ELockingMode.STRONG;
						else
							lm = ELockingMode.STRONG;
						skp = new SchedulableComponentProcess(currentMethod, lm);
						skp.scheduling = ps;
						result.add(skp);
					}
				}
			}
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
