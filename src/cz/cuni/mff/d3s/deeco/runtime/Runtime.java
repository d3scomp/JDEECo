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

import cz.cuni.mff.d3s.deeco.invokable.ComponentKnowledgeHelper;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.knowledge.ComponentKnowledge;
import cz.cuni.mff.d3s.deeco.scheduling.Scheduler;

/**
 * Class representing DEECo runtime
 * 
 * @author Michal Kit
 * 
 */
public class Runtime {

	private Scheduler scheduler;
	
	public Runtime() {
		scheduler = null;
	}
	
	public Runtime(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
	
	public void setScheduler(Object scheduler) {
		unsetScheduler(scheduler);
		if (scheduler instanceof Scheduler)
			this.scheduler = (Scheduler) scheduler;
	}
	
	public void unsetScheduler(Object scheduler) {
		if (this.scheduler != null)
			this.scheduler.clearAll();
		this.scheduler = null;
	}

	public synchronized void addSchedulableProcess(
			SchedulableProcess process) {
		if (process != null)
			scheduler.register(process);
	}


	public synchronized void addSchedulablePorcesses(
			List<? extends SchedulableProcess> processes) {
		if (processes != null)
			for (SchedulableProcess sp : processes) {
				addSchedulableProcess(sp);
			}
	}

	public synchronized boolean addComponentKnowledge(ComponentKnowledge initKnowledge) {
		if (initKnowledge != null)
			try {
				if (scheduler.km != null)
					return ComponentKnowledgeHelper.addComponentKnowledge(initKnowledge, scheduler.km);
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
