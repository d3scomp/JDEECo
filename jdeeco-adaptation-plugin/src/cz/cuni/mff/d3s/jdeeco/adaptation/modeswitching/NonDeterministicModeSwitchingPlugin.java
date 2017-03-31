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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorExtensionPoint;
import cz.cuni.mff.d3s.deeco.annotations.processor.NonDetModeSwitchAwareAnnotationProcessorExtension;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.modes.DEECoMode;
import cz.cuni.mff.d3s.deeco.modes.DEECoModeChart;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer.ShutdownListener;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer.StartupListener;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.runtime.PluginInitFailedException;
import cz.cuni.mff.d3s.deeco.runtime.PluginStartupFailedException;
import cz.cuni.mff.d3s.jdeeco.adaptation.AdaptationPlugin;
import cz.cuni.mff.d3s.jdeeco.adaptation.AdaptationUtility;
import cz.cuni.mff.d3s.jdeeco.modes.ModeSwitchingPlugin;
import cz.cuni.mff.d3s.metaadaptation.modeswitch.Component;
import cz.cuni.mff.d3s.metaadaptation.modeswitch.Mode;
import cz.cuni.mff.d3s.metaadaptation.modeswitch.NonDeterministicModeSwitchingManager;
import cz.cuni.mff.d3s.metaadaptation.modeswitch.Transition;

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
public class NonDeterministicModeSwitchingPlugin implements DEECoPlugin, StartupListener, ShutdownListener {

//	private static Map<ComponentInstance, StateSpaceSearch> sssMap = new HashMap<>();
//	
//	public static void storeStateSpaceSearch(ComponentInstance ci, StateSpaceSearch sss){
//		sssMap.put(ci, sss);
//	}
	
//	private long startTime = 0;
	private final Map<Class<?>, AdaptationUtility> utilities;
	private final Map<String, Double> precomputedUtilities;
//	private double startingNondeterminism = 0.0001;
	private DEECoContainer container = null;
	private AdaptationPlugin adaptationPlugin= null;
	private NonDeterministicModeSwitchingManager manager = null;
//	private final TimeProgress timer;
	
	private boolean verbose = false;
	private double transitionProbability = 0;
	private int transitionPriority = 0;
	private boolean training = false;
	private String trainFrom = null;
	private String trainTo = null;
	private String trainingOutput = null;
	
	
	/** Plugin dependencies. */
	@SuppressWarnings("unchecked")
	static private final List<Class<? extends DEECoPlugin>> DEPENDENCIES =
			Arrays.asList(new Class[]{
					AdaptationPlugin.class,
					ModeSwitchingPlugin.class});
	
	public NonDeterministicModeSwitchingPlugin(Map<Class<?>, AdaptationUtility> utilities, Map<String, Double> precomputedUtilities){
		if(utilities == null){
			throw new IllegalArgumentException(String.format("The %s argument is null.", "utilities"));
		}
//		if(timer == null){
//			throw new IllegalArgumentException(String.format("The %s argument is null.", "timer"));
//		}
		
		this.utilities = utilities;
		this.precomputedUtilities = precomputedUtilities;
//		this.timer = timer;
	}
	
	/*public NonDeterministicModeSwitchingPlugin startAt(long startTime) {
		this.startTime = startTime;
		return this;
	}
	
	public NonDeterministicModeSwitchingPlugin withStartingNondetermoinism(double nondeterminism) {
		this.startingNondeterminism = nondeterminism;
		return this;
	}*/
	
	public NonDeterministicModeSwitchingPlugin withVerbosity(boolean verbosity){
		verbose = verbosity;
		return this;
	}
	public NonDeterministicModeSwitchingPlugin withTransitionProbability(double transitionProbability){
		this.transitionProbability = transitionProbability;
		return this;
	}
	public NonDeterministicModeSwitchingPlugin withTransitionPriority(int transitionPriority){
		this.transitionPriority = transitionPriority;
		return this;
	}
	public NonDeterministicModeSwitchingPlugin withTraining(boolean training){
		this.training = training;
		return this;
	}
	public NonDeterministicModeSwitchingPlugin withTrainFrom(String trainFrom){
		this.trainFrom = trainFrom;
		return this;
	}
	public NonDeterministicModeSwitchingPlugin withTrainTo(String trainTo){
		this.trainTo = trainTo;
		return this;
	}
	public NonDeterministicModeSwitchingPlugin withTrainingOutput(String trainingOutput){
		this.trainingOutput = trainingOutput;
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
		container.addShutdownListener(this);
		
		adaptationPlugin = container.getPluginInstance(AdaptationPlugin.class);
		
//		NonDeterministicModeSwitchingManager.startingNondeterminism = startingNondeterminism;
		NonDeterministicModeSwitchingManager.verbose = verbose;
		NonDeterministicModeSwitchingManager.transitionProbability = transitionProbability;
		NonDeterministicModeSwitchingManager.transitionPriority = transitionPriority;
		NonDeterministicModeSwitchingManager.training = training;
		NonDeterministicModeSwitchingManager.trainingOutput = new File(trainingOutput);
		
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
		
		List<Component> components = new ArrayList<>();
		// Components with non-deterministic mode switching aspiration
		for (ComponentInstance c : container.getRuntimeMetadata().getComponentInstances()) {
			DEECoModeChart modeChart = c.getModeChart();
			if (modeChart != null) {
//				if(sssMap.containsKey(c)){
				if(training){
					for(DEECoMode mode : modeChart.getModes()){
						if(mode.getId().equals(trainFrom)){
							NonDeterministicModeSwitchingManager.trainFrom = new ModeImpl(mode);
						}
						if(mode.getId().equals(trainTo)){
							NonDeterministicModeSwitchingManager.trainTo = new ModeImpl(mode);
						}
//						if(!NonDetModeSwitchMode.class.isAssignableFrom(mode)){
//							throw new PluginStartupFailedException(String.format(
//									"The %s mode doesn't extend %s class.",
//									mode, NonDetModeSwitchMode.class));
//						}
					}
				}

					// Check the required utility was placed
				AdaptationUtility utility = null;
				for(Class<?> key : utilities.keySet()){
					if(key.getName().equals(c.getName())){
						utility = utilities.get(key);
					}
				}
				
				if(utility == null){
					throw new PluginStartupFailedException(String.format(
							"The %s component has no associated utility function.",
							c.getKnowledgeManager().getId()));
				}
				
				ComponentImpl componentImpl = new ComponentImpl(c, new ComponentTypeImpl(utility));
				components.add(componentImpl);
//				}
			}
		}

		if(training && (NonDeterministicModeSwitchingManager.trainFrom == null
				|| NonDeterministicModeSwitchingManager.trainTo == null)){
			throw new PluginStartupFailedException(String.format(
					"The \"%s\" or \"%s\" mode not found.", trainFrom, trainTo));
		}
		
		Map<Transition, Double> transitionAsocUtilities = null;
		if(!components.isEmpty()){
			transitionAsocUtilities = associateUtilities(components.get(0).getModeChart().getModes());
		}
		try {
			manager = new NonDeterministicModeSwitchingManager(components, transitionAsocUtilities);
			adaptationPlugin.registerAdaptation(manager);
		} catch (InstantiationException | IllegalAccessException | FileNotFoundException e) {
			throw new PluginStartupFailedException(e);
		}
	}
	
	private Map<Transition, Double> associateUtilities(Set<Mode> modes){
		if(precomputedUtilities == null){
			return null;
		}
		
		Map<Transition, Double> transitionAsocUtilities = new HashMap<>();
		final Pattern nameValuePattern = Pattern.compile("(\\w+)-(\\w+)");
		
		for(String transitionString : precomputedUtilities.keySet()){
			final Matcher nameValueMatcher = nameValuePattern.matcher(transitionString);
			if((!nameValueMatcher.matches()) ||
					nameValueMatcher.groupCount() != 2){
				String msg = String.format("The \"%s\" string doesn't represent a transition.", transitionString);
				Log.e(msg);
				throw new IllegalStateException(msg);
			}
			final String fromStr = nameValueMatcher.group(1);
			final String toStr = nameValueMatcher.group(2);
			Mode fromMode = null;
			Mode toMode = null;
			for(Mode mode : modes){
				if(mode.toString().equals(fromStr)){
					fromMode = mode;
				}
				if(mode.toString().equals(toStr)){
					toMode = mode;
				}
			}
			if(fromMode == null || toMode == null){
				String msg = String.format("The modes for transition \"%s\" not found.", transitionString);
				Log.e(msg);
				throw new IllegalStateException(msg);
			}
			
			transitionAsocUtilities.put(new PhonyTransition(fromMode, toMode), precomputedUtilities.get(transitionString));
		}
		
		return transitionAsocUtilities;
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.runtime.DEECoContainer.ShutdownListener#onShutdown()
	 */
	@Override
	public void onShutdown() {
		if(manager != null){
			manager.terminate();
		}
		
	}

}
