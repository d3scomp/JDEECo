package cz.cuni.mff.d3s.deeco.scheduling;

import cz.cuni.mff.d3s.deeco.exceptions.KMException;
import cz.cuni.mff.d3s.deeco.knowledge.ConstantKeys;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.TriggerType;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;
import cz.cuni.mff.d3s.deeco.runtime.model.Ensemble;
import cz.cuni.mff.d3s.deeco.runtime.model.TriggeredSchedule;

public class EnsembleJobProducer extends TriggeredJobProducer {

	private Ensemble ensemble;

	public EnsembleJobProducer(Ensemble ensemble, Scheduler scheduler,
			Runtime runtime) {
		super((TriggeredSchedule) ensemble.getSchedule(), scheduler, runtime);
		this.ensemble = ensemble;
	}

	@Override
	public void knowledgeChanged(String triggerer, TriggerType recMode) {
		try {
			KnowledgeManager km = runtime.getKnowledgeManager();
			Object[] ids = (Object[]) km
					.getKnowledge(ConstantKeys.ROOT_KNOWLEDGE_ID);
			if (recMode.equals(TriggerType.COORDINATOR)) {
				for (Object id : ids)
					scheduleJob(new EnsembleJob(ensemble, triggerer,
							(String) id, null, runtime));
			} else {
				for (Object id : ids)
					scheduleJob(new EnsembleJob(ensemble, (String) id,
							triggerer, null, runtime));
			}
		} catch (KMException kme) {
			Log.e("Knowledge Manager access exception", kme);
		}
	}

}
