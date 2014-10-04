package cz.cuni.mff.d3s.deeco.simulation.task;

import java.util.Collection;
import java.util.List;

import cz.cuni.mff.d3s.deeco.network.KnowledgeData;
import cz.cuni.mff.d3s.deeco.network.KnowledgeDataReceiver;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.simulation.TimerTaskListener;

public class KnowledgeUpdateTask extends TimerTask {

	private final List<? extends KnowledgeData> knowledgeData;
	private final String from;
	private final Collection<KnowledgeDataReceiver> receivers;
	
	public KnowledgeUpdateTask(Scheduler scheduler,
			TimerTaskListener taskListener, String from, List<? extends KnowledgeData> knowledgeData, Collection<KnowledgeDataReceiver> receivers, long delay) {
		super(scheduler, taskListener, delay);
		this.from = from;
		this.receivers = receivers;
		this.knowledgeData = knowledgeData;
	}
	
	public KnowledgeUpdateTask(Scheduler scheduler,
			TimerTaskListener taskListener, List<? extends KnowledgeData> knowledgeData, Collection<KnowledgeDataReceiver> receivers, long delay) {
		this(scheduler, taskListener, null, knowledgeData, receivers, delay);
	}

	public List<? extends KnowledgeData> getKnowledgeData() {
		return knowledgeData;
	}

	public String getFrom() {
		return from;
	}

	public Collection<KnowledgeDataReceiver> getReceivers() {
		return receivers;
	}
}
