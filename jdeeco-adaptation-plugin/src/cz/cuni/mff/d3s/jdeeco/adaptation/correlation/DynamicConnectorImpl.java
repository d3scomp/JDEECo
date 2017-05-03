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

import java.util.HashSet;
import java.util.Set;

import cz.cuni.mff.d3s.metaadaptation.correlation.ConnectorPort;
import cz.cuni.mff.d3s.metaadaptation.correlation.DynamicConnector;
import cz.cuni.mff.d3s.metaadaptation.correlation.Kind;

/**
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class DynamicConnectorImpl implements DynamicConnector {

	private final Class<?> ensemble;
	
	private final Set<ConnectorPort> ports;
	
	public DynamicConnectorImpl(Class<?> ensemble){
		if (ensemble == null) {
			throw new IllegalArgumentException(String.format("The %s argument is null.", "ensemble"));
		}
		
		this.ensemble = ensemble;
		ports = new HashSet<>();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ensemble.getName();
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.correlation.DynamicConnector#addPort(java.util.Set, cz.cuni.mff.d3s.metaadaptation.correlation.Kind, cz.cuni.mff.d3s.metaadaptation.correlation.ComponentPort)
	 */
	@Override
	public ConnectorPort addPort(Set<String> assumedKnowledge, Kind kind) {
		ConnectorPort cp = new ConnectorPortImpl(assumedKnowledge, kind);
		ports.add(cp);
		return cp;
	}
}
