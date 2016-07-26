package cz.cuni.mff.d3s.jdeeco.ros.sim;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoRuntimeException;
import cz.cuni.mff.d3s.deeco.runtime.PluginInitFailedException;
import cz.cuni.mff.d3s.deeco.timer.SimulationTimer;
import cz.cuni.mff.d3s.jdeeco.position.PositionPlugin;
import cz.cuni.mff.d3s.jdeeco.ros.RosServices;
import cz.cuni.mff.d3s.jdeeco.simulation.SimulationProvider;
import cz.cuni.mff.d3s.rosremote.server.ConfigIntf;
import cz.cuni.mff.d3s.rosremote.server.ServerIntf;

public class ROSSimulation implements SimulationProvider {
	/**
	 * Extension of ROS services for simulation
	 * 
	 * Basically this class adds support for creating ROS configuration from DEECo plugins
	 * 
	 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
	 *
	 */
	class SimulationROSServices extends RosServices {
		private final String color;
		private DEECoContainer container;

		public SimulationROSServices(String ros_master, String ros_host, String color) {
			super(ros_master, ros_host, String.format("/robot_%d", turtleboCounter++));
			this.color = color;
			simulationROSServices.add(this);
		}

		public SimulationROSServices(String ros_master, String ros_host) {
			this(ros_master, ros_host, "black");
		}

		@Override
		public void init(DEECoContainer container) throws PluginInitFailedException {
			super.init(container);
			this.container = container;
		}

		public void configureTurtlebot(ConfigIntf config) throws RemoteException {
			PositionPlugin positionPlugin = container.getPluginInstance(PositionPlugin.class);

			// Guard for missing position plugin
			if (positionPlugin == null) {
				throw new DEECoRuntimeException(
						"Position plugin is not defined for node, but ROS simulation needs to know initial position");
			}

			config.addTurtlebot(color, positionPlugin.getStaticPosition().x, positionPlugin.getStaticPosition().y);
		}
	}

	/**
	 * Counts turtlebots in simulation in order to assign name-spaces
	 */
	int turtleboCounter = 0;

	/**
	 * Simulation ROS services for this simulation
	 * 
	 * These needs to be sorted as we already defined name-spaces at construction time. Services name-spaces needs to
	 * match order of robots in the configuration object.
	 */
	List<SimulationROSServices> simulationROSServices = new LinkedList<>();

	/**
	 * The ROS_MASTER_URI value.
	 * 
	 * Used to connect to ROS master via ROS protocol
	 */
	private final String ros_master_uri;

	/**
	 * The ROS_MASTER
	 * 
	 * Used to connect to ROS master in order to start up the ROS simulation
	 */
	private final String ros_master;

	/**
	 * The ROS_HOST address.
	 */
	private final String ros_host;
	
	/**
	 * Map prefix for simulation
	 */
	private final String mapPrefix;
	
	/**
	 * Simulation resolution
	 */
	private final double resolution;
	
	/**
	 * Simulation time-step in milliseconds
	 */
	private final long intervalMs;

	/**
	 * ROS simulation timer
	 */
	private final ROSTimer timer;

	/**
	 * Simulation identifier used by remote simulation runner
	 */
	private int simulationId;

	/**
	 * Remote ROS simulation server
	 */
	ServerIntf server;

	/**
	 * Creates ROS simulation
	 * 
	 * @param ros_master
	 *            ROS machine hostname or IP address
	 * @param ros_host
	 *            Local hostname or IP address
	 */
	public ROSSimulation(String ros_master, int ros_master_port, String ros_host, String mapPrefix, double resolution, long intervalMs) {
		this.ros_master = ros_master;
		this.ros_host = ros_host;
		this.mapPrefix = mapPrefix;
		this.resolution = resolution;
		this.intervalMs = intervalMs;
		this.ros_master_uri = String.format("http://%s:%d", ros_master, ros_master_port);

		timer = new ROSTimer(this);
	}

	/**
	 * Creates ROS services instance with parameters specific to this simulation
	 * 
	 * @return RosServices instance for robot
	 */
	public RosServices createROSServices() {
		return new SimulationROSServices(ros_master_uri, ros_host);
	}
	
	/**
	 * Creates ROS services instance with parameters specific to this simulation
	 * 
	 * @param color Robot color
	 * @return RosServices instance for robot
	 */
	public RosServices createROSServices(String color) {
		return new SimulationROSServices(ros_master_uri, ros_host, color);
	}

	/**
	 * Provides simulation timer
	 * 
	 * ROS simulation is not proper simulation, it simulates hardware, but not time compression. Simulated hardware runs
	 * at "almost" wall time and the controller (jDEECo) has to keep up with it.
	 *
	 */
	@Override
	public SimulationTimer getTimer() {
		// return new ROSTimer();
		return timer;
	}

	public String getROSHost() {
		return ros_host;
	}

	public String getROSMaster() {
		return ros_master;
	}

	public String getROSMasterURI() {
		return ros_master_uri;
	}

	/**
	 * Starts remote ROS simulation
	 * 
	 * Expected to be called by timer when simulation is about to start
	 */
	void startSimulation() {
		try {
			server = (ServerIntf) Naming.lookup(String.format("//%s/ROSRemote", ros_master));

			ConfigIntf conf = server.createConfig(mapPrefix, resolution, intervalMs);
			for(SimulationROSServices simROSService: simulationROSServices) {
				simROSService.configureTurtlebot(conf);
			}
			
			conf.setStageWindow(640, 480, 15, 8, 0, 0, 15);

			System.out.println("Running simulation");
			simulationId = server.startSimulation(conf);
		} catch (InterruptedException | NotBoundException | IOException e) {
			throw new DEECoRuntimeException("ROS Simulation start has thrown an exception", e);
		}
	}

	/**
	 * Stops remote ROS simulation
	 * 
	 * Expected to be called by timer when simulation should end
	 */
	void stopSimulation() {
		try {
			server.stopSimulaiton(simulationId);
		} catch (InterruptedException | RemoteException e) {
			throw new DEECoRuntimeException("ROS simulation stop failed with exception", e);
		}
	}
}
