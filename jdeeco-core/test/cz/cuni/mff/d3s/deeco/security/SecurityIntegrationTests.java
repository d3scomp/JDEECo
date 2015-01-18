package cz.cuni.mff.d3s.deeco.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import cz.cuni.mff.d3s.deeco.model.runtime.RuntimeModelHelper;
import cz.cuni.mff.d3s.deeco.network.KnowledgeData;
import cz.cuni.mff.d3s.deeco.security.runtime.SecurityRuntimeModel;
import cz.cuni.mff.d3s.deeco.task.TaskInvocationException;

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
		verify(runtimeModel.dataSender).broadcastData(objectCaptor.capture());
		Object broadcastArgument = objectCaptor.getValue();
		
		@SuppressWarnings("unchecked")
		List<KnowledgeData> data = (List<KnowledgeData>)broadcastArgument;
		
		assertTrue(runtimeModel.policeComponent1.secrets.isEmpty());
		assertTrue(runtimeModel.policeComponent2.secrets.isEmpty());
		
		// recalculate signature
		data.stream().forEach(kd -> {
			kd.getMetaData().componentId += "_remote";
			try {
				kd.getMetaData().signature = runtimeModel.securityHelper.sign(runtimeModel.securityKeyManager.getIntegrityPrivateKey(),
						kd.getMetaData().componentId, kd.getMetaData().versionId, kd.getMetaData().targetRole);
			} catch (Exception e) {
				fail();
			}
		});
		// then data are received
		runtimeModel.knowledgeDataManager.receiveKnowledge(data);
		
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
}
