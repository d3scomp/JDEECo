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
import java.util.Set;

import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

/**
 * Captures the interaction between the Group Leaders and the Hexacopter.
 * 
 * @author Ilias Gerostathopoulos
 * 
 */

@Ensemble
@PeriodicScheduling(period=4000)
public class CriticalDataAggregationOnHexacopter {

	private static final long serialVersionUID = -7835430643756278821L;

	@Membership
	public static boolean membership(@In("member.id") String mId,
			@In("coord.id") String cId,
			@In("member.leaderPosition") Position mLeaderPosition,
			@In("coord.leaderPosition") Position cLeaderPosition,
			@In("member.spectrum") float mSpectrum) {
		return !mId.equals(cId)
				&& Utils.isInSpectrum(mLeaderPosition, cLeaderPosition,
						mSpectrum);
	}

	@KnowledgeExchange
	public static void map(
			@In("member.id") String mId,
			@In("member.FFsInDangerInTeam") Set<String> FFsInDangerInTeam,
			@InOut("coord.FFsInDangerInSite") ParamHolder<Map<String, Set<String>>> FFsInDangerInSite) {
		System.out.println("Copying GMsInDanger set from " + mId
				+ " to the hexacopter.");
		FFsInDangerInSite.value.put(mId, FFsInDangerInTeam);
	}
}
