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
package cz.cuni.mff.d3s.jdeeco.simulation.demo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.TriggerOnChange;
import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.deeco.task.ProcessContext;


/**
 * A leader component focusing on aggregating member data.
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 *
 */
@Component
public class Leader extends PositionAwareComponent {

	public static float TEMPERATURE_THRESHOLD = 50.0f;
	public String teamId;
	public Map<String, MemberData> memberAggregateData;
	public Map<String, Position> memberPositions;

	// list of supervised group members in danger:
	public Set<String> membersInDanger;
	
		
	// constant to determine critical condition
	public Float temperatureThreshold = 50.0f;
	
	
	public Leader(String id, String team_id, Position position, boolean hasIP)  {
		super(id, position, hasIP);
		this.teamId = team_id;
		this.memberPositions = new HashMap<>();
		this.memberAggregateData = new HashMap<>();
		this.membersInDanger = new HashSet<>();
	}

	@Process
	@PeriodicScheduling(period=500)	
	public static void processMemberData(@In("id") String id,
			@In("memberAggregateData") Map<String, MemberData> memberAggregateData,
			@In("memberPositions") Map<String, Position> memberPositions,
			@InOut("membersInDanger") ParamHolder<Set<String>> membersInDanger) {
		StringBuffer sb = new StringBuffer();

		sb.append(id + ": Processing member data of ");

		sb.append(memberAggregateData.keySet().size() + " members ");
		for (String mid : memberAggregateData.keySet()) {
			sb.append("[" + mid + ", " + memberAggregateData.get(mid).temperature + "]");
			if (memberAggregateData.get(mid).temperature > TEMPERATURE_THRESHOLD) {
				if (!membersInDanger.value.contains(mid)) {
					membersInDanger.value.add(mid);
					long currentTime = ProcessContext.getTimeProvider().getCurrentMilliseconds();
					Log.d(String.format("Leader %s discovered at %d that %s got in danger", 
							id, currentTime, mid));
				}
			}
		}
		System.out.println(sb.toString());
	}

	@Process
	public static void outputGMsInDanger(		
			@In("id") String id,
			@TriggerOnChange @In("membersInDanger")  Set<String> membersInDanger) {
		
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("(%d) %s: Firefighters in danger: ", ProcessContext.getTimeProvider().getCurrentMilliseconds(), id));
		for (String ff: membersInDanger) {
			sb.append(ff + ", ");
		}
		if (membersInDanger.isEmpty()) {
			sb.append("0");
		}
		
		System.err.println(sb.toString());
		
	}

}
