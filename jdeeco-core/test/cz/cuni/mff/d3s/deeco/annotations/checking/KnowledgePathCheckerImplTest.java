package cz.cuni.mff.d3s.deeco.annotations.checking;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.refEq;
import static org.mockito.Mockito.times;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import cz.cuni.mff.d3s.deeco.annotations.Role;
import cz.cuni.mff.d3s.deeco.annotations.checking.KnowledgePathCheckException;
import cz.cuni.mff.d3s.deeco.annotations.checking.KnowledgePathCheckerImpl;
import cz.cuni.mff.d3s.deeco.annotations.checking.TypeComparer;
import cz.cuni.mff.d3s.deeco.annotations.checking.KnowledgePathCheckerImpl.PathNodeCheckingException;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.ParseException;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.PathOrigin;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNode;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper;

public class KnowledgePathCheckerImplTest {
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	private class TestException extends Exception {
		public TestException(Throwable cause) {
			super(cause);
		}
	}
	
	private List<PathNode> pathNodeListFromString(String str) throws TestException {
		try {
			return KnowledgePathHelper.createKnowledgePath(str, PathOrigin.COMPONENT).getNodes();
		} catch (ParseException | AnnotationProcessorException e) {
			throw new TestException(e);
		}
	}
	
	/*
	 * Tests for isFieldInClass method.
	 */
	
	@Test
	public void isFieldInClassTest() throws KnowledgePathCheckException, PathNodeCheckingException, TestException {
		TypeComparer mock = Mockito.mock(TypeComparer.class);
		Mockito.when(mock.compareTypes(eq(Long.class), any())).thenReturn(true);
		Mockito.when(mock.compareTypes(eq(Short.class), any())).thenReturn(false);
		
		KnowledgePathCheckerImpl checker = Mockito.spy(new KnowledgePathCheckerImpl(mock));
		Mockito.doReturn(Integer.class).when(checker).getTypeInClass(any(), any());
		
		List<PathNode> list1 = pathNodeListFromString("x");
		List<PathNode> list2 = pathNodeListFromString("y");
		Boolean result1 = checker.isFieldInClass(Long.class, list1, String.class);
		Boolean result2 = checker.isFieldInClass(Short.class, list2, Object.class);
		
		Mockito.verify(checker, times(2)).isFieldInClass(any(), any(), any());
		Mockito.verify(checker, times(1)).getTypeInClass(refEq(list1), eq(String.class));
		Mockito.verify(checker, times(1)).getTypeInClass(refEq(list2), eq(Object.class));
		Mockito.verifyNoMoreInteractions(checker);
		
		Mockito.verify(mock, times(1)).compareTypes(Long.class, Integer.class);
		Mockito.verify(mock, times(1)).compareTypes(Short.class, Integer.class);
		Mockito.verifyNoMoreInteractions(mock);
		
		assertTrue(result1);
		assertFalse(result2);
	}
	
	@Test
	public void isFieldInClassEmptyKnowledgePathTest() throws KnowledgePathCheckException, PathNodeCheckingException {
		KnowledgePathCheckerImpl checker = Mockito.spy(new KnowledgePathCheckerImpl(null));
		Mockito.doReturn(Integer.class).when(checker).getTypeInClass(any(), any());
		
		List<PathNode> list = new ArrayList<>();
		KnowledgePathCheckException thrown = null;
		
		// save the exception, so that we can test the mock interactions (and then throw it)
		try {
			checker.isFieldInClass(Long.class, list, String.class);
		} catch (KnowledgePathCheckException e) {
			thrown = e;
		}
		
		exception.expect(KnowledgePathCheckException.class);
		exception.expectMessage("The field sequence cannot be null or empty.");
		
		Mockito.verify(checker, times(1)).isFieldInClass(any(), any(), any());
		Mockito.verifyNoMoreInteractions(checker);
		
		if (thrown != null)
			throw thrown;
	}
	
	@Test
	public void isFieldInClassNullKnowledgePathTest() throws KnowledgePathCheckException {
		exception.expect(KnowledgePathCheckException.class);
		exception.expectMessage("The field sequence cannot be null or empty.");		
		
		KnowledgePathCheckerImpl checker = new KnowledgePathCheckerImpl(null);
		checker.isFieldInClass(Integer.class, null, String.class);
	}
	
	@Test
	public void isFieldInClassNullClassTest() throws KnowledgePathCheckException, TestException {
		List<PathNode> list1 = pathNodeListFromString("x");

		exception.expect(KnowledgePathCheckException.class);
		exception.expectMessage("The role class cannot be null.");

		KnowledgePathCheckerImpl checker = new KnowledgePathCheckerImpl(null);
		checker.isFieldInClass(Integer.class, list1, null);
	}
	
	@Test
	public void isFieldInClassNullTypeTest() throws KnowledgePathCheckException, PathNodeCheckingException, TestException {
		// this is correct
		TypeComparer mock = Mockito.mock(TypeComparer.class);
		Mockito.when(mock.compareTypes(any(), any())).thenReturn(false);
		
		KnowledgePathCheckerImpl checker = Mockito.spy(new KnowledgePathCheckerImpl(mock));
		Mockito.doReturn(Integer.class).when(checker).getTypeInClass(any(), any());
		
		List<PathNode> list1 = pathNodeListFromString("x");
		List<PathNode> list2 = pathNodeListFromString("y");
		Boolean result1 = checker.isFieldInClass(null, list1, String.class);
		Boolean result2 = checker.isFieldInClass(null, list2, Object.class);
		
		assertTrue(result1);
		assertTrue(result2);
	}
	
	@Test
	public void isFieldInClassBadKnowledgePathTest() throws KnowledgePathCheckException, ParseException, AnnotationProcessorException {
		List<PathNode> list1 = KnowledgePathHelper.createKnowledgePath("coord.x", PathOrigin.ENSEMBLE).getNodes();
		String coordToStr = list1.get(0).toString();
		
		exception.expect(KnowledgePathCheckException.class);
		exception.expectMessage("Knowledge path '" + coordToStr + ".x': Invalid path node " + coordToStr
				+ ". Only " + PathNodeField.class.getSimpleName() + " instances are expected.");
		
		KnowledgePathCheckerImpl checker = new KnowledgePathCheckerImpl(null);
		checker.isFieldInClass(null, list1, String.class);				
	}	
	
	@Test
	public void isFieldInClassBadKnowledgePath2Test() throws KnowledgePathCheckException, TestException {
		class RoleClass {
			public HashMap<Integer, Integer> x;
		}
		
		List<PathNode> list1 = pathNodeListFromString("x.[y]");
		
		exception.expect(KnowledgePathCheckException.class);
		exception.expectMessage("Knowledge path 'x.[y]': Invalid path node [y]. Only "
				+ PathNodeField.class.getSimpleName() + " instances are expected.");
		
		KnowledgePathCheckerImpl checker = new KnowledgePathCheckerImpl(null);
		checker.isFieldInClass(null, list1, RoleClass.class);				
	}
	
	/*
	 * Tests for getTypeInClass method
	 */
	
	@Test
	public void getTypeInClassTest() throws KnowledgePathCheckException, TestException, PathNodeCheckingException {
		@Role
		class RoleClass {
			public Integer x;
			protected Integer z;
		}
		
		KnowledgePathCheckerImpl checker = new KnowledgePathCheckerImpl(null);
		assertEquals(Integer.class, checker.getTypeInClass(pathNodeListFromString("x"), RoleClass.class));
		assertNull(checker.getTypeInClass(pathNodeListFromString("y"), RoleClass.class));
		assertNull(checker.getTypeInClass(pathNodeListFromString("z"), RoleClass.class));
	}
	
	@Test
	public void getTypeInClassIdTest() throws PathNodeCheckingException, TestException {
		@Role
		class RoleClass {
			public Integer x;
		}
		
		KnowledgePathCheckerImpl checker = new KnowledgePathCheckerImpl(null);
		assertEquals(String.class, checker.getTypeInClass(pathNodeListFromString("id"), RoleClass.class));
		assertNull(checker.getTypeInClass(pathNodeListFromString("x.id"), RoleClass.class));
	}
	
	@Test
	public void isFieldInRoleMultilevelTest() throws PathNodeCheckingException, TestException {
		class Structured1 {
			public Integer a;
			public Integer b;
		}
		
		class Structured2 {
			public Integer u;
			public Structured1 v;
			protected Integer w;
		}
		
		@Role
		class RoleClass {
			public Integer x;
			public Structured2 y;
			public long[] z;
		}
		
		KnowledgePathCheckerImpl checker = new KnowledgePathCheckerImpl(null);
		assertEquals(Integer.class, checker.getTypeInClass(pathNodeListFromString("x"), RoleClass.class));
		assertEquals(Structured2.class, checker.getTypeInClass(pathNodeListFromString("y"), RoleClass.class));
		assertEquals(Integer.class, checker.getTypeInClass(pathNodeListFromString("y.u"), RoleClass.class));
		assertEquals(Structured1.class, checker.getTypeInClass(pathNodeListFromString("y.v"), RoleClass.class));
		assertNull(checker.getTypeInClass(pathNodeListFromString("y.w"), RoleClass.class));
		assertNull(checker.getTypeInClass(pathNodeListFromString("y.t"), RoleClass.class));
		assertEquals(Integer.class, checker.getTypeInClass(pathNodeListFromString("y.v.a"), RoleClass.class));
		assertEquals(Integer.class, checker.getTypeInClass(pathNodeListFromString("y.v.b"), RoleClass.class));
		assertNull(checker.getTypeInClass(pathNodeListFromString("y.v.c"), RoleClass.class));
		assertNull(checker.getTypeInClass(pathNodeListFromString("y.v.a.a"), RoleClass.class));
		assertEquals(long[].class, checker.getTypeInClass(pathNodeListFromString("z"), RoleClass.class));
	}

	interface _I_List_Long extends List<Long> { };
	interface _I_List_Integer extends List<Integer> { };
	interface _I_List_String extends List<String> { };
	interface _I_List_Short extends List<Short> { };
	
	@Test
	public void isFieldInRoleGenericTest() throws PathNodeCheckingException, TestException, NoSuchFieldException, SecurityException {
		class Structured<T> {
			public List<Integer> a;
			public List<T> b;
			public T c;
			public T[] d;
		}
		
		@Role
		class RoleClass {
			public List<Long> x;
			public Structured<String> y;
			public Structured<Short> z;
			public Structured[] sa;
		}
		
		class _Structured_String extends Structured<String> { };
		class _Structured_Short extends Structured<Short> { };
		
		Type List_Long = _I_List_Long.class.getGenericInterfaces()[0];
		Type List_Integer = _I_List_Integer.class.getGenericInterfaces()[0];
		Type List_T = Structured.class.getField("b").getGenericType();
		Type _T = Structured.class.getField("c").getGenericType();
		Type Structured_String = _Structured_String.class.getGenericSuperclass();
		Type Structured_Short = _Structured_Short.class.getGenericSuperclass();
		
		KnowledgePathCheckerImpl checker = new KnowledgePathCheckerImpl(null);
		assertEquals(List_Long, checker.getTypeInClass(pathNodeListFromString("x"), RoleClass.class));
		assertEquals(Structured_String, checker.getTypeInClass(pathNodeListFromString("y"), RoleClass.class));
		assertEquals(List_Integer, checker.getTypeInClass(pathNodeListFromString("y.a"), RoleClass.class));
		assertEquals(List_T, checker.getTypeInClass(pathNodeListFromString("y.b"), RoleClass.class));
		assertEquals(_T, checker.getTypeInClass(pathNodeListFromString("y.c"), RoleClass.class));
		assertEquals(Structured_Short, checker.getTypeInClass(pathNodeListFromString("z"), RoleClass.class));
		assertEquals(List_Integer, checker.getTypeInClass(pathNodeListFromString("z.a"), RoleClass.class));
		assertEquals(List_T, checker.getTypeInClass(pathNodeListFromString("z.b"), RoleClass.class));
		assertEquals(_T, checker.getTypeInClass(pathNodeListFromString("z.c"), RoleClass.class));
		assertEquals(Structured[].class, checker.getTypeInClass(pathNodeListFromString("sa"), RoleClass.class));
		assertNull(checker.getTypeInClass(pathNodeListFromString("sa.a"), RoleClass.class));	
	}
	
}
