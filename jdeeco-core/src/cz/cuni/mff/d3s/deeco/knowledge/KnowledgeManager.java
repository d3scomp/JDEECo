package cz.cuni.mff.d3s.deeco.knowledge;
/**
 * This interface allows the user to add, update and read the values from KnowledgeSet. Also, 
 * it allows to bind a trigger to tirggerListener or unbind it.
 * 
 * @author Rima Al Ali <alali@d3s.mff.cuni.cz>
 *
 */

public interface KnowledgeManager extends ReadOnlyKnowledgeManager {

	void update( ChangeSet changeSet);  
}
