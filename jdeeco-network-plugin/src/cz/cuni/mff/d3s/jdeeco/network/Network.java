package cz.cuni.mff.d3s.jdeeco.network;

import java.util.Collection;

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

//XXX TB: I would not implement the L1DataProcessor, L2PacketSender here. I think it is overengineered.
// I would make this class only to implement the init and getDependencies methods, plus getters for l1 and l2.

public class Network implements NetworkPlugin, L1DataProcessor, L2PacketSender {
	private Layer1 l1;
	private Layer2 l2;

	// Network to gossip interface

	public void processKnowledgeFromGossip(Object knowledge) {
		throw new UnsupportedOperationException();
	}

	
	// XXX TB: I would get rid of the delegators below. It seems overengineered to me. I think we don't need a facade to the network so dearly.
	
	
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
		l1.registerL1Strategy(strategy);
	}

	public Collection<L1Strategy> getRegisteredL1Strategies() {
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
