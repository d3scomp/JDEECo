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
import cz.cuni.mff.d3s.jdeeco.edl.validation.EdlValidationException;

public class StandaloneEnsemblesTest {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, DEECoException, AnnotationProcessorException, IOException, EdlValidationException {
		new StandaloneEnsemblesTest().testStandaloneEnsembles(false);
	}
	
	@Test
	@Ignore
	public void testEnsembles() throws InstantiationException, IllegalAccessException, DEECoException, AnnotationProcessorException, IOException, EdlValidationException {
		testStandaloneEnsembles(true);
	}

	public void testStandaloneEnsembles(boolean silent) throws InstantiationException, IllegalAccessException, DEECoException, AnnotationProcessorException, IOException, EdlValidationException {
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
			if (!silent) {
				System.out.println("Validation errors encountered when parsing the document. ");
				System.out.println(e);
				return;
			} else
				throw e;
			
		}	

		EnsembleFactory factory = new Z3IntelligentEnsembleFactory(model);
		DataclassKnowledgeContainer container = new DataclassKnowledgeContainer();		
		container.storeDataClass(new Rescuer("0", 100));
		container.storeDataClass(new Rescuer("1", 800));
		container.storeDataClass(new Rescuer("2", 60));
		container.storeDataClass(new Rescuer("3", 400));
		container.storeDataClass(new Rescuer("4", 20));
		container.storeDataClass(new Rescuer("5", 0));
		container.storeDataClass(new Rescuer("6", 10));
		container.storeDataClass(new Rescuer("7", 30));
		container.storeDataClass(new Rescuer("8", 50));/*
		container.storeDataClass(new Rescuer("9", 70));
		container.storeDataClass(new Rescuer("10", 90));
		container.storeDataClass(new Rescuer("11", 80));
		container.storeDataClass(new Rescuer("12", 80));
		container.storeDataClass(new Rescuer("13", 81));
		container.storeDataClass(new Rescuer("14", 82));*/
		container.storeDataClass(new FireFighter("100", 10));
		container.storeDataClass(new FireFighter("101", 20));
		container.storeDataClass(new FireFighter("102", 40));/*
		container.storeDataClass(new FireFighter("103", 50));/*
		*/
		/*
		container.storeDataClass(new FireFighter("104", 30));*/
		
		container.storeDataClass(new Position(10, 20));
		container.storeDataClass(new Position(50, 30));
		
		
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
			assertThat(baos.toString(), containsString("Rescuer 0: train 2"));
			assertThat(baos.toString(), containsString("Rescuer 1: train 2"));
			assertThat(baos.toString(), containsString("Rescuer 2: train 1"));
			assertThat(baos.toString(), containsString("Rescuer 3: train 2"));
			assertThat(baos.toString(), containsString("Rescuer 4: train 1"));
			assertThat(baos.toString(), containsString("Rescuer 5: train 1"));
		}

	}

}
