package cz.cuni.mff.d3s.deeco.demo.intelligent;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.demo.ensembles.Robot;
import cz.cuni.mff.d3s.deeco.demo.ensembles.SimpleEnsembleFactory;
import cz.cuni.mff.d3s.deeco.runners.DEECoSimulation;
import cz.cuni.mff.d3s.deeco.runtime.DEECoException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.timer.DiscreteEventTimer;
import cz.cuni.mff.d3s.deeco.timer.SimulationTimer;

public class IntelligentEnsemblesTest {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, DEECoException, AnnotationProcessorException {
		new IntelligentEnsemblesTest().testEnsembles(false);
	}
	
	@Test
	public void testEnsembles() throws InstantiationException, IllegalAccessException, DEECoException, AnnotationProcessorException {
		testEnsembles(true);
	}
	
	private void testEnsembles(boolean silent) throws InstantiationException, IllegalAccessException, DEECoException, AnnotationProcessorException {		
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (silent) {
			RescuerComponent.outputStream = new PrintStream(baos);
		} else {
			RescuerComponent.outputStream = System.out;
		}
		
		/* create main application container */
		SimulationTimer simulationTimer = new DiscreteEventTimer();
		DEECoSimulation realm = new DEECoSimulation(simulationTimer);
		
		/* create one and only deeco node (centralized deployment) */
		DEECoNode deeco = realm.createNode(0);
		/* deploy components and ensemble factories */
		
		deeco.deployComponent(new RescuerComponent("1", 30));
		deeco.deployComponent(new RescuerComponent("2", 0));
		deeco.deployComponent(new RescuerComponent("3", 100));
		deeco.deployComponent(new RescuerComponent("4", 60));
		deeco.deployComponent(new RescuerComponent("5", 70));
		deeco.deployComponent(new RescuerComponent("6", 80));
		deeco.deployEnsembleFactory(new IntelligentEnsembleFactory());
		
		/* WHEN simulation is performed */
		realm.start(1002);
		
		if (silent) {
			assertThat(baos.toString(), containsString("Rescuer 1: train 2"));
			assertThat(baos.toString(), containsString("Rescuer 2: train 2"));
			assertThat(baos.toString(), containsString("Rescuer 3: train 1"));
			assertThat(baos.toString(), containsString("Rescuer 4: train 2"));
			assertThat(baos.toString(), containsString("Rescuer 5: train 1"));
			assertThat(baos.toString(), containsString("Rescuer 6: train 1"));
		}
	}
}
