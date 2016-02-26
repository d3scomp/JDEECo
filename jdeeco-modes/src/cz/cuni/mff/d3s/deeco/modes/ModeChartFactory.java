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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ModeChartFactory {

	private ModeChartImpl modeChart;

	public ModeChartFactory() {
		modeChart = new ModeChartImpl();
	}

	public ModeChartFactory addTransitionWithGuard(
			Class<? extends DEECoMode> from, Class<? extends DEECoMode> to,
			ModeGuard guard) {
		if (from == null) throw new IllegalArgumentException(
				String.format("The \"%s\" argument is null.", "from"));
		if (to == null) throw new IllegalArgumentException(
				String.format("The \"%s\" argument is null.", "to"));
		if (guard == null) throw new IllegalArgumentException(
				String.format("The \"%s\" argument is null.", "guard"));

		if (!modeChart.modes.containsKey(from)) {
			modeChart.modes.put(from, new HashSet<>());
		} else {
			if (modeChart.modes.get(from).contains(to)) {
				throw new IllegalStateException(
						String.format("Transition \"%s\" -> \"%s\" already defined.",
								from, to));
			}
		}
		modeChart.modes.get(from).add(new ModeSuccessor(to, 1, guard));

		return this;
	}

	public ModeChartFactory addTransitionWithProbability(
			Class<? extends DEECoMode> from, Class<? extends DEECoMode> to,
			double probability) {
		if (from == null) throw new IllegalArgumentException(
				String.format("The \"%s\" argument is null.", "from"));
		if (to == null) throw new IllegalArgumentException(
				String.format("The \"%s\" argument is null.", "to"));
		if(probability < 0 || probability > 1) throw new IllegalArgumentException(
				String.format("The \"%s\" argument has to be within the [0, 1] interval.",
						"probability"));
			
		if (!modeChart.modes.containsKey(from)) {
			modeChart.modes.put(from, new HashSet<>());
		} else {
			if (modeChart.modes.get(from).contains(to)) {
				throw new IllegalStateException(
						String.format("Transition \"%s\" -> \"%s\" already defined.",
								from, to));
			}
		}
		modeChart.modes.get(from).add(new ModeSuccessor(to, probability, new TrueGuard()));

		return this;
	}

	public ModeChartFactory addTransition(
			Class<? extends DEECoMode> from, Class<? extends DEECoMode> to,
			ModeGuard guard, double probability) {
		if (from == null) throw new IllegalArgumentException(
				String.format("The \"%s\" argument is null.", "from"));
		if (to == null) throw new IllegalArgumentException(
				String.format("The \"%s\" argument is null.", "to"));
		if (guard == null) throw new IllegalArgumentException(
				String.format("The \"%s\" argument is null.", "guard"));
		if(probability < 0 || probability > 1) throw new IllegalArgumentException(
				String.format("The \"%s\" argument has to be within the [0, 1] interval.",
						"probability"));
			
		if (!modeChart.modes.containsKey(from)) {
			modeChart.modes.put(from, new HashSet<>());
		} else {
			if (modeChart.modes.get(from).contains(to)) {
				throw new IllegalStateException(
						String.format("Transition \"%s\" -> \"%s\" already defined.",
								from, to));
			}
		}
		modeChart.modes.get(from).add(new ModeSuccessor(to, probability, guard));

		return this;
	}
	
	public ModeChartFactory addTransitionListener(
			Class<? extends DEECoMode> from, Class<? extends DEECoMode> to,
			ModeTransitionListener transitionListener){
		if (from == null) throw new IllegalArgumentException(
				String.format("The \"%s\" argument is null.", "from"));
		if (to == null) throw new IllegalArgumentException(
				String.format("The \"%s\" argument is null.", "to"));
		if (transitionListener == null) throw new IllegalArgumentException(
				String.format("The \"%s\" argument is null.", "transitionListener"));
		
		if(!modeChart.transitionListeners.containsKey(from)){
			modeChart.transitionListeners.put(from, new HashMap<>());
		}
		Map<Class<? extends DEECoMode>, List<ModeTransitionListener>> fromTransitions = modeChart.transitionListeners.get(from);
		if(!fromTransitions.containsKey(to)){
			fromTransitions.put(to, new ArrayList<>());
		}
		List<ModeTransitionListener> transition = fromTransitions.get(to);
		transition.add(transitionListener);
		
		return this;
	}
	

	public ModeChartFactory addInitialMode(Class<? extends DEECoMode> mode){
		modeChart.setInitialNode(mode);
		return this;
	}

	public ModeChartImpl create(){
		// chceck probabilities sum, check initial mode, check graph consistency
		if(modeChart.getInitialMode() == null) throw new IllegalStateException(
				"The initial state has not been set.");
		checkChartConnected();
		
		return modeChart;
	}
	
	private void checkChartConnected(){
		Set<Class<? extends DEECoMode>> reachableModes = new HashSet<>();
		Set<Class<? extends DEECoMode>> oldReachableModes = new HashSet<>();
		
		reachableModes.add(modeChart.getInitialMode());
		while(!oldReachableModes.containsAll(reachableModes)){
			oldReachableModes.addAll(reachableModes);
			for(Object current : reachableModes.toArray()){
				@SuppressWarnings("unchecked")
				Class<? extends DEECoMode> currentMode = (Class<? extends DEECoMode>) current;
				if(modeChart.modes.containsKey(currentMode)){
					for(ModeSuccessor succMode : modeChart.modes.get(currentMode)){
						reachableModes.add(succMode.successor);
					}
				}
			}
		}
		
		if(!reachableModes.containsAll(modeChart.modes.keySet())){
			Set<Class<? extends DEECoMode>> unreachableModes = new HashSet<>();
			unreachableModes.addAll(modeChart.modes.keySet());
			unreachableModes.removeAll(reachableModes);
			
			StringBuilder builder = new StringBuilder();
			for(Class<? extends DEECoMode> unconnected : unreachableModes){
				builder.append("\n").append(unconnected);
			}
			throw new IllegalStateException(String.format(
					"The following states are unreachable from the initial state: %s",
					builder.toString()));
		}
	}
	
	
}
