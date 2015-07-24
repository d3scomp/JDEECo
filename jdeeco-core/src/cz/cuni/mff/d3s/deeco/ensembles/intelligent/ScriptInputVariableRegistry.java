package cz.cuni.mff.d3s.deeco.ensembles.intelligent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class ScriptInputVariableRegistry {

	class Entry {
		String name;
		String value;
		
		public Entry(String name2, String value2) {
			name = name2;
			value = value2;
		}
		
		@Override
		public String toString() {
			return String.format("%s=%s", name, value);
		}
	}
	
	private List<Entry> inputVariables;
	
	// supported primitive types (for others -> exception)
	// for each there needs to be an overload of the addVariable function
	// supported are also arrays and sets
	public static final Set<Class<?>> supportedPrimitiveTypes
			= new HashSet<Class<?>>(Arrays.asList(Boolean.class, Integer.class, Float.class, ScriptIdentifier.class));

	/**
	 * Just for testing purposes. Use the parameterless constructor instead
	 * @param entryList
	 */
	public ScriptInputVariableRegistry(List<Entry> entryList) {
		this.inputVariables = entryList;
	}
	
	public ScriptInputVariableRegistry() {
		this(new ArrayList<Entry>());
	}
	
	public List<Entry> getInputVariables() {
		return inputVariables;
	}
		
	private void addVariable(String name, Object value) {
		assert supportedPrimitiveTypes.contains(value.getClass());
		inputVariables.add(new Entry(name, String.format(Locale.US, value.toString())));
	}

	public void addVariable(String name, Boolean value) {
		addVariable(name, (Object) value);
	}
	
	public void addVariable(String name, Integer value) {
		addVariable(name, (Object) value);
	}

	public void addVariable(String name, Float value) {
		addVariable(name, (Object) value);
	}

	public void addVariable(String name, ScriptIdentifier value) {
		addVariable(name, (Object) value);
	}
	
	public void addIdentifierVariable(String name, String identifier) {
		addVariable(name, new ScriptIdentifier(identifier));
	}
	
	public void addVariable(String name, Object[] value) throws UnsupportedVariableTypeException {
		// TODO check types
		
		addVariable(name, (Object) value); // arrays have compatible toString
	}
	
	public void addVariable(String name, Object[][] value) throws UnsupportedVariableTypeException {
		// TODO
	}
	
	public void addVariable(String name, Set<Integer> value) throws UnsupportedVariableTypeException {
		// TODO
	}

}
