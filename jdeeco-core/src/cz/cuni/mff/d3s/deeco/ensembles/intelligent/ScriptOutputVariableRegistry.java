package cz.cuni.mff.d3s.deeco.ensembles.intelligent;

import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScriptOutputVariableRegistry {

	private Map<String, String> outputVariables;
	
	public ScriptOutputVariableRegistry(Map<String, String> outputVariables) {
		this.outputVariables = outputVariables;
	}
	
	private String getVarValue(String varName, Class<?> type) throws OutputVariableParseException {
		String value = outputVariables.get(varName);
		if (value == null) {
			throw new OutputVariableParseException(varName, type.getSimpleName(), "The variable is not present in the output.");
		}
		
		return value;
	}

	@SuppressWarnings("unchecked")
	private <T> T parseValue(String varName, String stringValue, Class<T> type)
			throws OutputVariableParseException, UnsupportedVariableTypeException {
		
		try {	
			if (type == Boolean.class) {
				if (stringValue.equalsIgnoreCase("true")) {
					return (T) new Boolean(true);
				} else if (stringValue.equalsIgnoreCase("false")) {
					return (T) new Boolean(false);
				} else {
					throw new OutputVariableParseException(varName, "Boolean", "Only true/false is accepted as Boolean.");
				}
			
			} else if (type == Integer.class) {
				return (T) Integer.valueOf(stringValue);
			
			} else if (type == Float.class) {
				return (T) Float.valueOf(stringValue);
			
			} else {
				throw new UnsupportedVariableTypeException(type);
			}
			
		} catch (NumberFormatException e) {
			throw new OutputVariableParseException(varName, type.getSimpleName(), e);
		}
	}
	
	private <T> T getPrimitiveValue(String varName, Class<T> type) throws OutputVariableParseException {
		String value = getVarValue(varName, type);

		try {
			T result = parseValue(varName, value, type);
			if (result == null) {
				throw new OutputVariableParseException(varName, type.getSimpleName());
			}
			
			return result;
			
		} catch (UnsupportedVariableTypeException e) {
			assert false; // should not happen (called only from getBoolean/Integer/Float methods, which use safe types)
			e.printStackTrace();
			return null;
		}
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
	
	@SuppressWarnings("unchecked")
	public <T> T[] getArray1dValue(String varName, Class<T> innerType)
			throws OutputVariableParseException, UnsupportedVariableTypeException {
		String value = getVarValue(varName, Object[].class).replace(" ", "");
		if (value.equalsIgnoreCase("array1d(,[])")) {
			return (T[]) Array.newInstance(innerType, 0);
		}
		
		Pattern pattern = Pattern.compile("array1d\\(([0-9]+)\\.\\.([0-9]+),\\[(.*)\\]\\)", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(value);
		if (!matcher.find()) {
			throw new OutputVariableParseException(varName, "an array");
		}
		
		int low, high;
		try {
			low = Integer.parseInt(matcher.group(1));
			high = Integer.parseInt(matcher.group(2));
		} catch (NumberFormatException e) {
			throw new OutputVariableParseException(varName, "an array", "The key set should be in the format LO..HI.");
		}
		
		int count = high - low + 1;
		String[] values = matcher.group(3).split(",");
		if (values.length != count) {
			throw new OutputVariableParseException(varName, "an array",
					String.format("The number of the items (%d) does not match to the key set (%d..%d).",
							values.length, low, high));
		}
		
		T[] result = (T[]) Array.newInstance(innerType, count);
		for (int i = 0; i < count; i++) {
			result[i] = parseValue(varName, values[i], innerType);
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T[][] getArray2dValue(String varName, Class<T> innerType)
			throws OutputVariableParseException, UnsupportedVariableTypeException {
		String value = getVarValue(varName, Object[].class).replace(" ", "");
		if (value.equalsIgnoreCase("array2d(,,[])")) {
			return (T[][]) Array.newInstance(innerType, 0, 0);
		}
		
		Pattern pattern = Pattern.compile("array2d\\(([0-9]+)\\.\\.([0-9]+),([0-9]+)\\.\\.([0-9]+),\\[(.*)\\]\\)", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(value);
		if (!matcher.find()) {
			throw new OutputVariableParseException(varName, "an array");
		}
		
		int low1, high1, low2, high2;
		try {
			low1 = Integer.parseInt(matcher.group(1));
			high1 = Integer.parseInt(matcher.group(2));
			low2 = Integer.parseInt(matcher.group(3));
			high2 = Integer.parseInt(matcher.group(4));
		} catch (NumberFormatException e) {
			throw new OutputVariableParseException(varName, "an array", "The key set should be in the format LO1..HI1, LO2..HI2.");
		}
		
		int count1 = high1 - low1 + 1;
		int count2 = high2 - low2 + 1;
		String[] values = matcher.group(5).split(",");
		if (values.length != count1 * count2) {
			throw new OutputVariableParseException(varName, "an array", 
					String.format("The number of the items (%d) does not match to the key set (%d..%d, %d..%d).",
							values.length, low1, high1, low2, high2));
		}
		
		T[][] result = (T[][]) Array.newInstance(innerType, count1, count2);
		for (int i = 0; i < count1; i++) {
			for (int j = 0; j < count2; j++) {
				result[i][j] = parseValue(varName, values[i * count2 + j], innerType);
			}
		}
		
		return result;
	} 
	
	public Set<Integer> getSetValue(String varName) throws OutputVariableParseException {
		String value = getVarValue(varName, Object[].class).replace(" ", "");
		Set<Integer> result = new HashSet<>();
		if (value.equals("{}")) {
			return result;
		}
		
		Pattern pattern = Pattern.compile("\\{(.*)\\}", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(value);
		if (matcher.find()) {
			// standard pattern (list of numbers separated by comma
			String[] values = matcher.group(1).split(",");	
			
			for (int i = 0; i < values.length; i++) {
				try {
					result.add(parseValue(varName, values[i], Integer.class));
				} catch (UnsupportedVariableTypeException e) {
					assert false; // should not happen (we use integer as the inner type)
					e.printStackTrace();
					return null;
				}
			}
			
		} else {
			// alternative pattern (LO..HI)
			pattern = Pattern.compile("(\\-?[0-9]+)\\.\\.(\\-?[0-9]+)");
			matcher = pattern.matcher(value);
			if (!matcher.find()) {
				throw new OutputVariableParseException(varName, "a set of integers");
			}
			
			int low, high;
			try {
				low = Integer.parseInt(matcher.group(1));
				high = Integer.parseInt(matcher.group(2));
			} catch (NumberFormatException e) {
				throw new OutputVariableParseException(varName, "an set of integers", "If using the LO..HI format the LO and HI must be integers.");
			}
			
			for (int i = low; i <= high; i++) {
				result.add(i);
			}		
		}
		
		return result;
	}
}
