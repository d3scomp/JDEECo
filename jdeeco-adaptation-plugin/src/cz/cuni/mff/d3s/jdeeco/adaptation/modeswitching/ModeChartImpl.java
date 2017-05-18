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
package cz.cuni.mff.d3s.jdeeco.adaptation.modeswitching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.modes.DEECoMode;
import cz.cuni.mff.d3s.deeco.modes.DEECoModeGuard;
import cz.cuni.mff.d3s.deeco.modes.DEECoTransition;
import cz.cuni.mff.d3s.jdeeco.adaptation.modeswitching.runtimelog.NonDetModeTransitionLogger;
import cz.cuni.mff.d3s.metaadaptation.modeswitch.Mode;
import cz.cuni.mff.d3s.metaadaptation.modeswitch.ModeChart;
import cz.cuni.mff.d3s.metaadaptation.modeswitch.Transition;

/**
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class ModeChartImpl implements ModeChart {

	private final cz.cuni.mff.d3s.deeco.modes.DEECoModeChart modeChart;
	
	private final Map<DEECoMode, ModeImpl> modes;
	
	private final Set<Transition> transitions;
	
	private final ComponentInstance component;

	
	public ModeChartImpl(ComponentInstance component){
		if(component == null){
			throw new IllegalArgumentException(String.format("The %s argument is null.", "component"));
		}
		
		this.component = component;
		modeChart = component.getModeChart();
		if(modeChart == null){
			throw new IllegalArgumentException(String.format("Component %s has no mode chart.", component));
		}
		modes = new HashMap<>();
		transitions = new HashSet<>();
		
		init();
	}
	
	private void init(){
		// Create modes
		for(DEECoMode m : modeChart.getModes()){
			ModeImpl mode = new ModeImpl(m);
			modes.put(m, mode);
		}
		
		// Create transitions
		for(DEECoTransition transition : modeChart.getTransitions()){
			//GuardImpl guard = new GuardImpl(transition.getGuard(), component);
			TransitionImpl transitionImpl =  new TransitionImpl(
					modes.get(transition.getFrom()),
					modes.get(transition.getTo()),
					transition, component);
			transitions.add(transitionImpl);
		}
	}
	
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.modeswitch.ModeChart#getModes()
	 */
	@Override
	public Set<Mode> getModes() {
		return new HashSet<>(modes.values());
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.modeswitch.ModeChart#getCurrentMode()
	 */
	@Override
	public Mode getCurrentMode() {
		return modes.get(modeChart.getCurrentMode());
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.modeswitch.ModeChart#getTransitions()
	 */
	@Override
	public Set<Transition> getTransitions() {
		return transitions;
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.modeswitch.ModeChart#addTransition(cz.cuni.mff.d3s.metaadaptation.modeswitch.Transition)
	 */
	@Override
	public Transition addTransition(Mode from, Mode to, Predicate<Void> guard) {
		DEECoMode DFrom = ((ModeImpl)from).getInnerMode();
		DEECoMode DTo = ((ModeImpl)to).getInnerMode();
		
		cz.cuni.mff.d3s.jdeeco.modes.Transition transition = new cz.cuni.mff.d3s.jdeeco.modes.Transition(
				DFrom, DTo, new DEECoModeGuard(){
			@Override
			protected void specifyParameters() {
				// Nothing to do here				
			}

			@Override
			public String[] getKnowledgeNames() {
				return new String[]{"id"};
			}

			@Override
			public boolean isSatisfied(Object[] knowledgeValues) {
				return guard.test(null);
			}});
		
		TransitionImpl transitionImpl = new TransitionImpl((ModeImpl) from, 
				(ModeImpl) to, transition, component);
		
		// Add the transition
		transitions.add(transitionImpl);
		modeChart.getTransitions().add(transition);
		
		// Add transition listener
		if(!modeChart.getTransitionListeners().containsKey(transition)){
			modeChart.getTransitionListeners().put(transition, new ArrayList<>());
		}
		modeChart.getTransitionListeners().get(transition).add(new NonDetModeTransitionLogger(DFrom, DTo));
		
		return transitionImpl;
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.modeswitch.ModeChart#removeTransition(cz.cuni.mff.d3s.metaadaptation.modeswitch.Transition)
	 */
	@Override
	public void removeTransition(Transition transition) {
		cz.cuni.mff.d3s.deeco.modes.DEECoTransition DTransition = ((TransitionImpl)transition).getInnerTransition();
				
		transitions.remove(transition);
		modeChart.getTransitions().remove(DTransition);
		if(modeChart.getTransitionListeners().containsKey(transition)){
			modeChart.getTransitionListeners().remove(transition);
		}
	}

}
