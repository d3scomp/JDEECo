package cz.cuni.mff.d3s.jdeeco.ros;

import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;

/**
 * The ROS node that runs {@link BeeClickComm} component for networking.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public class BeeClickNode extends AbstractNodeMain {

	/**
	 * The {@link BeeClickComm} networking device.
	 */
	private final BeeClickComm beeClickComm;
	
	/**
	 * Associate the given {@link BeeClickComm} device with the instance of {@link BeeClickNode}.
	 * @param beeClickComm The instance of {@link BeeClickComm} networking device.
	 */
	public BeeClickNode(BeeClickComm beeClickComm){
		this.beeClickComm = beeClickComm;
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
		beeClickComm.unsubscribe(node);

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
		beeClickComm.subscribe(node);
	}

	/**
	 * Provides the default name of this ROS node. This name will be used unless
	 * a node name is specified in the NodeConfiguration.
	 * 
	 * @return the default name of this ROS node.
	 */
	@Override
	public GraphName getDefaultNodeName() {
		return GraphName.of("BeeClick");
	}

}
