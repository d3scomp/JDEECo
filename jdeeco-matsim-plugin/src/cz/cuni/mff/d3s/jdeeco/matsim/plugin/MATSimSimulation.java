package cz.cuni.mff.d3s.jdeeco.matsim.plugin;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CyclicBarrier;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.events.StartupEvent;
import org.matsim.core.controler.listener.StartupListener;
import org.matsim.core.mobsim.framework.Mobsim;
import org.matsim.core.router.util.TravelTime;
import org.matsim.withinday.trafficmonitoring.TravelTimeCollector;
import org.matsim.withinday.trafficmonitoring.TravelTimeCollectorFactory;

import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.timer.SimulationTimer;
import cz.cuni.mff.d3s.deeco.timer.TimerEventListener;
import cz.cuni.mff.d3s.jdeeco.matsim.dataaccess.MATSimDataProviderReceiver;
import cz.cuni.mff.d3s.jdeeco.matsim.simulation.AdditionAwareAgentSource;
import cz.cuni.mff.d3s.jdeeco.matsim.simulation.DefaultMATSimExtractor;
import cz.cuni.mff.d3s.jdeeco.matsim.simulation.DefaultMATSimUpdater;
import cz.cuni.mff.d3s.jdeeco.matsim.simulation.JDEECoAgent;
import cz.cuni.mff.d3s.jdeeco.matsim.simulation.JDEECoAgentSource;
import cz.cuni.mff.d3s.jdeeco.matsim.simulation.JDEECoMobsimFactory;
import cz.cuni.mff.d3s.jdeeco.matsim.simulation.JDEECoWithinDayMobsimListener;
import cz.cuni.mff.d3s.jdeeco.matsim.simulation.MATSimDataProvider;
import cz.cuni.mff.d3s.jdeeco.matsim.simulation.MATSimDataReceiver;
import cz.cuni.mff.d3s.jdeeco.matsim.simulation.MATSimExtractor;
import cz.cuni.mff.d3s.jdeeco.matsim.simulation.MATSimPreloadingControler;
import cz.cuni.mff.d3s.jdeeco.matsim.simulation.MATSimRouter;
import cz.cuni.mff.d3s.jdeeco.matsim.simulation.MATSimSimulationStepListener;
import cz.cuni.mff.d3s.jdeeco.matsim.simulation.Simulation;

/**
 * Plug-in providing MATSim simulation
 * 
 * Based on the code from the jDEECo 2 simulation project
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class MATSimSimulation implements DEECoPlugin {
	private final Timer timer = new Timer();
	private final JDEECoAgentSource agentSource = new JDEECoAgentSource();
	private final MATSimRouter router;
	private final MATSimDataProviderReceiver matSimProviderReceiver = new MATSimDataProviderReceiver(
			new LinkedList<String>());	
	private long currentMilliseconds;
	private final long simulationStep; // in milliseconds
	private final TravelTime travelTime;
	protected final Controler controler;
	private final JDEECoWithinDayMobsimListener listener;
	private final MATSimDataProvider matSimProvider;
	private final MATSimDataReceiver matSimReceiver;
	private final Map<Integer, MATSimHost> hosts = new HashMap<>();
	private final MATSimExtractor extractor;
//	protected final Exchanger<Object> exchanger;
	protected final CyclicBarrier barrier;

	public MATSimSimulation(String configPath, AdditionAwareAgentSource... additionalAgentSources) throws IOException {
		this(configPath, null, additionalAgentSources);
	}
	
	public MATSimSimulation(String configPath,/* Exchanger<Object> exchanger*/ CyclicBarrier barrier, AdditionAwareAgentSource... additionalAgentSources) throws IOException {
		//his.exchanger = exchanger;
		this.barrier = barrier;
		List<AdditionAwareAgentSource> agentSources = new LinkedList<>();
		agentSources.add(agentSource);
		agentSources.addAll(Arrays.asList(additionalAgentSources));

		// Setup MATSim controller
		controler = new MATSimPreloadingControler(configPath);
		controler.setOverwriteFiles(true);
		controler.getConfig().getQSimConfigGroup().setSimStarttimeInterpretation("onlyUseStarttime");

		// Get simulation times
		final double end = this.controler.getConfig().getQSimConfigGroup().getEndTime();
		final double start = this.controler.getConfig().getQSimConfigGroup().getStartTime();
		final double step = this.controler.getConfig().getQSimConfigGroup().getTimeStepSize();
		Log.i("Starting simulation: matsimStartTime: " + start + " matsimEndTime: " + end);
		
		extractor = new DefaultMATSimExtractor();
		if(barrier == null) {
			listener = new JDEECoWithinDayMobsimListener(timer, new DefaultMATSimUpdater());
		} else {
			listener = new JDEECoWithinDayMobsimListener(timer, barrier, new DefaultMATSimUpdater());
		}
		matSimProvider = (MATSimDataProvider) matSimProviderReceiver;
		matSimReceiver = (MATSimDataReceiver) matSimProviderReceiver;

		Set<String> analyzedModes = new HashSet<String>();
		analyzedModes.add(TransportMode.car);
		travelTime = new TravelTimeCollectorFactory().createTravelTimeCollector(controler.getScenario(), analyzedModes);

		controler.addControlerListener(new StartupListener() {
			public void notifyStartup(StartupEvent event) {
				controler.getEvents().addHandler((TravelTimeCollector) travelTime);
				controler.getMobsimListeners().add((TravelTimeCollector) travelTime);
				controler.setMobsimFactory(new JDEECoMobsimFactory(listener, agentSources));
			}
		});
		
		/**
		 * Bind MATSim listener with the agent source. It is necessary to let the listener know about the jDEECo agents
		 * that it needs to update with data coming from a jDEECo runtime.
		 */
		for (AdditionAwareAgentSource source : agentSources) {
			if (source instanceof JDEECoAgentSource) {
				listener.registerAgentProvider((JDEECoAgentSource) source);
			}
		}

		simulationStep = Timer.secondsToMilliseconds(step);
		currentMilliseconds = Timer.secondsToMilliseconds(controler.getConfig().getQSimConfigGroup().getStartTime());

		router = new MATSimRouter(controler, travelTime, 10 /* TODO: FAKE VALUE */);
	}

	public Timer getTimer() {
		return timer;
	}

	public MATSimRouter getRouter() {
		return router;
	}

	public MATSimDataProviderReceiver getMATSimProviderReceiver() {
		return matSimProviderReceiver;
	}

	public void addVehicle(int vehicleId, Id startLink) {
		agentSource.addAgent(new JDEECoAgent(new IdImpl(vehicleId), startLink));
	}

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		// No dependencies
		return new LinkedList<Class<? extends DEECoPlugin>>();
	}

	@Override
	public void init(DEECoContainer container) {
		MATSimHost host = new MATSimHost(String.valueOf(container.getId()), getTimer());
		addHost(container.getId(), host);
	}
	
	private void addHost(int id, cz.cuni.mff.d3s.jdeeco.matsim.plugin.MATSimHost host) {
		hosts.put(id, host);
	}

	private MATSimHost getHost(int id) {
		return hosts.get(id);
	}
	
	private void run(long duration) {
		double startTime = controler.getConfig().getQSimConfigGroup().getStartTime();
		double endTime = startTime + (((double) (duration)) / 1000);
		controler.getConfig().getQSimConfigGroup().setEndTime(endTime);
		controler.run();
	}
	
	/**
	 * Class implementing timer for MATSim simulation
	 * 
	 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
	 *
	 */
	public class Timer extends Simulation implements SimulationTimer, MATSimSimulationStepListener {
		private final TreeSet<Callback> callbacks = new TreeSet<>();
		private final Map<Integer, Callback> hostIdToCallback = new HashMap<>();
		
		@Override
		public void notifyAt(long time, TimerEventListener listener, DEECoContainer node) {
			callAt(time, node.getId());
			MATSimSimulation.this.getHost(node.getId()).listener = listener;
		}

		@Override
		public synchronized void callAt(long absoluteTime, int hostId) {
			Callback callback = hostIdToCallback.remove(hostId);
			if (callback != null) {
				callbacks.remove(callback);
			}
			callback = new Callback(hostId, absoluteTime);
			hostIdToCallback.put(hostId, callback);
			callbacks.add(callback);
		}

		@Override
		public long getCurrentMilliseconds() {
			return MATSimSimulation.this.currentMilliseconds;
		}

		@Override
		public void start(long duration) {
			MATSimSimulation.this.run(duration);
		}

		@Override
		public void at(double seconds, Mobsim mobsim) {
			// Exchange data with MATSim
			long milliseconds = secondsToMilliseconds(seconds);
			matSimReceiver.setMATSimData(extractor.extractFromMATSim(listener.getAllJDEECoAgents(), mobsim));
			listener.updateJDEECoAgents(matSimProvider.getMATSimData());
			// Add callback for the MATSim step
			callAt(milliseconds + simulationStep, Callback.SIMULATION_CALLBACK_HOSTID);
			MATSimHost host;
			Callback callback;
			// Iterate through all the call-backs until the MATSim callback.
			while (!callbacks.isEmpty()) {
				callback = callbacks.pollFirst();
				if (callback.isSimulationCallaback()) {
					break;
				}
				currentMilliseconds = callback.getAbsoluteTime();
				// System.out.println("At: " + currentMilliseconds);
				host = hosts.get(callback.hostId);
				host.at(millisecondsToSeconds(currentMilliseconds));
			}
		}
	}

	private class Callback implements Comparable<Callback> {
		static final int SIMULATION_CALLBACK_HOSTID = -1;
		
		private final long milliseconds;
		private final int hostId;

		public Callback(int hostId, long milliseconds) {
			this.hostId = hostId;
			this.milliseconds = milliseconds;
		}

		public long getAbsoluteTime() {
			return milliseconds;
		}

		public int getHostId() {
			return hostId;
		}
		
		public boolean isSimulationCallaback() {
			return getHostId() == SIMULATION_CALLBACK_HOSTID;
		}

		@Override
		public int compareTo(Callback c) {
			if (c.getAbsoluteTime() < milliseconds) {
				return 1;
			} else if (c.getAbsoluteTime() > milliseconds) {
				return -1;
			} else if (this == c) {
				return 0;
			} else {
				return this.hashCode() < c.hashCode() ? 1 : -1;
			}
		}

		public String toString() {
			return hostId + " " + milliseconds;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + hostId;
			result = prime * result + (int) (milliseconds ^ (milliseconds >>> 32));
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Callback other = (Callback) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (hostId != other.hostId)
				return false;
			if (milliseconds != other.milliseconds)
				return false;
			return true;
		}

		private MATSimSimulation getOuterType() {
			return MATSimSimulation.this;
		}
	}
}
