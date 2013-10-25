package cz.cuni.mff.d3s.deeco.task;

import java.util.List;

import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeReference;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SchedulingSpecification;

public class ProcessTask extends Task {

	public ProcessTask(SchedulingSpecification schedulingSpecification) {
		super(schedulingSpecification);
	}

	@Override
	public ChangeSet invoke(ValueSet values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<KnowledgeReference> getInputReferences() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerTriggers(TriggerListener triggerListener) {
		// TODO Auto-generated method stub

	}
}
