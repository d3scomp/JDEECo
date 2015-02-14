package cz.cuni.mff.d3s.deeco.runtime;

import cz.cuni.mff.d3s.deeco.scheduler.notifier.SchedulerNotifier;

public interface SimulationSchedulerNotifier extends SchedulerNotifier {

	public void setTerminationTime(long terminationTime);
}
