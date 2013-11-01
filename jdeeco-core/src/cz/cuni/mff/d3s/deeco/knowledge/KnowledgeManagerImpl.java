package cz.cuni.mff.d3s.deeco.knowledge;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.rits.cloning.Cloner;

import cz.cuni.mff.d3s.deeco.exceptions.KnowledgeManagerNotExistentException;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNode;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;

/**
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public class KnowledgeManagerImpl implements KnowledgeManager,
		KnowledgeManagersView {

	private final Object knowledge;
	private final Cloner cloner;
	private final Map<KnowledgePath, TriggerListener> knowledgeChangeListeners;

	public KnowledgeManagerImpl(Object knowledge) {
		this.cloner = new Cloner();
		this.knowledge = cloner.deepClone(knowledge);
		this.knowledgeChangeListeners = new HashMap<KnowledgePath, TriggerListener>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager#get(java.util
	 * .Collection)
	 */
	@Override
	public ValueSet get(Collection<KnowledgePath> knowledgePathList)
			throws KnowledgeManagerNotExistentException {
		ValueSet result = new ValueSet();
		for (KnowledgePath kp : knowledgePathList)
				result.setValue(kp,
						cloner.deepClone(getKnowledge(kp.getNodes())));
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

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager#update(cz.cuni.mff.d3s
	 * .deeco.knowledge.ChangeSet)
	 */
	@Override
	public void update(ChangeSet changeSet) {
		// Update
		try {
			for (KnowledgePath kp : changeSet.getUpdatedReferences()) {
				updateKnowledge(kp, cloner.deepClone(changeSet.getValue(kp)));
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
	public Collection<ReadOnlyKnowledgeManager> getOthersKnowledgeManagers() {
		return null;
	}

	private Object getKnowledge(List<PathNode> knowledgePath)
			throws KnowledgeManagerNotExistentException {
		Object currentObject = knowledge;
		Object parent = null;
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
								throw new KnowledgeManagerNotExistentException();
						} else {
							throw new KnowledgeManagerNotExistentException();
						}
					} catch (Exception e) {
						throw new KnowledgeManagerNotExistentException();
					}
				}
			}
		} catch (IllegalAccessException e) {
			throw new KnowledgeManagerNotExistentException();
		}
		return currentObject;
	}

	private void updateKnowledge(KnowledgePath knowledgePath, Object value)
			throws Exception {
		List<PathNode> pathNodesToParent = new LinkedList<>(
				knowledgePath.getNodes());
		String fieldName = ((PathNodeField) pathNodesToParent
				.remove(pathNodesToParent.size() - 1)).getName();
		Object parent = getKnowledge(pathNodesToParent);
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
				throw new KnowledgeManagerNotExistentException();
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
			// This is important for lists.
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
}
