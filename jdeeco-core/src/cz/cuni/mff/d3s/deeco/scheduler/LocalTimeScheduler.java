package cz.cuni.mff.d3s.deeco.scheduler;


import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cz.cuni.mff.d3s.deeco.executor.Executor;
import cz.cuni.mff.d3s.deeco.task.Task;
import cz.cuni.mff.d3s.deeco.task.TaskTriggerListener;

/**
 * Implementation of the Scheduler as LocalTimeScheduler
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 * @author Andranik Muradyan <muradian@d3s.mff.cuni.cz>
 *
 */
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
		
		tasks.put(task, new TaskInfo());
		
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
			task.setTriggerListener(new TaskTriggerListener() {
				@Override
				public void triggered(Task task) {
					taskTriggerFired(task);
				}
			});
			
			if( task.getSchedulingPeriod() > 0){
				ti.timer.scheduleAtFixedRate(new TimerTask() {
					
					@Override
					public void run() {
						if( task != null )
							taskTimerFired(task);				
					}
				}, 0, task.getSchedulingPeriod()); // FIXME: TB: What about if scheduling period == 0, which probably means that we do not schedule periodically?
			}
		}
	}
	
	private void stopTask(final Task task) {
		if( task == null )
			return;
		
		TaskInfo ti = tasks.get(task);
		
		if( ti != null && ti.state != States.RUNNING ){
			task.setTriggerListener(null);
			
			if( task.getSchedulingPeriod() > 0){
				ti.timer.scheduleAtFixedRate(new TimerTask() {
					
					@Override
					public void run() {
						taskTimerFired(task);				
					}
				}, 0, task.getSchedulingPeriod()); // FIXME: TB: What about if scheduling period == 0, which probably means that we do not schedule periodically?
			}
			
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
	protected void taskTriggerFired(final Task task) {
		if( state == States.RUNNING && task != null && tasks.containsKey(task)){
			if( tasks.get(task).state == States.RUNNING ){
				// TODO : Implement error reporting
				return;
			}

			TaskInfo ti = tasks.get(task);
			ti.timer.cancel();
			ti.timer = new Timer();

			if( task.getSchedulingPeriod() > 0){			
				ti.timer.scheduleAtFixedRate(new TimerTask() {
					
					@Override
					public void run() {
						taskTimerFired(task);				
					}
				}, 0, task.getSchedulingPeriod()); // FIXME: What about if scheduling period == 0, which probably means that we do not schedule periodically?
			}
			
			ti.state = States.RUNNING;
			executor.execute(task);			
		}
	}
	
	protected void taskTimerFired(Task task) {
		if( task == null )
			return;
		
		if( tasks.get(task).state == States.RUNNING ){
			// TODO : Implement error reporting
			return;
		}
		
		tasks.get(task).state = States.RUNNING;
		executor.execute(task);
	}

	@Override
	public void executionFailed(Task task, Exception e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setExecutor(Executor executor) {
		this.executor = executor;		
	}
}

