package cz.cuni.mff.d3s.jdeeco.ros;

import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.ros.exception.RemoteException;
import org.ros.exception.ServiceNotFoundException;
import org.ros.message.MessageListener;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.service.ServiceClient;
import org.ros.node.service.ServiceResponseListener;
import org.ros.node.topic.Subscriber;

import beeclickarm_messages.IEEE802154BroadcastPacket;
import beeclickarm_messages.IEEE802154BroadcastPacketRequest;
import beeclickarm_messages.IEEE802154BroadcastPacketResponse;
import beeclickarm_messages.IEEE802154ReceivedPacket;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.runtime.PluginInitFailedException;
import cz.cuni.mff.d3s.deeco.timer.Timer;
import cz.cuni.mff.d3s.deeco.timer.TimerEventListener;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.address.Address;
import cz.cuni.mff.d3s.jdeeco.network.address.MANETBroadcastAddress;
import cz.cuni.mff.d3s.jdeeco.network.device.Device;
import cz.cuni.mff.d3s.jdeeco.network.l1.Layer1;

/**
 * The Bee Click (MRF24J40) networking device. This device can be used for DEECo
 * component knowledge broadcasting.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public class BeeClickComm extends TopicSubscriber implements DEECoPlugin {

	/**
	 * The name of ROS service used for broadcasting.
	 */
	private static final String BEE_SEND_SERVICE = "/MRF24J40/broadcast_packet";
	/**
	 * The name of ROS topic used for receiving broadcasted data.
	 */
	private static final String BEE_RECEIVE_TOPIC = "/MRF24J40/received_packets";

	/**
	 * The DEECo timer responsible for the invocation of scheduled events.
	 */
	private Timer timer;

	/**
	 * This class represents the Bee Click device from the point of view of
	 * DEECo.
	 */
	private class BeeClickDevice extends Device {

		/**
		 * The ROS service used for broadcasting.
		 */
		private ServiceClient<IEEE802154BroadcastPacketRequest, IEEE802154BroadcastPacketResponse> beePacketService = null;
		/**
		 * The ROS topic for packet receiving.
		 */
		private Subscriber<IEEE802154ReceivedPacket> packetReceiveTopic = null;
		
		/**
		 * The maximum number of bytes that can be sent in a single
		 * transmission.
		 */
		private static final int MTU = 100;

		/**
		 * The {@link DEECoContainer} associated with the instance of
		 * {@link BeeClickDevice}.
		 */
		public DEECoContainer container;

		/**
		 * Provides the unique identifier of the device. The identifier is not v
		 * until the {@link BeeClickComm#init(DEECoContainer)} method is called.
		 * 
		 * @return The unique identifier of the device.
		 */
		@Override
		public String getId() {
			return String.format("%d", container.getId());
		}

		/**
		 * Provides the maximum number of bytes that can be sent in one
		 * transmission.
		 * 
		 * @return The maximum number of bytes that can be sent in one
		 *         transmission.
		 */
		@Override
		public int getMTU() {
			return MTU;
		}

		/**
		 * Indicates whether this device can send data to the specified address.
		 * 
		 * @return True if the given address is an instance of
		 *         {@link MANETBroadcastAddress}. False otherwise.
		 */
		@Override
		public boolean canSend(Address address) {
			return address instanceof MANETBroadcastAddress;
		}

		/**
		 * This method is invoked by the L1 network layer. It broadcasts the
		 * given data.
		 * 
		 * @param data
		 *            The data to be sent.
		 * @param address
		 *            This parameter is ignored since the data are broadcasted.
		 */
		@Override
		public void send(byte[] data, Address address) {
			if (beePacketService == null) {
				return;
			}

			// Create new message
			IEEE802154BroadcastPacketRequest request = beePacketService.newMessage();
			// Fill the data into the message
			request.setData(ChannelBuffers.wrappedBuffer(ByteOrder.LITTLE_ENDIAN, data));

			// Send the message, don't wait for the result
			beePacketService.call(request,
					new ServiceResponseListener<IEEE802154BroadcastPacketResponse>() {
						@Override
						public void onSuccess(IEEE802154BroadcastPacketResponse arg0) {
							Log.d("BeeClickDevice sucessfully sent data.");
						}

						@Override
						public void onFailure(RemoteException arg0) {
							Log.w("BeeClickDevice failed to send data.");
						}
					});
		}

		/**
		 * Hook the "receive" routine.
		 * 
		 * @param connectedNode
		 *            The ROS node on which the DEECo node runs.
		 */
		public void subscribe(ConnectedNode connectedNode) {
			// Subscribe packet received topic
			packetReceiveTopic = connectedNode
					.newSubscriber(BEE_RECEIVE_TOPIC, IEEE802154ReceivedPacket._TYPE);
			packetReceiveTopic
					.addMessageListener(new MessageListener<IEEE802154ReceivedPacket>() {
						@Override
						public void onNewMessage(IEEE802154ReceivedPacket message) {
							// Inject new event into the timer
							timer.interruptionEvent(new TimerEventListener() {
								@Override
								public void at(long time) {
									// When the event is invoked process the
									// received data
									ChannelBuffer bufferData = message.getData();
									byte[] backingArray = bufferData.array();
									byte[] messageData = Arrays.copyOfRange(backingArray,
																bufferData.arrayOffset(),
																backingArray.length);
									receive(messageData, new MANETBroadcastAddress(
											String.format("%d", message.getSrcSAddr())));
								}
							}, "BeeClickComm_receive", null);
							Log.d("BeeClickDevice received data.");
						}
					});

			// Subscribe packet sending service
			try {
				beePacketService = connectedNode.newServiceClient(BEE_SEND_SERVICE,
						IEEE802154BroadcastPacket._TYPE);
			} catch (ServiceNotFoundException e) {
				throw new RuntimeException(e);
			}
		}

		/**
		 * Finalize the ROS subscriptions and registrations.
		 * 
		 * @param node
		 *            The ROS node on which the DEECo node runs.
		 */
		public void unsubscribe(Node node) {
			if(beePacketService != null){
				beePacketService.shutdown();
			}
			if(packetReceiveTopic != null){
				packetReceiveTopic.shutdown();
			}
		}
	}

	/**
	 * The {@link BeeClickDevice} associated with this {@link BeeClickComm}
	 * instance.
	 */
	public final BeeClickDevice beeClickDevice;

	/**
	 * Initialize new {@link BeeClickComm} instance and associate new
	 * {@link BeeClickDevice} with it.
	 */
	public BeeClickComm() {
		beeClickDevice = new BeeClickDevice();
	}

	/**
	 * Subscribe the {@link #beeClickDevice} to ROS topic for receiving packets
	 * and to a ROS service for broadcasting packets.
	 * 
	 * @param connectedNode
	 *            The ROS node on which the DEECo node runs.
	 */
	@Override
	protected void subscribeDescendant(ConnectedNode connectedNode) {
		beeClickDevice.subscribe(connectedNode);
	}

	/**
	 * Finalize the connection to ROS topics.
	 * 
	 * @param node
	 *            The ROS node on which the DEECo node runs.
	 */
	@Override
	void unsubscribe(Node node) {
		beeClickDevice.unsubscribe(node);
	}
	
	/**
	 * A list of DEECo plugins the {@link BeeClickComm} depends on.
	 * 
	 * @return a list of DEECo plugins the {@link BeeClickComm} depends on.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return Arrays.asList(new Class[] { Network.class, RosServices.class });
	}

	/**
	 * Initialize the {@link BeeClickComm} DEECo plugin. Launch the ROS node
	 * that handles the Bee Click device.
	 * 
	 * @param contained
	 *            is the DEECo container of this DEECo node.
	 */
	@Override
	public void init(DEECoContainer container) throws PluginInitFailedException {
		// Assign the device ID
		beeClickDevice.container = container;

		// Assign timer
		timer = container.getRuntimeFramework().getScheduler().getTimer();
		// Register BeeClickDevice
		Layer1 l1 = container.getPluginInstance(Network.class).getL1();
		l1.registerDevice(beeClickDevice);

		// Subscribe to ROS
		RosServices rosServices = container
				.getPluginInstance(RosServices.class);
		BeeClickNode beeClickNode = new BeeClickNode(this);
		rosServices.rosExecute(beeClickNode);

		// Wait defined time until subscribed
		if (!rosServices.isInitialized(this)) {
			throw new PluginInitFailedException(
					"The BeeClickComm was not subscribed within defined timeout.");
		}
	}
}
