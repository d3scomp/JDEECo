/**
 */
package cz.cuni.mff.d3s.deeco.scheduler;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


import cz.cuni.mff.d3s.deeco.executor.Executor;
import cz.cuni.mff.d3s.deeco.task.Task;

public class LocalTimeScheduler implements Scheduler{
	Map<Task, TaskInfo> tasks;
	Executor executor;
	private States state;
	
	private class TaskInfo{
		Timer timer;
		States state;
		
		public TaskInfo(){
			timer = new Timer();
			state = States.STOPPED;
		}		
	}
	
	private enum States{
		RUNNING,
		STOPPED
	}
	
	public LocalTimeScheduler( Executor executor ){
		this.executor = executor;
		tasks = new HashMap<>();
	}
	
	@Override
	public void executionCompleted(Task task) {
		tasks.get(task).state = States.STOPPED; 
	}

	@Override
	public void start() {
		if( state == States.RUNNING )
			return;
		
		
	}

	@Override
	public void stop() {
		if( state == States.STOPPED )
			return;		
	}
	
	@Override
	public void removeTasks( List<Task> tasks ){
//		tasks.removeAll(tasks);
	}
	
	@Override
	public void addTasks( List<Task> tasks ){
		
	}
	
	@Override
	public void addTask(Task task) {
		tasks.put(task, new TaskInfo());
	}

	@Override
	public void removeTask(Task task) {
		tasks.remove(task);
	}
	
	private void startTask(final Task task) {
		TaskInfo ti = tasks.get(task);
		ti.timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				taskTimerFired(task);				
			}
		}, 0, task.getSchedulingSpecification().getPeriod());
	}
	
	protected void taskTimerFired(Task task) {
		if( tasks.get(task).state == States.RUNNING ){
			// TODO : Implement error reporting
			return;
		}
		
		tasks.get(task).state = States.RUNNING;
		executor.execute(task);
	}
}
