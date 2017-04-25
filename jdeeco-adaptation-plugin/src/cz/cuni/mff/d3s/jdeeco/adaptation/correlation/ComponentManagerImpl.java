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

import java.util.Set;

import cz.cuni.mff.d3s.metaadaptation.correlation.Component;

/**
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class ComponentManagerImpl implements cz.cuni.mff.d3s.metaadaptation.correlation.ComponentManager {

	private final Set<Component> components;
	
	
	public ComponentManagerImpl(Set<Component> components){
		if(components == null){
			throw new IllegalArgumentException(String.format("The %s argument is null.", "components"));
		}
		
		this.components = components;
	}
	
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.componentisolation.ComponentManager#getComponents()
	 */
	@Override
	public Set<Component> getComponents() {
		return components;
	}

}
