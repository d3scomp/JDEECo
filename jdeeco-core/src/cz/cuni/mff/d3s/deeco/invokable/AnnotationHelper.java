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
package cz.cuni.mff.d3s.deeco.invokable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.AnnotationProxy;
import cz.cuni.mff.d3s.deeco.annotations.IValuedAnnotation;

/**
 * AnnotationHelper provides static methods which are used for annotation
 * parsing.
 * 
 * @author Michal Kit
 * 
 */
public class AnnotationHelper {

	/**
	 * Searches for annotation instance of the type specified by
	 * <code>annotationClass</code> in the array of annotations.
	 * 
	 * @param annotationClass
	 *            type of the annotation to be searched for.
	 * @param annotations
	 *            array of annotation instances where the search is performed
	 * @return found annotation instance or null if the search fails
	 */
	public static Annotation getAnnotation(Class<?> annotationClass,
			Annotation[] annotations) {
		for (Annotation a : annotations) {
			if (annotationClass.isInstance(a)) {
				return a;
			}
		}
		return null;
	}
	
	public static List<Integer> getAnnotationOuterIndecies(Class<?> annotationClass, Annotation [][] annotations) {
		List<Integer> result = new ArrayList<Integer>();
		for (int i = 0; i < annotations.length; i++) {
			for (int k = 0; k < annotations[i].length; k++) {
				if (annotationClass.isInstance(annotations[i][k]))
					result.add(i);
			}
		}
		return result;
	}

	/**
	 * Returns list of methods in the class definition which are annotated with
	 * the given annotation class.
	 * 
	 * @param c
	 *            class definition to be parsed
	 * @param annotationClass
	 *            class of annotation that should be considered during the
	 *            parsing
	 * @return list of methods
	 */
	public static List<Method> getAnnotatedMethods(Class<?> c,
			Class<? extends Annotation> annotationClass) {
		List<Method> result = new ArrayList<Method>();
		if (c != null) {
			Method[] methods = c.getMethods();
			for (Method m : methods) {
				if (m.getAnnotation(annotationClass) != null)
					result.add(m);
			}
		}
		return result;
	}

	/**
	 * Returns a method in the class definition which is annotated with the
	 * given annotation class.
	 * 
	 * @param c
	 *            class definition to be parsed
	 * @param annotationClass
	 *            class of annotation that should be considered during the
	 *            parsing
	 * @return matching method or null in case of search failure
	 */
	public static Method getAnnotatedMethod(Class<?> c, Class<? extends Annotation> annotationClass) {
		if (c != null) {
			Method result = null;
			Method[] methods = c.getMethods();
			for (Method m : methods) {
				if (m.getAnnotation(annotationClass) != null) {
					result = m;
					break;
				}
			}
			return result;
		}
		return null;
	}
	
	public static Object getAnnotationValue(Annotation annotation) {
		IValuedAnnotation valuedAnnotation = (IValuedAnnotation) AnnotationProxy
				.implement(IValuedAnnotation.class, annotation);
		return valuedAnnotation.value();
	}
}
