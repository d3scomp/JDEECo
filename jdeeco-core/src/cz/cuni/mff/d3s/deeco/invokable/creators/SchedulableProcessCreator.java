package cz.cuni.mff.d3s.deeco.invokable.creators;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.scheduling.ProcessSchedule;

/**
 * 
 * Class used to create instance of {@link SchedulableProcess}
 *  
 * @author alf
 *
 */
public abstract class SchedulableProcessCreator implements IScheduleableProcessCreator {

	private static final long serialVersionUID = -4306879355222355124L;

	protected final ProcessSchedule scheduling;

	
	public SchedulableProcessCreator(ProcessSchedule scheduling) {
		super();
		this.scheduling = scheduling;
	}


	public SchedulableProcess extract(KnowledgeManager km) {
		return extract(km, null);
	}
	
	public abstract SchedulableProcess extract(KnowledgeManager km, ClassLoader contextClassLoaader);
}
