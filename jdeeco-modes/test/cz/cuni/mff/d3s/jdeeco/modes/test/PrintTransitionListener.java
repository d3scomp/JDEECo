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
package cz.cuni.mff.d3s.jdeeco.modes.test;

import cz.cuni.mff.d3s.deeco.modes.DEECoMode;
import cz.cuni.mff.d3s.deeco.modes.DEECoTransitionListener;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.jdeeco.modes.Transition;

/**
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class PrintTransitionListener implements DEECoTransitionListener {

	private final DEECoMode fromMode;
	private final DEECoMode toMode;
	
	public PrintTransitionListener(Transition transition){
		this.fromMode = transition.getFrom();
		this.toMode = transition.getTo();
	}
	
	
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.modes.DEECoTransitionListener#getKnowledgeNames()
	 */
	@Override
	public String[] getKnowledgeNames() {
		return new String[]{"id", "canTransit", "inMode"};
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.modes.DEECoTransitionListener#transitionTaken(cz.cuni.mff.d3s.deeco.task.ParamHolder[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void transitionTaken(ParamHolder<?>[] knowledgeValues) {
		System.out.println(String.format("Transition from %s to %s in %s taken.",
				fromMode, toMode, knowledgeValues[0].value));
		
		// Forbid next transit
		ParamHolder<Boolean> canTransit = (ParamHolder<Boolean>) knowledgeValues[1];
		canTransit.value = false;
		
		// Assign current mode
		ParamHolder<Integer> inMode = (ParamHolder<Integer>) knowledgeValues[2];
		if(toMode.equals(new M1())){
			inMode.value = 1;
		} else if(toMode.equals(new M2())){
			inMode.value = 2;
		} else if(toMode.equals(new M3())){
			inMode.value = 3;
		} else if(toMode.equals(new M4())){
			inMode.value = 4;
		}
	}

}
