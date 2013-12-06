package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;

/**
 * Container for knowledge values corresponding to a set of {@link KnowledgeReference}s.
 * 
 * Allows storing {@code null} values.
 *  
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 *
 */
public class ValueSet {
	
	
	private Map<KnowledgePath, Object> values = new HashMap<>();
	
	/**
	 * Returns the list of {@link KnowledgeReference}s for which the container contains a value.
	 */
	public Collection<KnowledgePath> getKnowledgePaths() {
		return values.keySet();
	}
	
	/**
	 * Returns the value for the {@code reference}.
	 * 
	 * If there is no value for the reference, returns {@code null}. Note that
	 * the value for a reference can also be {@code null}, thus it is necessary
	 * to check {@link #getKnowledgePaths()} to distinguish between references
	 * with/without a value.
	 */
	public Object getValue(KnowledgePath path) {
		Object result = null;
		if (values.containsKey(path))
			result = values.get(path);
		return result;
	}
	
	/**
	 * Sets the {@code value} for the {@code reference}.
	 */
	public void setValue(KnowledgePath path, Object value) {		
		values.put(path, value);		
	}	
}
