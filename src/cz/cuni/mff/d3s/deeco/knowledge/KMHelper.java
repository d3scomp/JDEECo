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
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Class used to provide utility function for checking class structure.
 * 
 * @author Michal Kit
 * 
 */
public class KMHelper {

	/**
	 * Function checks whether the given class is a hierarchical structure, i.e.
	 * it contains public fields.
	 * 
	 * @param structure
	 *            class that needs to be checked
	 * @return true or false regarding the class structure
	 */
	public static boolean isHierarchical(Class structure) {
		return structure != null && structure.getFields().length > 0;
	}

	public static boolean isKnowledge(Type type) {
		Class structure = getClass(type);
		return structure != null && Knowledge.class.isAssignableFrom(structure);
	}

	public static boolean isTraversable(Type type) {
		Class structure = getClass(type);
		return structure != null
				&& (Collection.class.isAssignableFrom(structure) || Map.class
						.isAssignableFrom(structure));
	}

	public static boolean isGeneric(Type type) {
		return type != null
				&& (type instanceof ParameterizedType || (type instanceof Class && ((Class) type)
						.getGenericSuperclass() instanceof ParameterizedType));
	}

	public static Class<?> getClass(Type type) {
		if (type instanceof Class) {
			return (Class) type;
		} else if (type instanceof ParameterizedType) {
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
	
	public static Type getOutWrapperParamType(Type type) {
		if (type instanceof ParameterizedType) {
			return ((ParameterizedType) type).getActualTypeArguments()[0];
		} else 
			return null;
	}
	
	public static Type getTraversableElementType(Type type) {
		Class structure = getClass(type);
		if (Collection.class.isAssignableFrom(structure))
			return ((ParameterizedType) type).getActualTypeArguments()[0];
		else if (Map.class.isAssignableFrom(structure))
			return ((ParameterizedType) type).getActualTypeArguments()[1];
		else
			return null;
	}
	
	public static void addElementToTraversable(Object element, Object traversable, String key) {
		if (traversable instanceof Collection)
			((Collection) traversable).add(element);
		else if (traversable instanceof Map)
			((Map) traversable).put(key, element);
	}

	public static boolean isOutputWrapper(Type type) {
		Class structure = getClass(type);
		return structure != null
				&& OutWrapper.class.isAssignableFrom(structure);
	}

	public static Object getInstance(Type type) throws InstantiationException,
			IllegalAccessException {
		Class resultC = getClass(type);
		if (resultC != null)
			return resultC.newInstance();
		else
			throw new InstantiationException();
	}

	public static Field[] getFields(Type type) {
		Class resultC = getClass(type);
		if (resultC != null)
			return resultC.getFields();
		else
			return null;
	}

	public static Map<String, Object> getMapFromArray(Object[] objects) {
		Map<String, Object> result = new HashMap<String, Object>();
		for (int i = 0; i < objects.length; i++) {
			result.put(Integer.toString(i), objects[i]);
		}
		return result;
	}

	public static Map<String, Object> translateToMap(Object object) {
		Class objectClass = object.getClass();
		if (Collection.class.isAssignableFrom(objectClass))
			return getMapFromArray(((Collection) object).toArray());
		else if (Map.class.isAssignableFrom(objectClass))
			return (Map) object;
		return null;
	}
}
