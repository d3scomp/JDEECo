/**
 */
package cz.cuni.mff.d3s.deeco.scheduler;

import java.util.List;

import cz.cuni.mff.d3s.deeco.executor.ExecutionListener;
import cz.cuni.mff.d3s.deeco.task.Task;

public interface Scheduler extends ExecutionListener {
	public void start();
	public void stop();
	public void addTask( Task task );
	public void addTasks( List<Task> tasks ); 
	public void removeTask( Task task );
	public void removeTasks( List<Task> tasks );
}