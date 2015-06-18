package cz.cuni.mff.d3s.jdeeco.ros.test;

import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.timer.WallTimeTimer;
import cz.cuni.mff.d3s.jdeeco.ros.Actuators;
import cz.cuni.mff.d3s.jdeeco.ros.RosServices;
import cz.cuni.mff.d3s.jdeeco.ros.Sensors;

public class TestApp {

	public static void main(String[] args) {
		try {
			/*
			 * final SimulationTimer simulationTimer = new DiscreteEventTimer();
			 * final DEECoSimulation simulation = new
			 * DEECoSimulation(simulationTimer);
			 * simulation.addPlugin(Network.class);
			 * simulation.addPlugin(DefaultKnowledgePublisher.class);
			 * simulation.addPlugin(RosServices.class);
			 */

			// DEECoNode node = simulation.createNode(0);

			WallTimeTimer t = new WallTimeTimer();
			RosServices services = new RosServices();
			DEECoNode node = new DEECoNode(0, t, services);
			Sensors sensors = services.getService(Sensors.class);
			Actuators actuators = services.getService(Actuators.class);
			node.deployComponent(new TestComponent("testComponent", sensors,
					actuators));

			Log.i("Simulation started.");

			t.start();
			// simulation.start(120000);
			Log.i("Simulation finished.");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
