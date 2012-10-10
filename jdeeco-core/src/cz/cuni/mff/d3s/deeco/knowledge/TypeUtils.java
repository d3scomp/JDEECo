package cz.cuni.mff.d3s.deeco.knowledge;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class TypeUtils {

	/**
	 * Returns class instance for the specified type. Handles also generic
	 * parametricTypes.
	 * 
	 * @param type
	 *            type for which class instance is retrieved
	 * @return class instance for the specified type
	 */
	public static Class<?> getClass(Type type) {
		if (type instanceof Class) {
			return (Class<?>) type;
		} else if (isGenericType(type)) {
			return getClass(((ParameterizedType) type).getRawType());
		} else if (type instanceof GenericArrayType) {
			Type componentType = ((GenericArrayType) type)
					.getGenericComponentType();
			Class<?> componentClass = getClass(componentType);
			if (componentClass != null) {
				return Array.newInstance(componentClass, 0).getClass();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public static boolean isGenericType(Type type) {
		return type instanceof ParameterizedType;
	}

	public static String[] getClassFieldNamesAsString(Class<?> c) {
		if (c == null)
			return null;
		Field [] fields = c.getFields();
		if (fields == null || fields.length == 0)
			return null;
		String [] result = new String [fields.length];
		for (int i = 0; i < fields.length; i++) {
			result[i] = fields[i].getName();
		}
		return result;
	}
	
	public static Field[] getClassFields(Class<?> c) {
		if (c == null)
			return null;
		Field [] fields = c.getFields();
		if (fields == null || fields.length == 0)
			return null;
		return fields;
	}

	/**
	 * Retrieves type parameter from the parameterized type (either
	 * <code>Map</code> or <code>Collection</code>) specification.
	 * 
	 * @param type
	 *            generic type valued specification
	 * @return parameter type from the provided generic specification
	 */
	public static Type[] getGenericElementType(Type type) {
		if (isGenericType(type)) {
			return ((ParameterizedType) type).getActualTypeArguments();
		}
		return null;
	}
	
	/**
	 * Checks if the provided type is the <code>Knowledge</code> class.
	 * 
	 * @param type
	 *            type that needs to be checked
	 * @return true or false depending on the check result
	 */
	public static boolean isKnowledge(Class<?> structure) {
		return structure != null && Knowledge.class.isAssignableFrom(structure);
	}
	
	public static boolean isList(Class<?> structure) {
		return structure != null && List.class.isAssignableFrom((Class<?>) structure);
	}
	
	public static boolean isMap(Class<?> structure) {
		return structure != null && Map.class.isAssignableFrom((Class<?>) structure);
	}
	
	/**
	 * Checks if the type is the {@link OutWrapper} class.
	 * 
	 * @param type
	 *            type that needs to be checked
	 * @return true or false depending on the check result
	 */
	public static boolean isOutWrapper(Class<?> structure) {
		return structure != null && OutWrapper.class.isAssignableFrom(structure);
	}

	/**
	 * Creates the object instance for the specified type.
	 * 
	 * @param type
	 *            type that needs to be instantiated
	 * @return type instance.
	 * @throws InstantiationException
	 *             thrown whenever the instantiation
	 * @throws IllegalAccessException
	 *             thrown whenever type cannot be instantiated (as class object
	 *             cannot be obtained)
	 */
	public static Object getInstance(Type type) throws InstantiationException,
			IllegalAccessException {
		Class<?> resultC = TypeUtils.getClass(type);
		if (resultC != null)
			return resultC.newInstance();
		else
			throw new InstantiationException();
	}
	
	public static boolean isInstanceOf(Class<?> clazz, Object value) {
		if (value == null)
			return true;
		if (clazz == null)
			return false;
		return clazz.isAssignableFrom(value.getClass());
	}

}
