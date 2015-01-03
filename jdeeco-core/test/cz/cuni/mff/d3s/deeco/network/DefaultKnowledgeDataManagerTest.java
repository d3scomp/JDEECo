package cz.cuni.mff.d3s.deeco.network;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.crypto.SealedObject;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import cz.cuni.mff.d3s.deeco.annotations.*;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessor;
import cz.cuni.mff.d3s.deeco.executor.Executor;
import cz.cuni.mff.d3s.deeco.executor.SameThreadExecutor;
import cz.cuni.mff.d3s.deeco.knowledge.*;
import cz.cuni.mff.d3s.deeco.model.runtime.RuntimeModelHelper;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeFramework;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeFrameworkImpl;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.scheduler.SingleThreadedScheduler;
import cz.cuni.mff.d3s.deeco.security.SecurityHelper;
import cz.cuni.mff.d3s.deeco.security.SecurityKeyManager;

@SuppressWarnings("unchecked")
public class DefaultKnowledgeDataManagerTest {
	
	@Component 
	public static class VehicleComponent  {
		public String id;
		public String cityId;
		
		@Allow(role = "police", params = "cityId")
		public String secret;
		
		public VehicleComponent(String id, String cityId) {
			this.id = id;		
			this.cityId = cityId;
		}
	}
	
	@Component 
	@Role(role = "police", params = "cityId")
	public static class PoliceComponent  {
		public String id;
		public String cityId;
		
		@Local
		public Map<String, String> secrets;
		
		public PoliceComponent(String id, String cityId) {
			this.id = id;		
			this.cityId = cityId;
			this.secrets = new HashMap<>();
		}
		
	}
	
	@Ensemble
	@PeriodicScheduling(period = 1000)
	public static class AllEnsemble {
		
		@Membership
		public static boolean membership(@In("member.id") String id) {
			return true;
		}

		@KnowledgeExchange
		public static void exchange(@In("member.id") String id, @In("member.secret") String secret, @InOut("coord.secrets") Map<String, String> secrets) {
			secrets.put(id, secret);
		}
	}

	public RuntimeMetadata model;
	public AnnotationProcessor processor;
	public DefaultKnowledgeDataManager knowledgeDataManager;
	
	@Mock
	public DataSender dataSender;
	
	@Mock
	public SecurityKeyManager securityKeyManager;
	
	public Scheduler scheduler;
	
	public Executor executor;
	
	@Captor
	public ArgumentCaptor<Object> objectCaptor;
	
	public KnowledgeManagerContainer container;
	
	@Before
	public void setUp() throws Exception {
		initMocks(this);
		
		SecurityHelper securityHelper = new SecurityHelper();
		when(securityKeyManager.getPublicKeyFor(anyString(), anyObject())).thenReturn(securityHelper.generateKey());
		
		scheduler = new SingleThreadedScheduler();
		executor = new SameThreadExecutor();
		
		model = RuntimeMetadataFactoryExt.eINSTANCE.createRuntimeMetadata();
		processor = new AnnotationProcessor(RuntimeMetadataFactoryExt.eINSTANCE, model, new CloningKnowledgeManagerFactory());
		
		processor.process(new VehicleComponent("V1", "Prague"));
		processor.process(new PoliceComponent("P1", "Prague"));
		processor.process(new PoliceComponent("P2", "Pilsen"));
		processor.process(AllEnsemble.class);
		
		container = spy(new KnowledgeManagerContainer(new CloningKnowledgeManagerFactory(), model));
		RuntimeFramework runtime = new RuntimeFrameworkImpl(model, scheduler, executor, container);
		
		knowledgeDataManager = new DefaultKnowledgeDataManager(model.getEnsembleDefinitions(), null);
		knowledgeDataManager.initialize(container, dataSender, "1.2.3.4", scheduler, securityKeyManager);
	}

	@Test
	public void publishTest() {
		// when publish() is called
		knowledgeDataManager.publish();
		
		// then data are broadcasted
		verify(dataSender).broadcastData(objectCaptor.capture());
		Object broadcastArgument = objectCaptor.getValue();
		List<KnowledgeData> data = (List<KnowledgeData>)broadcastArgument;
		
		assertEquals(4, data.size());
		
		Object[] componentIds = data.stream().map(d -> d.getMetaData().componentId).sorted().toArray();
		assertEquals("P1", componentIds[0]);
		assertEquals("P2", componentIds[1]);
		assertEquals("V1", componentIds[2]);
		assertEquals("V1", componentIds[3]);
		
		testPartOfKnowledgeData(data, "P1", new String[] {"id", "cityId"}, new String[] {});
		testPartOfKnowledgeData(data, "P2", new String[] {"id", "cityId"}, new String[] {});
		testPartOfKnowledgeData(data, "V1", new String[] {}, new String[] {"secret"});
		testPartOfKnowledgeData(data, "V1", new String[] {"id", "cityId"}, new String[] {});
		
	}
		
	private void testPartOfKnowledgeData(List<KnowledgeData> listOfData, String componentId, String[] plainFields, String[] encryptedFields) {
		Stream<KnowledgeData> dataStream = listOfData.stream().filter(d -> d.getMetaData().componentId.equals(componentId));
		if (plainFields.length > 0) {
			dataStream = dataStream.filter(d -> d.getMetaData().encryptedKey == null);
		} else {
			dataStream = dataStream.filter(d -> d.getMetaData().encryptedKey != null);
		}
		
		KnowledgeData p1Data = dataStream.findFirst().get();
		
		for (String plainField : plainFields) {
			assertTrue(p1Data.getKnowledge().getKnowledgePaths().contains(RuntimeModelHelper.createKnowledgePath(plainField)));
			assertFalse(p1Data.getKnowledge().getValue(RuntimeModelHelper.createKnowledgePath(plainField)) instanceof SealedObject);	
		}
		for (String encryptedField : encryptedFields) {
			assertTrue(p1Data.getKnowledge().getKnowledgePaths().contains(RuntimeModelHelper.createKnowledgePath(encryptedField)));
			assertTrue(p1Data.getKnowledge().getValue(RuntimeModelHelper.createKnowledgePath(encryptedField)) instanceof SealedObject);	
		}
		assertEquals(plainFields.length + encryptedFields.length, p1Data.getKnowledge().getKnowledgePaths().size());
	}
	
	@Test
	@Ignore
	public void rebroadcastTest() {
		// TODO
	}
	
	@Test
	public void receiveKnowledge_LocalTest() {
		// given knowledge from local component is received
		List<KnowledgeData> data = new LinkedList<>();
		data.add(new KnowledgeData(new ValueSet(), new KnowledgeMetaData("V1", 123, "4.56", 456, 1)));
		
		// when receiveKnowledge() is called
		knowledgeDataManager.receiveKnowledge(data);
		
		// then processing stops
		verify(container, never()).createReplica(anyString());
	}
	
	@Test
	public void receiveKnowledge_OldKnowledgeTest() {
		// given knowledge from local component is received
		List<KnowledgeData> data = new LinkedList<>();
		data.add(new KnowledgeData(new ValueSet(), new KnowledgeMetaData("V_remote", 123, "4.56", 456, 1)));
		data.add(new KnowledgeData(new ValueSet(), new KnowledgeMetaData("V_remote", 122, "4.56", 450, 1)));
		
		// when receiveKnowledge() is called
		knowledgeDataManager.receiveKnowledge(data);
		
		// then processing happens only for the first data
		verify(container, times(1)).createReplica(anyString());
	}
	
	@Test
	public void receiveKnowledge_NewKnowledgeTest() throws KnowledgeUpdateException {
		Collection<KnowledgeManager> replicas = new LinkedList<>();
		
		// capture replicas as spies
		when(container.createReplica(eq("V_remote"))).then(new Answer<Collection<KnowledgeManager>>() {
			@Override
			public Collection<KnowledgeManager> answer(InvocationOnMock invocation) throws Throwable {
				replicas.addAll(((Collection<KnowledgeManager>)invocation.callRealMethod()).stream().map(km -> spy(km)).collect(Collectors.toList()));
				return replicas;
			}
		});
		
		// given knowledge from local component is received
		List<KnowledgeData> data = new LinkedList<>();
		ValueSet valueSet = new ValueSet();
		data.add(new KnowledgeData(valueSet, new KnowledgeMetaData("V_remote", 123, "4.56", 456, 1)));
		
		// when receiveKnowledge() is called
		knowledgeDataManager.receiveKnowledge(data);
		
		// then replica for each local component is created
		assertEquals(3, replicas.size());
		
		// then update() is called on each replica
		for (KnowledgeManager km : replicas) {
			verify(km).update(notNull(ChangeSet.class));
		}
	}
}
