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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.knowledge.Component;

/**
 * Template for "group leader" (GL).
 * 
 * These lie in the middle of the hierarchy.
 * 
 * A GL can be promoted to be a "site leader" (SL)
 * 
 * @author Ilias Gerostathopoulos
 * 
 */
public class GroupLeader extends Component {

	private static final long serialVersionUID = -1949643385305038287L;

	public String teamId;
	public Boolean isSiteLeader;
	public Map<String, Float> temperatures;
	public Map<String, Position> positions;
	// list of supervised group members in danger:
	public Set<String> GMsInDangerInTeam;
	// list of all group members in danger,
	// to be populated only if the GL is also a site leader:
	public Set<String> GMsInDangerInSite;

	// public Float temperatureThreshold = 50.0f;
	public Float temperatureThreshold;

	public GroupLeader() {
		// id (String) field always exists; it's the root of the tree
		this.id = "GL1";
		this.teamId = "T1";
		this.isSiteLeader = true;
		this.temperatures = new HashMap<String, Float>();
		this.positions = new HashMap<String, Position>();
		this.GMsInDangerInTeam = new HashSet<String>();
		this.GMsInDangerInSite = new HashSet<String>();
		this.temperatureThreshold = 50.0f;
	}

	public GroupLeader(String id, String team_id, boolean isSiteLeader) {
		// id field always exists; it's the root of the tree
		this.id = id;
		this.teamId = team_id;
		this.isSiteLeader = isSiteLeader;
		this.temperatures = new HashMap<String, Float>();
		this.positions = new HashMap<String, Position>();
		this.GMsInDangerInTeam = new HashSet<String>();
		this.GMsInDangerInSite = new HashSet<String>();
		this.temperatureThreshold = 50.0f;
	}

	@Process
	@PeriodicScheduling(5000)
	public static void processSensorData(
			@In("temperatures") Map<String, Float> temperatures,
			@In("temperatures") Map<String, Float> positions,
			@In("temperatureThreshold") Float temperatureThreshold,
			@InOut("GMsInDangerInTeam") Set<String> GMsInDangerInTeam) {
		System.out.println("GL: Processing sensor data...");
		System.out.println("Temperatures map holds "
				+ temperatures.keySet().size() + " items");
		for (String id : temperatures.keySet()) {
			System.out.println("[" + id + ", " + temperatures.get(id) + "]");
			if (temperatures.get(id) > temperatureThreshold) {
				GMsInDangerInTeam.add(id);
			}
		}
		for (String id : positions.keySet()) {
			System.out.println("Position: " + positions.get(id));
		}
	}

	// @Process
	// @PeriodicScheduling(10000)
	// public static void outputGMsInDanger(
	// @In("isSiteLeader") Boolean isSiteLeader,
	// @In("GMsInDangerInSite") Set<String> GMsInDangerInSite) {
	// if (isSiteLeader) {
	// System.out.println("There are " + GMsInDangerInSite.size()
	// + " firefighters in danger in the site.");
	// for (String ff : GMsInDangerInSite) {
	// System.out.println("Firefighter : " + ff);
	// }
	// }
	// }

}
