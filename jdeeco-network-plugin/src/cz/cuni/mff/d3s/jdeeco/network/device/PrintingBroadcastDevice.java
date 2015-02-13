package cz.cuni.mff.d3s.jdeeco.network.device;

import java.util.Arrays;
import java.util.List;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.address.Address;
import cz.cuni.mff.d3s.jdeeco.network.address.MANETBroadcastAddress;

/**
 * Dummy broadcast device
 * 
 * Just prints data it receives to console
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class PrintingBroadcastDevice implements Device, DEECoPlugin {

	@Override
	public String getId() {
		return "dummy";
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
		System.out.println("Broadcast packet:");
		int counter = 0;
		for (byte b : data) {
			if (counter % 16 == 0) {
				if (counter != 0) {
					System.out.println();
				}
				System.out.print(String.format("%02d: ", counter / 16));
			}
			System.out.print(String.format("%02X ", b));
			counter++;
		}
		System.out.println();
		System.out.println();
	}

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return Arrays.asList(Network.class);
	}

	@Override
	public void init(DEECoContainer container) {
		container.getPluginInstance(Network.class).getL1().registerDevice(this);
	}
}
