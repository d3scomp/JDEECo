package cz.cuni.mff.d3s.deeco.runtime;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.processor.ComponentParser;
import cz.cuni.mff.d3s.deeco.processor.EnsembleParser;
import cz.cuni.mff.d3s.deeco.scheduling.Scheduler;

public class NoJPFLauncher {
	private KnowledgeManager km;
	private Scheduler scheduler;

	public NoJPFLauncher(KnowledgeManager km, Scheduler scheduler) {
		this.km = km;
		this.scheduler = scheduler;
	}

	public void launch(Class<?>[] components, Class<?>[] ensembles) {
		Runtime runtime = new Runtime(km, scheduler);
		NoJPFLauncherHelper.parseEnsembles(ensembles, new EnsembleParser(),
				runtime);
		NoJPFLauncherHelper.parseComponents(components, new ComponentParser(),
				runtime);
		runtime.startRuntime();
	}
}
