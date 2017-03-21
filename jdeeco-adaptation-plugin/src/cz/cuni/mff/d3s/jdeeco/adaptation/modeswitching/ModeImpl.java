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

import cz.cuni.mff.d3s.deeco.modes.DEECoMode;
import cz.cuni.mff.d3s.metaadaptation.modeswitch.Mode;

/**
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class ModeImpl implements Mode {

	private final Class<? extends DEECoMode> mode;
	private NonDetModeSwitchMode modeInstance;
	
	public ModeImpl(Class<? extends DEECoMode> mode){
		if(mode == null){
			throw new IllegalArgumentException(String.format("The %s argument is null.", "mode"));
		}
		
		this.mode = mode;
		try{
			modeInstance = (NonDetModeSwitchMode) mode.newInstance();
		} catch(InstantiationException | IllegalAccessException e) {
			System.err.println(e.getMessage());
			modeInstance = null;
		}
	}
	
	public Class<? extends DEECoMode> getInnerMode(){
		return mode;
	}
	
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.modeswitch.Mode#getId()
	 */
	@Override
	public String getId() {
		return mode.getName();
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.modeswitch.Mode#nonDeterministicIn()
	 */
	@Override
	public boolean nonDeterministicIn() {
		return modeInstance.nonDeterministicIn();
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.modeswitch.Mode#nonDeterministicOut()
	 */
	@Override
	public boolean nonDeterministicOut() {
		return modeInstance.nonDeterministicOut();
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.modeswitch.Mode#isFitnessComputed()
	 */
	@Override
	public boolean isFitnessComputed() {
		return modeInstance.isFitnessComputed();
	}

}
