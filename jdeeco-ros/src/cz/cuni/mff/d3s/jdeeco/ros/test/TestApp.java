package cz.cuni.mff.d3s.jdeeco.ros.test;

import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.runners.DEECoSimulation;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.timer.DiscreteEventTimer;
import cz.cuni.mff.d3s.deeco.timer.SimulationTimer;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.publishing.DefaultKnowledgePublisher;
import cz.cuni.mff.d3s.jdeeco.ros.RosServices;

public class TestApp {

	public static void main(String[] args) {
		try
        {
                final SimulationTimer simulationTimer = new DiscreteEventTimer();
                final DEECoSimulation simulation = new DEECoSimulation(simulationTimer);
                simulation.addPlugin(Network.class);
                simulation.addPlugin(DefaultKnowledgePublisher.class);
                simulation.addPlugin(RosServices.class);

                DEECoNode node = simulation.createNode(0);
                node.deployComponent(new TestComponent("testComponent"));

                Log.i("Simulation started.");
                simulation.start(120000);
                Log.i("Simulation finished.");
        } catch (Exception e){
                e.printStackTrace();
        }


	}

}
