package cz.cuni.mff.d3s.deeco.performance;

import java.util.HashMap;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableComponentProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;

public class RegisterTimeStamp {
	public HashMap<SchedulableComponentProcess, TimeStamp> processHistoryReader=new HashMap<SchedulableComponentProcess, TimeStamp>();
	public HashMap<SchedulableEnsembleProcess, TimeStamp> ensembleHistoryReader=new HashMap<SchedulableEnsembleProcess, TimeStamp>();
	public HashMap<SchedulableComponentProcess, TimeStamp> processHistoryWriter=new HashMap<SchedulableComponentProcess, TimeStamp>();
	public HashMap<SchedulableEnsembleProcess, TimeStamp> ensembleHistoryWriter=new HashMap<SchedulableEnsembleProcess, TimeStamp>();

}
