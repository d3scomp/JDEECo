package cz.cuni.mff.d3s.deeco.knowledge;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeChangeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNode;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;

/**
 * This class implements the KnowledgeManager interface. It allows the user to
 * add, update and read the values from KnowledgeSet. Also, the class allows to
 * bind a trigger to tirggerListener or unbind it.
 * 
 * @author Rima Al Ali <alali@d3s.mff.cuni.cz>
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public class BaseKnowledgeManager implements KnowledgeManager {

	private final Map<KnowledgePath, Object> knowledge;
	private final Map<KnowledgeChangeTrigger, List<TriggerListener>> knowledgeChangeListeners;

	private final String id;
	
	
	public BaseKnowledgeManager(String id) {
		this.id = id;
		this.knowledge = new HashMap<>();
		this.knowledgeChangeListeners = new HashMap<>();
	}
	
	@Override
	public String getId() {
		return this.id;
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
		ValueSet result = new ValueSet();
		Object value;
		for (KnowledgePath kp : knowledgePaths) {
			value = getKnowledge(kp.getNodes());
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
	
	@Override
	public boolean equals(Object that) {
		if (that != null && that instanceof BaseKnowledgeManager)
			return ((BaseKnowledgeManager) that).id.equals(id);
		return false;
	}

	protected Object getKnowledge(List<PathNode> knowledgePath)
			throws KnowledgeNotFoundException {
		assert (knowledgePath != null);
		if (knowledgePath.isEmpty())
			return knowledge;
		int containmentEndIndex;
		for (KnowledgePath kp : knowledge.keySet()) {
			try {
				containmentEndIndex = containmentEndIndex(knowledgePath,
						kp.getNodes());
				if (containmentEndIndex > -1)
					return getKnowledgeFromNode(knowledgePath.subList(
							containmentEndIndex + 1, knowledgePath.size()),
							knowledge.get(kp));
			} catch (KnowledgeNotFoundException knfe) {
				continue;
			}
		}
		throw new KnowledgeNotFoundException();
	}

	protected void updateKnowledge(KnowledgePath knowledgePath, Object value) {
		List<PathNode> pathNodesToParent = new LinkedList<>(
				knowledgePath.getNodes());
		String fieldName = ((PathNodeField) pathNodesToParent
				.remove(pathNodesToParent.size() - 1)).getName();
		Object parent = null;
		try {
			parent = getKnowledge(pathNodesToParent);
		} catch (KnowledgeNotFoundException e) {
			knowledge.put(knowledgePath, value);
			return;
		}
		try {
			Field field = parent.getClass().getField(fieldName);
			field.set(parent, value);
		} catch (Exception e) {
			if (parent instanceof List<?>) {
				((List<Object>) parent).set(Integer.parseInt(fieldName), value);
			} else if (parent instanceof Map<?, ?>) {
				if (parent.equals(knowledge))
					knowledge.put(knowledgePath, value);
				else
					((Map<String, Object>) parent).put(fieldName, value);
			}
		}
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

	private void deleteKnowledge(Collection<KnowledgePath> knowledgePaths) {
		// First lets delete free (root) nodes
		List<KnowledgePath> reducedKnowledgePaths = new LinkedList<>(
				knowledgePaths);
		for (KnowledgePath kp : knowledgePaths) {
			if (knowledge.containsKey(kp)) {
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
	}

	private void notifyKnowledgeChangeListeners(KnowledgePath kp) {
		List<KnowledgeChangeTrigger> foundKCTs = new LinkedList<>();
		List<PathNode> kctNodes;
		List<PathNode> kpNodes = kp.getNodes();
		for (KnowledgeChangeTrigger kct : knowledgeChangeListeners.keySet()) {
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
				for (TriggerListener listener : knowledgeChangeListeners
						.get(kct)) {
					listener.triggered(kct);
				}
			}
		}
	}

	private ValueSet processInitialKnowledge(Object knowledge) {
		RuntimeMetadataFactory factory = RuntimeMetadataFactory.eINSTANCE;
		ValueSet result = new ValueSet();
		KnowledgePath kp;
		PathNodeField pnf;
		for (Field f : knowledge.getClass().getFields()) {
			kp = factory.createKnowledgePath();
			pnf = factory.createPathNodeField();
			pnf.setName(new String(f.getName()));
			kp.getNodes().add(pnf);
			try {
				result.setValue(kp, f.get(knowledge));
			} catch (IllegalAccessException e) {
				continue;
			}
		}
		return result;
	}

	/**
	 * Checks whether the shorter list is contained in the longer one, keeping
	 * the order. It returns the containment end index. FIXME: This method
	 * should be somewhere else.
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
