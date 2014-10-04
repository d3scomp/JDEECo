/**
 * 
 */
package cz.cuni.mff.d3s.deeco.simulation;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.network.AbstractHost;
import cz.cuni.mff.d3s.deeco.network.KnowledgeData;
import cz.cuni.mff.d3s.deeco.network.KnowledgeDataReceiver;
import cz.cuni.mff.d3s.deeco.simulation.scheduler.SimulationScheduler;
import cz.cuni.mff.d3s.deeco.simulation.task.KnowledgeUpdateTask;
import cz.cuni.mff.d3s.deeco.simulation.task.TimerTask;

/**
 * @author Michal
 * 
 */
public class DelayedKnowledgeDataHandler extends NetworkKnowledgeDataHandler implements TimerTaskListener {

	private final Map<String, SimulationScheduler> schedulers;
	private final long delay;
	
	
	public DelayedKnowledgeDataHandler(long delay) {
		this.delay = delay;
		this.schedulers = new HashMap<>();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.network.KnowledgeDataSender#broadcastKnowledgeData
	 * (java.util.List)
	 */
	@Override
	public void networkBroadcast(AbstractHost from, List<? extends KnowledgeData> knowledgeData, Collection<KnowledgeDataReceiver> receivers) {
		SimulationScheduler scheduler = schedulers.get(from.getHostId());
		//System.out.println("Broadcast request: " + from.getHostId() + " at " + scheduler.getCurrentMilliseconds());
		scheduler.addTask(new KnowledgeUpdateTask(scheduler, this, from.getHostId(), knowledgeData, receivers, delay));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.network.KnowledgeDataSender#sendKnowledgeData(java
	 * .util.List, java.lang.String)
	 */
	@Override
	public void  networkSend(AbstractHost from, List<? extends KnowledgeData> knowledgeData, KnowledgeDataReceiver recipient) {
		SimulationScheduler scheduler = schedulers.get(from.getHostId());
		scheduler.addTask(new KnowledgeUpdateTask(scheduler, this, knowledgeData, Arrays.asList(recipient), delay));
	}

	@Override
	public void at(long time, Object triger) {
		KnowledgeUpdateTask task = (KnowledgeUpdateTask) triger;
		if (task.getFrom() != null) {
			SimulationScheduler scheduler = schedulers.get(task.getFrom());
			//System.out.println("Broadcasting: " + task.getFrom() + " at " + scheduler.getCurrentMilliseconds());
		}
		for (KnowledgeDataReceiver receiver : task.getReceivers()) {
			receiver.receive(task.getKnowledgeData());
		}
	}

	@Override
	public TimerTask getInitialTask(SimulationScheduler scheduler) {
		schedulers.put(scheduler.getHost().getHostId(), scheduler);
		return null;
	}
}
