package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 * 
 *         TODO: maybe the class would need a separate list of references to be
 *         added (i.e., to be added, to be updated, and to be deleted) so that
 *         we can enforce some knowledge update rules (i.e., update is possible
 *         only at some places)
 * 
 */

public class ChangeSet {
	
	private enum KnowledgeValue { EMPTY } 
	
	private Map<KnowledgeReference, Object> values = new HashMap<>();
	
				
	public Collection<KnowledgeReference> getUpdatedReferences() {
		Collection<KnowledgeReference> ret = new HashSet<>();
		for (Entry<KnowledgeReference, Object> e: values.entrySet()) {
			if (e.getValue() != KnowledgeValue.EMPTY)
				ret.add(e.getKey());
		}
		return ret;	
	}
	
	public Collection<KnowledgeReference> getDeletedReferences() {
		Collection<KnowledgeReference> ret = new HashSet<>();
		for (Entry<KnowledgeReference, Object> e: values.entrySet()) {
			if (e.getValue() == KnowledgeValue.EMPTY)
				ret.add(e.getKey());
		}
		return ret;
	}
	
	public Object getValue(KnowledgeReference reference) {
		Object ret = values.get(reference);
		if (ret == KnowledgeValue.EMPTY)
			return null;
		else 
			return ret;
	}
	
	public void setValue(KnowledgeReference reference, Object value) {
		values.put(reference, value);		
	}
	
	public void setDeleted(KnowledgeReference reference) {
		values.put(reference, KnowledgeValue.EMPTY);		
	}
}
