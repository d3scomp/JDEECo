package cz.cuni.mff.d3s.deeco.executor;

import cz.cuni.mff.d3s.deeco.task.Task;

//FIXME: The class does not have the header stating the author

public interface Executor {
	void execute(Task task);
	void setExecutionListener(ExecutionListener listener);
}
