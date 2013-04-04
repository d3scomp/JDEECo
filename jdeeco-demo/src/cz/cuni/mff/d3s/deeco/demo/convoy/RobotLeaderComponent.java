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
package cz.cuni.mff.d3s.deeco.demo.convoy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.knowledge.Component;
import cz.cuni.mff.d3s.deeco.knowledge.OutWrapper;


public class RobotLeaderComponent extends Component {

	public final static long serialVersionUID = 1L;
	
	public Integer battery;
	public Path path;
	public List<Path> crossingRobots;

	public RobotLeaderComponent() {
		this.id = "leader";
		this.battery = new Integer(100);
		this.path = new Path();
		this.path.currentPosition = new Integer(0);
		this.path.remainingPath = new ArrayList<Integer>(Arrays.asList(new Integer[] {new Integer(1), new Integer(2),
				new Integer(3), new Integer(4), new Integer(5), new Integer(6),
				new Integer(7), new Integer(8), new Integer(9)}));
		this.crossingRobots = null;
	}

	/*
	 * Input: path, crossingRobots, convoyRobot Output: path
	 */
	@PeriodicScheduling(3000)
	@Process
	public static void process(@InOut("path") Path path,
			@InOut("battery") OutWrapper<Integer> battery) {
		//System.out.println("[RobotLeaderComponent.process] remainingPath = " + path.remainingPath);
		if (path.remainingPath.size() > 0) {
			path.currentPosition = path.remainingPath.remove(0);
			battery.value = new Integer(battery.value - 1);
			System.out.println("Leader is moving: " + path.remainingPath);
		}
	}
}
