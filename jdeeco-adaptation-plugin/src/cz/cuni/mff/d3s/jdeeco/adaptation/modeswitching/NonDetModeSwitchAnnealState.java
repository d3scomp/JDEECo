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
package cz.cuni.mff.d3s.jdeeco.adaptation.modeswitching;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.metaadaptation.modeswitch.NonDeterministicModeSwitchingManager;
import cz.cuni.mff.d3s.metaadaptation.search.annealing.AnnealingState;

/**
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class NonDetModeSwitchAnnealState implements AnnealingState {

//	public static double NON_DETERMINISTIC_STEP = 0.00005;
//	
//	private double nondeterminism;
//	private double energy;
//	private NonDetModeSwitchAnnealStateSpace stateSpace;
//	
//	public NonDetModeSwitchAnnealState(double nondeterminism,
//			NonDetModeSwitchAnnealStateSpace stateSpace) {
//		this.nondeterminism = nondeterminism;
//		this.stateSpace = stateSpace;
//		
//		energy = NonDeterministicModeSwitchingManager.DEFAULT_ENERGY;
//	}
//	
//	/* (non-Javadoc)
//	 * @see cz.cuni.mff.d3s.jdeeco.adaptation.search.annealing.AnnealingState#getEnergy()
//	 */
//	@Override
//	public double getEnergy() {
//		return energy;
//	}
//	
//	public void setEnergy(double energy){
//		this.energy = energy;
//	}
//	
//	public double getNondeterminism() {
//		return nondeterminism;
//	}
//	
//	public void setNondeterminism(double nondeterminism){
//		this.nondeterminism = nondeterminism;
//	}
//
//	/* (non-Javadoc)
//	 * @see cz.cuni.mff.d3s.jdeeco.adaptation.search.annealing.AnnealingState#getNeighbors()
//	 */
//	@Override
//	public AnnealingState[] getNeighbors() {
//		if(nondeterminism == 0){
//			return new NonDetModeSwitchAnnealState[]{};
//		}
//
//		double lower = nondeterminism - NON_DETERMINISTIC_STEP;
//		double upper = nondeterminism + NON_DETERMINISTIC_STEP;
//		List<NonDetModeSwitchAnnealState> neighbors = 
//			new ArrayList<>();
//		if(lower > 0){
//			neighbors.add(stateSpace.getState(lower));
//		}
//		if(upper < 1){
//			neighbors.add(stateSpace.getState(upper));
//		}
//		
//		return neighbors.toArray(new NonDetModeSwitchAnnealState[]{});
//	}
//
//	/* (non-Javadoc)
//	 * @see java.lang.Object#equals(java.lang.Object)
//	 */
//	@Override
//	public boolean equals(Object obj) {
//		if(!(obj instanceof NonDetModeSwitchAnnealState)){
//			return false;
//		}
//		NonDetModeSwitchAnnealState other = 
//				(NonDetModeSwitchAnnealState) obj;
//		
//		return Double.compare(this.nondeterminism,
//				other.nondeterminism) == 0;
//	}
//	
//	/* (non-Javadoc)
//	 * @see java.lang.Object#hashCode()
//	 */
//	@Override
//	public int hashCode() {
//		return Double.hashCode(nondeterminism);
//	}
//	
//	/* (non-Javadoc)
//	 * @see java.lang.Object#toString()
//	 */
//	@Override
//	public String toString() {
//		return String.format("NonDetModeSwitchAnnealState with non-determinism == %f",
//				nondeterminism);
//	}
}
