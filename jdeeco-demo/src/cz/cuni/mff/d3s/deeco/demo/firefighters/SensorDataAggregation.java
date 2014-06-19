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

import java.util.Map;

import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

/**
 * Captures the interaction between the Group Members and the Group Leaders.
 * 
 * @author Ilias Gerostathopoulos
 * 
 */
@Ensemble
@PeriodicScheduling(period=1000)
public class SensorDataAggregation {

	private static final long serialVersionUID = 5991804902054860542L;

	@Membership
	public static boolean membership(@In("member.teamId") String mteamId,
			@In("member.temperature") Float temperature,
			@In("coord.temperatures") Map<String, Float> temperatures,
			@In("member.position") Position position,
			@In("coord.positions") Map<String, Position> positions,
			@In("coord.teamId") String cteamId) {
		return mteamId.equals(cteamId) && temperature != null
				&& temperatures != null && position != null
				&& positions != null;
	}

	@KnowledgeExchange
	public static void map(@In("member.id") String mId,
			@In("coord.id") String cId,
			@In("member.position") Position newPosition,
			@InOut("coord.positions") ParamHolder<Map<String, Position>> positions,
			@In("member.temperature") Float newTemperature,
			@InOut("coord.temperatures") ParamHolder<Map<String, Float>> temperatures) {
		System.out.println("Copying sensor data from " + mId + " to " + cId);
		temperatures.value.put(mId, newTemperature);
		positions.value.put(mId, newPosition);
	}

}
