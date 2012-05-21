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
package cz.cuni.mff.d3s.deeco.ducktype;

import java.lang.reflect.Field;

import cz.cuni.mff.d3s.deeco.knowledge.RootKnowledge;

public class DuckType {
	public static boolean interfaceMatchesKnowledge(Class interfaceClass,
			Class knowledgeClass) {
		Field cField;
		for (Field field : interfaceClass.getFields()) {
			try {
				cField = knowledgeClass.getField(field.getName());
				if (!(cField.getName().equals(field.getName()) && cField
						.getType().equals(field.getType())))
					return false;
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}

	public static RootKnowledge getInterfacedKnowledge(Class interfaceClass,
			RootKnowledge knowledge) {
		try {
			RootKnowledge result = null;
			Class knowledgeClass = knowledge.getClass();
			if (RootKnowledge.class.isAssignableFrom(interfaceClass)) {
				result = (RootKnowledge) interfaceClass.newInstance();
				for (Field field : interfaceClass.getFields()) {
					field.set(result, knowledgeClass.getField(field.getName())
							.get(knowledge));
				}
			}
			return result;
		} catch (Exception e) {
			return null;
		}
	}

	public static boolean castKnowledgeToInterface(Object interfaceInstance,
			Object knowledgeInstance) {
		for (Field field : interfaceInstance.getClass().getFields()) {
			try {
				field.set(interfaceInstance, field.get(knowledgeInstance));
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}
}
