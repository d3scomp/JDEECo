package cz.cuni.mff.d3s.deeco.network;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import junitx.framework.ListAssert;

import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessor;
import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerContainer;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeUpdateException;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.network.Serializer;
import cz.cuni.mff.d3s.deeco.scheduler.CurrentTimeProvider;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;


public class TestSerializer {
	
	@Component 
	public static class TestComponent  {
		public String id;

		public TestComponent(String id) {
			this.id = id;		
		}

		@Process
		@PeriodicScheduling(period=500)
		public static void process(@In("id") String id) {
			// whatever
		}
	}
	

	RuntimeMetadata model;
	AnnotationProcessor processor;
	KnowledgePath kp;
	ComponentInstance component;
	
	
	@Before
	public void setUp() throws Exception {
		model = RuntimeMetadataFactoryExt.eINSTANCE.createRuntimeMetadata();
		processor = new AnnotationProcessor(RuntimeMetadataFactoryExt.eINSTANCE, model);
		
		processor.process(new TestComponent("M1"));
		component = model.getComponentInstances().get(0); 
		kp = component.getComponentProcesses().get(0).getParameters().get(0).getKnowledgePath();
	}

	@Test
	public void testKnowledgePathSerialization() throws IOException, ClassNotFoundException {
		byte[] data = Serializer.serialize(kp);
		KnowledgePath nkp = (KnowledgePath) Serializer.deserialize(data);
		
		assertEquals(kp, nkp);
	}
	
	@Test
	public void testKnowledgeDataSerialization() throws IOException, ClassNotFoundException, KnowledgeUpdateException, KnowledgeNotFoundException {
		KnowledgeManagerContainer container = new KnowledgeManagerContainer();
		List<EnsembleDefinition> ens = Collections.emptyList();
		KnowledgeDataManager kdManager = new KnowledgeDataManager(container, null, ens, "", mock(Scheduler.class), null);
		
		ValueSet initialKnowledge = null;
		
		KnowledgePath empty = RuntimeMetadataFactoryExt.eINSTANCE.createKnowledgePath();
		// get all the knowledge (corresponding to an empty knowledge path)
		initialKnowledge = component.getKnowledgeManager().get(Arrays.asList(empty));
		
		// copy all the knowledge values into a ChangeSet
		ChangeSet cs = new ChangeSet();
		if (initialKnowledge != null) {
			for (KnowledgePath p: initialKnowledge.getKnowledgePaths()) {
				cs.setValue(p, initialKnowledge.getValue(p));
			}			
		}
		
		// create a new KM with the same id and knowledge values
		KnowledgeManager km = container.createLocal(component.getKnowledgeManager().getId());
		km.update(cs);
				
		List<? extends KnowledgeData> kd = kdManager.prepareLocalKnowledgeData();		
		byte[] data = Serializer.serialize(kd);
		List<? extends KnowledgeData> nkd = (List<? extends KnowledgeData>) Serializer.deserialize(data);
		
		ListAssert.assertEquals(kd, nkd);
	}

}
