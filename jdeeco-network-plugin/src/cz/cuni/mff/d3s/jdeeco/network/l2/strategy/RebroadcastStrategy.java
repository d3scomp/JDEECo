package cz.cuni.mff.d3s.jdeeco.network.l2.strategy;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerContainer;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;
import cz.cuni.mff.d3s.deeco.network.CommunicationBoundaryPredicate;
import cz.cuni.mff.d3s.deeco.network.KnowledgeData;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
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
 * Using RSSSI and delay to determine when to rebroadcast. Already known packets are not processed due to limited
 * history of seen packets.
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class RebroadcastStrategy implements DEECoPlugin, L2Strategy {
	// Base for rebroadcast delay
	private static final long MAX_DELAY = 1500;

	// L1 packet history limit per source node
	private static final int HISTORY_LIMIT = 32;

	// OMNeT++ measured RSSI distance dependency
	public static final double RSSI_1m = 1.00e0;
	public static final double RSSI_2m = 1.74e-5;
	public static final double RSSI_5m = 1.76e-6;
	public static final double RSSI_10m = 3.12e-7;
	public static final double RSSI_20m = 5.52e-8;
	public static final double RSSI_50m = 5.59e-9;
	public static final double RSSI_100m = 9.98e-10;
	public static final double RSSI_250m = 1.11e-10;

	private Layer2 layer2;
	private Scheduler scheduler;
	private Collection<EnsembleDefinition> ensembleDefinitions;
	private KnowledgeManagerContainer kmContainer;

	/**
	 * Map of known packets
	 * 
	 * SourceNode -> History(datatId, StartPos)
	 */
	private Map<Byte, LimitedSortedSet<L2PacketInfo>> known = new HashMap<>();

	/**
	 * Processes the L2 packet by rebroadcast strategy
	 * 
	 * The packet is dropped based on its properties. If none of the filters drop the packet it is scheduled for
	 * rebroadcast.
	 */
	@Override
	public void processL2Packet(L2Packet packet) {
		// Get average RSSI
		double rssiSum = 0;
		int pktCnt = 0;
		for (L1Packet l1 : packet.getReceivedInfo().srcFragments) {
			if (l1.receivedInfo instanceof MANETReceivedInfo) {
				MANETReceivedInfo info = (MANETReceivedInfo) l1.receivedInfo;
				rssiSum += info.rssi;
				pktCnt++;
			}
		}
		double rssiAvg = rssiSum / pktCnt;

		// Skip packets formed completely by IP packets
		if (pktCnt == 0)
			return;

		// Skip packet if sender is close to us
		if(rssiAvg > RSSI_10m)
			return;
		
		// Skip rebroadcast if packet is already known to us
		if (shallDrop(packet))
			return;

		// If the packet is blocked by communication boundary
		if (isBounded(packet))
			return;

		// Calculate rebroadcast delay
		double ratio = Math.abs(Math.log(rssiAvg) / Math.log(RSSI_250m));
		long delayMs = 1 + (long) ((1 - ratio) * MAX_DELAY);
		scheduleRebroadcast(packet, delayMs);
	}

	/**
	 * Tests whether we shall drop a packet without rebroadcast
	 * 
	 * This tests whether the packet was already processed in the past. The test is approximation with limited history.
	 * 
	 * @param packet
	 *            Packet to inspect
	 * @return Whether we should drop packet as it was already processed.
	 */
	private boolean shallDrop(L2Packet packet) {
		if (!known.containsKey(packet.getReceivedInfo().srcNode))
			return false;

		LimitedSortedSet<L2PacketInfo> set = known.get(packet.getReceivedInfo().srcNode);
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
		if (!known.containsKey(packet.getReceivedInfo().srcNode)) {
			known.put(packet.getReceivedInfo().srcNode, new LimitedSortedSet<>(HISTORY_LIMIT));
		}
		known.get(packet.getReceivedInfo().srcNode).add(new L2PacketInfo(packet));
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
		// Do not rebroadcast the same packet again
		makeKnown(packet);
		// Schedule delayed rebroadcast
		new Rebroadcast(packet, delayMs);
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

	/**
	 * Check whether the packet is blocked by boundary condition
	 * 
	 * @param packet
	 *            Packet to inspect
	 * @return Whether the packet is blocked by some condition
	 */
	private boolean isBounded(L2Packet packet) {
		Object payload = packet.getObject();

		// Non knowledge data are not blocked by bound
		if (!(payload instanceof KnowledgeData))
			return false;

		KnowledgeData knowledgeData = (KnowledgeData) payload;

		for (EnsembleDefinition ens : ensembleDefinitions) {
			CommunicationBoundaryPredicate boundary = ens.getCommunicationBoundary();

			// null boundary condition counts as a satisfied one
			if (boundary == null || boundary.eval(knowledgeData, getNodeKnowledge())) {
				return false;
			}
		}

		// When ensemble definition were empty then relay packet
		return !ensembleDefinitions.isEmpty();
	}

	/**
	 * Gets the knowledge of the local node
	 * 
	 * @return
	 */
	private KnowledgeManager getNodeKnowledge() {
		// FIXME: in the future, we need to unify the knowledge of all the local KMs.
		return kmContainer.getLocals().iterator().next();
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
		kmContainer = container.getRuntimeFramework().getContainer();

		// Register this as L2 strategy
		layer2.registerL2Strategy(this);
	}

	/**
	 * Holds information about packet to be rebroadcast and serves as rebroadcast task listener
	 */
	private class Rebroadcast implements TimerTaskListener {
		private final L2Packet packet;
		private final TimerTask rebroadcastTask;

		public Rebroadcast(L2Packet packet, long delayMs) {
			this.packet = packet;
			rebroadcastTask = new TimerTask(scheduler, this, delayMs);
			rebroadcastTask.schedule();
		}

		@Override
		public void at(long time, Object triger) {
			RebroadcastStrategy.this.doRebroadcast(packet);
			rebroadcastTask.unSchedule();
		}
	}

	/**
	 * Sets layer 2
	 * 
	 * To be used in tests
	 * 
	 * @param layer2
	 */
	void setLayer2(Layer2 layer2) {
		this.layer2 = layer2;
	}

	/**
	 * Sets scheduler
	 * 
	 * To be used in tests
	 * 
	 * @param scheduler
	 */
	void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	/**
	 * Sets ensemble definitions
	 * 
	 * To be used in tests
	 * 
	 * @param ensembleDefinitions
	 */
	void setEnsembleDefinitions(Collection<EnsembleDefinition> ensembleDefinitions) {
		this.ensembleDefinitions = ensembleDefinitions;
	}

	/**
	 * Sets KnowledgeManagerContainer
	 * 
	 * To be used in tests
	 * 
	 * @param kmContainer
	 */
	void setKmContainer(KnowledgeManagerContainer kmContainer) {
		this.kmContainer = kmContainer;
	}

	/**
	 * Roughly converts rssi to expected distance
	 * 
	 * @param rssi
	 *            RSSI value
	 * @return Distance in meters
	 */
	double rssiToDist(double rssi) {
		return 10 * Math.exp(-0.34 * rssi);
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
		dataId = packet.getReceivedInfo().dataId;
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
