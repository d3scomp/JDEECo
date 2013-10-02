package cz.cuni.mff.d3s.deeco.runtime;

import java.util.Arrays;

import cz.cuni.mff.d3s.deeco.definitions.ComponentDefinition;
import cz.cuni.mff.d3s.deeco.exceptions.KMNotExistentException;
import cz.cuni.mff.d3s.deeco.knowledge.ConstantKeys;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.TimeProvider;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.Ensemble;
import cz.cuni.mff.d3s.deeco.model.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.monitoring.MonitorProvider;
import cz.cuni.mff.d3s.deeco.monitoring.MonitorProviderHolder;
import cz.cuni.mff.d3s.deeco.monitoring.StubMonitorProvider;
import cz.cuni.mff.d3s.deeco.runtime.jmx.RuntimeMX;
import cz.cuni.mff.d3s.deeco.scheduling.Scheduler;
import cz.cuni.mff.d3s.deeco.task.provider.PeriodicComponentProcessTaskProvider;
import cz.cuni.mff.d3s.deeco.task.provider.PeriodicEnsembleTaskProvider;
import cz.cuni.mff.d3s.deeco.task.provider.TriggeredComponentProcessTaskProvider;
import cz.cuni.mff.d3s.deeco.task.provider.TriggeredEnsembleTaskProvider;

public class Runtime extends MonitorProviderHolder implements TimeProvider {

	private RuntimeMetadata runtimeMetadata;
	private final KnowledgeManager km;
	private final Scheduler scheduler;

	// private final Oracle oracle;

	public Runtime(Scheduler scheduler, KnowledgeManager km) {
		this(scheduler, km, new StubMonitorProvider(), false);
	}

	public Runtime(Scheduler scheduler, KnowledgeManager km,
			MonitorProvider monitorProvider, boolean useMXBeans) {
		super(monitorProvider);
		assert (km != null);
		assert (scheduler != null);
		if (useMXBeans)
			RuntimeMX.registerMBeanForRuntime(this);
		this.scheduler = scheduler;
		this.km = km;
		RuntimeUtil.RUNTIME = this;
	}

	@Override
	public long getCurrentTime() {
		return scheduler.getCurrentTime();
	}

	public long getKnowledgeTimeStamp(String knowledgePath) {
		return km.getKnowledgeTimeStamp(knowledgePath);
	}

	public boolean isRunning() {
		return scheduler.isStarted();
	}

	public KnowledgeManager getKnowledgeManager() {
		return km;
	}

	public void run() {
		km.setListenersActive(true);
		scheduler.start();
		deployComponentInstances();
		deployEnsembles();
		deployComponentProcesses();
	}

	public void shutdown() {
		km.setListenersActive(false);
		scheduler.stop();
	}

	// TODO add hot deployment - merge metadata
	public synchronized void deployRuntimeMetadata(
			RuntimeMetadata runtimeMetadata) {
		assert (runtimeMetadata != null);
		this.runtimeMetadata = runtimeMetadata;
	}

	private void deployComponentInstances() {
		for (ComponentInstance ci : runtimeMetadata.getComponentInstances()) {
			try {
				putInitialKnowledge(ci.getInitialKnowledge());
			} catch (Exception e) {
				Log.e(String.format(
						"Error when initializing knowledge of component %s",
						ci.getClass()), e);
				continue;
			}
		}
	}

	private void deployComponentProcesses() {
		for (ComponentInstance ci : runtimeMetadata.getComponentInstances())
			for (ComponentProcess cp : ci.getComponent().getProcesses()) {
				if (cp.getSchedule().isPeriodic())
					scheduler.add(new PeriodicComponentProcessTaskProvider(cp,
							ci.getId(), monitorProvider));
				if (cp.getSchedule().isTriggered())
					scheduler.add(new TriggeredComponentProcessTaskProvider(cp,
							monitorProvider));
			}
	}

	private void deployEnsembles() {
		for (Ensemble e : runtimeMetadata.getEnsembles()) {
			if (e.getSchedule().isPeriodic()) {
				for (ComponentInstance coord : runtimeMetadata
						.getComponentInstances()) {
					for (ComponentInstance member : runtimeMetadata
							.getComponentInstances())
						scheduler.add(new PeriodicEnsembleTaskProvider(e, coord
								.getId(), member.getId(), monitorProvider));
				}
			}
			if (e.getSchedule().isTriggered())
				scheduler.add(new TriggeredEnsembleTaskProvider(e,
						monitorProvider));
		}
	}

	/**
	 * Adds component knowledge into the knowledge manager.
	 * 
	 * @param initKnowledge
	 *            component knowledge that needs to be added to the knowledge
	 *            manager.
	 * @param km
	 *            reference to the knowledge manager.
	 * @throws Exception
	 *             in case the knowledge couldn't be initialized, the message
	 *             contains the reason.
	 */
	private void putInitialKnowledge(ComponentDefinition componentKnowledge)
			throws Exception {

		assert (componentKnowledge != null);

		try {
			Object[] currentIds = (Object[]) km
					.getKnowledge(ConstantKeys.ROOT_KNOWLEDGE_ID);
			if (Arrays.asList(currentIds).contains(componentKnowledge.id))
				throw new Exception(String.format(
						"Knowledge of a component with id '%s' already exists",
						componentKnowledge.id));
		} catch (KMNotExistentException kmnee) {
		}

		km.putKnowledge(ConstantKeys.ROOT_KNOWLEDGE_ID, componentKnowledge.id);
		km.alterKnowledge(componentKnowledge.id, componentKnowledge);
	}
}
