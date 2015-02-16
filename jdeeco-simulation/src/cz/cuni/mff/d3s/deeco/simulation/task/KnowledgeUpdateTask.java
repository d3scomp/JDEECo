package cz.cuni.mff.d3s.deeco.simulation.task;

import java.util.Collection;

import cz.cuni.mff.d3s.deeco.network.DataReceiver;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.task.TimerTask;
import cz.cuni.mff.d3s.deeco.task.TimerTaskListener;

@SuppressWarnings("rawtypes")
public class KnowledgeUpdateTask extends TimerTask {

	private final Object data;
	private final String from;
	private final Collection<DataReceiver> receivers;
	
	public KnowledgeUpdateTask(Scheduler scheduler,
			TimerTaskListener taskListener, String from, Object data, Collection<DataReceiver> receivers, long delay) {
		super(scheduler, taskListener, delay);
		this.from = from;
		this.receivers = receivers;
		this.data = data;
	}
	
	public KnowledgeUpdateTask(Scheduler scheduler,
			TimerTaskListener taskListener, Object data, Collection<DataReceiver> receivers, long delay) {
		this(scheduler, taskListener, null, data, receivers, delay);
	}

	public Object getData() {
		return data;
	}

	public String getFrom() {
		return from;
	}

	public Collection<DataReceiver> getReceivers() {
		return receivers;
	}
}
