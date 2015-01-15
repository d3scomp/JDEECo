package cz.cuni.mff.d3s.deeco.annotations.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import cz.cuni.mff.d3s.deeco.annotations.Allow;
import cz.cuni.mff.d3s.deeco.annotations.CommunicationBoundary;
import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Local;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.HasRole;
import cz.cuni.mff.d3s.deeco.annotations.Rating;
import cz.cuni.mff.d3s.deeco.annotations.RatingsProcess;
import cz.cuni.mff.d3s.deeco.annotations.RoleDefinition;
import cz.cuni.mff.d3s.deeco.annotations.RoleParam;
import cz.cuni.mff.d3s.deeco.annotations.TriggerOnChange;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.ComponentIdentifier;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.EEnsembleParty;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.PNode;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.ParseException;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.PathOrigin;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.PathParser;
import cz.cuni.mff.d3s.deeco.integrity.RatingsHolder;
import cz.cuni.mff.d3s.deeco.integrity.ReadonlyRatingsHolder;
import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerFactory;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeUpdateException;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.AbsoluteSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.BlankSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Condition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleController;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Exchange;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeChangeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeSecurityTag;
import cz.cuni.mff.d3s.deeco.model.runtime.api.LocalKnowledgeTag;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterKind;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNode;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeComponentId;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeCoordinator;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMapKey;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMember;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityRole;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;
import cz.cuni.mff.d3s.deeco.network.CommunicationBoundaryPredicate;
import cz.cuni.mff.d3s.deeco.network.GenericCommunicationBoundaryPredicate;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper;

/**
 * Common gateway for processing of Java objects/classes with DEECo annotations.
 * <p>
 * "Processing" means parsing the file, creating an Ecore subgraph out of it,
 * attaching the subgraph to the top-level container, the "runtimeMetadata"
 * model of the application (which is provided in every <code>process</code>
 * method). The processor is parameterized by an EMF factory, which has to be
 * passed through the constructor.
 * </p>
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 * 
 */
public class AnnotationProcessor {
	
	/**
	 * Mapping of direction annotations to the respective runtime metadata model classes. 
	 */
	static final Map<Class<? extends Annotation>, ParameterKind> parameterAnnotationsToParameterKinds = Collections
			.unmodifiableMap(new HashMap<Class<? extends Annotation>, ParameterKind>() {
				private static final long serialVersionUID = 1L;
				{
					put(InOut.class, ParameterKind.INOUT);
					put(In.class, ParameterKind.IN);
					put(Out.class, ParameterKind.OUT);
					put(Rating.class, ParameterKind.RATING);
				}
			});
	
	/**
	 * Annotations that can appear in a class and can be handled by the main processor (this)
	 */
	static final Set<Class<? extends Annotation>> KNOWN_CLASS_ANNOTATIONS = new HashSet<>(
			Arrays.asList(PeriodicScheduling.class, Component.class, Ensemble.class, HasRole.class));
	
	/**
	 * Annotations that can appear in a method and can be handled by the main processor (this)  
	 */
	static final Set<Class<? extends Annotation>> KNOWN_METHOD_ANNOTATIONS = new HashSet<>(
			Arrays.asList(Process.class, PeriodicScheduling.class, KnowledgeExchange.class, Membership.class, CommunicationBoundary.class));

	/**
	 *  Places in the parsing process where the processor's extensions are called. 
	 */
	enum ParsingEvent {
		ON_COMPONENT_CREATION, ON_PROCESS_CREATION, ON_ENSEMBLE_CREATION, 
		ON_UNKNOWN_COMPONENT_METHOD_ANNOTATION, ON_UNKNOWN_ENSEMBLE_METHOD_ANNOTATION
	}
	
	/**
	 * Holds the EMF factory used to create runtime metadata model instances during the parsing process.
	 */
	RuntimeMetadataFactory factory;
	
	/**
	 * Holds the EMF model to be updated during the parsing process. 
	 */
	RuntimeMetadata model;
	
	/**
	 * Processors to handle additional annotations that are not handled by the main processor.
	 * They are called via the <code>callExtensions()</code>. 
	 */
	AnnotationProcessorExtensionPoint[] extensions;
	
	KnowledgeManagerFactory knowledgeManagerFactory;
	
	/**
	 * Initializes the processor with the given model factory (convenience
	 * method when no extensions are provided). All the model elements produced
	 * by the processor will be created via the provided factory and added to
	 * the provided model.
	 * 
	 * @param factory
	 *            EMF runtime metadata factory
	 * @param model
	 *            runtime metadata model to be updated by the processor
	 * @param knowledgeMangerFactory knowledge manager factory to be used
	 */

	public AnnotationProcessor(RuntimeMetadataFactory factory, KnowledgeManagerFactory knowledgeMangerFactory,
			RuntimeMetadata model) {
		this(factory, model, knowledgeMangerFactory);
	}
	
	/**
	 * Initializes the processor with the given model factory and extensions.
	 * All the model elements produced by the processor will be created via the
	 * provided factory and added to the provided model.
	 * 
	 * @param factory
	 *            EMF runtime metadata factory
	 * @param model
	 *            runtime metadata model to be updated by the processor
	 * @param extensions
	 *            one or more classes extending the <code>AnnotationProcessorExtensionPoint</code> that provide additional processing functionality
	 * @param knowledgeMangerFactory knowledge manager factory to be used
	 */
	public AnnotationProcessor(RuntimeMetadataFactory factory, RuntimeMetadata model, KnowledgeManagerFactory knowledgeMangerFactory, AnnotationProcessorExtensionPoint... extensions) {
		this.factory = factory;
		this.model = model;
		this.extensions = extensions;
		this.knowledgeManagerFactory = knowledgeMangerFactory;		
	}
	
	/**
	 * Processing of a single file.
	 * 
	 * @param obj
	 *            object to be processed
	 * @throws AnnotationProcessorException
	 */
	public void process(Object obj) throws AnnotationProcessorException {
		processObject(model, obj); 
	}
	
	/**
	 * Batch processing of multiple files provided as extra parameters.
	 * 
	 * @param obj
	 *            object to be processed
	 * @param objs
	 *            rest of objects to be processed, if any
	 * @throws AnnotationProcessorException
	 */
	public void process(Object obj, Object... objs) throws AnnotationProcessorException {
		processObject(model, obj); 
		for (Object o: objs) {
			processObject(model, o);
		}
	}
	
	/**
	 * Batch processing of multiple files provided as a list of objects.
	 * 
	 * @param objs
	 *            list of objects to be processed
	 * @throws AnnotationProcessorException
	 */
	public void process(List<Object> objs) throws AnnotationProcessorException {
		if (objs == null) {
			throw new AnnotationProcessorException("Provide an initialized object or a non-empty list of objects.");
		}
		if (objs.isEmpty()) {
			throw new AnnotationProcessorException("Cannot process an empty list.");
		}
		for (Object o: objs) {
			processObject(model, o);
		}
	}

	/**
	 * Checks if the object is annotated as @{@link Component}/@{@link Ensemble}
	 * and calls the respective creator. It also creates the appropriate
	 * {@link EnsembleController}s.
	 * <p>
	 * If both/no such annotations are found, it throws an exception.
	 * </p>
	 * 
	 * @param model
	 *            runtime model to be updated
	 * @param obj
	 *            object to be processed
	 * @throws AnnotationProcessorException
	 */
	void processObject(RuntimeMetadata model, Object obj) throws AnnotationProcessorException {
		if (model == null) {
			throw new AnnotationProcessorException("Provided model cannot be null.");
		}
		if (obj == null) {
			throw new AnnotationProcessorException("Provided object(s) cannot be null.");
		}
		
		boolean isClass = (obj instanceof Class<?>);
		Class<?> clazz = (isClass) ? (Class<?>) obj : obj.getClass();
		boolean isC = isComponentDefinition(clazz);
		boolean isE = isEnsembleDefinition(clazz);
		if (isC && isE) {
			throw new AnnotationProcessorException(
					"Class: " + clazz.getCanonicalName() +
					"->Both @" + Component.class.getSimpleName() + " or @" + Ensemble.class.getSimpleName() + " annotation found.");
		}
		if (isC) {			
			processComponentInstance(model, obj);
			return;
		} 
		if (isE) {
			EnsembleDefinition ed = createEnsembleDefinition(clazz);
			// Create ensemble controllers for all the already-processed component instance definitions
			for (ComponentInstance ci: model.getComponentInstances()) {
				EnsembleController ec = factory.createEnsembleController();
				ec.setComponentInstance(ci);
				ec.setEnsembleDefinition(ed);
				ci.getEnsembleControllers().add(ec);
			}
			
			model.getEnsembleDefinitions().add(ed);

			return;
		} 
		throw new AnnotationProcessorException(
				"Class: " + clazz.getCanonicalName() +
				"->No @" + Component.class.getSimpleName() + " or @" + Ensemble.class.getSimpleName() + " annotation found.");
	}
	
	/**
	 * Checks if the object is annotated as @{@link Component} and calls the respective creator. 
	 * It also creates the appropriate {@link EnsembleController}s for the {@link EnsembleDefinition}s in the model.
	 * The parsed {@link ComponentInstance} is automatically added to the model.
	 * 	
	 * @param model the model to which the instance is to be added
	 * @param obj instance definition to be processed
	 * @return	the parsed {@link ComponentInstance}
	 * @throws AnnotationProcessorException	if the instance definition object is invalid
	 */
	public ComponentInstance processComponentInstance(RuntimeMetadata model, Object obj) throws AnnotationProcessorException {
		if (model == null) {
			throw new AnnotationProcessorException("Provided model cannot be null.");
		}
		if (obj == null) {
			throw new AnnotationProcessorException("Provided object(s) cannot be null.");
		}
		
		boolean isClass = (obj instanceof Class<?>);
		Class<?> clazz = (isClass) ? (Class<?>) obj : obj.getClass();
		boolean isC = isComponentDefinition(clazz);
		
		// TODO: unify the checks (in processObject the presence of multiple annotations is checked)
		if (!isC || isClass) {
			throw new AnnotationProcessorException(
					"For a component to be parsed, it has to be an INSTANCE of a class annotated with @" + Component.class.getSimpleName() + ".");	
		}
		
		ComponentInstance ci = createComponentInstance(obj);
		// Create ensemble controllers for all the already-processed ensemble definitions
		for (EnsembleDefinition ed: model.getEnsembleDefinitions()) {
			EnsembleController ec = factory.createEnsembleController();
			ec.setComponentInstance(ci);
			ec.setEnsembleDefinition(ed);
			ci.getEnsembleControllers().add(ec);
		}
		
		model.getComponentInstances().add(ci);

		return ci;
	}

	/**
	 * Creator of a single correctly-initialized {@link ComponentInstance} object. 
	 * It calls all the necessary sub-creators to obtain the full graph of the Ecore object.    
	 * <p>
	 * If the input class contains a method that is annotated as @{@link ComponentProcess} but 
	 * is <b>not</b> public & static, it throws an exception.
	 * </p>
	 * <p>
	 * In case an {@link AnnotationProcessorException} or a {@link ParseException} exception is caught, it is cast to 
	 * {@link AnnotationProcessorException} and the prefix <code>"Component: 'canonical_name'->"</code> is prepended to the exception message.  
	 * </p>
	 * 
	 * @param obj instance of a class annotated as @{@link Component}
	 */
	ComponentInstance createComponentInstance(Object obj) throws AnnotationProcessorException {
		Class<?> clazz = obj.getClass();
		ComponentInstance componentInstance = factory.createComponentInstance();
		componentInstance.setName(clazz.getCanonicalName());
		
		try {
			ChangeSet initialK = extractInitialKnowledge(obj, false);
			ChangeSet initialLocalK = extractInitialKnowledge(obj, true);
			String id = getComponentId(initialK);
			if (id == null) {
				id = new StringBuilder().append(clazz.getSimpleName()).append(UUID.randomUUID().toString()).toString();
			}
			KnowledgeManager km = knowledgeManagerFactory.create(id, componentInstance);
			km.update(initialK);
			km.markAsLocal(initialLocalK.getUpdatedReferences());
			km.update(initialLocalK);
			componentInstance.setKnowledgeManager(km);
	   
			try {
				addSecurityTags(clazz, km, initialK);
				addVirtualSecurityTags(km, initialLocalK);
			} catch (Exception ex) {
				throw new AnnotationProcessorException(ex.getMessage());
			}
			
			List<Method> methodsMarkedAsProcesses = new ArrayList<>(); 
			List<Method> methodsMarkedAsRatingProcesses = new ArrayList<>();
			
			Method[] allMethods = clazz.getMethods();
			for (Method m : allMethods) {
				if (m.getAnnotations().length > 0) {
					if (m.getAnnotation(Process.class) == null && m.getAnnotation(RatingsProcess.class) == null) {
						// when the method has some annotation(s), but not a @Process nor @RatingsProcess
						callExtensions(ParsingEvent.ON_UNKNOWN_COMPONENT_METHOD_ANNOTATION, m, getUnknownAnnotations(m));
					} else if (m.getAnnotation(Process.class) != null && m.getAnnotation(RatingsProcess.class) != null) {
						throw new AnnotationProcessorException("The same method cannot be marked as @" + Process.class.getSimpleName() + " and @" + RatingsProcess.class.getSimpleName() + ".");
					} else {
						if (m.getAnnotation(Process.class) != null) {
							methodsMarkedAsProcesses.add(m);
						} else {
							methodsMarkedAsRatingProcesses.add(m);
						}
					}
					
				}
			}
			
			if (methodsMarkedAsRatingProcesses.size() > 1) {
				throw new AnnotationProcessorException("Cannot have more @" + RatingsProcess.class.getSimpleName() + " methods.");
			}
			
			if (methodsMarkedAsRatingProcesses.size() == 1) {
				Method m = methodsMarkedAsRatingProcesses.get(0);
				int modifier = m.getModifiers();
				
				if (Modifier.isPublic(modifier) && Modifier.isStatic(modifier)) {
					componentInstance.setRatingsProcess(createRatingsProcess(componentInstance, m));
				} else {
					throw new AnnotationProcessorException("Method "+ m.getName()+ " annotated as @" + RatingsProcess.class.getSimpleName() + " should be public and static.");
				}
			}
			
			for (Method m : methodsMarkedAsProcesses) {
				int modifier = m.getModifiers();
				if (Modifier.isPublic(modifier) && Modifier.isStatic(modifier)) {
					componentInstance.getComponentProcesses().add(
							createComponentProcess(componentInstance, m));
				} else {
					throw new AnnotationProcessorException("Method "+ m.getName()+ " annotated as @" + Process.class.getSimpleName() + " should be public and static.");
				}
			}
			
			Set<Class<?>> roles = new HashSet<>();
			for (HasRole role : clazz.getDeclaredAnnotationsByType(HasRole.class)) {
				if (roles.contains(role.roleClass())) {
					throw new AnnotationProcessorException("The same role cannot be assigned multiply to a component.");
				}
				SecurityRole securityRole = createRoleFromClassDefinition(role.roleClass());
				componentInstance.getRoles().add(securityRole);
				roles.add(role.roleClass());
			}
			
			callExtensions(ParsingEvent.ON_COMPONENT_CREATION, componentInstance, getUnknownAnnotations(clazz));
			
		} catch (KnowledgeUpdateException | AnnotationProcessorException
				| ParseException e) {
			String msg = Component.class.getSimpleName() + ": "
					+ componentInstance.getName() + "->" + e.getMessage();
			throw new AnnotationProcessorException(msg, e);
		}
		return componentInstance;
	}

	/**
	 * Creates ratings process - method used to get rating of knowledge
	 * @param componentInstance the component 
	 * @param m method used as a process
	 * @return 
	 * @throws AnnotationProcessorException
	 * @throws ParseException
	 */
	private cz.cuni.mff.d3s.deeco.model.runtime.api.RatingsProcess createRatingsProcess(ComponentInstance componentInstance, Method m) throws AnnotationProcessorException, ParseException {
		cz.cuni.mff.d3s.deeco.model.runtime.api.RatingsProcess process = factory.createRatingsProcess();
		try {
			process.setComponentInstance(componentInstance);
			process.setMethod(m);			
			process.getParameters().addAll(createParameters(m, PathOrigin.RATING_PROCESS));			
		} catch (AnnotationProcessorException e) {			
			throw new AnnotationProcessorException("Ratings process ->"+e.getMessage(), e);
		}
		return process;
	}

	/**
	 * Parses the class used as a role definition
	 * @param roleClass the class from annotation
	 * @return
	 * @throws AnnotationProcessorException
	 * @throws ParseException
	 */
	private SecurityRole createRoleFromClassDefinition(Class<?> roleClass) throws AnnotationProcessorException, ParseException {
		if (roleClass.getAnnotationsByType(RoleDefinition.class).length == 0) {
			throw new AnnotationProcessorException("Role class must be decoreted with @RoleDefinition.");
		}
		
		SecurityRole securityRole = factory.createSecurityRole();
		securityRole.setRoleName(roleClass.getName());		
		
		for (Class<?> iface : roleClass.getInterfaces()) {
			securityRole.getConsistsOf().add(createRoleFromClassDefinition(iface));
		}
		
		List<Field> fieldParameters = Arrays.stream(roleClass.getFields())
				.filter(field -> field.getAnnotationsByType(RoleParam.class).length > 0)				
				.collect(Collectors.toList());
		
		for (Field field : fieldParameters) {
			if (!isValidForRole(field, fieldParameters, securityRole)) {
				continue;
			}			
			if (!Modifier.isFinal(field.getModifiers()) || !Modifier.isStatic(field.getModifiers())) {
				throw new AnnotationProcessorException("Role parameter must be static and final.");
			}
										
			Object fieldValue = null;
			try {
				fieldValue = field.get(null);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new AnnotationProcessorException("Cannot read path from security role argument "+field.getName(), e);
			}
			
			SecurityRoleArgument argument = createSecurityRoleArgument(field, fieldValue);						
			
			securityRole.getArguments().add(argument);
			for (SecurityRole role : securityRole.getConsistsOf()) {
				overrideArgument(argument, role);
			}
			
		}
		
		return securityRole;
	}

	/**
	 * Creates a security role argument from the field
	 * @param field field used as an argument
	 * @param fieldValue either null, absolute value, or string path enclosed in brackets
	 * @return
	 * @throws ParseException
	 * @throws AnnotationProcessorException
	 */
	private SecurityRoleArgument createSecurityRoleArgument(Field field, Object fieldValue) throws ParseException, AnnotationProcessorException {
		SecurityRoleArgument argument = null;
		
		if (fieldValue == null) {
			argument = factory.createBlankSecurityRoleArgument();
			argument.setName(field.getName());				
		} else {
			boolean createConcreteArgument = true;
			if (fieldValue instanceof String) {
				KnowledgePath kp = createKnowledgePath((String)fieldValue, PathOrigin.SECURITY_ANNOTATION);
				if (kp.getNodes().get(0) instanceof PathNodeMapKey) {
					argument = factory.createPathSecurityRoleArgument();
					argument.setName(field.getName());
					
					KnowledgePath innerPath = ((PathNodeMapKey)kp.getNodes().get(0)).getKeyPath();
					((PathSecurityRoleArgument)argument).setKnowledgePath(innerPath);
					createConcreteArgument = false;
				}
			}
			
			if (createConcreteArgument) {					
				argument = factory.createAbsoluteSecurityRoleArgument();
				argument.setName(field.getName());
				((AbsoluteSecurityRoleArgument)argument).setValue(fieldValue);
			}
		}
		
		return argument;
	}

	/**
	 * Sets the current argument value in each parent of the security role
	 * @param argument
	 * @param securityRole
	 */
	private void overrideArgument(SecurityRoleArgument argument, SecurityRole securityRole) {
		Optional<SecurityRoleArgument> optionalArgument = securityRole.getArguments().stream().filter(arg -> arg.getName().equals(argument.getName())).findFirst();
		if (optionalArgument.isPresent()) {
			securityRole.getArguments().removeIf(arg -> arg.getName().equals(optionalArgument.get().getName()));
			
			if (argument instanceof BlankSecurityRoleArgument) {
				BlankSecurityRoleArgument arg = factory.createBlankSecurityRoleArgument();
				arg.setName(argument.getName());
				securityRole.getArguments().add(arg);
			} else if (argument instanceof PathSecurityRoleArgument) {
				PathSecurityRoleArgument arg = factory.createPathSecurityRoleArgument();
				arg.setName(argument.getName());
				arg.setKnowledgePath(KnowledgePathHelper.cloneKnowledgePath(((PathSecurityRoleArgument)argument).getKnowledgePath()));
				securityRole.getArguments().add(arg);
			} else {
				AbsoluteSecurityRoleArgument arg = factory.createAbsoluteSecurityRoleArgument();
				arg.setName(argument.getName());
				arg.setValue(((AbsoluteSecurityRoleArgument)argument).getValue());
				securityRole.getArguments().add(arg);
			}
			
			securityRole.getConsistsOf().stream().forEach(role -> overrideArgument(argument, role));
		}
	}

	/**
	 * Checks whether the given field is an applicable argument for the role
	 * @param field
	 * @param fieldParameters
	 * @param securityRole
	 * @return
	 */
	private boolean isValidForRole(Field field, List<Field> fieldParameters, SecurityRole securityRole) {
		if (field.getDeclaringClass().getName().equals(securityRole.getRoleName())) {
			return true;
		} else {
			boolean definedInParent = securityRole.getConsistsOf().stream().anyMatch(
					role -> role.getArguments().stream().anyMatch(argument -> argument.getName().equals(field.getName()))
					);
			boolean notAnyLocal = !fieldParameters.stream().anyMatch(otherField -> otherField.getName().equals(field.getName()) && otherField.getDeclaringClass().getName().equals(securityRole.getRoleName()));
			return notAnyLocal && definedInParent;
		}		
	}

	/**
	 * Iterates through the initial knowledge of the component and adds security tags for the associated knowledge manager
	 * @param clazz
	 * @param km
	 * @param initialKnowledge
	 * @throws NoSuchFieldException
	 * @throws ParseException
	 * @throws AnnotationProcessorException
	 */
	private void addSecurityTags(Class<?> clazz, KnowledgeManager km,
			ChangeSet initialKnowledge) throws NoSuchFieldException, ParseException, AnnotationProcessorException {
		for (KnowledgePath kp : initialKnowledge.getUpdatedReferences()) {
			if (kp.getNodes().size() != 1) throw new NoSuchFieldException();
			PathNodeField pathNodeField = (PathNodeField)kp.getNodes().get(0);
			Field field = clazz.getField(pathNodeField.getName());
			
			Allow[] allows = field.getAnnotationsByType(Allow.class);
			
			if (allows.length > 0 && field.getName().equals(ComponentIdentifier.ID.toString())) {
				throw new AnnotationProcessorException("The component ID must not be secured.");
			}
			
			Set<Class<?>> roleClasses = new HashSet<>();
			
			for (Allow allow : allows) {
				if (roleClasses.contains(allow.roleClass())) {
					throw new AnnotationProcessorException("Cannot assign the same role " + allow.roleClass().getSimpleName() + " multiple times.");
				}
				
				KnowledgeSecurityTag tag = factory.createKnowledgeSecurityTag();
				tag.setRequiredRole(createRoleFromClassDefinition(allow.roleClass()));
				km.addSecurityTags(kp, Arrays.asList(tag));			
				roleClasses.add(allow.roleClass());
			}
			
		}		
	}

	/**
	 * Adds blank security tags to the local knowledge, denotating it therefore as "infinitely secure"
	 * @param km
	 * @param initialKnowledge
	 */
	private void addVirtualSecurityTags(KnowledgeManager km, ChangeSet initialKnowledge) {
		for (KnowledgePath kp : initialKnowledge.getUpdatedReferences()) {
			LocalKnowledgeTag tag = factory.createLocalKnowledgeTag();
			km.addSecurityTags(kp, Arrays.asList(tag));			
		}
	}
	
	/**
	 * Creator of a single correctly-initialized {@link EnsembleDefinition} object. 
	 * It calls all the necessary sub-creators to obtain the full graph of the Ecore object.
	 * <p>
	 * If the input class contains no triggers, it throws an exception.
	 * </p>
	 * <p>
	 * In case an {@link AnnotationProcessorException} or a {@link ParseException} exception is caught, it is cast to 
	 * {@link AnnotationProcessorException} and the prefix <code>"Ensemble: 'canonical_name'->"</code> is prepended to the exception message.  
	 * </p>
	 * 
	 * @param obj
	 *            instance of a class annotated as @{@link EnsembleDefinition}
	 *            <b>or</b> the annotated class object itself
	 */
	EnsembleDefinition createEnsembleDefinition(Class<?> clazz) throws AnnotationProcessorException {
		
		EnsembleDefinition ensembleDefinition = factory.createEnsembleDefinition();
		ensembleDefinition.setName(clazz.getCanonicalName());
		
		try {

			Method[] allMethods = clazz.getMethods();
			for (Method m : allMethods) {
				if (m.getAnnotations().length > 0) {
					if ((m.getAnnotation(Membership.class) == null) && (m.getAnnotation(KnowledgeExchange.class) == null)){
						// when the method has some annotation(s), but not a @Membership or a @KnowledgeExchange one
						callExtensions(ParsingEvent.ON_UNKNOWN_ENSEMBLE_METHOD_ANNOTATION, m, getUnknownAnnotations(m));
					} 
				}
			}
			
			Condition condition = createCondition(clazz);
			ensembleDefinition.setMembership(condition);
			
			Exchange exchange = createExchange(clazz);
			ensembleDefinition.setKnowledgeExchange(exchange);
			
			CommunicationBoundaryPredicate cBoundary = createCommunicationBoundary(clazz);			
			ensembleDefinition.setCommunicationBoundary(cBoundary);
			
			TimeTrigger periodicEnsembleTrigger = createPeriodicTrigger(clazz);
			List<KnowledgeChangeTrigger> exchangeKChangeTriggers = createKnowledgeChangeTriggers(exchange.getMethod(), PathOrigin.ENSEMBLE);
			List<KnowledgeChangeTrigger> conditionKChangeTriggers = createKnowledgeChangeTriggers(condition.getMethod(), PathOrigin.ENSEMBLE);
			
			if (periodicEnsembleTrigger == null) {
				if (exchangeKChangeTriggers.isEmpty() && conditionKChangeTriggers.isEmpty()) {
					throw new AnnotationProcessorException("No triggers were found.");
				}
			} else {
				ensembleDefinition.getTriggers().add(periodicEnsembleTrigger);
			}
			
			ensembleDefinition.getTriggers().addAll(exchangeKChangeTriggers);
			ensembleDefinition.getTriggers().addAll(conditionKChangeTriggers);
			
			callExtensions(ParsingEvent.ON_ENSEMBLE_CREATION, ensembleDefinition, getUnknownAnnotations(clazz));
			
		} catch (AnnotationProcessorException | ParseException e) {
			String msg = Ensemble.class.getSimpleName()+": " + ensembleDefinition.getName() + "->" + e.getMessage();
			throw new AnnotationProcessorException(msg, e);
		}
		return ensembleDefinition;
	}

	/**
	 * Creator of a single correctly-initialized {@link Condition} object. 
	 * It calls all the necessary sub-creators to obtain the full graph of the Ecore object.
	 * <p>
	 * In case a {@link AnnotationProcessorException} exception is caught, 
	 * the prefix <code>"Membership->"</code> is prepended to the exception message.  
	 * </p>
	 */
	Condition createCondition(Class<?> clazz) throws AnnotationProcessorException, ParseException{
		Method m = getAnnotatedMethodInEnsemble(clazz, Membership.class);
		Condition condition = factory.createCondition();
		condition.setMethod(m);
		try {
			condition.getParameters().addAll(createParameters(m, PathOrigin.ENSEMBLE));
		} catch (AnnotationProcessorException e) {
			String msg = Membership.class.getSimpleName()+"->"+e.getMessage();
			throw new AnnotationProcessorException(msg, e);
		}
		return condition;
	}
	
	/**
	 * Creator of a single correctly-initialized {@link Exchange} object. 
	 * It calls all the necessary sub-creators to obtain the full graph of the Ecore object.
	 * <p>
	 * In case a {@link AnnotationProcessorException} exception is caught, 
	 * the prefix <code>"KnowledgeExchange->"</code> is prepended to the exception message.  
	 * </p>
	 */
	Exchange createExchange(Class<?> clazz) throws AnnotationProcessorException,ParseException {
		Method m = getAnnotatedMethodInEnsemble(clazz, KnowledgeExchange.class);
		Exchange exchange = factory.createExchange();
		exchange.setMethod(m);
		try{
			exchange.getParameters().addAll(createParameters(m, PathOrigin.ENSEMBLE));
		} catch (AnnotationProcessorException e) {
			String msg = KnowledgeExchange.class.getSimpleName()+"->"+e.getMessage();
			throw new AnnotationProcessorException(msg, e);
		}
		return exchange;
	}
	
	/**
	 * Creator of a single correctly-initialized {@link CommunicationBoundaryPredicate} object. 	
	 */
	CommunicationBoundaryPredicate createCommunicationBoundary(Class<?> clazz) throws AnnotationProcessorException,ParseException {
		try {
			Method m = getAnnotatedMethodInEnsemble(clazz, CommunicationBoundary.class);
			return new GenericCommunicationBoundaryPredicate(m);
		} catch (AnnotationProcessorException e) {
			// if the exception was caused by not finding the boundary, then
			// return null (it is correct not to define the boundary).
			if (e.getMessage().startsWith("No") && e.getMessage().endsWith("annotation was found")) {
				return null;
			} else {
				String msg = KnowledgeExchange.class.getSimpleName()+"->"+e.getMessage();
				throw new AnnotationProcessorException(msg, e);
			}
		}		
	}

	/**
	 * Creator of a single correctly-initialized {@link ComponentProcess} object from a method.
	 * It calls all the necessary sub-creators to obtain the full graph of the Ecore object.
	 * <p>
	 * If the method being parsed contains no triggers, it throws an exception.
	 * </p>
	 * <p>
	 * In case a {@link AnnotationProcessorException} exception is caught, 
	 * the prefix <code>"Process: 'method_name'->"</code> is prepended to the exception message.  
	 * </p>
	 * 
	 * @param componentInstance Ecore object to point to from the created object as back-reference. 
	 * @param m method to be parsed. It has to be public and static, and annotated with @{@link Process} annotation.
	 */
	ComponentProcess createComponentProcess(ComponentInstance componentInstance, Method m) throws AnnotationProcessorException, ParseException {
		ComponentProcess componentProcess = factory.createComponentProcess();
		try {
			componentProcess.setComponentInstance(componentInstance);
			componentProcess.setMethod(m);
			componentProcess.setName(m.getName());
			componentProcess.getParameters().addAll(createParameters(m, PathOrigin.COMPONENT));
			TimeTrigger periodicTrigger = createPeriodicTrigger(m);
			List<KnowledgeChangeTrigger> knowledgeChangeTriggers = createKnowledgeChangeTriggers(m, PathOrigin.COMPONENT);
			if (periodicTrigger == null) {
				if (knowledgeChangeTriggers.isEmpty()) {
					throw new AnnotationProcessorException("No triggers were found.");
				}
			} else {
				componentProcess.getTriggers().add(periodicTrigger);
			}
			componentProcess.getTriggers().addAll(knowledgeChangeTriggers);
			
			callExtensions(ParsingEvent.ON_PROCESS_CREATION, componentProcess, getUnknownAnnotations(m));

		} catch (AnnotationProcessorException e) {
			String msg = "Process: "+componentProcess.getName()+"->"+e.getMessage();
			throw new AnnotationProcessorException(msg, e);
		}
		return componentProcess;
	}
	
	/**
	 * Creator of a {@link PeriodicTrigger} from a method or a class. (Both Method and Class can hold a @ {@link PeriodicTrigger}.)
	 * <p>
	 * If no @{@link PeriodicScheduling} annotation is found, returns <code>null</code>.
	 * </p>
	 */
	TimeTrigger createPeriodicTrigger(Object o)
			throws AnnotationProcessorException {
		Annotation[] annotations = null;
		if (o instanceof Method) {
			annotations = ((Method) o).getAnnotations();
		}
		else if (o instanceof Class<?>) {
			annotations = ((Class<?>) o).getAnnotations();
		} else {
			return null;
		}
		for (Annotation a : annotations) {
			if (a instanceof PeriodicScheduling) {
				TimeTrigger periodicTrigger = factory.createTimeTrigger();
				periodicTrigger.setPeriod(((PeriodicScheduling) a).period());
				periodicTrigger.setOrder(((PeriodicScheduling) a).order());
				periodicTrigger.setOffset(((PeriodicScheduling) a).offset());
				return periodicTrigger;
			}
		}
		return null;
	}

	/**
	 * Creator of a list of {@link KnowledgeChangeTrigger} from a method.
	 * It considers only the method parameters that are annotated with @{@link TriggerOnChange}.
	 * <p>
	 * If no @{@link TriggerOnChange} annotation is found, it just returns an empty list.
	 * </p>
	 * 
	 * @param method method to be parsed
	 * @param pathOrigin indicates whether it is being called within the context of {@link #createComponentProcess}, {@link #createEnsembleDefinition} or {@link #addSecurityTags(Class, KnowledgeManager, ChangeSet)} . 
	 */
	List<KnowledgeChangeTrigger> createKnowledgeChangeTriggers(Method method, PathOrigin pathOrigin) 
			throws AnnotationProcessorException, ParseException {
		List<KnowledgeChangeTrigger> knowledgeChangeTriggers = new ArrayList<>();
		Type[] parameterTypes = method.getParameterTypes();
		Annotation[][] allAnnotations = method.getParameterAnnotations();
		for (int i = 0; i < parameterTypes.length; i++) {
			TriggerOnChange t = getAnnotation(allAnnotations[i], TriggerOnChange.class);
			if (t != null) {
				Annotation directionAnnotation = getKindAnnotation(allAnnotations[i]);
				String path = getKindAnnotationValue(directionAnnotation);
				KnowledgeChangeTrigger trigger = factory.createKnowledgeChangeTrigger();
				trigger.setKnowledgePath(createKnowledgePath(path, pathOrigin));
				knowledgeChangeTriggers.add(trigger);
			}
		}
		return knowledgeChangeTriggers;
	}

	/**
	 * Creator a list of {@link Parameter} from a method. 
	 * <p>
	 * If the method has no parameters, it throws an exception.
	 * </p>
	 * 
	 * @param method method to be parsed
	 * @param pathOrigin indicates whether it is being called within the context of {@link #createComponentProcess}, {@link #createEnsembleDefinition} or {@link #addSecurityTags(Class, KnowledgeManager, ChangeSet)} .
	 */
	List<Parameter> createParameters(Method method, PathOrigin pathOrigin)
			throws AnnotationProcessorException, ParseException {
		List<Parameter> parameters = new ArrayList<>();
		Class<?>[] parameterTypes = method.getParameterTypes();
		Annotation[][] allAnnotations = method.getParameterAnnotations();
		for (int i = 0; i < parameterTypes.length; i++) {
			parameters.add(createParameter(parameterTypes[i], i, allAnnotations[i], pathOrigin));
		}
		if (parameters.isEmpty()) {
 			throw new AnnotationProcessorException(
 					"The "+ (pathOrigin == PathOrigin.COMPONENT ? "component" : "ensemble") + " process cannot have zero parameters.");
		}
		return parameters;
	}
	
	
	/**
	 * Creator of a {@link Parameter} of a {@link ComponentProcess}/{@link Condition}/{@link Exchange}.
	 * <p>
	 * In case a {@link AnnotationProcessorException} exception is caught, 
	 * the prefix <code>"Parameter: '(parameter_index+1)'->"</code> is prepended to the exception message.  
	 * </p>
	 * 
	 * @param type	the type of the parameter
	 * @param parameterIndex order of the parameter in the method declaration
	 * @param parameterAnnotations list of annotations extracted from method signature
	 * @param pathOrigin indicates whether it is being called within the context of {@link #createComponentProcess}, {@link #createEnsembleDefinition} or {@link #addSecurityTags(Class, KnowledgeManager, ChangeSet)} .
	 */
	Parameter createParameter(Class<?> type, int parameterIndex, Annotation[] parameterAnnotations, PathOrigin pathOrigin)
			throws AnnotationProcessorException, ParseException {
		Parameter parameter = factory.createParameter();
		try {
			Annotation directionAnnotation = getKindAnnotation(parameterAnnotations);
			parameter.setKind(parameterAnnotationsToParameterKinds.get(directionAnnotation.annotationType()));
			String path = getKindAnnotationValue(directionAnnotation);
			parameter.setKnowledgePath(createKnowledgePath(path,pathOrigin));
			parameter.setType(type);
			
			if (parameter.getKind().equals(ParameterKind.RATING)) {
				if (pathOrigin == PathOrigin.RATING_PROCESS) {
					if (!type.equals(ReadonlyRatingsHolder.class) && !type.equals(RatingsHolder.class)) {
						throw new AnnotationProcessorException("The rating process method parameter must be of type " + RatingsHolder.class.getSimpleName() + " or " + ReadonlyRatingsHolder.class.getSimpleName() + ".");
					}
				} else {
					if (!type.equals(ReadonlyRatingsHolder.class)) {
						throw new AnnotationProcessorException("The rating process method parameter must be of type " + ReadonlyRatingsHolder.class.getSimpleName() + ".");
					}
				}
			}
			if (parameter.getKind().equals(ParameterKind.OUT) || parameter.getKind().equals(ParameterKind.INOUT)) {
				if (pathOrigin == PathOrigin.RATING_PROCESS) {
					throw new AnnotationProcessorException("The rating process method parameter cannot contain " + Out.class.getSimpleName() + " nor " + InOut.class.getSimpleName() + " parameters.");
				}
			}
			
		} catch (Exception e) {
			String msg = "Parameter: "+(parameterIndex+1)+"->"+e.getMessage();
			throw new AnnotationProcessorException(msg, e);
		}
		return parameter;
	}
	
	/**
	 * Creator of a {@link KnowledgePath} from a String.  
	 *  
	 * @param path string to be parsed into knowledge path
	 * @param pathOrigin indicates whether it is being called within the context of {@link #createComponentProcess}, {@link #createEnsembleDefinition} or {@link #addSecurityTags(Class, KnowledgeManager, ChangeSet)} .
	 */
	KnowledgePath createKnowledgePath(String path, PathOrigin pathOrigin) throws ParseException, AnnotationProcessorException {
		PNode pNode = PathParser.parse(path);
		return createKnowledgePath(pNode, pathOrigin);
	}
	
	/**
	 * Creator of {@link KnowledgePath} from a {@link PNode}.
	 * <p>
	 * It throws an exception in the following two cases:
	 * <ul>
	 * <li>When it is called in the context of an ensemble process and a (sub-)path does not start with one of the <code>coord</code> or <code>member</code> keywords.</li>
	 * <li>When a (sub-)path contains the <code>id</code> keyword and it is <b>not</b> in the last position in the (sub-)path.</li>
	 * </ul>
	 * </p>
	 * 
	 * @param pNode head of the list as retrieved by the {@link PathParser#parse} method.
	 * @param pathOrigin indicates whether it is being called within the context of {@link #createComponentProcess}, {@link #createEnsembleDefinition} or {@link #addSecurityTags(Class, KnowledgeManager, ChangeSet)} .
	 */
	KnowledgePath createKnowledgePath(PNode pNode, PathOrigin pathOrigin) throws AnnotationProcessorException {
		KnowledgePath knowledgePath = factory.createKnowledgePath();
		do {	
			Object nValue = pNode.value;
			if (nValue instanceof String) {
				// check if the first node in an ensemble path is not 'coord' or 'member':
				if (pathOrigin == PathOrigin.ENSEMBLE && knowledgePath.getNodes().isEmpty()) {
					throw new AnnotationProcessorException(
						"The path does not start with one of the '"
						+ EEnsembleParty.COORDINATOR.toString() + "' or '"
						+ EEnsembleParty.MEMBER.toString() + "' keywords."); 
				}
				// Check if this is a component identifier ("id") node.
				// In such case, this has to be the final node in the path:
				if ((nValue.equals(ComponentIdentifier.ID.toString()))
					&& (((pathOrigin == PathOrigin.COMPONENT || pathOrigin == PathOrigin.SECURITY_ANNOTATION || pathOrigin == PathOrigin.RATING_PROCESS) && knowledgePath.getNodes().isEmpty()) 
					|| (pathOrigin == PathOrigin.ENSEMBLE && (knowledgePath.getNodes().size() == 1)))) {
						PathNodeComponentId idField = factory.createPathNodeComponentId();
						knowledgePath.getNodes().add(idField); 
						if (pNode.next!=null) {
							throw new AnnotationProcessorException(
									"A component identifier cannot be followed by any other fields in a path.");
						} 
						return knowledgePath;
				} 
				PathNodeField pathNodeField = factory.createPathNodeField();
				pathNodeField.setName((String) nValue);
				knowledgePath.getNodes().add(pathNodeField);
			}
			if (nValue instanceof EEnsembleParty) {
				EEnsembleParty ensembleKeyword = (EEnsembleParty) nValue;
				if (pathOrigin == PathOrigin.ENSEMBLE && knowledgePath.getNodes().isEmpty())  {
					knowledgePath.getNodes().add(createMemberOrCoordinatorPathNode(ensembleKeyword));
				} else {
					PathNodeField pathNodeField = factory.createPathNodeField();
					pathNodeField.setName(ensembleKeyword.toString());
					knowledgePath.getNodes().add(pathNodeField);
				}
			}
			if (nValue instanceof PNode) {
				PathNodeMapKey pathNodeMapKey = factory.createPathNodeMapKey();
				pathNodeMapKey.setKeyPath(createKnowledgePath((PNode) nValue, pathOrigin));
				knowledgePath.getNodes().add(pathNodeMapKey);
			}
			pNode = pNode.next;
		} while (!(pNode == null));
		return knowledgePath;
	}

	/**
	 * Simple creator of either {@link PathNodeCoordinator} or {@link PathNodeMember} object.  
	 */
	PathNode createMemberOrCoordinatorPathNode(EEnsembleParty keyword)
			throws AnnotationProcessorException {
		switch (keyword) {
			case COORDINATOR:
				return factory.createPathNodeCoordinator();
			case MEMBER:
				return factory.createPathNodeMember();
			default:
				throw new AnnotationProcessorException(
						"Invalid identifier: 'coord' or 'member' keyword expected.");
		}
	}
	
	/**
	 * Returns a method in the class definition which is annotated as
	 * DEECo ensemble condition/exchange (ie, with @{@link Condition}/@{@link Exchange} annotation).
	 * <p>
	 * In case there is <i>not</i> <b>exactly one, public, static</b> method with the requested annotation, it throws an exception.
	 * </p>
	 */
	Method getAnnotatedMethodInEnsemble(Class<?> clazz,
			Class<? extends Annotation> annotationClass)
			throws AnnotationProcessorException {
		Method foundMethod = null;
		Method[] methods = clazz.getDeclaredMethods();
		for (Method m : methods) {
			if (m.getAnnotation(annotationClass) != null) {
				int modifier = m.getModifiers();
				if (Modifier.isPublic(modifier) && Modifier.isStatic(modifier)){
					if (foundMethod == null) {
						foundMethod = m;
					} else {
						throw new AnnotationProcessorException(
								"More than one instance of @"
										+ annotationClass.getSimpleName()
										+ " annotation was found");
					}
				} else {
					throw new AnnotationProcessorException("Method "+ m.getName() + 
							" annotated as @" + annotationClass.getSimpleName()
							+ " should be public and static.");
				}
			}
		}
		if (foundMethod == null) {
			throw new AnnotationProcessorException("No @" + annotationClass.getSimpleName() + " annotation was found");
		}
		return foundMethod;
	}

	/**
	 * Retrieves the direction annotation, i.e. one of {Inout, In, Out, Rank} from an array of annotations.  
	 * <p>
	 * If more than one / none direction annotation is found, it throws an exception.
	 * </p>
	 */
	Annotation getKindAnnotation(Annotation[] annotations) throws AnnotationProcessorException {
		Annotation foundAnnotation = null;
		for (Annotation a : annotations) {
			if (parameterAnnotationsToParameterKinds.containsKey(a.annotationType())) {
				if (foundAnnotation == null) {
					foundAnnotation = a;
				} else {
					throw new AnnotationProcessorException("More than one kind annotation was found.");
				}
			}
		}
		if (foundAnnotation == null) {
			throw new AnnotationProcessorException("No kind annotation was found.");
		}
		return foundAnnotation;
	}
	
	/**
	 * Helper method to get the value of a direction annotation. 
	 */
	String getKindAnnotationValue(Annotation a) {
		if (a instanceof In) {
			return ((In) a).value();
		}
		if (a instanceof Out) {
			return ((Out) a).value();
		}
		if (a instanceof InOut) {
			return ((InOut) a).value();
		}
		if (a instanceof Rating) {
			return ((Rating) a).value();
		}
		Log.w("Invalid argument in getKindAnnotation()");
		return null;
	}

	/**
	 * Helper method to get an annotation of a particular class out of an array of annotations. 
	 */
	@SuppressWarnings("unchecked")
	<T extends Annotation> T getAnnotation(Annotation[] annotations,
			Class<T> annotationClass) {
		for (Annotation a : annotations) {
			if (annotationClass.isInstance(a)) {
				return (T) a;
			}
		}
		return null;
	}

	boolean isComponentDefinition(Class<?> clazz) {
		return clazz != null && clazz.isAnnotationPresent(Component.class);
	}

	boolean isEnsembleDefinition(Class<?> clazz) {
		return clazz != null && clazz.isAnnotationPresent(Ensemble.class);
	}
	
	/**
	 * Returns a {@link ChangeSet} containing the initial values of the knowledge of the parsed Component.
	 *  
	 * @param knowledge the object of a class annotated as DEECo component (@{@link Component}).
	 * @throws AnnotationProcessorException 
	 */
	ChangeSet extractInitialKnowledge(Object knowledge, boolean local) throws AnnotationProcessorException {
		ChangeSet changeSet = new ChangeSet();
		for (Field f : knowledge.getClass().getDeclaredFields()) {
			if ((!Modifier.isPublic(f.getModifiers()))
					|| (Modifier.isStatic(f.getModifiers()))) {
				Log.w(new StringBuilder()
						.append("Non-public or static fields are ignored during the extraction of initial knowledge (")
						.append(knowledge.getClass().getCanonicalName())
						.toString());
				break;
			}
		}
		for (Field f : knowledge.getClass().getFields())
			if (!Modifier.isStatic(f.getModifiers())) {
				Allow[] allowAnnotations = f.getAnnotationsByType(Allow.class);
				if (allowAnnotations.length > 0 && local && isAnnotatedAsLocal(f)) {
					throw new AnnotationProcessorException("Local knowledge must not be secured.");
				}
				
				if (((isAnnotatedAsLocal(f)) && (local))
						|| ((!isAnnotatedAsLocal(f)) && (!local))) {
					KnowledgePath knowledgePath = this.factory
							.createKnowledgePath();
					PathNodeField pathNodeField = this.factory
							.createPathNodeField();
					pathNodeField.setName(new String(f.getName()));
					knowledgePath.getNodes().add(pathNodeField);
					try {
						changeSet.setValue(knowledgePath, f.get(knowledge));
					} catch (IllegalAccessException e) {
					}
				}
			}
		return changeSet;
	}
	
	private boolean isAnnotatedAsLocal(Field f) {
		return (f != null) && (f.getAnnotation(Local.class) != null);
	}

	/**
	 * Returns component ID given the initial knowledge. Returns null if ID is
	 * not specified as a public non-static String field.
	 */
	String getComponentId(ChangeSet initialK) {
		KnowledgePath idPath = factory.createKnowledgePath();
		PathNodeField pnf = factory.createPathNodeField();
		pnf.setName("id");
		idPath.getNodes().add(pnf);
		String id = (String) initialK.getValue(idPath);
		return id;
	}
	
	/**
	 * Calls the processor's extensions.
	 * 
	 * Annotations are passed both as a list and individually (for client code convenience).
	 * 
	 * @param event	the type of the event (point at the main processing process)
	 * @param object	the context of the call
	 * @param unknownAnnotations	annotations delegated to the callee
	 */
	private void callExtensions(ParsingEvent event, Object object, List<Annotation> unknownAnnotations) throws AnnotationProcessorException {
		if ((extensions != null) && (extensions.length > 0)) {
			for (AnnotationProcessorExtensionPoint extension : extensions) {
				Log.d("in 'CallExtensions': [EventType: "+ event + ", runtimeObject: " + object + ", extension: "+ extension +"]");
				switch (event) {
				case ON_COMPONENT_CREATION: 
					extension.onComponentInstanceCreation((ComponentInstance) object, unknownAnnotations); 
					for (Annotation a: unknownAnnotations) {
						extension.onComponentInstanceCreation((ComponentInstance) object, a); 
					}
					break;  
				case ON_PROCESS_CREATION: 
					extension.onComponentProcessCreation((ComponentProcess) object, unknownAnnotations); 
					for (Annotation a: unknownAnnotations) {
						extension.onComponentProcessCreation((ComponentProcess) object, a); 
					}
					break;
				case ON_ENSEMBLE_CREATION: 
					extension.onEnsembleDefinitionCreation((EnsembleDefinition) object, unknownAnnotations); 
					for (Annotation a: unknownAnnotations) {
						extension.onEnsembleDefinitionCreation((EnsembleDefinition) object, a); 
					}
					break;
				case ON_UNKNOWN_COMPONENT_METHOD_ANNOTATION:
					extension.onUnknownMethodAnnotation(this, true, (Method) object, unknownAnnotations); 
					for (Annotation a: unknownAnnotations) {
						extension.onUnknownMethodAnnotation(this, true, (Method) object, a); 
					}					
					break;
				case ON_UNKNOWN_ENSEMBLE_METHOD_ANNOTATION:
					extension.onUnknownMethodAnnotation(this, false, (Method) object, unknownAnnotations); 
					for (Annotation a: unknownAnnotations) {
						extension.onUnknownMethodAnnotation(this, false, (Method) object, a); 
					}					
					break;
				}
			}
		}
	}
	
	/**
	 * Go through the annotations of a class and get the ones that are not handled by this processor.
	 */
	List<Annotation> getUnknownAnnotations(Class<?> clazz) {
		List<Annotation> unknownAnnotations = new ArrayList<>();
		for (Annotation a: clazz.getAnnotations()) {
			if (!KNOWN_CLASS_ANNOTATIONS.contains(a.getClass())) {
				unknownAnnotations.add(a);	
			}
		}
		return unknownAnnotations;
	}

	/**
	 * Go through the annotations of a method and get the ones that are not handled by this processor. 
	 */
	List<Annotation> getUnknownAnnotations(Method m) {
		List<Annotation> unknownAnnotations = new ArrayList<>();
		for (Annotation a: m.getAnnotations()) {
			if (!KNOWN_METHOD_ANNOTATIONS.contains(a.getClass())) {
				unknownAnnotations.add(a);
			}
		}
		return unknownAnnotations;
	}
	
}
