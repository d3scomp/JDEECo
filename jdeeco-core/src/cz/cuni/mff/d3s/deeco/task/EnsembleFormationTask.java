package cz.cuni.mff.d3s.deeco.task;

import java.util.Collection;

import cz.cuni.mff.d3s.deeco.ensembles.EnsembleFactory;
import cz.cuni.mff.d3s.deeco.ensembles.EnsembleInstance;
import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainer;
import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainerException;
import cz.cuni.mff.d3s.deeco.knowledge.container.TrackingKnowledgeContainer;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.TimeTriggerExt;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;

public class EnsembleFormationTask extends Task {
	
	private EnsembleFactory factory;
	private ComponentInstance componentInstance;
	private TimeTrigger trigger;

	public EnsembleFormationTask(Scheduler scheduler, EnsembleFactory factory, ComponentInstance componentInstance) {
		super(scheduler);		
		this.factory = factory;
		this.componentInstance = componentInstance;
		this.trigger = new TimeTriggerExt();
		this.trigger.setOffset(factory.getOffset());
		this.trigger.setPeriod(factory.getPeriod());
	}

	@Override
	public void invoke(Trigger trigger) throws TaskInvocationException {
		KnowledgeContainer container = TrackingKnowledgeContainer.createFromKnowledgeManagers(componentInstance.getKnowledgeManager(), 
				componentInstance.getShadowKnowledgeManagerRegistry().getShadowKnowledgeManagers());
		
		Collection<EnsembleInstance> instances = factory.createInstances(container);
		
		for(EnsembleInstance instance : instances) {
			instance.performKnowledgeExchange();
		}
		
		try {
			container.commitChanges();
		} catch (KnowledgeContainerException e) {
			throw new TaskInvocationException(e);
		}
	}

	@Override
	protected void registerTriggers() {
		
	}

	@Override
	protected void unregisterTriggers() {
		
	}

	@Override
	public TimeTrigger getTimeTrigger() {
		return trigger;
	}	
}
