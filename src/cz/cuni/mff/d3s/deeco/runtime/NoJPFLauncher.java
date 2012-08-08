package cz.cuni.mff.d3s.deeco.runtime;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.processor.ComponentParser;
import cz.cuni.mff.d3s.deeco.processor.EnsembleParser;
import cz.cuni.mff.d3s.deeco.scheduling.Scheduler;

public class NoJPFLauncher extends Launcher {

	private Class<?>[] components;
	private Class<?>[] ensembles;

	public NoJPFLauncher(KnowledgeManager km, Scheduler scheduler,
			Class<?>[] components, Class<?>[] ensembles) {
		super(km, scheduler);
		this.components = components;
		this.ensembles = ensembles;
	}

	public void launch() {
		Runtime runtime = new Runtime(km, scheduler);
		NoJPFLauncherHelper.parseEnsembles(ensembles, new EnsembleParser(),
				runtime);
		NoJPFLauncherHelper.parseComponents(components, new ComponentParser(),
				runtime);
		runtime.startRuntime();
	}
}
