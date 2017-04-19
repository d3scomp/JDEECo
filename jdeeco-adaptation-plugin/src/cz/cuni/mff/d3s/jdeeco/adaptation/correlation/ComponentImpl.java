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

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainer;
import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainerException;
import cz.cuni.mff.d3s.deeco.knowledge.container.TrackingKnowledgeContainer;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.metaadaptation.correlation.CorrelationMetadataWrapper;

/**
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class ComponentImpl implements cz.cuni.mff.d3s.metaadaptation.correlation.Component {

	private final String componentName;

	private final ComponentInstance componentInstance;

	
	public ComponentImpl(String componentName, ComponentInstance componentInstance) {
		if (componentName == null || componentName.isEmpty()) {
			throw new IllegalArgumentException(String.format("The %s argument is null or empty.", "componentName"));
		}
		if (componentInstance == null) {
			throw new IllegalArgumentException(String.format("The %s argument is null.", "componentInstance"));
		}

		this.componentName = componentName;
		this.componentInstance = componentInstance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.metaadaptation.correlation.Component#getId()
	 */
	@Override
	public String getId() {
		return componentName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.metaadaptation.correlation.Component#getKnowledgeFields()
	 */
	@Override
	public Set<String> getKnowledgeFields() {
		try {
			Map<String, Map<String, CorrelationMetadataWrapper<? extends Object>>> knowledgeOfAllComponents = getKnowledgeOfAllComponents();

			if (knowledgeOfAllComponents.containsKey(componentName)) {
				return knowledgeOfAllComponents.get(componentName).keySet();
			}
		} catch (KnowledgeContainerException e) {
			Log.e(e.getMessage());
			e.printStackTrace();
		}
		return Collections.emptySet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.metaadaptation.correlation.Component#getKnowledgeValue(
	 * java.lang.String)
	 */
	@Override
	public Object getKnowledgeValue(String knowlegeField) {
		try {
			Map<String, Map<String, CorrelationMetadataWrapper<? extends Object>>> knowledgeOfAllComponents
				= getKnowledgeOfAllComponents();

			if (knowledgeOfAllComponents.containsKey(componentName)) {
				if (knowledgeOfAllComponents.get(componentName).containsKey(knowlegeField)) {
					return knowledgeOfAllComponents.get(componentName).get(knowlegeField);
				}
			}
		} catch (KnowledgeContainerException e) {
			Log.e(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	private Map<String, Map<String, CorrelationMetadataWrapper<? extends Object>>> getKnowledgeOfAllComponents()
			throws KnowledgeContainerException {
		KnowledgeContainer kc = TrackingKnowledgeContainer.createFromKnowledgeManagers(
				componentInstance.getKnowledgeManager(),
				componentInstance.getShadowKnowledgeManagerRegistry().getShadowKnowledgeManagers());
		Collection<CorrelationKnowledgeRole> knowledge = kc
				.getUntrackedKnowledgeForRole(CorrelationKnowledgeRole.class);

		if(knowledge.isEmpty()){
			return Collections.emptyMap();
		}
		CorrelationKnowledgeRole correlationKnowledge =
				knowledge.toArray(new CorrelationKnowledgeRole[] {})[0];
		if(correlationKnowledge.knowledgeOfAllComponents == null){
			return Collections.emptyMap();
		}
		
		return correlationKnowledge.knowledgeOfAllComponents;

	}

}
