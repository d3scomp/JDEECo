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
import cz.cuni.mff.d3s.deeco.modes.DEECoModeGuard;
import cz.cuni.mff.d3s.jdeeco.modes.ModeChartHolder;
import cz.cuni.mff.d3s.jdeeco.modes.Transition;

/**
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class TestModeChartHolder extends ModeChartHolder {

	public TestModeChartHolder(){
		
		// GUARDS #############################################################
		
		final DEECoModeGuard G_true = new DEECoModeGuard(){
			@Override
			protected void specifyParameters() {
				// Nothing to do, this is only relevant for meta-adaptation
			}

			@Override
			public String[] getKnowledgeNames() {
				return new String[]{"inMode", "canTransit"};
			}

			@Override
			public boolean isSatisfied(Object[] knowledgeValues) {
				// Check that knowledge was passed correctly
				int inMode = (int) knowledgeValues[0];
				System.out.println(String.format("Testing guard in Mode %d", inMode));
				
				boolean canTransit = (boolean) knowledgeValues[1];
				
				return canTransit;
			}			
		};
		
		final DEECoModeGuard G_false = new DEECoModeGuard(){
			@Override
			protected void specifyParameters() {
				// Nothing to do, this is only relevant for meta-adaptation
			}

			@Override
			public String[] getKnowledgeNames() {
				return new String[]{"inMode"};
			}

			@Override
			public boolean isSatisfied(Object[] knowledgeValues) {
				// Check that knowledge was passed correctly
				int inMode = (int) knowledgeValues[0];
				System.out.println(String.format("Testing guard in Mode %d", inMode));
				
				return false;
			}			
		};
		
		// MODES ##############################################################

		final DEECoMode m1 = new M1();
		final DEECoMode m2 = new M2();
		final DEECoMode m3 = new M3();
		final DEECoMode m4 = new M4();
		
		// TRANSITIONS ########################################################
		
		Transition transition;
		transition = addTransition(m1, m2, G_true);
		transition.setPriority(2);
		addTransitionListener(transition, new PrintTransitionListener(transition));
		
		transition = addTransition(m1, m3, G_false);
		transition.setPriority(3);
		addTransitionListener(transition, new PrintTransitionListener(transition));

		transition = addTransition(m1, m4, G_true);
		transition.setPriority(1);
		addTransitionListener(transition, new PrintTransitionListener(transition));

		transition = addTransition(m2, m4, G_true);
		transition.setPriority(1);
		addTransitionListener(transition, new PrintTransitionListener(transition));

		transition = addTransition(m2, m3, G_true);
		transition.setPriority(2);
		addTransitionListener(transition, new PrintTransitionListener(transition));

		transition = addTransition(m3, m2, G_false);
		transition.setPriority(1);
		addTransitionListener(transition, new PrintTransitionListener(transition));

		transition = addTransition(m3, m4, G_true);
		transition.setPriority(1);
		addTransitionListener(transition, new PrintTransitionListener(transition));
	}
	
	
	
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.jdeeco.modes.ModeChartHolder#getInitialMode()
	 */
	@Override
	public DEECoMode getInitialMode() {
		return new M1();
	}

}
