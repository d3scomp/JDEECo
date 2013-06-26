package cz.cuni.mff.d3s.deeco.performance;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class KnowledgeInfo {

	protected String knowPath;
	protected Object currentValue;
	protected TimeStamp valueCurrentTimeStamp = new TimeStamp();
	protected TimeStamp valueCreationTimeStamp = new TimeStamp();
	protected Writer writer=new Writer();
	protected List<Reader> readers=new ArrayList<Reader>();
	
	
	public void setKnowPath(String knowPath) {
		this.knowPath = knowPath;
	}
	
	public String getKnowPath(){
		return knowPath;
	}
	
	public void setCurrentValue(Object currentValue) {
		this.currentValue = currentValue;
	}
	
	public Object getCurrentValue() {
		return currentValue;
	}
	
	public void setValueCurrentTimeStamp(TimeStamp valueCurrentTimeStamp) {
		this.valueCurrentTimeStamp = valueCurrentTimeStamp;
	}
	
	public TimeStamp getValueCurrentTimeStamp() {
		return valueCurrentTimeStamp;
	}
	
	public void setValueCreationTimeStamp(TimeStamp valueCreationTimeStamp) {
		this.valueCreationTimeStamp = valueCreationTimeStamp;
	}
	
	public TimeStamp getValueCreationTimeStamp(){
		return valueCreationTimeStamp;
	}
	
	public void setWriter(Writer writer) {
		this.writer = writer;
	}
	
	public Writer getWriter(){
		return writer;
	}
	
	public void setReaders(List<Reader> readers) {
		this.readers = readers;
	}
	
	public List<Reader> getReaders(){
		return readers;
	}
	
	public void addReader(Reader reader){
		this.readers.add(reader);
	}
	
	public void setReader(int index, Reader reader){
		this.readers.set(index, reader);
	}
	
	public TimeStamp getLastWritingTimeStamp(){
		long max=-1;
		TimeStamp maxTime = new TimeStamp();
		Set<TimeStamp> times = getWriter().getWriterHistory().keySet();
		for (TimeStamp time : times) {
			if( max < time.finish ){
				max=time.finish;
				maxTime=time;
			}
		}
		return  maxTime;
	}
	
	public long getOldness(){
		long result = getLastWritingTimeStamp().finish-getValueCreationTimeStamp().finish;
		if(result < 0)
			result=-1;
		return result;
	}
	
}
