/*******************************************************************************
 * Copyright 2012 Charles University in Prague
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package cz.cuni.mff.d3s.deeco.knowledge;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class used to provide utility function for knowledge manager.
 * 
 * @author Michal Kit
 * 
 */
public class KMHelper {

	/**
	 * Checks if the provided type is the <code>Knowledge</code> class.
	 * 
	 * @param type
	 *            type that needs to be checked
	 * @return true or false depending on the check result
	 */
	public static boolean isKnowledge(Type type) {
		Class structure = getClass(type);
		return structure != null && Knowledge.class.isAssignableFrom(structure);
	}

	/**
	 * Checks whether the provided type is either <code>Collection</code> or
	 * <code>Map</code> class.
	 * 
	 * @param type
	 *            type that needs to be checked
	 * @return true or false depending on the check result
	 */
	public static boolean isTraversable(Type type) {
		Class structure = getClass(type);
		return structure != null
				&& (Collection.class.isAssignableFrom(structure) || Map.class
						.isAssignableFrom(structure));
	}

	/**
	 * Returns class instance for the specified type. Handles also generic
	 * types.
	 * 
	 * @param type
	 *            type for which class instance is retrieved
	 * @return class instance for the specified type
	 */
	public static Class<?> getClass(Type type) {
		if (type instanceof Class) {
			return (Class) type;
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

	/**
	 * Retrieves type parameter from the {@link OutWrapper} generic class.
	 * 
	 * @param type
	 *            <code>OutWrapper</code> type valued specification
	 * @return type parameter from the <code>OutWrapper</code> valued
	 *         specification
	 */
	public static Type getOutWrapperParamType(Type type) {
		if (type instanceof ParameterizedType) {
			return ((ParameterizedType) type).getActualTypeArguments()[0];
		} else
			return null;
	}
	
	public static boolean isGenericType(Type type) {
		return type instanceof ParameterizedType;
	}

	/**
	 * Retrieves type parameter from the parameterized type (either
	 * <code>Map</code> or <code>Collection</code>) specification.
	 * 
	 * @param type
	 *            generic type valued specification
	 * @return parameter type from the provided generic specification
	 */
	public static Type getGenericElementType(Type type) {
		Class structure = getClass(type);
		if (Map.class.isAssignableFrom(structure))
			return ((ParameterizedType) type).getActualTypeArguments()[1];
		else if (isGenericType(type)) {
			Type[] typeArguments = ((ParameterizedType) type)
					.getActualTypeArguments();
			if (typeArguments.length == 1) {
				return typeArguments[0];
			}
		}
		return null;
	}

	/**
	 * Adds a single element to either to the <code>Collection</code> or
	 * <code>Map</code>.
	 * 
	 * @param element
	 *            object that needs to be added
	 * @param traversable
	 *            object where the element should be added
	 * @param key
	 *            in case of the map, key which should be assigned to the added
	 *            element
	 */
	public static void addElementToTraversable(Object element,
			Object traversable, String key) {
		if (traversable instanceof List) {
			List lTraversable = (List) traversable;
			lTraversable.add(
					Math.min(lTraversable.size(), Integer.parseInt(key)),
					element);
		} else if (traversable instanceof Collection)
			((Collection) traversable).add(element);
		else if (traversable instanceof Map)
			((Map) traversable).put(key, element);
	}

	/**
	 * Checks if the type is the {@link OutWrapper} class.
	 * 
	 * @param type
	 *            type that needs to be checked
	 * @return true or false depending on the check result
	 */
	public static boolean isOutputWrapper(Type type) {
		Class structure = getClass(type);
		return structure != null
				&& OutWrapper.class.isAssignableFrom(structure);
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
		Class resultC = getClass(type);
		if (resultC != null)
			return resultC.newInstance();
		else
			throw new InstantiationException();
	}

	/**
	 * Converts <code>Collection</code> object to map or in case the input
	 * parameter is <code>Map</code> instance, returns it unchanged.
	 * 
	 * @param object
	 *            object that needs to be converted
	 * @return resulting map
	 */
	public static Map<String, Object> translateToMap(Object object) {
		Class objectClass = object.getClass();
		if (Collection.class.isAssignableFrom(objectClass))
			return getMapFromArray(((Collection) object).toArray());
		else if (Map.class.isAssignableFrom(objectClass))
			return (Map) object;
		return null;
	}

	/**
	 * Converts array of objects to map.
	 * 
	 * @param objects
	 *            array that needs to be converted
	 * @return resulting map
	 */
	private static Map<String, Object> getMapFromArray(Object[] objects) {
		Map<String, Object> result = new HashMap<String, Object>();
		for (int i = 0; i < objects.length; i++) {
			result.put(Integer.toString(i), objects[i]);
		}
		return result;
	}
}
