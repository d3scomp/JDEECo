package cz.cuni.mff.d3s.deeco.annotations.processor;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.d3s.deeco.annotations.processor.input.RobotFollowerComponent;
import cz.cuni.mff.d3s.deeco.annotations.processor.input.RobotLeaderComponent;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;

public class AnnotationProcessorTest {

	protected RuntimeMetadataFactory factory;
	protected AnnotationProcessor processor;
	protected RuntimeMetadata globalModel;

	@Before
	public void setUp() throws Exception {
		factory = RuntimeMetadataFactory.eINSTANCE;
		globalModel = factory.createRuntimeMetadata();
	}

	@Test
	public void testParsingOfSingleClass() {
		// Component with 2 processes, 3 parameters and 1 trigger per process:
		RuntimeMetadata model = factory.createRuntimeMetadata();
		processor = new AnnotationProcessor(model);
		processor.process(RobotFollowerComponent.class);
		saveToXMI(model, "test-temp/annotations-test1.xmi");
	}
	
	@Test
	public void testParsingOfMultipleClassesToSame () {
		// Component with 2 processes, 3 parameters and 1 trigger per process:
		processor = new AnnotationProcessor(globalModel);
		processor.process(RobotFollowerComponent.class);
		// Component with 2 processes, 3 parameters and 1 trigger per process:
		processor = new AnnotationProcessor(globalModel);
		processor.process(RobotLeaderComponent.class);
		
		saveToXMI(globalModel, "test-temp/annotations-test2.xmi");
	}

	public void saveToXMI(RuntimeMetadata model, String path) {
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet
				.getResourceFactoryRegistry()
				.getExtensionToFactoryMap()
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION,
						new XMIResourceFactoryImpl());
		File testXMIFile = new File(path);
		URI fileURI = URI.createFileURI(testXMIFile.getAbsolutePath());
		Resource resource = resourceSet.createResource(fileURI);
		resource.getContents().add(model);
		try {
			resource.save(Collections.EMPTY_MAP);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
