package cz.cuni.mff.d3s.jdeeco.network.device;

import org.junit.Assert;
import org.junit.Test;

import cz.cuni.mff.d3s.deeco.runners.DEECoSimulation;
import cz.cuni.mff.d3s.deeco.runtime.DEECoException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.timer.CurrentTimeProvider;
import cz.cuni.mff.d3s.deeco.timer.DiscreteEventTimer;
import cz.cuni.mff.d3s.deeco.timer.SimulationTimer;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.address.MANETBroadcastAddress;
import cz.cuni.mff.d3s.jdeeco.network.l1.L1Packet;
import cz.cuni.mff.d3s.jdeeco.network.l1.L1Strategy;

/**
 * Tests delayed deliver for simple broadcasting device
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class SimpleBroadcastDeviceDelayedDeliveryTest {
	final static long TEST_DELAY_MS = 5000;
	final static long TEST_DELAY_ALLOWED_INACCURACY_MS = 200;

	@Test
	public void testDelayedDelivery() throws InstantiationException, IllegalAccessException, DEECoException {
		// Setup simulation
		SimulationTimer simulationTimer = new DiscreteEventTimer();
		DEECoSimulation realm = new DEECoSimulation(simulationTimer);
		realm.addPlugin(new SimpleBroadcastDevice(TEST_DELAY_MS, 0, 250));
		realm.addPlugin(Network.class);

		// Setup two nodes
		DEECoNode node0 = realm.createNode();
		DEECoNode node1 = realm.createNode();

		// Register strategy for received packets to obtain received time
		Network node0Net = node0.getPluginInstance(Network.class);
		TimestampingL1Strategy receivedRegister = new TimestampingL1Strategy(simulationTimer);
		node0Net.getL1().registerL1Strategy(receivedRegister);

		// Send packet from node 1 (this is done at time == 0)
		Network node1Net = node1.getPluginInstance(Network.class);
		node1Net.getL1().sendL1Packet(new L1Packet(new byte[] { 10 }, (byte) 0, 12, 5, 100),
				MANETBroadcastAddress.BROADCAST);

		// Start simulation (Ensure we have enough time to do our tests)
		realm.start(TEST_DELAY_MS * 2);

		// Verify that the delay is correct
		Assert.assertTrue(
				"Test if delay is close enough to expected value (it may not be strictly equal as devices have some queues)",
				Math.abs(TEST_DELAY_MS - receivedRegister.lasteReceivedAt) < TEST_DELAY_ALLOWED_INACCURACY_MS);
	}
}

/**
 * Stores time when last packet was delivered
 */
class TimestampingL1Strategy implements L1Strategy {
	public long lasteReceivedAt = -1;
	final private CurrentTimeProvider clock;

	public TimestampingL1Strategy(CurrentTimeProvider clock) {
		this.clock = clock;
	}

	@Override
	public void processL1Packet(L1Packet packet) {
		lasteReceivedAt = clock.getCurrentMilliseconds();
		System.out.println("Node 0 received L1 packet at " + lasteReceivedAt);
	}
}
