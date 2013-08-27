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
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.TriggerOnChange;
import cz.cuni.mff.d3s.deeco.definitions.ComponentDefinition;

/**
 * Template for "group leaders" (GL).
 * 
 * These lie in the middle of the hierarchy.
 * 
 * A GL can be promoted to be a "site leader" (SL)
 * 
 * @author Ilias Gerostathopoulos
 * 
 */
public class GroupLeader extends ComponentDefinition {

	private static final long serialVersionUID = -1949643385305038287L;

	public String teamId;
	public Boolean isSiteLeader;
	public Map<String, Float> temperatures;
	public Map<String, Position> positions;
	// list of supervised group members in danger:
	public Set<String> FFsInDangerInTeam;
	// list of all group members in danger,
	// to be populated only if the GL is also a site leader:
	public Map<String, Set<String>> FFsInDangerInSite;
	// constant to determine critical condition
	public Float temperatureThreshold = 50.0f;

	public Position leaderPosition;
	public Float spectrum;

	public GroupLeader() {
		// id (String) field always exists; it's the root of the tree
		this.id = "GL1";
		this.teamId = "T1";
		this.isSiteLeader = true;
		this.temperatures = new HashMap<String, Float>();
		this.positions = new HashMap<String, Position>();
		this.FFsInDangerInTeam = new HashSet<String>();
		this.FFsInDangerInSite = new HashMap<String, Set<String>>();
		this.leaderPosition = new Position(300, 300);
		this.spectrum = 500.0f;
	}

	public GroupLeader(String id, String team_id, boolean isSiteLeader,
			Position leaderPosition) {
		// id field always exists; it's the root of the tree
		this.id = id;
		this.teamId = team_id;
		this.isSiteLeader = isSiteLeader;
		this.temperatures = new HashMap<String, Float>();
		this.positions = new HashMap<String, Position>();
		this.FFsInDangerInTeam = new HashSet<String>();
		this.FFsInDangerInSite = new HashMap<String, Set<String>>();
		this.leaderPosition = leaderPosition;
		this.spectrum = 500.0f;
	}

	@Process
	@PeriodicScheduling(2000)
	public static void processSensorData(@In("id") String GLId,
			@In("temperatures") Map<String, Float> temperatures,
			@In("positions") Map<String, Position> positions,
			@In("temperatureThreshold") Float temperatureThreshold,
			@InOut("FFsInDangerInTeam") Set<String> FFsInDangerInTeam) {
		System.out.println(GLId + ": Processing sensor data...");
		FFsInDangerInTeam.clear();
		System.out.println("Temperatures map holds "
				+ temperatures.keySet().size() + " items");
		for (String id : temperatures.keySet()) {
			System.out.println("[" + id + ", " + temperatures.get(id) + "]");
			if (temperatures.get(id) > temperatureThreshold) {
				FFsInDangerInTeam.add(id);
			}
		}
		/*
		 * do not output the members positions for now
		 * System.out.println("Positions map holds " +
		 * temperatures.keySet().size() + " items"); for (String id :
		 * positions.keySet()) { Position pos = positions.get(id);
		 * System.out.println("[" + id + ", {" + pos.latitude + ", " +
		 * pos.longitude + "}]"); }
		 */
	}

	@Process
	public static void outputGMsInDanger(
			@In("isSiteLeader") Boolean isSiteLeader,
			@TriggerOnChange @In("FFsInDangerInSite") Map<String, Set<String>> FFsInDangerInSite) {
		if (isSiteLeader) {
			System.out.println("Firefighters in danger: ");
			int count = 0;
			for (String f : FFsInDangerInSite.keySet()) {
				Set<String> set = FFsInDangerInSite.get(f);
				count += set.size();
				if (set.size() == 0)
					break;
				System.out.print("[" + f + ", (");
				for (String ff : FFsInDangerInSite.get(f)) {
					System.out.print(ff + ", ");
				}
				System.out.println(")]");
			}
			if (count == 0) {
				System.out.println("0");
			}
		}
	}

}
