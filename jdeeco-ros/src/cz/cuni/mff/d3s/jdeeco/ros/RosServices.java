package cz.cuni.mff.d3s.jdeeco.ros;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
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
	private final long ROS_START_TIMEOUT = 30_000; // Timeout in milliseconds

	/**
	 * The ROS_MASTER_URI value.
	 */
	private String ros_master;

	/**
	 * The ROS_HOST address.
	 */
	private String ros_host;
	
	/**
	 * Node namespace
	 * 
	 * Topics can use this value to register and publish with this prefix
	 */
	private String namespace;

	/**
	 * After {@link #onStart(ConnectedNode)} is called by ROS the connected
	 * contains a reference to the corresponding ROS node.
	 */
	private ConnectedNode connectedNode;

	/**
	 * A list of {@link TopicSubscriber}s that was registered in the
	 * {@link #register(TopicSubscriber)} method. Serves for correct termination
	 * of all the connection made to the ROS node.
	 */
	private List<TopicSubscriber> topicSubscribers;

	/**
	 * Create new instance of the {@link RosServices} class.
	 * 
	 * @param ros_master
	 *            The ROS_MASTER_URI value.
	 * @param ros_host
	 *            The ROS_HOST address.
	 * @param namespace
	 *            Node namespace in ROS
	 */
	public RosServices(String ros_master, String ros_host, String namespace) {
		connectedNode = null;
		topicSubscribers = new ArrayList<>();
		
		// Ensure namespace ends with /
		if(!namespace.endsWith("/"))
			namespace += "/";

		this.ros_master = ros_master;
		this.ros_host = ros_host;
		this.namespace = namespace;
	}
	
	public RosServices(String ros_master, String ros_host) {
		this(ros_master, ros_host, "");
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
	 * Register the given @{link TopicSubscriber}. This method is allowed to be
	 * called after {@link #onStart(ConnectedNode)} was invoked by ROS, but
	 * before {@link #onShutdown(Node)} is called.
	 * 
	 * @param subscriber
	 *            The {@link TopicSubscriber} to be subscribed to ROS topics and
	 *            services.
	 * 
	 * @throws IllegalArgumentException
	 *             If the subscriber argument is null.
	 * @throws IllegalStateException
	 *             If the {@link #connectedNode} was not initialized (in
	 *             {@link #onStart(ConnectedNode)} method) or was terminated (in
	 *             {@link #onShutdown(Node)}) method.
	 */
	public void register(TopicSubscriber subscriber) {
		if (subscriber == null) {
			throw new IllegalArgumentException(String.format("The \"%s\" argument cannot be null.", "subscriber"));
		}
		if (connectedNode == null) {
			throw new IllegalStateException(
					String.format("No ROS node connected. %s are either not initialized or ROS node was shutdown.",
							this.getClass().getName()));
		}

		topicSubscribers.add(subscriber);
		subscriber.subscribe(connectedNode);
	}
	
	/**
	 * Gets node namespace
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * Provides an instance of a ROS service if the requested service is
	 * registered. If the requested service has not been registered a null is
	 * returned.
	 * 
	 * @param serviceType
	 *            The class of the requested ROS service.
	 * @return An instance of the requested ROS service if such a service has
	 *         been registered, null otherwise.
	 */
	@SuppressWarnings("unchecked")
	public <T extends TopicSubscriber> T getService(Class<T> serviceType) {
		for (TopicSubscriber service : topicSubscribers) {
			if (service.getClass() == serviceType) {
				return (T) service;
			}
		}

		return null;
	}

	/**
	 * Initialize the {@link RosServices} DEECo plugin. Launch the ROS node that
	 * handles the DEECo-ROS interface.
	 * 
	 * @param contained
	 *            is the DEECo container of this DEECo node.
	 * @throws PluginInitFailedException
	 *             if the {@link #ros_master} URI is malformed or the ROS node
	 *             couldn't be started within {@link #ROS_START_TIMEOUT}.
	 */
	@Override
	public void init(DEECoContainer container) throws PluginInitFailedException {
		Log.i("Starting ROS node.");

		try {
			NodeConfiguration rosNodeConfig = NodeConfiguration.newPublic(ros_host, new URI(ros_master));
			NodeMainExecutor rosNode = DefaultNodeMainExecutor.newDefault();

			rosNode.execute(this, rosNodeConfig);
		} catch (URISyntaxException e) {
			throw new PluginInitFailedException("Malformed URI: " + ros_master, e);
		}

		// Wait defined timeout till the ROS node is started
		if (!isStarted()) {
			throw new PluginInitFailedException(
					String.format("The ROS node not started within %d milliseconds.", ROS_START_TIMEOUT));
		}
	}

	/**
	 * Check whether the ROS node is started within defined time limit.
	 * 
	 * @return True if the ROS node starts. False otherwise.
	 */
	private boolean isStarted() {
		final long startTime = System.currentTimeMillis();
		
		Log.i(String.format("Waiting up to %d milliseconds for ROS node to be started.", ROS_START_TIMEOUT));

		long remainingMs = ROS_START_TIMEOUT;
		while (remainingMs > 0) {
			remainingMs = ROS_START_TIMEOUT - (System.currentTimeMillis() - startTime);
			
			try {
				// Wait a while between checks
				Thread.sleep(250);
			} catch (InterruptedException e) {
				// Ignore interruptions
			}

			// If all the topics are not subscribed try again later
			if (connectedNode == null) {
				Log.i("Waiting for node to connect... " + remainingMs + "ms remaining");
				continue;
			}
			
			// If time is not yet available try again later
			try {
				connectedNode.getCurrentTime();
			} catch(Exception e) {
				Log.i("Waiting for node to provide current time... " + remainingMs + "ms remaining");
				continue;
			}
			
			return true;
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
		Log.e(String.format("ROS node experienced an error: %s", error.getMessage()));
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
		connectedNode = null;
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
		connectedNode = node;
	}

	/**
	 * Provides the default name of this ROS node. This name will be used unless
	 * a node name is specified in the NodeConfiguration.
	 * 
	 * @return the default name of this ROS node.
	 */
	@Override
	public GraphName getDefaultNodeName() {
		return GraphName.of(getNamespace() + this.getClass().getSimpleName());
	}

}
