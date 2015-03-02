package cz.cuni.mff.d3s.jdeeco.network.device;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.address.Address;
import cz.cuni.mff.d3s.jdeeco.network.address.MANETBroadcastAddress;
import cz.cuni.mff.d3s.jdeeco.network.l1.Layer1;

/**
 * Loop-back broadcast device
 * 
 * Can be initialized by more DEECo run-times at the same time. Packets send are then instantly delivered to all of
 * them.
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class LoopbackBroadcastDevice extends Device implements DEECoPlugin {
	// Layers this device is registered with
	Set<Layer1> l1Layers = new HashSet<Layer1>();

	@Override
	public String getId() {
		return "loopback";
	}

	@Override
	public int getMTU() {
		// Reasonable MTU for broadcasting device
		return 128;
	}

	@Override
	public boolean canSend(Address address) {
		return address instanceof MANETBroadcastAddress;
	}

	@Override
	public void send(byte[] data, Address address) {
		System.out.println("Sending broadcast packet to all registered L1 layers");
		for (Layer1 layer : l1Layers) {
			System.out.println("	Sending broadcast packet to L1 layers");
			// TODO: Would be nice to know sender address (have a sending layer/network as a parameter)
			// BUG: This is using recipient address, which is not correct
			receive(data, MANETBroadcastAddress.INSTANCE);
		}
	}

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return Arrays.asList(Network.class);
	}

	@Override
	public void init(DEECoContainer container) {
		Layer1 l1 = container.getPluginInstance(Network.class).getL1();
		l1.registerDevice(this);
		l1Layers.add(l1);
	}
}
