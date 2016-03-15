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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.modes.DEECoMode;
import cz.cuni.mff.d3s.deeco.modes.ModeChart;
import cz.cuni.mff.d3s.deeco.search.StateSpaceSearch;
import cz.cuni.mff.d3s.deeco.task.ProcessContext;
import cz.cuni.mff.d3s.jdeeco.adaptation.MAPEAdaptation;
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
public class NonDeterministicModeSwitchingManager implements MAPEAdaptation {

	public static final double DEFAULT_ENERGY = 1;

	public static double startingNondeterminism = NonDetModeSwitchAnnealStateSpace.DEFAULT_STARTING_NONDETERMINISM;
	
	static boolean verbose = false;
	
	
	private final ComponentInstance managedComponent;
		
	public NonDetModeSwitchAnnealStateSpace stateSpace;
		
	public final long startTime;

	public Double currentNonDeterminismLevel;
	public Double nextNonDeterminismLevel;
	
	public NonDetModeSwitchFitnessEval evaluator;
	
	public Map<Double, NonDetModeSwitchFitness> energies;
	
	
	public NonDeterministicModeSwitchingManager(long startTime,
			Class<? extends NonDetModeSwitchFitnessEval> evalClass,
			ComponentInstance component)
					throws InstantiationException, IllegalAccessException {
		if(!isValidComponent(component)){
			throw new IllegalArgumentException(String.format(
					"Non-deterministic mode switching cannot be applied "
					+ "to the given component %s, it doesn't specify "
					+ "either mode chart or state space search.",
					component.getClass().getName()));
		}
		
		this.startTime = startTime;
		stateSpace = new NonDetModeSwitchAnnealStateSpace(startingNondeterminism);
		evaluator = evalClass.newInstance();
		managedComponent = component;
		currentNonDeterminismLevel = startingNondeterminism;
		energies = new HashMap<>();
	}
	
	private boolean isValidComponent(ComponentInstance component) {
		ModeChart modeChart = component.getModeChart();
		if (modeChart == null) {
			return false;
		}
		
		StateSpaceSearch sss = modeChart.getStateSpaceSearch();
		return sss != null;
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.jdeeco.adaptation.MAPEAdaptation#monitor()
	 */
	@Override
	public void monitor() {
		long currentTime = ProcessContext.getTimeProvider().getCurrentMilliseconds();
		
		// Measure the current fitness
		String[] knowledge = evaluator.getKnowledgeNames();
		Object[] values = getValues(managedComponent, knowledge);
		NonDetModeSwitchFitness energy = evaluator.getFitness(currentTime, values);
		
		// Combine and store fitness value for the current non-determinism
		if(energies.containsKey(currentNonDeterminismLevel)){
			NonDetModeSwitchFitness p = energy.combineFitness(
					energies.get(currentNonDeterminismLevel));
			energies.put(currentNonDeterminismLevel, p);
		} else {
			energies.put(currentNonDeterminismLevel, energy);
		}
		
		// Assign the fitness to the state
		double energyValue = energies.get(currentNonDeterminismLevel).getFitness(); 
		stateSpace.getState(currentNonDeterminismLevel).setEnergy(energyValue);
		
		if(verbose){
			Log.i(String.format("Non-deterministic mode switching energy for the "
				+ "non-deterministic level %f at %d is %f",
				currentNonDeterminismLevel, currentTime, energyValue));
		}
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.jdeeco.adaptation.MAPEAdaptation#analyze()
	 */
	@Override
	public double analyze() {
		long currentTime = ProcessContext.getTimeProvider().getCurrentMilliseconds();
		
		// Don't add non-determinism until the start time
		if(currentTime < startTime){
			return 0;
		}
		
		// TODO: check whether the search is complete
		return 1;
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.jdeeco.adaptation.MAPEAdaptation#plan()
	 */
	@Override
	public void plan() {
		ModeChart modeChart = managedComponent.getModeChart();
		StateSpaceSearch sss = modeChart.getStateSpaceSearch();			
		
		// Get the next state
		NonDetModeSwitchAnnealState nextState =
				(NonDetModeSwitchAnnealState) sss.getNextState(
						stateSpace.getState(currentNonDeterminismLevel));
		
		nextNonDeterminismLevel = nextState.getNondeterminism();		
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.jdeeco.adaptation.MAPEAdaptation#execute()
	 */
	@Override
	public void execute() {
		ModeChart modeChart = managedComponent.getModeChart();
		if(!((ModeChartImpl) modeChart).isModified()){
			// Modify the mode chart if not yet modified
			addNondeterministicTransitions((ModeChartImpl) modeChart);
		}

		reconfigureModeChart((ModeChartImpl)modeChart, nextNonDeterminismLevel);
		currentNonDeterminismLevel = nextNonDeterminismLevel;

		if(verbose) {
			long currentTime = ProcessContext.getTimeProvider().getCurrentMilliseconds();
			Object knowledge[] = getValues(managedComponent, new String[]{"id"});
			Log.i(String.format("Non-deterministic mode switching the "
				+ "non-deterministic level of component %s set to %f at %d",
				knowledge[0], currentNonDeterminismLevel, currentTime));
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
