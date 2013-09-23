package cz.cuni.mff.d3s.deeco.scheduling;

import cz.cuni.mff.d3s.deeco.knowledge.TriggerType;
import cz.cuni.mff.d3s.deeco.monitoring.MonitorProvider;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;
import cz.cuni.mff.d3s.deeco.runtime.model.ComponentProcess;
import cz.cuni.mff.d3s.deeco.runtime.model.TriggeredSchedule;

public class ComponentProcessJobProducer extends TriggeredJobProducer {

	private ComponentProcess componentProcess;

	public ComponentProcessJobProducer(ComponentProcess componentProcess,
			Scheduler scheduler, Runtime runtime,
			MonitorProvider monitorProvider) {
		super((TriggeredSchedule) componentProcess.getSchedule(), scheduler,
				runtime, monitorProvider);
		this.componentProcess = componentProcess;
	}

	@Override
	public void knowledgeChanged(String triggerer, TriggerType recMode) {
		scheduleJob(new ComponentProcessJob(componentProcess, triggerer,
				monitorProvider.getMonitor(componentProcess.getId()), null,
				runtime));
	}

}
