package cz.cuni.mff.d3s.deeco.annotations.processor;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;

public class TestAnnotationProcessor {

	public static void main(String[] args) {

		RuntimeMetadataFactory factory = RuntimeMetadataFactory.eINSTANCE;
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(model);
		processor.process(RobotFollowerComponent.class);

		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet
				.getResourceFactoryRegistry()
				.getExtensionToFactoryMap()
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION,
						new XMIResourceFactoryImpl());
		File testXMIFile = new File("test-temp/annotations-test.xmi");
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
