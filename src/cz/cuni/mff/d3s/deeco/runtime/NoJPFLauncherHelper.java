package cz.cuni.mff.d3s.deeco.runtime;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcess;
import cz.cuni.mff.d3s.deeco.knowledge.ComponentKnowledge;
import cz.cuni.mff.d3s.deeco.processor.ComponentParser;
import cz.cuni.mff.d3s.deeco.processor.EnsembleParser;

public class NoJPFLauncherHelper {
	
	public static void parseComponents(Class<?>[] components, ComponentParser cp, Runtime rt) {
		ComponentKnowledge initialKnowledge;
		if (components != null) {
			for (Class<?> c : components) {
				initialKnowledge = cp.extractInitialKnowledge(c);
				if (initialKnowledge != null) {
					if (rt.addComponentKnowledge(initialKnowledge))
						rt.addComponentPorcesses(cp.extractComponentProcess(c, initialKnowledge.id));
				}
			}
		}
	}
	
	public static void parseEnsembles(Class<?>[] ensembles, EnsembleParser ep, Runtime rt) {
		List<SchedulableEnsembleProcess> seps = new LinkedList<SchedulableEnsembleProcess>();
		if (ensembles != null) {
			SchedulableEnsembleProcess sep;
			for (Class<?> e : ensembles) {
				sep = ep.extractEnsembleProcess(e);
				if (sep != null)
					seps.add(sep);
			}
			rt.addEnsembleProcesses(seps);
		}
	}
}
