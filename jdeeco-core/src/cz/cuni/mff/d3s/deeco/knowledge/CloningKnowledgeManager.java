package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.Collection;

import com.rits.cloning.Cloner;

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;

/**
 * 
 * A KnowledgeManager version that introduces cloning functionality for stored
 * data immutability.
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public class CloningKnowledgeManager extends BaseKnowledgeManager {

	private final Cloner cloner;
	
	public CloningKnowledgeManager(String id) {
		super(id);
		cloner = new Cloner();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.knowledge.BaseKnowledgeManager#get(java.util.Collection
	 * )
	 */
	@Override
	public ValueSet get(Collection<KnowledgePath> knowledgePaths)
			throws KnowledgeNotFoundException {		
		ValueSet values = super.get(knowledgePaths);
		ValueSet result = new ValueSet();
		// only values need to be cloned (cloning KnowledgePaths in a full model
		// causes a loopback in the cloner)
		for (KnowledgePath p: values.getKnowledgePaths()) {
			if (isLocal(p)) {
				result.setValue(p, values.getValue(p));
			} else {
				result.setValue(p, cloner.deepClone(values.getValue(p)));
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.knowledge.BaseKnowledgeManager#update(cz.cuni.mff
	 * .d3s.deeco.knowledge.ChangeSet)
	 */
	@Override
	public void update(ChangeSet changeSet) throws KnowledgeUpdateException {
		ChangeSet toUpdate = new ChangeSet();
		// only values need to be cloned (cloning KnowledgePaths in a full model
		// causes a loopback in the cloner)
		for (KnowledgePath p: changeSet.getUpdatedReferences()) {
			if (isLocal(p)) {
				toUpdate.setValue(p, changeSet.getValue(p));
			} else {
				toUpdate.setValue(p, cloner.deepClone(changeSet.getValue(p)));
			}
		}
		for (KnowledgePath p: changeSet.getDeletedReferences()) {
			toUpdate.setDeleted(p);
		}
		super.update(toUpdate);
	}
}
