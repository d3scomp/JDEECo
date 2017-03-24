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

import java.io.IOException;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.task.ProcessContext;
import cz.cuni.mff.d3s.jdeeco.adaptation.AdaptationUtility;
import cz.cuni.mff.d3s.metaadaptation.modeswitch.ModeChart;
import cz.cuni.mff.d3s.metaadaptation.search.StateSpaceSearch;

/**
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class ComponentImpl implements cz.cuni.mff.d3s.metaadaptation.modeswitch.Component {

	private final ComponentInstance componentInstance;

	private final StateSpaceSearch stateSpaceSearch;
	
	private final ModeChartImpl modeChart;
	
	private final AdaptationUtility adaptationUtility;
	
	public ComponentImpl(ComponentInstance ci, StateSpaceSearch sss, AdaptationUtility adaptationUtility){
		if(ci == null) {
			throw new IllegalArgumentException(String.format(
					"The %s argument is null.", "ci"));
		}
		if(sss == null) {
			throw new IllegalArgumentException(String.format(
					"The %s argument is null.", "sss"));
		}
		if(adaptationUtility == null) {
			throw new IllegalArgumentException(String.format(
					"The %s argument is null.", "adaptationUtility"));
		}
		
		componentInstance = ci;
		stateSpaceSearch = sss;
		modeChart = new ModeChartImpl((cz.cuni.mff.d3s.jdeeco.modes.ModeChartImpl) ci.getModeChart(), ci);
		this.adaptationUtility = adaptationUtility;
	}
	
	public ComponentInstance getComponentInstance(){
		return componentInstance;
	}
	
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.modeswitch.Component#getId()
	 */
	@Override
	public String getId() {
		return componentInstance.getKnowledgeManager().getId();
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.modeswitch.Component#getModeChart()
	 */
	@Override
	public ModeChart getModeChart() {
		return modeChart;
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.modeswitch.Component#getStateSpaceSearch()
	 */
	@Override
	public StateSpaceSearch getStateSpaceSearch() {
		return stateSpaceSearch;
	}
	
	@Override
	public void nonDeterminismLevelChanged(double probability){
		// Log the current non-determinism level
		try {
			NonDeterministicLevelRecord record = new NonDeterministicLevelRecord("EMS"); // Enhanced Mode Switching
			record.setProbability(probability);
			record.setComponent(componentInstance);
			ProcessContext.getRuntimeLogger().log(record);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.modeswitch.Component#getUtility()
	 */
	@Override
	public double getUtility() {
		return adaptationUtility.getUtility(componentInstance);
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.modeswitch.Component#restartUtility()
	 */
	@Override
	public void restartUtility() {
		adaptationUtility.restart();
		
	}

}
