package cz.cuni.mff.d3s.deeco.simulation;

import static cz.cuni.mff.d3s.deeco.simulation.Simulation.secondsToMilliseconds;
import cz.cuni.mff.d3s.deeco.network.AbstractHost;
import cz.cuni.mff.d3s.deeco.network.DataReceiver;
import cz.cuni.mff.d3s.deeco.network.DataSender;
import cz.cuni.mff.d3s.deeco.scheduler.CurrentTimeProvider;

public class DirectSimulationHost extends AbstractHost implements SimulationTimeEventListenerHolder {
	
	private final NetworkDataHandler handler;
	private SimulationTimeEventListener timeEventListener = null;

	public DirectSimulationHost(String id, CurrentTimeProvider timeProvider, NetworkDataHandler handler) {
		super(id, timeProvider);
		this.handler = handler;
		// TODO Auto-generated constructor stub
	}
	
	public void setSimulationTimeEventListener(
			SimulationTimeEventListener timeEventListener) {
		this.timeEventListener = timeEventListener;
	}
	
	public void at(double absoluteTime) {
		timeEventListener.at(secondsToMilliseconds(absoluteTime));
	}
	
	public String toString() {
		return "Host: " + id;
	}

	@Override
	public DataSender getDataSender() {
		return handler.getDataSender(this);
	}

	@Override
	public void addDataReceiver(DataReceiver dataReceiver) {
		// TODO Auto-generated method stub
		this.handler.addDataReceiver(this, dataReceiver);
		
	}

}
