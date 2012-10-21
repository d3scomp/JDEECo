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

public class ProcessInstantiator {

	public static List<SchedulableProcess> createProcesses(
			List<? extends IScheduleableProcessCreator> creators,
			KnowledgeManager km) {
		List<SchedulableProcess> result = new LinkedList<SchedulableProcess>();
		if (creators != null)
			for (IScheduleableProcessCreator c : creators)
				result.add(c.extract(km));
		return result;
	}

	public static List<SchedulableComponentProcess> createComponentProcesses(
			List<SchedulableComponentProcessCreator> creators,
			KnowledgeManager km) {
		List<SchedulableComponentProcess> result = new LinkedList<SchedulableComponentProcess>();
		if (creators != null)
			for (SchedulableComponentProcessCreator c : creators)
				result.add(c.extract(km));
		return result;
	}

	public static List<SchedulableEnsembleProcess> createEnsembleProcesses(
			List<SchedulableEnsembleProcessCreator> creators,
			KnowledgeManager km) {
		List<SchedulableEnsembleProcess> result = new LinkedList<SchedulableEnsembleProcess>();
		if (creators != null)
			for (SchedulableEnsembleProcessCreator c : creators)
				result.add(c.extract(km));
		return result;
	}

}
