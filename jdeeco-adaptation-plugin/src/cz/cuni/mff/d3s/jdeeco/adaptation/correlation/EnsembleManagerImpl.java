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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.annotations.Local;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;

/**
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class EnsembleManagerImpl implements cz.cuni.mff.d3s.metaadaptation.correlation.EnsembleManager {

	/**
	 * The list of the other DEECo nodes that exists in the system. Except the
	 * node on which the CorrelaitonManager component is deployed.
	 */
	@Local
	private final List<DEECoNode> nodes;

	public EnsembleManagerImpl() {
		this.nodes = new ArrayList<>();
	}
	
	public void addNode(DEECoNode node){
		nodes.add(node);
	}
	
	public void removeNode(DEECoNode node){
		nodes.remove(node);
	}
	
	public void setNodes(Set<DEECoNode> nodes){
		this.nodes.clear();
		for(DEECoNode node : nodes){
			this.nodes.add(node);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.metaadaptation.correlation.Connectors#addEnsemble(java.
	 * lang.Class)
	 */
	@Override
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.metaadaptation.correlation.Connectors#removeEnsemble(java
	 * .lang.String)
	 */
	@Override
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
