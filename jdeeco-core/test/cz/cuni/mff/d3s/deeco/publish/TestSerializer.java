package cz.cuni.mff.d3s.deeco.publish;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessor;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.publish.Serializer;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;


public class TestSerializer {
	
	@Component 
	public static class TestComponent  {
		public String id;

		public TestComponent(String id) {
			this.id = id;		
		}

		@Process
		@PeriodicScheduling(500)
		public static void process(@In("id") String id) {
			// whatever
		}
	}
	

	RuntimeMetadata model;
	AnnotationProcessor processor;
	KnowledgePath kp;
	@Before
	public void setUp() throws Exception {
		processor = new AnnotationProcessor(RuntimeMetadataFactoryExt.eINSTANCE);
		model = RuntimeMetadataFactoryExt.eINSTANCE.createRuntimeMetadata();
		
		processor.process(model, new TestComponent("M1"));
		kp = model.getComponentInstances().get(0).getComponentProcesses().get(0).getParameters().get(0).getKnowledgePath();
	}

	@Test
	public void testSerialization() throws IOException, ClassNotFoundException {
		byte[] data = Serializer.serialize(kp);
		KnowledgePath nkp = (KnowledgePath) Serializer.deserialize(data);
		
		assertEquals(kp, nkp);
	}

}
