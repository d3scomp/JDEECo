package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.Collection;
import java.util.Map;

class KnowledgeSet {
	enum KnowledgeValue { EMPTY } 
	
	private Map<KnowledgeReference, Object> values;
	
	public Collection<KnowledgeReference> getEmptyReferences() {
		return null;		
	}
	
	public Collection<KnowledgeReference> getNonEmptyReferences() {
		return null;		
	}
	

	
	public void setValue(KnowledgeReference reference, Object value) {
		
	}
	
	public Object getValue(KnowledgeReference reference) {
		return null;
	}
		
	
}
