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

import cz.cuni.mff.d3s.metaadaptation.correlation.Kind;

/**
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class ConnectorPortImpl implements cz.cuni.mff.d3s.metaadaptation.correlation.ComponentPort {
	
	private final Set<String> assumedKnowledge;
	private final Kind kind;

	public ConnectorPortImpl(Set<String> assumedKnowledge, Kind kind){
		if(assumedKnowledge == null){
			throw new IllegalArgumentException(String.format(
					"The \"%s\" argument is null.", "assumedKnowledge"));
		}
		
		this.assumedKnowledge = assumedKnowledge;
		this.kind = kind;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ConnectorPortImpl)) {
			return false;
		}

		ConnectorPortImpl other = (ConnectorPortImpl) obj;
		return this.assumedKnowledge.equals(other.assumedKnowledge)
				&& this.kind.equals(other.kind);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return assumedKnowledge.hashCode() + 17 * kind.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append("Assumed knowledge:\n)");
		for (String ek : assumedKnowledge) {
			builder.append("\t").append(ek).append("\n");
		}
		builder.append("Kind: ").append(kind).append("\n");

		return builder.toString();
	}
}
