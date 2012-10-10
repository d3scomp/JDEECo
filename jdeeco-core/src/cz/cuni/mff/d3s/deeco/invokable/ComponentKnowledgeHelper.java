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

import java.util.Arrays;

import cz.cuni.mff.d3s.deeco.exceptions.KMNotExistentException;
import cz.cuni.mff.d3s.deeco.knowledge.ComponentKnowledge;
import cz.cuni.mff.d3s.deeco.knowledge.ConstantKeys;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;

/**
 * Class providing component managing functionalities.
 * 
 * @author Michal Kit
 * 
 */
public class ComponentKnowledgeHelper {

	public static boolean addComponentKnowledge(ComponentKnowledge rootKnowledge, KnowledgeManager km) {
		if (rootKnowledge != null) {
			try {
				try {
					Object[] currentIds = (Object []) km
							.getKnowledge(ConstantKeys.ROOT_KNOWLEDGE_ID);
					if (Arrays.asList(currentIds).contains(rootKnowledge.id))
						return false;
				} catch (KMNotExistentException kmnee) {
				}
				km.putKnowledge(ConstantKeys.ROOT_KNOWLEDGE_ID,
						rootKnowledge.id);
				km.alterKnowledge(rootKnowledge.id, rootKnowledge);
				return true;
			} catch (Exception e) {
				System.out.println("Error when writing root knowledge: "
						+ e.getMessage());
			}
		}
		return false;
	}
}
