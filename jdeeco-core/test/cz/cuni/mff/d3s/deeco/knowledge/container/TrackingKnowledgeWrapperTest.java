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

import java.util.Collection;

import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import cz.cuni.mff.d3s.deeco.annotations.pathparser.ParseException;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeUpdateException;

public class TrackingKnowledgeWrapperTest {
	
	@Test
	public void getTrackedRoleKnowledgeTest() throws KnowledgeNotFoundException, ParseException, AnnotationProcessorException, RoleClassException, KnowledgeAccessException {
		KnowledgeManager kmMock = ReadOnlyKnowledgeWrapperTest.getTestRoleKnowledgeManager();		
		TrackingKnowledgeWrapper target = new TrackingKnowledgeWrapper(kmMock);
		TestRole result = target.getTrackedRoleKnowledge(TestRole.class);
		assertTrue(result.i == 42);
		assertTrue(result.s.equals("test"));
	}
	
	@Test(expected = RoleClassException.class)
	public void getTrackedRoleKnowledgeBadRoleTest() throws RoleClassException, KnowledgeAccessException {
		KnowledgeManager kmMock = Mockito.mock(KnowledgeManager.class);
		Mockito.when(kmMock.getRoles()).thenReturn(new Class<?>[] {Integer.class});
		TrackingKnowledgeWrapper target = new TrackingKnowledgeWrapper(kmMock);
		target.getTrackedRoleKnowledge(String.class);
	}
	
	@Test
	public void commitChangesTest() throws KnowledgeNotFoundException, KnowledgeUpdateException, RoleClassException, KnowledgeAccessException {
		KnowledgeManager kmMock = ReadOnlyKnowledgeWrapperTest.getTestRoleKnowledgeManager();
		Mockito.doNothing().when(kmMock).update(any()); // TODO use matcher (also in ReadOnlyKnowledgeWrapper.getTestRoleKnowledge)
		
		TrackingKnowledgeWrapper target = new TrackingKnowledgeWrapper(kmMock);
		TestRole result = target.getTrackedRoleKnowledge(TestRole.class);
		
	}

}
