/*******************************************************************************
 * Copyright 2016 Charles University in Prague
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
import java.util.HashMap;
import java.util.List;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.runtimelog.RuntimeLogRecord;

/**
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class NonDeterministicLevelRecord extends RuntimeLogRecord {

	public NonDeterministicLevelRecord(String id) {
		super(id, new HashMap<>());
	}

	public void setProbability(double probability) {
		recordValues.put("probability", probability);
	}

	public void setComponent(ComponentInstance component) {
		List<KnowledgePath> paths = new ArrayList<>();
		KnowledgePath path = RuntimeMetadataFactoryExt.eINSTANCE.createKnowledgePath();
		PathNodeField pNode = RuntimeMetadataFactoryExt.eINSTANCE.createPathNodeField();
		pNode.setName("id");
		path.getNodes().add(pNode);
		paths.add(path);
		try {
			ValueSet vSet = component.getKnowledgeManager().get(paths);
			recordValues.put("component", vSet.getValue(paths.get(0)));
		} catch (KnowledgeNotFoundException e) {
			Log.e("Couldn't find knowledge \"id\" in component " + component);
		}
	}
}