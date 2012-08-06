/*******************************************************************************
 * Copyright 2012 Charles University in Prague
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package cz.cuni.mff.d3s.deeco.demo.cloud;

import java.util.Random;

import cz.cuni.mff.d3s.deeco.annotations.DEECoComponent;
import cz.cuni.mff.d3s.deeco.annotations.DEECoInitialize;
import cz.cuni.mff.d3s.deeco.annotations.DEECoOut;
import cz.cuni.mff.d3s.deeco.annotations.DEECoPeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.DEECoProcess;
import cz.cuni.mff.d3s.deeco.knowledge.ComponentKnowledge;
import cz.cuni.mff.d3s.deeco.knowledge.OutWrapper;

@DEECoComponent
public class NodeA extends ComponentKnowledge {

	public Float loadRatio;
	public Float maxLoadRatio;
	public Integer networkId;
	public String targetNode;
	
	@DEECoInitialize
	public static ComponentKnowledge getInitialKnowledge() {
		NodeA k = new NodeA();
		k.id = "NodeA";
		k.loadRatio = 0.0f;
		k.maxLoadRatio = 0.5f;
		k.networkId = 1;
		k.targetNode = null;
		return k;
	}

	@DEECoProcess
	@DEECoPeriodicScheduling(4000)
	public static void process(@DEECoOut("loadRatio") OutWrapper<Float> loadRatio) {
		loadRatio.item = new Random().nextFloat();
		System.out.println("Node A new load ratio: " + loadRatio.item);
	}
}
