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
package cz.cuni.mff.d3s.jdeeco.adaptation.modeswitching;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.metaadaptation.modeswitch.Component;
import cz.cuni.mff.d3s.metaadaptation.modeswitch.NonDetModeSwitchFitness;

/**
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class NonDetModeSwitchFitnessImpl implements NonDetModeSwitchFitness {

	private final ModeSwitchFitness fitness;
	
	public NonDetModeSwitchFitnessImpl(ModeSwitchFitness fitness) {
		if(fitness == null) {
			throw new IllegalArgumentException(String.format(
					"The %s argument is null.", "fitness"));
		}
		
		this.fitness = fitness;
	}
	
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.modeswitch.NonDetModeSwitchFitness#getFitness(long, cz.cuni.mff.d3s.metaadaptation.modeswitch.Component)
	 */
	@Override
	public double getFitness(long currentTime, Component component) {
		
		ComponentInstance compoentInstance = ((ComponentImpl)component).getComponentInstance();
		String[] fields = fitness.getKnowledgeNames();
		return fitness.getFitness(currentTime, getKnowledgeValues(fields, compoentInstance));
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.modeswitch.NonDetModeSwitchFitness#restart()
	 */
	@Override
	public void restart() {
		fitness.restart();
	}
	
	public Object[] getKnowledgeValues(String[] knowledgeNames, ComponentInstance componentInstance) {
		List<KnowledgePath> paths = new ArrayList<>();
		for(String knowledgeName : knowledgeNames){
			KnowledgePath path = RuntimeMetadataFactoryExt.eINSTANCE.createKnowledgePath();
			PathNodeField pNode = RuntimeMetadataFactoryExt.eINSTANCE.createPathNodeField();
			pNode.setName(knowledgeName);
			path.getNodes().add(pNode);
			paths.add(path);
		}
		try {
			ValueSet vSet = componentInstance.getKnowledgeManager().get(paths);
			List<Object> values = new ArrayList<>();
			for(KnowledgePath path : paths){
				values.add(vSet.getValue(path));
			}
			return values.toArray();
		} catch (KnowledgeNotFoundException e) {
			System.err.println("Couldn't find knowledge " + knowledgeNames + " in component " + componentInstance);
			return null;
		}
	}

}
