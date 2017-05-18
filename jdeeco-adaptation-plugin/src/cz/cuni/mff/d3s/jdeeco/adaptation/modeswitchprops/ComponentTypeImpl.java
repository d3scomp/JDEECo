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
package cz.cuni.mff.d3s.jdeeco.adaptation.modeswitchprops;

import cz.cuni.mff.d3s.jdeeco.adaptation.AdaptationUtility;
import cz.cuni.mff.d3s.metaadaptation.modeswitchprops.ComponentType;

/**
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class ComponentTypeImpl implements ComponentType {

	private final AdaptationUtility utility;

	
	public ComponentTypeImpl(AdaptationUtility utility){
		if(utility == null){
			throw new IllegalArgumentException(String.format(
					"The %s argument is null.", "utility"));
		}
		
		this.utility = utility;
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.modeswitch.ComponentType#getAverageUtility()
	 */
	@Override
	public double getAverageUtility() {
		return utility.getUtility(null);
	}
	
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.modeswitch.ComponentType#getUtilityThreshold()
	 */
	@Override
	public double getUtilityThreshold() {
		return utility.getUtilityThreshold();
	}

}
