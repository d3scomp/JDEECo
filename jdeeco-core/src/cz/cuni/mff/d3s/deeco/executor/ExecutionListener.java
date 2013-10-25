package cz.cuni.mff.d3s.deeco.executor;

import cz.cuni.mff.d3s.deeco.task.Task;

public interface ExecutionListener {
	public void executionCompleted( Task task );
}
