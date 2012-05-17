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
package cz.cuni.mff.d3s.deeco.demo;

import java.util.Arrays;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.DEECoComponent;
import cz.cuni.mff.d3s.deeco.annotations.DEECoInitialize;
import cz.cuni.mff.d3s.deeco.annotations.DEECoPeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.DEECoProcess;
import cz.cuni.mff.d3s.deeco.annotations.DEECoProcessIn;
import cz.cuni.mff.d3s.deeco.annotations.DEECoProcessInOut;
import cz.cuni.mff.d3s.deeco.knowledge.OutWrapper;
import cz.cuni.mff.d3s.deeco.knowledge.RootKnowledge;

@DEECoComponent
public class RobotFollowerComponent extends RootKnowledge {

	public Integer battery;
	public Path path;
	public String convoyRobot; // 0 if there is no robot ahead 1 otherwise
	public List<Path> crossingRobots;

	@DEECoInitialize
	public static RootKnowledge getInitialKnowledge() {
		RobotFollowerComponent k = new RobotFollowerComponent();
		k.battery = new Integer(100);
		k.path = new Path();
		k.path.currentPosition = 1;
		k.path.remainingPath = Arrays.asList(new Integer[] { new Integer(2),
				new Integer(3), new Integer(4), new Integer(5), new Integer(6),
				new Integer(7), new Integer(8), new Integer(9)});
		k.convoyRobot = null;
		k.crossingRobots = null;
		return k;
	}

	@DEECoProcess
	@DEECoPeriodicScheduling(4000)
	public static void process(@DEECoProcessInOut("path") Path path,
			@DEECoProcessInOut("battery") OutWrapper<Integer> battery,
			@DEECoProcessIn("convoyRobot") String convoyRobot) {
		if (convoyRobot != null && path.remainingPath.size() > 0) {
				path.currentPosition = path.remainingPath.remove(0);
				battery.item = new Integer(battery.item - 1);
				System.out.println("Follower is moving: " + path.remainingPath);
		}
	}
}
