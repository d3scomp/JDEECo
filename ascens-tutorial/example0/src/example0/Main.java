package example0;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.runners.DEECoSimulation;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoException;
import cz.cuni.mff.d3s.deeco.timer.DiscreteEventTimer;
import cz.cuni.mff.d3s.deeco.timer.SimulationTimer;

public class Main {
	public static void main(String[] args) throws AnnotationProcessorException, DEECoException, InstantiationException, IllegalAccessException {
		// Simulation setup
		SimulationTimer timer = new DiscreteEventTimer();
		DEECoSimulation simulation = new DEECoSimulation(timer);
		
		// Node setup
		DEECoContainer node = simulation.createNode();
		node.deployComponent(new HelloWorld("HELLO"));
		
		// Run the simulation for 10 seconds
		simulation.start(10000);
	}
}
