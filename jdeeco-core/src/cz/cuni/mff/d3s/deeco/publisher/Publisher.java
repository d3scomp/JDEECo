package cz.cuni.mff.d3s.deeco.publisher;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PeriodicTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;

/**
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public class Publisher {

	private final Scheduler scheduler;
	private final List<PublisherTask> publisherTasks;
	private final PeriodicTrigger periodicTrigger;
	private final PacketSender packetSender;

	public Publisher(Scheduler scheduler, long period, PacketSender packetSender) {
		assert scheduler != null;
		
		this.scheduler = scheduler;
		this.publisherTasks = new LinkedList<>();		
		this.periodicTrigger = createPeriodicTrigger(period);
		this.packetSender = packetSender;
	}

	public void addKnowledgeSource(PublisherKnowledgeSource knowledgeSource) {
		if (knowledgeSource != null && findPublisherTaskForKnowledgeSource(knowledgeSource) == null)
			publisherTasks.add(new PublisherTask(scheduler, this,
					knowledgeSource));
	}

	public void removeKnowledgeSource(PublisherKnowledgeSource knowledgeSource) {
		if (knowledgeSource != null) {
			PublisherTask pTask = findPublisherTaskForKnowledgeSource(knowledgeSource);
			if (pTask != null)
				scheduler.removeTask(pTask);
		}
	}

	public PeriodicTrigger getPeriodicTrigger() {
		return periodicTrigger;
	}

	public void publish(String ownerId, ValueSet knowledge) {
		packetSender.sendData(new KnowledgeData(ownerId, knowledge));
	}

	// --------Private Methods-------------

	private PublisherTask findPublisherTaskForKnowledgeSource(
			PublisherKnowledgeSource knowledgeSource) {
		if (knowledgeSource != null)
			for (PublisherTask pTask : publisherTasks)
				if (pTask.getPublisherKnowledgeSource().equals(knowledgeSource))
					return pTask;
		return null;
	}
	
	private PeriodicTrigger createPeriodicTrigger(long period) {
		RuntimeMetadataFactory factory = RuntimeMetadataFactory.eINSTANCE;
		PeriodicTrigger result = factory.createPeriodicTrigger();
		result.setPeriod(period);
		return result;
	}
}
