package cz.cuni.mff.d3s.jdeeco.network.omnet;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.jdeeco.network.address.Address;
import cz.cuni.mff.d3s.jdeeco.network.address.MANETBroadcastAddress;
import cz.cuni.mff.d3s.jdeeco.network.l1.ReceivedInfo;

public class OMNeTBroadcastDevice extends OMNeTDevice {
	public OMNeTBroadcastDevice(/* , address, frequency, ... */) {
	}

	@Override
	public int getMTU() {
		// TODO: Read from omnet
		return 128;
	}

	@Override
	public boolean canSend(Address address) {
		return address instanceof MANETBroadcastAddress;
	}

	@Override
	public void send(byte[] data, Address address) {
		if(!(address instanceof MANETBroadcastAddress)) {
			throw new UnsupportedOperationException();
		}
		System.out.println("Sending broadcast packet, from host " + host.getId());
		host.sendBroadcastPacket(data);
	}

	@Override
	public void init(DEECoContainer container) {
		super.init(container);
		host.setBroadcastDevice(this);
	}
	
	public void receivePacket(byte[] data, double rssi) {
		System.out.println("Received broadcast packet, rssi: " + rssi + " for host " + host.getId());
		network.getL1().processL0Packet(data, this, new ReceivedInfo(MANETBroadcastAddress.INSTANCE));
	}
}
