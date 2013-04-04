package cz.cuni.mff.d3s.deeco.performance;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableComponentProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcess;
import cz.cuni.mff.d3s.deeco.scheduling.ProcessPeriodicSchedule;
import cz.cuni.mff.d3s.deeco.scheduling.ProcessTriggeredSchedule;

public interface SchedulableProcessVisitor {

	public void visit(SchedulableComponentProcess sc);
	public void visit(SchedulableEnsembleProcess se);

}
