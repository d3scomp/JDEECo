package cz.cuni.mff.d3s.deeco.runtimelog;

import java.util.Map;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;

/**
 * This class defines a record that can be logged using the {@link RuntimeLogger}. 
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public class RuntimeLogRecord 
{
	/**
	 * The type of the event being logged. There are no constraints on the types of event.
	 */
	private String eventType;
	/**
	 * The identifier of the {@link ComponentInstance}
	 * that logs the record.
	 */
	private String componentID;
	/**
	 * The data to be logged. The data are in the <em>field-value</em> form.
	 * The <em>field</em> represents the name of the components field and the <em>value</em>
	 * its value.
	 */
	private Map<String, Object> recordValues;
	
	/**
	 * Constructs an instance of the {@link RuntimeLogRecord} with the given values.
	 * @param eventType is the type of the event to be logged.
	 * @param componentID is the identifier of the {@link ComponentInstance}
	 * that is creating the record.
	 * @param values contains the <em>field names</em> and their <em>values</em> to be logged.
	 */
	public RuntimeLogRecord(String eventType, String componentID, Map<String, Object> values)
	{
		this.eventType = eventType;
		this.componentID = componentID;
		this.recordValues = values;
	}
	
	/**
	 * Provides the {@link RuntimeLogRecord#eventType}.
	 * @return The  {@link RuntimeLogRecord#eventType}.
	 */
	public String getEventType()
	{
		return eventType;
	}
	
	/**
	 * Provides the {@link RuntimeLogRecord#componentID}.
	 * @return The {@link RuntimeLogRecord#componentID}.
	 */
	public String getComponentID()
	{
		return componentID;
	}
	
	/**
	 * Provides the {@link RuntimeLogRecord#recordValues}.
	 * @return The {@link RuntimeLogRecord#componentID}.
	 */
	public Map<String, Object> getValues()
	{
		return recordValues;
	}
}
