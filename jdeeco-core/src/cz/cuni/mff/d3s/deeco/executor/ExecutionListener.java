package cz.cuni.mff.d3s.deeco.executor;

import cz.cuni.mff.d3s.deeco.task.Task;

//FIXME TB: The class does not have the header stating the author

public interface ExecutionListener {
	public void executionCompleted( Task task );
	public void executionFailed( Task task, Exception e );
}
