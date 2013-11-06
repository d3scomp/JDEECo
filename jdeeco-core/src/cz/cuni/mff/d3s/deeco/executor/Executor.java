package cz.cuni.mff.d3s.deeco.executor;

import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.task.Task;

//FIXME TB: The class does not have the header stating the author

public interface Executor {
	void execute(Task task, Trigger trigger);
	void setExecutionListener(ExecutionListener listener);
}
