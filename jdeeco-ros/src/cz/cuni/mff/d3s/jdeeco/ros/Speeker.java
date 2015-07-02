package cz.cuni.mff.d3s.jdeeco.ros;

import org.ros.concurrent.CancellableLoop;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

import cz.cuni.mff.d3s.jdeeco.ros.datatypes.SoundValue;

/**
 * Provides methods to command actuators through ROS. Registration of
 * appropriate ROS topics is handled in the {@link #subscribe(ConnectedNode)}
 * method.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public class Speeker extends TopicSubscriber {

	/**
	 * The name of the topic for sound messages.
	 */
	private static final String SOUND_TOPIC = "/mobile_base/commands/sound";

	/**
	 * The sound to be played.
	 */
	private SoundValue sound;
	/**
	 * The lock to wait and notify on when a sound should be played.
	 */
	private final Object soundLock;

	/**
	 * Internal constructor enables the {@link RosServices} to be in the control
	 * of instantiating {@link Speeker}.
	 */
	Speeker() {
		soundLock = new Object();
	}

	/**
	 * Subscribe to the ROS topics for sound messages. To publish sound message
	 * wait until notified by the {@link #playSound(SoundValue)} setter.
	 * 
	 * @param connectedNode
	 *            The ROS node on which the DEECo node runs.
	 */
	@Override
	protected void subscribeDescendant(ConnectedNode connectedNode) {
		final Publisher<kobuki_msgs.Sound> soundTopic = connectedNode
				.newPublisher(SOUND_TOPIC, kobuki_msgs.Sound._TYPE);
		connectedNode.executeCancellableLoop(new CancellableLoop() {
			@Override
			protected void setup() {
			}

			@Override
			protected void loop() throws InterruptedException {
				synchronized (soundLock) {
					soundLock.wait();
				}

				kobuki_msgs.Sound soundMsg = soundTopic.newMessage();
				soundMsg.setValue(sound.value);
				soundTopic.publish(soundMsg);
				
				// TODO: log
			}
		});
	}

	/**
	 * Play the specified sound.
	 * 
	 * @param sound
	 *            The sound to be played.
	 */
	public void playSound(SoundValue sound) {
		this.sound = sound;
		synchronized (soundLock) {
			soundLock.notify();
		}
	}

}
