package cz.cuni.mff.d3s.jdeeco.network.device;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
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
import cz.cuni.mff.d3s.jdeeco.network.l1.MANETReceivedInfo;
import cz.cuni.mff.d3s.jdeeco.position.Position;
import cz.cuni.mff.d3s.jdeeco.position.PositionPlugin;
import cz.cuni.mff.d3s.jdeeco.position.PositionProvider;

/**
 * Simple broadcast device plug-in
 * 
 * Can be initialized by more DEECo run-times at the same time. Packets send are then delivered to the group.
 * 
 * Packet delivery can be parameterized with mean and deviation. When not specified the packets are delivered
 * immediately.
 * 
 * Range can be also parameterized. By default the range is 250 meters. The received packets have RSSi set depending on
 * the distance between sender and receiver.
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class SimpleBroadcastDevice implements DEECoPlugin {
	public static final int PACKET_SIZE = 128; // bytes
	public static final int DEFAULT_RANGE = 250; // meters
	public static final long DEFAULT_DELAY_MEAN = 0; // ms
	public static final long DEFAULT_DELAY_VARIANCE = 0; // ms
	final long delayMean;
	final long delayDeviation;
	final int range;
	private Random random;

	Scheduler scheduler;

	// Loop devices this loop-back network is registered with
	private Set<LoopDevice> loops = new HashSet<>();

	/**
	 * Loop device used to provide broadcast device to layer 1
	 */
	private class LoopDevice extends Device {
		final Layer1 layer1;
		final MANETBroadcastAddress address;
		final DEECoContainer container;
		private PositionProvider positionProvider;

		public LoopDevice(DEECoContainer container) {
			this.container = container;
			address = new MANETBroadcastAddress(getId());
			layer1 = container.getPluginInstance(Network.class).getL1();
			positionProvider = container.getPluginInstance(PositionPlugin.class);
		}

		public Position getPosition() {
			if (positionProvider == null) {
				return null;
			}

			return positionProvider.getPosition();
		}

		@Override
		public String getId() {
			return String.valueOf(container.getId());
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
			// Get task delay
			long delay = (long) (random.nextGaussian()) * delayDeviation + delayMean;

			// Schedule send task
			Task task = new CustomStepTask(scheduler, new DeliveryListener(new PacketWrapper(data, this)), delay);
			SimpleBroadcastDevice.this.scheduler.addTask(task);
		}
	}

	/**
	 * Packet package used to carry information about packet
	 */
	private final class PacketWrapper {
		public final byte[] data;
		public final LoopDevice source;

		PacketWrapper(byte[] data, LoopDevice source) {
			this.data = data;
			this.source = source;
		}
	}

	/**
	 * Listener used to delayed delivery of data
	 */
	private class DeliveryListener implements TimerTaskListener {
		final private PacketWrapper packet;

		public DeliveryListener(PacketWrapper packet) {
			this.packet = packet;
		}

		@Override
		public void at(long time, Object triger) {
			SimpleBroadcastDevice.this.sendToAll(packet);
		}

		@Override
		public TimerTask getInitialTask(Scheduler scheduler) {
			return null;
		}
	}

	/**
	 * Constructs loop-back broadcast
	 * 
	 * @param delayMean
	 *            Delay between sending and delivering the packets
	 */
	public SimpleBroadcastDevice(long delayMean, long delayDeviation, int range) {
		this.delayMean = delayMean;
		this.delayDeviation = delayDeviation;
		this.range = range;
	}

	/**
	 * Constructs loop-back broadcast
	 * 
	 * Delivers packets with default values
	 */
	public SimpleBroadcastDevice() {
		this(DEFAULT_DELAY_MEAN, DEFAULT_DELAY_VARIANCE, DEFAULT_RANGE);
	}

	/**
	 * Sends packet to all registered loop devices
	 * 
	 * @param packet
	 *            Container containing packet data and sender information
	 */
	public void sendToAll(PacketWrapper packet) {
		for (LoopDevice loop : loops) {
			Position srcPos = packet.source.getPosition();
			Position dstPos = loop.getPosition();

			// Get distance
			// 0 means that distance is not relevant and packet should be delivered
			double distance = 0;
			if (dstPos != null && srcPos != null) {
				distance = srcPos.euclidDistanceTo(dstPos);
			}

			if (loop != packet.source && distance <= range) {
				// Calculates logarithmic RSSI
				double rssi = Math.log(range - distance) / Math.log(range);

				// Receive packet on the destination node
				MANETReceivedInfo info = new MANETReceivedInfo(packet.source.address, rssi);
				loop.layer1.processL0Packet(packet.data, packet.source, info);
			}
		}
	}

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return Arrays.asList(Network.class);
	}

	@Override
	public void init(DEECoContainer container) {
		random = new Random(container.getId());
		scheduler = container.getRuntimeFramework().getScheduler();
		Layer1 l1 = container.getPluginInstance(Network.class).getL1();
		LoopDevice loop = new LoopDevice(container);
		l1.registerDevice(loop);
		loops.add(loop);
	}
}
