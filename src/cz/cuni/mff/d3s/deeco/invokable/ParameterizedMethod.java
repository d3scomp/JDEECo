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

import java.lang.reflect.Method;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.DEECoIn;
import cz.cuni.mff.d3s.deeco.annotations.DEECoInOut;
import cz.cuni.mff.d3s.deeco.annotations.DEECoOut;
import cz.cuni.mff.d3s.deeco.path.grammar.ParseException;

/**
 * Base class representing a parameterized method.
 * 
 * @author Michal Kit
 * 
 */
public class ParameterizedMethod {
	public Method method;
	/**
	 * Input parameters
	 */
	public List<Parameter> in;
	/**
	 * Input/Output parameters
	 */
	public List<Parameter> inOut;
	/**
	 * Output parameters
	 */
	public List<Parameter> out;

	public ParameterizedMethod(Method method) {
		this.method = method;
	}

	/**
	 * Invokes process method execution.
	 * 
	 * @param parameters
	 *            list of method parameters
	 * @return invocation result or null in case of an exception.
	 */
	public Object invoke(Object[] parameters) {
		try {
			return method.invoke(null, parameters);
		} catch (Exception e) {
			System.out.println("Method invocation error: " + e.getMessage());
			return null;
		}
	}
	
	public static synchronized ParameterizedMethod extractParametrizedMethod(
			Method method) {
		return extractParametrizedMethod(method, null);
	}

	public static synchronized ParameterizedMethod extractParametrizedMethod(
			Method method, String root) {
		try {
			ParameterizedMethod result = null;
			if (method != null) {
				result = new ParameterizedMethod(method);
				result.in = AnnotationHelper.getParameters(method,
						DEECoIn.class, root);
				result.out = AnnotationHelper.getParameters(method,
						DEECoOut.class, root);
				result.inOut = AnnotationHelper.getParameters(method,
						DEECoInOut.class, root);
			}
			return result;
		} catch (ParseException pe) {
			return null;
		} 
	}
}
