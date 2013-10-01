package cz.cuni.mff.d3s.deeco.executor;

import cz.cuni.mff.d3s.deeco.scheduling.Task;

public interface ExecutionListener {
	void executionFinished(Task task);
	void executionStarted(Task task);
	void executionException(Task task, Throwable t);
}
