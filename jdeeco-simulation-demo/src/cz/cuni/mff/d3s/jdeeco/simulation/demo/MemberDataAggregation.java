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

import java.util.Arrays;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.annotations.CommunicationBoundary;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.network.KnowledgeData;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

/**
 * Captures the interaction between the Group Members and the Group Leaders.
 * 
 * @author Ilias Gerostathopoulos
 * 
 */
@Ensemble
@PeriodicScheduling(1000)
public class MemberDataAggregation {

	private static final long serialVersionUID = 5991804902054860542L;

	@Membership
	public static boolean membership(
			@In("member.teamId") String mteamId,
			@In("member.memberData") MemberData memberData,
			@In("member.position") Position position,
			@In("coord.teamId") String cteamId,
			@In("coord.memberAggregateData") Map<String, MemberData> memberAggregateData,
			@In("coord.memberPositions") Map<String, Position> memberPositions) {
		return mteamId.equals(cteamId) && memberData != null
				&& memberAggregateData != null && position != null
				&& memberPositions != null;
	}

	@KnowledgeExchange
	public static void map(@In("member.id") String mId,
			@In("coord.id") String cId,
			@In("member.position") Position newPosition,
			@InOut("coord.memberPositions") ParamHolder<Map<String, Position>> memberPositions,
			@In("member.memberData") MemberData newMemberData,
			@InOut("coord.memberAggregateData") ParamHolder<Map<String, MemberData>> memberAggregateData) {
		
		memberAggregateData.value.put(mId, newMemberData);
		memberPositions.value.put(mId, newPosition);
	}
	
	@CommunicationBoundary
	public static boolean boundary(KnowledgeData data, ReadOnlyKnowledgeManager sender) throws KnowledgeNotFoundException {
		return true;
//		KnowledgePath kpPosition = KnowledgePathBuilder.buildSimplePath("position");
//		KnowledgePath kpTeam = KnowledgePathBuilder.buildSimplePath("teamId");
//		//Position ownerPos = (Position) data.getKnowledge().getValue(kpPosition);
//		Position senderPos = (Position) sender.get(Arrays.asList(kpPosition)).getValue(kpPosition);
//		String ownerTeam = (String) data.getKnowledge().getValue(kpTeam);
//		
////		return distance(ownerPos, senderPos) < 550;
//		
//		// if the current component does not hawe a position or the owner does
//		// not belong to a team then we do not propagate the knowledge (includes
//		// all O*)
//		if (senderPos == null || ownerTeam == null)
//			return false;
//		return TeamLocationService.INSTANCE.isAtTheTeamsSite(ownerTeam, senderPos);
	}
	
	private static double distance(Position a, Position b) {
		return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
	}

}
