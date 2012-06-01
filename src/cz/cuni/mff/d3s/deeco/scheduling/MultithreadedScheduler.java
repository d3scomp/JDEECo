package cz.cuni.mff.d3s.deeco.scheduling;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;

public class MultithreadedScheduler extends Scheduler implements
		IKnowledgeChangeListener {

	protected KnowledgeManager km;
	private Map<SchedulableProcess, ScheduledExecutorService> threads;

	public MultithreadedScheduler(KnowledgeManager km) {
		super();
		this.km = km;
		threads = new HashMap<SchedulableProcess, ScheduledExecutorService>();
	}

	@Override
	public void start() {
		if (!running) {
			for (SchedulableProcess sp : processes) {
				if (sp.isTriggered()) {
					// TODO register listener in km.
				} else if (sp.isPeriodic()) {
					startPeriodicProcess(sp,
							((ProcessPeriodicSchedule) sp.scheduling).interval);
				}
			}
		}
	}

	@Override
	public void stop() {
		if (running) {
			for (SchedulableProcess sp : processes) {
				if (sp.isTriggered()) {
					// TODO unregister listener from km.
				} else if (sp.isPeriodic()) {
					threads.get(sp).shutdown();
				}
			}
		}
	}

	private void startPeriodicProcess(SchedulableProcess process, long period) {
		ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
		ses.scheduleAtFixedRate(new PeriodicProcessThread(process) , 0, period, TimeUnit.MILLISECONDS);
		threads.put(process, ses);
	}

	class PeriodicProcessThread implements Runnable {

		private SchedulableProcess process;

		public PeriodicProcessThread(SchedulableProcess process) {
			this.process = process;
		}

		@Override
		public void run() {
			process.invoke();
		}

	}

	@Override
	public void knowledgeChanged(String knowledgePathChanged) {

	}

}
