package cz.cuni.mff.d3s.jdeeco.ros;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.Node;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.runtime.PluginInitFailedException;

/**
 * DEECo plugin that provides an interface between DEECo and ROS. It registers
 * interface services to ROS topics and launches ROS node that handles the DEECo
 * part of the communication with the rest of the ROS system.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public class RosServices extends AbstractNodeMain implements DEECoPlugin {

	/**
	 * The timeout in milliseconds to wait for the ROS topic subscription.
	 */
	private final long SUBSCRIPTION_TIMEOUT = 30_000; // Timeout in milliseconds

	/**
	 * The ROS node running DEECo.
	 */
	private NodeMainExecutor rosNode;

	/**
	 * The configuration of the ROS node running DEECo.
	 */
	private NodeConfiguration rosNodeConfig;

	/**
	 * The list of {@link TopicSubscriber}s in the DEECo-ROS interface. This
	 * variable is static because the creation of ROS node loads new instance of
	 * this class an otherwise it initialize different TopicSubscribers than the
	 * ones available in jDEECo simulation.
	 * 
	 * @return the list of {@link TopicSubscriber}s in the DEECo-ROS interface.
	 */
	private TopicSubscriber[] topicSubscribers = new TopicSubscriber[] {
			new Wheels(), new Bumper(), new Buttons(), new DockIR(),
			new Position(), new LEDs(), new Speeker(), new FloorDistance(),
			new Info(), new SHT1x() };

	/**
	 * Create new instance of the {@link RosServices} class. In the constructor
	 * there is created new {@link #rosNode} and {@link #rosNodeConfig}. If
	 * there is a problem with their creation an {@link RuntimeException}
	 * arises.
	 */
	public RosServices(String ros_master, String ros_host) {
		try {
			rosNodeConfig = NodeConfiguration.newPublic(ros_host, new URI(
					ros_master));
			rosNode = DefaultNodeMainExecutor.newDefault();
		} catch (URISyntaxException e) {
			throw new RuntimeException("Malformed URI: " + ros_master, e);
		}
	}

	/**
	 * A list of DEECo plugins the {@link RosServices} depends on.
	 * 
	 * @return a list of DEECo plugins the {@link RosServices} depends on.
	 */
	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return new ArrayList<>();
	}

	/**
	 * Provides an instance of requested type if registered.
	 * 
	 * @param serviceType
	 *            The type of the requested service.
	 * @return The instance of requested service if registered. Null otherwise.
	 */
	@SuppressWarnings("unchecked")
	public <T> T getService(Class<T> serviceType) {
		for (TopicSubscriber service : topicSubscribers) {
			if (service.getClass().equals(serviceType)) {
				return (T) service;
			}
		}
		return null;
	}

	/**
	 * Execute the given ROS node in the context of {@link #rosNode} with
	 * {@link #rosNodeConfig}.
	 * 
	 * @param node
	 *            The ROS node to be launched.
	 */
	public void rosExecute(AbstractNodeMain node) {
		rosNode.execute(node, rosNodeConfig);
	}

	/**
	 * Initialize the {@link RosServices} DEECo plugin. Launch the ROS node that
	 * handles the DEECo-ROS interface.
	 * 
	 * @param contained
	 *            is the DEECo container of this DEECo node.
	 */
	@Override
	public void init(DEECoContainer container) throws PluginInitFailedException {
		Log.i("Starting ROS node.");
		rosExecute(this);

		// Wait defined time until subscribed
		if (!isInitialized(topicSubscribers)) {
			throw new PluginInitFailedException(
					String.format(
							"The ROS topics were not subscribed within %d milliseconds.",
							SUBSCRIPTION_TIMEOUT));
		}
	}

	/**
	 * Check whether all the given ROS topics are subscribed within a defined
	 * time limit.
	 * 
	 * @return True if all the ROS topics are subscribed. False otherwise.
	 */
	boolean isInitialized(TopicSubscriber... subscribers) {
		final Date startTime = new Date();
		Date currentTime = new Date();

		Log.i(String.format("Waiting up to %d milliseconds for ROS topic subscriptions.",
				SUBSCRIPTION_TIMEOUT));
		
		while (currentTime.getTime() < startTime.getTime()
				+ SUBSCRIPTION_TIMEOUT) {
			try {
				// Wait a while between checks
				Thread.sleep(1000);

				// Check whether all ROS topics are subscribed
				boolean allSubscribed = true;
				for (TopicSubscriber subscriber : subscribers) {
					if (!subscriber.isSubscribed()) {
						allSubscribed = false;
					}
				}

				// If all the topics are subscribed return true
				if (allSubscribed) {
					return true;
				}

			} catch (InterruptedException e) {
				// Ignore interruptions
			}
			currentTime = new Date();
		}

		// If any of the topics is not subscribed within the given time limit
		// return false
		return false;
	}

	/**
	 * On error callback. Handle erroneous situations. This method is called
	 * when an error occurs in the Node itself. These errors are typically
	 * fatal. The NodeListener.onShutdown and NodeListener.onShutdownComplete
	 * methods will be called following the call to NodeListener.onError.
	 * 
	 * @param node
	 *            is used to build things like Publishers and Subscribers and
	 *            interact with roscore.
	 * @param error
	 *            is the error that has occurred.
	 */
	@Override
	public void onError(Node node, Throwable error) {
		// Auto-generated method stub
	}

	/**
	 * On shutdown callback. Handle shutdown event in its beginning. This method
	 * is the first exit point for your program. It will be executed as soon as
	 * shutdown is started (i.e. before all publishers, subscribers, etc. have
	 * been shutdown). The shutdown of all created publishers, subscribers, etc.
	 * will be delayed until this method returns or the shutdown timeout
	 * expires.
	 * 
	 * @param node
	 *            is used to build things like Publishers and Subscribers and
	 *            interact with roscore.
	 */
	@Override
	public void onShutdown(Node node) {
		for (TopicSubscriber subscriber : topicSubscribers) {
			subscriber.unsubscribe(node);
		}
	}

	/**
	 * On shutdown callback. Handle shutdown event in its end. This method is
	 * the final exit point for your program. It will be executed after all
	 * publishers, subscribers, etc. have been shutdown. This is the preferred
	 * place to handle clean up since it will not delay shutdown.
	 * 
	 * @param node
	 *            is used to build things like Publishers and Subscribers and
	 *            interact with roscore.
	 */
	@Override
	public void onShutdownComplete(Node node) {
		// Auto-generated method stub

	}

	/**
	 * On start callback. Subscribe to and register ROS topics. This method is
	 * the entry point for your program (or node). It will execute as soon as
	 * the node is started. It can be considered as then main method of the
	 * rosjava node.
	 * 
	 * @param node
	 *            is used to build things like Publishers and Subscribers and
	 *            interact with roscore.
	 */
	@Override
	public void onStart(ConnectedNode node) {
		for (TopicSubscriber subscriber : topicSubscribers) {
			subscriber.subscribe(node);
		}
	}

	/**
	 * Provides the default name of this ROS node. This name will be used unless
	 * a node name is specified in the NodeConfiguration.
	 * 
	 * @return the default name of this ROS node.
	 */
	@Override
	public GraphName getDefaultNodeName() {
		return GraphName.of("RosServices");
	}

}
