package cz.cuni.mff.d3s.deeco.task;

import cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.TimeTriggerExt;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;

public class TimerTask extends Task {
	protected final TimeTrigger trigger;
	protected final TimerTaskListener taskListener;

	/**
	 * Constructs an instant task
	 * 
	 * INstant task is executed after 1 ms step in the simulation
	 * 
	 * @param scheduler
	 *            Scheduler used to schedule the task
	 * @param taskListener
	 *            Listener to be executed
	 */
	public TimerTask(Scheduler scheduler, TimerTaskListener taskListener) {
		this(scheduler, taskListener, 1, 0);
	}

	/**
	 * Constructs a one shot task
	 * 
	 * One shot task is executed after specified delay
	 * 
	 * @param scheduler
	 *            Scheduler used to schedule the task
	 * @param taskListener
	 *            Listener to be executed
	 * @param delay
	 *            Execution delay
	 */
	public TimerTask(Scheduler scheduler, TimerTaskListener taskListener, long delay) {
		this(scheduler, taskListener, delay, 0);
	}

	/**
	 * Constructs periodic task
	 * 
	 * @param scheduler
	 *            Scheduler used to schedule the task
	 * @param taskListener
	 *            Listener to be executed
	 * @param delay
	 *            Initial execution delay
	 * @param period
	 *            Execution period
	 */
	public TimerTask(Scheduler scheduler, TimerTaskListener taskListener, long delay, long period) {
		super(scheduler);

		this.trigger = new TimeTriggerExt();
		this.trigger.setOffset(delay);
		this.trigger.setPeriod(period);
		this.taskListener = taskListener;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.task.Task#invoke(cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger)
	 */
	@Override
	public void invoke(Trigger trigger) throws TaskInvocationException {
		taskListener.at(scheduler.getTimer().getCurrentMilliseconds(), this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.task.Task#registerTriggers()
	 */
	@Override
	protected void registerTriggers() {
		/**
		 * There are no triggers as it is assumed that publishing occurs purely periodically.
		 */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.task.Task#unregisterTriggers()
	 */
	@Override
	protected void unregisterTriggers() {
		/**
		 * There are no triggers as it is assumed that publishing occurs purely periodically.
		 */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.task.Task#getPeriodicTrigger()
	 */
	@Override
	public TimeTrigger getTimeTrigger() {
		return trigger;
	}

	/**
	 * Schedules this task to be executed
	 */
	public void schedule() {
		scheduler.addTask(this);
	}

	/**
	 * Remove task from scheduler
	 */
	public void unSchedule() {
		scheduler.removeTask(this);
	}
}
