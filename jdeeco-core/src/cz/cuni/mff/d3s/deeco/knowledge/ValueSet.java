package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ValueSet {
	
	
	private Map<KnowledgeReference, Object> values = new HashMap<>();
	
			
	public Collection<KnowledgeReference> getReferences() {
		return values.keySet();
	}
	
	
	public Object getValue(KnowledgeReference reference) {	
		if (values.containsKey(reference))
			return values.get(reference);
		else
			return null;
	}
	
	public void setValue(KnowledgeReference reference, Object value) {		
		values.put(reference, value);		
	}	
}
