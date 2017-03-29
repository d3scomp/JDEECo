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
package cz.cuni.mff.d3s.jdeeco.modes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.modes.DEECoMode;
import cz.cuni.mff.d3s.deeco.modes.DEECoTransition;
import cz.cuni.mff.d3s.deeco.modes.ModeGuard;
import cz.cuni.mff.d3s.deeco.modes.ModeTransitionListener;

public abstract class ModeChartHolder {
	
	private Set<DEECoMode> modes;
	private Set<DEECoTransition> transitions;
	private Map<DEECoTransition, List<ModeTransitionListener>> transitionListeners;
	
	public ModeChartHolder() {
		modes = new HashSet<>();
		transitions = new HashSet<>();
		transitionListeners = new HashMap<>();
	}
	
	public abstract DEECoMode getInitialMode();
	
	public Set<DEECoMode> getModes(){
		return modes;
	}
	
	public Set<DEECoTransition> getTransitions(){
		return transitions;
	}
	
	public Map<DEECoTransition, List<ModeTransitionListener>> getTransitionListeners(){
		return transitionListeners;
	}
	
	private void addMode(DEECoMode mode){
		if (mode == null) throw new IllegalArgumentException(
				String.format("The \"%s\" argument is null.", "mode"));
		
		if(!modes.contains(mode)){
			modes.add(mode);
		}
	}
	
	public Transition addTransition(DEECoMode from, DEECoMode to, ModeGuard guard) {
		if (from == null) throw new IllegalArgumentException(
				String.format("The \"%s\" argument is null.", "from"));
		if (to == null) throw new IllegalArgumentException(
				String.format("The \"%s\" argument is null.", "to"));
		if (guard == null) throw new IllegalArgumentException(
				String.format("The \"%s\" argument is null.", "guard"));

		addMode(from);
		addMode(to);
		
		Transition transition = new Transition(from, to, guard);
		if(transitions.contains(transition)){
			throw new IllegalStateException(
					String.format("Transition \"%s\" -> \"%s\" already defined.",
							from, to));
		}
		transitions.add(transition);

		return transition;
	}

//	public ModeChartFactory addTransitionWithProbability(
//			Class<? extends DEECoMode> from, Class<? extends DEECoMode> to,
//			double probability) {
//		if (from == null) throw new IllegalArgumentException(
//				String.format("The \"%s\" argument is null.", "from"));
//		if (to == null) throw new IllegalArgumentException(
//				String.format("The \"%s\" argument is null.", "to"));
//		if(probability < 0 || probability > 1) throw new IllegalArgumentException(
//				String.format("The \"%s\" argument has to be within the [0, 1] interval.",
//						"probability"));
//			
//		if (!modeChart.modes.containsKey(from)) {
//			modeChart.modes.put(from, new HashSet<>());
//		} else {
//			if (modeChart.modes.get(from).contains(to)) {
//				throw new IllegalStateException(
//						String.format("Transition \"%s\" -> \"%s\" already defined.",
//								from, to));
//			}
//		}
//		modeChart.modes.get(from).add(new ModeSuccessor(to, probability, new TrueGuard()));
//
//		return this;
//	}

//	public ModeChartFactory addTransition(
//			Class<? extends DEECoMode> from, Class<? extends DEECoMode> to,
//			ModeGuard guard, double probability) {
//		if (from == null) throw new IllegalArgumentException(
//				String.format("The \"%s\" argument is null.", "from"));
//		if (to == null) throw new IllegalArgumentException(
//				String.format("The \"%s\" argument is null.", "to"));
//		if (guard == null) throw new IllegalArgumentException(
//				String.format("The \"%s\" argument is null.", "guard"));
//		if(probability < 0 || probability > 1) throw new IllegalArgumentException(
//				String.format("The \"%s\" argument has to be within the [0, 1] interval.",
//						"probability"));
//			
//		if (!modeChart.modes.containsKey(from)) {
//			modeChart.modes.put(from, new HashSet<>());
//		} else {
//			if (modeChart.modes.get(from).contains(to)) {
//				throw new IllegalStateException(
//						String.format("Transition \"%s\" -> \"%s\" already defined.",
//								from, to));
//			}
//		}
//		modeChart.modes.get(from).add(new ModeSuccessor(to, probability, guard));
//
//		return this;
//	}
	
	public void addTransitionListener(Transition transition,
			ModeTransitionListener transitionListener){
		if (transition == null) throw new IllegalArgumentException(
				String.format("The \"%s\" argument is null.", "transition"));
		if (transitionListener == null) throw new IllegalArgumentException(
				String.format("The \"%s\" argument is null.", "transitionListener"));
		if(!transitions.contains(transition)) throw new IllegalStateException(
					String.format("The \"%s\" transition is undefined.", transition));
		
		if(!transitionListeners.containsKey(transition)){
			transitionListeners.put(transition, new ArrayList<>());
		}
		transitionListeners.get(transition).add(transitionListener);
	}
	

//	public ModeChartFactory addInitialMode(Class<? extends DEECoMode> mode){
//		modeChart.setInitialNode(mode);
//		return this;
//	}

	public void validate(){
		if(getInitialMode() == null) throw new IllegalStateException(
				"The initial state has not been set.");
		checkChartConnected();
	}
	
	private void checkChartConnected(){
		Set<DEECoMode> reachableModes = new HashSet<>();
		Set<DEECoMode> oldReachableModes = new HashSet<>();
		
		// Accumulate reachable modes
		reachableModes.add(getInitialMode());
		while(!oldReachableModes.containsAll(reachableModes)){
			oldReachableModes.addAll(reachableModes);
			for(DEECoTransition transition : transitions){
				if(reachableModes.contains(transition.getFrom())){
					reachableModes.add(transition.getTo());
				}
			}
		}
		
		// Check all reachable modes was specified
		for(DEECoMode mode : reachableModes){
			if(!modes.contains(mode)){
				throw new IllegalStateException(String.format(
						"The state %s not found.", mode));
			}
		}
		
		// Check all modes all reachable
		if(!reachableModes.containsAll(modes)){
			Set<DEECoMode> unreachableModes = new HashSet<>();
			unreachableModes.addAll(modes);
			unreachableModes.removeAll(reachableModes);
			
			StringBuilder builder = new StringBuilder();
			for(DEECoMode unconnected : unreachableModes){
				builder.append("\n").append(unconnected);
			}
			throw new IllegalStateException(String.format(
					"The following states are unreachable from the initial state: %s",
					builder.toString()));
		}
	}
}
