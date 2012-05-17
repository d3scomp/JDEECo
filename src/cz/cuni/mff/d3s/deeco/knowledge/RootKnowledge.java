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

import java.lang.reflect.Method;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.DEECoInitialize;
import cz.cuni.mff.d3s.deeco.invokable.AnnotationHelper;

/**
 * RootKnowledge is the base class representing top-level component knowledge.
 * 
 * 
 * @author Michal Kit
 *
 */
public class RootKnowledge extends Knowledge {
	public String id;

	public static Method getInitMethod(Class c) {
		List<Method> result = AnnotationHelper.getAnnotatedMethods(c,
				DEECoInitialize.class);
		if (result.size() == 1) {
			return result.get(0);
		}
		return null;
	}
}
