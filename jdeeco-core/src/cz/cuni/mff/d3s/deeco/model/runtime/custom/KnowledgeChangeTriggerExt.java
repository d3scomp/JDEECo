package cz.cuni.mff.d3s.deeco.model.runtime.custom;

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeChangeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.impl.KnowledgeChangeTriggerImpl;

/**
 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
 *
 */
public class KnowledgeChangeTriggerExt extends KnowledgeChangeTriggerImpl {

	public KnowledgeChangeTriggerExt() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object that) {
		if (that != null && that instanceof KnowledgeChangeTrigger) {
			return getKnowledgePath().equals(((KnowledgeChangeTrigger)that).getKnowledgePath());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return getKnowledgePath().hashCode();
	}
}
