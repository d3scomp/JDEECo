package cz.cuni.mff.d3s.deeco.knowledge.container;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.refEq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;

public class TrackingKnowledgeContainerTest {

	@Test
	public void getUntrackedKnowledgeEmptyShadowTest() throws KnowledgeContainerException, RoleClassException, KnowledgeAccessException {
		TrackingKnowledgeWrapper localWrapperMock = Mockito.mock(TrackingKnowledgeWrapper.class);
		List<ReadOnlyKnowledgeWrapper> shadowWrapperMocks = new ArrayList<ReadOnlyKnowledgeWrapper>();
		Mockito.when(localWrapperMock.hasRole(eq(String.class))).thenReturn(true);
		Mockito.when(localWrapperMock.getUntrackedRoleKnowledge(eq(String.class))).thenReturn("S0");
		
		TrackingKnowledgeContainer target = new TrackingKnowledgeContainer(localWrapperMock, shadowWrapperMocks);
		Collection<String> result = target.getUntrackedKnowledgeForRole(String.class);
		
		assertTrue(result.size() == 1);
		assertTrue(result.contains("S0"));
		Mockito.verify(localWrapperMock, atLeast(1)).hasRole(eq(String.class));
		Mockito.verify(localWrapperMock, times(1)).getUntrackedRoleKnowledge(eq(String.class));
		Mockito.verifyNoMoreInteractions(localWrapperMock);
	}
	
	@Test
	public void getUntrackedKnowledgeEmptyResultTest() throws RoleClassException, KnowledgeAccessException, KnowledgeContainerException {
		TrackingKnowledgeWrapper localWrapperMock = Mockito.mock(TrackingKnowledgeWrapper.class);
		List<ReadOnlyKnowledgeWrapper> shadowWrapperMocks = Arrays.asList(
				Mockito.mock(ReadOnlyKnowledgeWrapper.class), Mockito.mock(ReadOnlyKnowledgeWrapper.class)
				);
		Mockito.when(localWrapperMock.hasRole(eq(String.class))).thenReturn(false);
		Mockito.when(shadowWrapperMocks.get(0).hasRole(eq(String.class))).thenReturn(false);
		Mockito.when(shadowWrapperMocks.get(1).hasRole(eq(String.class))).thenReturn(false);
		
		TrackingKnowledgeContainer target = new TrackingKnowledgeContainer(localWrapperMock, shadowWrapperMocks);
		Collection<String> result = target.getUntrackedKnowledgeForRole(String.class);
		
		assertTrue(result.size() == 0);
		
		Mockito.verify(localWrapperMock, atLeast(1)).hasRole(eq(String.class));
		Mockito.verify(shadowWrapperMocks.get(0), atLeast(1)).hasRole(eq(String.class));
		Mockito.verify(shadowWrapperMocks.get(1), atLeast(1)).hasRole(eq(String.class));
		Mockito.verifyNoMoreInteractions(localWrapperMock, shadowWrapperMocks.get(0), shadowWrapperMocks.get(1));

	}
	
	@Test
	public void getUntrackedKnowledgeTest() throws KnowledgeContainerException, RoleClassException, KnowledgeAccessException {
		TrackingKnowledgeWrapper localWrapperMock = Mockito.mock(TrackingKnowledgeWrapper.class);
		List<ReadOnlyKnowledgeWrapper> shadowWrapperMocks = Arrays.asList(
				Mockito.mock(ReadOnlyKnowledgeWrapper.class), Mockito.mock(ReadOnlyKnowledgeWrapper.class), Mockito.mock(ReadOnlyKnowledgeWrapper.class)
				);
		Mockito.when(localWrapperMock.hasRole(eq(String.class))).thenReturn(true);
		Mockito.when(localWrapperMock.getUntrackedRoleKnowledge(eq(String.class))).thenReturn("S0");
		Mockito.when(shadowWrapperMocks.get(0).hasRole(eq(String.class))).thenReturn(true);
		Mockito.when(shadowWrapperMocks.get(0).getUntrackedRoleKnowledge(eq(String.class))).thenReturn("S1");
		Mockito.when(shadowWrapperMocks.get(1).hasRole(eq(String.class))).thenReturn(false);
		Mockito.when(shadowWrapperMocks.get(2).hasRole(eq(String.class))).thenReturn(true);
		Mockito.when(shadowWrapperMocks.get(2).getUntrackedRoleKnowledge(eq(String.class))).thenReturn("S2");
		
		TrackingKnowledgeContainer target = new TrackingKnowledgeContainer(localWrapperMock, shadowWrapperMocks);
		Collection<String> result = target.getUntrackedKnowledgeForRole(String.class);
		
		assertTrue(result.size() == 3);
		assertTrue(result.contains("S0"));
		assertTrue(result.contains("S1"));
		assertTrue(result.contains("S2"));
		
		Mockito.verify(localWrapperMock, atLeast(1)).hasRole(eq(String.class));
		Mockito.verify(shadowWrapperMocks.get(0), atLeast(1)).hasRole(eq(String.class));
		Mockito.verify(shadowWrapperMocks.get(1), atLeast(1)).hasRole(eq(String.class));
		Mockito.verify(shadowWrapperMocks.get(2), atLeast(1)).hasRole(eq(String.class));
		Mockito.verify(localWrapperMock, times(1)).getUntrackedRoleKnowledge(eq(String.class));
		Mockito.verify(shadowWrapperMocks.get(0), times(1)).getUntrackedRoleKnowledge(eq(String.class));
		Mockito.verify(shadowWrapperMocks.get(2), times(1)).getUntrackedRoleKnowledge(eq(String.class));
		Mockito.verifyNoMoreInteractions(localWrapperMock, shadowWrapperMocks.get(0), shadowWrapperMocks.get(1), shadowWrapperMocks.get(2));
	}

	@Test
	// variant of getUntrackedKnowledgeEmptyShadowTest using tracking
	public void getTrackedKnowledgeEmptyShadowTest() throws KnowledgeContainerException, RoleClassException, KnowledgeAccessException {
		TrackingKnowledgeWrapper localWrapperMock = Mockito.mock(TrackingKnowledgeWrapper.class);
		List<ReadOnlyKnowledgeWrapper> shadowWrapperMocks = new ArrayList<ReadOnlyKnowledgeWrapper>();
		Mockito.when(localWrapperMock.hasRole(eq(String.class))).thenReturn(true);
		Mockito.when(localWrapperMock.getTrackedRoleKnowledge(eq(String.class))).thenReturn("S0");
		
		TrackingKnowledgeContainer target = new TrackingKnowledgeContainer(localWrapperMock, shadowWrapperMocks);
		Collection<String> result = target.getTrackedKnowledgeForRole(String.class);
		
		assertTrue(result.size() == 1);
		assertTrue(result.contains("S0"));
		Mockito.verify(localWrapperMock, atLeast(1)).hasRole(eq(String.class));
		Mockito.verify(localWrapperMock, times(1)).getTrackedRoleKnowledge(eq(String.class));
		Mockito.verifyNoMoreInteractions(localWrapperMock);
	}
	
	@Test
	// variant of getUntrackedKnowledgeEmptyResultTest using tracking
	public void getTrackedKnowledgeEmptyResultTest() throws RoleClassException, KnowledgeAccessException, KnowledgeContainerException {
		TrackingKnowledgeWrapper localWrapperMock = Mockito.mock(TrackingKnowledgeWrapper.class);
		List<ReadOnlyKnowledgeWrapper> shadowWrapperMocks = Arrays.asList(
				Mockito.mock(ReadOnlyKnowledgeWrapper.class), Mockito.mock(ReadOnlyKnowledgeWrapper.class)
				);
		Mockito.when(localWrapperMock.hasRole(eq(String.class))).thenReturn(false);
		Mockito.when(shadowWrapperMocks.get(0).hasRole(eq(String.class))).thenReturn(false);
		Mockito.when(shadowWrapperMocks.get(1).hasRole(eq(String.class))).thenReturn(false);
		
		TrackingKnowledgeContainer target = new TrackingKnowledgeContainer(localWrapperMock, shadowWrapperMocks);
		Collection<String> result = target.getTrackedKnowledgeForRole(String.class);
		
		assertTrue(result.size() == 0);
		
		Mockito.verify(localWrapperMock, atLeast(1)).hasRole(eq(String.class));
		Mockito.verify(shadowWrapperMocks.get(0), atLeast(1)).hasRole(eq(String.class));
		Mockito.verify(shadowWrapperMocks.get(1), atLeast(1)).hasRole(eq(String.class));
		Mockito.verifyNoMoreInteractions(localWrapperMock, shadowWrapperMocks.get(0), shadowWrapperMocks.get(1));

	}
	
	@Test
	// variant of getUntrackedKnowledgeTest using tracking
	public void getTrackedKnowledgeTest() throws KnowledgeContainerException, RoleClassException, KnowledgeAccessException {
		TrackingKnowledgeWrapper localWrapperMock = Mockito.mock(TrackingKnowledgeWrapper.class);
		List<ReadOnlyKnowledgeWrapper> shadowWrapperMocks = Arrays.asList(
				Mockito.mock(ReadOnlyKnowledgeWrapper.class), Mockito.mock(ReadOnlyKnowledgeWrapper.class), Mockito.mock(ReadOnlyKnowledgeWrapper.class)
				);
		Mockito.when(localWrapperMock.hasRole(eq(String.class))).thenReturn(true);
		Mockito.when(localWrapperMock.getTrackedRoleKnowledge(eq(String.class))).thenReturn("S0");
		Mockito.when(shadowWrapperMocks.get(0).hasRole(eq(String.class))).thenReturn(true);
		Mockito.when(shadowWrapperMocks.get(0).getUntrackedRoleKnowledge(eq(String.class))).thenReturn("S1");
		Mockito.when(shadowWrapperMocks.get(1).hasRole(eq(String.class))).thenReturn(false);
		Mockito.when(shadowWrapperMocks.get(2).hasRole(eq(String.class))).thenReturn(true);
		Mockito.when(shadowWrapperMocks.get(2).getUntrackedRoleKnowledge(eq(String.class))).thenReturn("S2");
		
		TrackingKnowledgeContainer target = new TrackingKnowledgeContainer(localWrapperMock, shadowWrapperMocks);
		Collection<String> result = target.getTrackedKnowledgeForRole(String.class);
		
		assertTrue(result.size() == 3);
		assertTrue(result.contains("S0"));
		assertTrue(result.contains("S1"));
		assertTrue(result.contains("S2"));
		
		Mockito.verify(localWrapperMock, atLeast(1)).hasRole(eq(String.class));
		Mockito.verify(shadowWrapperMocks.get(0), atLeast(1)).hasRole(eq(String.class));
		Mockito.verify(shadowWrapperMocks.get(1), atLeast(1)).hasRole(eq(String.class));
		Mockito.verify(shadowWrapperMocks.get(2), atLeast(1)).hasRole(eq(String.class));
		Mockito.verify(localWrapperMock, times(1)).getTrackedRoleKnowledge(eq(String.class));
		Mockito.verify(shadowWrapperMocks.get(0), times(1)).getUntrackedRoleKnowledge(eq(String.class));
		Mockito.verify(shadowWrapperMocks.get(2), times(1)).getUntrackedRoleKnowledge(eq(String.class));
		Mockito.verifyNoMoreInteractions(localWrapperMock, shadowWrapperMocks.get(0), shadowWrapperMocks.get(1), shadowWrapperMocks.get(2));
	}
	
	@Test
	public void commitChangesTest() throws KnowledgeContainerException, KnowledgeCommitException, KnowledgeAccessException, RoleClassException {
		TrackingKnowledgeWrapper localWrapperMock = Mockito.mock(TrackingKnowledgeWrapper.class);
		List<ReadOnlyKnowledgeWrapper> shadowWrapperMocks = Arrays.asList(
				Mockito.mock(ReadOnlyKnowledgeWrapper.class), Mockito.mock(ReadOnlyKnowledgeWrapper.class)
				);
		
		TrackingKnowledgeContainer target = new TrackingKnowledgeContainer(localWrapperMock, shadowWrapperMocks);
		target.commitChanges();
		
		Mockito.verify(localWrapperMock, times(1)).commitChanges();
		Mockito.verifyNoMoreInteractions(localWrapperMock, shadowWrapperMocks.get(0), shadowWrapperMocks.get(1));
	}
	
	@Test
	public void resetTrackingTest() throws KnowledgeContainerException, KnowledgeCommitException, KnowledgeAccessException, RoleClassException {
		TrackingKnowledgeWrapper localWrapperMock = Mockito.mock(TrackingKnowledgeWrapper.class);
		List<ReadOnlyKnowledgeWrapper> shadowWrapperMocks = Arrays.asList(
				Mockito.mock(ReadOnlyKnowledgeWrapper.class), Mockito.mock(ReadOnlyKnowledgeWrapper.class)
				);
		
		TrackingKnowledgeContainer target = new TrackingKnowledgeContainer(localWrapperMock, shadowWrapperMocks);
		target.resetTracking();
		
		Mockito.verify(localWrapperMock, times(1)).resetTracking();
		Mockito.verifyNoMoreInteractions(localWrapperMock, shadowWrapperMocks.get(0), shadowWrapperMocks.get(1));
	}
	
	// TODO implement tests for reset and commit

}
