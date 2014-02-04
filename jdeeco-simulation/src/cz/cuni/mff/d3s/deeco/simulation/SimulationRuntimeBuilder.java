package cz.cuni.mff.d3s.deeco.simulation;

import cz.cuni.mff.d3s.deeco.executor.Executor;
import cz.cuni.mff.d3s.deeco.executor.SameThreadExecutor;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerContainer;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PeriodicTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;
import cz.cuni.mff.d3s.deeco.network.PublisherTask;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeFramework;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeFrameworkImpl;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.simulation.scheduler.SimulationScheduler;

public class SimulationRuntimeBuilder {

	public RuntimeFramework build(Host host, RuntimeMetadata model,
			long publishingPeriod) {
		if (model == null) {
			throw new IllegalArgumentException("Model must not be null");
		}

		// Set up the executor
		Executor executor = new SameThreadExecutor();

		// Set up the simulation scheduler
		Scheduler scheduler = new SimulationScheduler(host);
		scheduler.setExecutor(executor);

		// Set up the host container
		KnowledgeManagerContainer container = new KnowledgeManagerContainer();

		// Bind KnowledgeDataReceiver with PacketDataReceiver
		host.getPacketReceiver().setKnowledgeDataReceiver(container);

		// Set up the publisher task
		PublisherTask publisherTask = new PublisherTask(scheduler, host, container,
				createPeriodicTrigger(publishingPeriod));
		// Add publisher task to the scheduler
		scheduler.addTask(publisherTask);

		return new RuntimeFrameworkImpl(model, scheduler, executor,
				container);
	}

	private PeriodicTrigger createPeriodicTrigger(long pieriod) {
		RuntimeMetadataFactory factory = RuntimeMetadataFactory.eINSTANCE;
		PeriodicTrigger result = factory.createPeriodicTrigger();
		result.setPeriod(pieriod);
		return result;
	}

}
