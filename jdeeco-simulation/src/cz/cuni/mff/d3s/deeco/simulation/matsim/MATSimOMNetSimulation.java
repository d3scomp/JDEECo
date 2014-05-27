package cz.cuni.mff.d3s.deeco.simulation.matsim;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.matsim.api.core.v01.TransportMode;
import org.matsim.core.controler.events.StartupEvent;
import org.matsim.core.controler.listener.StartupListener;
import org.matsim.core.router.util.TravelTime;
import org.matsim.withinday.trafficmonitoring.TravelTimeCollector;
import org.matsim.withinday.trafficmonitoring.TravelTimeCollectorFactory;

import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.simulation.SimulationStepListener;
import cz.cuni.mff.d3s.deeco.simulation.omnet.OMNetSimulation;
import cz.cuni.mff.d3s.deeco.simulation.task.SimulationStepTask;

/**
 * Main simulation class.
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public class MATSimOMNetSimulation extends OMNetSimulation implements
		SimulationStepListener {

	private static final int MATSIM_WAITING_TIME = 10; // in milliseconds
	private static final int MILLIS_IN_SECOND = 1000;

	private final Exchanger<Map<String, ?>> exchanger;
	private final MATSimDataProvider matSimProvider;
	private final MATSimDataReceiver matSimReceiver;
	private final MATSimPreloadingControler controler;
	private final TravelTime travelTime;
	private final long simulationStep; // in milliseconds
	private final JDEECoWithinDayMobsimListener listener;
	private final MATSimOMNetCoordinatesTranslator positionTranslator;

	private Thread matSimThread;

	public MATSimOMNetSimulation(MATSimDataReceiver matSimReceiver,
			MATSimDataProvider matSimProvider,
			final Collection<? extends AdditionAwareAgentSource> agentSources,
			String matSimConf) {

		this.exchanger = new Exchanger<Map<String, ?>>();
		this.listener = new JDEECoWithinDayMobsimListener(exchanger);
		this.matSimProvider = matSimProvider;
		this.matSimReceiver = matSimReceiver;

		this.controler = new MATSimPreloadingControler(matSimConf);
		this.controler.setOverwriteFiles(true);

		this.positionTranslator = new MATSimOMNetCoordinatesTranslator(
				controler.getNetwork());

		Set<String> analyzedModes = new HashSet<String>();
		analyzedModes.add(TransportMode.car);
		travelTime = new TravelTimeCollectorFactory()
				.createTravelTimeCollector(controler.getScenario(),
						analyzedModes);

		controler.addControlerListener(new StartupListener() {
			public void notifyStartup(StartupEvent event) {
				controler.getEvents().addHandler(
						(TravelTimeCollector) travelTime);
				controler.getMobsimListeners().add(
						(TravelTimeCollector) travelTime);
				controler.setMobsimFactory(new JDEECoMobsimFactory(listener,
						agentSources));
			}
		});

		this.simulationStep = Math.round(controler.getConfig()
				.getQSimConfigGroup().getTimeStepSize())
				* MILLIS_IN_SECOND;
		/**
		 * Bind MATSim listener with the agent source. It is necessary to let
		 * the listener know about the jDEECo agents that it needs to update
		 * with data coming from a jDEECo runtime.
		 */
		for (AdditionAwareAgentSource source: agentSources) {
			if (source instanceof JDEECoAgentSource) {
				listener.registerAgentProvider((JDEECoAgentSource) source);
			}
		}
	}

	public void at(long time, SimulationStepTask task) {
		try {
			if (matSimThread == null) {
				matSimThread = new Thread(new Runnable() {

					public void run() {
						controler.run();
						System.out.println("MATSim ended");
					}

				});
				matSimThread.start();
			}
			if (matSimThread.isAlive()) {
//				long currentTime = getCurrentTime();
//				long matsimTime = getMATSimTime();
				boolean loop = true;
				while (loop) {
					try {
						matSimReceiver.setMATSimData(exchanger.exchange(
								matSimProvider.getMATSimData(),
								MATSIM_WAITING_TIME, TimeUnit.MILLISECONDS));
//						Log.w("jDEECo After data exchange at " + currentTime
//								+ " MATSim time: " + matsimTime);
						loop = false;
					} catch (TimeoutException e) {
						// Wait for the MATSim thread.
						loop = matSimThread.isAlive();
					}
				}
				task.scheduleNextExecutionAfter(simulationStep);
			}
		} catch (InterruptedException e) {
			Log.e("MATSimOMNetSimulation", e);
		}
	}

	public long getMATSimTime() {
		long currentTime = getCurrentTime();
		return Math.round((currentTime / MILLIS_IN_SECOND)
				+ controler.getConfig().getQSimConfigGroup()
						.getStartTime());
	}

	public MATSimPreloadingControler getControler() {
		return controler;
	}

	public TravelTime getTravelTime() {
		return travelTime;
	}

	public MATSimOMNetCoordinatesTranslator getPositionTranslator() {
		return positionTranslator;
	}

	public void run(String environment, String configFile) {
		throw new UnsupportedOperationException(
				"Use run(double matsimStartTime, String environment, String configFile)");
	}

	public void run(double matsimStartTime, double matsimEndTime,
			String environment, String configFile) {
		controler.getConfig().getQSimConfigGroup().setEndTime(matsimEndTime);
		controler.getConfig().getQSimConfigGroup()
				.setStartTime(matsimStartTime);
		super.run(environment, configFile);
		System.out.println("OMNet ended");
		try {
			if (matSimThread != null) {
				matSimThread.join();
			}
		} catch (Exception e) {
			Log.e("MATSimOMNetSimulation", e);
		}
	}
}
