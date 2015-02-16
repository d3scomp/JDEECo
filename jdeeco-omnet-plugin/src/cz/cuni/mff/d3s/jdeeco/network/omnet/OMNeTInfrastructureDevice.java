package cz.cuni.mff.d3s.jdeeco.network.omnet;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.jdeeco.network.address.Address;
import cz.cuni.mff.d3s.jdeeco.network.address.IPAddress;

public class OMNeTInfrastructureDevice extends OMNeTDevice {
	public OMNeTInfrastructureDevice(/* , address, virtual lan, ... */) {
	}

	@Override
	public String getId() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMTU() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean canSend(Address address) {
		return address instanceof IPAddress;
	}

	@Override
	public void send(byte[] data, Address address) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void init(DEECoContainer container) {
		super.init(container);

		throw new UnsupportedOperationException();
	}
}
