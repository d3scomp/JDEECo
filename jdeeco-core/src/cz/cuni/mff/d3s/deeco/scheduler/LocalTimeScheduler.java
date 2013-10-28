/**
 */
package cz.cuni.mff.d3s.deeco.scheduler;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;


import cz.cuni.mff.d3s.deeco.executor.Executor;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.task.Task;
import cz.cuni.mff.d3s.deeco.task.NotificationsForScheduler;

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
		
		Iterator<Entry<Task, TaskInfo>> it = tasks.entrySet().iterator();
		
		for(Task task: tasks.keySet()){			
			startTask(task);
		}
		
		state = States.RUNNING;
	}

	@Override
	public synchronized void stop() {
		if( state == States.STOPPED )
			return;
		
		state = States.STOPPED;
	}
	
	@Override
	public synchronized void removeTasks( List<Task> tasks ){
		for (Task task : tasks) {
			removeTask(task);
		}
	}
	
	@Override
	public synchronized void addTasks( List<Task> tasks ){
		for (Task task : tasks) {
			startTask(task);
		}
	}
	
	@Override
	public synchronized void addTask(Task task) {
		if( !tasks.containsKey(task) )
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
		TaskInfo ti = tasks.get(task);
		task.setSchedulingNotificationTarget(new NotificationsForScheduler() { // FIXME: TB: It is an overkill to instantiate this for each task since the task now passes itself as a parameter.
			@Override
			public void triggered(Task task) {
				taskTriggerFired(task);
			}
		});
		
		ti.timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				taskTimerFired(task);				
			}
		}, 0, task.getSchedulingPeriod()); // FIXME: TB: What about if scheduling period == 0, which probably means that we do not schedule periodically?
		
	}
	
	private void stopTask(final Task task) {
		TaskInfo ti = tasks.get(task);
		ti.timer.cancel();
		ti.timer = new Timer();
		ti.state = States.STOPPED;
	}
	
	/**
	 * Restarts the timer and executes the task.
	 * NOT thread safe!!!<br/>
	 * @throws NullPointerException when {@code task} is not in the {@link #tasks}. 
	 * @param task
	 */
	protected void taskTriggerFired(final Task task) {
		if( tasks.get(task).state == States.RUNNING ){
			// TODO : Implement error reporting
			return;
		}

		TaskInfo ti = tasks.get(task);
		ti.timer.cancel();
		ti.timer = new Timer();

		ti.timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				taskTimerFired(task);				
			}
		}, 0, task.getSchedulingPeriod()); // FIXME: What about if scheduling period == 0, which probably means that we do not schedule periodically?
		
		ti.state = States.RUNNING;
		executor.execute(task);
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
