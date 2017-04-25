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

import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.metaadaptation.correlation.DynamicConnector;

/**
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class ConnectorManagerImpl implements cz.cuni.mff.d3s.metaadaptation.correlation.ConnectorManager {
	
	private final Set<DEECoNode> nodes;
	private final CorrelationEnsembleFactory ensembleFactory;
	private boolean verbose;
	
	
	public ConnectorManagerImpl(CorrelationEnsembleFactory ensembleFactory, Set<DEECoNode> nodes){
		if(ensembleFactory == null){
			throw new IllegalArgumentException(String.format(
					"The \"%s\" argument is null.", "ensembleFactory"));
		}
		if(nodes == null){
			throw new IllegalArgumentException(String.format(
					"The \"%s\" argument is null.", "nodes"));
		}
		
		this.ensembleFactory = ensembleFactory;
		this.nodes = nodes;
		verbose = false;
	}
	
	public void setVerbosity(boolean verbosity){
		this.verbose = verbosity;
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.correlation.ConnectorManager#getConnectors()
	 */
	@Override
	public Set<DynamicConnector> getConnectors() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.correlation.ConnectorManager#addConnector(java.util.function.Predicate, cz.cuni.mff.d3s.metaadaptation.correlation.ConnectorManager.MediatedKnowledge)
	 */
	@Override
	public void addConnector(Predicate<Map<String, Object>> filter, MediatedKnowledge mediatedKnowledge) {
		if(mediatedKnowledge == null){
			throw new IllegalArgumentException(String.format(
					"The \"%s\" argument is null.", "mediatedKnowledge"));
		}
		
		// Undeploy if ensemble already exist
		removeEnsemble(ensembleFactory.getHelper().composeClassName(
				mediatedKnowledge.correlationFilter,
				mediatedKnowledge.correlationSubject));
		
		if(filter == null){
			return;
		}
		
		try {
			// Deploy ensemble
			Class<?> ensemble = ensembleFactory.createEnsembleDefinitionWithMembership(
					mediatedKnowledge.correlationFilter,
					mediatedKnowledge.correlationSubject,
					filter);
			ensembleFactory.getHelper().bufferEnsembleDefinition(
					mediatedKnowledge.correlationFilter,
					mediatedKnowledge.correlationSubject,
					ensemble);
			addEnsemble(ensemble);
			if(verbose){
				Log.i(String.format("Deploying ensemble %s", mediatedKnowledge));
			}
		} catch (Exception e) {
			Log.e(e.getMessage());
			throw new RuntimeException(e);
		}
	}
	
	public void addEnsemble(Class<?> ensemble) {
		try {
			for (DEECoNode node : nodes) {
				node.deployEnsemble(ensemble);
			}
		} catch (Exception e) {
			Log.e(e.getMessage());
			e.printStackTrace();
		}
	}

	public void removeEnsemble(String name) {
		try {
			for (DEECoNode node : nodes) {
				node.undeployEnsemble(name);
			}
		} catch (Exception e) {
			Log.e(e.getMessage());
			e.printStackTrace();
		}
	}
	
}
