package cz.cuni.mff.d3s.deeco.runtime;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.definitions.ComponentDefinition;
import cz.cuni.mff.d3s.deeco.exceptions.KMNotExistentException;
import cz.cuni.mff.d3s.deeco.knowledge.ConstantKeys;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.runtime.jmx.RuntimeMX;
import cz.cuni.mff.d3s.deeco.runtime.model.ComponentInstance;
import cz.cuni.mff.d3s.deeco.runtime.model.ComponentProcess;
import cz.cuni.mff.d3s.deeco.runtime.model.Ensemble;
import cz.cuni.mff.d3s.deeco.runtime.model.PeriodicSchedule;
import cz.cuni.mff.d3s.deeco.runtime.model.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.scheduling.ComponentProcessJob;
import cz.cuni.mff.d3s.deeco.scheduling.ComponentProcessJobProducer;
import cz.cuni.mff.d3s.deeco.scheduling.EnsembleJob;
import cz.cuni.mff.d3s.deeco.scheduling.EnsembleJobProducer;
import cz.cuni.mff.d3s.deeco.scheduling.Scheduler;
import cz.cuni.mff.d3s.deeco.scheduling.TriggeredJobProducer;

public class Runtime {

	private RuntimeMetadata runtimeMetadata;
	private final KnowledgeManager km;
	private final Scheduler scheduler;
	private final List<TriggeredJobProducer> triggeredJobProducers;
	//private final Oracle oracle;

	public Runtime(Scheduler scheduler, KnowledgeManager km) {
		this(scheduler, km, false);
	}

	public Runtime(Scheduler scheduler, KnowledgeManager km, boolean useMXBeans) {
		assert (km != null);
		assert (scheduler != null);
		if (useMXBeans)
			RuntimeMX.registerMBeanForRuntime(this);
		this.scheduler = scheduler;
		this.km = km;
		this.triggeredJobProducers = new LinkedList<>();
		//this.oracle = new Oracle(new SAT4JSolver());
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

	//TODO add hot deployment - merge metadata
	public synchronized void deployRuntimeMetadata(RuntimeMetadata runtimeMetadata) {
		assert (runtimeMetadata != null);
		this.runtimeMetadata = runtimeMetadata;
	}
	
//	public synchronized void deployIRMInvariant(Invariant invariant) {
//		assert (invariant != null);
//		//oracle.addIRMInvariant(invariant);
//	}
	

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
		ComponentProcessJobProducer cpjp;
		for (ComponentInstance ci : runtimeMetadata.getComponentInstances())
			for (ComponentProcess cp : ci.getComponent().getProcesses()) {
				if (cp.getSchedule() instanceof PeriodicSchedule)
					scheduler.schedule(new ComponentProcessJob(cp, ci.getId(),
							scheduler, this));
				else {
					cpjp = new ComponentProcessJobProducer(cp, scheduler, this);
					triggeredJobProducers.add(cpjp);
					km.registerListener(cpjp);
				}
			}
	}

	private void deployEnsembles() {
		EnsembleJobProducer ejp;
		for (Ensemble e : runtimeMetadata.getEnsembles()) {
			if (e.getSchedule() instanceof PeriodicSchedule) {
				for (ComponentInstance coord : runtimeMetadata
						.getComponentInstances()) {
					for (ComponentInstance member : runtimeMetadata
							.getComponentInstances())
						scheduler.schedule(new EnsembleJob(e, coord.getId(),
								member.getId(), scheduler, this));

				}
			} else {
				System.out.println("Triggered ensemble");
				ejp = new EnsembleJobProducer(e, scheduler, this);
				triggeredJobProducers.add(ejp);
				km.registerListener(ejp);
			}
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
