package cz.cuni.mff.d3s.deeco.provider;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableComponentProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.invokable.creators.IScheduleableProcessCreator;
import cz.cuni.mff.d3s.deeco.invokable.creators.SchedulableComponentProcessCreator;
import cz.cuni.mff.d3s.deeco.invokable.creators.SchedulableEnsembleProcessCreator;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;

/**
 * Utility class used for instantiating processes from their creators.
 * 
 * @author Michal Kit
 * 
 */
public class ProcessInstantiator {

	/**
	 * Creates instances of {@link SchedulableProcess} from the given creators.
	 * 
	 * @param creators
	 *            Process creators.
	 * @param km
	 *            Knowledge manager instance used by processes.
	 * @return Created processes.
	 */
	public static List<SchedulableProcess> createProcesses(
			List<? extends IScheduleableProcessCreator> creators,
			KnowledgeManager km, ClassLoader contextClassLoader) {
		List<SchedulableProcess> result = new LinkedList<SchedulableProcess>();
		if (creators != null)
			for (IScheduleableProcessCreator c : creators)
				result.add(c.extract(km, contextClassLoader));
		return result;
	}

	/**
	 * Creates instances of {@link SchedulableComponentProcess} from the given
	 * creators.
	 * 
	 * @param creators
	 *            Component process creators.
	 * @param km
	 *            Knowledge manager instance used by processes.
	 * @return Created processes.
	 */
	public static List<SchedulableComponentProcess> createComponentProcesses(
			List<SchedulableComponentProcessCreator> creators,
			KnowledgeManager km, ClassLoader contextClassLoader) {
		List<SchedulableComponentProcess> result = new LinkedList<SchedulableComponentProcess>();
		if (creators != null)
			for (SchedulableComponentProcessCreator c : creators)
				result.add(c.extract(km, contextClassLoader));
		return result;
	}

	/**
	 * Creates instances of {@link SchedulableEnsembleProcess} from the given
	 * creators.
	 * 
	 * @param creators
	 *            Ensemble process creators.
	 * @param km
	 *            Knowledge manager instance used by processes.
	 * @return Created processes.
	 */
	public static List<SchedulableEnsembleProcess> createEnsembleProcesses(
			List<SchedulableEnsembleProcessCreator> creators,
			KnowledgeManager km, ClassLoader contextClassLoader) {
		List<SchedulableEnsembleProcess> result = new LinkedList<SchedulableEnsembleProcess>();
		if (creators != null)
			for (SchedulableEnsembleProcessCreator c : creators)
				result.add(c.extract(km, contextClassLoader));
		return result;
	}

}
