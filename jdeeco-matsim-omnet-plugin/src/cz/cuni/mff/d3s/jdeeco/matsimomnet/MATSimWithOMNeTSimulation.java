package cz.cuni.mff.d3s.jdeeco.matsimomnet;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Exchanger;

import org.matsim.api.core.v01.Id;

import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.task.TimerTask;
import cz.cuni.mff.d3s.deeco.task.TimerTaskListener;
import cz.cuni.mff.d3s.jdeeco.matsim.plugin.MATSimSimulation;
import cz.cuni.mff.d3s.jdeeco.matsim.simulation.AdditionAwareAgentSource;
import cz.cuni.mff.d3s.jdeeco.matsim.simulation.MATSimDataProvider;
import cz.cuni.mff.d3s.jdeeco.matsim.simulation.MATSimDataReceiver;
import cz.cuni.mff.d3s.jdeeco.matsim.simulation.MATSimInput;
import cz.cuni.mff.d3s.jdeeco.matsim.simulation.MATSimOutput;
import cz.cuni.mff.d3s.jdeeco.network.omnet.OMNeTSimulation;
import cz.cuni.mff.d3s.jdeeco.network.omnet.OMNeTSimulation.BindedSimulation;

public class MATSimWithOMNeTSimulation extends MATSimSimulation implements TimerTaskListener {
	private boolean initialized = false;
	private Thread matSimThread;
	private OMNeTSimulation omnet;
	private long exchangeInterval;

	public MATSimWithOMNeTSimulation(String configPath, AdditionAwareAgentSource... additionalAgentSources)
			throws IOException {
		super(configPath, new Exchanger<Object>(), additionalAgentSources);
	}

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		List<Class<? extends DEECoPlugin>> dependencies = new LinkedList<>();
		dependencies.addAll(super.getDependencies());
		dependencies.add(OMNeTSimulation.class);
		return dependencies;
	}

	@Override
	public void init(DEECoContainer container) {
		super.init(container);

		if (initialized)
			return;

		omnet = container.getPluginInstance(OMNeTSimulation.class);

		omnet.getTimer().addBindedSimulation(new BindedSimulation() {
			@Override
			public void run(long duration) {
				MATSimWithOMNeTSimulation.this.runMATSim(duration);
			}
		});

		// Determine exchange interval
		exchangeInterval = (long) (this.controler.getConfig().getQSimConfigGroup().getTimeStepSize() * 1000);

		// Start exchange task
		Scheduler scheduler = container.getRuntimeFramework().getScheduler();
		scheduler.addTask(getInitialTask(scheduler));

		initialized = true;
	}

	private void runMATSim(long duration) {
		matSimThread = new Thread(new Runnable() {
			public void run() {
				getTimer().start(duration);
				System.out.println("MATSim ended");
			}
		});
		matSimThread.start();

		// Do the initial exchange, needed to initialize variables
		doExchange();
	}

	@SuppressWarnings("unchecked")
	private void doExchange() {
		// Do the exchange
		MATSimDataProvider provider = getMATSimProviderReceiver();
		MATSimDataReceiver receiver = getMATSimProviderReceiver();
		if (matSimThread.isAlive() /* && this.remainingExchanges > 0 */) {
			// System.out.println("OMNet before: " + getCurrentMilliseconds());

			try {
				Map<Id, MATSimInput> out = provider.getMATSimData();
				Object in = exchanger.exchange(out);
				receiver.setMATSimData((Map<Id, MATSimOutput>) in);
			} catch (InterruptedException e) {
				Log.e("MATSimOMNetSimulation", e);
			}
			// System.out.println("OMNet after: " + getCurrentMilliseconds());
			// this.remainingExchanges--;
		}
	}

	@Override
	public void at(long time, Object triger) {
		// TODO Auto-generated method stub
		System.out.println("DATA EXCHANGE TASK");

		doExchange();

		// Reschedule exchange task
		SimulationStepTask task = (SimulationStepTask) triger;
		task.scheduleNextExecutionAfter(exchangeInterval);
	}

	@Override
	public TimerTask getInitialTask(Scheduler scheduler) {
		return new SimulationStepTask(scheduler, this, 0);
	}
}
