package cz.cuni.mff.d3s.deeco.simulation;

import static cz.cuni.mff.d3s.deeco.simulation.Simulation.secondsToMilliseconds;
import cz.cuni.mff.d3s.deeco.network.AbstractHost;
import cz.cuni.mff.d3s.deeco.network.KnowledgeDataReceiver;
import cz.cuni.mff.d3s.deeco.network.KnowledgeDataSender;
import cz.cuni.mff.d3s.deeco.scheduler.CurrentTimeProvider;

public class SimpleSimulationHost extends AbstractHost implements SimulationTimeEventListenerHolder {
	
	private final KnowledgeDataSender sender;
	private KnowledgeDataReceiver receiver;
	private SimulationTimeEventListener timeEventListener = null;

	public SimpleSimulationHost(String id, CurrentTimeProvider timeProvider, KnowledgeDataSender sender) {
		super(id, timeProvider);
		this.sender = sender;
		// TODO Auto-generated constructor stub
	}

	@Override
	public KnowledgeDataSender getKnowledgeDataSender() {
		return sender;
	}

	@Override
	public void setKnowledgeDataReceiver(
			KnowledgeDataReceiver knowledgeDataReceiver) {
		this.receiver = receiver;
	}
	
	public void setSimulationTimeEventListener(
			SimulationTimeEventListener timeEventListener) {
		this.timeEventListener = timeEventListener;
	}
	
	public void at(double absoluteTime) {
		timeEventListener.at(secondsToMilliseconds(absoluteTime));
	}

}
