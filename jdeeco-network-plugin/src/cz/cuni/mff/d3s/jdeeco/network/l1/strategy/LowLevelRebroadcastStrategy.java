package cz.cuni.mff.d3s.jdeeco.network.l1.strategy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.task.TimerTask;
import cz.cuni.mff.d3s.deeco.task.TimerTaskListener;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.address.MANETBroadcastAddress;
import cz.cuni.mff.d3s.jdeeco.network.l1.L1Packet;
import cz.cuni.mff.d3s.jdeeco.network.l1.L1Strategy;
import cz.cuni.mff.d3s.jdeeco.network.l1.Layer1;
import cz.cuni.mff.d3s.jdeeco.network.l1.MANETReceivedInfo;
import cz.cuni.mff.d3s.jdeeco.network.utils.LimitedSortedSet;

/**
 * Simple low-level rebroadcast
 * 
 * This rebroadcasts packets received from wireless MANET device. It uses RSSI to calculate rebroadcast time,
 * rebroadcast is performed with delay dependent on RSSI. Packets already seen are not processed again. Whether the
 * packet has been seen is determined by history of limited size. If the packet is older than first record in history
 * then it is considered seen.
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class LowLevelRebroadcastStrategy implements DEECoPlugin, L1Strategy {
	// Base for rebroadcast delay
	private static final long DELAY = 1500;

	// L1 packet history limit per source node
	private static final int HISTORY_LIMIT = 32;

	private Layer1 layer1;
	private Scheduler scheduler;
	
	/**
	 * Map of known packets
	 * 
	 * SourceNode -> History(datatId, StartPos)
	 */
	private Map<Byte, LimitedSortedSet<L1PacketKey>> known = new HashMap<>();

	@Override
	public void processL1Packet(L1Packet packet) {
		// Rebroadcast packet only if received from MANET
		if (!(packet.receivedInfo instanceof MANETReceivedInfo))
			return;

		// Skip rebroadcast if packet is already known to us
		if (shallDrop(packet))
			return;

		// Calculate rebroadcast delay
		MANETReceivedInfo info = (MANETReceivedInfo) packet.receivedInfo;
		long delayMs = (long) ((1 - info.rssi) * DELAY);

		scheduleRebroadcast(packet, delayMs);
	}

	/**
	 * Tests whether we shall drop a packet without rebroadcast
	 * 
	 * @param packet
	 * @return
	 */
	private boolean shallDrop(L1Packet packet) {
		if (!known.containsKey(packet.srcNode))
			return false;

		LimitedSortedSet<L1PacketKey> set = known.get(packet.srcNode);
		L1PacketKey info = new L1PacketKey(packet);

		// Skip packets that are older than oldest packets from history
		if (set.first().compareTo(info) > 0) {
			return true;
		}

		// If packet is in history range then skip those already in history
		return set.contains(info);
	}

	/**
	 * Make packet known by history
	 * 
	 * Used to decide whether to drop packet without rebroadcast
	 * 
	 * @param packet
	 */
	private void makeKnown(L1Packet packet) {
		if (!known.containsKey(packet.srcNode)) {
			known.put(packet.srcNode, new LimitedSortedSet<>(HISTORY_LIMIT));
		}
		known.get(packet.srcNode).add(new L1PacketKey(packet));
	}

	/**
	 * Schedule packet for rebroadcast
	 * 
	 * @param packet
	 *            Packet to rebroadcast
	 * @param delayMs
	 *            Rebroadcast delay
	 */
	private void scheduleRebroadcast(L1Packet packet, long delayMs) {
		// Schedule delayed rebroadcast
		makeKnown(packet);
		new TimerTask(scheduler, new Rebroadcast(packet), delayMs).schedule();
	}

	/**
	 * Perform the rebroadcast action
	 * 
	 * @param packet
	 *            Packet to rebroadcast
	 */
	private void doRebroadcast(L1Packet packet) {
		layer1.sendL1Packet(packet, MANETBroadcastAddress.BROADCAST);
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
	}
}

/**
 * Information about L1 packets
 * 
 * Used to distinguish L1 packet without storing extra information
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
class L1PacketKey implements Comparable<L1PacketKey> {
	public final int dataId;
	public final int startPos;

	public L1PacketKey(L1Packet packet) {
		dataId = packet.dataId;
		startPos = packet.startPos;
	}

	public L1PacketKey(int dataId, int startPos) {
		this.dataId = dataId;
		this.startPos = startPos;
	}

	@Override
	public boolean equals(Object obj) {
		return Objects.deepEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataId, startPos);
	}

	@Override
	public int compareTo(L1PacketKey o) {
		if (dataId < o.dataId) {
			return -1;
		} else if (dataId > o.dataId) {
			return 1;
		} else if (startPos < o.startPos) {
			return -1;
		} else if (startPos > o.startPos) {
			return 1;
		} else {
			return 0;
		}
	}
}