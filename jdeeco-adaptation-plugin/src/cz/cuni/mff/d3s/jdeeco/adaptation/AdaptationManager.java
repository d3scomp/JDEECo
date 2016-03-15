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
package cz.cuni.mff.d3s.jdeeco.adaptation;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.Local;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.SystemComponent;

/**
 * Adapts the annotated components in the same DEECo node by adding
 * non-deterministic mode transitions.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
@Component
@SystemComponent
public class AdaptationManager {

	@Local
	static boolean verbose = false;

	@Local
	static double applicabilityThreshold = 0.5; // FIXME: Make this either parameter or provided by each adaptation
	
	/**
	 * Mandatory knowledge field - id of the component.
	 */
	public String id = "AdaptationManager";
	
	@Local
	public List<MAPEAdaptation> adaptations;
	
	public AdaptationManager(){
		adaptations = new ArrayList<>();
	}
	
	public void addAdaptation(MAPEAdaptation adaptation){
		adaptations.add(adaptation);
	}
	
	/**
	 * Run the MAPE loop of all registered {@link MAPEAdaptation}s.
	 * The period of this process can be set via the 
	 * {@link AdaptationPlugin}.
	 * @param id
	 */
	@Process
	@PeriodicScheduling(period = 1000)
	public static void reason(@In("id") String id,
			@In("adaptations") List<MAPEAdaptation> adaptations) {
		for(MAPEAdaptation adaptation : adaptations){
			adaptation.monitor();
			double applicability = adaptation.analyze();
			if(applicability >= applicabilityThreshold){
				adaptation.plan();
				adaptation.execute();
			}
		}
		
	}
	
}
