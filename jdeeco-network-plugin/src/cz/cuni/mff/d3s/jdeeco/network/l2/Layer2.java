package cz.cuni.mff.d3s.jdeeco.network.l2;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import cz.cuni.mff.d3s.jdeeco.network.address.Address;
import cz.cuni.mff.d3s.jdeeco.network.l1.L2PacketSender;
import cz.cuni.mff.d3s.jdeeco.network.marshaller.MarshallerRegistry;

/**
 * Network Layer 2 implementation
 * 
 * The purpose of this layer is to filter complete data belonging to upper layers (for example knowledge) by layer 2
 * strategies. These strategies are responsible for re-broadcasting ad delivering the data to respective upper layers.
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class Layer2 implements L2StrategyManager, L1DataProcessor {
	private final Collection<L2Strategy> strategies = new HashSet<L2Strategy>();
	private final MarshallerRegistry marshallers;
	private L2PacketSender l2Sender;

	/**
	 * Creates L2 layer
	 * 
	 * @param l2Sender
	 *            Reference to implementation of lower layer, that can send the L2 packets produced by Layer 2
	 * 
	 * @param marshallerRegistry
	 *            MarshallerRegistry to be used by L2 and L2 packets
	 */
	public Layer2(MarshallerRegistry marshallerRegistry) {
		marshallers = marshallerRegistry;
	}

	/**
	 * Sets l2 packet sender
	 * 
	 * @param l2packetSender
	 *            packet sender used by L2 to send packets towards L1
	 */
	public void setL2PacketSender(L2PacketSender l2packetSender) {
		this.l2Sender = l2packetSender;
	}

	/**
	 * Gets marshaler registry for this L2
	 * 
	 * @return marshaler registry used by layer
	 */
	public MarshallerRegistry getMarshallers() {
		return marshallers;
	}

	/**
	 * Processes L2 packet by registered L2 strategies
	 * 
	 * Passing packet from L1 to L2
	 * 
	 * @param packet
	 *            Packet to be processed
	 */
	@Override
	public void processL2Packet(L2Packet packet) {
		// Assign packet to this layer 2 instance
		packet.setLayer(this);

		// Filter packet by L2 strategies
		for (L2Strategy strategy : strategies) {
			strategy.processL2Packet(packet);
		}
	}

	/**
	 * Sends L2 Packet to L1
	 * 
	 * Passing packet from L2 to L1
	 * 
	 * @param packet
	 *            Packet to be sent
	 * @param address
	 *            destination address for packet
	 */
	public void sendL2Packet(L2Packet packet, Address address) {
		// Associate L2 packet with this L2 instance
		packet.setLayer(this);

		// Pass packet to lower layer
		l2Sender.sendL2Packet(packet, address);
	}

	/**
	 * Registers L2 strategy for processing L2 packets
	 * 
	 * @param strategy
	 *            Strategy to register
	 */
	public void registerL2Strategy(L2Strategy strategy) {
		strategies.add(strategy);
	}

	/**
	 * Gets registered strategies
	 * 
	 * @return Read-only collection of registered strategies
	 */
	public Collection<L2Strategy> getRegisteredL2Strategies() {
		return Collections.unmodifiableCollection(strategies);
	}

	/**
	 * Removes strategy
	 * 
	 * @param strategy
	 *            Strategy to remove
	 */
	public boolean unregisterL2Strategy(L2Strategy strategy) {
		return strategies.remove(strategy);
	}
}
