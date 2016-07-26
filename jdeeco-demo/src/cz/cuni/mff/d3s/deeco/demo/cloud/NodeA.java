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

import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.TriggerOnChange;
import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

@Component
public class NodeA {

	public final static long serialVersionUID = 1L;

	public Float loadRatio;
	public Float maxLoadRatio;
	public Integer networkId;
	public String targetNode;

	public String id;

	public NodeA() {
		this.id = "NodeA";
		this.maxLoadRatio = .5f;
		this.loadRatio = 0.0f;
		this.networkId = 1;
		this.targetNode = null;
	}

	public NodeA(String id, Float maxLoadRatio, Integer networkId) {
		this.id = id;
		this.maxLoadRatio = maxLoadRatio;
		this.loadRatio = 0.0f;
		this.networkId = networkId;
		this.targetNode = null;
	}

	@Process
	@PeriodicScheduling(period=6000)
	public static void process(@Out("loadRatio") ParamHolder<Float> loadRatio) {
		loadRatio.value = new Random().nextFloat();
		System.out.println("Node A new load ratio: "
				+ Math.round(loadRatio.value * 100) + "%");
	}

	@Process
	public static void process2(
			@In("targetNode") @TriggerOnChange String mTargetNode) {
		System.out.println("New target node: " + mTargetNode);
	}

}
