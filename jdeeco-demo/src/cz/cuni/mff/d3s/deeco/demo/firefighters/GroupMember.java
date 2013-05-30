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
package cz.cuni.mff.d3s.deeco.demo.firefighters;

import java.util.Random;

import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.knowledge.Component;
import cz.cuni.mff.d3s.deeco.knowledge.OutWrapper;

/**
 * Template for firefighters - group members (GM).
 * 
 * These lie at the bottom of the hierarchy.
 * 
 * @author Ilias Gerostathopoulos
 * 
 */
public class GroupMember extends Component {

	private static final long serialVersionUID = 5243757300436836781L;

	public Float temperature;
	public Position position;
	public Boolean isMoving;
	public String teamId;

	public GroupMember() {
		// id (String) field always exists; it's the root of the tree
		this.id = "FF1";
		this.teamId = "T1";
		this.temperature = 25.0f;
		this.position = new Position(300, 300);
	}

	public GroupMember(String id, String team_id) {
		// id field always exists; it's the root of the tree
		this.id = id;
		this.teamId = team_id;
		this.temperature = 25.0f;
		this.position = new Position(300, 300);
	}

	@Process
	@PeriodicScheduling(500)
	public static void measureTemperature(@In("id") String id,
			@Out("temperature") OutWrapper<Float> temperature) {
		temperature.value = new Random().nextFloat() * 100;
		System.out.println(id + " new temperature: "
				+ Math.round(temperature.value) + " degrees Celcious.");
	}

}
