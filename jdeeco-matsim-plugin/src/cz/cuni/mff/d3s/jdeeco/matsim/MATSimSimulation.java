package cz.cuni.mff.d3s.jdeeco.matsim;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.matsim.api.core.v01.Id;
import org.matsim.core.basic.v01.IdImpl;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.timer.CurrentTimeProvider;
import cz.cuni.mff.d3s.deeco.timer.SimulationTimer;
import cz.cuni.mff.d3s.deeco.timer.TimerEventListener;
import cz.cuni.mff.d3s.jdeeco.matsim.old.matsim.DefaultMATSimExtractor;
import cz.cuni.mff.d3s.jdeeco.matsim.old.matsim.DefaultMATSimUpdater;
import cz.cuni.mff.d3s.jdeeco.matsim.old.matsim.JDEECoAgent;
import cz.cuni.mff.d3s.jdeeco.matsim.old.matsim.JDEECoAgentSource;
import cz.cuni.mff.d3s.jdeeco.matsim.old.matsim.MATSimRouter;
import cz.cuni.mff.d3s.jdeeco.matsim.old.roadtrains.MATSimDataProviderReceiver;
import cz.cuni.mff.d3s.jdeeco.matsim.old.simulation.DirectSimulationHost;

/**
 * Plug-in providing MATSim simulation
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class MATSimSimulation implements DEECoPlugin {
	class TimerProvider implements SimulationTimer {
		@Override
		public void notifyAt(long time, TimerEventListener listener, DEECoContainer node) {
			// System.out.println("NOTIFY AT CALLED FOR: " + time + " NODE:" + node.getId());
			MATSimSimulation.this.oldSimulation.callAt(time, String.valueOf(node.getId()));
			hosts.get(node.getId()).listener = listener;
		}

		@Override
		public long getCurrentMilliseconds() {
			return MATSimSimulation.this.oldSimulation.getCurrentMilliseconds();
		}

		@Override
		public void start(long duration) {
			MATSimSimulation.this.oldSimulation.getControler().getConfig().getQSimConfigGroup()
					.setEndTime(((double) (duration)) / 1000);
			MATSimSimulation.this.oldSimulation.run();
		}
	}

	class Host extends DirectSimulationHost {
		public TimerEventListener listener;

		public Host(String id, CurrentTimeProvider timeProvider) {
			super(id, timeProvider);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void at(double absoluteTime) {
			// System.out.println("CALLBACK CALLED AT: " + getCurrentMilliseconds() + " NODE: " + getHostId());
			listener.at(getCurrentMilliseconds());
		}

	}

	private final Map<Integer, Host> hosts = new HashMap<>();

	private final TimerProvider timer = new TimerProvider();
	private final cz.cuni.mff.d3s.jdeeco.matsim.old.matsim.MATSimSimulation oldSimulation;
	private final JDEECoAgentSource agentSource = new JDEECoAgentSource();
	private final MATSimRouter router;
	private final MATSimDataProviderReceiver matSimProviderReceiver = new MATSimDataProviderReceiver(
			new LinkedList<String>());

	public MATSimSimulation(String mapFile) throws IOException {
		File config = MATSimConfigGenerator.writeToTemp(mapFile);

		oldSimulation = new cz.cuni.mff.d3s.jdeeco.matsim.old.matsim.MATSimSimulation(matSimProviderReceiver,
				matSimProviderReceiver, new DefaultMATSimUpdater(), new DefaultMATSimExtractor(),
				Arrays.asList(agentSource), config.getAbsolutePath());

		router = new MATSimRouter(oldSimulation.getControler(), oldSimulation.getTravelTime(), 10 /* TODO: FAKE VALUE */);
	}

	public SimulationTimer getTimer() {
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
		Host host = new Host(String.valueOf(container.getId()), oldSimulation);

		hosts.put(container.getId(), host);

		oldSimulation.addHost(String.valueOf(container.getId()), host);
	}
}
