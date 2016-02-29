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

/**
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class LinearTemperature implements Temperature {

	/**
	 * The number of steps the temperature takes to cool down.
	 */
	private int stepCnt;
		
	/**
	 * The temperature cools from 1 to 0 in the given number of steps.
	 * 
	 * @param stepCnt the number of steps the temperature takes to cool down.
	 */
	public LinearTemperature(int stepCnt){
		this.stepCnt = stepCnt;
	}
	
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.jdeeco.adaptation.search.annealing.Temperature#getTemperature()
	 */
	@Override
	public double getTemperature() {
		
		if(stepCnt > 1){
			// Decrease the stepCnt step by step until it equals 1
			stepCnt--;
		}
		
		return getCurrentTemperature();
	}
	
	/**
	 * Calling this method doesn't affect the temperature value.
	 * 
	 * @return The current temperature.
	 */
	private double getCurrentTemperature(){
		if(stepCnt < 1){
			throw new IllegalStateException(String.format(
					"The %s variable cannot drop below 1.", "stepCnt"));
		}
		
		return 1 - 1/stepCnt;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Double.toString(getCurrentTemperature());
	}

}
