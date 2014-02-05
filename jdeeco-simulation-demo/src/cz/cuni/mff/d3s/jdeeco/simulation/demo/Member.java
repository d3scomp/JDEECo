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

import java.util.Random;

import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

/**
 * A component representing an ensemble member, measured member-specific data to be published to a leader.
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 *
 */
@Component 
public class Member extends PositionAwareComponent {
	
	public MemberData memberData;
	public String teamId;

	public Member(String id, String team_id, Position position) {
		super(id, position);		
		this.teamId = team_id;
		this.memberData = new MemberData(25.0f);
	}

	@Process
	@PeriodicScheduling(500)
	public static void measureMemberData(@In("id") String id,
			@Out("memberData") ParamHolder<MemberData> memberData) {
		memberData.value = new MemberData(new Random().nextFloat() * 100);
		System.out.println(id + " new temperature: "
				+ Math.round(memberData.value.temperature) + " degrees Celcious.");
	}

}
