package cz.cuni.mff.d3s.deeco.simulation.matsim;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Exchanger;

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

public class MATSimOMNetSimulation extends OMNetSimulation implements SimulationStepListener {

	private final Exchanger<Map<String, ?>> exchanger;
	private final MATSimDataProvider matSimProvider;
	private final MATSimDataReceiver matSimReceiver;
	private final Collection<? extends AdditionAwareAgentSource> agentSources;
	private final MATSimPreloadingControler controler;
	private final TravelTime travelTime;
	private final long simulationStep; // in milliseconds
	private final long simulationEndTime; // in milliseconds
	private final JDEECoWithinDayMobsimListener listener;
	private final MATSimOMNetCoordinatesTranslator positionTranslator;

	private Thread matSimThread;
	private long remainingExchanges;

	public MATSimOMNetSimulation(
			MATSimDataReceiver matSimReceiver, MATSimDataProvider matSimProvider,
			Collection<? extends AdditionAwareAgentSource> agentSources, String matSimConf) {

		this.exchanger = new Exchanger<Map<String, ?>>();
		this.listener = new JDEECoWithinDayMobsimListener(exchanger);
		this.matSimProvider = matSimProvider;
		this.matSimReceiver = matSimReceiver;
		this.agentSources = agentSources;
		
		this.controler = new MATSimPreloadingControler(matSimConf);
		this.controler.setOverwriteFiles(true);
		
		this.positionTranslator = new MATSimOMNetCoordinatesTranslator(controler.getNetwork());

		Set<String> analyzedModes = new HashSet<String>();
		analyzedModes.add(TransportMode.car);
		travelTime = new TravelTimeCollectorFactory()
				.createTravelTimeCollector(controler.getScenario(),
						analyzedModes);

		this.simulationStep = Math.round(controler.getConfig()
				.getQSimConfigGroup().getTimeStepSize())
				* Constants.MILLIS_IN_SECOND;
		this.simulationEndTime = Math.round(controler.getConfig()
				.getQSimConfigGroup().getEndTime()
				* Constants.MILLIS_IN_SECOND);
		this.remainingExchanges = (this.simulationEndTime / this.simulationStep) + 1;
	}

	public void at(long time, SimulationStepTask task) {
		try {
			if (remainingExchanges > 0) {
				if (matSimThread == null) {
					matSimThread = new Thread(new Runnable() {

						public void run() {
							controler.run();
							System.out.println("MATSim ended");
						}

					});
					matSimThread.start();
				}
				//Log.w("jDEECo Before data exchange at " + getCurrentTime());
				matSimReceiver.setMATSimData(exchanger
						.exchange(matSimProvider.getMATSimData()));
				//Log.w("jDEECo After data exchange at " + getCurrentTime());
				task.scheduleNextExecutionAfter(simulationStep);
				remainingExchanges--;
			}
		} catch (InterruptedException e) {
			Log.e("MATSimOMNetSimulation", e);
		}
	}
	
	public JDEECoWithinDayMobsimListener getJDEECoMobsimListener() {
		return listener;
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

	/**
	 * As the source needs to be correlated with the DEECo model being deployed,
	 * it needs to come from the outside. As a result the simulation listener is
	 * returned.
	 * 
	 */
	public void initialize() {
		// Initialize OMNet part
		super.initialize();

		// Initialize MATSim part
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
		
		
	}

	public void run(String environment, String configFile) {
		super.run(environment, configFile);
		System.out.println("OMNet ended");
		try {
			if (matSimThread != null) {
				matSimThread.join();
				matSimThread = null;
			}
		} catch (Exception e) {
			Log.e("MATSimOMNetSimulation", e);
		}
	}
}
