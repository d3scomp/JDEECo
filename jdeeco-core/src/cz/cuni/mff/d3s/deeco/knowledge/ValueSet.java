package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.Collection;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeSet.KnowledgeValue;

public class ValueSet {
	
	private KnowledgeSet ks;
	
	public ValueSet() {
		ks = new KnowledgeSet();
	}
	
	ValueSet(KnowledgeSet ks) {
		this.ks = ks;
	}
			
	public Collection<KnowledgeReference> getFoundReferences() {
		return ks.getNonEmptyReferences();
	}
	
	public Collection<KnowledgeReference> getNotFoundReferences() {
		return ks.getEmptyReferences();
	}
	
	public Object getValue(KnowledgeReference reference) {
		Object ret = ks.getValue(reference);
		if (ret == KnowledgeValue.EMPTY)
			return null;
		else 
			return ret;
	}
	
	public void setValue(KnowledgeReference reference, Object value) {
		ks.setValue(reference, value);		
	}
	
	public void setNotFound(KnowledgeReference reference) {
		ks.setValue(reference, KnowledgeValue.EMPTY);		
	}
}
