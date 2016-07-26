package cz.cuni.mff.d3s.deeco.task;

import java.util.Collection;

import cz.cuni.mff.d3s.deeco.ensembles.EnsembleFactory;
import cz.cuni.mff.d3s.deeco.ensembles.EnsembleFormationException;
import cz.cuni.mff.d3s.deeco.ensembles.EnsembleInstance;
import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainer;
import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainerException;
import cz.cuni.mff.d3s.deeco.knowledge.container.TrackingKnowledgeContainer;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.TimeTriggerExt;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;

/**
 * Represents a task responsible for periodically forming ensemble instances (i.e. implementors of {@link EnsembleInstance}) using
 * the associated {@link EnsembleFactory} and performing knowledge exchange on the resultant instances. Uses {@link TrackingKnowledgeContainer}
 * for providing knowledge access to the ensembles, as well as committing the changes back into the knowledge storage. 
 * 
 * @author Filip Krijt
 */
public class EnsembleFormationTask extends Task {
	
	private EnsembleFactory factory;
	private ComponentInstance componentInstance;
	private TimeTrigger trigger;

	/**
	 * Creates a new {@link EnsembleFormationTask} associated with the provided factory and component instance.
	 * @param scheduler
	 * @param factory
	 * @param componentInstance
	 */
	public EnsembleFormationTask(Scheduler scheduler, EnsembleFactory factory, ComponentInstance componentInstance) {
		super(scheduler, "Ensemble formation");		
		this.factory = factory;
		this.componentInstance = componentInstance;
		this.trigger = new TimeTriggerExt();
		this.trigger.setOffset(factory.getSchedulingOffset());
		this.trigger.setPeriod(factory.getSchedulingPeriod());
	}

	@Override
	public void invoke(Trigger trigger) throws TaskInvocationException {		
		
		try {
			KnowledgeContainer container = TrackingKnowledgeContainer.createFromKnowledgeManagers(componentInstance.getKnowledgeManager(), 
					componentInstance.getShadowKnowledgeManagerRegistry().getShadowKnowledgeManagers());
			
			Collection<EnsembleInstance> instances = factory.createInstances(container);
			
			for(EnsembleInstance instance : instances) {
				instance.performKnowledgeExchange();
			}
			container.commitChanges();
		} catch (KnowledgeContainerException | EnsembleFormationException e) {
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
	
	public EnsembleFactory getFactory() {
		return factory;
	}
}
