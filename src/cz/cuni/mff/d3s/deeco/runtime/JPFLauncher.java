package cz.cuni.mff.d3s.deeco.runtime;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableComponentProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.knowledge.ComponentKnowledge;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.processor.ParsedObjectReader;
import cz.cuni.mff.d3s.deeco.processor.SchedulableProcessWrapper;
import cz.cuni.mff.d3s.deeco.scheduling.Scheduler;

public class JPFLauncher extends Launcher {

	private String fileName;
	
	public JPFLauncher(KnowledgeManager km, Scheduler scheduler, String fileName) {
		super(km, scheduler);
		this.fileName = fileName;
	}
	
	public JPFLauncher(KnowledgeManager km, Scheduler scheduler) {
		super(km, scheduler);
		this.fileName = null;
	}

	public void launch() {
		ParsedObjectReader por = new ParsedObjectReader(fileName);
		List<SchedulableProcessWrapper> spws = new LinkedList<SchedulableProcessWrapper>();
		List<ComponentKnowledge> cks = new LinkedList<ComponentKnowledge>();
		if (por.read(spws, cks)) {
			Runtime rt = new Runtime(km, scheduler);
			SchedulableProcess sp;
			for (SchedulableProcessWrapper spw : spws) {
				sp = spw.extract();
				if (sp instanceof SchedulableEnsembleProcess) {
					rt.addEnsembleProcess((SchedulableEnsembleProcess) sp);
				} else if (sp instanceof SchedulableComponentProcess) {
					rt.addComponentProcess((SchedulableComponentProcess) sp);
				}
			}
			for (ComponentKnowledge ck : cks) {
				rt.addComponentKnowledge(ck);
			}
			rt.startRuntime();
		}
	}
}
