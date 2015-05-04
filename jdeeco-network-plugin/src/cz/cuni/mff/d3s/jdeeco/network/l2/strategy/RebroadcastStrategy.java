package cz.cuni.mff.d3s.jdeeco.network.l2.strategy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cz.cuni.mff.d3s.deeco.annotations.CommunicationBoundary;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;
import cz.cuni.mff.d3s.deeco.network.CommunicationBoundaryPredicate;
import cz.cuni.mff.d3s.deeco.network.KnowledgeData;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.task.CustomStepTask;
import cz.cuni.mff.d3s.deeco.task.TimerTask;
import cz.cuni.mff.d3s.deeco.task.TimerTaskListener;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.address.MANETBroadcastAddress;
import cz.cuni.mff.d3s.jdeeco.network.l1.L1Packet;
import cz.cuni.mff.d3s.jdeeco.network.l1.MANETReceivedInfo;
import cz.cuni.mff.d3s.jdeeco.network.l2.L2Packet;
import cz.cuni.mff.d3s.jdeeco.network.l2.L2Strategy;
import cz.cuni.mff.d3s.jdeeco.network.l2.Layer2;
import cz.cuni.mff.d3s.jdeeco.network.utils.LimitedSortedSet;
import cz.cuni.mff.d3s.jdeeco.network.utils.TimeSorted;

/**
 * Network Layer 2 rebroadcast strategy
 * 
 * Using RSSSI and delay to determine when to rebroadcast. Already known packets are not processed due to limited history
 * of seen packets.
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class RebroadcastStrategy implements DEECoPlugin, L2Strategy {
	// Base for rebroadcast delay
	private static final long DELAY = 1500;

	// L1 packet history limit per source node
	private static final int HISTORY_LIMIT = 32;

	private Layer2 layer2;
	private Scheduler scheduler;
	private List<EnsembleDefinition> ensembleDefinitions;

	/**
	 * Map of known packets
	 * 
	 * SourceNode -> History(datatId, StartPos)
	 */
	private Map<Byte, LimitedSortedSet<L2PacketInfo>> known = new HashMap<>();

	@Override
	public void processL2Packet(L2Packet packet) {
		// Get average RSSI
		double rssiVal = 0;
		int rssiCnt = 0;
		for (L1Packet l1 : packet.receivedInfo.srcFragments) {
			if (l1.receivedInfo instanceof MANETReceivedInfo) {
				MANETReceivedInfo info = (MANETReceivedInfo) l1.receivedInfo;
				rssiVal += info.rssi;
				rssiCnt++;
			}
		}

		// Skip packets formed completely by IP packets
		if (rssiCnt == 0)
			return;

		// Skip rebroadcast if packet is already known to us
		if (shallDrop(packet))
			return;
		
		// If the packet is blocked by communication boundary
		if(isBounded(packet))
			return;

		// Calculate rebroadcast delay
		double rssi = rssiVal / rssiCnt;
		long delayMs = (long) ((1 - rssi) * DELAY);
		
		scheduleRebroadcast(packet, delayMs);
	}

	/**
	 * Tests whether we shall drop a packet without rebroadcast
	 * 
	 * @param packet
	 * @return
	 */
	private boolean shallDrop(L2Packet packet) {
		if (!known.containsKey(packet.receivedInfo.srcNode))
			return false;

		LimitedSortedSet<L2PacketInfo> set = known.get(packet.receivedInfo.srcNode);
		L2PacketInfo info = new L2PacketInfo(packet);

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
	private void makeKnown(L2Packet packet) {
		if (!known.containsKey(packet.receivedInfo.srcNode)) {
			known.put(packet.receivedInfo.srcNode, new LimitedSortedSet<>(HISTORY_LIMIT));
		}
		known.get(packet.receivedInfo.srcNode).add(new L2PacketInfo(packet));
	}

	/**
	 * Schedule packet for rebroadcast
	 * 
	 * @param packet
	 *            Packet to rebroadcast
	 * @param delayMs
	 *            Rebroadcast delay
	 */
	private void scheduleRebroadcast(L2Packet packet, long delayMs) {
		// Schedule delayed rebroadcast
		makeKnown(packet);
		new CustomStepTask(scheduler, new Rebroadcast(packet), delayMs).schedule();
	}

	/**
	 * Perform the rebroadcast action
	 * 
	 * @param packet
	 *            Packet to rebroadcast
	 */
	private void doRebroadcast(L2Packet packet) {
		layer2.sendL2Packet(packet, MANETBroadcastAddress.BROADCAST);
	}
	
	private boolean isBounded(L2Packet packet) {
		Object payload = packet.getObject();
		
		// Non knowledge data are not blocked by bound
		if(!(payload instanceof KnowledgeData)) 
			return false;
		
		KnowledgeData knowledgeData = (KnowledgeData) payload;
		
		for (EnsembleDefinition ens: ensembleDefinitions) {
			CommunicationBoundaryPredicate boundary = ens.getCommunicationBoundary();
		
			// null boundary condition counts as a satisfied one
			if (boundary == null || boundary.eval(knowledgeData, null /*sender*/)) {
				return false;
			}
		}
		
		return true;
	}

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return Arrays.asList(Network.class);
	}

	@Override
	public void init(DEECoContainer container) {
		// Resolve dependencies
		Network network = container.getPluginInstance(Network.class);
		layer2 = network.getL2();
		scheduler = container.getRuntimeFramework().getScheduler();
		ensembleDefinitions = container.getRuntimeMetadata().getEnsembleDefinitions();

		// Register this as L2 strategy
		layer2.registerL2Strategy(this);
	}

	/**
	 * Holds information about packet to be rebroadcast and serves as rebroadcast task listener
	 */
	private class Rebroadcast implements TimerTaskListener {
		private L2Packet packet;

		public Rebroadcast(L2Packet packet) {
			this.packet = packet;
		}

		@Override
		public void at(long time, Object triger) {
			RebroadcastStrategy.this.doRebroadcast(packet);
		}

		@Override
		public TimerTask getInitialTask(Scheduler scheduler) {
			return null;
		}
	}
}

/**
 * Info about L2 packet
 * 
 * Used to identify L2 packets without storing unnecessary information
 */
class L2PacketInfo implements TimeSorted<L2PacketInfo> {
	public final int dataId;

	public L2PacketInfo(final L2Packet packet) {
		dataId = packet.receivedInfo.dataId;
	}

	@Override
	public boolean equals(Object obj) {
		return Objects.deepEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return dataId;
	}

	@Override
	public int compareTo(L2PacketInfo o) {
		return ((Integer) dataId).compareTo(o.dataId);
	}
}
