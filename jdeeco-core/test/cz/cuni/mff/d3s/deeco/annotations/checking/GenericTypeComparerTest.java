package cz.cuni.mff.d3s.deeco.annotations.checking;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class GenericTypeComparerTest {
	
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void basicTypesComparisonTest() {
		
		class C { };
		
		GenericTypeComparer comparer = new GenericTypeComparer();
		assertTrue(comparer.compareTypes(Integer.class, Integer.class));
		assertTrue(comparer.compareTypes(String.class, String.class));
		assertTrue(comparer.compareTypes(long.class, long.class));
		assertTrue(comparer.compareTypes(C.class, C.class));
		
		assertFalse(comparer.compareTypes(Integer.class, String.class));
		assertFalse(comparer.compareTypes(String.class, Integer.class));
		assertFalse(comparer.compareTypes(int.class, Integer.class));
		assertFalse(comparer.compareTypes(Integer.class, int.class));
		assertFalse(comparer.compareTypes(C.class, String.class));
	}
	
	private interface _I { };
	
	@Test
	public void inheritanceTest() {
		// comparison of inherited types (should not work, inherited types are different)
		
		class A { };
		class B extends A { };
		class C implements _I { };
		
		GenericTypeComparer comparer = new GenericTypeComparer();
		assertTrue(comparer.compareTypes(_I.class, _I.class));
		assertTrue(comparer.compareTypes(A.class, A.class));
		assertTrue(comparer.compareTypes(B.class, B.class));
		assertTrue(comparer.compareTypes(C.class, C.class));
		
		assertFalse(comparer.compareTypes(A.class, B.class));
		assertFalse(comparer.compareTypes(B.class, A.class));
		assertFalse(comparer.compareTypes(C.class, _I.class));
		assertFalse(comparer.compareTypes(_I.class, C.class));
	}
	
	interface _List_I extends List<_I> { };
	
	@Test
	public void genericsTest() {
		// comparison of generic types (should work correctly, ie. List<Integer> != List<String>, etc.)
		
		class A { };
		class B extends A { };
		class C implements _I { };
		
		class List_A extends ArrayList<A> { };
		class List_B extends ArrayList<B> { };
		class List_C extends ArrayList<C> { };
		class List_Integer extends ArrayList<Integer> { };
		
		GenericTypeComparer comparer = new GenericTypeComparer();
		assertTrue(comparer.compareTypes(List_Integer.class.getGenericSuperclass(), List_Integer.class.getGenericSuperclass()));
		assertTrue(comparer.compareTypes(List_A.class.getGenericSuperclass(), List_A.class.getGenericSuperclass()));
		assertTrue(comparer.compareTypes(List_B.class.getGenericSuperclass(), List_B.class.getGenericSuperclass()));
		assertTrue(comparer.compareTypes(List_C.class.getGenericSuperclass(), List_C.class.getGenericSuperclass()));
		assertTrue(comparer.compareTypes(_List_I.class.getGenericInterfaces()[0], _List_I.class.getGenericInterfaces()[0]));
		
		assertFalse(comparer.compareTypes(List_Integer.class.getGenericSuperclass(), List_A.class.getGenericSuperclass()));
		assertFalse(comparer.compareTypes(List_A.class.getGenericSuperclass(), List_Integer.class.getGenericSuperclass()));
		assertFalse(comparer.compareTypes(List_A.class.getGenericSuperclass(), List_B.class.getGenericSuperclass()));
		assertFalse(comparer.compareTypes(List_B.class.getGenericSuperclass(), List_A.class.getGenericSuperclass()));
		assertFalse(comparer.compareTypes(List_C.class.getGenericSuperclass(), _List_I.class.getGenericInterfaces()[0]));
		assertFalse(comparer.compareTypes(_List_I.class.getGenericInterfaces()[0], List_C.class.getGenericSuperclass()));
	}
	
	@Test
	public void genericsGenericsTest() {
		
		class List_Map_Integer_String extends ArrayList<HashMap<Integer, String>> { };
		class List_Map_Integer_Integer extends ArrayList<HashMap<Integer, Integer>> { };
		
		GenericTypeComparer comparer = new GenericTypeComparer();
		assertTrue(comparer.compareTypes(List_Map_Integer_Integer.class.getGenericSuperclass(), List_Map_Integer_Integer.class.getGenericSuperclass()));
		assertTrue(comparer.compareTypes(List_Map_Integer_String.class.getGenericSuperclass(), List_Map_Integer_String.class.getGenericSuperclass()));
		
		assertFalse(comparer.compareTypes(List_Map_Integer_String.class.getGenericSuperclass(), List_Map_Integer_Integer.class.getGenericSuperclass()));
		assertFalse(comparer.compareTypes(List_Map_Integer_Integer.class.getGenericSuperclass(), List_Map_Integer_String.class.getGenericSuperclass()));
	}
	
	@Test
	public void genericsWithUnknownsTest() throws NoSuchFieldException, SecurityException {
		// comparisons with unknown generic types (ie. that List<Integer> == List<T>, etc.)
		
		class A { };
		class B { };
		class G<T> { 
			public T x;
			public ArrayList<T> y;
			public T[] z;
			public A[] a;
			public B[] b;
		};
		
		Type T_type = G.class.getField("x").getGenericType();
		Type List_T_type = G.class.getField("y").getGenericType();
		class List_A extends ArrayList<A> { };
		Type T_array_type = G.class.getField("z").getGenericType();
		Type A_array_type = G.class.getField("a").getGenericType();
		Type B_array_type = G.class.getField("b").getGenericType();
		
		GenericTypeComparer comparer = new GenericTypeComparer();
		// unknown type is equal to any type
		assertTrue(comparer.compareTypes(T_type, T_type));
		assertTrue(comparer.compareTypes(T_type, A.class));
		assertTrue(comparer.compareTypes(T_type, B.class));
		assertTrue(comparer.compareTypes(A.class, T_type));
		
		assertTrue(comparer.compareTypes(List_T_type, List_T_type));
		assertTrue(comparer.compareTypes(List_T_type, List_A.class.getGenericSuperclass()));
		assertTrue(comparer.compareTypes(List_A.class.getGenericSuperclass(), List_T_type));
		
		assertTrue(comparer.compareTypes(T_array_type, T_array_type));
		assertTrue(comparer.compareTypes(T_array_type, A_array_type));
		assertTrue(comparer.compareTypes(A_array_type, T_array_type));
		
		assertFalse(comparer.compareTypes(List_T_type, T_array_type));
		assertFalse(comparer.compareTypes(List_T_type, A_array_type));
		assertFalse(comparer.compareTypes(List_A.class.getGenericSuperclass(), T_array_type));	
		assertFalse(comparer.compareTypes(A_array_type, B_array_type));
		assertFalse(comparer.compareTypes(B_array_type, A_array_type));
	}
	

}
