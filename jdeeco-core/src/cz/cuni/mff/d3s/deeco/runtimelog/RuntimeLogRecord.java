package cz.cuni.mff.d3s.deeco.runtimelog;

import java.util.Map;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;

/**
 * This class defines a record that can be logged using the {@link RuntimeLogger}. 
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public abstract class RuntimeLogRecord 
{
	/**
	 * The identifier of the {@link ComponentInstance} that logs the record.
	 * (e.g. "network", "firefighter", ...)
	 */
	private String id;
	/**
	 * The data to be logged. The data are in the <em>field-value</em> form.
	 * The <em>field</em> represents the name of the components field and the <em>value</em>
	 * its value.
	 */
	protected Map<String, Object> recordValues;
	
	/**
	 * Constructs an instance of the {@link RuntimeLogRecord} with the given values.
	 * @param eventType is the type of the event to be logged.
	 * @param id is the identifier of the {@link ComponentInstance}
	 * that is creating the record.
	 * @param values contains the <em>field names</em> and their <em>values</em> to be logged.
	 */
	public RuntimeLogRecord(String id, Map<String, Object> values)
	{
		this.id = id;
		this.recordValues = values;
	}
	
	/**
	 * Provides the {@link RuntimeLogRecord#id}.
	 * @return The {@link RuntimeLogRecord#id}.
	 */
	public String getId()
	{
		return id;
	}
	
	/**
	 * Provides the {@link RuntimeLogRecord#recordValues}.
	 * @return The {@link RuntimeLogRecord#id}.
	 */
	public Map<String, Object> getValues()
	{
		return recordValues;
	}
}
