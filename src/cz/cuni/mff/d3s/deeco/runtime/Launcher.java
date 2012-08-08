package cz.cuni.mff.d3s.deeco.runtime;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.scheduling.Scheduler;

public abstract class Launcher {
	protected KnowledgeManager km;
	protected Scheduler scheduler;

	public Launcher(KnowledgeManager km, Scheduler scheduler) {
		this.km = km;
		this.scheduler = scheduler;
	}
	
	abstract public void launch();
}
