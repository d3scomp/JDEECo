package cz.cuni.mff.d3s.deeco.performance;

import java.util.HashMap;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableComponentProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;

public class KnowledgeInfo {

	public Object knowPath;
	public Object value;
	public int oldness;
	public boolean visited;

	public RegisterTimeStamp registerTimeStamp=new RegisterTimeStamp();
	public RegisterPerformanceInfo registerPerformanceInfo=new RegisterPerformanceInfo();
}
