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
	
	private static class Interval {
		public int low;
		public int high;
		
		public int count() { return high - low + 1; }
		
		public static Interval parseFromString(String intervalStr) throws NumberFormatException {
			Pattern pattern = Pattern.compile("(\\-?[0-9]+)\\.\\.(\\-?[0-9]+)");
			Matcher matcher = pattern.matcher(intervalStr);
			if (!matcher.find()) {
				return null;
			}
			
			Interval result = new Interval();
			result.low = Integer.parseInt(matcher.group(1));
			result.high = Integer.parseInt(matcher.group(2));
			return result;
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T[] getArray1dValue(String varName, Class<T> innerType)
			throws OutputVariableParseException, UnsupportedVariableTypeException {
		String value = getVarValue(varName, Object[].class).replace(" ", "");
		if (value.equalsIgnoreCase("array1d(,[])")) {
			return (T[]) Array.newInstance(innerType, 0);
		}
		
		Pattern pattern = Pattern.compile("array1d\\((.+),\\[(.*)\\]\\)", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(value);
		if (!matcher.find()) {
			throw new OutputVariableParseException(varName, "an array");
		}
		
		Interval interval; 
		try {
			interval = Interval.parseFromString(matcher.group(1));
		} catch (NumberFormatException e) {
			throw new OutputVariableParseException(varName, "an array", "The key set should be in the format LO..HI (LO and HI being integers).", e);
		}
		
		if (interval == null) {
			throw new OutputVariableParseException(varName, "an array", "The key set should be in the format LO..HI.");
		}
		
		String[] values = matcher.group(2).split(",");
		if (values.length != interval.count()) {
			throw new OutputVariableParseException(varName, "an array",
					String.format("The number of the items (%d) does not match to the key set (%d..%d).",
							values.length, interval.low, interval.high));
		}
		
		T[] result = (T[]) Array.newInstance(innerType, interval.count());
		for (int i = 0; i < interval.count(); i++) {
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
		
		Pattern pattern = Pattern.compile("array2d\\((.+),(.+),\\[(.*)\\]\\)", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(value);
		if (!matcher.find()) {
			throw new OutputVariableParseException(varName, "an array");
		}
		
		Interval firstInterval, secondInterval;
		try {
			firstInterval = Interval.parseFromString(matcher.group(1));
			secondInterval = Interval.parseFromString(matcher.group(2));
		} catch (NumberFormatException e) {
			throw new OutputVariableParseException(varName, "an array", "The key set should be in the format LO1..HI1, LO2..HI2 (LO and HI being integers).", e);
		}
		
		if (firstInterval == null || secondInterval == null) {
			throw new OutputVariableParseException(varName, "an array", "The key set should be in the format LO1..HI1, LO2..HI2.");
		}
		
		String[] values = matcher.group(3).split(",");
		if (values.length != firstInterval.count() * secondInterval.count()) {
			throw new OutputVariableParseException(varName, "an array", 
					String.format("The number of the items (%d) does not match to the key set (%d..%d, %d..%d).",
							values.length, firstInterval.low, firstInterval.high, secondInterval.low, secondInterval.high));
		}
		
		T[][] result = (T[][]) Array.newInstance(innerType, firstInterval.count(), secondInterval.count());
		for (int i = 0; i < firstInterval.count(); i++) {
			for (int j = 0; j < secondInterval.count(); j++) {
				result[i][j] = parseValue(varName, values[i * secondInterval.count() + j], innerType);
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
			Interval interval;
			try {
				interval = Interval.parseFromString(value);
				if (interval == null) {
					throw new OutputVariableParseException(varName, "a set of integers");
				}
			} catch (NumberFormatException e) {
				throw new OutputVariableParseException(varName, "an set of integers", "If using the LO..HI format the LO and HI must be integers.");
			}
			
			for (int i = interval.low; i <= interval.high; i++) {
				result.add(i);
			}		
		}
		
		return result;
	}
}
