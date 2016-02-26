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

import java.util.Random;

import cz.cuni.mff.d3s.deeco.logging.Log;

/**
 * This class employs simulated annealing algorithm to search optimum in a given state space.
 * For detailed description of this method see this
 * <a href="https://en.wikipedia.org/wiki/Simulated_annealing">wikipedia</a> article.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class Annealing {
	
	private Temperature temperature;
	private AcceptanceProbabilityFunction acceptanceProbability;
	private Random rand;
	
	public Annealing(Temperature temperature,
			AcceptanceProbabilityFunction acceptanceProbability){
		this.temperature = temperature;
		this.acceptanceProbability = acceptanceProbability;
		
		rand = new Random();
	}
	
	public Annealing(Temperature temperature, 
			AcceptanceProbabilityFunction acceptanceProbability, int seed){
		this.temperature = temperature;
		this.acceptanceProbability = acceptanceProbability;
		
		rand = new Random(seed);
	}
	
	public AnnealingState getNextState(AnnealingState currentState){
		AnnealingState[] neighbors = currentState.getNeighbors();
		if(neighbors.length < 1){
			Log.w(String.format("The state %s has no neighbors. Annealing search got stuck.",
					currentState));
			return currentState;
		}
		
		// Randomly choose next state
		AnnealingState newState = neighbors[rand.nextInt(neighbors.length)];
		
		// Accept the next state with the probability depending on the temperature
		return acceptanceProbability.getAcceptanceProbability(
				currentState, newState, temperature) >= rand.nextDouble()
				? newState
				: currentState;
	}
}
