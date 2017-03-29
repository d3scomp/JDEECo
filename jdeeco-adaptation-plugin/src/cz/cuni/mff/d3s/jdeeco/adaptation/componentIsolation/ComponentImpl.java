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

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainer;
import cz.cuni.mff.d3s.deeco.knowledge.container.TrackingKnowledgeContainer;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.metaadaptation.componentisolation.Port;

/**
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class ComponentImpl implements cz.cuni.mff.d3s.metaadaptation.componentisolation.Component {

	private final ComponentInstance component;
	
	public ComponentImpl(ComponentInstance component){
		if(component == null){
			throw new IllegalArgumentException(String.format("The %s argument is null.", "component"));
		}

		this.component = component;
	}
	
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.componentisolation.Component#addPort(cz.cuni.mff.d3s.metaadaptation.componentisolation.Port)
	 */
	@Override
	public void addPort(Port port) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.componentisolation.Component#removePort(cz.cuni.mff.d3s.metaadaptation.componentisolation.Port)
	 */
	@Override
	public void removePort(Port port) {
		KnowledgeManager kManager = component.getKnowledgeManager();
		kManager.updateRoles(null);
		Log.i("Removing the role of the " + kManager.getId());
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
	 * @see cz.cuni.mff.d3s.metaadaptation.componentisolation.Component#monitorHealth(cz.cuni.mff.d3s.metaadaptation.componentisolation.Port)
	 */
	@Override
	public void monitorHealth(Port port) {

		
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.componentisolation.Component#getHealth(cz.cuni.mff.d3s.metaadaptation.componentisolation.Port)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean getHealth(Port port) {
		try {
			KnowledgeContainer kc = TrackingKnowledgeContainer.createFromKnowledgeManagers(
					component.getKnowledgeManager(),
					component.getShadowKnowledgeManagerRegistry().getShadowKnowledgeManagers());
			Collection<Object> knowledge = kc.getUntrackedKnowledgeForRole(((PortImpl) port).getRole());

			if (knowledge.size() > 0) {
				for (Object o : knowledge) {
					Field id = o.getClass().getField("id");
					if (component.getKnowledgeManager().getId().equals(id.get(o))) {
						Field isWorking = o.getClass().getField("isWorking");
						return (boolean) isWorking.get(o);
					}

				}
			}
		} catch (Exception e) {
		}

		return true;
	}

}
