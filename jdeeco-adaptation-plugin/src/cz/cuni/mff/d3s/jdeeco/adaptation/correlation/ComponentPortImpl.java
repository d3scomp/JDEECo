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

/**
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class ComponentPortImpl implements cz.cuni.mff.d3s.metaadaptation.correlation.ComponentPort {

	private final Set<String> exposedKnowledge;
	
	public ComponentPortImpl(Set<String> exposedKnowledge){
		if(exposedKnowledge == null){
			throw new IllegalArgumentException(String.format(
					"The \"%s\" argument is null.", "exposedKnowledge"));
		}
		
		this.exposedKnowledge = exposedKnowledge;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ComponentPortImpl)){
			return false;
		}
		
		ComponentPortImpl other = (ComponentPortImpl) obj;
		return this.exposedKnowledge.equals(other.exposedKnowledge);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return exposedKnowledge.hashCode();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("Exposed knowledge:\n)");
		for(String ek : exposedKnowledge){
			builder.append("\t")
				.append(ek)
				.append("\n");
		}
		
		return builder.toString();
	}
}
