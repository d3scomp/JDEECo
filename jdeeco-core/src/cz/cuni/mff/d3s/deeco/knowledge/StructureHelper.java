package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.processor.TypeUtils;

/**
 * Helper clas used by the knowledge manager for getting the knowledge structure
 * description. Description of the knowledge structure is represented by the
 * array of String containing either field names, keys or indices.
 * 
 * @author Michal
 * 
 */
public class StructureHelper {

	/**
	 * Retrieves the structure description of the given object.
	 * 
	 * @param value
	 *            - an object for which the knowledge description should be
	 *            retrieved for.
	 * @return knowledge description.
	 */
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

	private static Object[] getStructureFromClass(Class<?> c) {
		return TypeUtils.getClassFieldNamesAsString(c);
	}

	private static Object[] getStructureFromList(Collection<?> value) {
		Object[] result = new String[value.size()];
		for (int i = 0; i < result.length; i++)
			result[i] = Integer.toString(i);
		return result;
	}

	private static Object[] getStructureFromMap(Map<String, ?> value) {
		return value.keySet().toArray();
	}
}
