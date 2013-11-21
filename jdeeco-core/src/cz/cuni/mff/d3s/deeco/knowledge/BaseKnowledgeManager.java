package cz.cuni.mff.d3s.deeco.knowledge;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeChangeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNode;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;

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

	private transient Map<KnowledgePath, Object> knowledge;
	private transient final Map<KnowledgeChangeTrigger, List<TriggerListener>> kcListeners;

	private transient final String identifier;

	public BaseKnowledgeManager(String identifier) {
		this.identifier = identifier;
		this.knowledge = new HashMap<>();
		this.kcListeners = new HashMap<>();
	}

	@Override
	public String getId() {
		return this.identifier;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager#get(java.util
	 * .Collection)
	 */
	@Override
	public ValueSet get(Collection<KnowledgePath> knowledgePaths)
			throws KnowledgeNotFoundException {
		final ValueSet result = new ValueSet();
		Object value;
		for (final KnowledgePath knowledgePath : knowledgePaths) {
			value = getKnowledge(knowledgePath.getNodes());
			if (knowledge.equals(value)) {
				for (final KnowledgePath rootKP : knowledge.keySet()) {
					result.setValue(rootKP, knowledge.get(rootKP));
				}
			} else {
				result.setValue(knowledgePath, value);
			}
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
	public void register(Trigger trigger, TriggerListener triggerListener) {
		if (trigger instanceof KnowledgeChangeTrigger) {
			final KnowledgeChangeTrigger kct = (KnowledgeChangeTrigger) trigger;
			List<TriggerListener> listeners;
			if (kcListeners.containsKey(kct)) {
				listeners = kcListeners.get(kct);
			} else {
				listeners = new LinkedList<>();
				kcListeners.put(kct, listeners);
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
	public void unregister(Trigger trigger, TriggerListener triggerListener) {
		if (trigger instanceof KnowledgeChangeTrigger) {
			final KnowledgeChangeTrigger kct = (KnowledgeChangeTrigger) trigger;
			if (kcListeners.containsKey(kct)) {
				kcListeners.get(kct).remove(triggerListener);
			}
		}
	}

	// FIXME TB: The code here desperately needs comments - in this case even
	// inside the functions
	// BTW, is the check implemented whether a path forming a prefix is already
	// contained?
	// BTW, why do you decompose the initial object on the first level of
	// nesting? Why can't you just store the initial object as such?

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager#update(cz.cuni.mff.d3s
	 * .deeco.knowledge.ChangeSet)
	 */
	@Override
	public void update(ChangeSet changeSet) throws KnowledgeUpdateException {
		Map<KnowledgePath, Object> updated = new HashMap<>();
		List<KnowledgePath> added = new LinkedList<>();
		Object original = null;
		try {
			boolean exists;
			for (final KnowledgePath updateKP : changeSet
					.getUpdatedReferences()) {
				// We need to preserve the state of the knowledge, in order to
				// revert it
				// back in case of problems
				exists = true;
				try {
					original = getKnowledge(updateKP.getNodes());
				} catch (KnowledgeNotFoundException e) {
					exists = false;
				}
				updateKnowledge(updateKP, changeSet.getValue(updateKP));
				if (exists) {
					updated.put(updateKP, original);
				} else {
					added.add(updateKP);
				}
			}
		} catch (KnowledgeUpdateException e) {
			// Revert changes;
			for (KnowledgePath revertKP : updated.keySet()) {
				updateKnowledge(revertKP, updated.get(revertKP));
			}
			// Revert adding new nodes
			deleteKnowledge(added);
			// Throw the exception
			throw e;
		}
		// Delete
		KnowledgePath invalidDelete = null;
		for (KnowledgePath deleteKP : changeSet.getDeletedReferences()) {
			if (!isValidDeletePath(deleteKP)) {
				invalidDelete = deleteKP;
				break;
			}
		}
		if (invalidDelete == null) {
			deleteKnowledge(changeSet.getDeletedReferences());
		} else {
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

	@Override
	public boolean equals(Object that) {
		if (that != null && that instanceof BaseKnowledgeManager)
			return ((BaseKnowledgeManager) that).identifier.equals(identifier);
		return false;
	}

	@Override
	public int hashCode() {
		return identifier.hashCode();
	}

	protected Object getKnowledge(List<PathNode> knowledgePath)
			throws KnowledgeNotFoundException {
		assert knowledgePath != null;
		Object result = null;
		if (knowledgePath.isEmpty()) {
			result = knowledge;
		} else {
			int endIndex = -1;
			for (final KnowledgePath kPath : knowledge.keySet()) {
				try {
					endIndex = containmentEndIndex(knowledgePath,
							kPath.getNodes());
					if (endIndex > -1) {
						result = getKnowledgeFromNode(knowledgePath.subList(
								endIndex + 1, knowledgePath.size()),
								knowledge.get(kPath));
						break;
					}
				} catch (KnowledgeNotFoundException knfe) {
					continue;
				}
			}
			if (endIndex < 0) {
				throw new KnowledgeNotFoundException();
			}
		}
		return result;
	}

	protected void updateKnowledge(KnowledgePath knowledgePath, Object value)
			throws KnowledgeUpdateException {
		if (noPathOverlapWithAny(knowledgePath, knowledge.keySet())) {
			knowledge.put(knowledgePath, value);
		} else {
			List<PathNode> pathNodesToParent = new LinkedList<>(
					knowledgePath.getNodes());
			String fieldName = ((PathNodeField) pathNodesToParent
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
				} else
					((Map<String, Object>) parent).put(fieldName, value);
			} else {
				try {
					Field field = parent.getClass().getField(fieldName);
					field.set(parent, value);
				} catch (Exception e) {
					throw new KnowledgeUpdateException("Forbidden update: "
							+ e.getMessage() + " - " + knowledgePath);
				}
			}
		}
	}

	private boolean noPathOverlapWithAny(KnowledgePath path,
			Collection<KnowledgePath> paths) {
		for (KnowledgePath p : paths) {
			if (containmentEndIndex(p.getNodes(), path.getNodes()) > -1
					|| containmentEndIndex(path.getNodes(), p.getNodes()) > -1)
				return false;
		}
		return true;
	}

	private boolean isValidDeletePath(KnowledgePath knowledgePath) {
		boolean result = false;
		if (knowledge.containsKey(knowledgePath)) {
			result = true;
		} else {
			List<PathNode> pathNodesToParent = new LinkedList<>(
					knowledgePath.getNodes());
			String fieldName = ((PathNodeField) pathNodesToParent
					.remove(pathNodesToParent.size() - 1)).getName();
			Object parent = null;
			try {
				parent = getKnowledge(pathNodesToParent);
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

	private Object getKnowledgeFromNode(List<PathNode> knowledgePath,
			Object node) throws KnowledgeNotFoundException {
		Object currentObject = node;
		Field currentField;
		String fieldName = null;
		try {
			for (PathNode pn : knowledgePath) {
				try {
					/**
					 * All path nodes are PathNodeField
					 */
					fieldName = ((PathNodeField) pn).getName();
					currentField = currentObject.getClass().getField(fieldName);
					currentObject = currentField.get(currentObject);
				} catch (NoSuchFieldException nfe) {
					// Consider maps and lists indexed by the key
					currentField = null;
					try {
						if (currentObject instanceof List<?>) {
							currentObject = ((List<?>) currentObject)
									.get(Integer.parseInt(fieldName));
						} else if (currentObject instanceof Map<?, ?>) {
							Map<String, Object> currentObjectAsMap = (Map<String, Object>) currentObject;
							if (currentObjectAsMap.containsKey(fieldName))
								currentObject = currentObjectAsMap
										.get(fieldName);
							else
								throw new KnowledgeNotFoundException();
						} else {
							throw new KnowledgeNotFoundException();
						}
					} catch (Exception e) {
						throw new KnowledgeNotFoundException();
					}
				}
			}
		} catch (IllegalAccessException e) {
			throw new KnowledgeNotFoundException();
		}
		return currentObject;
	}

	private Map<KnowledgePath, Object> deleteKnowledge(
			Collection<KnowledgePath> knowledgePaths) {
		Map<KnowledgePath, Object> deleted = new HashMap<>();
		// First lets delete free (root) nodes
		List<KnowledgePath> reducedKnowledgePaths = new LinkedList<>(
				knowledgePaths);
		for (KnowledgePath kp : knowledgePaths) {
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
		Map<Object, List<String>> parentsToDeleteKeys = new HashMap<>();
		List<String> keysToDelete;
		for (KnowledgePath kp : reducedKnowledgePaths) {
			try {
				pathNodesToParent = new LinkedList<>(kp.getNodes());
				fieldName = ((PathNodeField) pathNodesToParent
						.remove(pathNodesToParent.size() - 1)).getName();
				parent = getKnowledge(pathNodesToParent);
				if (parentsToDeleteKeys.containsKey(parent)) {
					keysToDelete = parentsToDeleteKeys.get(parent);
				} else {
					keysToDelete = new LinkedList<>();
					parentsToDeleteKeys.put(parent, keysToDelete);
				}
				keysToDelete.add(fieldName);
			} catch (Exception e) {
				continue;
			}
		}
		for (Object p : parentsToDeleteKeys.keySet()) {
			keysToDelete = parentsToDeleteKeys.get(p);
			// We need to sort the keys in order to start deleting from the end.
			// This is important for list consitency.
			Collections.sort(keysToDelete);
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

	private void notifyKnowledgeChangeListeners(KnowledgePath kp) {
		List<KnowledgeChangeTrigger> foundKCTs = new LinkedList<>();
		List<PathNode> kctNodes;
		List<PathNode> kpNodes = kp.getNodes();
		for (KnowledgeChangeTrigger kct : kcListeners.keySet()) {
			kctNodes = kct.getKnowledgePath().getNodes();
			// kp: a.b.c, kct: a.b
			// kp : a.b, kct: a.b.c
			if (containmentEndIndex(kpNodes, kctNodes) > -1
					|| containmentEndIndex(kctNodes, kpNodes) > -1) {
				foundKCTs.add(kct);
			}
		}
		if (!foundKCTs.isEmpty()) {
			for (KnowledgeChangeTrigger kct : foundKCTs) {
				for (TriggerListener listener : kcListeners.get(kct)) {
					listener.triggered(kct);
				}
			}
		}
	}

	/**
	 * Checks whether the shorter list is contained in the longer one, keeping
	 * the order. It returns the containment end index.
	 * 
	 * @param longer
	 * @param shorter
	 * @return
	 */
	private int containmentEndIndex(List<?> longer, List<?> shorter) {
		assert (longer != null && shorter != null);
		// We need this as EList has differs in implementation of List
		// specification
		// See indexOf method.
		List<Object> longerList = new LinkedList<>();
		List<Object> shorterList = new LinkedList<>();
		longerList.addAll(longer);
		shorterList.addAll(shorter);
		if (shorterList.isEmpty())
			return 0;
		if (longerList.isEmpty())
			return -1;
		int index = longerList.indexOf(shorterList.get(0));
		if (index < 0)
			return -1;
		index++;
		for (int i = 1; i < shorterList.size(); i++, index++) {
			if (index != longerList.indexOf(shorterList.get(i)))
				return -1;
		}
		index--;
		return index;
	}
}
