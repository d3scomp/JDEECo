package cz.cuni.mff.d3s.deeco.simlog;

import java.util.Map;

import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.task.CustomStepTask;
import cz.cuni.mff.d3s.deeco.task.TimerTask;
import cz.cuni.mff.d3s.deeco.task.TimerTaskListener;

public class SimLogTimerTaskListener implements TimerTaskListener 
{
	private SnapshotProvider snapshotProvider;
	private long period;
	
	public SimLogTimerTaskListener(SnapshotProvider snapshotProvider, long period)
	{
		this.snapshotProvider = snapshotProvider;
		this.period = period;
	}
	
	
	@Override
	public void at(long time, Object triger) 
	{
		Map<String, Object> snapshot = snapshotProvider.getSnapshot();
		// SimLogger.logSnapshot(snapshot); // TODO: find out whether the potential IO exception should be suppressed or expressed some other way  
		
		CustomStepTask task = (CustomStepTask) triger;
		task.scheduleNextExecutionAfter(period);

	}

	@Override
	public TimerTask getInitialTask(Scheduler scheduler) 
	{
		return new CustomStepTask(scheduler, this, period);
	}

	
	
}