package cz.cuni.mff.d3s.deeco.network;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;

import java.util.Arrays;
import java.util.Collection;
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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import cz.cuni.mff.d3s.deeco.integrity.PathRating;
import cz.cuni.mff.d3s.deeco.integrity.RatingsChangeSet;
import cz.cuni.mff.d3s.deeco.integrity.ReadonlyRatingsHolder;
import cz.cuni.mff.d3s.deeco.knowledge.*;
import cz.cuni.mff.d3s.deeco.model.runtime.RuntimeModelHelper;
import cz.cuni.mff.d3s.deeco.security.runtime.SecurityRuntimeModel;

/**
 * @author Ondřej Štumpf  
 */
@SuppressWarnings("unchecked")
public class DefaultKnowledgeDataManagerTest {
	
	SecurityRuntimeModel runtimeModel;
	
	@Captor
	public ArgumentCaptor<Object> objectCaptor;
	
	@Before
	public void setUp() throws Exception {	
		initMocks(this);
		
		this.runtimeModel = new SecurityRuntimeModel();		
	}

	@Test
	public void publishTest1() {
		// when publish() is called
		runtimeModel.knowledgeDataManager.publish();
		
		// then knowledge data are broadcasted
		verify(runtimeModel.dataSender).broadcastData(objectCaptor.capture());
		Object broadcastArgument = objectCaptor.getValue();
		List<KnowledgeData> data = (List<KnowledgeData>)broadcastArgument;
		
		assertEquals(6, data.size());
		
		Object[] componentIds = data.stream().map(d -> d.getMetaData().componentId).sorted().toArray();
		assertEquals("G1", componentIds[0]);
		assertEquals("P1", componentIds[1]);
		assertEquals("P2", componentIds[2]);
		assertEquals("V1", componentIds[3]);
		assertEquals("V1", componentIds[4]);
		assertEquals("V1", componentIds[5]);
				
		testPartOfKnowledgeData(data, "P1", 0, new String[] {"id", "cityId"}, new String[] {});
		testPartOfKnowledgeData(data, "P2", 0, new String[] {"id", "cityId"}, new String[] {});
		testPartOfKnowledgeData(data, "V1", 1, new String[] {}, new String[] {"secret"});
		testPartOfKnowledgeData(data, "V1", 0, new String[] {}, new String[] {"secret_for_city"});
		testPartOfKnowledgeData(data, "V1", 0, new String[] {"id", "cityId"}, new String[] {});
		testPartOfKnowledgeData(data, "G1", 0, new String[] {"id"}, new String[] {});		
	}
	
	@Test
	public void publishTest2() {
		// given ratings manager contains pending changes
		RatingsChangeSet changeSet1 = new RatingsChangeSet("author", "target", RuntimeModelHelper.createKnowledgePath("level1"), PathRating.OUT_OF_RANGE);
		RatingsChangeSet changeSet2 = new RatingsChangeSet("author", "target", RuntimeModelHelper.createKnowledgePath("level2"), PathRating.OK);
		runtimeModel.ratingsManager.addToPendingChangeSets(Arrays.asList(changeSet1, changeSet2));
		
		// when publish() is called
		runtimeModel.knowledgeDataManager.publish();
		
		// then ratings data are broadcasted
		verify(runtimeModel.dataSender, times(2)).broadcastData(objectCaptor.capture());
		Object broadcastArgument = objectCaptor.getValue();
		RatingsData ratingsData = ((List<RatingsData>)broadcastArgument).get(0);
		
		assertEquals(2, ratingsData.getRatings().size());
	}
		
	private void testPartOfKnowledgeData(List<KnowledgeData> listOfData, String componentId, int index, String[] plainFields, String[] encryptedFields) {
		Stream<KnowledgeData> dataStream = listOfData.stream().filter(d -> d.getMetaData().componentId.equals(componentId));
		if (plainFields.length > 0) {
			dataStream = dataStream.filter(d -> d.getMetaData().encryptedKey == null);
		} else {
			dataStream = dataStream.filter(d -> d.getMetaData().encryptedKey != null);
		}
		
		KnowledgeData p1Data = dataStream.skip(index).findFirst().get();
		
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
		data.add(new KnowledgeData(new ValueSet(), new ValueSet(), new ValueSet(), new KnowledgeMetaData("V1", 123, "4.56", 456, 1)));
		
		// when receiveKnowledge() is called
		runtimeModel.knowledgeDataManager.receiveKnowledge(data);
		
		// then processing stops
		verify(runtimeModel.container, never()).createReplica("V1");
	}
	
	@Test
	public void receiveKnowledge_OldKnowledgeTest1() {
		// given knowledge is received
		List<KnowledgeData> data = new LinkedList<>();
		data.add(new KnowledgeData(new ValueSet(), new ValueSet(), new ValueSet(), new KnowledgeMetaData("V_remote", 123, "4.56", 456, 1)));
		data.add(new KnowledgeData(new ValueSet(), new ValueSet(), new ValueSet(), new KnowledgeMetaData("V_remote", 122, "4.56", 450, 1)));
		
		// when receiveKnowledge() is called
		runtimeModel.knowledgeDataManager.receiveKnowledge(data);
		
		// then processing happens only for the first data
		verify(runtimeModel.container, times(1)).createReplica("V_remote");
	}
	
	@Test
	public void receiveKnowledge_OldKnowledgeTest2() {
		// given knowledge of various roles is received
		List<KnowledgeData> data = new LinkedList<>();
		data.add(new KnowledgeData(new ValueSet(), new ValueSet(), new ValueSet(), new KnowledgeMetaData("V_remote", 123, "4.56", 456, 1)));
		data.add(new KnowledgeData(new ValueSet(), new ValueSet(), new ValueSet(), new KnowledgeMetaData("V_remote", 122, "4.56", 450, 1, null, null, runtimeModel.securityKeyManager.getRoleKey("role", null), null)));
		
		// when receiveKnowledge() is called
		runtimeModel.knowledgeDataManager.receiveKnowledge(data);
		
		// then processing happens for both data
		verify(runtimeModel.container, times(2)).createReplica("V_remote");
	}

	@Test
	public void receiveKnowledge_OldKnowledgeTest3() {
		// given knowledge of various roles is received
		List<KnowledgeData> data = new LinkedList<>();
		data.add(new KnowledgeData(new ValueSet(), new ValueSet(), new ValueSet(), new KnowledgeMetaData("V_remote", 123, "4.56", 456, 1, null, null, runtimeModel.securityKeyManager.getRoleKey("role", null), null)));
		data.add(new KnowledgeData(new ValueSet(), new ValueSet(), new ValueSet(), new KnowledgeMetaData("V_remote", 122, "4.56", 450, 1)));
		
		// when receiveKnowledge() is called
		runtimeModel.knowledgeDataManager.receiveKnowledge(data);
		
		// then processing happens for both data
		verify(runtimeModel.container, times(2)).createReplica("V_remote");
	}
	
	@Test
	public void receiveKnowledge_OldKnowledgeTest4() {
		// given knowledge of various roles is received
		List<KnowledgeData> data = new LinkedList<>();
		data.add(new KnowledgeData(new ValueSet(), new ValueSet(), new ValueSet(), new KnowledgeMetaData("V_remote", 123, "4.56", 456, 1)));
		data.add(new KnowledgeData(new ValueSet(), new ValueSet(), new ValueSet(), new KnowledgeMetaData("V_remote", 122, "4.56", 450, 1, null, null, runtimeModel.securityKeyManager.getRoleKey("role", null), null)));
		data.add(new KnowledgeData(new ValueSet(), new ValueSet(), new ValueSet(), new KnowledgeMetaData("V_remote", 123, "4.56", 450, 1, null, null, runtimeModel.securityKeyManager.getRoleKey("role", null), null)));
		
		// when receiveKnowledge() is called
		runtimeModel.knowledgeDataManager.receiveKnowledge(data);
		
		// then processing happens for both data
		verify(runtimeModel.container, times(3)).createReplica("V_remote");
	}
	
	@Test
	public void receiveKnowledge_OldKnowledgeTest5() {
		// given knowledge of various roles is received
		List<KnowledgeData> data = new LinkedList<>();
		data.add(new KnowledgeData(new ValueSet(), new ValueSet(), new ValueSet(), new KnowledgeMetaData("V_remote", 123, "4.56", 456, 1)));
		data.add(new KnowledgeData(new ValueSet(), new ValueSet(), new ValueSet(), new KnowledgeMetaData("V_remote", 123, "4.56", 450, 1, null, null, runtimeModel.securityKeyManager.getRoleKey("role", null), null)));
		
		// when receiveKnowledge() is called
		runtimeModel.knowledgeDataManager.receiveKnowledge(data);
		
		// then processing happens for both data
		verify(runtimeModel.container, times(2)).createReplica("V_remote");
	}
	
	@Test
	public void receiveKnowledge_OldKnowledgeTest6() {
		// given knowledge of various roles is received
		List<KnowledgeData> data = new LinkedList<>();
		data.add(new KnowledgeData(new ValueSet(), new ValueSet(), new ValueSet(), new KnowledgeMetaData("V_remote", 123, "4.56", 456, 1)));
		data.add(new KnowledgeData(new ValueSet(), new ValueSet(), new ValueSet(), new KnowledgeMetaData("V_remote", 122, "4.56", 450, 1)));
		data.add(new KnowledgeData(new ValueSet(), new ValueSet(), new ValueSet(), new KnowledgeMetaData("V_remote", 123, "4.56", 450, 1, null, null, runtimeModel.securityKeyManager.getRoleKey("role", null), null)));
		data.add(new KnowledgeData(new ValueSet(), new ValueSet(), new ValueSet(), new KnowledgeMetaData("V_remote", 122, "4.56", 450, 1, null, null, runtimeModel.securityKeyManager.getRoleKey("role", null), null)));
		data.add(new KnowledgeData(new ValueSet(), new ValueSet(), new ValueSet(), new KnowledgeMetaData("V_remote", 125, "4.56", 450, 1, null, null, runtimeModel.securityKeyManager.getRoleKey("role", null), null)));
		
		// when receiveKnowledge() is called
		runtimeModel.knowledgeDataManager.receiveKnowledge(data);
		
		// then processing happens for both data
		verify(runtimeModel.container, times(3)).createReplica("V_remote");
	}
	
	@Test
	public void receiveKnowledge_NewKnowledgeTest() throws KnowledgeUpdateException {
		Collection<KnowledgeManager> replicas = new LinkedList<>();
		
		// capture replicas as spies
		when(runtimeModel.container.createReplica(eq("V_remote"))).then(new Answer<Collection<KnowledgeManager>>() {
			@Override
			public Collection<KnowledgeManager> answer(InvocationOnMock invocation) throws Throwable {
				replicas.addAll(((Collection<KnowledgeManager>)invocation.callRealMethod()).stream().map(km -> spy(km)).collect(Collectors.toList()));
				return replicas;
			}
		});
		
		// given knowledge from local component is received
		List<KnowledgeData> data = new LinkedList<>();
		ValueSet valueSet = new ValueSet();
		ValueSet securitySet = new ValueSet();
		valueSet.setValue(RuntimeModelHelper.createKnowledgePath("field"), 123);
		ValueSet authors = new ValueSet();
		authors.setValue(RuntimeModelHelper.createKnowledgePath("field"), "author");
		data.add(new KnowledgeData(valueSet, securitySet, authors, new KnowledgeMetaData("V_remote", 123, "4.56", 456, 1)));
		
		// when receiveKnowledge() is called
		runtimeModel.knowledgeDataManager.receiveKnowledge(data);
		
		// then replica for each local component is created
		assertEquals(4, replicas.size());
		
		// then update() is called on each replica
		for (KnowledgeManager km : replicas) {
			verify(km).update(notNull(ChangeSet.class), eq("author"));
		}
	}
	
	@Test
	public void prepareRatingsDataTest() {
		// given ratings manager contains pending changes
		RatingsChangeSet changeSet1 = new RatingsChangeSet("author", "target", RuntimeModelHelper.createKnowledgePath("level1"), PathRating.OUT_OF_RANGE);
		RatingsChangeSet changeSet2 = new RatingsChangeSet("author", "target", RuntimeModelHelper.createKnowledgePath("level2"), PathRating.OK);
		runtimeModel.ratingsManager.addToPendingChangeSets(Arrays.asList(changeSet1, changeSet2));
		
		// when prepareRatingsData() is called
		RatingsData ratingsData = runtimeModel.knowledgeDataManager.prepareRatingsData();
		
		// then ratings data are correct
		assertEquals(2, ratingsData.getRatings().size());
		
		// then there are no pending changes in the ratings manager
		assertTrue(runtimeModel.ratingsManager.getPendingChangeSets().isEmpty());
	}
	
	@Test
	public void receiveRatingsTest() {
		// given ratings data are prepared
		RatingsChangeSet changeSet1 = new RatingsChangeSet("author1", "target", RuntimeModelHelper.createKnowledgePath("level1"), PathRating.OUT_OF_RANGE);
		RatingsChangeSet changeSet2 = new RatingsChangeSet("author2", "target", RuntimeModelHelper.createKnowledgePath("level1"), PathRating.OK);
		RatingsMetaData metaData = new RatingsMetaData(123, 4);
		List<SealedObject> ratings = runtimeModel.knowledgeDataManager.ratingsEncryptor.encryptRatings(Arrays.asList(changeSet1, changeSet2), metaData);
		RatingsData ratingsData = new RatingsData(ratings, metaData);
		
		// when receive() is called
		runtimeModel.knowledgeDataManager.receive(Arrays.asList(ratingsData), 45);
		
		// then ratings manager is updated
		assertTrue(runtimeModel.ratingsManager.getPendingChangeSets().isEmpty());
		
		ReadonlyRatingsHolder holder = runtimeModel.ratingsManager.createReadonlyRatingsHolder("target", RuntimeModelHelper.createKnowledgePath("level1"));
		assertEquals(1, holder.getRatings(PathRating.OUT_OF_RANGE));
		assertEquals(1, holder.getRatings(PathRating.OK));
		assertEquals(0, holder.getRatings(PathRating.UNUSUAL));
		assertEquals(0, holder.getRatings(null));
	}
	
	@Test
	public void createChangeSetsTest() {
		// given knowledge and authors are prepared
		ValueSet knowledge = new ValueSet();
		knowledge.setValue(RuntimeModelHelper.createKnowledgePath("field1"), 1);
		knowledge.setValue(RuntimeModelHelper.createKnowledgePath("field2"), 2);
		knowledge.setValue(RuntimeModelHelper.createKnowledgePath("field3"), 3);
		knowledge.setValue(RuntimeModelHelper.createKnowledgePath("field4"), 4);
		ValueSet authors = new ValueSet();
		authors.setValue(RuntimeModelHelper.createKnowledgePath("field1"), "author1");
		authors.setValue(RuntimeModelHelper.createKnowledgePath("field2"), "author1");
		authors.setValue(RuntimeModelHelper.createKnowledgePath("field3"), "author2");
		KnowledgeMetaData meta = new KnowledgeMetaData("123", 1, "", 888l, 4);
		
		// when toChangeSets() is called 
		Map<String, ChangeSet> changeSets = runtimeModel.knowledgeDataManager.toChangeSets(knowledge, authors, meta);
		
		// then knowledge is split according to authors
		assertEquals(3, changeSets.size());
		
		assertEquals(2, changeSets.get("author1").getUpdatedReferences().size());
		assertEquals(1, changeSets.get("author2").getUpdatedReferences().size());
		assertEquals(1, changeSets.get("123").getUpdatedReferences().size());
		
		assertEquals(1, changeSets.get("author1").getValue(RuntimeModelHelper.createKnowledgePath("field1")));
		assertEquals(2, changeSets.get("author1").getValue(RuntimeModelHelper.createKnowledgePath("field2")));
		assertEquals(3, changeSets.get("author2").getValue(RuntimeModelHelper.createKnowledgePath("field3")));
		assertEquals(4, changeSets.get("123").getValue(RuntimeModelHelper.createKnowledgePath("field4")));
	}
}
