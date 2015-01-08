package cz.cuni.mff.d3s.deeco.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

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
		List<KnowledgeData> data = (List<KnowledgeData>)broadcastArgument;
		
		// modify component ids so that they are not accepted as local
		data.stream().forEach(d -> d.getMetaData().componentId += "_remote");
		
		assertTrue(runtimeModel.policeComponent1.secrets.isEmpty());
		assertTrue(runtimeModel.policeComponent2.secrets.isEmpty());
		
		// then data are received
		runtimeModel.knowledgeDataManager.receiveKnowledge(data);
		
		runtimeModel.invokeEnsembleTasks();
		
		assertEquals(1, runtimeModel.policeComponent1.secrets.size());
		assertEquals("top secret", runtimeModel.policeComponent1.secrets.get("V1_remote"));
		assertTrue(runtimeModel.policeComponent2.secrets.isEmpty());
	}
	
	@Test
	public void localKnowledgeExchangeSecurityTest() throws TaskInvocationException {
		runtimeModel.invokeEnsembleTasks();
		
		assertEquals(1, runtimeModel.policeComponent1.secrets.size());
		assertEquals("top secret", runtimeModel.policeComponent1.secrets.get("V1"));
		assertTrue(runtimeModel.policeComponent2.secrets.isEmpty());
	}
}
