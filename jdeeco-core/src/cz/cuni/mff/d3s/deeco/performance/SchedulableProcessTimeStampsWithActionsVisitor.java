package cz.cuni.mff.d3s.deeco.performance;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableComponentProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcess;
import cz.cuni.mff.d3s.deeco.scheduling.ProcessPeriodicSchedule;
import cz.cuni.mff.d3s.deeco.scheduling.ProcessTriggeredSchedule;

public class SchedulableProcessTimeStampsWithActionsVisitor implements ProcessScheduleVisitor {

	@Override
	public void visitEnsemble(ProcessPeriodicSchedule pp, IPerformanceInfo pInfo,
			TimeStamp time) {
		// TODO Auto-generated method stub
		PeriodicEnsembleInfo v = (PeriodicEnsembleInfo)pInfo;
		v.runningPeriodsCoord.add(time);
		v.runningPeriodsMem.add(time);
		System.out.println("time stamp ensemble : "+this+"  release :"+time.release+" start:"+time.start+"  finish:"+time.finish);
	}

	
	@Override
	public void visitEnsemble(ProcessTriggeredSchedule pt,
			IPerformanceInfo pInfo, TimeStamp time) {
		// TODO Auto-generated method stub
		TriggeredEnsembleInfo v = (TriggeredEnsembleInfo)pInfo;
		v.timeStampsCoord.add(time);
		v.timeStampsMem.add(time);
		System.out.println("time stamp ensemble : "+this+"  release :"+time.release+" start:"+time.start+"  finish:"+time.finish);
	}

	@Override
	public void visitProcess(ProcessPeriodicSchedule pp,
			IPerformanceInfo pInfo, TimeStamp time) {
		// TODO Auto-generated method stub
		PeriodicProcessInfo v = (PeriodicProcessInfo)pInfo;
		((PeriodicProcessInfo)pInfo).runningPeriods.add(time);
		System.out.println("time stamp process:"+this+"  release :"+time.release+" start:"+time.start+"  finish:"+time.finish);
	}

	@Override
	public void visitProcess(ProcessTriggeredSchedule pt,
			IPerformanceInfo pInfo, TimeStamp time) {
		// TODO Auto-generated method stub
		TriggeredProcessInfo v = (TriggeredProcessInfo)pInfo;
		((TriggeredProcessInfo)pInfo).timeStamps.add(time);
		System.out.println("time stamp process:"+this+"  release :"+time.release+" start:"+time.start+"  finish:"+time.finish);
	}	


}
