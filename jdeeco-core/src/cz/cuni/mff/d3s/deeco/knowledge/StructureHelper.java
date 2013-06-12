package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class StructureHelper {
	public static Object[] getStructureFromObject(Object value) {
		if (value == null)
			return null;
		Class<?> structure = value.getClass();
		if (TypeUtils.isKnowledge(structure))
			return getStructureFromClass(structure);
		else if (TypeUtils.isMap(structure))
			return getStructureFromMap((Map<String, ?>) value);
		else if (TypeUtils.isList(structure))
			return getStructureFromList((List<?>) value);
		return null;
	}

	public static Object[] getStructureFromClass(Class<?> c) {
		return TypeUtils.getClassFieldNamesAsString(c);
	}

	public static Object[] getStructureFromList(Collection<?> value) {
		Object[] result = new String[value.size()];
		for (int i = 0; i < result.length; i++)
			result[i] = Integer.toString(i);
		return result;
	}

	public static Object[] getStructureFromMap(Map<String, ?> value) {
		return value.keySet().toArray();
	}
}
