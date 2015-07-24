package cz.cuni.mff.d3s.deeco.ensembles.intelligent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ScriptOutputVariableRegistryTest {

	Map<String, String> outputVariables;
	ScriptOutputVariableRegistry target;
	
	@Before
	public void initialize() {
		outputVariables = new HashMap<>();
		outputVariables.put("b1", "true");
		outputVariables.put("b2", "false");
		outputVariables.put("i1", "42");
		outputVariables.put("i2", "0");
		outputVariables.put("f1", "3.14");
		outputVariables.put("f2", "0.0");
		outputVariables.put("a1d1", "array1d(1..2, [4, 5])");
		outputVariables.put("a1d2", "array1d(1..4, [false, true])");
		outputVariables.put("unsupp", "array1d(1..3, [a, b, c])");
		outputVariables.put("a2d", "array2d(1..2 ,1..3 ,[2, 3, 4, 3, 4, 5])");
		outputVariables.put("s1", "{2, 4}");
		outputVariables.put("s2", "3..6");
		outputVariables.put("s3", "{2.4,4.2}");
		
		target = new ScriptOutputVariableRegistry(outputVariables);
	}
	
	@Test
	public void getBooleanValueTest() throws OutputVariableParseException {
		Boolean b1 = target.getBooleanValue("b1");
		Boolean b2 = target.getBooleanValue("b2");
		
		assertEquals(b1, true);
		assertEquals(b2, false);
	}

	@Test
	public void getIntegerValueTest() throws OutputVariableParseException {
		Integer i1 = target.getIntegerValue("i1");
		Integer i2 = target.getIntegerValue("i2");
		
		assertEquals(i1, new Integer(42));
		assertEquals(i2, new Integer(0));
	}
	
	@Test
	public void getFloatValueTest() throws OutputVariableParseException {
		Float f1 = target.getFloatValue("f1");
		Float f2 = target.getFloatValue("f2");
		
		assertEquals(f1, new Float(3.14f));
		assertEquals(f2, new Float(0f));
	}
	
	@Test
	public void getArray1dValueTest() throws OutputVariableParseException, UnsupportedVariableTypeException {
		Integer[] a1d1 = target.getArray1dValue("a1d1", Integer.class);
		Boolean[] a1d2 = target.getArray1dValue("a1d2", Boolean.class);
		
		Assert.assertArrayEquals(new Integer[] {4, 5}, a1d1);
		Assert.assertArrayEquals(new Boolean[] {false, true}, a1d2);
	}
	
	@Test(expected = OutputVariableParseException.class)
	public void getArray1dWrongInnerTypeTest() throws OutputVariableParseException, UnsupportedVariableTypeException {
		target.getArray1dValue("a1d1", Boolean.class);
	}
	
	@Test(expected = UnsupportedVariableTypeException.class)
	public void getArray1dInvalidInnerTypeTest() throws OutputVariableParseException, UnsupportedVariableTypeException {
		target.getArray1dValue("unsupp", String.class);
	}
	
	@Test
	public void getArray2dValueTest() throws OutputVariableParseException, UnsupportedVariableTypeException {
		Integer[][] a2d = target.getArray2dValue("a2d", Integer.class);
		Assert.assertArrayEquals(new Integer[][] {{2, 3, 4}, {3, 4, 5}}, a2d);
	}
	
	@Test
	public void getSetValueTest() throws OutputVariableParseException, UnsupportedVariableTypeException {
		Set<Integer> s1 = target.getSetValue("s1");
		Set<Integer> s2 = target.getSetValue("s2");

		assertEquals(2, s1.size());
		assertTrue(s1.contains(2));
		assertTrue(s1.contains(4));

		assertEquals(4, s2.size());
		assertTrue(s2.contains(3));
		assertTrue(s2.contains(4));
		assertTrue(s2.contains(5));
		assertTrue(s2.contains(6));
	}
	
	@Test(expected = OutputVariableParseException.class)
	public void getSetValueWrongInnerTypeTest() throws OutputVariableParseException, UnsupportedVariableTypeException {
		target.getSetValue("s3"); // not a set of Integer, which is the only supported inner type for sets
	}
}
