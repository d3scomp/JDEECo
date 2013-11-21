package cz.cuni.mff.d3s.deeco.scheduler;

import cz.cuni.mff.d3s.deeco.executor.ExecutionListener;
import cz.cuni.mff.d3s.deeco.executor.Executor;
import cz.cuni.mff.d3s.deeco.task.Task;


/**
 * Interface Scheduler for LocalTimeScheduler(and others if needed)
 * 
 * @author Andranik Muradyan <muradian@d3s.mff.cuni.cz>
 *
 */
public interface Scheduler extends ExecutionListener {
	public void start();
	public void stop();
	public void addTask( Task task );
	public void removeTask( Task task );
	
	public void executionFailed(Task task, Exception e);
	public void executionCompleted( Task task );
	public void setExecutor(Executor executor);
	
	void invokeAndWait(Runnable doRun) throws InterruptedException;}