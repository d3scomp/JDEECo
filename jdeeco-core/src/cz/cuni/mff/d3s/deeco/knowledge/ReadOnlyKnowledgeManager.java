package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.Collection;

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;

/**
 * This interface allows the user to read the values from KnowledgeSet. Also,
 * the interface allows to bind a trigger to tirggerListener or unbind it. 
 * 
 * It is assumed that {@link KnowledgePath} instances used with this instance are
 * absolute, meaning that they are fully evaluated.
 * 
 * @author Rima Al Ali <alali@d3s.mff.cuni.cz>
 * 
 */
public interface ReadOnlyKnowledgeManager {

	/**
	 * Retrieves values for the collection of the {@link KnowledgePath} objects.
	 * 
	 * @param knowledgeReferenceList
	 * @return {@link ValueSet} object containing values for the specified
	 *         knowledge paths
	 * @throws KnowledgeNotExistentException
	 *             when there is no value for at least one knowledge path
	 */
	public ValueSet get(Collection<KnowledgePath> knowledgeReferenceList)
			throws KnowledgeNotFoundException;

	/**
	 * Registers the specified trigger and its listener within this knowledge
	 * manager.
	 * 
	 * @param trigger
	 *            trigger to be listen for.
	 * @param triggerListener
	 *            listener to be notified in case of triggering event.
	 */
	public void register(Trigger trigger, TriggerListener triggerListener);

	/**
	 * Unregisters the specified trigger and its listener from this knowledge
	 * manager.
	 * 
	 * @param trigger
	 *            trigger to be unregistered.
	 * @param triggerListener
	 *            listener to be unregistered.
	 */
	public void unregister(Trigger trigger, TriggerListener triggerListener);
}
