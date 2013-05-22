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

import cz.cuni.mff.d3s.deeco.invokable.Parameter;
import cz.cuni.mff.d3s.deeco.invokable.ParameterizedMethod;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableComponentProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.invokable.TriggeredSchedulableProcess;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.path.grammar.EEnsembleParty;
import cz.cuni.mff.d3s.deeco.path.grammar.PathGrammar;
import cz.cuni.mff.d3s.deeco.scheduling.ProcessPeriodicSchedule;
import cz.cuni.mff.d3s.deeco.scheduling.Scheduler;

/**
 * Single-threaded scheduler to be used for discrete event simulations.Processes
 * are executed based on their position in a priority queue. <br>
 * Triggered processes are not implemented with listeners. Instead, after each
 * process execution, they are checked to be scheduled or not.
 * 
 * @author Ilias Gerostathopoulos
 * 
 */
public class DiscreteScheduler extends Scheduler {

	private long virtualTime;
	private PriorityQueue<SchedulableProcessExecution> pQueue = new PriorityQueue<SchedulableProcessExecution>();
	private Thread discreteSchedulerThread;

	@Override
	public void start() {

		if (!running) {

			virtualTime = 0;
			for (SchedulableProcess sp : periodicProcesses) {
				scheduleProcessForExecution(sp);
			}

			running = true;
			discreteSchedulerThread = new Thread(new Runnable() {
				@Override
				public void run() {

					for (;;) {
						// get the first process execution in the queue:
						SchedulableProcessExecution pExecution = pQueue.poll();
						SchedulableProcess sp = pExecution.getProcess();
						virtualTime = pExecution.getExecTime();
						Log.d("Executing process " + sp
								+ " in virtual time (with seed): "
								+ virtualTime + pExecution.getSeed());
						sp.invoke();

						// re-schedule only the periodic processes:
						if (sp.isPeriodic())
							scheduleProcessForExecution(sp);

						// schedule for execution all triggered processes that
						// are affected
						List<String> affectedKnowledgePaths = new ArrayList<String>();
						/*
						 * In case of an ensemble process, only the
						 * knowledgeExchange method is considered. This means
						 * that triggers will get activated even if membership
						 * returns false. TODO(?): make triggering via ensembles
						 * membership-aware
						 */
						ParameterizedMethod method = (sp instanceof SchedulableEnsembleProcess) ? ((SchedulableEnsembleProcess) sp).knowledgeExchange
								: ((SchedulableComponentProcess) sp).process;
						for (Parameter par : method.inOut) {
							affectedKnowledgePaths
									.add(getKnowledgePath(sp, par));
						}
						for (Parameter par : method.out) {
							affectedKnowledgePaths
									.add(getKnowledgePath(sp, par));
						}
						for (String affected : affectedKnowledgePaths) {
							for (TriggeredSchedulableProcess spt : triggeredProcesses) {
								List<String> triggeredKnowledgePaths = spt
										.getKnowledgePaths();
								for (String triggered : triggeredKnowledgePaths) {
									if (isSamePath(affected, triggered)) {
										Log.d("Path '"
												+ triggered
												+ "' changed. Process will get triggered.");
										scheduleProcessForExecution(spt.sp);
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

	protected void scheduleProcessForExecution(SchedulableProcess process) {
		// if periodic, calculate the next execution time: add the period
		// to the current virtual time
		long addedTime = (process.isPeriodic()) ? ((ProcessPeriodicSchedule) process.scheduling).interval
				: 0;
		// reminder: execTime in SchedulableProcessExecution in usec
		long execTime = virtualTime + addedTime * 1000;
		// always add random seed
		Random rnd = new Random();
		int seed = rnd.nextInt(10);
		Log.d("Adding process " + process + " with virtual execution time "
				+ execTime + " and seed " + seed);
		pQueue.add(new SchedulableProcessExecution(process, execTime, seed));
	}

	/*
	 * Utility method to extract the complete knowledge path of a parameter of a
	 * schedulable process
	 */
	private String getKnowledgePath(SchedulableProcess sp, Parameter par) {
		String coord = null;
		String member = null;
		String kPath = "";
		if (sp instanceof SchedulableEnsembleProcess) {
			coord = EEnsembleParty.COORDINATOR.toString();
			member = EEnsembleParty.MEMBER.toString();
		}
		ISession session = sp.km.createSession();
		session.begin();
		while (session.repeat()) {
			kPath = par.kPath.getEvaluatedPath(sp.km, coord, member, session);
			session.end();
		}
		return kPath;
	}

	/*
	 * Utility method to compare two knowledge paths. If a path starts with the
	 * "member" or "ensemble" prefix then the rest of the paths are compared.
	 */
	private boolean isSamePath(String triggered, String affected) {
		if ((triggered.startsWith(EEnsembleParty.COORDINATOR.toString()))
				|| (triggered.startsWith(EEnsembleParty.MEMBER.toString()))
				|| (affected.startsWith(EEnsembleParty.COORDINATOR.toString()))
				|| (affected.startsWith(EEnsembleParty.MEMBER.toString()))) {
			triggered = triggered.substring(triggered
					.indexOf(PathGrammar.PATH_SEPARATOR));
			affected = affected.substring(affected
					.indexOf(PathGrammar.PATH_SEPARATOR));
		}
		return triggered.equals(affected);
	}
}
