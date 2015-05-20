package cz.cuni.mff.d3s.jdeeco.matsimomnet;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.matsim.api.core.v01.Id;

import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.task.TimerTaskListener;
import cz.cuni.mff.d3s.deeco.timer.SimulationTimer;
import cz.cuni.mff.d3s.deeco.timer.TimerEventListener;
import cz.cuni.mff.d3s.jdeeco.matsim.dataaccess.MATSimDataProviderReceiver;
import cz.cuni.mff.d3s.jdeeco.matsim.plugin.IMATSimSimulaton;
import cz.cuni.mff.d3s.jdeeco.matsim.plugin.MATSimSimulation;
import cz.cuni.mff.d3s.jdeeco.matsim.simulation.AdditionAwareAgentSource;
import cz.cuni.mff.d3s.jdeeco.matsim.simulation.MATSimDataProvider;
import cz.cuni.mff.d3s.jdeeco.matsim.simulation.MATSimDataReceiver;
import cz.cuni.mff.d3s.jdeeco.matsim.simulation.MATSimInput;
import cz.cuni.mff.d3s.jdeeco.matsim.simulation.MATSimOutput;
import cz.cuni.mff.d3s.jdeeco.matsim.simulation.MATSimRouter;
import cz.cuni.mff.d3s.jdeeco.network.omnet.IOMNeTSimulation;
import cz.cuni.mff.d3s.jdeeco.network.omnet.OMNeTSimulation;
import cz.cuni.mff.d3s.jdeeco.network.omnet.OMNeTSimulation.OMNeTHost;

public class OMNeTMATSimSimulation implements IMATSimSimulaton, IOMNeTSimulation, TimerTaskListener {
	private final long EXCHANGE_TIMEOUT_MS = 5000;
	
	private final Timer timer = new Timer();
	private final OMNeTSimulation omnet;
	private final MATSimSimulation matsim;
	private final Exchanger<Object> exchanger = new Exchanger<>();
	private Thread matSimThread;
	private long exchangeInterval;
	boolean taskStarted = false;
	
	public OMNeTMATSimSimulation(String matsimConfig, AdditionAwareAgentSource... agentSources) throws IOException {
		omnet = new OMNeTSimulation();
		matsim = new MATSimSimulation(matsimConfig, exchanger, agentSources);
	}

	/**
	 * Gets MATSim and OMNet dependencies 
	 */
	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		List<Class<? extends DEECoPlugin>> dependencies = new LinkedList<>();
		dependencies.addAll(omnet.getDependencies());
		dependencies.addAll(matsim.getDependencies());
		return dependencies;
	}

	/**
	 * Initializes both MATSim and OMNeT plug-ins
	 */
	@Override
	public void init(DEECoContainer container) {
		omnet.init(container);
		matsim.init(container);
		
		// Determine exchange interval
		exchangeInterval = matsim.getSimStepSize();
		
		// Start exchange task
		if(!taskStarted) {
			taskStarted = true;
			Scheduler scheduler = container.getRuntimeFramework().getScheduler();
			scheduler.addTask(new SimulationStepTask(scheduler, this, 0));
		}
	}

	@Override
	public Timer getTimer() {
		return timer;
	}
	
	/**
	 * Custom timer to start MATSim timer and OMNeT timer
	 * 
	 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
	 *
	 */
	class Timer implements SimulationTimer {
		@Override
		public void notifyAt(long time, TimerEventListener listener, DEECoContainer node) {
			omnet.getTimer().notifyAt(time, listener, node);
		}

		@Override
		public long getCurrentMilliseconds() {
			return omnet.getTimer().getCurrentMilliseconds();
		}

		@Override
		public void start(long duration) {
			OMNeTMATSimSimulation.this.runMATSim(duration);
			omnet.getTimer().start(duration);
		}
	}
	
	/**
	 * Periodically exchanges the data
	 */
	@Override
	public void at(long time, Object triger) {
		Log.d("DataExchangeTask");

		doExchange();

		// Reschedule exchange task
		SimulationStepTask task = (SimulationStepTask) triger;
		task.scheduleNextExecutionAfter(exchangeInterval);
	}
	
	/**
	 * Runs MATSim simulation in separate thread
	 * 
	 * @param duration
	 */
	private void runMATSim(long duration) {
		matSimThread = new Thread(new Runnable() {
			public void run() {
				matsim.getTimer().start(duration);
				System.out.println("MATSim ended");
			}
		});
		matSimThread.start();

		// Do the initial exchange, needed to initialize variables
		doExchange();
	}

	/**
	 * Does the data exchange between MATSim and OMNeT
	 */
	@SuppressWarnings("unchecked")
	private void doExchange() {
		// Do the exchange
		MATSimDataProvider provider = getMATSimProviderReceiver();
		MATSimDataReceiver receiver = getMATSimProviderReceiver();

		// Try to exchange until it succeeds, or the MATSim is done 
		while(matSimThread.isAlive()) {
			try {
				Map<Id, MATSimInput> out = provider.getMATSimData();
				Object in = exchanger.exchange(out, EXCHANGE_TIMEOUT_MS, TimeUnit.MILLISECONDS);
				receiver.setMATSimData((Map<Id, MATSimOutput>) in);
				break;
			} catch(TimeoutException e) {
				// This is expected when MATSim is busy
			} catch (InterruptedException e) {
				Log.e(getClass().getSimpleName(), e);
			}
		}
		
		omnet.updatePositions();
	}
	
	// #### Mimic MATSim interface ####
	@Override
	public MATSimRouter getRouter() {
		return matsim.getRouter();
	}

	@Override
	public MATSimDataProviderReceiver getMATSimProviderReceiver() {
		return matsim.getMATSimProviderReceiver();
	}

	@Override
	public void addVehicle(int vehicleId, Id startLink) {
		matsim.addVehicle(vehicleId, startLink);		
	}
	
	// #### Mimic OMNeT interface ####
	@Override
	public OMNeTHost getHost(int id) {
		return omnet.getHost(id);
	}
}
