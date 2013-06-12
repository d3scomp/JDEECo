package cz.cuni.mff.d3s.deeco.performance;

import java.util.HashMap;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableComponentProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;

public class RegisterPerformanceInfo {
	//change to determined Info? (periodic or triggered)
	public HashMap<SchedulableComponentProcess,IPerformanceInfo> processWriter=new HashMap<SchedulableComponentProcess,IPerformanceInfo>();
	public HashMap<SchedulableEnsembleProcess,IPerformanceInfo> ensembleWriter=new HashMap<SchedulableEnsembleProcess,IPerformanceInfo>();
	public HashMap<SchedulableComponentProcess,IPerformanceInfo> processReader=new HashMap<SchedulableComponentProcess,IPerformanceInfo>();
	public HashMap<SchedulableEnsembleProcess,IPerformanceInfo> ensembleReader=new HashMap<SchedulableEnsembleProcess,IPerformanceInfo>();
}
