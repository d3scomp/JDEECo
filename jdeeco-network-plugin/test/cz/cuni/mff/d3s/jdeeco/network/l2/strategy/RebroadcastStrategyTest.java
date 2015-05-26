package cz.cuni.mff.d3s.jdeeco.network.l2.strategy;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerContainer;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;
import cz.cuni.mff.d3s.deeco.network.CommunicationBoundaryPredicate;
import cz.cuni.mff.d3s.deeco.network.KnowledgeData;
import cz.cuni.mff.d3s.jdeeco.network.InstantSchedulerMock;
import cz.cuni.mff.d3s.jdeeco.network.address.IPAddress;
import cz.cuni.mff.d3s.jdeeco.network.address.MANETBroadcastAddress;
import cz.cuni.mff.d3s.jdeeco.network.l1.L1Packet;
import cz.cuni.mff.d3s.jdeeco.network.l1.MANETReceivedInfo;
import cz.cuni.mff.d3s.jdeeco.network.l1.ReceivedInfo;
import cz.cuni.mff.d3s.jdeeco.network.l2.L2Packet;
import cz.cuni.mff.d3s.jdeeco.network.l2.L2ReceivedInfo;
import cz.cuni.mff.d3s.jdeeco.network.l2.Layer2;

/**
 * Basic unit tests for layer 2 rebroadcast strategy
 * 
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class RebroadcastStrategyTest {
	private RebroadcastStrategy strategy;
	private Layer2 layer2;
	private final Object payloadObject = Mockito.mock(KnowledgeData.class);

	@Before
	public void configureStrategy() {
		// Create strategy for tests
		strategy = new RebroadcastStrategy();

		// Initial setup
		strategy.setEnsembleDefinitions(Collections.emptyList());
		strategy.setScheduler(new InstantSchedulerMock());
		layer2 = Mockito.mock(Layer2.class);
		strategy.setLayer2(layer2);
	}

	private L2Packet getL2Packet(int dataId, L2ReceivedInfo receivedInfo) {
		L2Packet packet = Mockito.mock(L2Packet.class);
		Mockito.when(packet.getObject()).thenReturn(payloadObject);
		Mockito.when(packet.getReceivedInfo()).thenReturn(receivedInfo);
		return packet;
	}

	private L2Packet getIPSourcedL2Packet(int dataId) {
		// Mock received info
		L1Packet l1Packet = Mockito.mock(L1Packet.class);
		l1Packet.receivedInfo = new ReceivedInfo(new IPAddress("1.1.1.1"));
		L2ReceivedInfo receivedInfo = new L2ReceivedInfo(Arrays.asList(l1Packet), (byte) 0, dataId);

		return getL2Packet(dataId, receivedInfo);
	}

	private L2Packet getMANETSourcedL2Packet(int dataId) {
		// Mock received info
		L1Packet l1Packet = Mockito.mock(L1Packet.class);
		l1Packet.receivedInfo = new MANETReceivedInfo(MANETBroadcastAddress.BROADCAST, RebroadcastStrategy.RSSI_100m);
		L2ReceivedInfo receivedInfo = new L2ReceivedInfo(Arrays.asList(l1Packet), (byte) 0, dataId);

		return getL2Packet(dataId, receivedInfo);
	}

	/**
	 * Test if packet is scheduled for rebroadcast
	 * 
	 * In this case the L2 packet is from MANET network => should be rebroadcast
	 */
	@Test
	public void schedulePacketForRebroadcastTest() {
		L2Packet packet = getMANETSourcedL2Packet(0);
		strategy.processL2Packet(packet);
		verify(layer2, Mockito.times(1)).sendL2Packet(packet, MANETBroadcastAddress.BROADCAST);
	}

	/**
	 * Test if the same packet is not rebroadcast twice
	 */
	@Test
	public void schedulePacketForRebroadcastTwiceSamePacketTest() {
		L2Packet packet0 = getMANETSourcedL2Packet(0);
		L2Packet packet1 = getMANETSourcedL2Packet(0);
		strategy.processL2Packet(packet0);
		strategy.processL2Packet(packet1);
		verify(layer2, times(1)).sendL2Packet(packet0, MANETBroadcastAddress.BROADCAST);
	}

	/**
	 * Test if the different packet is rebroadcast
	 */
	@Test
	public void schedulePacketForRebroadcastTwiceTest() {
		L2Packet packet0 = getMANETSourcedL2Packet(0);
		L2Packet packet1 = getMANETSourcedL2Packet(1);
		strategy.processL2Packet(packet0);
		strategy.processL2Packet(packet1);
		Mockito.verify(layer2, times(2)).sendL2Packet(any(), any());
	}

	/**
	 * Tests is IP packet is not rebroadcast
	 */
	@Test
	public void scheduleIPPacketForRebroadcastTest() {
		L2Packet packet = getIPSourcedL2Packet(0);
		strategy.processL2Packet(packet);
		Mockito.verify(layer2, atMost(0)).sendL2Packet(any(), any());
	}

	/**
	 * Tests boundary condition blocks packet propagation
	 */
	@Test
	public void boundaryConditionBlockTest() {
		// Configure knowledge manager container
		KnowledgeManagerContainer kmContainer = mock(KnowledgeManagerContainer.class);
		KnowledgeManager manager = mock(KnowledgeManager.class);
		when(kmContainer.getLocals()).thenReturn(Arrays.asList(manager));
		strategy.setKmContainer(kmContainer);

		// Configure boundary condition
		EnsembleDefinition ensembleDefinition = mock(EnsembleDefinition.class);
		strategy.setEnsembleDefinitions(Arrays.asList(ensembleDefinition));
		CommunicationBoundaryPredicate predicate = mock(CommunicationBoundaryPredicate.class);
		when(ensembleDefinition.getCommunicationBoundary()).thenReturn(predicate);

		// Prevent packet from propagation and verify correct parameters for predicate evaluation
		when(predicate.eval((KnowledgeData) (payloadObject), manager)).thenReturn(false);

		// Try the packet processing
		L2Packet packet = getMANETSourcedL2Packet(0);
		strategy.processL2Packet(packet);

		// verify packet was blocked
		Mockito.verify(layer2, atMost(0)).sendL2Packet(any(), any());
	}

	/**
	 * Tests packet is propagated when boundary condition let it pass
	 */
	@Test
	public void boundaryConditionPassTest() {
		// Configure knowledge manager container
		KnowledgeManagerContainer kmContainer = mock(KnowledgeManagerContainer.class);
		KnowledgeManager manager = mock(KnowledgeManager.class);
		when(kmContainer.getLocals()).thenReturn(Arrays.asList(manager));
		strategy.setKmContainer(kmContainer);

		// Configure boundary condition
		EnsembleDefinition ensembleDefinition = mock(EnsembleDefinition.class);
		strategy.setEnsembleDefinitions(Arrays.asList(ensembleDefinition));
		CommunicationBoundaryPredicate predicate = mock(CommunicationBoundaryPredicate.class);
		when(ensembleDefinition.getCommunicationBoundary()).thenReturn(predicate);

		// Allow packet to propagate and verify correct parameters for predicate evaluation
		when(predicate.eval((KnowledgeData) (payloadObject), manager)).thenReturn(true);

		// Try the packet processing
		L2Packet packet = getMANETSourcedL2Packet(0);
		strategy.processL2Packet(packet);

		// verify packet was blocked
		Mockito.verify(layer2, times(1)).sendL2Packet(packet, MANETBroadcastAddress.BROADCAST);
	}
}
