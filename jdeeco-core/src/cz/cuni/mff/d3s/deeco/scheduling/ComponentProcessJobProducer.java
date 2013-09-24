package cz.cuni.mff.d3s.deeco.scheduling;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.TriggerType;
import cz.cuni.mff.d3s.deeco.monitoring.MonitorProvider;
import cz.cuni.mff.d3s.deeco.runtime.model.ComponentProcess;
import cz.cuni.mff.d3s.deeco.runtime.model.TriggeredSchedule;

public class ComponentProcessJobProducer extends TriggeredJobProducer {

	private ComponentProcess componentProcess;

	public ComponentProcessJobProducer(ComponentProcess componentProcess,
			Scheduler scheduler, KnowledgeManager km,
			MonitorProvider monitorProvider) {
		super((TriggeredSchedule) componentProcess.getSchedule(), scheduler,
				km, monitorProvider);
		this.componentProcess = componentProcess;
	}

	@Override
	public void knowledgeChanged(String triggerer, TriggerType recMode) {
		ComponentProcessJob job = new ComponentProcessJob(componentProcess,
				triggerer, null, km);
		job.setMonitor(monitorProvider.getProcessMonitor(job));
		scheduleJob(job);
	}

}
