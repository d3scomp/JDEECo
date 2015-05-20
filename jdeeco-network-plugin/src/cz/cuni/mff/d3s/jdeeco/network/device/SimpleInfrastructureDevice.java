package cz.cuni.mff.d3s.jdeeco.network.device;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.task.CustomStepTask;
import cz.cuni.mff.d3s.deeco.task.TimerTaskListener;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.address.Address;
import cz.cuni.mff.d3s.jdeeco.network.address.IPAddress;
import cz.cuni.mff.d3s.jdeeco.network.l1.Layer1;
import cz.cuni.mff.d3s.jdeeco.network.l1.ReceivedInfo;

/**
 * Simple infrastructure device plug-in
 * 
 * Can be initialized by more DEECo run-times at the same time. Packets send are then delivered (instantly or with
 * delay) to destination loop device.
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class SimpleInfrastructureDevice implements DEECoPlugin {
	/**
	 * Loop device used to provide broadcast device to layer 1
	 */
	private class LoopDevice extends Device {
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
			return PACKET_SIZE;
		}

		@Override
		public boolean canSend(Address address) {
			return address instanceof IPAddress;
		}

		@Override
		public void send(byte[] data, Address destination) {
			if (!(destination instanceof IPAddress)) {
				throw new UnsupportedOperationException("Required destination address is not IPAddress");
			}
			IPAddress ipAddress = (IPAddress) (destination);

			// Schedule packet delivery
			PacketWrapper packet = new PacketWrapper(data, this, ipAddress);
			Scheduler scheduler = SimpleInfrastructureDevice.this.scheduler;
			new DeliveryListener(packet, scheduler);
		}
	}

	/**
	 * Packet source and destination wrapper
	 */
	private final class PacketWrapper {
		public final byte[] data;
		public final LoopDevice sender;
		public final IPAddress destination;

		PacketWrapper(byte[] data, LoopDevice sender, IPAddress destination) {
			this.data = data;
			this.sender = sender;
			this.destination = destination;
		}
	}

	/**
	 * Delayed delivery listener
	 */
	private class DeliveryListener implements TimerTaskListener {
		private final PacketWrapper packet;
		private final CustomStepTask deliveryTask;

		public DeliveryListener(PacketWrapper packet, Scheduler scheduler) {
			this.packet = packet;
			deliveryTask = new CustomStepTask(scheduler, this);
			deliveryTask.schedule();
		}

		@Override
		public void at(long time, Object triger) {
			SimpleInfrastructureDevice.this.route(packet);
			deliveryTask.unSchedule();
		}
	}

	final int PACKET_SIZE = 128;

	final long constantDelay;

	Scheduler scheduler;

	// Layers this device is registered with
	private Map<IPAddress, LoopDevice> loops = new HashMap<>();

	/**
	 * Constructs infrastructure loop-back to maintain constant delay
	 * 
	 * @param constantDelay
	 *            Packet delivery delay
	 */
	public SimpleInfrastructureDevice(long constantDelay) {
		this.constantDelay = constantDelay;
	}

	/**
	 * Constructs infrastructure loop-back with zero delay
	 */
	public SimpleInfrastructureDevice() {
		this(0);
	}

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
	public void route(PacketWrapper packet) {
		LoopDevice loop = loops.get(packet.destination);
		if (loop != null) {
			loop.layer1.processL0Packet(packet.data, packet.sender, new ReceivedInfo(packet.sender.address));
		} else {
			throw new UnsupportedOperationException("Destination address not found in loop network");
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
		String name = String.valueOf(container.getId());
		IPAddress address = new IPAddress(name);
		LoopDevice loop = new LoopDevice(name, address, l1);
		l1.registerDevice(loop);
		loops.put(address, loop);
	}
}
