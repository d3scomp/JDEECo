package cz.cuni.mff.d3s.jdeeco.gossip;

import java.util.Arrays;
import java.util.List;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.task.TimerTask;
import cz.cuni.mff.d3s.deeco.task.TimerTaskListener;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.l2.L2Packet;
import cz.cuni.mff.d3s.jdeeco.network.l2.L2Strategy;

public class DummyKnowledgePublisher implements L2Strategy, DEECoPlugin, TimerTaskListener {
	private Network network;

	@Override
	public void processL2Packet(L2Packet packet) {
		System.out.println("PROCESS CALLED");
	}

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return Arrays.asList(Network.class);
	}

	@Override
	public void init(DEECoContainer container) {
		// Resolve dependencies
		network = container.getPluginInstance(Network.class);

		// Register this as layer 2 strategy (packets will be passed from l2 to here)
		network.getL2().registerL2Strategy(this);
	}

	@Override
	public void at(long time, Object triger) {
		System.out.println("PUBLISHER CALLED");
	}

	@Override
	public TimerTask getInitialTask(Scheduler scheduler) {
		TimerTask task = new TimerTask(scheduler, this) {
		};
		
		return task;
	}
}
