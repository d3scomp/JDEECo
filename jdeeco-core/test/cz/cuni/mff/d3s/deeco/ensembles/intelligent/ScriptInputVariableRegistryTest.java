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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.InOrder;
import org.mockito.Mockito;

import cz.cuni.mff.d3s.deeco.ensembles.intelligent.ScriptInputVariableRegistry.Entry;

public class ScriptInputVariableRegistryTest {

	interface EntryList extends List<Entry> {
		// just to avoid warnings
	}
	
	class EntryMatcher extends ArgumentMatcher<Entry> {
		String expectedName;
		String expectedValue;
		
		public EntryMatcher(String expectedName, String expectedValue) {
			this.expectedName = expectedName;
			this.expectedValue = expectedValue;
		}
		
		@Override
		public boolean matches(Object argument) {
			if (argument instanceof Entry) {
				Entry entryArgument = (Entry) argument;
				return entryArgument.name.equals(expectedName) && entryArgument.value.equals(expectedValue);
			} else {
				return false;
			}
		}
		
		@Override
		public void describeTo(Description description) {
			description.appendText(String.format("Entry with name=%s, value=%s", expectedName, expectedValue));
		}
	}
	
	List<Entry> entriesMock;
	ScriptInputVariableRegistry target;
	
	private void verifyInteractions(EntryMatcher... entryMatchers) {
		InOrder inOrder = Mockito.inOrder(entriesMock);
		for(EntryMatcher entryMatcher : entryMatchers) {
			inOrder.verify(entriesMock).add(argThat(entryMatcher));
		}
		
		inOrder.verifyNoMoreInteractions();
	}
	
	@Before
	public void initialize() {
		entriesMock = Mockito.mock(EntryList.class);
		target = new ScriptInputVariableRegistry(entriesMock);
	}
	
	@Test
	public void addVariableBooleanTest() {
		target.addVariable("b1", true);
		target.addVariable("b2", false);
		
		verifyInteractions(new EntryMatcher("b1", "true"), new EntryMatcher("b2", "false"));
	}
	
	@Test
	public void addVariableIntegerTest() {
		target.addVariable("i1", 42);
		target.addVariable("i2", 0);
		
		verifyInteractions(new EntryMatcher("i1", "42"), new EntryMatcher("i2", "0"));
	}
	
	@Test
	public void addVariableFloatTest() {
		target.addVariable("f1", 3.14f);
		target.addVariable("f2", 0f);
		
		verifyInteractions(new EntryMatcher("f1", "3.14"), new EntryMatcher("f2", "0.0"));
	}
	
	@Test
	public void addVariableIdentifierTest() {
		target.addVariable("id1", new ScriptIdentifier("blue"));
		target.addIdentifierVariable("id2", "maroon");
		
		verifyInteractions(new EntryMatcher("id1", "blue"), new EntryMatcher("id2", "maroon"));
	}
	
	@Test
	public void addVariableArray1dTest() throws UnsupportedVariableTypeException, HeterogeneousArrayException {
		target.addVariable("a1d1", new Integer[] {12, 13});
		target.addVariable("a1d2", new Object[] {new ScriptIdentifier("train"), new ScriptIdentifier("car")});
		
		verifyInteractions(new EntryMatcher("a1d1", "[12,13]"), new EntryMatcher("a1d2", "[train,car]"));
	}
	
	@Test(expected = UnsupportedVariableTypeException.class)
	public void addVariableArray1dWrongInnerTypeTest() throws UnsupportedVariableTypeException, HeterogeneousArrayException {
		target.addVariable("a", new String[] {"x", "y"});
	}
	
	@Test(expected = HeterogeneousArrayException.class)
	public void addVariableArray1dHeteroTest() throws UnsupportedVariableTypeException, HeterogeneousArrayException {
		target.addVariable("a", new Object[] {new Integer(14), new Integer(15), new Float(16.1f)});
	}
	
	@Test
	public void addVariableArray1dEmptyTest() throws UnsupportedVariableTypeException, HeterogeneousArrayException {
		target.addVariable("a1d", new Integer[0]);
		verifyInteractions(new EntryMatcher("a1d", "[]"));
	}
	
	@Test
	public void addVariableArray2dTest() throws UnsupportedVariableTypeException, HeterogeneousArrayException {
		target.addVariable("a2d", new Float[][] {{4f, 5f, 6f}, {0.5f, 1f, 0f}});
		verifyInteractions(new EntryMatcher("a2d", "[|4.0,5.0,6.0|0.5,1.0,0.0|]"));
	}
	
	@Test(expected = HeterogeneousArrayException.class)
	public void addVariableArray2dHeteroTest() throws UnsupportedVariableTypeException, HeterogeneousArrayException {
		target.addVariable("a2d", new Float[][] {{4f, 5f}, {0.5f, 1f, 0f}});
	}

	@Test
	public void addVariableArray2dEmptyTest() throws UnsupportedVariableTypeException, HeterogeneousArrayException {
		target.addVariable("a1d", new Integer[0][0]);
		verifyInteractions(new EntryMatcher("a1d", "[]"));
	}
	
	@Test
	public void addVariableIntegerSetTest() throws UnsupportedVariableTypeException {
		target.addVariable("s1", new HashSet<Integer>(Arrays.asList(3)));
		target.addVariable("s2", new HashSet<Integer>(Arrays.asList(7, 8)));
		target.addVariable("s3", new HashSet<Integer>());
		verifyInteractions(new EntryMatcher("s1", "{3}"),
				new EntryMatcher("s2", "{7,8}"), // NOTE: this is implementation-specific - {8, 7} is also correct
				new EntryMatcher("s3", "{}"));
	}
}
