package cz.cuni.mff.d3s.deeco.timer;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;

/**
 * {@link WallTimeTimer} holds a queue of events waiting to be executed and the
 * current time of the DEECo node. The time starts at 0 and is being increased
 * according to the {@link System} time.
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public class WallTimeTimer extends BaseTimer implements RunnerTimer {

	protected final Queue<EventTime> eventTimes;
	protected final Map<DEECoContainer, EventTime> containerEvents;
	protected long startTime;
	protected long currentTime;
	protected boolean running;

	public WallTimeTimer() {
		startTime = 0;
		currentTime = 0;
		running = false;
		eventTimes = new PriorityQueue<>();
		containerEvents = new HashMap<>();
	}

	/**
	 * Returns the number of milliseconds since the {@link #start()} method was
	 * invoked. If the {@link #start()} method was not called before the call to
	 * this method then the return value is 0.
	 * 
	 * @return The number of milliseconds since the {@link #start()} method was
	 *         invoked.
	 */
	@Override
	public long getCurrentMilliseconds() {
		if(startTime == 0){
			// Before the start() method is called always return 0
			return 0;
		}
		
		// Adjust the time to return precise value
		adjustCurrentTime();

		return currentTime;
	}

	public void start() {
		runStartupListeners();
		
		running = true;
		// Save the current time as the start time of the timer
		startTime = System.currentTimeMillis();
		while (running) {
			// The next event to be processed. Assigned when its execution time
			// arises
			EventTime eventToProcess = null;

			synchronized (eventTimes) {
				final EventTime nextEvent = eventTimes.peek();
				if (nextEvent == null) {
					Log.e("No event found in the queue in the WallTimeTimer.");
					throw new IllegalStateException(
							"No event found in the queue in the WallTimeTimer.");
				}

				// Get the time of the next event
				final long nextTime = nextEvent.getTimePoint();
				try {
					// Wait till the time of the next event
					if (nextTime > currentTime) {
						eventTimes.wait(nextTime - currentTime);
					}
					else if (nextTime != currentTime){
						Log.w(String.format(
								"An event \"%s\" missed its time slot and was delayed by %d milliseconds.",
								nextEvent.getEventName(), currentTime - nextTime));
					}
					// Take the next event to be executed. Interruption events
					// notify the wait before the timeout expires
					eventToProcess = eventTimes.poll();
				} catch (InterruptedException e) {
					// Ignore interruptions
					adjustCurrentTime();
					continue;
				}
			}

			// eventToProcess should never be null since the nextEvent wasn't
			// null either. The at() method gets its planned time of execution.
			eventToProcess.getListener().at(eventToProcess.getTimePoint());

			// Adjust the current time after the event execution
			adjustCurrentTime();
		}
		
		runShutdownListeners();
	}
	
	/**
	 * Stops infinite wait for next event
	 * 
	 * This is supposed to be called from tests.
	 */
	protected void stop() {
		running = false;
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

	@Override
	public void notifyAt(long time, TimerEventListener listener, String eventName,
			DEECoContainer container) {
		synchronized (eventTimes) {
			EventTime eventTime = new EventTime(time, listener, eventName, false);
			if (!eventTimes.contains(eventTime)) {
				replaceEvent(eventTime, container);
			}
		}
	}

	/**
	 * Add new event at the beginning of the event queue. Notify the timer about
	 * the new event.
	 * 
	 * @param listener
	 *            The event listener to be invoked at its specific time.
	 * @param node
	 *            The DEECoContainer is not used in this method.
	 */
	@Override
	public void interruptionEvent(TimerEventListener listener, String eventName,
			DEECoContainer container) {
		synchronized (eventTimes) {
			adjustCurrentTime();
			EventTime eventTime = new EventTime(currentTime, listener, eventName, false);
			replaceEvent(eventTime, container);
			eventTimes.notify();
		}
	}

	/**
	 * Synchronize the {@link #currentTime} by the difference of
	 * {@link System#currentTimeMillis()} and lastSystemTime.
	 * 
	 * @param lastSystemTime
	 *            The last measured {@link System#currentTimeMillis()} before a
	 *            time-consuming operation.
	 */
	protected void adjustCurrentTime() {
		final long currentSystemTime = System.currentTimeMillis();

		// Compute the amount of elapsed time since the timer was started.
		currentTime = currentSystemTime - startTime;
	}
}
