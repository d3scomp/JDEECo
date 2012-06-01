package cz.cuni.mff.d3s.deeco.scheduling;

import java.util.HashMap;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;

public class MultithreadedScheduler extends Scheduler {

	private Map<SchedulableProcess, Thread> threads;

	public MultithreadedScheduler(KnowledgeManager km) {
		super(km);
		threads = new HashMap<SchedulableProcess, Thread>();
	}

	@Override
	public void start() {
		if (!running) {
			Thread processThread;
			for (SchedulableProcess sp : processes) {
				if (sp.isTriggered()) {
					// TODO register listener in km.
				} else if (sp.isPeriodic()) {
					processThread = new Thread(new PeriodicProcessThread(sp,
							((ProcessPeriodicSchedule) sp.scheduling).interval));
					threads.put(sp, processThread);
					processThread.start();
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
					threads.get(sp).interrupt();
				}
			}
		}
	}

	class PeriodicProcessThread implements Runnable {

		private SchedulableProcess process;
		private long period;

		public PeriodicProcessThread(SchedulableProcess process, long period) {
			this.process = process;
			this.period = period;
		}

		@Override
		public void run() {
			try {
				while (true) {
					process.invoke();
					Thread.sleep(period);
				}
			} catch (Exception e) {
				System.out.println("ERROR - Process execution error: "
						+ e.getMessage());
			}
		}

	}

}
