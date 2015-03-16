package cz.cuni.mff.d3s.jdeeco.network.l1;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.jdeeco.network.InstantSchedulerMock;
import cz.cuni.mff.d3s.jdeeco.network.address.MANETBroadcastAddress;
import cz.cuni.mff.d3s.jdeeco.network.device.Device;

/**
 * Tests for DeviceOutputQueue
 */
@RunWith(MockitoJUnitRunner.class)
public class DeviceOutputQueueTest {
	static final int DEVICE_MTU = 256;
	static final byte[] PACKET_DATA = { 't', 'e', 's', 't' };
	static final L1Packet L1_PACKET = new L1Packet(PACKET_DATA, (byte) 0, 0, 0, PACKET_DATA.length);
	
	private DeviceOutputQueue queue;
	private Device device;

	/**
	 * Setups device output queue with needed dependencies
	 */
	@Before
	public void setupQueue() {
		// Setup mock device
		device = Mockito.mock(Device.class);
		Mockito.when(device.getMTU()).thenReturn(DEVICE_MTU);

		// Setup mock scheduler
		Scheduler scheduler = new InstantSchedulerMock();

		// Initialize queue used for testing
		queue = new DeviceOutputQueue(device, MANETBroadcastAddress.BROADCAST, scheduler);
	}

	/**
	 * Tests available device buffer space calculation
	 */
	@Test
	public void availableL0SpaceTest() {
		// Test whenever available space is equal to MTU of the device
		// Queue initial state should be empty
		Assert.assertEquals(DEVICE_MTU, queue.availableL0Space());

		// Add packet to queue buffer
		queue.fillL0Packet(L1_PACKET);

		// Test that the remaining space was limited by size of L1 packet and header
		Assert.assertEquals(DEVICE_MTU - PACKET_DATA.length - L1Packet.HEADER_SIZE, queue.availableL0Space());

		// Send buffered data
		queue.send();

		// Test whenever available space is equal to MTU of the device
		// Queue should be empty again
		Assert.assertEquals(DEVICE_MTU, queue.availableL0Space());
	}

	/**
	 * Tests immediate sending of packet
	 */
	@Test
	public void sendImmediatelyTest() {
		// Try to send packet immediately
		queue.sendImmediately(L1_PACKET);

		// Check that the device was used to send the data
		Mockito.verify(device, Mockito.atLeastOnce()).send(Mockito.any(), Mockito.any());
	}

	/**
	 * Tests delayed sending of packet
	 * 
	 * With mocked instant scheduler the output is immediate send of the packet.
	 */
	@Test
	public void sendDelayedTest() {
		// Try to send packet with timeout
		queue.sendDelayed(L1_PACKET);

		// Check that the device was used to send the data
		Mockito.verify(device, Mockito.atLeastOnce()).send(Mockito.any(), Mockito.any());
	}
}
