package cz.cuni.mff.d3s.deeco.knowledge.container;

import java.util.Collection;

import cz.cuni.mff.d3s.deeco.annotations.PlaysRole;
import cz.cuni.mff.d3s.deeco.annotations.Role;

/**
 * Knowledge container is useful when operating with multiple component's knowledge. It provides access to
 * the knowledge via roles.
 * 
 * @author Zbyněk Jiráček
 * 
 * @see TrackingKnowledgeContainer
 * @see Role
 * @see PlaysRole
 *
 */
public interface KnowledgeContainer {

	/**
	 * Traverses through all underlying knowledge and for each knowledge implementing the specified
	 * role it returns an instance of the role class. This instance represents a view on the component's
	 * knowledge.
	 * @param roleClass The role class.
	 * @return Instances of the role class for each component that implements the specified role.
	 * @throws KnowledgeContainerException
	 */
	public abstract <TRole> Collection<TRole> getUntrackedKnowledgeForRole(
			Class<TRole> roleClass) throws KnowledgeContainerException;

	/**
	 * Similarly to the {@link #getUntrackedKnowledgeForRole(Class)} it returns role view on the components'
	 * knowledge. Additionally, the returned instances are tracked, which means that if the caller modifies
	 * the returned instances and calls the {@link #commitChanges()} method, the changes will be propagated
	 * into the component knowledge.
	 * @param roleClass The role class.
	 * @return Instances of the role class for each component that implements the specified role.
	 * @throws KnowledgeContainerException
	 *
	 * @see #getUntrackedKnowledgeForRole(Class)
	 * @see #commitChanges()
	 * @see #resetTracking()
	 */
	public abstract <TRole> Collection<TRole> getTrackedKnowledgeForRole(
			Class<TRole> roleClass) throws KnowledgeContainerException;

	/**
	 * Clears the list of tracked role class instances returned by the {@link #getTrackedKnowledgeForRole(Class)} method.
	 * Consequently, changes to the earlier returned role class instances cannot be committed into the knowledge anymore.
	 * 
	 * @see #commitChanges()
	 * @see #getTrackedKnowledgeForRole(Class)
	 */
	public abstract void resetTracking();

	/**
	 * Commits all the changes made to the tracked role class instances returned by the {@link #getTrackedKnowledgeForRole(Class)}
	 * method (if the tracking was not reset by the {@link #resetTracking()} method). All values in the tracked role
	 * class instances are stored into the respective knowledge.
	 * 
	 * @throws KnowledgeContainerException
	 * 
	 * @see #getTrackedKnowledgeForRole(Class)
	 * @see #resetTracking()
	 */
	public abstract void commitChanges() throws KnowledgeContainerException;

}