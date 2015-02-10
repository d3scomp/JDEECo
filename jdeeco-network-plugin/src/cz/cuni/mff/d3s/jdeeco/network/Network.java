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
public class Network implements NetworkToDevice, NetworkToGossip, ILayer1, ILayer2 {
	private Layer1 l1;
	private Layer2 l2;

	// Network to gossip interface

	public void processKnowledgeFromGossip(Object knowledge) {
		throw new UnsupportedOperationException();
	}

	// Network layer 2 interface

	public void registerL2Strategy(L2Strategy strategy) {
		l2.registerStrategy(strategy);
	}

	public Collection<L2Strategy> getRegisteredL2Strategies() {
		return l2.getRegisteredStrategies();
	}

	public boolean unregisterL2Strategy(L2Strategy strategy) {
		return l2.unregisterStrategy(strategy);
	}

	// Network layer 1 interface

	public void registerL1Strategy(L1Strategy strategy) {
		l1.registerStrategy(strategy);
	}

	public Collection<L2Strategy> getRegisteredL1Strategies() {
		throw new UnsupportedOperationException();
		// return l1.getRegisteredStrategies();
	}

	public boolean unregisterL1Strategy(L1Strategy strategy) {
		throw new UnsupportedOperationException();
		// return l1.unregisterStrategy(strategy);
	}

	// Network to device interface

	public void registerDevice(Device device) {
		l1.registerDevice(device);
	}

	public void processDevicePacket(byte[] l0Packet, Address srcAddress) {
		// l1.processL0Packet(l0Packet, srcAddress);
		throw new UnsupportedOperationException();
	}
}
