package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;

/**
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public class KnowledgeManagerViewImpl implements KnowledgeManagersView,
		ReplicaListener, LocalListener {

	private final KnowledgeManager knowledgeManager;
	private final KnowledgeManagerContainer container;
	private final Map<Trigger, List<ShadowsTriggerListener>> triggerListeners;

	private final Map<ShadowsTriggerListener, List<KnowledgeManagerTriggerListener>> listenerToTriggerListeners;

	public KnowledgeManagerViewImpl(KnowledgeManager knowledgeManager,
			KnowledgeManagerContainer container) {
		this.knowledgeManager = knowledgeManager;
		this.container = container;
		this.triggerListeners = new HashMap<>();
		listenerToTriggerListeners = new HashMap<>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.knowledge.LocalListener#localCreated(cz.cuni.mff
	 * .d3s.deeco.knowledge.KnowledgeManager)
	 */
	@Override
	public synchronized void localCreated(KnowledgeManager km) {
		replicaCreated(km);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.knowledge.LocalListener#localRemoved(cz.cuni.mff
	 * .d3s.deeco.knowledge.KnowledgeManager)
	 */
	@Override
	public synchronized void localRemoved(KnowledgeManager km) {
		replicaRemoved(km);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.knowledge.ReplicaListener#replicaCreated(cz.cuni
	 * .mff.d3s.deeco.knowledge.KnowledgeManager)
	 */
	@Override
	public synchronized void replicaCreated(KnowledgeManager km) {
		for (Trigger trigger : triggerListeners.keySet())
			for (ShadowsTriggerListener listener : triggerListeners
					.get(trigger))
				registerTriggerAtKnowledgeManager(km, trigger, listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.knowledge.ReplicaListener#replicaRemoved(cz.cuni
	 * .mff.d3s.deeco.knowledge.KnowledgeManager)
	 */
	@Override
	public synchronized void replicaRemoved(KnowledgeManager km) {
		List<KnowledgeManagerTriggerListener> toRemove = new LinkedList<>();
		for (List<KnowledgeManagerTriggerListener> tListeners : listenerToTriggerListeners
				.values()) {
			toRemove.clear();
			for (KnowledgeManagerTriggerListener tListener : tListeners)
				if (tListener.getKnowledgeManager().equals(km))
					toRemove.add(tListener);
			tListeners.removeAll(toRemove);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagersView#
	 * getOthersKnowledgeManagers()
	 */
	@Override
	public synchronized Collection<ReadOnlyKnowledgeManager> getOtherKnowledgeManagers() {
		List<ReadOnlyKnowledgeManager> result = new LinkedList<>();
		result.addAll(container.getLocals());
		result.addAll(container.getReplicas());
		result.remove(knowledgeManager);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagersView#register(cz.cuni
	 * .mff.d3s.deeco.model.runtime.api.Trigger,
	 * cz.cuni.mff.d3s.deeco.knowledge.ShadowsTriggerListener)
	 */
	@Override
	public synchronized void register(Trigger trigger,
			ShadowsTriggerListener triggerListener) {
		// Memorize trigger and its listener
		List<ShadowsTriggerListener> listeners;
		if (triggerListeners.containsKey(trigger)) {
			listeners = triggerListeners.get(trigger);
		} else {
			listeners = new LinkedList<>();
			triggerListeners.put(trigger, listeners);
		}
		if (listeners.contains(triggerListener))
			return; // Already registered

		listeners.add(triggerListener);
		List<KnowledgeManager> kms = new LinkedList<>();
		kms.addAll(container.getLocals());
		kms.addAll(container.getReplicas());

		for (KnowledgeManager km : kms)
			registerTriggerAtKnowledgeManager(km, trigger, triggerListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagersView#unregister(cz.cuni
	 * .mff.d3s.deeco.model.runtime.api.Trigger,
	 * cz.cuni.mff.d3s.deeco.knowledge.ShadowsTriggerListener)
	 */
	@Override
	public synchronized void unregister(Trigger trigger,
			ShadowsTriggerListener triggerListener) {
		// Remove from memory
		if (triggerListeners.containsKey(trigger)) {
			triggerListeners.get(trigger).remove(triggerListener);
		}
		// Unregister from kms
		for (KnowledgeManagerTriggerListener tListener : listenerToTriggerListeners
				.get(triggerListener))
			tListener.getKnowledgeManager().unregister(trigger, tListener);
		listenerToTriggerListeners.remove(triggerListener);

	}

	private void registerTriggerAtKnowledgeManager(KnowledgeManager km,
			Trigger trigger, ShadowsTriggerListener triggerListener) {
		List<KnowledgeManagerTriggerListener> tListeners;
		KnowledgeManagerTriggerListener tListener = new KnowledgeManagerTriggerListener(
				km, triggerListener);
		// Update map of listenerToTriggerListeners
		if (listenerToTriggerListeners.containsKey(triggerListener))
			tListeners = listenerToTriggerListeners.get(triggerListener);
		else {
			tListeners = new LinkedList<>();
			listenerToTriggerListeners.put(triggerListener, tListeners);
		}
		tListeners.add(tListener);

		// Register the listener in the km
		km.register(trigger, tListener);
	}

	private class KnowledgeManagerTriggerListener implements TriggerListener {

		private final KnowledgeManager knowledgeManager;
		private final ShadowsTriggerListener listener;

		public KnowledgeManagerTriggerListener(
				KnowledgeManager knowledgeManager,
				ShadowsTriggerListener listener) {
			this.knowledgeManager = knowledgeManager;
			this.listener = listener;
		}

		@Override
		public void triggered(Trigger trigger) {
			listener.triggered(knowledgeManager, trigger);
		}

		public KnowledgeManager getKnowledgeManager() {
			return knowledgeManager;
		}
	}

}
