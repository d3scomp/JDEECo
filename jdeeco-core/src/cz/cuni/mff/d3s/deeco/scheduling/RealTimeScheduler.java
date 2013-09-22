package cz.cuni.mff.d3s.deeco.scheduling;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import cz.cuni.mff.d3s.deeco.runtime.model.PeriodicSchedule;
import cz.cuni.mff.d3s.deeco.runtime.model.Schedule;

public class RealTimeScheduler extends Scheduler {

	public final static int THREAD_POOL_SIZE = 10;

	protected ScheduledThreadPoolExecutor executor;

	@Override
	public void jobExecutionFinished(Job job) {
		Schedule schedule = job.getSchedule();
		if (schedule instanceof PeriodicSchedule) {
			executor.schedule(job, ((PeriodicSchedule) schedule).getPeriod(),
					TimeUnit.MILLISECONDS);
		}
	}

	@Override
	public void jobExecutionStarted(Job job) {
		// Do nothing.
	}

	@Override
	public void schedule(Job job) {
		if (executor != null)
			executor.schedule(job, 0, TimeUnit.MILLISECONDS);
	}

	@Override
	public synchronized void start() {
		stop();
		executor = new ScheduledThreadPoolExecutor(THREAD_POOL_SIZE);
	}

	@Override
	public synchronized void stop() {
		if (executor != null) {
			executor.shutdown();
		}
		executor = null;
	}

	@Override
	public synchronized boolean isStarted() {
		return executor != null;
	}

	@Override
	public void jobExecutionException(Job job, Throwable t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getCurrentTime() {
		return System.currentTimeMillis();
	}

}
