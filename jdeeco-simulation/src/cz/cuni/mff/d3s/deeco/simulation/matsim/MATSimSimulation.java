package cz.cuni.mff.d3s.deeco.simulation.matsim;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Exchanger;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.events.StartupEvent;
import org.matsim.core.controler.listener.StartupListener;
import org.matsim.core.router.util.TravelTime;
import org.matsim.withinday.trafficmonitoring.TravelTimeCollector;
import org.matsim.withinday.trafficmonitoring.TravelTimeCollectorFactory;

import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.network.Host;
import cz.cuni.mff.d3s.deeco.network.NetworkProvider;
import cz.cuni.mff.d3s.deeco.simulation.Simulation;
import cz.cuni.mff.d3s.deeco.simulation.SimulationHost;

public class MATSimSimulation extends Simulation {

	private static final int MILLIS_IN_SECOND = 1000;
	
	private long currentMilliseconds = 0;
	private final TreeSet<Callback> callbacks;
	private final Map<String, Callback> hostIdToCallback;
	private final MATSimPreloadingControler controler;
	private final Exchanger<Map<Id, ?>> exchanger;
	private final JDEECoWithinDayMobsimListener listener;
	private final MATSimDataProvider matSimProvider;
	private final MATSimDataReceiver matSimReceiver;
	private final TravelTime travelTime;
	
	public MATSimSimulation(NetworkProvider np, MATSimDataReceiver matSimReceiver,
			MATSimDataProvider matSimProvider,
			final Collection<? extends AdditionAwareAgentSource> agentSources,
			String matSimConf) {
		super(np);
		this.callbacks = new TreeSet<>();
		this.hostIdToCallback = new HashMap<>();

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
		this.exchanger = new Exchanger<Map<Id, ?>>();
		this.listener = new JDEECoWithinDayMobsimListener(exchanger);
		this.matSimProvider = matSimProvider;
		this.matSimReceiver = matSimReceiver;

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
	
	public Controler getControler() {
		return this.controler;
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
	
	public TravelTime getTravelTime() {
		return this.travelTime;
	}

	@Override
	public long getCurrentMilliseconds() {
		return currentMilliseconds;
	}

	@Override
	public synchronized void callAt(long absoluteTime, String hostId) {
		Callback callback = hostIdToCallback.remove(hostId);
		if (callback != null) {
			callbacks.remove(callback);
		}
		callback = new Callback(hostId, absoluteTime);
		hostIdToCallback.put(hostId, callback);
		callbacks.add(callback);
	}
	
	public synchronized void run() {
		Callback callback;
		SimulationHost host;
		while (!callbacks.isEmpty()) {
			callback = callbacks.pollFirst();
			currentMilliseconds = callback.getAbsoluteTime();
			host = (SimulationHost) networkProvider.getNetworkInterfaceByNetworkAddress(callback.hostId);
			host.at(currentMilliseconds);
			
		}
	}
	
	private class Callback implements Comparable<Callback> {

		private final long absoluteTime;
		private final String hostId;
		
		public Callback(String hostId, long absoluteTime) {
			this.hostId = hostId;
			this.absoluteTime = absoluteTime;
		}
		
		public long getAbsoluteTime() {
			return absoluteTime;
		}

		public String getHostId() {
			return hostId;
		}

		@Override
		public int compareTo(Callback c) {
			if (c.getAbsoluteTime() < absoluteTime) {
				return 1;
			} else if (c.getAbsoluteTime() > absoluteTime) {
				return -1;
			} else {
				return 0;
			}
		}
	}

}
