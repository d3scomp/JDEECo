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
import cz.cuni.mff.d3s.deeco.network.NetworkProvider;
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
	private long remainingExchanges;

	public MATSimOMNetSimulation(NetworkProvider np,
			MATSimDataReceiver matSimReceiver,
			MATSimDataProvider matSimProvider,
			final Collection<? extends AdditionAwareAgentSource> agentSources,
			String matSimConf) {
		super(np);

		this.controler = new MATSimPreloadingControler(matSimConf);
		this.controler.setOverwriteFiles(true);
		this.controler.getConfig().getQSimConfigGroup()
				.setSimStarttimeInterpretation("onlyUseStarttime");

		double end = this.controler.getConfig().getQSimConfigGroup()
				.getEndTime();
		double start = this.controler.getConfig().getQSimConfigGroup()
				.getStartTime();
		double step = this.controler.getConfig().getQSimConfigGroup()
				.getTimeStepSize();
		Log.i("Starting simulation: matsimStartTime: " + start
				+ " matsimEndTime: " + end);
		this.remainingExchanges = new Double((end - start) / step).longValue() + 1;

		this.exchanger = new Exchanger<Map<String, ?>>();
		this.listener = new JDEECoWithinDayMobsimListener(exchanger,
				this.remainingExchanges);
		this.matSimProvider = matSimProvider;
		this.matSimReceiver = matSimReceiver;

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

		this.simulationStep = new Double(step * MILLIS_IN_SECOND).longValue();
		/**
		 * Bind MATSim listener with the agent source. It is necessary to let
		 * the listener know about the jDEECo agents that it needs to update
		 * with data coming from a jDEECo runtime.
		 */
		for (AdditionAwareAgentSource source : agentSources) {
			if (source instanceof JDEECoAgentSource) {
				listener.registerAgentProvider((JDEECoAgentSource) source);
			}
		}
	}

	public MATSimOMNetSimulation(MATSimDataReceiver matSimReceiver,
			MATSimDataProvider matSimProvider,
			final Collection<? extends AdditionAwareAgentSource> agentSources,
			String matSimConf) {
		this(null, matSimReceiver, matSimProvider, agentSources, matSimConf);
	}

	public double getDuration() {
		double end = this.controler.getConfig().getQSimConfigGroup()
				.getEndTime();
		double start = this.controler.getConfig().getQSimConfigGroup()
				.getStartTime();
		return end - start;
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
			if (matSimThread.isAlive() && this.remainingExchanges > 0) {
				// long currentTime = getCurrentTime();
				// long matsimTime = getMATSimTime();
				matSimReceiver.setMATSimData(exchanger.exchange(matSimProvider
						.getMATSimData()));
				this.remainingExchanges--;
				// Log.w("jDEECo After data exchange at " + currentTime
				// + " MATSim time: " + matsimTime + " " + remainingExchanges);
				task.scheduleNextExecutionAfter(simulationStep);
			}
		} catch (InterruptedException e) {
			Log.e("MATSimOMNetSimulation", e);
		}
	}

	public long getMATSimSeconds() {
		long currentTime = getCurrentMilliseconds();
		return Math.round((currentTime / MILLIS_IN_SECOND)
				+ controler.getConfig().getQSimConfigGroup().getStartTime());
	}

	public long getMATSimMilliseconds() {
		return getCurrentMilliseconds()
				+ (Math.round(controler.getConfig().getQSimConfigGroup()
						.getStartTime()) * MILLIS_IN_SECOND);
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
