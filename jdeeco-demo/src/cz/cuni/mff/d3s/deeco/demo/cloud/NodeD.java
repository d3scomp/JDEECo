/*******************************************************************************
 * Copyright 2013 Charles University in Prague
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

import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.definitions.ComponentDefinition;
import cz.cuni.mff.d3s.deeco.knowledge.OutWrapper;

/**
 * 
 * @author Petr Hnetynka
 */
public class NodeD extends ComponentDefinition {
	public final static long serialVersionUID = 1L;

	public Float loadRatio;
	public Float maxLoadRatio;
	public Integer networkId;
	public String targetNode;

	public NodeD() {
		this.id = "NodeD";
		this.loadRatio = 0.0f;
		this.maxLoadRatio = 0.3f;
		this.networkId = 1;
		this.targetNode = null;
	}

	@cz.cuni.mff.d3s.deeco.annotations.Process
	@PeriodicScheduling(6000)
	public static void process(@Out("loadRatio") OutWrapper<Float> loadRatio) {
		loadRatio.value = new Random().nextFloat();

		System.out.println("Node D new load ratio: "
				+ Math.round(loadRatio.value * 100) + "%");
	}

}
