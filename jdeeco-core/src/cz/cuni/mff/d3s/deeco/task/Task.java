package cz.cuni.mff.d3s.deeco.task;

import java.util.List;

import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeReference;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SchedulingSpecification;

/**
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 * 
 */
public abstract class Task {
	
	SchedulingSpecification schedulingSpecification;

	public Task(SchedulingSpecification schedulingSpecification){
		this.schedulingSpecification = schedulingSpecification;
	}
		
	public abstract ChangeSet invoke(ValueSet values);

	public abstract List<KnowledgeReference> getInputReferences();

	public SchedulingSpecification getSchedulingSpecification(){
		return schedulingSpecification;
	}

	public abstract void registerTriggers(TriggerListener triggerListener);
}
