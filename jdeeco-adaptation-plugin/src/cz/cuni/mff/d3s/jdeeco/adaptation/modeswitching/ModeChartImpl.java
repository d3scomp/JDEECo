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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.modes.DEECoMode;
import cz.cuni.mff.d3s.deeco.modes.ModeGuard;
import cz.cuni.mff.d3s.jdeeco.adaptation.modeswitching.runtimelog.NonDetModeTransitionLogger;
import cz.cuni.mff.d3s.jdeeco.modes.ModeSuccessor;
import cz.cuni.mff.d3s.metaadaptation.modeswitch.Guard;
import cz.cuni.mff.d3s.metaadaptation.modeswitch.Mode;
import cz.cuni.mff.d3s.metaadaptation.modeswitch.ModeChart;
import cz.cuni.mff.d3s.metaadaptation.modeswitch.Transition;

/**
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class ModeChartImpl implements ModeChart {

	private final cz.cuni.mff.d3s.jdeeco.modes.ModeChartImpl modeChart;
	
	private final Map<Class<? extends DEECoMode>, ModeImpl> modes;
	
	private final Set<Transition> transitions;
	
	private final ComponentInstance component;
		
	public ModeChartImpl(cz.cuni.mff.d3s.jdeeco.modes.ModeChartImpl modeChart, ComponentInstance component){
		if(modeChart == null){
			throw new IllegalArgumentException(String.format("The %s argument is null.", "modeChart"));
		}
		if(component == null){
			throw new IllegalArgumentException(String.format("The %s argument is null.", "component"));
		}
		
		this.modeChart = modeChart;
		this.component = component;
		modes = new HashMap<>();
		transitions = new HashSet<>();
		
		init();
	}
	
	private void init(){		
		for(Class<? extends DEECoMode> m : modeChart.getModes()){
			ModeImpl mode = new ModeImpl(m);
			modes.put(m, mode);
		}
		
		for(Class<? extends DEECoMode> m : modeChart.getModes()){
			Mode fromModeImpl = modes.get(m);
			if(modeChart.modes.containsKey(m)){
				for(ModeSuccessor ms : modeChart.modes.get(m)){
					Class<? extends DEECoMode> succMode = ms.successor;
					ModeImpl succModeImpl;
					if(modes.containsKey(succMode)){
						succModeImpl = modes.get(succMode);
					} else {
						succModeImpl = new ModeImpl(succMode);
						modes.put(succMode, succModeImpl);
					}
					
					GuardImpl guard = new GuardImpl(ms.getGuard(), component);
					TransitionImpl transition = (TransitionImpl) createTransition(fromModeImpl, succModeImpl, guard);
					transitions.add(transition);
				}
			}
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
	public void addTransition(Transition transition) {
		ModeImpl fromImpl = (ModeImpl) transition.getFrom();
		Class<? extends DEECoMode> from = fromImpl.getInnerMode();
		ModeImpl toImpl = (ModeImpl) transition.getTo();
		Class<? extends DEECoMode> to = toImpl.getInnerMode();
		
		transitions.add(transition);
		if(!modeChart.modes.containsKey(from)){
			modeChart.modes.put(from, new HashSet<>());
		}
		
		modeChart.modes.get(from).add(((TransitionImpl)transition).getModeSuccessor());

		// Add transition listener
		modeChart.addTransitionListener(from, to, new NonDetModeTransitionLogger(from, to));
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.modeswitch.ModeChart#removeTransition(cz.cuni.mff.d3s.metaadaptation.modeswitch.Transition)
	 */
	@Override
	public void removeTransition(Transition transition) {
		ModeImpl fromImpl = (ModeImpl) transition.getFrom();
		Class<? extends DEECoMode> from = fromImpl.getInnerMode();
		ModeImpl toImpl = (ModeImpl) transition.getTo();
		Class<? extends DEECoMode> to = toImpl.getInnerMode();
		
		transitions.remove(transition);
		double probability = transition.getProbability();
		ModeGuard guard = ((GuardImpl) transition.getGuard()).getInnerGuard();
		modeChart.modes.get(from).remove(new ModeSuccessor(to, probability, guard));
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.modeswitch.ModeChart#createTransition(cz.cuni.mff.d3s.metaadaptation.modeswitch.Mode, cz.cuni.mff.d3s.metaadaptation.modeswitch.Mode, cz.cuni.mff.d3s.metaadaptation.modeswitch.Guard)
	 */
	@Override
	public Transition createTransition(Mode from, Mode to, Guard guard) {
		ModeImpl toImpl = (ModeImpl) to;
		ModeImpl fromImpl = (ModeImpl) from;
		Class<? extends DEECoMode> toClass = toImpl.getInnerMode();
		
		ModeGuard modeGuard = new ModeGuard(){
			@Override
			protected void specifyParameters(){}
			@Override
			public String[] getKnowledgeNames() {
				return new String[]{};
			}
			@Override
			public boolean isSatisfied(Object[] knowledgeValues) {
				return guard.isSatisfied();
			}};
		GuardImpl guardImpl = new GuardImpl(modeGuard, component);
		
		ModeSuccessor successor = new ModeSuccessor(toClass, 0, modeGuard);
		return new TransitionImpl(fromImpl, toImpl, guardImpl, successor);
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.modeswitch.ModeChart#isModified()
	 */
	@Override
	public boolean isModified() {
		return modeChart.isModified();
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.modeswitch.ModeChart#setModified()
	 */
	@Override
	public void setModified() {
		modeChart.wasModified();
	}
	

}
