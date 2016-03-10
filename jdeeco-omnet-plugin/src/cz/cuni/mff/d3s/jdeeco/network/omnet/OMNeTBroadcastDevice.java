package cz.cuni.mff.d3s.jdeeco.network.omnet;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.jdeeco.network.address.Address;
import cz.cuni.mff.d3s.jdeeco.network.address.MANETBroadcastAddress;
import cz.cuni.mff.d3s.jdeeco.network.l1.MANETReceivedInfo;
import cz.cuni.mff.d3s.jdeeco.position.PositionPlugin;

public class OMNeTBroadcastDevice extends OMNeTDevice {
	public PositionPlugin positionPlugin;
	
	@Override
	public int getMTU() {
		// TODO: Read from OMNeT
		return 1024;
	}

	@Override
	public boolean canSend(Address address) {
		return address instanceof MANETBroadcastAddress;
	}

	@Override
	public void send(byte[] data, Address address) {
		if (!(address instanceof MANETBroadcastAddress)) {
			throw new UnsupportedOperationException();
		}
		host.sendBroadcastPacket(data);
	}
	
	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		// Require base dependencies + position
		List<Class<? extends DEECoPlugin>> dependencies = new LinkedList<>();
		dependencies.addAll(super.getDependencies());
		dependencies.add(PositionPlugin.class);
		return dependencies;
	}

	@Override
	public void init(DEECoContainer container) {
		super.init(container);
		host.setBroadcastDevice(this);
		positionPlugin = container.getPluginInstance(PositionPlugin.class);
	}
	
	public void receivePacket(byte[] data, double rssi) {
		// TODO: Get source address from OMNeT
		Address source = MANETBroadcastAddress.BROADCAST;
		
		network.getL1().processL0Packet(data, this, new MANETReceivedInfo(source, rssi));
	}
}
