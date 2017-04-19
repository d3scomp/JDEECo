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
package cz.cuni.mff.d3s.jdeeco.adaptation.componentIsolation;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainer;
import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainerException;
import cz.cuni.mff.d3s.deeco.knowledge.container.TrackingKnowledgeContainer;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.metaadaptation.componentisolation.Port;

/**
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class ComponentImpl implements cz.cuni.mff.d3s.metaadaptation.componentisolation.Component {

	private final ComponentInstance component;
	private boolean verbose;
	
	public ComponentImpl(ComponentInstance component){
		if(component == null){
			throw new IllegalArgumentException(String.format("The %s argument is null.", "component"));
		}

		this.component = component;
		verbose = false;
	}
	
	public void setVerbosity(boolean verbosity){
		this.verbose = verbosity;
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.componentisolation.Component#removePort(cz.cuni.mff.d3s.metaadaptation.componentisolation.Port)
	 */
	@Override
	public void removePort(Port port) {
		KnowledgeManager kManager = component.getKnowledgeManager();
		kManager.removeRole(((PortImpl) port).getRole());
		Log.i(String.format("Removing the role \"%s\" of the \"%s\" component.",
				port.toString(), kManager.getId()));
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.componentisolation.Component#getPorts()
	 */
	@Override
	public Set<Port> getPorts() {
		Class<?>[] roles = component.getKnowledgeManager().getRoles();
		
		Set<Port> ports = new HashSet<Port>();
		for(Class<?> role : roles){
			ports.add(new PortImpl(role));
		}
		
		return ports;
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.componentisolation.Component#getFaultyKnowledge()
	 */
	@Override
	public Set<String> getFaultyKnowledge() {
		try {
			KnowledgeContainer kc = TrackingKnowledgeContainer.createFromKnowledgeManagers(
					component.getKnowledgeManager(),
					component.getShadowKnowledgeManagerRegistry().getShadowKnowledgeManagers());
			FaultyKnowledgeReportingRole knowledge;
			knowledge = kc.getUntrackedLocalKnowledgeForRole(component, FaultyKnowledgeReportingRole.class);
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
	 * @see cz.cuni.mff.d3s.metaadaptation.componentisolation.Component#getKnowledge()
	 */
	@Override
	public Map<String, Object> getKnowledge() {
		Map<String, Object> knowledge = new HashMap<>();
		
		try {
			Collection<KnowledgePath> kps = component.getKnowledgeManager().getAllPaths();
			ValueSet vSet = component.getKnowledgeManager().get(kps);
			for (KnowledgePath kp : vSet.getKnowledgePaths()) {
				String k = kp.getNodes().get(kp.getNodes().size() - 1).toString();
				knowledge.put(k, vSet.getValue(kp));
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
		return knowledge;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return component.getKnowledgeManager().getId();
	}
	
}
