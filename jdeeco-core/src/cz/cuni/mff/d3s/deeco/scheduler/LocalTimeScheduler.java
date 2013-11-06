package cz.cuni.mff.d3s.deeco.scheduler;


import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cz.cuni.mff.d3s.deeco.executor.Executor;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PeriodicTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.task.Task;
import cz.cuni.mff.d3s.deeco.task.TaskTriggerListener;

/**
 * Implementation of the Scheduler as LocalTimeScheduler
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 * @author Andranik Muradyan <muradian@d3s.mff.cuni.cz>
 *
 */
public class LocalTimeScheduler implements Scheduler, TaskTriggerListener {
	Map<Task, TaskInfo> tasks;
	Executor executor;
	private States state;
	
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
	
	private enum States{
		RUNNING,
		FAILED,
		STOPPED
	}
	
	public LocalTimeScheduler( ){		
		tasks = new HashMap<>();
	}
	
	@Override
	public void executionCompleted(Task task) {
		tasks.get(task).state = States.STOPPED; 
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
	 * @throws NullPointerException when {@code task} is not in the {@link #tasks}. 
	 * @param task
	 */
	protected void taskTriggerFired(final Task task, Trigger trigger) {
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
	public void executionFailed(Task task, Exception e) {
		executionCompleted(task);
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

