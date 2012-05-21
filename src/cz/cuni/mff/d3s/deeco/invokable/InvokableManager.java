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
package cz.cuni.mff.d3s.deeco.invokable;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;

/**
 * Generic class used to provide common functionalities for all
 * {@link SchedulableProcess}.
 * 
 * @author Michal Kit
 * 
 * @param class extending {@link SchedulableProcess} - either
 *        {@link SchedulableKnowledgeProcess} or
 *        {@link SchedulableEnsembleProcess}
 */
public abstract class InvokableManager<T extends SchedulableProcess> {

	protected List<T> processes;
	protected KnowledgeManager km;

	public InvokableManager(KnowledgeManager km) {
		processes = new ArrayList<T>();
		this.km = km;
	}

	/**
	 * Adds a schedulable process to the managed list.
	 * 
	 * @param invokableDefinition
	 *            class having a definitions of potential schedulable processes.
	 */
	public abstract void addInvokable(Class invokableDefinition);

	/**
	 * Starts the execution of all schedulable processes.
	 */
	public void startInvokables() {
		for (T i : processes) {
			i.start();
		}
	}

	/**
	 * Stops the execution of all schedulable processes.
	 */
	public void stopInvokables() {
		for (T i : processes) {
			i.stop();
		}
	}
}
