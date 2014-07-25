package cz.cuni.mff.d3s.deeco.network;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessor;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeUpdateException;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;
import cz.cuni.mff.d3s.deeco.network.KnowledgeData;
import cz.cuni.mff.d3s.deeco.network.KnowledgeDataManager;
import cz.cuni.mff.d3s.deeco.network.Serializer;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.jdeeco.simulation.demo.Member;
import cz.cuni.mff.d3s.jdeeco.simulation.demo.Position;


public class TestKnowledgeDataSize {
		

	RuntimeMetadata model;
	AnnotationProcessor processor;
	ComponentInstance component;
	List<KnowledgePath> emptyPath;
	
	@Before
	public void setUp() throws Exception {
		RuntimeMetadataFactory factory = RuntimeMetadataFactoryExt.eINSTANCE;
		
		
		
		model = factory.createRuntimeMetadata();
		processor = new AnnotationProcessor(factory, model);
		
		processor.process(new Member("M1", "T1", new Position(1, 2), false));
		component = model.getComponentInstances().get(0); 
		
		KnowledgePath empty = factory.createKnowledgePath();
		emptyPath = new LinkedList<>();
		emptyPath.add(empty);
	}


	
	@Test
	public void testMemberKnowledgeDataSize() throws IOException, ClassNotFoundException, KnowledgeUpdateException, KnowledgeNotFoundException {
		List<EnsembleDefinition> ens = Collections.emptyList();
		KnowledgeDataManager kdManager = new KnowledgeDataManager(null, null, null, "", mock(Scheduler.class), null, null);
		
		
		KnowledgeData kd = kdManager.prepareLocalKnowledgeData(component.getKnowledgeManager());
		byte[] data = Serializer.serialize(kd);
		
		System.out.println("Member data size: " + data.length);
		System.out.println("Member metadata size: " + Serializer.serialize(kd.getMetaData()).length);
		System.out.println("Member valueset size: " + Serializer.serialize(kd.getKnowledge()).length);

	}

}
