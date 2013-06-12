package cz.cuni.mff.d3s.deeco.performance;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import cz.cuni.mff.d3s.deeco.knowledge.ISession;

public class TriggeredEnsembleInfo implements IPerformanceInfo {

	public long D;
	public ArrayList<TimeStamp> timeStampsCoord=new  ArrayList<TimeStamp>();//<thco,<tsco,tfco>>
	public ArrayList<TimeStamp> timeStampsMem=new  ArrayList<TimeStamp>();//<thmem,<tsmem,tfmem>>
	

}
