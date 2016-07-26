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
package cz.cuni.mff.d3s.deeco.annotations.processor;

import java.lang.annotation.Annotation;

import cz.cuni.mff.d3s.deeco.annotations.NonDeterministicModeSwitching;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.modes.ModeChart;
import cz.cuni.mff.d3s.deeco.search.SearchParameters;
import cz.cuni.mff.d3s.deeco.search.StateSpaceSearch;
import cz.cuni.mff.d3s.jdeeco.annotations.ComponentModeChart;

/**
 * Processes annotations related to non-deterministic mode switching.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 * @see AnnotationProcessor
 */
public class NonDetModeSwitchAwareAnnotationProcessorExtension extends AnnotationProcessorExtensionPoint {

	@Override
	public void onComponentInstanceCreation(ComponentInstance componentInstance, Annotation unknownAnnotation) {
		if (unknownAnnotation instanceof NonDeterministicModeSwitching) {
			NonDeterministicModeSwitching nonDetModeSwitchAnnotation =
					(NonDeterministicModeSwitching) unknownAnnotation;
			Class<? extends StateSpaceSearch> searchEngine = nonDetModeSwitchAnnotation.searchEngine();
			Class<? extends SearchParameters> searchParameters = nonDetModeSwitchAnnotation.searchParameters();
			
			try { 
				ModeChart modeChart = componentInstance.getModeChart();
				if(modeChart == null){
					throw new IllegalStateException(String.format(
							"The %s requires \"%s\" to be set. Do you miss @%s annotation?",
							this.getClass().getName(), "modeChart", ComponentModeChart.class.getName()));
				}
				StateSpaceSearch sss = searchEngine.newInstance();
				sss.processParameters(searchParameters.newInstance());
				modeChart.setStateSpaceSearch(sss);
				
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
}
