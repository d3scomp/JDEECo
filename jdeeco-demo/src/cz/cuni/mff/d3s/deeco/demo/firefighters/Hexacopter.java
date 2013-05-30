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
import java.util.Map;
import java.util.Random;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.knowledge.Component;
import cz.cuni.mff.d3s.deeco.knowledge.OutWrapper;

/**
 * Template for the hexacopter component.
 * 
 * The hexacopter is an <a
 * href="http://en.wikipedia.org/wiki/Unmanned_aerial_vehicle">Unmanned aerial
 * vehicle</a> that can be used in forest fires to ease communication (relays)
 * and sensing (thermal photographs).
 * 
 * Here, it is just a moving object that lets GoupLeaders exchange information.
 * 
 * @author Ilias Gerostathopoulos
 * 
 */
public class Hexacopter extends Component {

	private static final long serialVersionUID = 4635712337733218714L;

	public Map<String, Set<String>> FFsInDangerInSite;
	public Position leaderPosition;

	public Hexacopter() {
		// id (String) field always exists; it's the root of the tree
		this.id = "H1";
		this.FFsInDangerInSite = new HashMap<String, Set<String>>();
		this.leaderPosition = new Position(300, 300);
	}

	public Hexacopter(String id, Position leaderPosition) {
		// id field always exists; it's the root of the tree
		this.id = id;
		this.FFsInDangerInSite = new HashMap<String, Set<String>>();
		this.leaderPosition = leaderPosition;
	}

	@Process
	@PeriodicScheduling(2000)
	public static void moveAlongGLs(
			@Out("leaderPosition") OutWrapper<Position> leaderPosition) {
		// just a random position inside the (0,0), (0,1000), (1000,1000),
		// (1000,0) square
		Random rnd = new Random();
		leaderPosition.value = new Position(rnd.nextInt(1000),
				rnd.nextInt(1000));
		System.out.println("Hexacopter's new position: ("
				+ leaderPosition.value.latitude + ", "
				+ leaderPosition.value.longitude + ")");
	}
}
