package cz.cuni.mff.d3s.deeco.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import cz.cuni.mff.d3s.deeco.integrity.PathRating;
import cz.cuni.mff.d3s.deeco.integrity.RatingsChangeSet;
import cz.cuni.mff.d3s.deeco.integrity.ReadonlyRatingsHolder;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.model.runtime.RuntimeModelHelper;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.network.KnowledgeData;
import cz.cuni.mff.d3s.deeco.network.RatingsData;
import cz.cuni.mff.d3s.deeco.security.runtime.SecurityRuntimeModel;
import cz.cuni.mff.d3s.deeco.security.runtime.SecurityRuntimeModel.PoliceEverywhereEnsemble;
import cz.cuni.mff.d3s.deeco.task.TaskInvocationException;

/**
 * 
 * @author Ondřej Štumpf
 *
 */
public class SecurityIntegrationTests {
	
	SecurityRuntimeModel runtimeModel;
	
	@Captor
	public ArgumentCaptor<Object> objectCaptor;
	
	@Before
	public void setUp() throws Exception {		
		initMocks(this);
		
		runtimeModel = new SecurityRuntimeModel();
	}	
	
	@Test
	public void publishReceiveSecurityTest() throws InterruptedException, TaskInvocationException {		
		// set ensemble to allow only remote components, so that we can ignore local-to-local knowledge exchange
		SecurityRuntimeModel.AllEnsemble.membership = id -> id.contains("remote");
				
		runtimeModel.invokeEnsembleTasks();
				
		// when publish() is called
		runtimeModel.knowledgeDataManager.publish();
		
		// then data are broadcasted and captured
		List<KnowledgeData> data = captureKnowledgeData();
		
		assertTrue(runtimeModel.policeComponent1.secrets.isEmpty());
		assertTrue(runtimeModel.policeComponent2.secrets.isEmpty());
		// then data are received
		runtimeModel.knowledgeDataManager.receive(data, 123);
		
		runtimeModel.invokeEnsembleTasks();
		
		assertEquals(1, runtimeModel.policeComponent1.secrets.size());
		assertEquals("top secret", runtimeModel.policeComponent1.secrets.get("V1_remote"));
		assertTrue(runtimeModel.policeComponent2.secrets.isEmpty());
		assertEquals(1, runtimeModel.globalPoliceComponent.secrets.size());
		assertEquals("top secret", runtimeModel.globalPoliceComponent.secrets.get("V1_remote"));
	}
	
	@Test
	public void localKnowledgeExchangeSecurityTest() throws TaskInvocationException {
		runtimeModel.invokeEnsembleTasks();
		
		assertEquals(1, runtimeModel.policeComponent1.secrets.size());
		assertEquals("top secret", runtimeModel.policeComponent1.secrets.get("V1"));
		assertTrue(runtimeModel.policeComponent2.secrets.isEmpty());
		assertEquals(1, runtimeModel.globalPoliceComponent.secrets.size());
		assertEquals("top secret", runtimeModel.globalPoliceComponent.secrets.get("V1"));	
	}
	
	@Test
	public void localAuthorsTest() throws TaskInvocationException {
		runtimeModel.invokeEnsembleTasks();
		
		assertEquals("V1", runtimeModel.container.getLocal("P1").getAuthor(RuntimeModelHelper.createKnowledgePath("secrets", "V1")));
		assertEquals("V1", runtimeModel.container.getLocal("G1").getAuthor(RuntimeModelHelper.createKnowledgePath("secrets", "V1")));
		assertEquals("P2", runtimeModel.container.getLocal("P2").getAuthor(RuntimeModelHelper.createKnowledgePath("secrets", "V1")));
	}
	
	@Test	
	public void remoteAuthorsTest() throws TaskInvocationException {
		SecurityRuntimeModel.AllEnsemble.membership = id -> id.contains("remote");
	
		// when publish() is called
		runtimeModel.knowledgeDataManager.publish();
		
		// then data are broadcasted and captured
		List<KnowledgeData> data = captureKnowledgeData();
		
		// then data are received
		runtimeModel.knowledgeDataManager.receive(data, 123);
		
		assertEquals(16, runtimeModel.container.getReplicas().size());
		int counter = 0;
		
		for (KnowledgeManager replica : runtimeModel.container.getReplicas()) {
			if (replica.getId().equals("V1_remote") && replica.getComponent().getKnowledgeManager().getId().equals("P1")) {
				assertEquals("V1", replica.getAuthor(RuntimeModelHelper.createKnowledgePath("secret")));
				counter++;
			}
			if (replica.getId().equals("V1_remote") && replica.getComponent().getKnowledgeManager().getId().equals("P2")) {
				assertNull(replica.getAuthor(RuntimeModelHelper.createKnowledgePath("secret")));
				counter++;
			}
			if (replica.getId().equals("V1_remote") && replica.getComponent().getKnowledgeManager().getId().equals("G1")) {
				assertEquals("V1", replica.getAuthor(RuntimeModelHelper.createKnowledgePath("secret")));
				counter++;
			}
		}
		assertEquals(3, counter);
	}

	@Test
	public void localRatingsTest() throws TaskInvocationException {
		runtimeModel.invokeEnsembleTasks();
		
		List<RatingsChangeSet> ratings = runtimeModel.ratingsManager.getPendingChangeSets();
		assertEquals(1, ratings.size());
		
		assertEquals("P1", ratings.get(0).getAuthorComponentId());
		assertEquals("P1", ratings.get(0).getTargetComponentId());
		assertEquals(RuntimeModelHelper.createKnowledgePath("cityId"), ratings.get(0).getKnowledgePath());
		assertEquals(PathRating.UNUSUAL, ratings.get(0).getPathRating());
	}
	
	@Test
	public void remoteRatingsTest() throws TaskInvocationException {
		runtimeModel.invokeEnsembleTasks();
		
		// when publish() is called
		runtimeModel.knowledgeDataManager.publish();
		
		// then ratings data are broadcasted and captured
		List<RatingsData> data = captureRatingsData();
		
		// then data are received
		runtimeModel.knowledgeDataManager.receive(data, 123);
		
		// change the rating so that remote changes are reflected
		runtimeModel.ratingsManager.update(Arrays.asList(new RatingsChangeSet("X", "P1", RuntimeModelHelper.createKnowledgePath("cityId"), PathRating.OUT_OF_RANGE)));
		List<RatingsChangeSet> ratings = runtimeModel.ratingsManager.getPendingChangeSets();
		assertEquals(0, ratings.size());
		
		ReadonlyRatingsHolder p1Holder = runtimeModel.ratingsManager.createReadonlyRatingsHolder("P1", RuntimeModelHelper.createKnowledgePath("cityId"));				
		assertEquals(1, p1Holder.getRatings(PathRating.OUT_OF_RANGE));		
		assertEquals(1, p1Holder.getRatings(PathRating.UNUSUAL));
		assertEquals(0, p1Holder.getRatings(PathRating.OK));
	}
	
	@Test
	public void localKnowledgeExchangeSecurityTest2() throws TaskInvocationException, KnowledgeNotFoundException {		
		// first round: the knowledge gets from vehicle to global police
		runtimeModel.invokeEnsembleTasks();
		KnowledgePath citySecretPath = RuntimeModelHelper.createKnowledgePath("secret_for_city");
		
		assertEquals("city secret", runtimeModel.container.getLocal("V1").get(Arrays.asList(citySecretPath)).getValue(citySecretPath));
		assertEquals("city secret modified", runtimeModel.container.getLocal("G1").get(Arrays.asList(citySecretPath)).getValue(citySecretPath));
		assertEquals("V1", runtimeModel.container.getLocal("G1").getAuthor(citySecretPath));
		
		// second round: the knowledge gets to the ordinary police
		runtimeModel.invokeEnsembleTasks();
		
		assertEquals("city secret modified modified", runtimeModel.container.getLocal("P1").get(Arrays.asList(citySecretPath)).getValue(citySecretPath));
	}
	
	@Test
	public void remoteKnowledgeExchangeSecurityTest2() throws TaskInvocationException, KnowledgeNotFoundException {
		PoliceEverywhereEnsemble.membership = (memberId, coordId) -> (memberId.equals("V1_remote") && coordId.equals("G1")) || (memberId.equals("G1_remote") && coordId.equals("P1"))
				|| (memberId.equals("V1") && coordId.equals("G1"));
		
		// first round: the knowledge gets from vehicle to global police
		runtimeModel.invokeEnsembleTasks();
		
		// delete ratings data
		runtimeModel.ratingsManager.getPendingChangeSets().clear();
		
		// when publish() is called
		runtimeModel.knowledgeDataManager.publish();
		
		// then data are broadcasted and captured
		List<KnowledgeData> data = captureKnowledgeData();
		
		// then data are received
		runtimeModel.knowledgeDataManager.receive(data, 123);
		
		KnowledgePath citySecretPath = RuntimeModelHelper.createKnowledgePath("secret_for_city");
		
		assertEquals("city secret", runtimeModel.container.getLocal("V1").get(Arrays.asList(citySecretPath)).getValue(citySecretPath));
		assertEquals("V1", runtimeModel.container.getLocal("G1").getAuthor(citySecretPath));
		assertEquals("city secret modified", runtimeModel.container.getLocal("G1").get(Arrays.asList(citySecretPath)).getValue(citySecretPath));		
		
		// second round: the knowledge gets to the ordinary police
		runtimeModel.invokeEnsembleTasks();
		
		assertEquals("city secret modified modified", runtimeModel.container.getLocal("P1").get(Arrays.asList(citySecretPath)).getValue(citySecretPath));
	}
	
	
	@SuppressWarnings("unchecked")
	private List<KnowledgeData> captureKnowledgeData() {
		verify(runtimeModel.dataSender).broadcastData(objectCaptor.capture());
		Object broadcastArgument = objectCaptor.getValue();			
		List<KnowledgeData> data = (List<KnowledgeData>)broadcastArgument;

		// recalculate signature
		data.stream().forEach(kd -> {
			kd.getMetaData().componentId += "_remote";
			try {
				kd.getMetaData().signature = runtimeModel.securityHelper.sign(runtimeModel.securityKeyManager.getIntegrityPrivateKey(),
						kd.getMetaData().componentId, kd.getMetaData().versionId, kd.getMetaData().targetRoleHash,
						kd.getKnowledge(), kd.getSecuritySet(), kd.getAuthors());
			} catch (Exception e) {
				fail();
			}
		});
		return data;
	}
	
	@SuppressWarnings("unchecked")
	private List<RatingsData> captureRatingsData() {
		verify(runtimeModel.dataSender, times(2)).broadcastData(objectCaptor.capture());
		Object broadcastArgument = objectCaptor.getValue();			
		List<RatingsData> data = (List<RatingsData>)broadcastArgument;
		return data;
	}
}
