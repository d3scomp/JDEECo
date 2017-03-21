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
import cz.cuni.mff.d3s.deeco.modes.ModeGuard;
import cz.cuni.mff.d3s.metaadaptation.modeswitch.Guard;

/**
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class GuardImpl implements Guard {
	
	private final ModeGuard guard;
	
	private final ComponentInstance component;
	
	public GuardImpl(ModeGuard guard, ComponentInstance comonent){
		if(guard == null){
			throw new IllegalArgumentException(String.format("The %s argument is null.", "guard"));
		}
		if(comonent == null){
			throw new IllegalArgumentException(String.format("The %s argument is null.", "comonent"));
		}
		
		this.guard = guard;
		this.component = comonent;
	}
	
	public ModeGuard getInnerGuard(){
		return guard;
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.modeswitch.Guard#isSatisfied()
	 */
	@Override
	public boolean isSatisfied() {
		String[] fields = guard.getKnowledgeNames();
		Object[] values = getKnowledgeValues(fields);
		return guard.isSatisfied(values);
	}

	private Object[] getKnowledgeValues(String[] knowledgeNames) {
		List<KnowledgePath> paths = new ArrayList<>();
		for(String knowledgeName : knowledgeNames){
			KnowledgePath path = RuntimeMetadataFactoryExt.eINSTANCE.createKnowledgePath();
			PathNodeField pNode = RuntimeMetadataFactoryExt.eINSTANCE.createPathNodeField();
			pNode.setName(knowledgeName);
			path.getNodes().add(pNode);
			paths.add(path);
		}
		try {
			ValueSet vSet = component.getKnowledgeManager().get(paths);
			List<Object> values = new ArrayList<>();
			for(KnowledgePath path : paths){
				values.add(vSet.getValue(path));
			}
			return values.toArray();
		} catch (KnowledgeNotFoundException e) {
			System.err.println("Couldn't find knowledge " + knowledgeNames + " in component " + component);
			return null;
		}
	}
}
