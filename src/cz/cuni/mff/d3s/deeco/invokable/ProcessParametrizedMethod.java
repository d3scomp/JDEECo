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

import cz.cuni.mff.d3s.deeco.annotations.DEECoProcessIn;
import cz.cuni.mff.d3s.deeco.annotations.DEECoProcessInOut;
import cz.cuni.mff.d3s.deeco.annotations.DEECoProcessOut;

/**
 * Class representing component process method.
 * 
 * @author Michal Kit
 * 
 */
public class ProcessParametrizedMethod extends ParameterizedMethod {
	public String root;
	public List<Parameter> in;
	public List<Parameter> inOut;
	public List<Parameter> out;

	public ProcessParametrizedMethod(String root, Method method) {
		super(method);
		this.root = root;
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
			System.out.println(e.getMessage());
			return null;
		}
	}

	/**
	 * Static utility function used to extract {@link ProcessParametrizedMethod}
	 * instance from a given method.
	 * 
	 * @param method
	 *            method that needs to be parsed
	 * @param root
	 *            component knowledge id for which process method performs its
	 *            execution
	 * @return {@link ProcessParametrizedMethod} instance or null in case of
	 *         parsing error
	 */
	public static synchronized ProcessParametrizedMethod extractParametrizedMethod(
			Method method, String root) {
		ProcessParametrizedMethod result = null;
		if (method != null) {
			result = new ProcessParametrizedMethod(root, method);
			result.in = AnnotationHelper.getParameters(method,
					DEECoProcessIn.class);
			result.out = AnnotationHelper.getParameters(method,
					DEECoProcessOut.class);
			result.inOut = AnnotationHelper.getParameters(method,
					DEECoProcessInOut.class);
		}
		return result;
	}
}
