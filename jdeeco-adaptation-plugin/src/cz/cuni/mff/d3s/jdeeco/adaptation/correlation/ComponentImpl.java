/*******************************************************************************
 * Copyright 2017 Charles University in Prague
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *******************************************************************************/
package cz.cuni.mff.d3s.jdeeco.adaptation.correlation;

import java.util.Collections;
import java.util.Set;

/**
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class ComponentImpl implements cz.cuni.mff.d3s.metaadaptation.correlation.Component {

	private final String componentName;
	
	private final CorrelationKnowledgeData correlationData; // FIXME: this is wrong
	
	public ComponentImpl(String componentName, CorrelationKnowledgeData correlationData){
		if(correlationData == null){
			throw new IllegalArgumentException(String.format("The %s argument is null.",
					"correlationData"));
		}
		if(componentName == null || componentName.isEmpty()){
			throw new IllegalArgumentException(String.format("The %s argument is null or empty.",
					"componentName"));
		}
		
		this.componentName = componentName;
		this.correlationData = correlationData;
	}
	
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.correlation.Component#getId()
	 */
	@Override
	public String getId() {
		return componentName;
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.correlation.Component#getKnowledgeFields()
	 */
	@Override
	public Set<String> getKnowledgeFields() {
		if(correlationData.knowledgeOfAllComponents.containsKey(componentName)){
			return correlationData.knowledgeOfAllComponents.get(componentName)
					.keySet();
		} else {
			return Collections.emptySet();
		}
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.correlation.Component#getKnowledgeValue(java.lang.String)
	 */
	@Override
	public Object getKnowledgeValue(String knowlegeField) {
		if(correlationData.knowledgeOfAllComponents.containsKey(componentName)){
			if(correlationData.knowledgeOfAllComponents.get(componentName)
					.containsKey(knowlegeField)){
				return correlationData.knowledgeOfAllComponents.get(componentName)
						.get(knowlegeField);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

}
