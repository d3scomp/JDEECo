package cz.cuni.mff.d3s.jdeeco.network.omnet;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.jdeeco.network.address.Address;
import cz.cuni.mff.d3s.jdeeco.network.address.IPAddress;
import cz.cuni.mff.d3s.jdeeco.network.l1.ReceivedInfo;

public class OMNeTInfrastructureDevice extends OMNeTDevice {
	public final IPAddress address;
	
	public OMNeTInfrastructureDevice(IPAddress address /* , virtual lan, ... */) {
		this.address = address;
	}

	@Override
	public String getId() {
		return String.format("%d (%s)", host.getId(), address);
	}

	@Override
	public int getMTU() {
		// TODO: Get this from OMNeT
		return 1500;
	}

	@Override
	public boolean canSend(Address address) {
		return address instanceof IPAddress;
	}

	@Override
	public void send(byte[] data, Address address) {
		if(!(address instanceof IPAddress)) {
			throw new UnsupportedOperationException();
		}
		System.out.println("Sending ip packet, from host " + getId() + " to " + address);
		host.sendInfrastructurePacket(data, (IPAddress) address);
	}

	@Override
	public void init(DEECoContainer container) {
		super.init(container);
		host.setInfrastructureDevice(this);
	}
	
	public void receivePacket(byte[] data, int sender) {
		// TODO: Get real IP address from OMNeT++ instead of just sender id 
		System.out.println("Received infrastructure packet");
		network.getL1().processL0Packet(data, this, new ReceivedInfo(new IPAddress("UNKNOWN")));
	}
}
