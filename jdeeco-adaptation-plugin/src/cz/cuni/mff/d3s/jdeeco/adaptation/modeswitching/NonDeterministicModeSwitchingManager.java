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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.SystemComponent;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.modes.DEECoMode;
import cz.cuni.mff.d3s.deeco.modes.ModeChart;
import cz.cuni.mff.d3s.deeco.search.StateSpaceSearch;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.deeco.task.ProcessContext;
import cz.cuni.mff.d3s.jdeeco.modes.ModeChartImpl;
import cz.cuni.mff.d3s.jdeeco.modes.ModeSuccessor;
import cz.cuni.mff.d3s.jdeeco.modes.TrueGuard;
import cz.cuni.mff.d3s.jdeeco.modes.runtimelog.ModeTransitionLogger;

/**
 * Adapts the annotated components in the same DEECo node by adding
 * non-deterministic mode transitions.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
@Component
@SystemComponent
public class NonDeterministicModeSwitchingManager {

	/**
	 * Mandatory knowledge field - id of the component.
	 */
	public String id = "NonDeterministicModeSwitchingManager";
	
	public NonDetModeSwitchAnnealStateSpace stateSpace;
	
	public static final double DEFAULT_ENERGY = 1;
	
	public final long startTime;
		
	public double currentNonDeterminismLevel;
	
	public Map<ComponentInstance, NonDetModeSwitchEval> evaluators;

	public Class<? extends NonDetModeSwitchEval> evalClass;
	
	public Map<Double, NonDetModeSwitchPerformance> energies;
	
	
	
	public NonDeterministicModeSwitchingManager(long startTime) {
		this.startTime = startTime;
		
		currentNonDeterminismLevel = NonDetModeSwitchAnnealStateSpace.startingNondeterminism;
		stateSpace = new NonDetModeSwitchAnnealStateSpace();
	}
	
	@Process
	@PeriodicScheduling(period = 100) // TODO: parametrize
	public static void evaluate(
			@In("currentNonDeterminismLevel") double currentNonDeterminismLevel,
			@InOut("energies") ParamHolder<Map<Double, NonDetModeSwitchPerformance>> energies,
			@InOut("evaluators") ParamHolder<Map<ComponentInstance, NonDetModeSwitchEval>> evaluators,
			@In("evalClass") Class<? extends NonDetModeSwitchEval> evalClass,
			@InOut("evaluators") ParamHolder<NonDetModeSwitchAnnealStateSpace> stateSpace)
					throws InstantiationException, IllegalAccessException{
		
		ComponentInstance component = ProcessContext.getCurrentProcess().getComponentInstance();
		RuntimeMetadata runtime = (RuntimeMetadata) component.eContainer();

		for (ComponentInstance c : runtime.getComponentInstances()) {
			ModeChart modeChart = c.getModeChart();
			if (modeChart != null) {
				StateSpaceSearch sss = modeChart.getStateSpaceSearch();
				if(sss != null)	{
					if(!evaluators.value.containsKey(c)){
						evaluators.value.put(c, evalClass.newInstance());
					}
					
					NonDetModeSwitchEval evaluator = evaluators.value.get(c);
					String[] knowledge = evaluator.getKnowledgeNames();
					Object[] values = getValues(c, knowledge);
					long currentTime = ProcessContext.getTimeProvider().getCurrentMilliseconds();
					NonDetModeSwitchPerformance energy = evaluator.getEnergy(currentTime, values);
					
					if(energies.value.containsKey(currentNonDeterminismLevel)){
						NonDetModeSwitchPerformance p = energy.combineEnergies(
								energies.value.get(currentNonDeterminismLevel));
						energies.value.put(currentNonDeterminismLevel, p);
					} else {
						energies.value.put(currentNonDeterminismLevel, energy);
					}
					
					stateSpace.value.getState(currentNonDeterminismLevel).setEnergy(
							energies.value.get(currentNonDeterminismLevel).getEnergy());
				}
			}
		}
	}
	
	/**
	 * Configure the probabilities of non-deterministic mode transitions.
	 * The period of this process can be set via the 
	 * {@link NonDeterministicModeSwitchingPlugin}.
	 * @param id
	 */
	@Process
	@PeriodicScheduling(period = 100) // TODO: parametrize
	public static void reason(@In("id") String id,
			@In("startTime") long startTime,
			@InOut("stateSpace") ParamHolder<NonDetModeSwitchAnnealStateSpace> stateSpace,
			@InOut("currentNonDeterminismLevel") ParamHolder<Double> currentNonDeterminismLevel) {
		
		// Don't add non-determinism until the start time
		if(ProcessContext.getTimeProvider().getCurrentMilliseconds() < startTime){
			return;
		}
		
		ComponentInstance component = ProcessContext.getCurrentProcess().getComponentInstance();
		RuntimeMetadata runtime = (RuntimeMetadata) component.eContainer();

		for (ComponentInstance c : runtime.getComponentInstances()) {
			ModeChart modeChart = c.getModeChart();
			if (modeChart != null) {
				StateSpaceSearch sss = modeChart.getStateSpaceSearch();
				if(sss != null)	{
					if(!((ModeChartImpl) modeChart).isModified()){
						// Modify the mode chart if not yet modified
						addNondeterministicTransitions((ModeChartImpl) modeChart);
					}
					
					// Get the next state
					NonDetModeSwitchAnnealState nextState =
							(NonDetModeSwitchAnnealState)
							sss.getNextState(stateSpace.value.getState(
									currentNonDeterminismLevel.value));
					
					currentNonDeterminismLevel.value = nextState.getNondeterminism();
					
					reconfigureModeChart((ModeChartImpl)modeChart, currentNonDeterminismLevel.value);
				}
			}
		}
	}
	
	private static void reconfigureModeChart(ModeChartImpl modeChart, double nondeterminism){
		
		for(Class<? extends DEECoMode> from : modeChart.getModes())
		{
			Set<ModeSuccessor> dynamicTransitions = new HashSet<>();
			Set<ModeSuccessor> staticTransitions = new HashSet<>();
			if(!modeChart.modes.containsKey(from)){
				continue;
			}
			
			// Sort out dynamic and static transitions
			for(ModeSuccessor transition : modeChart.modes.get(from)){
				if(transition.isDynamic()){
					dynamicTransitions.add(transition);
				} else {
					staticTransitions.add(transition);
				}
			}
			
			// Set probability to dynamic transitions
			for(ModeSuccessor s : dynamicTransitions){
				// +1 for one static transition (suppose they are exclusive
				s.setProbability(nondeterminism / (dynamicTransitions.size() + 1));
			}

			// Suppose all static transitions are exclusive
			for(ModeSuccessor s : staticTransitions){
				s.setProbability(1 - nondeterminism);
			}
		}	
	}
	
	private static void addNondeterministicTransitions(ModeChartImpl modeChart){
		// Make full graph
		// TODO: exclude nodes prohibited for non-deterministic switching
		for(Class<? extends DEECoMode> from : modeChart.getModes())
		{
			// Identify missing transitions
			Set<Class<? extends DEECoMode>> allModes = modeChart.getModes();
			Set<Class<? extends DEECoMode>> missingModes = new HashSet<>(allModes);
			missingModes.removeAll(modeChart.getSuccessors(from));
			
			// Add missing transitions
			for(Class<? extends DEECoMode> to : missingModes){
				if (!modeChart.modes.containsKey(from)) {
					modeChart.modes.put(from, new HashSet<>());
				} else {
					if (modeChart.modes.get(from).contains(to)) {
						throw new IllegalStateException(
								String.format("Transition \"%s\" -> \"%s\" already defined.",
								from, to));
					}
				}
				
				// Add transition with 0 probability mark it as dynamic
				ModeSuccessor successor = new ModeSuccessor(to, 0, new TrueGuard());
				successor.setDynamic(true);
				modeChart.modes.get(from).add(successor);
				
				// Add transition listener
				modeChart.addTransitionListener(from, to, new ModeTransitionLogger(from, to));
			}
		}

		modeChart.wasModified();	
	}
	
	private static Object[] getValues(ComponentInstance component, String[] knowledgeNames) {
		List<KnowledgePath> paths = new ArrayList<>();
		for(String knowledgeName : knowledgeNames){
			KnowledgePath path = RuntimeMetadataFactoryExt.eINSTANCE.createKnowledgePath();
			PathNodeField pNode = RuntimeMetadataFactoryExt.eINSTANCE.createPathNodeField();
			pNode.setName(knowledgeName);
			path.getNodes().add(pNode);
			paths.add(path);
		}
		try {
			ValueSet vSet = component.getKnowledgeManager().get(paths);
			Object[] values = new Object[knowledgeNames.length];
			for (int i = 0; i < knowledgeNames.length; i++) {
				values[i] = vSet.getValue(paths.get(i));
			}
			return values;
		} catch (KnowledgeNotFoundException e) {
			Log.e("Couldn't find knowledge " + knowledgeNames + " in component " + component);
			return null;
		}
	}
	
}
