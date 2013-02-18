package cz.cuni.mff.d3s.deeco.scheduling;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableComponentProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcessTrigger;

/**
 * Utility class providing functions for component and ensemble processes
 * retrieval.
 * 
 * @author Michal Kit
 * 
 */
public class SchedulerUtils {

	/**
	 * Gets all component processes (both triggered and periodic) available in
	 * the given scheduler for a specified component.
	 * 
	 * @param componentId
	 *            ID of a component for which processes should be returned.
	 * @param scheduler
	 *            {@link IScheduler} instance that process should be retrieved
	 *            from.
	 * @return List of component processes.
	 */
	public static List<SchedulableComponentProcess> getComponentProcesses(
			String componentId, IScheduler scheduler) {
		List<SchedulableComponentProcess> result = new LinkedList<SchedulableComponentProcess>();
		if (componentId != null) {
			SchedulableComponentProcess process;
			for (SchedulableProcess sp : scheduler.getPeriodicProcesses()) {
				if (sp instanceof SchedulableComponentProcess) {
					process = (SchedulableComponentProcess) sp;
					if (process.getComponentId().equals(componentId))
						result.add(process);
				}
			}
			for (SchedulableProcessTrigger tsp : scheduler
					.getTriggeredProcesses()) {
				if (tsp.sp instanceof SchedulableComponentProcess) {
					process = (SchedulableComponentProcess) tsp.sp;
					if (process.getComponentId().equals(componentId))
						result.add(process);
				}
			}
		}
		return result;
	}

	/**
	 * Gets all ensemble processes (both triggered and periodic) available in
	 * the given scheduler.
	 * 
	 * @param scheduler
	 *            {@link IScheduler} instance that process should be retrieved
	 *            from.
	 * @return List of ensemble processes.
	 */
	public static List<SchedulableEnsembleProcess> getEnsembleProcesses(
			IScheduler scheduler) {
		List<SchedulableEnsembleProcess> result = new LinkedList<SchedulableEnsembleProcess>();
		for (SchedulableProcess sp : scheduler.getPeriodicProcesses()) {
			if (sp instanceof SchedulableEnsembleProcess) {
				result.add((SchedulableEnsembleProcess) sp);
			}
		}
		for (SchedulableProcessTrigger tsp : scheduler
				.getTriggeredProcesses()) {
			if (tsp.sp instanceof SchedulableEnsembleProcess)
				result.add((SchedulableEnsembleProcess) tsp.sp);
		}
		return result;
	}
}
