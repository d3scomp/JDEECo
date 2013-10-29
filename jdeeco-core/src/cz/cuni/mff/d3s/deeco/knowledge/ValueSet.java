package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Container for knowledge values corresponding to a set of {@link KnowledgeReference}s.
 * 
 * Allows storing {@code null} references.
 *  
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 *
 */
public class ValueSet {
	
	
	private Map<KnowledgeReference, Object> values = new HashMap<>();
	
	/**
	 * Returns the list of {@link KnowledgeReference}s for which the container contains a value.
	 */
	public Collection<KnowledgeReference> getReferences() {
		return values.keySet();
	}
	
	/**
	 * Returns the value for the {@code reference}.
	 * 
	 * If there is no value for the reference, returns {@code null}. Note that
	 * the value for a reference can also be {@code null}, thus it is necessary
	 * to check {@link #getReferences()} to distinguish between references
	 * with/without a value.
	 */
	public Object getValue(KnowledgeReference reference) {	
		if (values.containsKey(reference))
			return values.get(reference);
		else
			return null;
	}
	
	/**
	 * Sets the {@code value} for the {@code reference}.
	 */
	public void setValue(KnowledgeReference reference, Object value) {		
		values.put(reference, value);		
	}	
}
