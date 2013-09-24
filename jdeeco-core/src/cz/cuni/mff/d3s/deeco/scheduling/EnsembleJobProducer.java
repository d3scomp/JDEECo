package cz.cuni.mff.d3s.deeco.scheduling;

import cz.cuni.mff.d3s.deeco.exceptions.KMException;
import cz.cuni.mff.d3s.deeco.knowledge.ConstantKeys;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.TriggerType;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.monitoring.MonitorProvider;
import cz.cuni.mff.d3s.deeco.runtime.model.Ensemble;
import cz.cuni.mff.d3s.deeco.runtime.model.TriggeredSchedule;

public class EnsembleJobProducer extends TriggeredJobProducer {

	private Ensemble ensemble;

	public EnsembleJobProducer(Ensemble ensemble, Scheduler scheduler,
			KnowledgeManager km, MonitorProvider monitorProvider) {
		super((TriggeredSchedule) ensemble.getSchedule(), scheduler, km,
				monitorProvider);
		this.ensemble = ensemble;
	}

	@Override
	public void knowledgeChanged(String triggerer, TriggerType recMode) {
		try {
			Object[] ids = (Object[]) km
					.getKnowledge(ConstantKeys.ROOT_KNOWLEDGE_ID);
			EnsembleJob job;
			if (recMode.equals(TriggerType.COORDINATOR)) {
				for (Object id : ids) {
					job = new EnsembleJob(ensemble, triggerer, (String) id,
							null, km);
					job.setMonitor(monitorProvider.getExchangeMonitor(job));
					scheduleJob(job);
				}

			} else {
				for (Object id : ids) {
					job = new EnsembleJob(ensemble, (String) id, triggerer,
							null, km);
					job.setMonitor(monitorProvider.getExchangeMonitor(job));
					scheduleJob(job);
				}
			}
		} catch (KMException kme) {
			Log.e("Knowledge Manager access exception", kme);
		}
	}

}
