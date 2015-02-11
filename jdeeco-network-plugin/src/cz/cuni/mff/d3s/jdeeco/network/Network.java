package cz.cuni.mff.d3s.jdeeco.network;

import java.util.Collection;

import cz.cuni.mff.d3s.jdeeco.network.l0.Device;
import cz.cuni.mff.d3s.jdeeco.network.l1.L1Strategy;
import cz.cuni.mff.d3s.jdeeco.network.l1.Layer1;
import cz.cuni.mff.d3s.jdeeco.network.l2.L2Packet;
import cz.cuni.mff.d3s.jdeeco.network.l2.L2Strategy;
import cz.cuni.mff.d3s.jdeeco.network.l2.Layer2;

/**
 * Provides network plug-in for jDEECO
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class Network implements NetworkPlugin, L2PacketProcessor, L2PacketSender {
	private Layer1 l1;
	private Layer2 l2;

	// Network to gossip interface

	public void processKnowledgeFromGossip(Object knowledge) {
		throw new UnsupportedOperationException();
	}

	// Network layer 2 interface

	public void registerL2Strategy(L2Strategy strategy) {
		l2.registerL2Strategy(strategy);
	}

	public Collection<L2Strategy> getRegisteredL2Strategies() {
		return l2.getRegisteredL2Strategies();
	}

	public boolean unregisterL2Strategy(L2Strategy strategy) {
		return l2.unregisterL2Strategy(strategy);
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
	
	// Interlayer interface

	@Override
	public void processL2Packet(L2Packet packet) {
		l2.processL2Packet(packet);		
	}

	@Override
	public boolean sendL2Packet(L2Packet l2Packet, Address address) {
		return l1.sendL2Packet(l2Packet, address);
	}
}
