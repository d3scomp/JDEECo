package cz.cuni.mff.d3s.deeco.knowledge;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.exceptions.KnowledgeNotExistentException;
import cz.cuni.mff.d3s.deeco.logging.Log;
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
public class BaseKnowledgeManager implements KnowledgeManager,
		KnowledgeManagersView {

	private final Map<KnowledgePath, Object> knowledge;
	private final Map<KnowledgeChangeTrigger, List<TriggerListener>> knowledgeChangeListeners;
	private final KnowledgePath baseReference;

	public BaseKnowledgeManager() {
		this.knowledge = new HashMap<>();
		this.knowledgeChangeListeners = new HashMap<>();
		this.baseReference = KnowledgePathUtils.createKnowledgePath();
	}

	public BaseKnowledgeManager(Object baseKnowledge) {
		this();
		if (baseKnowledge != null)
			knowledge.put(baseReference, baseKnowledge);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager#get(java.util
	 * .Collection)
	 */
	@Override
	public synchronized ValueSet get(Collection<KnowledgePath> knowledgePaths)
			throws KnowledgeNotExistentException {
		ValueSet result = new ValueSet();
		for (KnowledgePath kp : knowledgePaths)
			result.setValue(kp, getKnowledge(kp.getNodes()));
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
	public synchronized void register(Trigger trigger,
			TriggerListener triggerListener) {
		if (trigger instanceof KnowledgeChangeTrigger) {
			KnowledgeChangeTrigger kct = (KnowledgeChangeTrigger) trigger;
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
	public synchronized void unregister(Trigger trigger,
			TriggerListener triggerListener) {
		if (trigger instanceof KnowledgeChangeTrigger) {
			KnowledgePath kp = ((KnowledgeChangeTrigger) trigger)
					.getKnowledgePath();
			if (knowledgeChangeListeners.containsKey(kp)) {
				knowledgeChangeListeners.get(kp).remove(triggerListener);
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
	public synchronized void update(ChangeSet changeSet) {
		// Update
		try {
			for (KnowledgePath kp : changeSet.getUpdatedReferences()) {
				updateKnowledge(kp, changeSet.getValue(kp));
				notifyKnowledgeChangeListeners(kp);
			}
		} catch (Exception e) {
			Log.e("Knowledge update error", e);
		}
		// Delete list or map items
		deleteKnowledge(changeSet.getDeletedReferences());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagersView#
	 * getOthersKnowledgeManagers()
	 */
	@Override
	public synchronized Collection<ReadOnlyKnowledgeManager> getOthersKnowledgeManagers() {
		KnowledgeManagerRegistry kmRegistry = KnowledgeManagerRegistry
				.getInstance();
		List<ReadOnlyKnowledgeManager> result = new LinkedList<>();
		result.addAll(kmRegistry.getLocals());
		result.addAll(kmRegistry.getShadows());
		result.remove(this);
		return result;
	}

	protected Object getKnowledge(List<PathNode> knowledgePath)
			throws KnowledgeNotExistentException {
		Object currentObject = knowledge;
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
								throw new KnowledgeNotExistentException();
						} else {
							throw new KnowledgeNotExistentException();
						}
					} catch (Exception e) {
						throw new KnowledgeNotExistentException();
					}
				}
			}
		} catch (IllegalAccessException e) {
			throw new KnowledgeNotExistentException();
		}
		return currentObject;
	}

	protected void updateKnowledge(KnowledgePath knowledgePath, Object value) {
		List<PathNode> pathNodesToParent = new LinkedList<>(
				knowledgePath.getNodes());
		String fieldName = ((PathNodeField) pathNodesToParent
				.remove(pathNodesToParent.size() - 1)).getName();
		Object parent = null;
		try {
			parent = getKnowledge(pathNodesToParent);
		} catch (KnowledgeNotExistentException e) {
			//TODO add new entry to the base map
		}
		try {
			Field field = parent.getClass().getField(fieldName);
			field.set(parent, value);
		} catch (Exception e) {
			// Consider maps and lists indexed by the key
			if (parent instanceof List<?>) {
				((List<Object>) parent).set(Integer.parseInt(fieldName), value);
			} else if (parent instanceof Map<?, ?>) {
				((Map<String, Object>) parent).put(fieldName, value);
			} else {
				//TODO add new entry to the base map
			}
		}
	}

	private void deleteKnowledge(Collection<KnowledgePath> knowledgePaths) {
		List<PathNode> pathNodesToParent;
		String fieldName;
		Object parent;
		Map<Object, List<String>> parentsToDeleteKeys = new HashMap<>();
		List<String> keysToDelete;
		for (KnowledgePath kp : knowledgePaths) {
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
	}

	private void notifyKnowledgeChangeListeners(KnowledgePath kp) {
		KnowledgeChangeTrigger foundKCT = null;
		for (KnowledgeChangeTrigger kct : knowledgeChangeListeners.keySet()) {
			if (kct.getKnowledgePath().equals(kp)) {
				foundKCT = kct;
				break;
			}
		}
		if (foundKCT != null) {
			for (TriggerListener listener : knowledgeChangeListeners
					.get(foundKCT)) {
				listener.triggered(foundKCT);
			}
		}
	}
}
