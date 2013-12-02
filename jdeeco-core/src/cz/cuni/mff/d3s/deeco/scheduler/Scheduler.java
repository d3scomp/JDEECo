package cz.cuni.mff.d3s.deeco.scheduler;

import cz.cuni.mff.d3s.deeco.executor.ExecutionListener;
import cz.cuni.mff.d3s.deeco.executor.Executor;
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

public interface Scheduler extends ExecutionListener {
	/**
	 * Starts the scheduler thus triggering a sequence of operations on serving tasks in queue(if any). 
	 * <p>
	 * After any implementation of scheduler is instantiated, it is by default stopped.
	 * Even though it allows adding/removing tasks, none of them will be handled/executed
	 * properly until the scheduler is started explicitly
	 * <p>
	 * <b>Note:</b> This method is strongly implementation-based so it is advised to check with the documentation
	 * specific for each Scheduler implementation. 
	 */
	public void start();
	
	/**
	 * Stops the scheduler by triggering it into an idle state. While new tasks may still be added and old ones 
	 * removed from the queue none of them will be served according to their scheduling plan. After the scheduler
	 * is started again it will serve the new queue. 
	 * <p>
	 * <b>Note:</b> This method is strongly implementation-based so it is advised to check with the documentation
	 * specific for each Scheduler implementation. 
	 */
	public void stop();
	
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
	 * Notifies the scheduler that the task has been completed with an error.
	 * <p>
	 * This method is identical to {@link cz.cuni.mff.d3s.deeco.scheduler.Scheduler#executionCompleted <code>executionCompleted</code>},
	 * however the difference is that this method is used to signal that task execution completed with an error. 
	 * It also passes the exception which interrupted execution flow of the task.  
	 * @param task the task whose execution exited with failure
	 * @param e the exception, which interrupted that execution
	 * 
	 * */
	public void executionFailed(Task task, Exception e);
	
	/**
	 * Notifies the scheduler that the task has been completed.
	 * <p>
	 * After the executed task is finished, this method is called on the scheduler by the executor to notify that the task
	 * was executed successfully and needs further handling by scheduler.
	 * Now scheduler may process the task further whether by rescheduling it or by leaving it until another trigger received.
	 * @param task the task whose execution exited with failure
	 * @see 	{@link cz.cuni.mff.d3s.deeco.scheduler.Scheduler#executionFailed executionFailed}
	 * */
	public void executionCompleted( Task task );
	
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
	
	void invokeAndWait(Runnable doRun) throws InterruptedException;}
