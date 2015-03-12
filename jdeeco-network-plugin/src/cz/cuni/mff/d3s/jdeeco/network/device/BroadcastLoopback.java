package cz.cuni.mff.d3s.jdeeco.network.device;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.task.CustomStepTask;
import cz.cuni.mff.d3s.deeco.task.Task;
import cz.cuni.mff.d3s.deeco.task.TimerTask;
import cz.cuni.mff.d3s.deeco.task.TimerTaskListener;
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
	final long constantDelay;

	Scheduler scheduler;

	// Layers this device is registered with
	private Set<LoopDevice> loops = new HashSet<>();

	/**
	 * Loop device used to provide broadcast device to layer 1
	 */
	private class LoopDevice extends Device {
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
			Task task = new CustomStepTask(scheduler, new DeliveryTask(constantDelay, new PacketPackage(data, this)));
			BroadcastLoopback.this.scheduler.addTask(task);
		}
	}

	/**
	 * Packet package used to carry information about packet
	 * 
	 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
	 *
	 */
	private class PacketPackage {
		public byte[] data;
		public LoopDevice source;

		PacketPackage(byte[] data, LoopDevice source) {
			this.data = data;
			this.source = source;
		}
	}

	/**
	 * Task used to delayed delivery of data
	 * 
	 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
	 *
	 */
	private class DeliveryTask implements TimerTaskListener {
		final private PacketPackage packet;

		public DeliveryTask(long delay, PacketPackage packet) {
			this.packet = packet;
		}

		@Override
		public void at(long time, Object triger) {
			BroadcastLoopback.this.sendToAll(packet);
		}

		@Override
		public TimerTask getInitialTask(Scheduler scheduler) {
			return null;
		}
	}

	/**
	 * Constructs loop-back broadcast
	 * 
	 * @param constantDelay
	 *            Delay between sending and delivering the packets
	 */
	public BroadcastLoopback(long constantDelay) {
		this.constantDelay = constantDelay;
	}

	/**
	 * Constructs loop-back broadcast
	 * 
	 * Delivers packets immediately
	 */
	public BroadcastLoopback() {
		this(0);
	}

	/**
	 * Sends packet to all registered loop devices
	 * 
	 * @param packet
	 *            Container containing packet data and sender information
	 */
	public void sendToAll(PacketPackage packet) {
		for (LoopDevice loop : loops) {
			loop.layer1.processL0Packet(packet.data, packet.source, new ReceivedInfo(packet.source.address));
		}
	}

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return Arrays.asList(Network.class);
	}

	@Override
	public void init(DEECoContainer container) {
		scheduler = container.getRuntimeFramework().getScheduler();
		Layer1 l1 = container.getPluginInstance(Network.class).getL1();
		LoopDevice loop = new LoopDevice(String.valueOf(container.getId()), l1);
		l1.registerDevice(loop);
		loops.add(loop);
	}
}
