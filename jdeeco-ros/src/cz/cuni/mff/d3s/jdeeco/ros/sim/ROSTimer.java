package cz.cuni.mff.d3s.jdeeco.ros.sim;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import org.apache.commons.lang.NotImplementedException;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.Node;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMain;
import org.ros.node.NodeMainExecutor;
import org.ros.node.topic.Subscriber;

import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.timer.BaseTimer;
import cz.cuni.mff.d3s.deeco.timer.EventTime;
import cz.cuni.mff.d3s.deeco.timer.SimulationTimer;
import cz.cuni.mff.d3s.deeco.timer.TimerEventListener;
import rosgraph_msgs.Clock;

/**
 * Implements simulation timer for ROS simulation
 * 
 * ROS is not discrete time simulation. ROS just usually goes real-time or slower, so this is supposed to keep up with
 * ROS by listening to time announcements from ROS and executing callback on time or later.
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class ROSTimer extends BaseTimer implements SimulationTimer, NodeMain {
	/**
	 * Queue of events
	 */
	protected final Queue<EventTime> eventTimes = new PriorityQueue<>();

	/**
	 * Mapping of events to containers
	 */
	protected final Map<DEECoContainer, EventTime> containerEvents = new HashMap<>();;

	/**
	 * Listens to this topic in order to obtain time
	 */
	private final static String CLOCK_TOPIC = "/clock";

	/**
	 * Maximal wait time for clock message
	 */
	private final static int CLOCK_MAX_WAIT_MILLIS = 30_000;

	/**
	 * Current time as ROS message
	 */
	private Clock currentClockMessage = null;

	/**
	 * Reference to ROS simulation this timer belongs to
	 */
	private ROSSimulation simulation;

	/**
	 * Simulation start time
	 */
	private long startTimeMs;

	/**
	 * Simulation duration
	 */
	private long durationMs;

	/**
	 * ROS node executor used to execute timer reading node
	 */
	private NodeMainExecutor rosNodeExecutor;

	/**
	 * Creates ROS timer
	 * 
	 * @param ros_master_uri
	 *            URI or ROS master node
	 * @param ros_host
	 *            IP or hostname of local ROS node
	 */
	public ROSTimer(ROSSimulation simulation) {
		this.simulation = simulation;
	}

	/**
	 * Processes new clock from ROS
	 * 
	 * @param newClock
	 */
	private void onROSStep(Clock newClock) {
		currentClockMessage = newClock;
		Log.d("Now, ROS time is: " + getCurrentMilliseconds() + " ms");

		// Wake-up timer queue
		synchronized (eventTimes) {
			eventTimes.notify();
		}
	}

	@Override
	public void notifyAt(long time, TimerEventListener listener, String eventName, DEECoContainer container) {
		synchronized (eventTimes) {
			EventTime eventTime = new EventTime(time, listener, eventName, false);
			if (!eventTimes.contains(eventTime)) {
				replaceEvent(eventTime, container);
			}
		}
	}

	@Override
	public void interruptionEvent(TimerEventListener listener, String eventName, DEECoContainer container) {
		synchronized (eventTimes) {
			EventTime eventTime = new EventTime(getCurrentMilliseconds(), listener, eventName, false);
			replaceEvent(eventTime, container);
			eventTimes.notify();
		}
	}

	@Override
	public long getCurrentMilliseconds() {
		if (currentClockMessage != null) {
			return getCurrentROSTime() - startTimeMs;
		} else {
			return 0;
		}
	}

	private long getCurrentROSTime() {
		return currentClockMessage.getClock().totalNsecs() / 1000000;
	}

	@Override
	public void start(long duration) {
		durationMs = duration;

		// First start the simulation by running ROS on the remote machine
		simulation.startSimulation();

		// Then start to listen to clock
		connectROS();

		// Finally wait for clock to actually start working
		waitForTime();
		
		// Startup system
		runStartupListeners();

		// Run jDEECo simulation
		startTimeMs = getCurrentROSTime();
		runSimulation();
		Log.i("Simulation time limit reached");

		// Shutdown system
		runShutdownListeners();

		// Simulation is done lets tear down ROS simulation on remote machine
		simulation.stopSimulation();
	}

	@Override
	public void onError(Node node, Throwable error) {
		throw new NotImplementedException(node.toString(), error);
	}

	@Override
	public void onShutdown(Node node) {
		Log.i("Timer ROS node shutting down");
	}

	@Override
	public void onShutdownComplete(Node node) {
		Log.i("Timer ROS node shut down completed");
	}

	/**
	 * Handles local ROS node start
	 * 
	 * On ROS node start registers clock topic lister which updates current time
	 * 
	 */
	@Override
	public void onStart(ConnectedNode connectedNode) {
		Subscriber<Clock> clockTopic = connectedNode.newSubscriber(CLOCK_TOPIC, Clock._TYPE);
		clockTopic.addMessageListener(new MessageListener<Clock>() {
			@Override
			public void onNewMessage(Clock message) {
				onROSStep(message);
			}
		});
	}

	@Override
	public GraphName getDefaultNodeName() {
		return GraphName.of(this.getClass().getSimpleName());
	}

	/**
	 * Create ROS node and connect to master
	 */
	private void connectROS() {
		try {
			NodeConfiguration rosNodeConfig = NodeConfiguration.newPublic(simulation.getROSHost(),
					new URI(simulation.getROSMasterURI()));
			rosNodeExecutor = DefaultNodeMainExecutor.newDefault();

			// Shutdown ROS node when simulation ends
			addShutdownListener(new ShutdownListener() {
				@Override
				public void onShutdown() {
					rosNodeExecutor.shutdown();
				}
			});

			rosNodeExecutor.execute(this, rosNodeConfig);
		} catch (URISyntaxException e) {
			throw new RuntimeException("Malformed URI: " + simulation.getROSMasterURI(), e);
		}
	}

	/**
	 * Waits for clock message from ROS until time limit is exceeded
	 */
	private void waitForTime() {
		for (long start = System.currentTimeMillis(); System.currentTimeMillis() - start < CLOCK_MAX_WAIT_MILLIS;) {
			if (currentClockMessage != null) {
				return;
			}

			long remainingMs = CLOCK_MAX_WAIT_MILLIS - (System.currentTimeMillis() - start);
			Log.i(ROSTimer.class.getSimpleName() + " waiting for clock to be published " + remainingMs
					+ "ms remaining");
			try {
				Thread.sleep(250);
			} catch (Exception e) {
			}
		}

		throw new RuntimeException("Time limit exceeded waiting for clock to be published by ROS");
	}

	/**
	 * Runs the simulation
	 */
	private void runSimulation() {
		// While time limit is not exceeded
		while (getCurrentMilliseconds() < durationMs) {
			// The next event to be processed. Assigned when its execution time
			// arises
			EventTime eventToProcess = null;

			synchronized (eventTimes) {
				final EventTime nextEvent = eventTimes.peek();
				if (nextEvent == null) {
					Log.e("No event found in the queue in the " + this.getClass().getSimpleName());
					throw new IllegalStateException(
							"No event found in the queue in the " + this.getClass().getSimpleName());
				}

				// Get the time of the next event
				final long nextTime = nextEvent.getTimePoint();

				// If it is time to execute, then execute
				if (nextTime <= getCurrentMilliseconds()) {
					if (nextTime < getCurrentMilliseconds()) {
						Log.w(String.format("An event \"%s\" missed its time slot and was delayed by %d milliseconds.",
								nextEvent.getEventName(), getCurrentMilliseconds() - nextTime));
					}

					eventToProcess = eventTimes.poll();
					eventToProcess.getListener().at(eventToProcess.getTimePoint());
				} else {
					// Wait for next ROS time or interruption event
					try {
						eventTimes.wait();
					} catch (InterruptedException e) {
						Log.e("Failed waiting for next clock message from ROS", e);
					}
				}
			}
		}
	}

	/**
	 * Replaces event for container
	 */
	private void replaceEvent(EventTime eventTime, DEECoContainer container) {
		// Replace old event for container by the new one
		eventTimes.add(eventTime);
		EventTime old = containerEvents.get(container);
		if (old != null) {
			eventTimes.remove(old);
		}
		containerEvents.put(container, eventTime);
	}
}
