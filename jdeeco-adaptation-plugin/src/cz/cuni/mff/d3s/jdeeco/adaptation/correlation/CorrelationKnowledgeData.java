/*******************************************************************************
 * Copyright 2017 Charles University in Prague
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
package cz.cuni.mff.d3s.jdeeco.adaptation.correlation;

import java.util.Map;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.Local;
import cz.cuni.mff.d3s.deeco.annotations.SystemComponent;
import cz.cuni.mff.d3s.metaadaptation.correlation.CorrelationMetadataWrapper;

/**
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */

@Component
@SystemComponent
public class CorrelationKnowledgeData {
	/**
	 * Specify whether to print the values being processed by the correlation computation.
	 */
	@Local
	static boolean dumpValues = false;

	@Local
	static boolean verbose = false;
	
	@Local
	static boolean logGeneratedEnsembles = false;
	
	public String id = "CorrelationData";

	/**
	 * Holds the knowledge of all the other components in the system. The @link{CorrelationDataAggregation}
	 * ensemble ensures the data are up-to-date.
	 *
	 * String - ID of a component
	 * String - Label of a knowledge field of the component
	 * MetadataWrapper - knowledge field value together with its meta data
	 */
	@Local
	public Map<String, Map<String, CorrelationMetadataWrapper<? extends Object>>> knowledgeOfAllComponents;
}
