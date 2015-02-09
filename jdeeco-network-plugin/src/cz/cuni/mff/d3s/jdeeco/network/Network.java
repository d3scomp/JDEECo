package cz.cuni.mff.d3s.jdeeco.network;

import cz.cuni.mff.d3s.jdeeco.network.l1.L1Strategy;
import cz.cuni.mff.d3s.jdeeco.network.l1.Layer1;
import cz.cuni.mff.d3s.jdeeco.network.l2.L2Strategy;
import cz.cuni.mff.d3s.jdeeco.network.l2.Layer2;

/**
 * Provides network plug-in for jDEECO
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class Network {
	private Layer1 l1;
	private Layer2 l2;

	/**
	 * Registers L2 strategy for processing L2 packets
	 * 
	 * @param strategy
	 *            Strategy to register
	 */
	public void registerL2Strategy(L2Strategy strategy) {
		l2.registerStrategy(strategy);
	}

	/**
	 * Registers network device
	 * 
	 * @param device
	 *            Network device to register
	 */
	public void registerDevice(Device device) {
		l1.registerDevice(device);
	}

	/**
	 * Processes packet from network device
	 * 
	 * @param l0Packet
	 *            Packet received by device
	 * @param srcAddress
	 *            Source address
	 */
	public void processDevicePacket(byte[] l0Packet, Address srcAddress) {
		// l1.processL0Packet(l0Packet, srcAddress);
		throw new UnsupportedOperationException();
	}

	/**
	 * Registers L1 strategy for processing L1 packets
	 * 
	 * @param strategy
	 *            Strategy to register
	 */
	public void registerL1Strategy(L1Strategy strategy) {
		l1.registerStrategy(strategy);
	}
}
