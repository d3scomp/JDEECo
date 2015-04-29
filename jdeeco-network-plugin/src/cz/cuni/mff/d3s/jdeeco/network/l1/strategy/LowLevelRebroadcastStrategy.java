package cz.cuni.mff.d3s.jdeeco.network.l1.strategy;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.task.CustomStepTask;
import cz.cuni.mff.d3s.deeco.task.Task;
import cz.cuni.mff.d3s.deeco.task.TimerTask;
import cz.cuni.mff.d3s.deeco.task.TimerTaskListener;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.address.MANETBroadcastAddress;
import cz.cuni.mff.d3s.jdeeco.network.l1.L1Packet;
import cz.cuni.mff.d3s.jdeeco.network.l1.L1Strategy;
import cz.cuni.mff.d3s.jdeeco.network.l1.Layer1;
import cz.cuni.mff.d3s.jdeeco.network.l1.MANETReceivedInfo;

/**
 * Simple low-level rebroadcast
 * 
 * This rebroadcasts packets received from wireless MANET device. It uses RSSI to calculate rebroadcast time,
 * rebroadcast is performed with delay dependent on RSSI. While packet is in the queue for rebroadcast the same packets
 * are ignored to avoid congestion.
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class LowLevelRebroadcastStrategy implements DEECoPlugin, L1Strategy {
	private static long DELAY = 1500;
	private Layer1 layer1;
	private Scheduler scheduler;
	private Set<L1Packet> toRebroadcast = new HashSet<>();

	@Override
	public void processL1Packet(L1Packet packet) {
		// Rebroadcast packet only if received from MANET
		if (!(packet.receivedInfo instanceof MANETReceivedInfo))
			return;
		MANETReceivedInfo info = (MANETReceivedInfo) packet.receivedInfo;

		// Calculate rebroadcast delay
		long delayMs = (long) ((1 - info.rssi) * DELAY);

		scheduleRebroadcast(packet, delayMs);
	}

	private void scheduleRebroadcast(L1Packet packet, long delayMs) {
		// Skip rebroadcast if packet is already scheduled
		if (toRebroadcast.contains(packet))
			return;

		// Schedule delayed rebroadcast
		toRebroadcast.add(packet);
		new CustomStepTask(scheduler, new Rebroadcast(packet), delayMs).schedule();
	}

	private void doRebroadcast(L1Packet packet) {
		layer1.sendL1Packet(packet, MANETBroadcastAddress.BROADCAST);
		
		// TODO: We want to remove packets from history
		// toRebroadcast.remove(packet);
	}

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return Arrays.asList(Network.class);
	}

	@Override
	public void init(DEECoContainer container) {
		// Resolve layer1 dependencies
		Network network = container.getPluginInstance(Network.class);
		layer1 = network.getL1();
		scheduler = container.getRuntimeFramework().getScheduler();

		// Register this as layer 1 strategy
		layer1.registerL1Strategy(this);
	}

	/**
	 * Holds information about packet to be rebroadcast and serves as rebroadcast task listener
	 */
	private class Rebroadcast implements TimerTaskListener {
		private L1Packet packet;

		public Rebroadcast(L1Packet packet) {
			this.packet = packet;
		}

		@Override
		public void at(long time, Object triger) {
			LowLevelRebroadcastStrategy.this.doRebroadcast(packet);
		}

		@Override
		public TimerTask getInitialTask(Scheduler scheduler) {
			return null;
		}
	}
}
