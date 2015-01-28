package cz.cuni.mff.d3s.deeco.knowledge;

import java.io.Serializable;
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
@SuppressWarnings("serial")
public class ValueSet implements Serializable {
	
	
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

	/**
	 * Removes the value associated with this knowledgePath
	 * @param kp
	 */
	public void remove(KnowledgePath kp) {
		values.remove(kp);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((values == null) ? 0 : values.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ValueSet other = (ValueSet) obj;
		if (values == null) {
			if (other.values != null)
				return false;
		} else if (!values.equals(other.values))
			return false;
		return true;
	}	
	
	public String toString() {
		return values.toString();
	}
}
