/*******************************************************************************
 * Copyright 2015 Charles University in Prague
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package cz.cuni.mff.d3s.deeco.annotations.processor;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.annotations.ComponentModeChart;
import cz.cuni.mff.d3s.deeco.annotations.ExcludeMode;
import cz.cuni.mff.d3s.deeco.annotations.ExcludeModes;
import cz.cuni.mff.d3s.deeco.annotations.Mode;
import cz.cuni.mff.d3s.deeco.annotations.Modes;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.modes.DEECoMode;
import cz.cuni.mff.d3s.deeco.modes.ModeChart;
import cz.cuni.mff.d3s.deeco.modes.ModeChartHolder;

/**
 * Processes the annotations related to DEECo processes' modes.
 *
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 * @see AnnotationProcessor
 */
public class ModesAwareAnnotationProcessorExtension extends AnnotationProcessorExtensionPoint {

	/**
	 * Keeps the mode classes included in the mode chart associated with the
	 * component currently being parsed
	 */
	private Set<Class<? extends DEECoMode>> includedModeClasses = new HashSet<>();
	
	@Override
	public void onComponentInstanceCreation(ComponentInstance componentInstance, Annotation unknownAnnotation) {
		if (unknownAnnotation instanceof ComponentModeChart) {
			Class<? extends ModeChartHolder> modeChartHolder = ((ComponentModeChart) unknownAnnotation).value();
			try { 
				ModeChart modeChart = (modeChartHolder.newInstance()).getModeChart();
				includedModeClasses = modeChart.getModes();
				modeChart.setComponent(componentInstance);
				
				componentInstance.setModeChart(modeChart);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void onComponentProcessCreation(ComponentProcess componentProcess, Annotation unknownAnnotation) {
		if (unknownAnnotation instanceof Mode) {
			Class<? extends DEECoMode> modeClass = ((Mode) unknownAnnotation).value();
			try {
				componentProcess.getModes().add((modeClass.newInstance()));
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		if (unknownAnnotation instanceof Modes) {
			Class<? extends DEECoMode>[] modeClasses = ((Modes) unknownAnnotation).value();
			
			for (Class<? extends DEECoMode> modeClass : modeClasses) {
				try {
					componentProcess.getModes().add((modeClass.newInstance()));
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}	
			}
		}
		if (unknownAnnotation instanceof ExcludeMode) {
			Class<? extends DEECoMode> excludeModeClass = ((ExcludeMode) unknownAnnotation).value();
			
			if (includedModeClasses.isEmpty()) {
				Log.w("When using the ExcludeMode annotation you should include some modes in the mode chart of the component.");
				return;
			}
			
			for (Class<? extends DEECoMode> modeClass : includedModeClasses) {
				if (!excludeModeClass.equals(modeClass)) {
					try {
						componentProcess.getModes().add((modeClass.newInstance()));
					} catch (InstantiationException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
		if (unknownAnnotation instanceof ExcludeModes) {
			List<Class<? extends DEECoMode>> excludeModeClasses = Arrays.asList(((ExcludeModes) unknownAnnotation).value());
			
			if (includedModeClasses.isEmpty()) {
				Log.w("When using the ExcludeModes annotation you should include some modes in the mode chart of the component.");
				return;
			}
			
			for (Class<? extends DEECoMode> modeClass : includedModeClasses) {
				if (!excludeModeClasses.contains(modeClass)) {
					try {
						componentProcess.getModes().add((modeClass.newInstance()));
					} catch (InstantiationException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
