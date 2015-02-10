package cz.cuni.mff.d3s.deeco.runtimelog;

import java.util.Map;


public class RuntimeLogRecord 
{
	private String eventType;
	private String componentID;
	private Map<String, Object> recordValues;
	
	public RuntimeLogRecord(String eventType, String componentID, Map<String, Object> values)
	{
		this.eventType = eventType;
		this.componentID = componentID;
		this.recordValues = values;
	}
	
	public String getEventType()
	{
		return eventType;
	}
	
	public String getComponentID()
	{
		return componentID;
	}
	
	public Map<String, Object> getValues()
	{
		return recordValues;
	}
}
