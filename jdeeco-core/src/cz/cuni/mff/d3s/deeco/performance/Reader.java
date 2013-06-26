package cz.cuni.mff.d3s.deeco.performance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;

public class Reader {
	
	protected SchedulableProcess readerObject;
	protected HashMap<TimeStamp,Object> readerHistory=new HashMap<TimeStamp,Object>();
	
	
	public SchedulableProcess getReaderObject() {
		return readerObject;
	}
	
	public void setReaderObject(SchedulableProcess readerObject) {
		this.readerObject = readerObject;
	}
	
	public HashMap<TimeStamp, Object> getReaderTimeStamps() {
		return readerHistory;
	}
	
	public void setReaderTimeStamps(HashMap<TimeStamp, Object> readerTimeStamps) {
		this.readerHistory = readerTimeStamps;
	}
	
	public void addTimeStampValue(TimeStamp time, Object value){
		readerHistory.put(time, value);
	}
	
}
