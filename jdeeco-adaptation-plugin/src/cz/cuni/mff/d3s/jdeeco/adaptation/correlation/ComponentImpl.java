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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainer;
import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainerException;
import cz.cuni.mff.d3s.deeco.knowledge.container.TrackingKnowledgeContainer;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.jdeeco.adaptation.FaultyKnowledgeReportingRole;
import cz.cuni.mff.d3s.metaadaptation.correlation.ComponentPort;
import cz.cuni.mff.d3s.metaadaptation.correlation.CorrelationMetadataWrapper;

/**
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class ComponentImpl implements cz.cuni.mff.d3s.metaadaptation.correlation.Component {

	private final ComponentInstance componentInstance;
	private final Set<ComponentPort> componentPorts;
	private boolean verbose;

	
	public ComponentImpl(ComponentInstance componentInstance) {
		if (componentInstance == null) {
			throw new IllegalArgumentException(String.format("The %s argument is null.", "componentInstance"));
		}

		this.componentInstance = componentInstance;
		componentPorts = new HashSet<>();
		verbose = false;
	}
	
	public void setVerbosity(boolean verbosity){
		this.verbose = verbosity;
	}


	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.correlation.Component#getKnowledge()
	 */
	@Override
	public Map<String, Object> getKnowledge() {
		try {
			Map<String, Map<String, CorrelationMetadataWrapper<? extends Object>>> knowledgeOfAllComponents
				= getKnowledgeOfAllComponents();

			final String componentName = componentInstance.getKnowledgeManager().getId();
			
			if (knowledgeOfAllComponents.containsKey(componentName)) {
				Map<String, Object> result = new HashMap<>(knowledgeOfAllComponents.get(componentName));
				return result;
			}			
		} catch (KnowledgeContainerException e) {
			Log.e(e.getMessage());
			e.printStackTrace();
		}

		return Collections.emptyMap();
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.correlation.Component#getFaultyKnowledge()
	 */
	@Override
	public Set<String> getFaultyKnowledge() {
		try {
			KnowledgeContainer kc = TrackingKnowledgeContainer.createFromKnowledgeManagers(
					componentInstance.getKnowledgeManager(),
					componentInstance.getShadowKnowledgeManagerRegistry().getShadowKnowledgeManagers());
			FaultyKnowledgeReportingRole knowledge;
			knowledge = kc.getUntrackedLocalKnowledgeForRole(componentInstance, FaultyKnowledgeReportingRole.class);
			return knowledge.faultyKnowledge;
		} catch (KnowledgeContainerException e) {
			// If the component doesn't implement FaultyKnowledgeReportingRole don't check it
			if(verbose){
				Log.w(e.getMessage());
			}
			return Collections.emptySet();
		}
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.correlation.Component#getPorts()
	 */
	@Override
	public Set<ComponentPort> getPorts() {
		return componentPorts;
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.correlation.Component#addPort(java.util.Set)
	 */
	@Override
	public ComponentPort addPort(Set<String> exposedKnowledge) {
		ComponentPort cp = new ComponentPortImpl(exposedKnowledge);
		componentPorts.add(cp);
		return cp;
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
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ComponentImpl)){
			return false;
		}
		
		ComponentImpl other = (ComponentImpl) obj;
		String thisId = componentInstance.getKnowledgeManager().getId();
		String otherId = other.componentInstance.getKnowledgeManager().getId();
		
		return thisId.equals(otherId);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return componentInstance.getKnowledgeManager().getId().hashCode();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return componentInstance.getKnowledgeManager().getId();
	}
	
}
