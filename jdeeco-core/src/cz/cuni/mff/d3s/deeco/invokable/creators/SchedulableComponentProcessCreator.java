package cz.cuni.mff.d3s.deeco.invokable.creators;

import cz.cuni.mff.d3s.deeco.annotations.ELockingMode;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableComponentProcess;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.scheduling.ProcessSchedule;

/**
 * 
 * Class used to create instance of {@link SchedulableComponentProcess}
 *  
 * @author alf
 *
 */
public class SchedulableComponentProcessCreator extends
		SchedulableProcessCreator {

	private static final long serialVersionUID = -3125146627152989834L;

	protected final ParametrizedMethodCreator process;
	protected final ELockingMode lockingMode;
	protected final String componentId;
	
	

	public SchedulableComponentProcessCreator(ProcessSchedule scheduling,
			ParametrizedMethodCreator process, ELockingMode lockingMode, String componentId) {
		super(scheduling);
		this.process = process;
		this.lockingMode = lockingMode;
		this.componentId = componentId;
	}



	@Override
	public SchedulableComponentProcess extract(KnowledgeManager km, ClassLoader contextClassLoader) {
		return new SchedulableComponentProcess(km, scheduling, process.extract(), lockingMode, componentId, contextClassLoader);
	}

}
