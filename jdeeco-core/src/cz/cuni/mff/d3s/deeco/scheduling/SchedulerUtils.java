package cz.cuni.mff.d3s.deeco.scheduling;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableComponentProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.invokable.TriggeredSchedulableProcess;

public class SchedulerUtils {
	
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
			for (TriggeredSchedulableProcess tsp : scheduler.getTriggeredProcesses()) {
				if (tsp.sp instanceof SchedulableComponentProcess) {
					process = (SchedulableComponentProcess) tsp.sp;
					if (process.getComponentId().equals(componentId))
						result.add(process);
				}
			}
		}
		return result;
	}

	public static List<SchedulableEnsembleProcess> getEnsembleProcesses(IScheduler scheduler) {
		List<SchedulableEnsembleProcess> result = new LinkedList<SchedulableEnsembleProcess>();
		for (SchedulableProcess sp : scheduler.getPeriodicProcesses()) {
			if (sp instanceof SchedulableEnsembleProcess) {
				result.add((SchedulableEnsembleProcess) sp);
			}
		}
		for (TriggeredSchedulableProcess tsp : scheduler.getTriggeredProcesses()) {
			if (tsp.sp instanceof SchedulableEnsembleProcess)
				result.add((SchedulableEnsembleProcess) tsp.sp);
		}
		return result;
	}
}
