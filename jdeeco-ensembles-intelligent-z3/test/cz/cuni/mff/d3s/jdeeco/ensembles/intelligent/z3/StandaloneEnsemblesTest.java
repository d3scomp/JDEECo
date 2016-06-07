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
		
		PrintStream outputStream;
		
		if (silent) {
			outputStream = new PrintStream(baos);
		} else {
			outputStream = System.out;
		}

		EDLReader reader = new EDLReader();
		
		EdlDocument model = null;
		
		try {
			model = (EdlDocument) reader.readDocument("test/cz/cuni/mff/d3s/jdeeco/ensembles/intelligent/z3/pendolino.edl");
		} catch (Exception e) {
			System.out.println("Validation errors encountered when parsing the document. ");
			System.out.println(e);
			return;
		}		

		EnsembleFactory factory = new Z3IntelligentEnsembleFactory(model);
		DataclassKnowledgeContainer container = new DataclassKnowledgeContainer();		
		container.storeComponent(new Rescuer("1", 100));
		container.storeComponent(new Rescuer("2", 800));
		container.storeComponent(new Rescuer("3", 60));
		container.storeComponent(new Rescuer("4", 40));/*
		container.storeComponent(new Rescuer("5", 20));
		container.storeComponent(new Rescuer("6", 0));
		container.storeComponent(new Rescuer("7", 10));
		container.storeComponent(new Rescuer("8", 30));
		container.storeComponent(new Rescuer("9", 50));
		container.storeComponent(new Rescuer("10", 70));
		container.storeComponent(new Rescuer("11", 90));
		container.storeComponent(new Rescuer("12", 80));
		container.storeComponent(new Rescuer("13", 80));/*
		container.storeComponent(new Rescuer("14", 81));
		container.storeComponent(new Rescuer("15", 82));*/
		container.storeComponent(new FireFighter("101", 10));
		container.storeComponent(new FireFighter("102", 20));/*
		container.storeComponent(new FireFighter("104", 40));
		container.storeComponent(new FireFighter("105", 50));/*
		*/
		/*
		container.storeComponent(new FireFighter("103", 30));*/
		
		
		Collection<EnsembleInstance> formed = factory.createInstances(container);
		
		for (EnsembleInstance instance : formed) {
			instance.performKnowledgeExchange();
		}
		
		for (Rescuer rescuer : container.getTrackedKnowledgeForRole(Rescuer.class)) {
			if (rescuer.trainId > 0) {
				outputStream.printf("Rescuer %s: train %d", rescuer.id, rescuer.trainId);
				if (rescuer.isLeader) {
					outputStream.print(" (leader)");
				}
				outputStream.println();
			} else {
				outputStream.printf("Rescuer %s: unassigned\n", rescuer.id);
			}
		}
		
		for (FireFighter fighter : container.getTrackedKnowledgeForRole(FireFighter.class)) {
			if (fighter.trainId > 0) {
				outputStream.printf("FireFighter %s: train %d\n", fighter.id, fighter.trainId);
			} else {
				outputStream.printf("FireFighter %s: unassigned\n", fighter.id);
			}
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
