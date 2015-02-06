package cz.cuni.mff.d3s.jdeeco.network.l2;

import java.util.*;

import cz.cuni.mff.d3s.jdeeco.network.l1.Address;
import cz.cuni.mff.d3s.jdeeco.network.l1.Layer;
import cz.cuni.mff.d3s.jdeeco.network.l2.Strategy;
import cz.cuni.mff.d3s.jdeeco.network.marshaller.MarshallerRegistry;

/**
 * Interface for L2 methods to be called from layer 1 or knowledge management
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class Layer2 {
	private final Collection<Strategy> strategies = new HashSet<Strategy>();
	private final MarshallerRegistry marshallers;
	private final Layer layer1;

	/**
	 * Creates L2 layer
	 * 
	 * @param marshallerRegistry
	 *            MarshallerRegistry to be used by L2 and L2 packets
	 */
	public Layer2(Layer layer1, MarshallerRegistry marshallerRegistry) {
		marshallers = marshallerRegistry;
		this.layer1 = layer1;
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
	 * @param packet
	 *            Packet to be processed
	 */
	public void processL2Packet(L2Packet packet) {
		for (Strategy strategy : strategies) {
			strategy.processL2Packet(packet);
		}
	}

	/**
	 * Sends L2 Packet to L1
	 * 
	 * TODO: Address is L1 specific ???
	 * 
	 * @param packet
	 *            Packet to be sent
	 * @param address
	 *            destination address for packet
	 */
	public void sendL2Packet(L2Packet packet, Address address) {
		// Pass packet to lower layer
		// TODO: Missing the address parameter
		layer1.sendL2Packet(packet);
	}

	/**
	 * Registers L2 strategy for processing L2 packets
	 * 
	 * @param strategy
	 *            Strategy to register
	 */
	public void registerL2Strategy(Strategy strategy) {
		strategies.add(strategy);
	}

	/**
	 * Creates L2 packet from object
	 * 
	 * @param object
	 *            Source object to be stored in packet
	 * @param header
	 *            Packet header
	 */
	public L2Packet createPacket(PacketHeader header, Object object) {
		return new L2Packet(this, header, object);
	}

	/**
	 * Creates L2 packet from binary data
	 * 
	 * @param header
	 *            Packet header
	 * @param data
	 *            Source binary data for object
	 * @param receifedInfo
	 *            Information about received packet
	 */
	public L2Packet createPacket(PacketHeader header, byte[] data, ReceivedInfo receivedInfo) {
		return new L2Packet(this, header, data, receivedInfo);
	}
}
