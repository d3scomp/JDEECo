package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;

/**
 * Container for a set of changes to a component's knowledge.
 * 
 * <p>
 * Contains a set of deleted {@link KnowledgeReference}s and values
 * corresponding to a set of added/updated {@link KnowledgeReference}s.
 * </p>
 * 
 * <p>
 * Implemented as a map reference->value, where a special EMPTY value indicates
 * that the reference is to be deleted
 * </p>
 * 
 * <p>
 * TODO: maybe the class would need a separate list of references to be added
 * (i.e., to be added, to be updated, and to be deleted) so that we can enforce
 * some knowledge update rules (i.e., update is possible only at some places).
 * </p>
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 * 
 */

public class ChangeSet {
	/**
	 * Singleton indicating a reference to be deleted.
	 */
	private enum KnowledgeValue { EMPTY } 
	
	/**
	 * The reference->value map.
	 */
	private Map<KnowledgePath, Object> values = new HashMap<>();
	
	
	/**
	 * Returns all added/updated references.
	 */
	public Collection<KnowledgePath> getUpdatedReferences() {
		/*
		 * The added/updated references are those not being assigned the value
		 * KnowledgeValue.EMPTY
		 */
		Collection<KnowledgePath> ret = new HashSet<>();
		for (Entry<KnowledgePath, Object> e: values.entrySet()) {
			if (e.getValue() != KnowledgeValue.EMPTY)
				ret.add(e.getKey());
		}
		return ret;	
	}
	
	/**
	 * Returns all deleted references.
	 */
	public Collection<KnowledgePath> getDeletedReferences() {
		/*
		 * The deleted references are those that are assigned the value
		 * KnowledgeValue.EMPTY
		 */
		Collection<KnowledgePath> ret = new HashSet<>();
		for (Entry<KnowledgePath, Object> e: values.entrySet()) {
			if (e.getValue() == KnowledgeValue.EMPTY)
				ret.add(e.getKey());
		}
		return ret;
	}
	
	/**
	 * Returns the value for the {@code reference}.
	 * <p>
	 * Returns null if the reference is not included in the {@link ChangeSet} or
	 * if the reference is deleted. Otherwise returns the added/updated value.
	 * </p>
	 */
	public Object getValue(KnowledgePath reference) {
		if (!values.containsKey(reference))
			return null;
		
		Object ret = values.get(reference);
		if (ret == KnowledgeValue.EMPTY)
			return null;
		else 
			return ret;
	}
	
	/**
	 * Sets the {@code value} for the {@code reference}.
	 * 
	 * Overrides previous calls of {@link #setDeleted(KnowledgeReference)} and
	 * {@link #setValue(KnowledgeReference, Object)}.
	 */
	public void setValue(KnowledgePath reference, Object value) {
		values.put(reference, value);		
	}
	
	/**
	 * Marks the {@code reference} as deleted.
	 * 
	 * Overrides previous calls of {@link #setDeleted(KnowledgeReference)} and
	 * {@link #setValue(KnowledgeReference, Object)}.
	 */
	public void setDeleted(KnowledgePath reference) {
		values.put(reference, KnowledgeValue.EMPTY);	
	}
}
