package cz.cuni.mff.d3s.deeco.simulation;

import static cz.cuni.mff.d3s.deeco.simulation.Simulation.secondsToMilliseconds;
import cz.cuni.mff.d3s.deeco.network.AbstractHost;
import cz.cuni.mff.d3s.deeco.network.KnowledgeDataReceiver;
import cz.cuni.mff.d3s.deeco.network.KnowledgeDataSender;
import cz.cuni.mff.d3s.deeco.scheduler.CurrentTimeProvider;

public class DirectSimulationHost extends AbstractHost implements SimulationTimeEventListenerHolder {
	
	private final NetworkKnowledgeDataHandler handler;
	private SimulationTimeEventListener timeEventListener = null;

	public DirectSimulationHost(String id, CurrentTimeProvider timeProvider, NetworkKnowledgeDataHandler handler) {
		super(id, timeProvider);
		this.handler = handler;
		// TODO Auto-generated constructor stub
	}

	@Override
	public KnowledgeDataSender getKnowledgeDataSender() {
		return handler.getKnowledgeDataSender(this);
	}

	@Override
	public void setKnowledgeDataReceiver(
			KnowledgeDataReceiver knowledgeDataReceiver) {
		this.handler.addKnowledgeDataReceiver(this, knowledgeDataReceiver);
	}
	
	public void setSimulationTimeEventListener(
			SimulationTimeEventListener timeEventListener) {
		this.timeEventListener = timeEventListener;
	}
	
	public void at(double absoluteTime) {
		timeEventListener.at(secondsToMilliseconds(absoluteTime));
	}

}
