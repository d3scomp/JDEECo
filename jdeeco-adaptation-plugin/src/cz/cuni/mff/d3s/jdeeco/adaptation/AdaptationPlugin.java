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

import java.util.Collections;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.runtime.PluginInitFailedException;
import cz.cuni.mff.d3s.metaadaptation.MAPEAdaptation;
import cz.cuni.mff.d3s.metaadaptation.MetaAdaptationManager;

/**
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class AdaptationPlugin implements DEECoPlugin {

	private long period = 10000;
	
	private MetaAdaptationManager adaptationManager;
	
	/** Plugin dependencies. */
	static private final List<Class<? extends DEECoPlugin>> DEPENDENCIES =
			Collections.emptyList();
	
	public AdaptationPlugin withVerbosity(boolean verbosity){
		return this;
	}
	
	public AdaptationPlugin withPeriod(long period) {
		this.period = period;
		return this;
	}
	
	public void registerAdaptation(MAPEAdaptation adaptation){
		if(adaptationManager == null){
			throw new IllegalStateException(String.format(
					"Registering %s but %s not initialized.",
					adaptation.getClass().getName(),
					this.getClass().getName()));
		}
		
		adaptationManager.addAdaptation(adaptation);
	}
		
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin#getDependencies()
	 */
	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return DEPENDENCIES;
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin#init(cz.cuni.mff.d3s.deeco.runtime.DEECoContainer)
	 */
	@Override
	public void init(DEECoContainer container) throws PluginInitFailedException {
		try {
			adaptationManager = new MetaAdaptationManager();
			container.deployComponent(new AdaptationComponent(adaptationManager));
			
			for (ComponentInstance c : container.getRuntimeMetadata().getComponentInstances()) {
				if (c.getName().equals(MetaAdaptationManager.class.getName())) {
					// Adjust non-deterministic mode switching manager periods
					for (ComponentProcess p: c.getComponentProcesses()) {
						if(p.getName().equals("reason")){
							for (Trigger t : p.getTriggers()){
								if (t instanceof TimeTrigger) {
									((TimeTrigger) t).setPeriod(period);
								}
							}
						}
					}
				}
			}
		} catch (AnnotationProcessorException e) {
			Log.e("Error while trying to deploy AdaptationManager", e);
		}
	}

}
