package cz.cuni.mff.d3s.jdeeco.ros;

import java.util.ArrayList;
import java.util.List;

import org.ros.node.ConnectedNode;
import org.ros.node.Node;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer.StartupListener;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.runtime.PluginInitFailedException;
import cz.cuni.mff.d3s.deeco.runtime.PluginStartupFailedException;

/**
 * An ancestor of classes which register of subscribe to any ROS topic. The
 * {@link #subscribe(ConnectedNode)} method has to be overridden in a derived
 * class and serves for the registration and subscription of topics. The
 * {@link TopicSubscriber} is a abstract class rather than interface because the
 * {@link #subscribe(ConnectedNode)} method is internal to the
 * {@link cz.cuni.mff.d3s.jdeeco.ros} package.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public abstract class TopicSubscriber implements DEECoPlugin {
	/**
	 * Instance of DEECo container this plugin belongs to
	 */
	protected DEECoContainer container;
	
	/**
	 * Indicates whether the {@link #subscribe(ConnectedNode)} method has been
	 * called (true) or not (false).
	 */
	private boolean subscribed;
	
	/**
	 * Reference to rosServices used by this subscriber
	 */
	RosServices rosServices;

	/**
	 * Initialize new @{link TopicSubscriber} object.
	 */
	public TopicSubscriber() {
		subscribed = false;
	}

	/**
	 * This method is invoked during the initialization of the
	 * {@link RosServices} DEECo plugin. In this method the derived class is
	 * supposed to register and subscribe to required ROS topics. After this
	 * method was called the {@link #isSubscribed()} method returns true.
	 * 
	 * @param connectedNode
	 *            The ROS node on which the DEECo node runs.
	 */
	final void subscribe(ConnectedNode connectedNode) {
		subscribeDescendant(connectedNode);
		subscribed = true;
	}

	/**
	 * Indicates whether the {@link #subscribe(ConnectedNode)} method has been
	 * called (true) or not (false).
	 * 
	 * @return True if the {@link #subscribe(ConnectedNode)} method has been
	 *         already called. False otherwise.
	 */
	protected boolean isSubscribed() {
		return subscribed;
	}

	/**
	 * In this method all the descendants of the {@link TopicSubscriber} has to
	 * subscribe to desired ROS topics.
	 * 
	 * @param connectedNode
	 *            The ROS node on which the DEECo node runs.
	 */
	abstract protected void subscribeDescendant(ConnectedNode connectedNode);
	
	/**
	 * Finalize the connection to ROS topics.
	 * 
	 * @param node
	 *            The ROS node on which the DEECo node runs.
	 */
	abstract void unsubscribe(Node node);
	

	/**
	 * A list of DEECo plugins the {@link TopicSubscriber} instance depends on.
	 * Each {@link TopicSubscriber} depends on {@link RosServices}.
	 * 
	 * @return a list of DEECo plugins the {@link TopicSubscriber} depends on.
	 */
	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		List<Class<? extends DEECoPlugin>> dependencies = new ArrayList<>();
		dependencies.add(RosServices.class);
		return dependencies;
	}

	/**
	 * Initialize the {@link TopicSubscriber} instance DEECo plugin.
	 * Subscribe it to ROS node topics and services.
	 * 
	 * @param container
	 *            is the DEECo container of this DEECo node.
	 */
	@Override
	public void init(DEECoContainer container) throws PluginInitFailedException {
		this.container = container;
		rosServices = container.getPluginInstance(RosServices.class);
		
		TopicSubscriber topicSubscriber = this;
		container.addStartupListener(new StartupListener() {
			@Override
			public void onStartup() throws PluginStartupFailedException {
				try{
					rosServices.register(topicSubscriber);
				} catch(IllegalStateException e){
					throw new PluginStartupFailedException(String.format(
							"The %s plugin couldn't be initialized.",
							this.getClass().getName()), e);
				}
			}
		});
	}
}
