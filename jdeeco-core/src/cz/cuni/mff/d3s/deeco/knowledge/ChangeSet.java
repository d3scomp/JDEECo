package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.Collection;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeSet.KnowledgeValue;

public class ChangeSet {
	
	private KnowledgeSet ks;
	
	public ChangeSet() {
		ks = new KnowledgeSet();
	}
	
	ChangeSet(KnowledgeSet ks) {
		this.ks = ks;
	}
			
	public Collection<KnowledgeReference> getUpdatedReferences() {
		return ks.getNonEmptyReferences();
	}
	
	public Collection<KnowledgeReference> getDeletedReferences() {
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
	
	public void setDeleted(KnowledgeReference reference) {
		ks.setValue(reference, KnowledgeValue.EMPTY);		
	}
}
