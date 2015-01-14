package cz.cuni.mff.d3s.deeco.security.runtime;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;

import java.security.KeyStoreException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import cz.cuni.mff.d3s.deeco.annotations.Allow;
import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Local;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.HasRole;
import cz.cuni.mff.d3s.deeco.annotations.RoleDefinition;
import cz.cuni.mff.d3s.deeco.annotations.RoleParam;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessor;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.executor.Executor;
import cz.cuni.mff.d3s.deeco.executor.SameThreadExecutor;
import cz.cuni.mff.d3s.deeco.knowledge.CloningKnowledgeManagerFactory;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerContainer;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.network.DataSender;
import cz.cuni.mff.d3s.deeco.network.DefaultKnowledgeDataManager;
import cz.cuni.mff.d3s.deeco.runtime.ArchitectureObserver;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeFramework;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeFrameworkImpl;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.scheduler.SingleThreadedScheduler;
import cz.cuni.mff.d3s.deeco.security.SecurityHelper;
import cz.cuni.mff.d3s.deeco.security.SecurityKeyManager;
import cz.cuni.mff.d3s.deeco.security.SecurityKeyManagerImpl;
import cz.cuni.mff.d3s.deeco.task.EnsembleTask;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.deeco.task.Task;
import cz.cuni.mff.d3s.deeco.task.TaskInvocationException;

public class SecurityRuntimeModel {
	
	@RoleDefinition
	public static interface PoliceInCity {
		@RoleParam
		public static final String cityId = "[cityId]";
	}
	
	@RoleDefinition
	public static interface PoliceEverywhere extends PoliceInCity {
		@RoleParam
		public static final String cityId = null;
	}
	
	@Component 
	public static class VehicleComponent  {
		public String id;
		public String cityId;
		
		@Allow(roleClass = PoliceInCity.class)
		public String secret;
		
		public VehicleComponent(String id, String cityId, String secret) {
			this.id = id;		
			this.cityId = cityId;
			this.secret = secret;
			
		}
	}
	
	@Component 
	@HasRole(roleClass = PoliceInCity.class)
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
	
	@Component 
	@HasRole(roleClass = PoliceEverywhere.class)
	public static class GlobalPoliceComponent  {
		public String id;
		
		@Local
		public Map<String, String> secrets;
		
		public GlobalPoliceComponent(String id) {
			this.id = id;		
			this.secrets = new HashMap<>();
		}
	}
	
	@Ensemble
	@PeriodicScheduling(period = 1000)
	public static class AllEnsemble {
		
		public static Predicate<String> membership;
		
		@Membership
		public static boolean membership(@In("member.id") String id) {
			return membership.test(id);
		}

		@KnowledgeExchange
		public static void exchange(@In("member.id") String id, @In("member.secret") String secret, @InOut("coord.secrets") ParamHolder<Map<String, String>> secrets) {
			secrets.value.put(id, secret);
		}
	}

	public RuntimeMetadata model;
	public AnnotationProcessor processor;
	public DefaultKnowledgeDataManager knowledgeDataManager;
	
	public DataSender dataSender;
	public SecurityKeyManager securityKeyManager;
	
	public Scheduler scheduler;
	
	public Executor executor;
	
	public KnowledgeManagerContainer container;
	public VehicleComponent vehicleComponent;
	public PoliceComponent policeComponent1, policeComponent2;
	public GlobalPoliceComponent globalPoliceComponent;
	
	public RuntimeFramework runtime;
	public SecurityHelper securityHelper;
	public ArchitectureObserver architectureObserver;
	
	public SecurityRuntimeModel() throws KeyStoreException, AnnotationProcessorException {
		initMocks(this);
		
		securityKeyManager = new SecurityKeyManagerImpl();
		scheduler = new SingleThreadedScheduler();
		executor = new SameThreadExecutor();
		securityHelper = new SecurityHelper();
		
		model = RuntimeMetadataFactoryExt.eINSTANCE.createRuntimeMetadata();
		processor = new AnnotationProcessor(RuntimeMetadataFactoryExt.eINSTANCE, model, new CloningKnowledgeManagerFactory());
		dataSender = mock(DataSender.class);
		architectureObserver = mock(ArchitectureObserver.class);
		
		vehicleComponent = new VehicleComponent("V1", "Prague", "top secret");
		policeComponent1 = new PoliceComponent("P1", "Prague");
		policeComponent2 = new PoliceComponent("P2", "Pilsen");
		globalPoliceComponent = new GlobalPoliceComponent("G1");
		
		processor.process(vehicleComponent);
		processor.process(policeComponent1);
		processor.process(policeComponent2);
		processor.process(globalPoliceComponent);
		processor.process(AllEnsemble.class);
		
		// set ensemble to allow all components
		AllEnsemble.membership = id -> true;
		
		container = spy(new KnowledgeManagerContainer(new CloningKnowledgeManagerFactory(), model));
		runtime = spy(new RuntimeFrameworkImpl(model, scheduler, executor, container));

		knowledgeDataManager = new DefaultKnowledgeDataManager(model.getEnsembleDefinitions(), null);
		knowledgeDataManager.initialize(container, dataSender, "1.2.3.4", scheduler, securityKeyManager);		
	}
	
	public void invokeEnsembleTasks() throws TaskInvocationException {
		// manually invoke ensemble knowledge exchange
		Trigger trigger = model.getEnsembleDefinitions().get(0).getTriggers().get(0);
		
		for (ComponentInstance ci : model.getComponentInstances()) {
			Task task = new EnsembleTask(ci.getEnsembleControllers().get(0), scheduler, architectureObserver, container);
			task.invoke(trigger);
		}
		
	}
}
