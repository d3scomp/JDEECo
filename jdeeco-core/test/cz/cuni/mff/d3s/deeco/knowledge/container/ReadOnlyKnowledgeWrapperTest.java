package cz.cuni.mff.d3s.deeco.knowledge.container;

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

import java.util.Collection;

import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import cz.cuni.mff.d3s.deeco.annotations.pathparser.ParseException;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.PathOrigin;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper;
import cz.cuni.mff.d3s.deeco.test.common.CollectionMatcher;


public class ReadOnlyKnowledgeWrapperTest {

	@Test
	public void getComponentIdTest() {
		KnowledgeManager kmMock = Mockito.mock(KnowledgeManager.class);
		Mockito.when(kmMock.getId()).thenReturn("42");
		
		ReadOnlyKnowledgeWrapper target = new ReadOnlyKnowledgeWrapper(kmMock);
		String id = target.getComponentId();
		assertTrue(id.equals("42"));
	}
	
	@Test
	public void hasRoleTest() {
		KnowledgeManager kmMock = Mockito.mock(KnowledgeManager.class);
		Mockito.when(kmMock.getRoles()).thenReturn(new Class<?>[] {String.class, Long.class});
		
		ReadOnlyKnowledgeWrapper target = new ReadOnlyKnowledgeWrapper(kmMock);
		assertTrue(target.hasRole(String.class));
		assertTrue(target.hasRole(Long.class));
		assertFalse(target.hasRole(Integer.class));
		assertFalse(target.hasRole(StringBuilder.class));
	}
	
	public static KnowledgeManager getTestRoleKnowledgeManager() throws KnowledgeNotFoundException {
		KnowledgeManager kmMock = Mockito.mock(KnowledgeManager.class);
		Mockito.doReturn(new Class<?>[] {TestRole.class}).when(kmMock).getRoles();
		Mockito.doAnswer(new Answer<ValueSet>() {
			@Override
			public ValueSet answer(InvocationOnMock invocation) throws Throwable {
				ValueSet valueSet = new ValueSet();
				valueSet.setValue(TestRole.kps, "test");
				valueSet.setValue(TestRole.kpi, 42);
				return valueSet;
			} 
		}).when(kmMock).get(argThat(new CollectionMatcher<KnowledgePath>(TestRole.kps, TestRole.kpi)));
		
		return kmMock;
	}
	
	@Test
	public void getUntrackedRoleKnowledgeTest() throws KnowledgeNotFoundException, ParseException, AnnotationProcessorException, RoleClassException, KnowledgeAccessException {
		KnowledgeManager kmMock = getTestRoleKnowledgeManager();
		ReadOnlyKnowledgeWrapper target = new ReadOnlyKnowledgeWrapper(kmMock);
		TestRole result = target.getUntrackedRoleKnowledge(TestRole.class);
		assertTrue(result.i == 42);
		assertTrue(result.s.equals("test"));
	}
	
	@Test(expected = RoleClassException.class)
	public void getUntrackedRoleKnowledgeBadRoleTest() throws RoleClassException, KnowledgeAccessException {
		KnowledgeManager kmMock = Mockito.mock(KnowledgeManager.class);
		Mockito.when(kmMock.getRoles()).thenReturn(new Class<?>[] {Integer.class});
		
		ReadOnlyKnowledgeWrapper target = new ReadOnlyKnowledgeWrapper(kmMock);
		target.getUntrackedRoleKnowledge(String.class);
	}

}
