package cz.cuni.mff.d3s.deeco.task;

import java.util.List;

import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeReference;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SchedulingSpecification;

public abstract class Task {	
	public abstract ChangeSet invoke(ValueSet values);
	public abstract List<KnowledgeReference> getInputReferences();
	public abstract SchedulingSpecification getSchedulingSpecification();
}
