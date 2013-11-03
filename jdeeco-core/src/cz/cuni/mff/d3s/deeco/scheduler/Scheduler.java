/**
 */
package cz.cuni.mff.d3s.deeco.scheduler;

import java.util.List;

import cz.cuni.mff.d3s.deeco.executor.ExecutionListener;
import cz.cuni.mff.d3s.deeco.executor.Executor;
import cz.cuni.mff.d3s.deeco.task.Task;


//FIXME TB: The class does not have the header stating the author
// test text

public interface Scheduler extends ExecutionListener {
	public void start();
	public void stop();
	public void addTask( Task task );
	public void removeTask( Task task );
	
	void setExecutor(Executor executor);
}