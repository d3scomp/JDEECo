package cz.cuni.mff.d3s.deeco.simulation;

import java.util.Collection;
import java.util.Random;

import cz.cuni.mff.d3s.deeco.DeecoProperties;
import cz.cuni.mff.d3s.deeco.executor.Executor;
import cz.cuni.mff.d3s.deeco.executor.SameThreadExecutor;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerContainer;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerFactory;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.TimeTriggerExt;
import cz.cuni.mff.d3s.deeco.network.AbstractHost;
import cz.cuni.mff.d3s.deeco.network.KnowledgeDataManager;
import cz.cuni.mff.d3s.deeco.network.PublisherTask;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeFramework;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeFrameworkImpl;
import cz.cuni.mff.d3s.deeco.simulation.scheduler.SimulationScheduler;
import cz.cuni.mff.d3s.deeco.simulation.task.TimerTask;

public class SimulationRuntimeBuilder {

	public RuntimeFramework build(AbstractHost host,
			CallbackProvider callbackProvider, Collection<? extends TimerTaskListener> listeners, RuntimeMetadata model,
			KnowledgeDataManager knowledgeDataManager, KnowledgeManagerFactory knowledgeManagerFactory) {
		if (model == null) {
			throw new IllegalArgumentException("Model must not be null");
		}

		// Set up the executor
		Executor executor = new SameThreadExecutor();

		// Set up the simulation scheduler
		SimulationScheduler scheduler = new SimulationScheduler(host,
				callbackProvider);
		scheduler.setExecutor(executor);
		((SimulationTimeEventListenerHolder) host)
				.setSimulationTimeEventListener(scheduler);

		// Set up the host container
		KnowledgeManagerContainer container = new KnowledgeManagerContainer(knowledgeManagerFactory);
		knowledgeDataManager.initialize(container, host.getKnowledgeDataSender(), host.getHostId(), scheduler);
		host.setKnowledgeDataReceiver(knowledgeDataManager);
		// Set up the publisher task
		TimeTriggerExt publisherTrigger = new TimeTriggerExt();
		publisherTrigger.setPeriod(Integer.getInteger(
				DeecoProperties.PUBLISHING_PERIOD,
				PublisherTask.DEFAULT_PUBLISHING_PERIOD));
		long seed = 0;
		for (char c : host.getHostId().toCharArray())
			seed = seed * 32 + (c - 'a');
		Random rnd = new Random(seed);
		publisherTrigger.setOffset(rnd.nextInt((int) publisherTrigger
				.getPeriod()) + 1);
		PublisherTask publisherTask = new PublisherTask(scheduler, knowledgeDataManager,
				publisherTrigger, host.getHostId());

		// Add publisher task to the scheduler
		scheduler.addTask(publisherTask);
		
		//Add extra tasks
		if (listeners != null) {
			for (TimerTaskListener listener: listeners) {
				TimerTask task = listener.getInitialTask(scheduler);
				if (task != null) {
					scheduler.addTask(task);
				}
			}
		}
		return new RuntimeFrameworkImpl(model, scheduler, executor, container);
	}

}
