/*******************************************************************************
 * Copyright 2015 Charles University in Prague
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
package cz.cuni.mff.d3s.deeco.modes;

import java.util.Collections;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorExtensionPoint;
import cz.cuni.mff.d3s.deeco.annotations.processor.ModesAwareAnnotationProcessorExtension;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;

public class ModeSwitchingPlugin implements DEECoPlugin {

	/** Period of the reason process of the ModesManager,
	 * changed after EMF model is created in the init(),
	 * default period is 2000 ms */
	private int period = 2000;
	
	public ModeSwitchingPlugin withPeriod(int period) {
		this.period = period;
		return this;
	}
	
	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		// This plugin has no dependencies other than jDEECo core
		return Collections.emptyList();
	}

	@Override
	public void init(DEECoContainer container) {
		AnnotationProcessorExtensionPoint modesAwareAnnotationProcessorExtension = new ModesAwareAnnotationProcessorExtension();
		container.getProcessor().addExtension(modesAwareAnnotationProcessorExtension);
		
		try {
			container.deployComponent(new ModeSwitchingManager());
		} catch (AnnotationProcessorException e) {
			Log.e("Error while trying to deploy ModesManager", e);
		}

		for (ComponentInstance c : container.getRuntimeMetadata().getComponentInstances()) {
			if (c.getName().equals(ModeSwitchingManager.class.getName())) {

				for (ComponentProcess p: c.getComponentProcesses()) {
					for (Trigger t : p.getTriggers()){
						if (t instanceof TimeTrigger) {
							((TimeTrigger) t).setPeriod(period);
						}
					}
				}

			}
		}
	}

}
