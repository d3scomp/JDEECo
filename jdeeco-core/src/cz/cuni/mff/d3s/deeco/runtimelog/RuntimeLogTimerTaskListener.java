package cz.cuni.mff.d3s.deeco.runtimelog;

import java.io.IOException;

import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.task.CustomStepTask;
import cz.cuni.mff.d3s.deeco.task.Task;
import cz.cuni.mff.d3s.deeco.task.TimerTask;
import cz.cuni.mff.d3s.deeco.task.TimerTaskListener;

/**
 * The {@link TimerTaskListener} for the {@link SnapshotProvider}s registered
 * on {@link RuntimeLogger}. The {@link RuntimeLogTimerTaskListener} invokes
 * the {@link RuntimeLogger#logSnapshot(RuntimeLogRecord)} method with the
 * snapshot provided by a {@link SnapshotProvider}, when being fired
 * by the {@link Scheduler}. After processing the snapshot the
 * {@link Task} corresponding to the
 * {@link TimerTaskListener} is being planned again for the new period cycle.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public class RuntimeLogTimerTaskListener implements TimerTaskListener 
{
	/**
	 * The {@link SnapshotProvider} for which the {@link TimerTaskListener} instance
	 * is being fired by the {@link Scheduler}.
	 */
	private SnapshotProvider snapshotProvider;
	/**
	 * The period of the {@link Task} that handles the {@link TimerTaskListener} instance.
	 */
	private long period;
	
	/**
	 * Constructs an instance of the {@link RuntimeLogTimerTaskListener} for the
	 * given <em>snapshotProvider</em> with the given <em>period</em>.
	 * @param snapshotProvider is the {@link SnapshotProvider} that will be periodically asked for a snapshot.
	 * @param period specifies the period in which will be the <em>snapshotProvider</em> invoked.
	 */
	public RuntimeLogTimerTaskListener(SnapshotProvider snapshotProvider, long period)
	{
		this.snapshotProvider = snapshotProvider;
		this.period = period;
	}
	
	/**
	 * Logs the snapshot from the {@link RuntimeLogTimerTaskListener#snapshotProvider}
	 * using the {@link RuntimeLogger}. After the snapshot is processed the {@link Task}
	 * is planned for the next period.
	 */
	@Override
	public void at(long time, Object triger)
	{
		try
		{
			RuntimeLogRecord snapshot = snapshotProvider.getSnapshot();
			RuntimeLogger.logSnapshot(snapshot);
		}
		catch(IOException e)
		{
			// If the runtime logging fails interrupt the simulation
			throw new RuntimeException(e);
		}
		
		CustomStepTask task = (CustomStepTask) triger;
		task.scheduleNextExecutionAfter(period);

	}

	/**
	 * Provides the {@link CustomStepTask} for the {@link TimerTaskListener} instance
	 * that will be fired with the defined period.
	 */
	@Override
	public TimerTask getInitialTask(Scheduler scheduler) 
	{
		return new CustomStepTask(scheduler, this, period);
	}

	
	
}