package cz.cuni.mff.d3s.deeco.knowledge.container;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.refEq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;

import java.util.Arrays;
import java.util.Collection;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import cz.cuni.mff.d3s.deeco.annotations.pathparser.ParseException;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeUpdateException;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;

public class TrackingKnowledgeWrapperTest {
	
	@Test
	public void getTrackedRoleKnowledgeTest() throws KnowledgeNotFoundException, ParseException, AnnotationProcessorException, RoleClassException, KnowledgeAccessException {
		KnowledgeManager kmMock = ReadOnlyKnowledgeWrapperTest.getTestRoleKnowledgeManager();
		RoleDisjointednessChecker rdcMock = Mockito.mock(RoleDisjointednessChecker.class);
		TrackingKnowledgeWrapper target = new TrackingKnowledgeWrapper(kmMock, rdcMock);
		TestRole result = target.getTrackedRoleKnowledge(TestRole.class);
		assertEquals((Integer) 42, result.i);
		assertEquals("test", result.s);
		
		Mockito.verify(kmMock, times(1)).get(any());
		Mockito.verifyNoMoreInteractions(rdcMock);
	}
	
	@Test(expected = RoleClassException.class)
	public void getTrackedRoleKnowledgeBadRoleTest() throws RoleClassException, KnowledgeAccessException {
		KnowledgeManager kmMock = Mockito.mock(KnowledgeManager.class);
		Mockito.when(kmMock.getRoles()).thenReturn(new Class<?>[] {Integer.class});
		TrackingKnowledgeWrapper target = new TrackingKnowledgeWrapper(kmMock);
		target.getTrackedRoleKnowledge(String.class);
	}
	
	@Test
	public void commitChangesTest() throws KnowledgeNotFoundException, KnowledgeUpdateException, RoleClassException, KnowledgeAccessException, KnowledgeCommitException, ParseException, AnnotationProcessorException {
		KnowledgeManager kmMock = ReadOnlyKnowledgeWrapperTest.getTestRoleKnowledgeManager();
		Mockito.doNothing().when(kmMock).update(any());
		RoleDisjointednessChecker rdcMock = Mockito.mock(RoleDisjointednessChecker.class);
		Mockito.doNothing().when(rdcMock).checkRolesAreDisjoint(any());
		
		TrackingKnowledgeWrapper target = new TrackingKnowledgeWrapper(kmMock, rdcMock);
		TestRole result = target.getTrackedRoleKnowledge(TestRole.class);
		result.i = 43;
		result.s = "test2";
		
		target.commitChanges();
		
		Mockito.verify(kmMock, times(1)).get(any());
		Mockito.verify(kmMock, times(1)).update(argThat(new ArgumentMatcher<ChangeSet>() {
			@Override
			public boolean matches(Object argument) {
				ChangeSet changeSet = (ChangeSet) argument;
				return changeSet.getUpdatedReferences().size() == 2 && changeSet.getDeletedReferences().isEmpty()
						&& changeSet.getValue(TestRole.kpi).equals(43) && changeSet.getValue(TestRole.kps).equals("test2");
			}
		}));
		Mockito.verify(rdcMock, times(1)).checkRolesAreDisjoint(argThat(new CollectionMatcher<Class<?>>(TestRole.class)));
	}
	
	@Test
	public void commitChangesEmptyTest() throws KnowledgeNotFoundException, KnowledgeUpdateException, KnowledgeCommitException, KnowledgeAccessException, RoleClassException {
		KnowledgeManager kmMock = Mockito.mock(KnowledgeManager.class);
		Mockito.doNothing().when(kmMock).update(any());
		RoleDisjointednessChecker rdcMock = Mockito.mock(RoleDisjointednessChecker.class);
		Mockito.doNothing().when(rdcMock).checkRolesAreDisjoint(any());
		
		TrackingKnowledgeWrapper target = new TrackingKnowledgeWrapper(kmMock, rdcMock);
		target.commitChanges();
		
		Mockito.verify(kmMock, atMost(1)).update(argThat(new ArgumentMatcher<ChangeSet>() {
			@Override
			public boolean matches(Object argument) {
				ChangeSet changeSet = (ChangeSet) argument;
				return changeSet.getUpdatedReferences().size() == 0 && changeSet.getDeletedReferences().size() == 0;
			}
		}));
		Mockito.verify(rdcMock, times(1)).checkRolesAreDisjoint(argThat(new CollectionMatcher<Class<?>>()));
	}
	
	@Test(expected = RoleClassException.class)
	public void commitChangesNondisjointRolesTest() throws KnowledgeUpdateException, RoleClassException, KnowledgeCommitException, KnowledgeAccessException {
		KnowledgeManager kmMock = Mockito.mock(KnowledgeManager.class);
		Mockito.doNothing().when(kmMock).update(any());
		RoleDisjointednessChecker rdcMock = Mockito.mock(RoleDisjointednessChecker.class);
		Mockito.doThrow(RoleClassException.class).when(rdcMock).checkRolesAreDisjoint(argThat(new CollectionMatcher<Class<?>>()));
		
		TrackingKnowledgeWrapper target = new TrackingKnowledgeWrapper(kmMock, rdcMock);
		target.commitChanges();
	}
	
	@Test
	public void commitChangesMultipleTest() throws KnowledgeNotFoundException, KnowledgeUpdateException, RoleClassException, KnowledgeAccessException, KnowledgeCommitException {
		KnowledgeManager kmMock = ReadOnlyKnowledgeWrapperTest.getTestRoleKnowledgeManager();
		Mockito.doReturn(new Class<?>[] {TestRole.class, TestRole2.class}).when(kmMock).getRoles();
		Mockito.doAnswer(new Answer<ValueSet>() {
			@Override
			public ValueSet answer(InvocationOnMock invocation) throws Throwable {
				ValueSet valueSet = new ValueSet();
				valueSet.setValue(TestRole2.kpc, 'c');
				return valueSet;
			} 
		}).when(kmMock).get(argThat(new CollectionMatcher<KnowledgePath>(TestRole2.kpc)));
		
		Mockito.doNothing().when(kmMock).update(any());
		RoleDisjointednessChecker rdcMock = Mockito.mock(RoleDisjointednessChecker.class);
		Mockito.doNothing().when(rdcMock).checkRolesAreDisjoint(any());
		
		TrackingKnowledgeWrapper target = new TrackingKnowledgeWrapper(kmMock, rdcMock);
		TestRole result = target.getTrackedRoleKnowledge(TestRole.class);
		TestRole2 result2 = target.getTrackedRoleKnowledge(TestRole2.class);
		result.i = 44;
		result2.c = 'a';
		
		target.commitChanges();
		
		Mockito.verify(kmMock, times(2)).get(any());
		Mockito.verify(kmMock, times(1)).update(argThat(new ArgumentMatcher<ChangeSet>() {
			@Override
			public boolean matches(Object argument) {
				ChangeSet changeSet = (ChangeSet) argument;
				return changeSet.getUpdatedReferences().size() == 2 && changeSet.getDeletedReferences().isEmpty()
						&& changeSet.getValue(TestRole.kpi).equals(44) && changeSet.getValue(TestRole.kps).equals("test");
			}
		}));
		Mockito.verify(kmMock, times(1)).update(argThat(new ArgumentMatcher<ChangeSet>() {
			@Override
			public boolean matches(Object argument) {
				ChangeSet changeSet = (ChangeSet) argument;
				return changeSet.getUpdatedReferences().size() == 1 && changeSet.getDeletedReferences().isEmpty()
						&& changeSet.getValue(TestRole2.kpc).equals('a');
			}
		}));
		Mockito.verify(rdcMock, times(1)).checkRolesAreDisjoint(argThat(new CollectionMatcher<Class<?>>(TestRole.class, TestRole2.class)));
	}
	
	@Test
	public void resetTrackingTest() throws KnowledgeNotFoundException, RoleClassException, KnowledgeAccessException, KnowledgeCommitException, KnowledgeUpdateException {
		KnowledgeManager kmMock = ReadOnlyKnowledgeWrapperTest.getTestRoleKnowledgeManager();
		RoleDisjointednessChecker rdcMock = Mockito.mock(RoleDisjointednessChecker.class);
		Mockito.doNothing().when(rdcMock).checkRolesAreDisjoint(any());
		
		TrackingKnowledgeWrapper target = new TrackingKnowledgeWrapper(kmMock, rdcMock);
		TestRole result = target.getTrackedRoleKnowledge(TestRole.class);
		result.i = 43;
		result.s = "test2";
		
		target.resetTracking();
		target.commitChanges();
		
		Mockito.verify(kmMock, atMost(1)).update(argThat(new ArgumentMatcher<ChangeSet>() {
			@Override
			public boolean matches(Object argument) {
				ChangeSet changeSet = (ChangeSet) argument;
				return changeSet.getUpdatedReferences().size() == 0 && changeSet.getDeletedReferences().size() == 0;
			}
		}));
		Mockito.verify(rdcMock, times(1)).checkRolesAreDisjoint(argThat(new CollectionMatcher<Class<?>>()));
	}
}
