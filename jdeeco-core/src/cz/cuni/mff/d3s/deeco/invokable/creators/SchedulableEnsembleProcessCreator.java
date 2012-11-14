package cz.cuni.mff.d3s.deeco.invokable.creators;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcess;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.scheduling.ProcessSchedule;

/**
 * 
 * Class used to create instance of {@link SchedulableEnsembleProcess}
 *  
 * @author alf
 *
 */
public class SchedulableEnsembleProcessCreator extends
		SchedulableProcessCreator {

	private static final long serialVersionUID = -2140094601232508615L;

	protected final ParametrizedMethodCreator mapper;
	protected final MembershipCreator membership;

	
	public SchedulableEnsembleProcessCreator(ProcessSchedule scheduling,
			ParametrizedMethodCreator mapper, MembershipCreator membership) {
		super(scheduling);
		this.mapper = mapper;
		this.membership = membership;
	}


	@Override
	public SchedulableEnsembleProcess extract(KnowledgeManager km, ClassLoader contextClassLoader) {
		return new SchedulableEnsembleProcess(km, scheduling, membership.extract(), mapper.extract(), contextClassLoader);
	}

}
