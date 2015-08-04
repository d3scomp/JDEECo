package cz.cuni.mff.d3s.deeco.timer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessor;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.runtime.DuplicateEnsembleDefinitionException;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeFramework;
import cz.cuni.mff.d3s.deeco.runtimelog.RuntimeLogger;

public class WallTimeTimerTest {
	private WallTimeTimer timer;
	private DEECoContainer container;

	@Before
	public void init() {
		timer = new WallTimeTimer();
		container = Mockito.mock(DEECoContainer.class);
	}

	/**
	 * Tests initial time before the timer is started
	 */
	@Test
	public void testInitialValue() {
		assertEquals("Initial time is zero", 0, timer.getCurrentMilliseconds());
	}

	/**
	 * Tests single time interval
	 */
	@Test
	public void testSingleTimePoint() {
		// Register single time event and terminate when notified
		timer.notifyAt(1000, new TimerEventListener() {
			@Override
			public void at(long time) {
				assertEquals("Notification scheduled time matches scheduled time", 1000, time);
				assertTrue("Current time is greater than scheduled event time", timer.getCurrentMilliseconds() >= time);
				assertTrue("Current time is within some sane distance from scheduled time",
						timer.getCurrentMilliseconds() - time < 100);

				// Stop the timer in order to let the test complete
				timer.stop();
			}
		}, "Test event", container);

		// Start the timer
		timer.start();
	}

	/**
	 * Tests multiple time intervals
	 */
	@Test
	public void testMultipleTimeIntervals() {
		final long limit = 1000;

		class CustomTestListener implements TimerEventListener {
			public int notifies = 0;
			
			@Override
			public void at(long time) {
				Log.d(String.format("Notified at %d ms. The \"real\" time %d ms.%n", time, timer.getCurrentMilliseconds()));
				
				// Count how many times we have been notified
				notifies++;

				// Terminate timer if test limit is reached
				if (time > limit) {
					timer.stop();
				}

				// Set next event
				long nextTime = time + 100;
				timer.notifyAt(nextTime, this, String.format("At %d ms", nextTime), container);
			}
		}
		
		CustomTestListener listener = new CustomTestListener();

		// Initial notification
		timer.notifyAt(0, listener, "Initial", container);

		// Start the timer
		timer.start();
		
		// Check notification count
		assertEquals("Notification count matches the expected count", 12, listener.notifies);
	}

	/**
	 * Tests overwriting events
	 */
	@Test
	public void testEventReplacement() {
		// Set notify that will be replaced
		timer.notifyAt(1000, new TimerEventListener() {
			@Override
			public void at(long time) {
				// This point should not be reached as this event should have been replaced by the next one.
				assertTrue(false);
			}
		}, "Replaced", container);
		
		// Replace previous notification
		timer.notifyAt(1000, new TimerEventListener() {
			@Override
			public void at(long time) {
				// This event handler should replace the previous one
				
				// Stop the timer  to let the test complete
				timer.stop();
			}
		}, "Replacement", container);

		// Start the timer
		timer.start();
	}
}
