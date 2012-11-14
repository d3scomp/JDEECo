package cz.cuni.mff.d3s.deeco.invokable.creators;

import java.io.Serializable;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;

/**
 * 
 * Class used to create instance of {@link SchedulableProcess}
 *  
 * @author alf
 *
 */
public interface IScheduleableProcessCreator extends Serializable {

	public abstract SchedulableProcess extract(KnowledgeManager km);
	public abstract SchedulableProcess extract(KnowledgeManager km, ClassLoader contextClasslLoader);

}