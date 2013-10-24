package cz.cuni.mff.d3s.deeco.task;

import java.util.List;

import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeReference;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SchedulingSpecification;

public interface Task {
	ChangeSet invoke(ValueSet values);
	List<KnowledgeReference> getInputReferences();
	SchedulingSpecification getSchedulingSpecification();
}
