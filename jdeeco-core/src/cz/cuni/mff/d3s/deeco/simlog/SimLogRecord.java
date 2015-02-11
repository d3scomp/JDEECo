package cz.cuni.mff.d3s.deeco.simlog;

import java.util.Map;


public interface SimLogRecord // TODO: Why is this an interface, wouldn't it be sufficient to be an object? 
{
	String getEventType();
	String getID();
	Map<String, Object> getValues();
}
