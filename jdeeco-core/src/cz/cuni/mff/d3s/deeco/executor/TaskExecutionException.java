package cz.cuni.mff.d3s.deeco.executor;

import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.runtime.DEECoRuntimeException;
import cz.cuni.mff.d3s.deeco.task.Task;

/**
 * Exception indicating a failure in task execution
 */
public class TaskExecutionException extends DEECoRuntimeException {
	private static final long serialVersionUID = 1L;

	public TaskExecutionException(Task task, Trigger trigger, Throwable e) {
		super(String.format("Task %s execution failed", task == null?"unknown":task.getClass().getName()), e);
	}
}
