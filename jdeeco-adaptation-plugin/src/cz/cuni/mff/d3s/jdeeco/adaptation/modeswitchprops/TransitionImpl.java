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
package cz.cuni.mff.d3s.jdeeco.adaptation.modeswitchprops;

import java.util.Map;

import cz.cuni.mff.d3s.deeco.modes.DEECoTransition;
import cz.cuni.mff.d3s.metaadaptation.modeswitchprops.Mode;

/**
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class TransitionImpl implements cz.cuni.mff.d3s.metaadaptation.modeswitchprops.Transition {

	private final ModeImpl from;
	private final ModeImpl to;
	private final DEECoTransition transition;
	
	public TransitionImpl(ModeImpl from, ModeImpl to, DEECoTransition transition) {
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
		this.to = to;
		this.transition = transition;
	}
	
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.modeswitchprops.Transition#getFrom()
	 */
	@Override
	public Mode getFrom() {
		return from;
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.modeswitchprops.Transition#GetTo()
	 */
	@Override
	public Mode GetTo() {
		return to;
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.modeswitchprops.Transition#getGuardParams()
	 */
	@Override
	public Map<String, Double> getGuardParams() {
		return transition.getGuard().getParameters();
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.modeswitchprops.Transition#setGuardParam(java.lang.String, double)
	 */
	@Override
	public void setGuardParam(String name, double value) {
		transition.getGuard().setParameter(name, value);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return transition.toString();
	}

}
