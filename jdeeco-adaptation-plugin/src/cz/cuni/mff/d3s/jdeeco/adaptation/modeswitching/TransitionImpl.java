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

import java.util.function.Predicate;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.modes.DEECoTransition;
import cz.cuni.mff.d3s.metaadaptation.modeswitch.Mode;
import cz.cuni.mff.d3s.metaadaptation.modeswitch.Transition;

/**
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class TransitionImpl implements Transition {

	private final ModeImpl from;
	private final ModeImpl to;
	private final DEECoTransition transition;
	private final GuardImpl guard;
	
	public TransitionImpl(ModeImpl from, ModeImpl to, DEECoTransition transition, ComponentInstance component){
		if(from == null){
			throw new IllegalArgumentException(String.format("The %s argument is null.", "from"));
		}
		if(to == null){
			throw new IllegalArgumentException(String.format("The %s argument is null.", "to"));
		}
		if(transition == null){
			throw new IllegalArgumentException(String.format("The %s argument is null.", "transition"));
		}
		
		this.from = from;
		this.to= to;
		this.transition = transition;
		guard = new GuardImpl(transition.getGuard(), component);
	}
	
	public DEECoTransition getInnerTransition(){
		return transition;
	}
	
	
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.modeswitch.Transition#getFrom()
	 */
	@Override
	public Mode getFrom() {
		return from;
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.modeswitch.Transition#getTo()
	 */
	@Override
	public Mode getTo() {
		return to;
	}

//	/* (non-Javadoc)
//	 * @see cz.cuni.mff.d3s.metaadaptation.modeswitch.Transition#isGuardSatisfied()
//	 */
//	@Override
//	public boolean isGuardSatisfied() {
//		return transition.getGuard().isSatisfied();
//	}
//
//	/* (non-Javadoc)
//	 * @see cz.cuni.mff.d3s.metaadaptation.modeswitch.Transition#getProbability()
//	 */
//	@Override
//	public double getProbability() {
//		return modeSuccessor.getProbability();
//	}
//
//	/* (non-Javadoc)
//	 * @see cz.cuni.mff.d3s.metaadaptation.modeswitch.Transition#setProbability(double)
//	 */
//	@Override
//	public void setProbability(double probability) {
//		modeSuccessor.setProbability(probability);
//	}
//
//	/* (non-Javadoc)
//	 * @see cz.cuni.mff.d3s.metaadaptation.modeswitch.Transition#isDynamic()
//	 */
//	@Override
//	public boolean isDynamic() {
//		return modeSuccessor.isDynamic();
//	}
//
//	/* (non-Javadoc)
//	 * @see cz.cuni.mff.d3s.metaadaptation.modeswitch.Transition#setDynamic(boolean)
//	 */
//	@Override
//	public void setDynamic(boolean isDynamic) {
//		modeSuccessor.setDynamic(isDynamic);
//		
//	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.modeswitch.Transition#getGuard()
	 */
	@Override
	public Predicate<Void> getGuard() {
		return new Predicate<Void>(){
			@Override
			public boolean test(Void t) {
				return guard.isSatisfied();
			}
			
		};
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.modeswitch.Transition#getPriority()
	 */
	@Override
	public int getPriority() {
		return transition.getPriority();
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.modeswitch.Transition#setPriority(int)
	 */
	@Override
	public void setPriority(int priority) {
		transition.setPriority(priority);		
	}

}
