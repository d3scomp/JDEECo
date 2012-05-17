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
import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.DEECoCoordinatorIn;
import cz.cuni.mff.d3s.deeco.annotations.DEECoCoordinatorInOut;
import cz.cuni.mff.d3s.deeco.annotations.DEECoCoordinatorOut;
import cz.cuni.mff.d3s.deeco.annotations.DEECoMemberIn;
import cz.cuni.mff.d3s.deeco.annotations.DEECoMemberInOut;
import cz.cuni.mff.d3s.deeco.annotations.DEECoMemberOut;

/**
 * Classs used to represent ensemble specific method. Either membership or
 * mapper.
 * 
 * @author Michal Kit
 * 
 */
public class EnsembleParametrizedMethod extends ParameterizedMethod {

	public List<Parameter> memberIn;
	public List<Parameter> memberInOut;
	public List<Parameter> memberOut;
	public List<Parameter> coordinatorIn;
	public List<Parameter> coordinatorInOut;
	public List<Parameter> coordinatorOut;

	public EnsembleParametrizedMethod(Method method) {
		super(method);
	}

	public Object invoke(Object[] parameters) {
		try {
			return method.invoke(null, parameters);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	private List<Parameter> getCombined(List<Parameter>... lists) {
		List<Parameter> result = new ArrayList<Parameter>();
		for (List<Parameter> l : lists) {
			result.addAll(l);
		}
		return result;
	}

	/**
	 * Static function extracting {@link EnsembleParametrizedMethod} instance
	 * from the given {@link Method} object.
	 * 
	 * @param method
	 *            to be parsed
	 * @return {@link EnsembleParametrizedMethod} instance or null in case of
	 *         parsing failure.
	 */
	public static EnsembleParametrizedMethod extractParametrizedMethod(
			Method method) {
		EnsembleParametrizedMethod result = null;
		if (method != null) {
			result = new EnsembleParametrizedMethod(method);
			result.memberIn = AnnotationHelper.getParameters(method,
					DEECoMemberIn.class);
			result.memberOut = AnnotationHelper.getParameters(method,
					DEECoMemberOut.class);
			result.memberInOut = AnnotationHelper.getParameters(method,
					DEECoMemberInOut.class);
			result.coordinatorIn = AnnotationHelper.getParameters(method,
					DEECoCoordinatorIn.class);
			result.coordinatorOut = AnnotationHelper.getParameters(method,
					DEECoCoordinatorOut.class);
			result.coordinatorInOut = AnnotationHelper.getParameters(method,
					DEECoCoordinatorInOut.class);
		}
		return result;
	}
}
