package cz.cuni.mff.d3s.jdeeco.network;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.jdeeco.network.l0.Device;
import cz.cuni.mff.d3s.jdeeco.network.l1.L1Strategy;
import cz.cuni.mff.d3s.jdeeco.network.l1.Layer1;
import cz.cuni.mff.d3s.jdeeco.network.l2.L2Packet;
import cz.cuni.mff.d3s.jdeeco.network.l2.L2ReceivedInfo;
import cz.cuni.mff.d3s.jdeeco.network.l2.L2Strategy;
import cz.cuni.mff.d3s.jdeeco.network.l2.Layer2;

/**
 * Provides network plug-in for jDEECO
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class Network implements NetworkPlugin, L1DataProcessor, L2PacketSender {
	private Layer1 l1;
	private Layer2 l2;

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return new LinkedList<Class<? extends DEECoPlugin>>();
	}

	@Override
	public void init(DEECoContainer container) {
		// TODO Register at Gossip for data
	}

	// Network to gossip interface

	public void processDataFromGossipLayer(Object knowledge) {
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
	public boolean sendL2Packet(L2Packet l2Packet, Address address) {
		return l1.sendL2Packet(l2Packet, address);
	}

	@Override
	public void processL1Data(byte[] data, L2ReceivedInfo receivedInfo) {
		l2.processL1Data(data, receivedInfo);
	}
}
