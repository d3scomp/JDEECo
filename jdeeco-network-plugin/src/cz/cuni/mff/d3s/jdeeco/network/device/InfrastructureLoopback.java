package cz.cuni.mff.d3s.jdeeco.network.device;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.address.Address;
import cz.cuni.mff.d3s.jdeeco.network.address.IPAddress;
import cz.cuni.mff.d3s.jdeeco.network.l1.Layer1;
import cz.cuni.mff.d3s.jdeeco.network.l1.ReceivedInfo;

/**
 * Infrastructure loop-back plug-in
 * 
 * Can be initialized by more DEECo run-times at the same time. Packets send are then instantly delivered to destination
 * loop device.
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class InfrastructureLoopback implements DEECoPlugin {
	/**
	 * Loop device used to provide broadcast device to layer 1
	 */
	class LoopDevice extends Device {
		public Layer1 layer1;
		public IPAddress address;

		private String id;

		public LoopDevice(String id, IPAddress address, Layer1 layer1) {
			this.id = id;
			this.address = address;
			this.layer1 = layer1;
		}

		@Override
		public String getId() {
			return id;
		}

		@Override
		public int getMTU() {
			return 128;
		}

		@Override
		public boolean canSend(Address address) {
			return address instanceof IPAddress;
		}

		@Override
		public void send(byte[] data, Address destination) {
			IPAddress ipAddress = (IPAddress) (destination);
			InfrastructureLoopback.this.route(data, this, ipAddress);
		}
	}

	// Layers this device is registered with
	private Map<IPAddress, LoopDevice> loops = new HashMap<>();

	/**
	 * Routes packet to matching loop device
	 * 
	 * @param data
	 *            Packet data
	 * @param source
	 *            Source loop device
	 * @param destination
	 *            Destination IP address
	 */
	public void route(byte[] data, LoopDevice source, IPAddress destination) {
		LoopDevice loop = loops.get(destination);
		if (loop != null) {
			loop.layer1.processL0Packet(data, source, new ReceivedInfo(source.address));
		}
	}

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return Arrays.asList(Network.class);
	}

	@Override
	public void init(DEECoContainer container) {
		Layer1 l1 = container.getPluginInstance(Network.class).getL1();
		String name = String.valueOf(container.getId());
		IPAddress address = new IPAddress(name);
		LoopDevice loop = new LoopDevice(name, address, l1);
		l1.registerDevice(loop);
		loops.put(address, loop);
	}
}
