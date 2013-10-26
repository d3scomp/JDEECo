package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.Collection;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeSet.KnowledgeValue;

// TODO: maybe the class would need a separate of references to be added (i.e., to be added, to be updated, and to be deleted)
// so that we can enforce some knowledge update rules (i.e., update is possible only at some places)
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
