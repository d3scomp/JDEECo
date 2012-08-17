package cz.cuni.mff.d3s.deeco.runtime;

import cz.cuni.mff.d3s.deeco.knowledge.ComponentKnowledge;
import cz.cuni.mff.d3s.deeco.provider.IDEECoObjectProvider;
import cz.cuni.mff.d3s.deeco.scheduling.Scheduler;

public class Launcher {
	private Runtime rt;
	private IDEECoObjectProvider provider;

	public Launcher(Scheduler scheduler, IDEECoObjectProvider provider) {
		this.rt = new Runtime(scheduler);
		this.provider = provider;
	}
	
	public void launch() {
		rt.addSchedulablePorcesses(provider.getProcesses());
		for (ComponentKnowledge ck : provider.getKnowledges()) {
			if (!rt.addComponentKnowledge(ck)) {
				System.out.println("Error when writng initial knowledge: " + ck.getClass());
				return;
			}
		}
		rt.startRuntime();
	}
}
