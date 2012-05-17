/*******************************************************************************
 * Copyright 2012 Charles University in Prague
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package cz.cuni.mff.d3s.deeco.runtime;

import cz.cuni.mff.d3s.deeco.invokable.ComponentManager;
import cz.cuni.mff.d3s.deeco.invokable.EnsembleManager;
import cz.cuni.mff.d3s.deeco.invokable.InvokableManager;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;

/**
 * Class representing DEECo runtime
 * 
 * @author Michal Kit
 * 
 */
public class Runtime {

	private InvokableManager<? extends SchedulableProcess> ensembleManager;
	private InvokableManager<? extends SchedulableProcess> componentManager;

	public Runtime(Class[] components, Class[] ensembles, KnowledgeManager km) {
		componentManager = new ComponentManager(km);
		ensembleManager = new EnsembleManager(km);
		if (components != null)
			for (Class c : components) {
				componentManager.addInvokable(c);
			}
		if (ensembles != null)
			for (Class e : ensembles) {
				ensembleManager.addInvokable(e);
			}
		startRuntime();
	}

	/**
	 * Starts components and ensembles operation.
	 */
	public synchronized void startRuntime() {
		componentManager.startInvokables();
		ensembleManager.startInvokables();
	}

	/**
	 * Stops components and ensembles operation.
	 */
	public synchronized void stopRuntime() {
		componentManager.stopInvokables();
		ensembleManager.stopInvokables();
	}
}
