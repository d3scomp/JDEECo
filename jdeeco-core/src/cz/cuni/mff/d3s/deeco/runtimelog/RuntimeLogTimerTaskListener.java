package cz.cuni.mff.d3s.deeco.runtimelog;

import java.io.IOException;

import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.task.CustomStepTask;
import cz.cuni.mff.d3s.deeco.task.TimerTask;
import cz.cuni.mff.d3s.deeco.task.TimerTaskListener;

public class RuntimeLogTimerTaskListener implements TimerTaskListener 
{
	private SnapshotProvider snapshotProvider;
	private long period;
	
	public RuntimeLogTimerTaskListener(SnapshotProvider snapshotProvider, long period)
	{
		this.snapshotProvider = snapshotProvider;
		this.period = period;
	}
	
	
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

	@Override
	public TimerTask getInitialTask(Scheduler scheduler) 
	{
		return new CustomStepTask(scheduler, this, period);
	}

	
	
}