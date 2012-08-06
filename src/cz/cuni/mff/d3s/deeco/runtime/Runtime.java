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

import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.ComponentManager;
import cz.cuni.mff.d3s.deeco.invokable.EnsembleManager;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableComponentProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcess;
import cz.cuni.mff.d3s.deeco.knowledge.ComponentKnowledge;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.scheduling.Scheduler;

/**
 * Class representing DEECo runtime
 * 
 * @author Michal Kit
 * 
 */
public class Runtime {

	private EnsembleManager ensembleManager;
	private ComponentManager componentManager;
	private Scheduler scheduler;
	
	public Runtime(KnowledgeManager km, Scheduler scheduler) {
		componentManager = new ComponentManager(km, scheduler);
		ensembleManager = new EnsembleManager(km, scheduler);
		this.scheduler = scheduler;
	}

	public synchronized void addComponentProcess(
			SchedulableComponentProcess componentProcess) {
		if (componentProcess != null)
			componentManager.addProcess(componentProcess);
	}

	public synchronized void addEnsembleProcess(
			SchedulableEnsembleProcess ensembleProcess) {
		if (ensembleProcess != null)
			ensembleManager.addProcess(ensembleProcess);
	}

	public synchronized void addComponentPorcesses(
			List<SchedulableComponentProcess> componentProcesses) {
		if (componentProcesses != null)
			for (SchedulableComponentProcess scp : componentProcesses) {
				addComponentProcess(scp);
			}
	}

	public synchronized void addEnsembleProcesses(
			List<SchedulableEnsembleProcess> ensembleProcesses) {
		if (ensembleProcesses != null)
			for (SchedulableEnsembleProcess sep : ensembleProcesses) {
				addEnsembleProcess(sep);
			}
	}

	public synchronized boolean addComponentKnowledge(ComponentKnowledge initKnowledge) {
		if (initKnowledge != null)
			try {
				return componentManager.addComponentKnowledge(initKnowledge);
			} catch (Exception e) {
				System.out.println("Initial knowlege retrival exception");
			}
		return false;
	}

	/**
	 * Starts components and ensembles operation.
	 */
	public synchronized void startRuntime() {
		scheduler.start();
	}

	/**
	 * Stops components and ensembles operation.
	 */
	public synchronized void stopRuntime() {
		scheduler.stop();
	}
}
