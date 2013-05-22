/*******************************************************************************
 * Copyright 2012-2013 Charles University in Prague
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
package cz.cuni.mff.d3s.deeco.scheduling;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.invokable.TriggeredSchedulableProcess;

public abstract class Scheduler implements IScheduler {

	protected List<SchedulableProcess> periodicProcesses;
	protected List<TriggeredSchedulableProcess> triggeredProcesses;
	protected boolean running;

	public Scheduler() {
		periodicProcesses = new ArrayList<SchedulableProcess>();
		triggeredProcesses = new ArrayList<TriggeredSchedulableProcess>();
		running = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.scheduling.IScheduler#isRunning()
	 */
	@Override
	public boolean isRunning() {
		return running;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.scheduling.IScheduler#register(java.util.List)
	 */
	@Override
	public synchronized void add(List<? extends SchedulableProcess> processes) {
		for (SchedulableProcess sp : processes) {
			add(sp);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.scheduling.IScheduler#register(cz.cuni.mff.d3s.
	 * deeco.invokable.SchedulableProcess)
	 */
	@Override
	public synchronized void add(SchedulableProcess process) {
		if (process.scheduling instanceof ProcessTriggeredSchedule)
			triggeredProcesses.add(new TriggeredSchedulableProcess(process));
		else {
			periodicProcesses.add(process);
			if (running) {
				scheduleProcessForExecution(process);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.scheduling.IScheduler#unregister(java.util.List)
	 */
	@Override
	public synchronized void remove(List<SchedulableProcess> processes) {
		if (!running)
			for (SchedulableProcess sp : processes) {
				remove(sp);
			}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.scheduling.IScheduler#unregister(cz.cuni.mff.d3s
	 * .deeco.invokable.SchedulableProcess)
	 */
	@Override
	public synchronized void remove(SchedulableProcess process) {
		if (!running) {
			if (process.scheduling instanceof ProcessTriggeredSchedule)
				for (TriggeredSchedulableProcess tsp : triggeredProcesses) {
					if (tsp.sp == process) {
						tsp.unregisterListener();
						triggeredProcesses
								.remove(process);
					}
				}
			else
				periodicProcesses.remove(process);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.scheduling.IScheduler#getPeriodicProcesses()
	 */
	@Override
	public synchronized List<SchedulableProcess> getPeriodicProcesses() {
		return periodicProcesses;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.scheduling.IScheduler#getTriggeredProcesses()
	 */
	@Override
	public synchronized List<TriggeredSchedulableProcess> getTriggeredProcesses() {
		return triggeredProcesses;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.scheduling.IScheduler#clearAll()
	 */
	@Override
	public synchronized void clearAll() {
		if (running)
			stop();
		periodicProcesses.clear();
		for (TriggeredSchedulableProcess spt : triggeredProcesses)
			spt.unregisterListener();
		triggeredProcesses.clear();
	}

	protected abstract void scheduleProcessForExecution(SchedulableProcess process);
}
