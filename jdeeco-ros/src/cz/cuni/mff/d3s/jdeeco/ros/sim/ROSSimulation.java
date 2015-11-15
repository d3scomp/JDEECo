package cz.cuni.mff.d3s.jdeeco.ros.sim;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoRuntimeException;
import cz.cuni.mff.d3s.deeco.runtime.PluginInitFailedException;
import cz.cuni.mff.d3s.deeco.timer.SimulationTimer;
import cz.cuni.mff.d3s.jdeeco.ros.RosServices;
import cz.cuni.mff.d3s.jdeeco.simulation.SimulationProvider;
import cz.cuni.mff.d3s.rosremote.server.ConfigIntf;
import cz.cuni.mff.d3s.rosremote.server.ServerIntf;

public class ROSSimulation implements SimulationProvider {
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
	public ROSSimulation(String ros_master, int ros_master_port, String ros_host) {
		this.ros_master = ros_master;
		this.ros_host = ros_host;
		this.ros_master_uri = String.format("http://%s:%d", ros_master, ros_master_port);

		timer = new ROSTimer(this);
	}

	/**
	 * Creates ROS services instance with parameters specific to this simulation
	 * 
	 * @param namespace
	 *            Robot namespace
	 * @return RosServices instance for robot
	 */
	public RosServices createROSServices(String namespace) {
		return new RosServices(ros_master_uri, ros_host, namespace) {
			@Override
			public void init(DEECoContainer container) throws PluginInitFailedException {
				super.init(container);
				// TODO: Add probes in order to detect simulation configuration
			}
		};
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
			server = (ServerIntf) Naming.lookup("//192.168.56.101/ROSRemote");

			ConfigIntf conf = server.createConfig("corridor", 0.02, 100);
			conf.addTurtlebot(12, 12);
			conf.addTurtlebot(25, 12);
			conf.setStageWindow(640, 480, 15, 8, 0, 0, 15);
	
			System.out.println("Running simulation");
			simulationId = server.startSimulation(conf);
		} catch(InterruptedException | NotBoundException | IOException e) {
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
		} catch(InterruptedException | RemoteException e) {
			throw new DEECoRuntimeException("ROS simulation stop failed with exception", e);
		}
	}
}
