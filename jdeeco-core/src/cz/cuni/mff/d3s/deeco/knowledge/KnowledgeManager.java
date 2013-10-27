package cz.cuni.mff.d3s.deeco.knowledge;
/**
 * This class allows the user to deal with KnowledgeSet from three perspectives:
 * 1- reading the values from the KnowledgeSet. ( from ReadOnlyKnowledgeManager )
 * 2- associating the registers with their tirggerListeners. ( from ReadOnlyKnowledgeManager )
 * 3- adding new value or updating the existing ones.
 * 
 * @author Rima Al Ali <alali@d3s.mff.cuni.cz>
 *
 */

public interface KnowledgeManager extends ReadOnlyKnowledgeManager {

	void update( ChangeSet changeSet);  
}
