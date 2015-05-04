package cz.cuni.mff.d3s.deeco.network;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.security.KeyStoreException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.crypto.SealedObject;

import junitx.framework.ListAssert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessor;
import cz.cuni.mff.d3s.deeco.integrity.PathRating;
import cz.cuni.mff.d3s.deeco.integrity.RatingsChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.CloningKnowledgeManagerFactory;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerContainer;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeUpdateException;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.model.runtime.RuntimeModelHelper;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.security.RatingsEncryptor;
import cz.cuni.mff.d3s.deeco.security.SecurityKeyManagerImpl;
import cz.cuni.mff.d3s.deeco.timer.Timer;
import cz.cuni.mff.d3s.deeco.timer.TimerEventListener;


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
		processor = new AnnotationProcessor(RuntimeMetadataFactoryExt.eINSTANCE, model, new CloningKnowledgeManagerFactory());
		
		processor.processComponent(new TestComponent("M1"));
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
	@SuppressWarnings("unchecked")
	public void testKnowledgeDataSerialization() throws IOException, ClassNotFoundException, KnowledgeUpdateException, KnowledgeNotFoundException {
		KnowledgeManagerContainer container = new KnowledgeManagerContainer(new CloningKnowledgeManagerFactory(), model);
		List<EnsembleDefinition> ens = Collections.emptyList();
		DefaultKnowledgeDataManager kdManager = new DefaultKnowledgeDataManager(ens, null);
		Scheduler scheduler = getFakeScheduler();
		kdManager.initialize(container, null, "", scheduler, null, null);
		
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
		KnowledgeManager km = container.createLocal(component.getKnowledgeManager().getId(), component, 
				component.getKnowledgeManager().getRoles());
		km.update(cs);
				
		List<? extends KnowledgeData> kd = kdManager.prepareLocalKnowledgeData();		
		byte[] data = Serializer.serialize(kd);
		List<? extends KnowledgeData> nkd = (List<? extends KnowledgeData>) Serializer.deserialize(data);
		
		ListAssert.assertEquals(kd, nkd);
	}
	
	private Scheduler getFakeScheduler() {
		Scheduler scheduler = mock(Scheduler.class);
		Mockito.when(scheduler.getTimer()).thenReturn(new Timer() {
			@Override
			public long getCurrentMilliseconds() {
				return 0;
			}
			
			@Override
			public void notifyAt(long time, TimerEventListener listener, DEECoContainer node) {
				throw new UnsupportedOperationException("Fakt timer dows not support notifications");
			}
		});
		return scheduler;
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void testRatingsDataSerialization() throws KeyStoreException, IOException, ClassNotFoundException {
		// given unsealed data are prepared
		List<RatingsChangeSet> changeSets = new ArrayList<>();
		changeSets.add(new RatingsChangeSet("a", "b", RuntimeModelHelper.createKnowledgePath("level1", "field1"), PathRating.OK));
		changeSets.add(new RatingsChangeSet("c", "d", RuntimeModelHelper.createKnowledgePath("level1", "field2"), PathRating.OUT_OF_RANGE));
		changeSets.add(new RatingsChangeSet("e", "f", RuntimeModelHelper.createKnowledgePath("level1", "field3"), null));
		
		RatingsMetaData metaData = new RatingsMetaData(123, 4);		
		RatingsEncryptor ratingsEncryptor = new RatingsEncryptor(SecurityKeyManagerImpl.getInstance());
		
		// given RatingsData instance is prepared
		List<SealedObject> sealedChangeSets = ratingsEncryptor.encryptRatings(changeSets, metaData);
		RatingsData ratingsData = new RatingsData(sealedChangeSets, metaData);
		
		// when the data are serialized
		byte[] data = Serializer.serialize(Arrays.asList(ratingsData));
		
		// when the data are then deserialized
		RatingsData deserializedRatingsData = ((List<? extends RatingsData>) Serializer.deserialize(data)).get(0);
		
		// then they are equal		
		List<RatingsChangeSet> deserializedChangeSets = ratingsEncryptor.decryptRatings(deserializedRatingsData.getRatings(), deserializedRatingsData.getRatingsMetaData());
		assertEquals(ratingsData.getRatingsMetaData(), deserializedRatingsData.getRatingsMetaData());
		assertEquals(changeSets, deserializedChangeSets);
	}
}
