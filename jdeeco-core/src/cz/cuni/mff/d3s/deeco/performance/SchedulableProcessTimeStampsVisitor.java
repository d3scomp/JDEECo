package cz.cuni.mff.d3s.deeco.performance;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableComponentProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcess;
import cz.cuni.mff.d3s.deeco.scheduling.ProcessPeriodicSchedule;

public class SchedulableProcessTimeStampsVisitor implements SchedulableProcessVisitor {

	@Override
	public void visit(SchedulableComponentProcess sc) {
		// TODO Auto-generated method stub
		PeriodicProcessInfo v = (PeriodicProcessInfo)(sc.pInfo);
		ProcessPeriodicSchedule n = (ProcessPeriodicSchedule)(sc.scheduling);
		v.R=(((ProcessPeriodicSchedule)(sc.scheduling)).interval)*1000000000;
		v.startPeriods=System.nanoTime();
		System.out.println("process :  " + ((PeriodicProcessInfo)(sc.pInfo)).startPeriods);
	}

	@Override
	public void visit(SchedulableEnsembleProcess se) {
		// TODO Auto-generated method stub
		PeriodicEnsembleInfo v = (PeriodicEnsembleInfo) (se.pInfo);
		ProcessPeriodicSchedule n = (ProcessPeriodicSchedule)(se.scheduling);
		v.R=(n.interval)*1000000000;
		v.startPeriodsCoord=System.nanoTime();
		v.startPeriodsMem=System.nanoTime();
		System.out.println("ensemble :  co " + v.startPeriodsCoord+ "  mem "+v.startPeriodsMem);

		
	}

}
