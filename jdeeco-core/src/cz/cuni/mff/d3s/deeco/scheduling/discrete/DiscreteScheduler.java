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
package cz.cuni.mff.d3s.deeco.scheduling.discrete;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableComponentProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableComponentProcessWrapper;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcessWrapper;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcessWrapper;
import cz.cuni.mff.d3s.deeco.invokable.TriggeredSchedulableProcess;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.path.grammar.EEnsembleParty;
import cz.cuni.mff.d3s.deeco.path.grammar.PathGrammar;
import cz.cuni.mff.d3s.deeco.scheduling.IScheduler;
import cz.cuni.mff.d3s.deeco.scheduling.ProcessPeriodicSchedule;
import cz.cuni.mff.d3s.deeco.scheduling.ProcessTriggeredSchedule;

/**
 * Single-threaded scheduler to be used for discrete event simulations.Processes
 * are executed based on their position in a priority queue. <br>
 * Triggered processes are not implemented with listeners. Instead, after each
 * process execution, the updated knowledge paths are collected and new
 * processes are scheduled when appropriate.
 * 
 * @author Ilias Gerostathopoulos
 * 
 */
public class DiscreteScheduler implements IScheduler {

	private List<SchedulableProcessWrapper> periodicProcesses = new ArrayList<SchedulableProcessWrapper>();;
	private List<TriggeredSchedulableProcess> triggeredProcesses = new ArrayList<TriggeredSchedulableProcess>();;
	private boolean running = false;

	private long virtualTime;
	private PriorityQueue<SchedulableProcessExecution> pQueue = new PriorityQueue<SchedulableProcessExecution>();
	private Thread discreteSchedulerThread;

	@Override
	public void start() {
		if (!running) {
			virtualTime = 0;
			for (SchedulableProcessWrapper spw : periodicProcesses) {
				scheduleProcessForExecution(spw);
			}
			running = true;
			discreteSchedulerThread = new Thread(new Runnable() {

				public void run() {
					// list to collect the updated knowledge paths:
					List<String> changedKnowledgePaths = new ArrayList<String>();
					// loop forever:
					for (;;) {
						// get the first process execution in the queue:
						SchedulableProcessExecution pExecution = pQueue.poll();
						SchedulableProcessWrapper spWrapper = pExecution
								.getProcessWrapper();
						virtualTime = pExecution.getExecTime();
						// execute the process through its wrapper:
						Log.d("Executing process " + spWrapper.getProcess()
								+ " in virtual time (with seed): "
								+ virtualTime + pExecution.getSeed());
						spWrapper.invoke();
						// collect the changed paths:
						changedKnowledgePaths = spWrapper
								.getChangedKnowledgePaths();
						// re-schedule only the periodic processes:
						if (spWrapper.getProcess().isPeriodic()) {
							scheduleProcessForExecution(spWrapper);
						}
						// trigger the processes listening to knowledge changes:
						if (!changedKnowledgePaths.isEmpty()) {
							for (String changed : changedKnowledgePaths) {
								for (TriggeredSchedulableProcess tsp : triggeredProcesses) {
									List<String> triggers = tsp
											.getKnowledgePaths();
									for (String trigger : triggers) {
										if (pathsMatch(changed, trigger)) {
											Log.d("Path '" + trigger
													+ "' changed. Process "
													+ tsp.sp + " is triggered.");
											scheduleProcessForExecution(wrapProcess(tsp.sp));
										}
									}
								}
							}
						}
					}
				}
			});
			discreteSchedulerThread.start();
		}
	}

	@Override
	public void stop() {
		if (running) {
			discreteSchedulerThread.interrupt();
			running = false;
		}
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	@Override
	public void add(List<? extends SchedulableProcess> processes) {
		for (SchedulableProcess sp : processes) {
			add(sp);
		}
	}

	@Override
	public void add(SchedulableProcess process) {
		if (process.scheduling instanceof ProcessTriggeredSchedule)
			triggeredProcesses.add(new TriggeredSchedulableProcess(process));
		else {
			SchedulableProcessWrapper spWrapper = wrapProcess(process);
			periodicProcesses.add(spWrapper);
			if (running) {
				scheduleProcessForExecution(spWrapper);
			}
		}
	}

	@Override
	public void remove(List<SchedulableProcess> processes) {
		if (!running) {
			for (SchedulableProcess sp : processes) {
				remove(sp);
			}
		}
	}

	@Override
	public void remove(SchedulableProcess process) {
		if (!running) {
			if (process.scheduling instanceof ProcessTriggeredSchedule) {
				triggeredProcesses.remove(process);
			} else {
				periodicProcesses.remove(process);
			}
		}
	}

	@Override
	public List<TriggeredSchedulableProcess> getTriggeredProcesses() {
		return triggeredProcesses;
	}

	@Override
	public List<SchedulableProcess> getPeriodicProcesses() {
		List<SchedulableProcess> schedulableProcesses = new ArrayList<SchedulableProcess>();
		for (SchedulableProcessWrapper spWrapper : periodicProcesses) {
			schedulableProcesses.add(spWrapper.getProcess());
		}
		return schedulableProcesses;
	}

	@Override
	public void clearAll() {
		if (running)
			stop();
		periodicProcesses.clear();
		triggeredProcesses.clear();
	}

	/**
	 * Adds a process wrapper to the queue for execution.
	 * 
	 * @param spWrapper
	 *            process wrapper to be scheduled
	 */
	private void scheduleProcessForExecution(SchedulableProcessWrapper spWrapper) {
		// if the wrapped process is periodic, calculate the next execution
		// time: add the period to the current virtual time
		long addedTime = (spWrapper.getProcess().isPeriodic()) ? ((ProcessPeriodicSchedule) spWrapper
				.getProcess().scheduling).interval : 0;
		// reminder: execTime in SchedulableProcessExecution in usec
		long execTime = virtualTime + addedTime * 1000;
		// always add random seed
		Random rnd = new Random();
		int seed = rnd.nextInt(10);
		Log.d("Adding process " + spWrapper.getProcess()
				+ " with virtual execution time " + execTime + " and seed "
				+ seed);
		pQueue.add(new SchedulableProcessExecution(spWrapper, execTime, seed));
	}

	/**
	 * Utility method to compare two knowledge paths. In case of a triggerable
	 * ensemble (where the path starts with "member" or "ensemble" prefix) the
	 * rest of the paths are compared.
	 * 
	 * @param changed
	 *            knowledge path that has been updated
	 * @param trigger
	 *            knowledge path that listens to changes
	 * @return true, if the paths match
	 */
	private boolean pathsMatch(String changed, String trigger) {
		if ((trigger.startsWith(EEnsembleParty.COORDINATOR.toString()))
				|| (trigger.startsWith(EEnsembleParty.MEMBER.toString()))) {
			changed = changed.substring(changed
					.indexOf(PathGrammar.PATH_SEPARATOR));
			trigger = trigger.substring(trigger
					.indexOf(PathGrammar.PATH_SEPARATOR));
		}
		return changed.equals(trigger);
	}

	/**
	 * Creates a process wrapper out of an existing SchedulableProcess. Wrapping
	 * serves to keep track of the knowledge updates during execution.
	 * 
	 * @param process
	 *            wrapped process (adaptee)
	 * @return wrapper (adapter)
	 */
	private SchedulableProcessWrapper wrapProcess(SchedulableProcess process) {
		if (process instanceof SchedulableComponentProcess)
			return new SchedulableComponentProcessWrapper(process);
		else
			return new SchedulableEnsembleProcessWrapper(process);
	}

}
