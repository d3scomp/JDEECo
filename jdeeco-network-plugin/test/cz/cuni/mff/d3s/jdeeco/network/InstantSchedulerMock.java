package cz.cuni.mff.d3s.jdeeco.network;

import cz.cuni.mff.d3s.deeco.executor.Executor;
import cz.cuni.mff.d3s.deeco.executor.TaskExecutionException;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.task.Task;
import cz.cuni.mff.d3s.deeco.task.TaskInvocationException;
import cz.cuni.mff.d3s.deeco.timer.Timer;
import cz.cuni.mff.d3s.deeco.timer.TimerEventListener;

/**
 * Mock scheduler used to instantly execute every scheduled task
 * 
 * Time is constant zero
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class InstantSchedulerMock implements Scheduler {
	@Override
	public void at(long time) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void executionFailed(Task task, Trigger trigger, Exception e) {
		throw new TaskExecutionException(task, trigger, e);
	}

	@Override
	public void executionCompleted(Task task, Trigger trigger) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setExecutor(Executor executor) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeTask(Task task) {
	}

	@Override
	public Timer getTimer() {
		return new Timer() {
			@Override
			public long getCurrentMilliseconds() {
				return 0;
			}

			@Override
			public void notifyAt(long time, TimerEventListener listener, String eventName, DEECoContainer node) {
				throw new UnsupportedOperationException();
			}

			@Override
			public void interruptionEvent(TimerEventListener listener, String eventName,
					DEECoContainer node) {
				throw new UnsupportedOperationException("Fake timer does not support interruptionEvent");
			}

			@Override
			public void addShutdownListener(ShutdownListener listener) {
				throw new UnsupportedOperationException("Fake timer does not support shutdown");
			}

			@Override
			public void addStartupListener(StartupListener listener) {
				throw new UnsupportedOperationException("Fake timer does not support startup");
			}
		};
	}

	@Override
	public void addTask(Task task) {
		// Instantly execute task
		try {
			task.invoke(task.getTimeTrigger());
		} catch (TaskInvocationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void hibernateTask(Task task) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deHibernateTask(Task task) {
		throw new UnsupportedOperationException();
	}
}
