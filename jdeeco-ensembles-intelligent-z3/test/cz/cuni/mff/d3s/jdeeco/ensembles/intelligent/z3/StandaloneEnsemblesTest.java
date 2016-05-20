package cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;

import org.junit.Ignore;
import org.junit.Test;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.ensembles.EnsembleFactory;
import cz.cuni.mff.d3s.deeco.ensembles.EnsembleInstance;
import cz.cuni.mff.d3s.deeco.runtime.DEECoException;
import cz.cuni.mff.d3s.jdeeco.edl.EDLReader;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EdlDocument;

public class StandaloneEnsemblesTest {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, DEECoException, AnnotationProcessorException, IOException {
		new StandaloneEnsemblesTest().testStandaloneEnsembles(false);
	}
	
	@Test
	@Ignore
	public void testEnsembles() throws InstantiationException, IllegalAccessException, DEECoException, AnnotationProcessorException, IOException {
		testStandaloneEnsembles(true);
	}

	public void testStandaloneEnsembles(boolean silent) throws InstantiationException, IllegalAccessException, DEECoException, AnnotationProcessorException, IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (silent) {
			RescuerComponent.outputStream = new PrintStream(baos);
		} else {
			RescuerComponent.outputStream = System.out;
		}

		cz.cuni.mff.d3s.jdeeco.edl.model.edl.EdlPackage.eINSTANCE.eClass();		
		
		EdlDocument model = (EdlDocument) new EDLReader().readDocument("test/cz/cuni/mff/d3s/jdeeco/ensembles/intelligent/z3/pendolino.edl");

		EnsembleFactory factory = new Z3IntelligentEnsembleFactory(model);
		DataclassKnowledgeContainer container = new DataclassKnowledgeContainer();		
		container.storeComponent(new RescuerComponent("1", 100));
		container.storeComponent(new RescuerComponent("2", 80));
		container.storeComponent(new RescuerComponent("3", 60));
		container.storeComponent(new RescuerComponent("4", 40));
		container.storeComponent(new RescuerComponent("5", 20));
		container.storeComponent(new RescuerComponent("6", 0));
		container.storeComponent(new RescuerComponent("7", 10));
		container.storeComponent(new RescuerComponent("8", 30));
		container.storeComponent(new RescuerComponent("9", 50));
		container.storeComponent(new RescuerComponent("10", 70));
		container.storeComponent(new RescuerComponent("11", 90));
		container.storeComponent(new RescuerComponent("12", 80));
		container.storeComponent(new RescuerComponent("13", 80));
		container.storeComponent(new RescuerComponent("14", 80));
		container.storeComponent(new RescuerComponent("15", 80));
		container.storeComponent(new FireFighterComponent("101", 10));
		container.storeComponent(new FireFighterComponent("102", 20));
		container.storeComponent(new FireFighterComponent("103", 30));
		
		
		Collection<EnsembleInstance> formed = factory.createInstances(container);
		
		for (EnsembleInstance instance : formed) {
			instance.performKnowledgeExchange();
		}		
		
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
