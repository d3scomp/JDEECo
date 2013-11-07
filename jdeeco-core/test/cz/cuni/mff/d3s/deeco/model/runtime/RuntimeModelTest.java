package cz.cuni.mff.d3s.deeco.model.runtime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Collections;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNode;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;

/**
 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
 * 
 */
public class RuntimeModelTest {

	@Before
	public void setUp() throws Exception {
	}

	private KnowledgePath createSamplePathInstance() {
		RuntimeMetadataFactory factory = RuntimeMetadataFactory.eINSTANCE;

		PathNodeField pn;
		KnowledgePath p = factory.createKnowledgePath();

		pn = factory.createPathNodeField();
		pn.setName(new String("level1"));
		p.getNodes().add(pn);

		pn = factory.createPathNodeField();
		pn.setName(new String("level2"));
		p.getNodes().add(pn);

		return p;
	}

	private PathNode createPathNodeInstance(String name) {
		RuntimeMetadataFactory factory = RuntimeMetadataFactory.eINSTANCE;
		PathNodeField pn = factory.createPathNodeField();
		pn.setName(new String(name));
		return pn;
	}

	@Test
	public void testEqualsWorksWithPathNodes() {
		// WHEN two instance of PathNode designate the same path node
		PathNode pn1 = createPathNodeInstance("test");
		PathNode pn2 = createPathNodeInstance("test");
		// THEN the two instances of PathNode are equal w.r.t equals method
		assertEquals(pn1, pn2);
	}

	@Test
	public void testHashCodeWorksWithPathNodes() {
		// WHEN two instance of PathNode designate the same path node
		PathNode pn1 = createPathNodeInstance("test");
		PathNode pn2 = createPathNodeInstance("test");
		// THEN the two instances of PathNode are equal w.r.t hashCode method
		assertEquals(pn1.hashCode(), pn2.hashCode());
	}

	@Test
	public void testEqualsWorksWithKnowledgePath() {
		// WHEN two instance of KnowledgePath designate the same path
		KnowledgePath p1 = createSamplePathInstance();
		KnowledgePath p2 = createSamplePathInstance();
		 // THEN they should be equal using equals method
		assertEquals(p1, p2);
	}
	
	@Test
	public void testHashCodeWorksWithKnowledgePath() {
		// WHEN two instance of KnowledgePath designate the same path
		KnowledgePath p1 = createSamplePathInstance();
		KnowledgePath p2 = createSamplePathInstance();
		// WHEN the two instance of KnowledgePath are equal w.r.t hashCode methods
		assertEquals(p1.hashCode(), p2.hashCode());
	}

	@Test
	public void testExtensions() {
		// WHEN a RuntimeMetadataFactory is obtained
		RuntimeMetadataFactory factory = RuntimeMetadataFactory.eINSTANCE;
		// THEN RuntimeMetadataFactory is an instance of
		// RuntimeMetadataFactoryExt (i.e. our custom class)
		assertTrue(factory instanceof RuntimeMetadataFactoryExt);
	}

	public static void dummyMethodThatStandsForAComponentProcess() {
	}
	 @Test
     @Ignore
     public void testSaveAndLoad() throws Exception {
             // WHEN a RuntimeMetadata instance, which contains a method (within an Invocable)
             // and contains a knowledge manager with some knowledge is created
             SampleRuntimeModel oModel = new SampleRuntimeModel();
             
             // THEN the instance can be saved
             ResourceSet resourceSet = new ResourceSetImpl();

             resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());
             
             File testXMIFile = new File("test-temp/test.xmi");
             URI fileURI = URI.createFileURI(testXMIFile.getAbsolutePath());
             Resource resource = resourceSet.createResource(fileURI);
             resource.getContents().add(oModel.model);
             resource.save(Collections.EMPTY_MAP);
             assertTrue(testXMIFile.exists());

             // WHEN a RuntimeMetadata instance with a method is loaded
             resourceSet = new ResourceSetImpl();
             
             // This needs to be uncommented, but for that it needs a dependency on XMI
             // resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());
             
             RuntimeMetadata nModel = (RuntimeMetadata)resourceSet.getResource(fileURI, true).getContents().get(0);
             
             // THEN it has the same values
             // AND refers to the same method within the same class as before
             // AND contains the same knowledge as before
             ComponentInstance nComponentInstance = nModel.getComponentInstances().get(0);
             ComponentProcess nComponentProcess = nComponentInstance.getComponentProcesses().get(0);

             assertEquals(oModel.componentInstance.getName(), nComponentInstance.getName());
             assertEquals(oModel.process.getName(), nComponentProcess.getName());
             assertEquals(oModel.process.getMethod(), nComponentProcess.getMethod());
             // TODO: Compare the triggers and process params
             // TODO: Compare the data stored in the knowledge manager
     }
}
