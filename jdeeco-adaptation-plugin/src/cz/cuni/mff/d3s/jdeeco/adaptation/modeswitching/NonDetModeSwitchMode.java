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

import cz.cuni.mff.d3s.deeco.modes.DEECoMode;

/**
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public interface NonDetModeSwitchMode extends DEECoMode {

	/**
	 * Indicate whether it is safe to non-deterministically enter the mode.
	 * 
	 * @return True if its safe to enter the mode non-deterministically.
	 * 	False otherwise.
	 */
	boolean nonDeterministicIn();

	/**
	 * Indicate whether it is safe to non-deterministically leave the mode.
	 * 
	 * @return True if its safe to leave the mode non-deterministically.
	 * 	False otherwise.
	 */
	boolean nonDeterministicOut();
}
