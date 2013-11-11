package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeChangeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;

/**
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public class Synchronizer implements TriggerListener { 
	
	private static final KnowledgeChangeTrigger EMPTY;

	static {
		RuntimeMetadataFactory factory = RuntimeMetadataFactory.eINSTANCE;
		EMPTY = factory.createKnowledgeChangeTrigger();
		EMPTY.setKnowledgePath(factory.createKnowledgePath());
	}
	
	private final KnowledgeManager local;
	private final KnowledgeManager replica;

	public Synchronizer(KnowledgeManager local, KnowledgeManager replica) {
		super();
		this.local = local;
		this.replica = replica;
		// Register yourself as a listener for any change in the local
		// knowledge manager
		local.register(EMPTY, this);
	}

	@Override
	public void triggered(Trigger trigger) {
		// Update replica
		try {
			List<KnowledgePath> changedPath = new LinkedList<>();
			// TODO This should be optimised to update only some part of the
			// knowledge
			changedPath.add(EMPTY.getKnowledgePath());
			ValueSet changedValue = local.get(changedPath);
			replica.update(toUpdateChangeSet(changedValue));
		} catch (KnowledgeNotFoundException knfe) {
			Log.e("Synchronizer error", knfe);
		}
	}

	@Override
	public boolean equals(Object that) {
		if (that != null && that instanceof Synchronizer) {
			Synchronizer thatSyncer = (Synchronizer) that;
			return thatSyncer.local.equals(local)
					&& thatSyncer.replica.equals(replica);
		}
		return false;
	}
	
	public void unregister() {
		local.unregister(EMPTY, this);
	}

	private ChangeSet toUpdateChangeSet(ValueSet valueSet) {
		if (valueSet == null)
			return null;
		ChangeSet result = new ChangeSet();
		for (KnowledgePath kp : valueSet.getKnowledgePaths())
			result.setValue(kp, valueSet.getValue(kp));
		return result;
	}
}
