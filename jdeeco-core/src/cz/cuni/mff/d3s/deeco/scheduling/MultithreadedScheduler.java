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

public class MultithreadedScheduler extends Scheduler {
	private Map<SchedulableProcess, ScheduledExecutorService> threads;

	public MultithreadedScheduler() {
		super();
		threads = new HashMap<SchedulableProcess, ScheduledExecutorService>();
	}

	@Override
	public synchronized void start() {
		if (!running) {
			for (SchedulableProcess sp : periodicProcesses) {
				startPeriodicProcess(sp,
						((ProcessPeriodicSchedule) sp.scheduling).interval);
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
			for (SchedulableProcess sp : periodicProcesses) {
				threads.get(sp).shutdown();
			}
			List<KnowledgeManager> kms = new LinkedList<KnowledgeManager>();
			for (TriggeredSchedulableProcess tsp : triggeredProcesses) {
				if (!kms.contains(tsp.getKnowledgeManager()))
					kms.add(tsp.getKnowledgeManager());
			}
			for (KnowledgeManager km : kms) {
				km.setListenersActive(false);
			}
			running = false;
		}
	}

	private void startPeriodicProcess(SchedulableProcess process, long period) {
		ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
		ses.scheduleAtFixedRate(new PeriodicProcessThread(process), 0, period,
				TimeUnit.MILLISECONDS);
		threads.put(process, ses);
	}

	class PeriodicProcessThread implements Runnable {

		private SchedulableProcess process;

		public PeriodicProcessThread(SchedulableProcess process) {
			this.process = process;
		}

		@Override
		public void run() {
			try {
				if (process.contextClassLoader != null)
					Thread.currentThread().setContextClassLoader(process.contextClassLoader);
				process.invoke();
			} catch (Exception e) {
				System.out.println("Process scheduled exception!");
			}
		}
	}

}
