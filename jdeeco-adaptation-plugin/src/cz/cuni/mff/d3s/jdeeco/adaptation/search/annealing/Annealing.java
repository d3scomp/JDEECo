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
import cz.cuni.mff.d3s.deeco.search.SearchParameters;
import cz.cuni.mff.d3s.deeco.search.SearchState;
import cz.cuni.mff.d3s.deeco.search.StateSpaceSearch;

/**
 * This class employs simulated annealing algorithm to search optimum in a given state space.
 * For detailed description of this method see this
 * <a href="https://en.wikipedia.org/wiki/Simulated_annealing">wikipedia</a> article.
 * 
 * <p>
 * The {@link SearchParameters} accepted by this {@link StateSpaceSearch}:
 * <ul>
 *   <li>"temperature" : {@link Temperature} (mandatory)</li>
 *   <li>"probability" : {@link AcceptanceProbabilityFunction} (mandatory)</li>
 *   <li>"seed" : {@link Integer} (optional)</li>
 * </p>
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class Annealing implements StateSpaceSearch {
	
	public static final String TEMPERATURE_PARAMETER = "temperature";
	public static final String PROBABILITY_PARAMETER = "probability";
	public static final String SEED_PARAMETER = "seed";
	
	private Temperature temperature;
	private AcceptanceProbabilityFunction acceptanceProbability;
	private Random rand;
	
	public Annealing(){
		// All the fields are required to be set in the processParameters method
		temperature = null;
		acceptanceProbability = null;
		rand = null;
	}
	
	@Override
	public AnnealingState getNextState(SearchState currentState){
		if(!(currentState instanceof AnnealingState)){
			throw new IllegalArgumentException(String.format(
					"The \"%s\" argument has to be instance of %s",
					"currentState", AnnealingState.class));
		}
		if(temperature == null){
			throw new IllegalStateException(String.format(
					"The \"%s\" parameter not set.", TEMPERATURE_PARAMETER));
		}
		if(acceptanceProbability == null){
			throw new IllegalStateException(String.format(
					"The \"%s\" parameter not set.", PROBABILITY_PARAMETER));
		}
		
		AnnealingState current = (AnnealingState) currentState;
		
		AnnealingState[] neighbors = current.getNeighbors();
		if(neighbors.length < 1){
			Log.w(String.format("The state %s has no neighbors. Annealing search got stuck.",
					current));
			return current;
		}
		
		// Randomly choose next state
		AnnealingState newState = neighbors[rand.nextInt(neighbors.length)];
		
		// Accept the next state with the probability depending on the temperature
		return acceptanceProbability.getAcceptanceProbability(
				current, newState, temperature) >= rand.nextDouble()
				? newState
				: current;
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.jdeeco.adaptation.search.StateSpaceSearch#processParameters(cz.cuni.mff.d3s.jdeeco.adaptation.search.SearchParameters)
	 */
	@Override
	public void processParameters(SearchParameters parameters) {
		if(parameters == null){
			throw new IllegalArgumentException(String.format(
					"The \"%s\" argument cannot be null.", "parameters"));
		}
		
		Object oTemperature = parameters.getParameter(TEMPERATURE_PARAMETER);
		Object oProbability = parameters.getParameter(PROBABILITY_PARAMETER);
		Object oSeed = parameters.getParameter(SEED_PARAMETER);
		
		if(oTemperature == null){
			throw new IllegalArgumentException(String.format(
					"The \"%s\" parameter is required by the %s class.",
					TEMPERATURE_PARAMETER, Annealing.class.getName()));
		}
		if(!(oTemperature instanceof Temperature)){
			throw new IllegalArgumentException(String.format(
					"The \"%s\" parameter is not an instance of %s.",
					TEMPERATURE_PARAMETER, Temperature.class.getName()));
		}
		
		if(oProbability == null){
			throw new IllegalArgumentException(String.format(
					"The \"%s\" parameter is required by the %s class.",
					PROBABILITY_PARAMETER, Annealing.class.getName()));
		}
		if(!(oProbability instanceof AcceptanceProbabilityFunction)){
			throw new IllegalArgumentException(String.format(
					"The \"%s\" parameter is not an instance of %s.",
					PROBABILITY_PARAMETER,
					AcceptanceProbabilityFunction.class.getName()));
		}
		
		if(oSeed != null && !(oSeed instanceof Integer)){
			throw new IllegalArgumentException(String.format(
					"The \"%s\" parameter is not an instance of %s.",
					SEED_PARAMETER,
					Integer.class.getName()));
		}
		
		temperature = (Temperature) oTemperature;
		acceptanceProbability = (AcceptanceProbabilityFunction) oProbability;
		rand = oSeed != null
				? new Random((int) oSeed)
				: new Random();
	}
}
