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
 * The acceptance probability function for simulated annealing.
 * For detailed description of this method see this
 * <a href="https://en.wikipedia.org/wiki/Simulated_annealing">wikipedia</a> article.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public interface AcceptanceProbabilityFunction {
	
	/** 
	 * As the temperature T tends to 0, the probability gets to
	 * <ul>
	 *   <li>0 if S' > S</li>
	 *   <li>1 if S' < S</li>  
	 * </ul>
	 * Where S is the current state and S' is the next state. Supposing that
	 * states with a smaller energy are better than those with a greater energy.
	 * 
	 * @param currentState is the current state.
	 * @param newState is the new state candidate.
	 * @param temperature is the current system temperature.
	 * @return the probability of accepting the new state.
	 */
	double getAcceptanceProbability(AnnealingState currentState,
			AnnealingState newState, Temperature temperature);
}
