package cz.cuni.mff.d3s.deeco.performance;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import cz.cuni.mff.d3s.deeco.knowledge.ISession;

public class PeriodicEnsembleInfo implements IPerformanceInfo {

	public long startPeriodsCoord; // oc=sys-startc;
	public long startPeriodsMem; // om=sys-startm;
	public long R;
	public ArrayList<TimeStamp> runningPeriodsCoord=new  ArrayList<TimeStamp>();//<release, tsco, tfco>
	public ArrayList<TimeStamp> runningPeriodsMem=new  ArrayList<TimeStamp>();//<release, tsmem, tfmem>
	
	

}
