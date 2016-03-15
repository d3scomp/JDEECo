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

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorExtensionPoint;
import cz.cuni.mff.d3s.deeco.annotations.processor.NonDetModeSwitchAwareAnnotationProcessorExtension;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.modes.DEECoMode;
import cz.cuni.mff.d3s.deeco.modes.ModeChart;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer.StartupListener;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.runtime.PluginInitFailedException;
import cz.cuni.mff.d3s.deeco.runtime.PluginStartupFailedException;
import cz.cuni.mff.d3s.deeco.search.StateSpaceSearch;
import cz.cuni.mff.d3s.jdeeco.adaptation.AdaptationPlugin;
import cz.cuni.mff.d3s.jdeeco.modes.ModeSwitchingPlugin;

/**
 * Non-Deterministic Mode Switching plugin deploys a adaptation strategy,
 * that handles non-deterministic mode switching.
 * 
 * <p>The non-deterministic component needs to be deployed at each node
 * that employs this strategy. Therefor deploy this plugin to all desired
 * DEECo nodes.</p>
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public class NonDeterministicModeSwitchingPlugin implements DEECoPlugin, StartupListener {

	private long startTime = 0;
	private Class<? extends NonDetModeSwitchFitnessEval> evalClass = null;
	private double startingNondeterminism = 0.0001;
	private DEECoContainer container = null;
	AdaptationPlugin adaptationPlugin= null;
	
	private boolean verbose = false;
	
	/** Plugin dependencies. */
	@SuppressWarnings("unchecked")
	static private final List<Class<? extends DEECoPlugin>> DEPENDENCIES =
			Arrays.asList(new Class[]{
					AdaptationPlugin.class,
					ModeSwitchingPlugin.class});
	
	public NonDeterministicModeSwitchingPlugin(Class<? extends NonDetModeSwitchFitnessEval> evalClass){
		this.evalClass = evalClass;
	}
	
	public NonDeterministicModeSwitchingPlugin startAt(long startTime) {
		this.startTime = startTime;
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
		this.container = container;
				
		AnnotationProcessorExtensionPoint nonDetModeAwareAnnotationProcessor = new NonDetModeSwitchAwareAnnotationProcessorExtension();
		container.getProcessor().addExtension(nonDetModeAwareAnnotationProcessor);
		
		container.addStartupListener(this);
		
		adaptationPlugin = container.getPluginInstance(AdaptationPlugin.class);
		
		NonDeterministicModeSwitchingManager.startingNondeterminism = startingNondeterminism;
		NonDeterministicModeSwitchingManager.verbose = verbose;
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.runtime.DEECoContainer.StartupListener#onStartup()
	 */
	@Override
	public void onStartup() throws PluginStartupFailedException {
		if(container == null){
			throw new PluginStartupFailedException(String.format(
					"The %s plugin doesn't have a reference to %s.",
					"NonDeterministicModeSwitching",
					"DEECo container"));
		}
		if(adaptationPlugin == null){
			throw new PluginStartupFailedException(String.format(
					"The %s plugin doesn't have a reference to %s.",
					"NonDeterministicModeSwitching",
					"AdaptationPlugin"));
		}
		
		
		// Components with non-deterministic mode switching aspiration
		for (ComponentInstance c : container.getRuntimeMetadata().getComponentInstances()) {
			ModeChart modeChart = c.getModeChart();
			if (modeChart != null) {
				StateSpaceSearch sss = modeChart.getStateSpaceSearch();
				if(sss != null)	{
					// Check if modes are of correct type
					for(Class<? extends DEECoMode> mode : modeChart.getModes()){
						if(!NonDetModeSwitchMode.class.isAssignableFrom(mode)){
							throw new PluginStartupFailedException(String.format(
									"The %s mode doesn't extend %s class.",
									mode, NonDetModeSwitchMode.class));
						}
					}

					// Create non-deterministic mode switching manager or thecomponent
					NonDeterministicModeSwitchingManager manager;
					try {
						manager = new NonDeterministicModeSwitchingManager(startTime,
								evalClass, c);
					} catch (InstantiationException | IllegalAccessException e) {
						throw new PluginStartupFailedException(e);
					}	
					adaptationPlugin.registerAdaptation(manager);
				}
			}
		}
	}

}
