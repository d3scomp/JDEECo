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

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.scheduling.Scheduler;

/**
 * Class providing ensemble managing functionalities.
 * 
 * @author Michal Kit
 * 
 */
public class EnsembleManager extends
		InvokableManager<SchedulableEnsembleProcess> {

	public EnsembleManager(KnowledgeManager km, Scheduler scheduler) {
		super(km, scheduler);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.invokable.InvokableManager#addInvokable(java.lang
	 * .Class)
	 */
	@Override
	public void addProcess(SchedulableProcess schedulableProcess) {
		if (schedulableProcess != null) {
			schedulableProcess.setKnowledgeManager(km);
			processes.add((SchedulableEnsembleProcess) schedulableProcess);
			scheduler.register(schedulableProcess);
		}
	}

}
