package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

class KnowledgeSet {
	enum KnowledgeValue { EMPTY } 
	
	private Map<KnowledgeReference, Object> values = new HashMap<>();
	
	public Collection<KnowledgeReference> getEmptyReferences() {
		Collection<KnowledgeReference> ret = new HashSet<>();
		for (Entry<KnowledgeReference, Object> e: values.entrySet()) {
			if (e.getValue() == KnowledgeValue.EMPTY)
				ret.add(e.getKey());
		}
		return ret;
	}
	
	public Collection<KnowledgeReference> getNonEmptyReferences() {
		Collection<KnowledgeReference> ret = new HashSet<>();
		for (Entry<KnowledgeReference, Object> e: values.entrySet()) {
			if (e.getValue() != KnowledgeValue.EMPTY)
				ret.add(e.getKey());
		}
		return ret;	
	}
	

	
	public void setValue(KnowledgeReference reference, Object value) {
		values.put(reference, value);
	}
	
	public Object getValue(KnowledgeReference reference) {
		return values.get(reference);
	}
		
	
}
