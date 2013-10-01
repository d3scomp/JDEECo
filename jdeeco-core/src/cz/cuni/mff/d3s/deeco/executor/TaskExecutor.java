package cz.cuni.mff.d3s.deeco.executor;

import cz.cuni.mff.d3s.deeco.scheduling.Task;

public interface TaskExecutor {
	void submitTask(Task task);
}
