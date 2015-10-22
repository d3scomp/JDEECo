package cz.cuni.mff.d3s.deeco.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import cz.cuni.mff.d3s.deeco.integrity.PathRating;
import cz.cuni.mff.d3s.deeco.integrity.RatingsChangeSet;
import cz.cuni.mff.d3s.deeco.integrity.RatingsManagerImpl;
import cz.cuni.mff.d3s.deeco.integrity.ReadonlyRatingsHolder;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.model.runtime.RuntimeModelHelper;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.network.KnowledgeData;
import cz.cuni.mff.d3s.deeco.network.RatingsData;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeFramework;
import cz.cuni.mff.d3s.deeco.runtimelog.RuntimeLogger;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
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
		
		Scheduler scheduler = mock(Scheduler.class);
		
		RuntimeFramework runtimeFramework = mock(RuntimeFramework.class);
		Mockito.when(runtimeFramework.getScheduler()).thenAnswer(new Answer<Scheduler>() {
		    @Override
		    public Scheduler answer(InvocationOnMock invocation) throws Throwable 
		    {
		      return scheduler;
		    }
		  });
		
		RuntimeLogger logger = mock(RuntimeLogger.class);
		
		DEECoContainer deecoContainer = mock(DEECoContainer.class);
		Mockito.when(deecoContainer.getRuntimeFramework()).thenAnswer(new Answer<RuntimeFramework>() {
		    @Override
		    public RuntimeFramework answer(InvocationOnMock invocation) throws Throwable 
		    {
		      return runtimeFramework;
		    }
		  });
		Mockito.when(deecoContainer.getRuntimeLogger()).thenAnswer(new Answer<RuntimeLogger>() {
		    @Override
		    public RuntimeLogger answer(InvocationOnMock invocation) throws Throwable 
		    {
		      return logger;
		    }
		  });
		
		runtimeModel = new SecurityRuntimeModel(deecoContainer);
	}
	
	@After
	public void tearDown() {
		RatingsManagerImpl.resetSingleton();
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
						kd.getMetaData().componentId, kd.getMetaData().versionId, kd.getMetaData().targetRoleHash);
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
