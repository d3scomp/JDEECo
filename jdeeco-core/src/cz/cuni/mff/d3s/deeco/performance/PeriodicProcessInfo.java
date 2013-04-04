package cz.cuni.mff.d3s.deeco.performance;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import cz.cuni.mff.d3s.deeco.knowledge.ISession;


public class PeriodicProcessInfo implements IPerformanceInfo {
	 public long startPeriods; // o=sys-start;  // System.nanoSeconds();
	 public long R;
	 public ArrayList<TimeStamp> runningPeriods=new ArrayList<TimeStamp>(); 
	
	

}
