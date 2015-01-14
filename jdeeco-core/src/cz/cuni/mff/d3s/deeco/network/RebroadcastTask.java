package cz.cuni.mff.d3s.deeco.network;

import cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.task.Task;
import cz.cuni.mff.d3s.deeco.task.TaskInvocationException;

public class RebroadcastTask extends Task {

	private final TimeTrigger trigger;
	private final KnowledgeMetaData dataToRebroadcast;
	private final KnowledgeDataPublisher publisher;
	private final NICType nicType;
	
	public RebroadcastTask(Scheduler scheduler, KnowledgeDataPublisher  publisher, int rebroadcastAfter, KnowledgeMetaData metadata, NICType nicType) {
		super(scheduler);
		this.dataToRebroadcast = metadata;
		this.publisher = publisher;
		this.nicType = nicType;
		
		trigger = RuntimeMetadataFactoryExt.eINSTANCE.createTimeTrigger();
		trigger.setPeriod(0);
		trigger.setOffset(rebroadcastAfter);
	}

	@Override
	public void invoke(Trigger trigger) throws TaskInvocationException {
		publisher.rebroacast(dataToRebroadcast, nicType);
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.task.Task#registerTriggers()
	 */
	@Override
	protected void registerTriggers() {
		/**
		 * There are no triggers as it is assumed that publishing occurs purely periodically.
		 */
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.task.Task#unregisterTriggers()
	 */
	@Override
	protected void unregisterTriggers() {
		/**
		 * There are no triggers as it is assumed that publishing occurs purely periodically.
		 */
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.task.Task#getPeriodicTrigger()
	 */
	@Override
	public TimeTrigger getTimeTrigger() {
		return trigger;
	}

}

