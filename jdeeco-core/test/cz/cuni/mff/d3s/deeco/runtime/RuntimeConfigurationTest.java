package cz.cuni.mff.d3s.deeco.runtime;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * The class <code>RuntimeConfigurationTest</code> contains tests for the class <code>{@link RuntimeConfiguration}</code>.
 *
 * @generatedBy CodePro at 8.11.13 10:33
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 *
 */
public class RuntimeConfigurationTest {
	/**
	 * Run the RuntimeConfiguration(Scheduling,Distribution,Execution) constructor test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 8.11.13 10:33
	 */
	@Test
	public void testRuntimeConfiguration()
		throws Exception {
		RuntimeConfiguration.Scheduling scheduling = RuntimeConfiguration.Scheduling.WALL_TIME;
		RuntimeConfiguration.Distribution distribution = RuntimeConfiguration.Distribution.LOCAL;
		RuntimeConfiguration.Execution execution = RuntimeConfiguration.Execution.SINGLE_THREADED;

		RuntimeConfiguration result = new RuntimeConfiguration(scheduling, distribution, execution);

		// add additional test code here
		assertNotNull(result);
		assertEquals("RuntimeConfiguration [scheduling=WALL_TIME, distribution=LOCAL, execution=SINGLE_THREADED]", result.toString());
	}

	/**
	 * Run the RuntimeConfiguration.Distribution getDistribution() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 8.11.13 10:33
	 */
	@Test
	public void testGetDistribution()
		throws Exception {
		RuntimeConfiguration fixture = new RuntimeConfiguration(RuntimeConfiguration.Scheduling.WALL_TIME, RuntimeConfiguration.Distribution.LOCAL, RuntimeConfiguration.Execution.SINGLE_THREADED);

		RuntimeConfiguration.Distribution result = fixture.getDistribution();

		// add additional test code here
		assertNotNull(result);
		assertEquals("LOCAL", result.toString());
		assertEquals("LOCAL", result.name());
		assertEquals(0, result.ordinal());
	}

	/**
	 * Run the RuntimeConfiguration.Execution getExecution() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 8.11.13 10:33
	 */
	@Test
	public void testGetExecution()
		throws Exception {
		RuntimeConfiguration fixture = new RuntimeConfiguration(RuntimeConfiguration.Scheduling.WALL_TIME, RuntimeConfiguration.Distribution.LOCAL, RuntimeConfiguration.Execution.SINGLE_THREADED);

		RuntimeConfiguration.Execution result = fixture.getExecution();

		// add additional test code here
		assertNotNull(result);
		assertEquals("SINGLE_THREADED", result.toString());
		assertEquals("SINGLE_THREADED", result.name());
		assertEquals(0, result.ordinal());
	}

	/**
	 * Run the RuntimeConfiguration.Scheduling getScheduling() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 8.11.13 10:33
	 */
	@Test
	public void testGetScheduling()
		throws Exception {
		RuntimeConfiguration fixture = new RuntimeConfiguration(RuntimeConfiguration.Scheduling.WALL_TIME, RuntimeConfiguration.Distribution.LOCAL, RuntimeConfiguration.Execution.SINGLE_THREADED);

		RuntimeConfiguration.Scheduling result = fixture.getScheduling();

		// add additional test code here
		assertNotNull(result);
		assertEquals("WALL_TIME", result.toString());
		assertEquals("WALL_TIME", result.name());
		assertEquals(0, result.ordinal());
	}

	/**
	 * Run the String toString() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 8.11.13 10:33
	 */
	@Test
	public void testToString()
		throws Exception {
		RuntimeConfiguration fixture = new RuntimeConfiguration(RuntimeConfiguration.Scheduling.WALL_TIME, RuntimeConfiguration.Distribution.LOCAL, RuntimeConfiguration.Execution.SINGLE_THREADED);

		String result = fixture.toString();

		// add additional test code here
		assertEquals("RuntimeConfiguration [scheduling=WALL_TIME, distribution=LOCAL, execution=SINGLE_THREADED]", result);
	}


}