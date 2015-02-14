package cz.cuni.mff.d3s.deeco.scheduler;

import cz.cuni.mff.d3s.deeco.executor.ExecutionListener;
import cz.cuni.mff.d3s.deeco.executor.Executor;
import cz.cuni.mff.d3s.deeco.scheduler.notifier.SchedulerNotifier;
import cz.cuni.mff.d3s.deeco.scheduler.notifier.SchedulerNotifierEventListener;
import cz.cuni.mff.d3s.deeco.task.Task;

/**
 * Scheduler is the abstract base class for all kinds of schedulers which organize 
 * task execution in a way specific to chosen architecture of the system.
 * <p>
 * Common scheduler interface consists of the following methods
 * <ul>
 * 	<li>{@link cz.cuni.mff.d3s.deeco.scheduler.Scheduler#start <code>start</code>} - Starts the scheduler
 * 	<li>{@link cz.cuni.mff.d3s.deeco.scheduler.Scheduler#stop <code>stop</code>} - Stops the scheduler
 * 	<li>{@link cz.cuni.mff.d3s.deeco.scheduler.Scheduler#addTask <code>addTask</code>} - Adds a task to the scheduler
 * 	<li>{@link cz.cuni.mff.d3s.deeco.scheduler.Scheduler#removeTask <code>removeTask</code>} - Removes a task from the scheduler
 *	<li>{@link cz.cuni.mff.d3s.deeco.scheduler.Scheduler#executionFailed <code>executionFailed</code>} - Notifies the scheduler that task was completed with an error
 * 	<li>{@link cz.cuni.mff.d3s.deeco.scheduler.Scheduler#executionCompleted <code>executionCompleted</code>} - Notifies the scheduler that task was completed 
 * 	<li>{@link cz.cuni.mff.d3s.deeco.scheduler.Scheduler#setExecutor <code>setExecutor</code>} - Assigns an {@link cz.cuni.mff.d3s.deeco.executor.Executor#Executor <code>Executor</code>}
 * 		instance to the scheduler
 * </ul>
 * 
 * @see 	{@link cz.cuni.mff.d3s.deeco.scheduler.LocalTimeScheduler#LocalTimeScheduler LocalTimeScheduler}
 * @see 	{@link cz.cuni.mff.d3s.deeco.scheduler.LocalTimeScheduler#SingleThreadedScheduler SingleThreadedScheduler}
 * 
 * @author 	Andranik Muradyan 	<muradian@d3s.mff.cuni.cz>
 * @author 	Jaroslav Keznikl 	<keznikl@d3s.mff.cuni.cz>
 *
 */

public interface Scheduler extends ExecutionListener, SchedulerNotifierEventListener {
	
	/**
	 * Adds the task to the scheduler. This function does not imply that the task will 
	 * be executed as expected after the addition since stopped scheduler also allows 
	 * task manipulations like adding or removing. 
	 * 
	 * @param 	task the task to be added
	 * @see 	{@link Task}
	 */
	public void addTask( Task task );
	
	/**
	 * Removes the task from the schedulers queue.
	 * <p>
	 * Depending on different types of schedulers this operation may be performed differently
	 * however the idea behind it is that the task is eventually being removed from the scheduler task list  
	 * 
	 * @param 	task the task to be removed
	 * @see 	{@link Task}
	 */
	public void removeTask( Task task );
	
	
	
	/**
	 * Associates an {@link Executor <code>Executor</code>} instance with this scheduler.
	 * <p>
	 * Since the scheduler is responsible not for executing tasks but for when to execute them,
	 * this method binds an executer instance to the scheduler. When a period elapses or a trigger
	 * is fired for a task to be executed, scheduler passes the task to the executor and hears again
	 * about the task from the executor when the execution is completed(by 
	 * {@link cz.cuni.mff.d3s.deeco.scheduler.Scheduler#executionCompleted success} or 
	 * {@link cz.cuni.mff.d3s.deeco.scheduler.Scheduler#executionFailed failure}). 
	 * @param 	executor the executor to be assigned to scheduler
	 * 
	 */
	public void setExecutor(Executor executor);

	public void setSchedulerNotifier(SchedulerNotifier schedulerNotifier); 
	
	public SchedulerNotifier getSchedulerNotifier();

}
