package cz.cuni.mff.d3s.deeco.scheduling;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.invokable.TriggeredSchedulableProcess;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.logging.Log;

public class MultithreadedScheduler extends Scheduler {
	
	public static final int THREAD_NUMBER = 10;
	
	private ScheduledExecutorService threads;

	public MultithreadedScheduler() {
		super();
	}
	
	@Override
	public long getTime() {
		return System.currentTimeMillis();
	}

	@Override
	public synchronized void start() {
		if (!running) {
			threads = Executors.newScheduledThreadPool(THREAD_NUMBER);
			for (SchedulableProcess sp : periodicProcesses) {
				scheduleProcessForExecution(sp);
			}

			List<KnowledgeManager> kms = new LinkedList<KnowledgeManager>();
			for (TriggeredSchedulableProcess tsp : triggeredProcesses) {
				tsp.registerListener();
				if (!kms.contains(tsp.getKnowledgeManager()))
					kms.add(tsp.getKnowledgeManager());
			}
			for (KnowledgeManager km : kms) {
				km.setListenersActive(true);
			}
			running = true;
		}
	}

	@Override
	public synchronized void stop() {
		if (running) {
			threads.shutdown();
			List<KnowledgeManager> kms = new LinkedList<KnowledgeManager>();
			for (TriggeredSchedulableProcess tsp : triggeredProcesses) {
				if (!kms.contains(tsp.getKnowledgeManager()))
					kms.add(tsp.getKnowledgeManager());
			}
			for (KnowledgeManager km : kms) {
				km.setListenersActive(false);
			}
			threads = null;
			running = false;
		}
	}

	@Override
	protected synchronized void scheduleProcessForExecution(SchedulableProcess process) {
		threads.scheduleAtFixedRate(new PeriodicProcessThread(process), 0,
				((ProcessPeriodicSchedule) process.scheduling).interval,
				TimeUnit.MILLISECONDS);
	}

	static class PeriodicProcessThread implements Runnable {

		private SchedulableProcess process;

		public PeriodicProcessThread(SchedulableProcess process) {
			this.process = process;
		}

		@Override
		public void run() {
			try {
				if (process.contextClassLoader != null)
					Thread.currentThread().setContextClassLoader(
							process.contextClassLoader);
				process.invoke();
			} catch (Exception e) {
				Log.e("Process scheduled exception",e);
			}
		}
	}

}
