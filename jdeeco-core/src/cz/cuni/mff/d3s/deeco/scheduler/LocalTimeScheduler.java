package cz.cuni.mff.d3s.deeco.scheduler;


import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cz.cuni.mff.d3s.deeco.executor.Executor;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PeriodicTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.task.Task;
import cz.cuni.mff.d3s.deeco.task.TaskTriggerListener;

/**
 * Implementation of the Scheduler as LocalTimeScheduler
 * <p>
 * This class holds one of the implementations of scheduler called LocalTimeScheduler.
 * It can be though of as the simplest of schedulers that simply contains a set of 
 * tasks mapped to a data structure containing information about them. As a timing mechanism
 * java {@link Timer} class was used and tasks are being scheduled periodically by means of 
 * java {@link TimerTask} class.
 * 
 * @author Jaroslav Keznikl 	<keznikl@d3s.mff.cuni.cz>
 * @author Andranik Muradyan 	<muradian@d3s.mff.cuni.cz>
 *
 */
public class LocalTimeScheduler implements Scheduler, TaskTriggerListener {
	private Map<Task, TaskInfo> tasks = new HashMap<>();
	private Executor executor;
	private States state;
	
	/**
	 * A helper class that carries all valuable information 
	 * about a task and their scheduling.
	 */
	private class TaskInfo{
		Timer timer;
		States state;
		PeriodicTrigger periodicTrigger;
		
		public TaskInfo(PeriodicTrigger periodicTrigger){
			timer = new Timer();
			state = States.STOPPED;
			this.periodicTrigger = periodicTrigger;
		}
	}
	
	// Possible state enumeration used both by the scheduler and the task
	private enum States{
		RUNNING,
		FAILED,
		STOPPED
	}
	

	@Override
	public void executionCompleted(Task task) {
		tasks.get(task).state = States.STOPPED; 
	}
	
	@Override
	public void executionFailed(Task task, Exception e) {
		Log.e(e.getMessage());
		executionCompleted(task);
	}
	
	@Override
	public synchronized void start () {
		if( state == States.RUNNING )
			return;
		
		for(Task task: tasks.keySet()){			
			startTask(task);
		}
		
		state = States.RUNNING;
	}

	@Override
	public synchronized void stop() {
		if( state == States.STOPPED )
			return;
		
		if(tasks != null && tasks.size() > 0)
			for (Task t : tasks.keySet()) {
				stopTask(t);
			}
		
		state = States.STOPPED;
	}
	
	@Override
	public synchronized void addTask(Task task) {
		if( task == null || tasks.containsKey(task) )
			return;
		
		tasks.put(task, new TaskInfo(task.getPeriodicTrigger()));
		
		if( state == States.RUNNING )
			startTask(task);
	}

	@Override
	public synchronized void removeTask(Task task) {
		if( tasks.containsKey(task) ){
			stopTask(task);
			tasks.remove(task);
		}
	}
	
	private void startTask(final Task task) {
		if( task == null )
			return;
				
		TaskInfo ti = tasks.get(task);
		if( ti != null && ti.state != States.RUNNING ){
			task.setTriggerListener(this);
			
			taskTimerReset(task, ti);
		}
	}
	
	private void stopTask(final Task task) {
		if( task == null )
			return;
		
		TaskInfo ti = tasks.get(task);
		
		if( ti != null && ti.state != States.RUNNING ){
			task.unsetTriggerListener();
			
			
			ti.timer.cancel();
			ti.timer = new Timer();
			ti.state = States.STOPPED;
		}
	}
	
	/**
	 * Restarts the timer and executes the task.
	 * NOT thread safe!!!<br/>
	 * 
	 * @throws 	NullPointerException when {@code task} is not in the {@link #tasks}
	 * @param task		the task whose trigger fired
	 * @param trigger	the trigger that fired
	 */
	void taskTriggerFired(final Task task, Trigger trigger) {
		if( state == States.STOPPED )
			return;

		if( task == null )
			return;

		TaskInfo ti = tasks.get(task);
		if( ti == null || ti.state == States.RUNNING ){
			return;
		}

		ti.timer.cancel();
		ti.timer = new Timer();

		taskTimerReset(task, ti);

		ti.state = States.RUNNING;
		executor.execute(task, trigger);			
	}

	/**
	 * This function is used for reseting the scheduling plan for the task.
	 * Method does it by scheduling the task once again with the same period
	 * starting from now.
	 * 
	 * @param task	that may be rescheduled 
	 * @param ti	contains information about the task
	 */
	private void taskTimerReset(final Task task, TaskInfo ti) {
		if( ti.periodicTrigger != null ){			
			ti.timer.scheduleAtFixedRate(new TimerTask() {
				
				@Override
				public void run() {
					taskTimerFired(task);				
				}
			}, 0, ti.periodicTrigger.getPeriod()); 
		}
	}
	
	/**
	 * Method executes the task when the timer for that task is fired.
	 * 
	 * @param task the task to be executed
	 */
	protected void taskTimerFired(Task task) {
		if( task == null )
			return;

		TaskInfo ti = tasks.get(task);

		if( ti.state == States.RUNNING ){
			return;
		}
		
		ti.state = States.RUNNING;
		executor.execute(task, ti.periodicTrigger);
	}

	@Override
	public void setExecutor(Executor executor) {
		this.executor = executor;		
	}

	@Override
	public void triggered(Task task, Trigger trigger) {
		taskTriggerFired(task, trigger);
	}
}

