package cz.cuni.mff.d3s.jdeeco.network;

import java.util.Collection;

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

	// Network layer 2 interface
	
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
	 * Gets registered L2 strategies
	 * 
	 * @return Read-only collection of registered strategies
	 */
	public Collection<L2Strategy> getRegisteredL2Strategies() {
		return l2.getRegisteredStrategies();
	}

	/**
	 * Removes L2 strategy
	 * 
	 * @param strategy
	 *            Strategy to remove
	 */
	public boolean unregisterL2Strategy(L2Strategy strategy) {
		return l2.unregisterStrategy(strategy);
	}
	
	
	// Network layer 1 interface

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
	
	/**
	 * Gets registered L1 strategies
	 * 
	 * @return Read-only collection of registered strategies
	 */
	public Collection<L2Strategy> getRegisteredL1Strategies() {
		throw new UnsupportedOperationException();
		//return l1.getRegisteredStrategies();
	}

	/**
	 * Removes L1 strategy
	 * 
	 * @param strategy
	 *            Strategy to remove
	 */
	public boolean unregisterL1Strategy(L1Strategy strategy) {
		throw new UnsupportedOperationException();
		// return l1.unregisterStrategy(strategy);
	}
}
