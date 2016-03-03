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
package cz.cuni.mff.d3s.jdeeco.adaptation.modeswitching;

import java.util.Arrays;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorExtensionPoint;
import cz.cuni.mff.d3s.deeco.annotations.processor.NonDetModeSwitchAwareAnnotationProcessorExtension;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.runtime.PluginInitFailedException;
import cz.cuni.mff.d3s.jdeeco.modes.ModeSwitchingPlugin;

/**
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class NonDeterministicModeSwitchingPlugin implements DEECoPlugin {

	private long startTime = 0;
	private Class<? extends NonDetModeSwitchFitnessEval> evalClass = null;
	private long evalPeriod = 100;
	private long reconfPeriod = 1000;
	private double startingNondeterminism = 0.0001;
	
	private boolean verbose = false;
	
	/** Plugin dependencies. */
	@SuppressWarnings("unchecked")
	static private final List<Class<? extends DEECoPlugin>> DEPENDENCIES =
			Arrays.asList(new Class[]{ModeSwitchingPlugin.class});
	
	public NonDeterministicModeSwitchingPlugin(Class<? extends NonDetModeSwitchFitnessEval> evalClass){
		this.evalClass = evalClass;
	}
	
	public NonDeterministicModeSwitchingPlugin startAt(long startTime) {
		this.startTime = startTime;
		return this;
	}
	
	public NonDeterministicModeSwitchingPlugin withEvalPeriod(long evalPeriod) {
		this.evalPeriod = evalPeriod;
		return this;
	}
	
	public NonDeterministicModeSwitchingPlugin withReconfigPeriod(long reconfPeriod) {
		this.reconfPeriod = reconfPeriod;
		return this;
	}
	
	public NonDeterministicModeSwitchingPlugin withStartingNondetermoinism(double nondeterminism) {
		this.startingNondeterminism = nondeterminism;
		return this;
	}
	
	public NonDeterministicModeSwitchingPlugin withVerbosity(boolean verbosity){
		verbose = verbosity;
		return this;
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
		AnnotationProcessorExtensionPoint nonDetModeAwareAnnotationProcessor = new NonDetModeSwitchAwareAnnotationProcessorExtension();
		container.getProcessor().addExtension(nonDetModeAwareAnnotationProcessor);
		
		try {
			final NonDeterministicModeSwitchingManager manager =
					new NonDeterministicModeSwitchingManager(startTime,
							startingNondeterminism, evalClass);
			NonDeterministicModeSwitchingManager.verbose = verbose;
			
			container.deployComponent(manager);
		} catch (AnnotationProcessorException e) {
			Log.e("Error while trying to deploy AdaptationManager", e);
		}
		
		for (ComponentInstance c : container.getRuntimeMetadata().getComponentInstances()) {
			if (c.getName().equals(NonDeterministicModeSwitchingManager.class.getName())) {

				for (ComponentProcess p: c.getComponentProcesses()) {
					Long period = null;
					if(p.getName().equals("evaluate")){
						period = evalPeriod;
					}
					if(p.getName().equals("reason")){
						period = reconfPeriod;
					}
					
					if(period != null){
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

}
