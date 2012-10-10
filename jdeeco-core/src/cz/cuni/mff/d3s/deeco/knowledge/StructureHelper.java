package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class StructureHelper {
	public static String[] getStructureFromObject(Object value) {
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

	public static String[] getStructureFromClass(Class<?> c) {
		return TypeUtils.getClassFieldNamesAsString(c);
	}

	public static String[] getStructureFromList(Collection<?> value) {
		String[] result = new String[value.size()];
		for (int i = 0; i < result.length; i++)
			result[i] = Integer.toString(i);
		return result;
	}

	public static String[] getStructureFromMap(Map<String, ?> value) {
		return (String[]) value.keySet().toArray();
	}
}
