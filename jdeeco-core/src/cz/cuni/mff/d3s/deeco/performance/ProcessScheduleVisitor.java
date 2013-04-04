package cz.cuni.mff.d3s.deeco.performance;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableComponentProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcess;
import cz.cuni.mff.d3s.deeco.scheduling.ProcessPeriodicSchedule;
import cz.cuni.mff.d3s.deeco.scheduling.ProcessTriggeredSchedule;

public interface ProcessScheduleVisitor {
	
	public void visitEnsemble(ProcessPeriodicSchedule pp, IPerformanceInfo pInfo, TimeStamp time);
	public void visitEnsemble(ProcessTriggeredSchedule pt, IPerformanceInfo pInfo, TimeStamp time);
	public void visitProcess(ProcessPeriodicSchedule pp, IPerformanceInfo pInfo, TimeStamp time);
	public void visitProcess(ProcessTriggeredSchedule pt, IPerformanceInfo pInfo, TimeStamp time);
}
