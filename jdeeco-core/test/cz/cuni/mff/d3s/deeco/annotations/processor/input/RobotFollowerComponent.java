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
package cz.cuni.mff.d3s.deeco.annotations.processor.input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.TriggerOnChange;
import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.knowledge.OutWrapper;

@Component
public class RobotFollowerComponent {

	public final static long serialVersionUID = 1L;
	
	public Integer battery;
	public Path path;
	public String convoyRobot; 
	public List<Path> crossingRobots;

	public RobotFollowerComponent() {
//		this.id = "follower"; FIXME(IG): what should be done for this?
		this.battery = new Integer(100);
		this.path = new Path();
		this.path.currentPosition = new Integer(1);
		this.path.remainingPath = new ArrayList<Integer>(
				Arrays.asList(new Integer[] { new Integer(2), new Integer(3),
						new Integer(4), new Integer(5), new Integer(6),
						new Integer(7), new Integer(8), new Integer(9) }));
		this.convoyRobot = null;
		this.crossingRobots = null;
	}

	@Process
	@PeriodicScheduling(6000)
	public static void move(@Out("path.level2") Path path,
			@InOut("battery") OutWrapper<Integer> battery,
			@In("convoyRobot") @TriggerOnChange String convoyRobot) {
		if (path.remainingPath.size() > 0) {
			path.currentPosition = path.remainingPath.remove(0);
			battery.value = new Integer(battery.value - 1);
			System.out.println("Follower is moving: " + path.remainingPath);
		}
	}
	
	@Process
	@PeriodicScheduling(5000)
	public static void follow(@In("path") Path path,
			@InOut("battery.level2.level3") @TriggerOnChange OutWrapper<Integer> battery,
			@In("convoyRobot") String convoyRobot) {
		
	}
	
}
