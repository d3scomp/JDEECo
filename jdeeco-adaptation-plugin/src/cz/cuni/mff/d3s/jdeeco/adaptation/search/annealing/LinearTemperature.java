/*******************************************************************************
 * Copyright 2016 Charles University in Prague
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *******************************************************************************/
package cz.cuni.mff.d3s.jdeeco.adaptation.search.annealing;

import cz.cuni.mff.d3s.deeco.timer.SimulationTimer;

/**
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class LinearTemperature implements Temperature {

	/**
	 * The time of the cool down.
	 */
	private long coolDownTime;
	
	private long coolStartTime;
	
	private SimulationTimer timer;
		
	/**
	 * The temperature cools from 1 to 0 linearly in the given amount of time.
	 * 
	 * @param start The time when the cooling starts.
	 * @param finish The time when the cooling ends.
	 */
	public LinearTemperature(long start, long finish, SimulationTimer timer){
		coolStartTime = start;
		coolDownTime = finish;
		this.timer = timer;
	}
	
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.jdeeco.adaptation.search.annealing.Temperature#getTemperature()
	 */
	@Override
	public double getTemperature() {
						
		return getCurrentTemperature(timer.getCurrentMilliseconds());
	}
	
	/**
	 * Calling this method doesn't affect the temperature value.
	 * 
	 * @return The current temperature.
	 */
	private double getCurrentTemperature(long currentTime){
		if(currentTime < coolStartTime){
			return 1;
		}
		if(currentTime > coolDownTime){
			return 0;
		}
		
		return 1 - ((currentTime - coolStartTime) / (coolDownTime - coolStartTime));
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Double.toString(getCurrentTemperature(
				timer.getCurrentMilliseconds()));
	}

}
