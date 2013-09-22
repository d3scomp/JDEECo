package cz.cuni.mff.d3s.deeco.processor;

import static cz.cuni.mff.d3s.deeco.processor.AnnotationHelper.getAnnotatedMethods;
import static cz.cuni.mff.d3s.deeco.processor.AnnotationHelper.getAnnotation;
import static cz.cuni.mff.d3s.deeco.processor.ScheduleHelper.getPeriodicSchedule;
import static cz.cuni.mff.d3s.deeco.processor.ScheduleHelper.getTriggeredSchedule;
import static cz.cuni.mff.d3s.deeco.processor.ParserHelper.getParameterList;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.StrongLocking;
import cz.cuni.mff.d3s.deeco.annotations.WeakLocking;
import cz.cuni.mff.d3s.deeco.definitions.ComponentDefinition;
import cz.cuni.mff.d3s.deeco.exceptions.ParametersParseException;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.path.grammar.ParseException;
import cz.cuni.mff.d3s.deeco.runtime.model.ComponentProcess;
import cz.cuni.mff.d3s.deeco.runtime.model.LockingMode;
import cz.cuni.mff.d3s.deeco.runtime.model.Parameter;
import cz.cuni.mff.d3s.deeco.runtime.model.PeriodicSchedule;
import cz.cuni.mff.d3s.deeco.runtime.model.Schedule;

/**
 * Parser class for component definitions.
 * 
 * @author Michal Kit
 * 
 */
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
	public static List<ComponentProcess> extractComponentProcesses(Class<?> c) throws ParseException {

		assert (c != null);

		if (!isComponentDefinition(c)) {
			throw new ParseException("The class " + c.getName()
					+ " is not a component definition.");
		}

		List<Method> methods = getAnnotatedMethods(c, Process.class);

		if (methods == null || methods.size() == 0) {
			throw new ParseException("The class " + c.getName()
					+ " has no process defined.");
		}

		final List<ComponentProcess> result = new LinkedList<ComponentProcess>();

		Schedule schedule;
		LockingMode lockingMode;
		List<Parameter> parameters;
		for (Method m : methods) {
			try {
				parameters = getParameterList(m);
			} catch (ParametersParseException cepe) {
				throw new ParseException(c.getName()
						+ ": Parameters for the method " + m.getName()
						+ " cannot be parsed.");
			}
			schedule = getPeriodicSchedule(getAnnotation(
					PeriodicScheduling.class, m.getAnnotations()));
			if (schedule == null)
				schedule = getTriggeredSchedule(m.getParameterAnnotations(),
						parameters);

			if (getAnnotation(StrongLocking.class, m.getAnnotations()) == null) {
				if (getAnnotation(WeakLocking.class, m.getAnnotations()) == null)
					lockingMode = (schedule instanceof PeriodicSchedule) ? LockingMode.WEAK
							: LockingMode.STRONG;
				else
					lockingMode = LockingMode.WEAK;
			} else {
				lockingMode = LockingMode.STRONG;
			}
			String id = m.getAnnotation(Process.class).value();
			if (id == null || id.equals(""))
				id = m.toString();
			result.add(new ComponentProcess(id, parameters, m, schedule,
					lockingMode));
		}
		return result;
	}

	/**
	 * Retrieves initial knowledge of a component from the non-parametric
	 * constructor.
	 * 
	 * @param c
	 *            class to be parsed
	 * @return component knowledge.
	 */
	public static ComponentDefinition extractInitialKnowledge(Class<?> c) {
		ComponentDefinition ck;
		try {
			Constructor<?> constructor = c.getConstructor();
			if (constructor != null) {
				ck = (ComponentDefinition) constructor
						.newInstance(new Object[] {});
				assignUIDIfNotSet(ck);
				return ck;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.w("No initial state can be retrieved for the component: " + c);
		return null;
	}

	/**
	 * Checks whether the given class is a component definition.
	 * 
	 * @param clazz
	 *            class to be checked.
	 * @return True if the class is a component definition. False otherwise.
	 */
	public static boolean isComponentDefinition(Class<?> clazz) {
		return clazz != null
				&& ComponentDefinition.class.isAssignableFrom(clazz);
	}

	// ------------- Private functions -------------------

	private static void assignUIDIfNotSet(ComponentDefinition cd) {
		if (cd.id == null || cd.id.equals(""))
			cd.id = UUID.randomUUID().toString();
	}

}
