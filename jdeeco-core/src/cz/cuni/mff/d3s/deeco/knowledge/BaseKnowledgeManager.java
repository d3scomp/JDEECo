package cz.cuni.mff.d3s.deeco.knowledge;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeChangeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeSecurityTag;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNode;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeComponentId;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;

// TB: XXX - This would really benefit from being re-implemented using trie datastructure

/**
 * This class implements the KnowledgeManager interface. It allows the user to
 * add, update and read the values from KnowledgeSet. Also, the class allows to
 * bind a trigger to tirggerListener or unbind it.
 * 
 * @author Rima Al Ali <alali@d3s.mff.cuni.cz>
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
@SuppressWarnings("unchecked")
public class BaseKnowledgeManager implements KnowledgeManager {

	private final Map<KnowledgePath, Object> knowledge;
	private final Map<KnowledgePath, List<KnowledgeSecurityTag>> securityTags;
	private final Map<KnowledgeChangeTrigger, List<TriggerListener>> knowledgeChangeListeners;
	private final Collection<KnowledgePath> localKnowledgePaths;
	
	protected final ComponentInstance component;
	private final String id;

	public BaseKnowledgeManager(String id, ComponentInstance component) {
		this.id = id;
		this.component = component;
		this.knowledge = new HashMap<>();
		this.knowledgeChangeListeners = new HashMap<>();
		this.localKnowledgePaths = new LinkedList<>();
		this.securityTags = new HashMap<>();
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public ComponentInstance getComponent() {
		return component;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager#get(java.util
	 * .Collection)
	 */
	@Override
	public ValueSet get(final Collection<KnowledgePath> knowledgePaths)
			throws KnowledgeNotFoundException {
		final ValueSet result = new ValueSet();
		Object value;
		for (KnowledgePath kp : knowledgePaths) {
			try {				
				value = getKnowledge(kp.getNodes());
			} catch (KnowledgeNotFoundException knfe) {
				throw new KnowledgeNotFoundException(kp);
			}
			if (knowledge.equals(value))
				for (KnowledgePath rootKP : knowledge.keySet())
					result.setValue(rootKP, knowledge.get(rootKP));
			else
				result.setValue(kp, value);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager#register(cz.
	 * cuni.mff.d3s.deeco.model.runtime.api.Trigger,
	 * cz.cuni.mff.d3s.deeco.knowledge.TriggerListener)
	 */
	@Override
	public void register(Trigger trigger,
			TriggerListener triggerListener) {
		if (trigger instanceof KnowledgeChangeTrigger) {
			final KnowledgeChangeTrigger kct = (KnowledgeChangeTrigger) trigger;
			List<TriggerListener> listeners;
			if (knowledgeChangeListeners.containsKey(kct)) {
				listeners = knowledgeChangeListeners.get(kct);
			} else {
				listeners = new LinkedList<>();
				knowledgeChangeListeners.put(kct, listeners);
			}
			listeners.add(triggerListener);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager#unregister(cz
	 * .cuni.mff.d3s.deeco.model.runtime.api.Trigger,
	 * cz.cuni.mff.d3s.deeco.knowledge.TriggerListener)
	 */
	@Override
	public void unregister(Trigger trigger,
			TriggerListener triggerListener) {
		if (trigger instanceof KnowledgeChangeTrigger) {
			final KnowledgeChangeTrigger kct = (KnowledgeChangeTrigger) trigger;
			if (knowledgeChangeListeners.containsKey(kct)) {
				knowledgeChangeListeners.get(kct).remove(triggerListener);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager#update(cz.cuni.mff.d3s
	 * .deeco.knowledge.ChangeSet)
	 */
	@Override
	public void update(final ChangeSet changeSet) throws KnowledgeUpdateException {
		final Map<KnowledgePath, Object> updated = new HashMap<>();
		final List<KnowledgePath> added = new LinkedList<>();
		
		Object original = null;
		try {
			boolean exists;
			for (final KnowledgePath updateKP : changeSet
					.getUpdatedReferences()) {
				exists = true;
				try {
					original = getKnowledge(updateKP.getNodes());
				} catch (KnowledgeNotFoundException e) {
					// This means that our update will try to add a new entry to
					// the knowledge
					exists = false;
				}
				updateKnowledge(updateKP, changeSet.getValue(updateKP));
				// We need to preserve the state of the knowledge, in order to
				// revert it back in case of problems
				if (exists) {
					updated.put(updateKP, original);
				} else {
					added.add(updateKP);
				}
			}
		} catch (KnowledgeUpdateException e) {
			// Revert changes
			revert(updated, added);
			// Throw the exception
			throw e;
		}
		// Delete
		// First we need to check if the delete of all requested paths can be
		// done without problems
		KnowledgePath invalidDelete = null;
		for (final KnowledgePath deleteKP : changeSet.getDeletedReferences()) {
			if (!isValidDeletePath(deleteKP)) {
				invalidDelete = deleteKP;
				break;
			}
		}
		// If so, then we delete all the relevant entries from the knowledge
		if (invalidDelete == null) {
			deleteKnowledge(changeSet.getDeletedReferences());
		} else {
			// Otherwise, we need to:
			// Revert changes
			revert(updated, added);
			// Notify about invalid update
			throw new KnowledgeUpdateException(
					"Update exception - Failed to delete " + invalidDelete);
		}
		// Now afeter doing the update and delete - if no exception has been
		// thrown, we need to notify the listeners about updates done.
		for (final KnowledgePath knowledgePath : changeSet
				.getUpdatedReferences()) {
			notifyKnowledgeChangeListeners(knowledgePath);
		}
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object that) {
		//FIXME: this does not seem right (equals does not consider the contents)
		boolean result = false;
		if (that instanceof BaseKnowledgeManager) {
			result = ((BaseKnowledgeManager) that).id
					.equals(id);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */	
	@Override
	public int hashCode() {		
		return id.hashCode();
	}

	/**
	 * Retrieves data from knowledge for a single path.
	 * 
	 * @param knowledgePath
	 *            path to the requested data
	 * @return referenced data
	 * @throws KnowledgeNotFoundException
	 *             thrown when the requested data cannot be found
	 */
	protected Object getKnowledge(final List<PathNode> knowledgePath)
			throws KnowledgeNotFoundException {
		assert knowledgePath != null;
		Object result = null;
		// If the path points to root knowledge, then we should return the
		// reference knowledge
		if (knowledgePath.isEmpty()) {
			result = knowledge;
		} else {
		// handle ID separately
		if ((knowledgePath.size() == 1) && (knowledgePath.get(0) instanceof PathNodeComponentId))
			return id;
			// Otherwise we should go through each knowledge entry, find the
			// partial path matching and try to retrieve the data from the
			// matched node in the knowledge
			boolean wasFound = false;
			for (final KnowledgePath kPath : knowledge.keySet()) {
				try {
					List<PathNode> kPathNodes = kPath.getNodes();
					
					if (startsWith(knowledgePath, kPathNodes)) {
						result = getKnowledgeFromNode(knowledgePath.subList(
								kPathNodes.size(), knowledgePath.size()),
								knowledge.get(kPath));
						wasFound = true;
						break;
						
					}
				} catch (KnowledgeNotFoundException knfe) {
					continue;
				}
			}
			if (!wasFound) {
				throw new KnowledgeNotFoundException();
			}
		}
		return result;
	}

	// TB: FIXME - This method is supposedly buggy. Assume that someone puts a.b.c and then tries to put a.b 
	/**
	 * Updates current knowledge entry with the specified value. It adds new
	 * knowledge entries in case of maps and lists as well as entries, which
	 * knowledge path do not overlap in any way with the existing paths in the
	 * knowledge.
	 * 
	 * @param knowledgePath
	 *            path to the knowledge that needs to be updated.
	 * @param value
	 *            new value of the knowledge entry.
	 * @throws KnowledgeUpdateException
	 *             thrown when the update failed.
	 */
	protected void updateKnowledge(final KnowledgePath knowledgePath,
			final Object value) throws KnowledgeUpdateException {
		if (noPathOverlapWithAny(knowledgePath, knowledge.keySet())) {
			knowledge.put(knowledgePath, value);
		} else {
			final List<PathNode> pathNodesToParent = new LinkedList<>(
					knowledgePath.getNodes());
			final String fieldName = ((PathNodeField) pathNodesToParent
					.remove(pathNodesToParent.size() - 1)).getName();
			Object parent = null;
			try {
				parent = getKnowledge(pathNodesToParent);
			} catch (KnowledgeNotFoundException e) {
				throw new KnowledgeUpdateException(
						"Forbidden update: knowledge does not exist - "
								+ knowledgePath);
			}
			if (parent instanceof List<?>) {
				((List<Object>) parent).set(Integer.parseInt(fieldName), value);
			} else if (parent instanceof Map<?, ?>) {
				if (parent.equals(knowledge)) {
					if (knowledge.containsKey(knowledgePath)) {
						knowledge.put(knowledgePath, value);
					} else {
						throw new KnowledgeUpdateException(
								"Forbidden update: path overlaps - "
										+ knowledgePath);
					}
				} else {
					((Map<String, Object>) parent).put(fieldName, value);
				}
			} else {
				try {
					final Field field = parent.getClass().getField(fieldName);
					field.set(parent, value);
				} catch (final NoSuchFieldException | IllegalAccessException e) {
					throw new KnowledgeUpdateException("Forbidden update: "
							+ e.getMessage() + " - " + knowledgePath);
				}
			}
		}
	}

	/**
	 * Checks whether the given knowledge path overlaps in any way with any of
	 * path from the collection given in the parameter lists.
	 * 
	 * @param path
	 *            knowledge path to be checked
	 * @param paths
	 *            collection of knowledge paths
	 * @return true in case there is no overlap or false otherwise.
	 */
	private boolean noPathOverlapWithAny(final KnowledgePath path,
			final Collection<KnowledgePath> paths) {
		boolean result = true;
		for (final KnowledgePath p : paths) {
			if (startsWith(p.getNodes(), path.getNodes())
					|| startsWith(path.getNodes(), p.getNodes())) {
				result = false;
				break;
			}
		}
		return result;
	}

	/**
	 * Checks whether the knowledge path can be considered for deletion.
	 * 
	 * @param knowledgePath
	 *            the path considered for deletion
	 * @return true in case the we try to delete one of the (first level)
	 *         knowledge entry, map entry or list entry
	 */
	private boolean isValidDeletePath(final KnowledgePath knowledgePath) {
		boolean result = false;
		// If the path refers to one of the first level in the knowledge
		if (knowledge.containsKey(knowledgePath)) {
			result = true;
		} else {
			// Otherwise, get the path to the parent (owner) of the object being
			// deleted.
			final List<PathNode> pathNodesToParent = new LinkedList<>(
					knowledgePath.getNodes());
			final String fieldName = ((PathNodeField) pathNodesToParent
					.remove(pathNodesToParent.size() - 1)).getName();
			Object parent = null;
			try {
				parent = getKnowledge(pathNodesToParent);
				// And check if it is a Map or a List and if it consists element
				// being deleted
				if (parent instanceof Map) {
					result = ((Map<String, ?>) parent).containsKey(fieldName);
				} else if (parent instanceof List<?>) {
					result = ((List<?>) parent).size() > Integer
							.parseInt(fieldName);
				}
			} catch (KnowledgeNotFoundException e) {
				result = false;
			}
		}
		return result;
	}

	/**
	 * Retrieves data for the single knowledgePath from the particular knowledge
	 * node
	 * 
	 * @param knowledgePath
	 *            path to data
	 * @param node
	 *            knowledge node
	 * @return requested data
	 * @throws KnowledgeNotFoundException
	 *             thrown when the requested data does not exist
	 */
	private Object getKnowledgeFromNode(final List<PathNode> knowledgePath,
			final Object node) throws KnowledgeNotFoundException {
		Object currentObject = node;
		Field currentField;
		String fieldName = null;
		try {
			// For each path node in the knowledge path, we need to go deeper
			// into object structure using reflection
			for (final PathNode pn : knowledgePath) {
				try {
					fieldName = ((PathNodeField) pn).getName();
					currentField = currentObject.getClass().getField(fieldName);
					currentObject = currentField.get(currentObject);
				} catch (NoSuchFieldException nfe) {
					// Object does not have such a field. We need to consider
					// maps and lists indexed by the current path node.
					if (currentObject instanceof List<?>) {
						currentObject = ((List<?>) currentObject).get(Integer
								.parseInt(fieldName));
					} else if (currentObject instanceof Map<?, ?>) {
						final Map<String, Object> currentObjectAsMap = (Map<String, Object>) currentObject;
						if (currentObjectAsMap.containsKey(fieldName))
							currentObject = currentObjectAsMap.get(fieldName);
						else
							throw new KnowledgeNotFoundException();
					} else {
						throw new KnowledgeNotFoundException();
					}
				}
			}
		} catch (final IllegalAccessException e) {
			throw new KnowledgeNotFoundException();
		}
		return currentObject;
	}

	/**
	 * Deletes the elements from root knowledge, lists or maps. It takes whole
	 * collection at once as it needs to delete elements in the decreasing order
	 * of index in case of lists.
	 * 
	 * @param knowledgePaths
	 * @return
	 */
	private Map<KnowledgePath, Object> deleteKnowledge(
			final Collection<KnowledgePath> knowledgePaths) {
		final Map<KnowledgePath, Object> deleted = new HashMap<>();
		// First lets delete free (root) nodes
		final List<KnowledgePath> reducedKnowledgePaths = new LinkedList<>(
				knowledgePaths);
		for (final KnowledgePath kp : knowledgePaths) {
			if (knowledge.containsKey(kp)) {
				deleted.put(kp, knowledge.get(kp));
				knowledge.remove(kp);
				reducedKnowledgePaths.remove(kp);
			}
		}
		// As roots has been removed, lets go and check inner knowledge. In that
		// case we consider only lists and maps.
		List<PathNode> pathNodesToParent;
		String fieldName;
		Object parent;
		final Map<Object, List<String>> parentsToPaths = new HashMap<>();
		List<String> keysToDelete;
		// We need to go through the remaining paths and collect paths to
		// the owners of the objects being deleted.
		for (final KnowledgePath kp : reducedKnowledgePaths) {
			try {
				// Retrieve path to the owner of the object being deleted
				// but keep the name of object being deleted.
				pathNodesToParent = new LinkedList<>(kp.getNodes());
				fieldName = ((PathNodeField) pathNodesToParent
						.remove(pathNodesToParent.size() - 1)).getName();
				parent = getKnowledge(pathNodesToParent);
				if (parentsToPaths.containsKey(parent)) {
					keysToDelete = parentsToPaths.get(parent);
				} else {
					keysToDelete = new LinkedList<>();
					parentsToPaths.put(parent, keysToDelete);
				}
				keysToDelete.add(fieldName);
			} catch (final KnowledgeNotFoundException e) {
				continue;
			}
		}
		// Now we need to go through collected parents and try to remove the
		// the objects from them.
		for (final Object p : parentsToPaths.keySet()) {
			keysToDelete = parentsToPaths.get(p);
			// We need to sort the keys in order to start deleting from the end.
			// This is important for list consistency.
			Collections.sort(keysToDelete);
			// And then we need to start deleting entries from the end.
			// This way we will preserve right referencing in case of lists.
			for (int i = keysToDelete.size() - 1; i >= 0; i--) {
				fieldName = keysToDelete.get(i);
				if (p instanceof List<?>) {
					((List<Object>) p).remove(Integer.parseInt(fieldName));
				} else if (p instanceof Map<?, ?>) {
					((Map<String, Object>) p).remove(fieldName);
				}
			}
		}
		return deleted;
	}

	/**
	 * Notifies knowledge listeners about the change in a particular knowledge
	 * path
	 * 
	 * @param knowledgePath
	 *            knowledge path for which knowledge value has changed.
	 */
	private void notifyKnowledgeChangeListeners(
			final KnowledgePath knowledgePath) {
		List<PathNode> kctNodes;
		final List<PathNode> kpNodes = knowledgePath.getNodes();
		// Go through each knowledge change trigger
		for (final KnowledgeChangeTrigger kct : knowledgeChangeListeners.keySet()) {
			kctNodes = kct.getKnowledgePath().getNodes();
			// Now we need to check if the knowledge change trigger matches
			// the knowledge path that has changed.
			// There can be diffferent path prefixing that we need to consider.
			// Examples:
			// knowledgePath: a.b.c, kct: a.b
			// knowledgePath : a.b, kct: a.b.c
			if (startsWith(kpNodes, kctNodes)
					|| startsWith(kctNodes, kpNodes)) {
				// If the knowledge change trigger matches then we need to
				// notify its listeners about the change
				for (final TriggerListener listener : knowledgeChangeListeners.get(kct)) {
					listener.triggered(kct);
				}
			}
		}
	}

	
	/**
	 * Checks whether the list longerNodes starts with the list of shorterNodes.
	 */
	private boolean startsWith(final List<PathNode> longerNodes, final List<PathNode> shorterNodes) {
		assert (longerNodes != null && shorterNodes != null);
		
		if (shorterNodes.isEmpty()) {
			return true;
		} else if (longerNodes.size() < shorterNodes.size()) {
			return false;
		}
		
		Iterator<PathNode> shorterIterator = shorterNodes.iterator();
		Iterator<PathNode> longerIterator = longerNodes.iterator();
		
		while (shorterIterator.hasNext()) {
			PathNode shorterNode = shorterIterator.next();
			PathNode longerNode = longerIterator.next();
			
			assert (shorterNode != null && longerNode != null);
			if (!shorterNode.equals(longerNode)) {
				return false;
			}
		}
		
		return true;
	}

	/**
	 * Reverts the knowledge entries to their original values and removes added
	 * new entries.
	 * 
	 * @param updated
	 *            changed values
	 * @param added
	 *            newly added entries
	 * @throws KnowledgeUpdateException
	 *             It should not be thrown.
	 */
	private void revert(Map<KnowledgePath, Object> updated,
			List<KnowledgePath> added) throws KnowledgeUpdateException {
		// Revert updates of the values
		for (final KnowledgePath revertKP : updated.keySet()) {
			updateKnowledge(revertKP, updated.get(revertKP));
		}
		// Revert adding new nodes to the knowledge
		deleteKnowledge(added);
	}

	@Override
	public void markAsLocal(Collection<KnowledgePath> knowledgePaths) {
		localKnowledgePaths.addAll(knowledgePaths);
	}

	@Override
	public boolean isLocal(KnowledgePath knowledgePath) {
		return localKnowledgePaths.contains(knowledgePath);
	}

	@Override
	public Collection<KnowledgePath> getLocalPaths() {
		return localKnowledgePaths;
	}

	@Override
	public void markAsSecured(KnowledgePath knowledgePath, Collection<KnowledgeSecurityTag> newSecurityTags) {		
		if (!securityTags.containsKey(knowledgePath)) {
			securityTags.put(knowledgePath, new LinkedList<>());
		}
		securityTags.get(knowledgePath).addAll(newSecurityTags);		
	}

	@Override
	public List<KnowledgeSecurityTag> getSecurityTagsFor(KnowledgePath knowledgePath) { 
		List<KnowledgeSecurityTag> tags = securityTags.get(knowledgePath);
		if (tags == null) {
			return Collections.EMPTY_LIST;
		} else {
			return tags;
		}
	}
}
