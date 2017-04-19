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

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.metaadaptation.modeswitchprops.ModeChart;

/**
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class ComponentImpl implements cz.cuni.mff.d3s.metaadaptation.modeswitchprops.Component {

	private final ComponentInstance component;

	private final ModeChartImpl modeChart;
	
	public ComponentImpl(ComponentInstance component){
		if(component == null){
			throw new IllegalArgumentException(String.format("The %s argument is null.", "component"));
		}
		
		this.component = component;
		modeChart = new ModeChartImpl(component.getModeChart(), component);
	}
	
	@Override
	public ModeChart getModeChart() {
		return modeChart;
	}
	
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.modeswitchprops.Component#getUtility()
	 */
	@Override
	public double getUtility() {
		return 0; // TODO:
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.modeswitchprops.Component#getUtilityThreshold()
	 */
	@Override
	public double getUtilityThreshold() {
		return 0; // TODO:
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return component.getKnowledgeManager().getId();
	}

}
