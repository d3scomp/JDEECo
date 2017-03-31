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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.modes.DEECoMode;
import cz.cuni.mff.d3s.deeco.modes.DEECoModeChart;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer.StartupListener;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.runtime.PluginStartupFailedException;
import cz.cuni.mff.d3s.jdeeco.adaptation.AdaptationPlugin;
import cz.cuni.mff.d3s.jdeeco.adaptation.AdaptationUtility;
import cz.cuni.mff.d3s.jdeeco.adaptation.componentIsolation.ComponentIsolationPlugin;
import cz.cuni.mff.d3s.metaadaptation.modeswitch.NonDeterministicModeSwitchingManager;
import cz.cuni.mff.d3s.metaadaptation.modeswitchprops.Component;
import cz.cuni.mff.d3s.metaadaptation.modeswitchprops.ModeSwitchPropsManager;

/**
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class ModeSwitchPropsPlugin implements DEECoPlugin, StartupListener {

	private boolean verbose;
	
	private final Set<DEECoNode> nodes;
	
	private final Map<Class<?>, AdaptationUtility> utilities;

	private AdaptationPlugin adaptationPlugin = null;
	
	/** Plugin dependencies. */
	@SuppressWarnings("unchecked")
	static private final List<Class<? extends DEECoPlugin>> DEPENDENCIES =
			Arrays.asList(new Class[]{AdaptationPlugin.class});;


	public ModeSwitchPropsPlugin(Set<DEECoNode> nodes, Map<Class<?>, AdaptationUtility> utilities){
		if(nodes == null){
			throw new IllegalArgumentException(String.format("The %s argument is null.", "nodes"));
		}
		
		verbose = false;
		
		this.nodes = nodes;
		this.utilities = utilities;
	}
		
	/**
	 * Specify the verbosity of the correlation process.
	 * @param verbose True to be verbose, false to be still.
	 * @return The self instance of {@link ComponentIsolationPlugin} 
	 */
	public ModeSwitchPropsPlugin withVerbosity(boolean verbose){
		this.verbose = verbose;
		return this;
	}

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return DEPENDENCIES;
	}

	@Override
	public void init(DEECoContainer container) {
		
		container.addStartupListener(this);
		
		adaptationPlugin = container.getPluginInstance(AdaptationPlugin.class);
	}

	@Override
	public void onStartup() throws PluginStartupFailedException {
		if(adaptationPlugin == null){
			throw new PluginStartupFailedException(String.format(
					"The %s plugin doesn't have a reference to %s.",
					"NonDeterministicModeSwitching",
					"AdaptationPlugin"));
		}
		
		for(DEECoNode node : nodes){
			for(ComponentInstance ci : node.getRuntimeMetadata().getComponentInstances()){
				DEECoModeChart modeChart = ci.getModeChart();
				if (modeChart != null) {
					// Check the required utility was placed
					AdaptationUtility utility = null;
					for(Class<?> key : utilities.keySet()){
						if(key.getName().equals(ci.getName())){
							utility = utilities.get(key);
						}
					}
					
					if(utility == null){
						throw new PluginStartupFailedException(String.format(
								"The %s component has no associated utility function.",
								ci.getKnowledgeManager().getId()));
					}
				
					Component c = new ComponentImpl(ci, utility);
					cz.cuni.mff.d3s.metaadaptation.modeswitchprops.ModeChart m = 
							new ModeChartImpl((cz.cuni.mff.d3s.jdeeco.modes.ModeChartImpl) modeChart);
					adaptationPlugin.registerAdaptation(new ModeSwitchPropsManager(c, m));
					
					if(verbose){
						Log.i(String.format("%s deploid for %s %s",
								getClass().getName(),
								ci.getName(), ci.getKnowledgeManager().getId()));
					}
				}
			}
		}		
	}
}