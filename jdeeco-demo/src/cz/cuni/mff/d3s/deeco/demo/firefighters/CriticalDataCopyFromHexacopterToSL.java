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

import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.definitions.EnsembleDefinition;

/**
 * Captures the interaction between the Hexacopter and the Site Leader.
 * 
 * @author Ilias Gerostathopoulos
 * 
 */
public class CriticalDataCopyFromHexacopterToSL extends EnsembleDefinition {

	private static final long serialVersionUID = 409847004319943982L;

	@Membership
	public static boolean membership(@In("member.id") String mId,
			@In("coord.id") String cId,
			@In("member.leaderPosition") Position mLeaderPosition,
			@In("coord.leaderPosition") Position cLeaderPosition,
			@In("coord.spectrum") float cSpectrum,
			@In("coord.isSiteLeader") Boolean isSiteLeader) {
		return isSiteLeader
				&& !mId.equals(cId)
				&& Utils.isInSpectrum(cLeaderPosition, mLeaderPosition,
						cSpectrum);
	}

	@KnowledgeExchange
	@PeriodicScheduling(4000)
	public static void map(
			@In("coord.id") String cId,
			@In("member.FFsInDangerInSite") Map<String, Set<String>> mFFsInDangerInSite,
			@InOut("coord.FFsInDangerInSite") Map<String, Set<String>> cFFsInDangerInSite) {
		Set<String> set;
		for (String GLId : mFFsInDangerInSite.keySet()) {
			set = mFFsInDangerInSite.get(GLId);
			cFFsInDangerInSite.put(GLId, set);
		}
		System.out
				.println("Copying data from hexacopter to GroupLeader " + cId);
	}
}
