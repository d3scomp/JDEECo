package cz.cuni.mff.d3s.jdeeco.network.omnet;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.jdeeco.network.address.Address;
import cz.cuni.mff.d3s.jdeeco.network.address.MANETBroadcastAddress;
import cz.cuni.mff.d3s.jdeeco.network.l1.MANETReceivedInfo;

public class OMNeTBroadcastDevice extends OMNeTDevice {
	public final Position position;
	
	public OMNeTBroadcastDevice(/* , address, frequency, ... */) {
		position = new Position(0, 0, 0);
	}
	
	public OMNeTBroadcastDevice(int x, int y, int z) {
		this(new Position(x, y, z));
	}
	
	public OMNeTBroadcastDevice(final Position position) {
		this.position = position;
	}

	@Override
	public int getMTU() {
		// TODO: Read from OMNeT
		return 128;
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
	public void init(DEECoContainer container) {
		super.init(container);
		host.setBroadcastDevice(this);
	}

	public void receivePacket(byte[] data, double rssi) {
		// TODO: Get source address from OMNeT
		network.getL1().processL0Packet(data, this, new MANETReceivedInfo(MANETBroadcastAddress.BROADCAST, rssi));
	}
}
