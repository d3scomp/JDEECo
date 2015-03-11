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
import cz.cuni.mff.d3s.jdeeco.network.l1.ReceivedInfo;

/**
 * Loop-back broadcast plug-in
 * 
 * Can be initialized by more DEECo run-times at the same time. Packets send are then instantly delivered to all of
 * them.
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class BroadcastLoopback implements DEECoPlugin {
	final int PACKET_SIZE = 128;
	
	/**
	 * Loop device used to provide broadcast device to layer 1
	 */
	class LoopDevice extends Device {
		public Layer1 layer1;
		public MANETBroadcastAddress address;
		
		private String id;

		public LoopDevice(String id, Layer1 layer1) {
			this.id = id;
			this.address = new MANETBroadcastAddress(getId());
			this.layer1 = layer1;
		}

		@Override
		public String getId() {
			return id;
		}

		@Override
		public int getMTU() {
			return PACKET_SIZE;
		}

		@Override
		public boolean canSend(Address address) {
			return address instanceof MANETBroadcastAddress;
		}

		@Override
		public void send(byte[] data, Address addressNotUsed) {
			BroadcastLoopback.this.sendToAll(data, this);
		}
	}

	// Layers this device is registered with
	private Set<LoopDevice> loops = new HashSet<>();

	/**
	 * Sends packet to all registered loop devices
	 * 
	 * @param data
	 *            Packet data
	 * @param source
	 *            Source loop device
	 */
	public void sendToAll(byte[] data, LoopDevice source) {
		for (LoopDevice loop : loops) {
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
		LoopDevice loop = new LoopDevice(String.valueOf(container.getId()), l1);
		l1.registerDevice(loop);
		loops.add(loop);
	}
}
