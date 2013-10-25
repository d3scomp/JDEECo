package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.Collection;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeSet.KnowledgeValue;

public class ValueSet {
	
	// FIXME: Doesn't have to be a knowledge set, can be just a hashmap. Then KnowledgeSet can be merged with ChangeSet because it does not serve for any other purpose.
	private KnowledgeSet ks;
	
	public ValueSet() {
		ks = new KnowledgeSet();
	}
	
	ValueSet(KnowledgeSet ks) {
		this.ks = ks;
	}
			
	// FIXME: Better would be getReferences
	public Collection<KnowledgeReference> getFoundReferences() {
		return ks.getNonEmptyReferences();
	}
	
	// FIXME: We actually don't need this. When it's not there, it is just not there.
	public Collection<KnowledgeReference> getNotFoundReferences() {
		return ks.getEmptyReferences();
	}
	
	public Object getValue(KnowledgeReference reference) {
		Object ret = ks.getValue(reference);
		// FIXME: Should not get to a value set, assert would be better
		if (ret == KnowledgeValue.EMPTY)
			return null;
		else 
			return ret;
	}
	
	public void setValue(KnowledgeReference reference, Object value) {
		ks.setValue(reference, value);		
	}
	
	// FIXME: We actually don't need this. When it's not there, it is just not there.
	public void setNotFound(KnowledgeReference reference) {
		ks.setValue(reference, KnowledgeValue.EMPTY);		
	}
}
