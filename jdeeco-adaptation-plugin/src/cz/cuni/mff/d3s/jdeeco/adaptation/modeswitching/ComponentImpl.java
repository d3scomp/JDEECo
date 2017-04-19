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
package cz.cuni.mff.d3s.jdeeco.adaptation.modeswitching;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.metaadaptation.modeswitch.ComponentType;
import cz.cuni.mff.d3s.metaadaptation.modeswitch.ModeChart;

/**
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class ComponentImpl implements cz.cuni.mff.d3s.metaadaptation.modeswitch.Component {

	private final ComponentInstance componentInstance;
	
	private final ModeChartImpl modeChart;
	
	private final ComponentTypeImpl componentType;
	
	
	public ComponentImpl(ComponentInstance ci, ComponentTypeImpl componentType){
		if(ci == null) {
			throw new IllegalArgumentException(String.format(
					"The %s argument is null.", "ci"));
		}
		if(componentType == null) {
			throw new IllegalArgumentException(String.format(
					"The %s argument is null.", "componentType"));
		}
		
		componentInstance = ci;
		modeChart = new ModeChartImpl(ci.getModeChart(), ci);
		this.componentType = componentType;
	}
	
	public ComponentInstance getComponentInstance(){
		return componentInstance;
	}
	
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.modeswitch.Component#getModeChart()
	 */
	@Override
	public ModeChart getModeChart() {
		return modeChart;
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.modeswitch.Component#getType()
	 */
	@Override
	public ComponentType getType() {
		return componentType;
	}

}
