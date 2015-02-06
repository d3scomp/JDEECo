package cz.cuni.mff.d3s.jdeeco.network.l2;

import cz.cuni.mff.d3s.jdeeco.network.l1.Address;
import cz.cuni.mff.d3s.jdeeco.network.marshaller.MarshallerRegistry;

/**
 * Interface for L2 methods to be called from layer 1 or knowledge management
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class Layer {
	private final MarshallerRegistry marshallers;

	/**
	 * Creates L2 layer
	 * 
	 * @param marshallerRegistry
	 *            MarshallerRegistry to be used by L2 and L2 packets
	 */
	public Layer(MarshallerRegistry marshallerRegistry) {
		marshallers = marshallerRegistry;
	}

	/**
	 * Gets marshaller registry for this L2
	 * 
	 * @return marshaller registry used by layer
	 */
	MarshallerRegistry getMarshallers() {
		return marshallers;
	}

	/**
	 * Processes L2 packet by registered L2 strategies
	 * 
	 * @param packet
	 *            Packet to be processed
	 */
	void processL2Packet(L2Packet packet) {
		throw new UnsupportedOperationException();
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
	void sendL2Packet(L2Packet packet, Address address) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Registers L2 strategy for processing L2 packets
	 * 
	 * @param strategy
	 *            Strategy to register
	 */
	void registerL2Strategy(Strategy strategy) {
		throw new UnsupportedOperationException();
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
	 * @param data
	 *            Source binary data for object
	 * @param header
	 *            Packet header
	 */
	public L2Packet createPacket(PacketHeader header, byte[] data) {
		return new L2Packet(this, header, data);
	}
}
