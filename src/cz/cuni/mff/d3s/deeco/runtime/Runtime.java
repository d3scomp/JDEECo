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
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.provider.AbstractDEECoObjectProvider;
import cz.cuni.mff.d3s.deeco.scheduling.Scheduler;

/**
 * Class representing DEECo runtime
 * 
 * @author Michal Kit
 * 
 */
public class Runtime {

	private Scheduler scheduler;
	private KnowledgeManager knowledgeManager;
	
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
		// TODO the scheduler parameter is not used, fix or remove
		if (this.scheduler != null)
			this.scheduler.clearAll();
		this.scheduler = null;
	}
	
	public void setKnowledgeManager(KnowledgeManager km) {		
			this.knowledgeManager = km;
	}
	
	/**
	 * Adds all the component and ensemble definitions given by the provider object.
	 * @param provider provides the component/ensemble definitions to be added.
	 * 
	 * TODO: use local km, not the one from the provider 
	 */
	public void addDefinitions(AbstractDEECoObjectProvider provider) {
		addSchedulablePorcesses(provider.getProcesses());
		for (ComponentKnowledge ck : provider.getKnowledges()) {
			if (!addComponentKnowledge(ck, provider.getKnowledgeManager())) {
				System.out.println("Error when writng initial knowledge: " + ck.getClass());
				continue;
			}
		}		
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
	
	public synchronized void addKnowledges(
			List<? extends ComponentKnowledge> knowledges, KnowledgeManager km) {
		if (knowledges != null)
			for (ComponentKnowledge ck : knowledges) {
				addComponentKnowledge(ck, km);
			}
	}

	public synchronized boolean addComponentKnowledge(ComponentKnowledge initKnowledge, KnowledgeManager km) {
		if (initKnowledge != null)
			try {
				if (km != null)
					return ComponentKnowledgeHelper.addComponentKnowledge(initKnowledge, km);
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
