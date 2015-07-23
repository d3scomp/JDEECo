package cz.cuni.mff.d3s.deeco.ensembles.intelligent;

import java.util.Map;
import java.util.Set;

public class ScriptOutputVariableRegistry {

	private Map<String, String> outputVariables;
	
	public ScriptOutputVariableRegistry(Map<String, String> outputVariables) {
		this.outputVariables = outputVariables;
	}

	private <T> T parseValue(String stringValue, Class<T> type) {
		if (type == Boolean.class) {
			// TODO
			return null;
		} else if (type == Integer.class) {
			// TODO
			return null;
		} else if (type == Float.class) {
			// TODO
			return null;
		} else {
			assert false;
			return null;
		}
	}
	
	private <T> T getPrimitiveValue(String varName, Class<T> type) throws OutputVariableParseException {
		String value = outputVariables.get(varName);
		if (value == null) {
			throw new OutputVariableParseException(varName, type, "The variable is not present in the output.");
		}
		
		return parseValue(value, type);
	}
	
	public Boolean getBooleanValue(String varName) throws OutputVariableParseException {
		return getPrimitiveValue(varName, Boolean.class);
	}
	
	public Integer getIntegerValue(String varName) throws OutputVariableParseException {
		return getPrimitiveValue(varName, Integer.class);
	}
	
	public Float getFloatValue(String varName) throws OutputVariableParseException {
		return getPrimitiveValue(varName, Float.class);
	}
	
	public <T> T[] getArray1dValue(String varName, Class<T> innerType)
			throws OutputVariableParseException, UnsupportedVariableTypeException {
		// TODO
		return null;
	}
	
	public <T> T[][] getArray2dValue(String varName, Class<T> innerType)
			throws OutputVariableParseException, UnsupportedVariableTypeException {
		// TODO
		return null;
	} 
	
	public <T> Set<T> getSetValue(String varName, Class<T> innerType)
			throws OutputVariableParseException, UnsupportedVariableTypeException {
		// TODO
		return null;
	}
}
