package cz.cuni.mff.d3s.deeco.performance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableComponentProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;

public class Writer {
	
	protected SchedulableProcess writerObject;
	protected HashMap<TimeStamp,Object> writerHistory=new HashMap<TimeStamp,Object>();
	
	
	public void setWriterObject(SchedulableProcess writerObject) {
		this.writerObject = writerObject;
	}

	public SchedulableProcess getWriterObject(){
		return writerObject;
	}

	public void setWriterHistory(HashMap<TimeStamp, Object> writerHistory) {
		this.writerHistory = writerHistory;
	}
	
	public HashMap<TimeStamp, Object> getWriterHistory() {
		return writerHistory;
	}
	
	public void addTimeStampValue(TimeStamp time, Object value){
		this.writerHistory.put(time, value);
	}
	
	public Object getValue(TimeStamp key){
		return this.writerHistory.get(key);
	}
}
