package cz.cuni.mff.d3s.jdeeco.ros;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ros.exception.RosRuntimeException;
import org.ros.internal.loader.CommandLineLoader;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.Node;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMain;
import org.ros.node.NodeMainExecutor;

import com.google.common.base.Preconditions;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;

/**
 * DEECo plugin that provides an interface between DEECo and ROS. It registers
 * interface services to ROS topics and launches ROS node that handles the DEECo
 * part of the communication with the rest of the ROS system.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public class RosServices extends AbstractNodeMain implements DEECoPlugin {

	/**
	 * The list of {@link TopicSubscriber}s in the DEECo-ROS interface.
	 * 
	 * @return the list of {@link TopicSubscriber}s in the DEECo-ROS interface.
	 */
	private TopicSubscriber[] topicSubscribers() {
		return new TopicSubscriber[] {
				Sensors.getInstance(),
				Actuators.getInstance(),
				Communication.getInstance()};
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
	 * Initialize the {@link RosServices} DEECo plugin. Launch the ROS node that
	 * handles the DEECo-ROS interface.
	 * 
	 * @param contained
	 *            is the DEECo container of this DEECo node.
	 */
	@Override
	public void init(DEECoContainer container) {
		CommandLineLoader loader = new CommandLineLoader(Arrays.asList(this
				.getClass().getName()));
		String nodeClassName = loader.getNodeClassName();
		System.out.println("Loading node class: " + nodeClassName);
		NodeConfiguration nodeConfiguration = loader.build();

		NodeMain nodeMain = null;
		try {
			nodeMain = loader.loadClass(nodeClassName);
		} catch (ClassNotFoundException e) {
			throw new RosRuntimeException("Unable to locate node: "
					+ nodeClassName, e);
		} catch (InstantiationException e) {
			throw new RosRuntimeException("Unable to instantiate node: "
					+ nodeClassName, e);
		} catch (IllegalAccessException e) {
			throw new RosRuntimeException("Unable to instantiate node: "
					+ nodeClassName, e);
		}

		Preconditions.checkState(nodeMain != null);
		NodeMainExecutor nodeMainExecutor = DefaultNodeMainExecutor
				.newDefault();
		nodeMainExecutor.execute(nodeMain, nodeConfiguration);
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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

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
		for (TopicSubscriber subscriber : topicSubscribers()) {
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
