package cz.cuni.mff.d3s.deeco.annotations.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cz.cuni.mff.d3s.deeco.annotations.*;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.*;
import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.CloningKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeUpdateException;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.*;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.InaccuracyParamHolder;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.ModelInterface;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.TSParamHolder;
import cz.cuni.mff.d3s.deeco.network.CommunicationBoundaryPredicate;
import cz.cuni.mff.d3s.deeco.network.GenericCommunicationBoundaryPredicate;

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
 * @author Rima Al Ali <alali@d3s.mff.cuni.cz>
 * 
 */
public class AnnotationProcessor {
	
	protected static final double SEC_NANOSECOND_FACTOR = 1000000000;
	
	
	private static final Map<Class<? extends Annotation>, ParameterDirection> parameterAnnotationsToParameterDirections = Collections
			.unmodifiableMap(new HashMap<Class<? extends Annotation>, ParameterDirection>() {
				private static final long serialVersionUID = 1L;
				{
					put(InOut.class, ParameterDirection.INOUT);
					put(In.class, ParameterDirection.IN);
					put(Out.class, ParameterDirection.OUT);
				}
			});
	protected RuntimeMetadataFactory factory;
	private double currentTime = System.nanoTime() / SEC_NANOSECOND_FACTOR;
	
	/**
	 * Initializes the processor with the given model factory.
	 * All the model elements produced by the processor will be created via this factory.
	 */
	public AnnotationProcessor(RuntimeMetadataFactory factory) {
		this.factory = factory;
	}
		
	

	/**
	 * Processing of a single file.
	 * 
	 * @param model
	 *            EMF model to be updated
	 * @param obj
	 *            object to be processed
	 * @throws AnnotationProcessorException
	 */
	public void process(RuntimeMetadata model, Object obj) throws AnnotationProcessorException {
		processObject(model, obj); 
	}
	
	/**
	 * Batch processing of multiple inputs provided as extra parameters.
	 * 
	 * @param model
	 *            EMF model to be updated
	 * @param obj
	 *            object to be processed
	 * @param objs
	 *            rest of objects to be processed, if any
	 * @throws AnnotationProcessorException
	 */
	public void process(RuntimeMetadata model, Object obj, Object... objs) throws AnnotationProcessorException {
		processObject(model, obj); 
		for (Object o: objs) {
			processObject(model, o);
		}
	}
	
	/**
	 * Batch processing of multiple inputs provided as a list of objects.
	 * 
	 * @param model
	 *            EMF model to be updated
	 * @param objs
	 *            list of objects to be processed
	 * @throws AnnotationProcessorException
	 */
	public void process(RuntimeMetadata model, List<Object> objs) throws AnnotationProcessorException {
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
	 * Checks if the object is annotated as @{@link Component}/@{@link Ensemble} and calls the respective creator. 
	 * It also creates the appropriate {@link EnsembleController}s.
	 * <p>
	 * If both/no such annotations are found, it throws an exception.
	 * </p>
	 * 
	 * @param obj
	 *            object to be processed
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
		boolean isSSM = isStateSpaceModel(clazz);
		
		if ((isC && isE) || (isE && isSSM)) {
			throw new AnnotationProcessorException(
					"Class: " + clazz.getCanonicalName() +
					"->Both @" + Component.class.getSimpleName() + " or @" + Ensemble.class.getSimpleName() + " annotation found."+
					"or ->Both @"+ Ensemble.class.getSimpleName() + " and @"+ StateSpaceModel.class.getSimpleName() + " are found.");
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
					"For a component to be parsed, it has to be an INSTANCE of a class annotated with @"
							+ Component.class.getSimpleName() + ".");	
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

			ChangeSet initialK = extractInitialKnowledge(obj);
			String id = getComponentId(initialK);
			if (id == null) {
				id = clazz.getSimpleName() + UUID.randomUUID().toString();
			}
	        KnowledgeManager km = new CloningKnowledgeManager(id);		
			km.update(initialK);
	        
	        boolean isSSM = isStateSpaceModel(clazz);
		    if(isSSM){
		    	List<StateSpaceModelDefinition> statespaceModels = new ArrayList<>();
		    	statespaceModels = createStateSpaceModelAnnotation(clazz, componentInstance, initialK); 
			    componentInstance.getStateSpaceModels().addAll(statespaceModels);
			    initialK = extractInitialKnowledgeWithInaccuracy(initialK, statespaceModels);
			}
	        
			List<Method> methods = getMethodsMarkedAsProcesses(clazz);
	        
			for (Method m : methods) {
				int modifier = m.getModifiers();
				if (Modifier.isPublic(modifier) && Modifier.isStatic(modifier)) {
					componentInstance.getComponentProcesses().add(
							createComponentProcess(componentInstance, m));
				} else {
					throw new AnnotationProcessorException(
							"Method "+ m.getName()+ " annotated as @" + Process.class.getSimpleName() + 
							" should be public and static.");
				}
			}
			
			for (Method m : methods) {
				createTriggerTransition(componentInstance,m);
			}
			
			
			km.update(initialK);
			componentInstance.setKnowledgeManager(km);

		} catch (KnowledgeUpdateException | AnnotationProcessorException
				| ParseException | InstantiationException | IllegalAccessException e) {
			String msg = Component.class.getSimpleName() + ": "
					+ componentInstance.getName() + "->" + e.getMessage();
			throw new AnnotationProcessorException(msg, e);
		}
		return componentInstance;
	}

	
	public List<StateSpaceModelDefinition> createStateSpaceModelAnnotation(Class<?> clazz,ComponentInstance componentInstance, ChangeSet initialK) throws InstantiationException, IllegalAccessException{
		
		
		List<StateSpaceModelDefinition> statespaceModels = new ArrayList<>();
		for (Annotation ann : clazz.getAnnotations()) {
			if(ann.annotationType().getSimpleName().equals("StateSpaceModel")){
				
				for (Model models : ((StateSpaceModel)ann).models()) {
					StateSpaceModelDefinition ssm = factory.createStateSpaceModelDefinition();
					ssm.setComponentInstance(componentInstance);
					ssm.setIsActive(true);
					
					
					//Adding inStates....
					for (String state : models.state()) {
						for (KnowledgePath kp : initialK.getUpdatedReferences()) {
							if((kp.toString().equals(state)) && (initialK.getValue(kp) instanceof TSParamHolder)){
								InaccuracyParamHolder newValue = new InaccuracyParamHolder();
								TSParamHolder oldValue = (TSParamHolder)initialK.getValue(kp);
								newValue.setWithTS(oldValue);
								initialK.setValue(kp, newValue);
								ssm.getInStates().add(kp);										
							}
						}
					}
				
					
					//Adding dervStates....
					Class<?> m = models.result().referenceModel();
					Object newModel = m.newInstance();
					ValueSet vs = new ValueSet();
					ArrayList<InaccuracyParamHolder<Object>> inaccs = new ArrayList<InaccuracyParamHolder<Object>>(); 
					for (KnowledgePath inKP : ssm.getInStates()) {
						inaccs.add((InaccuracyParamHolder<Object>)initialK.getValue(inKP));
					}

					int[] returnIndex = models.result().returnedIndex();
					for (int i = 0; i< models.result().returnedIndex().length; i++) {
						if(returnIndex[i] > -1){	
							ssm.getDerivationStates().add(ssm.getInStates().get(returnIndex[i]));
						}else{
							InaccuracyParamHolder newBoundraies = new InaccuracyParamHolder<>();
							for (KnowledgePath kp : initialK.getUpdatedReferences()) {
								if((kp.toString().equals(models.state()[i]))){
									if(newModel instanceof ModelInterface){
										ssm.setModel((ModelInterface)newModel);
										newBoundraies = ((ModelInterface<Object>) newModel).getModelBoundaries(inaccs);	
										ssm.setModelValue(newBoundraies);
									}
									TimeTrigger timeTrigger = factory.createTimeTrigger();
									timeTrigger.setPeriod(models.period());
									timeTrigger.setOffset(0);
									ssm.getTriggers().add(timeTrigger);
								}
							}
						}
					}
					statespaceModels.add(ssm);
				}	
			}
		}

		return statespaceModels;
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
			Condition condition = createCondition(clazz);
			ensembleDefinition.setMembership(condition);
			Exchange exchange = createExchange(clazz);
			ensembleDefinition.setKnowledgeExchange(exchange);
			CommunicationBoundaryPredicate cBoundary = createCommunicationBoundary(clazz);			
			ensembleDefinition.setCommunicationBoundary(cBoundary);
			
			TimeTrigger periodicEnsembleTrigger = createPeriodicTrigger(clazz);
			List<KnowledgeChangeTrigger> exchangeKChangeTriggers = createKnowledgeChangeTriggers(exchange.getMethod(), false);
			List<KnowledgeChangeTrigger> conditionKChangeTriggers = createKnowledgeChangeTriggers(condition.getMethod(), false);
			if (periodicEnsembleTrigger == null) {
				if (exchangeKChangeTriggers.isEmpty() && conditionKChangeTriggers.isEmpty()) {
					throw new AnnotationProcessorException("No triggers were found.");
				}
			} else {
				ensembleDefinition.getTriggers().add(periodicEnsembleTrigger);
			}
			ensembleDefinition.getTriggers().addAll(exchangeKChangeTriggers);
			ensembleDefinition.getTriggers().addAll(conditionKChangeTriggers);
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
			condition.getParameters().addAll(createParameters(m, false));
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
			exchange.getParameters().addAll(createParameters(m, false));
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
			componentProcess.getParameters().addAll(createParameters(m, true));
			TimeTrigger periodicTrigger = createPeriodicTrigger(m);
			List<KnowledgeChangeTrigger> knowledgeChangeTriggers = createKnowledgeChangeTriggers(m, true);
			List<KnowledgeValueChangeTrigger> knowledgeValueChangeTriggers = createKnowledgeValueChangeTriggers(componentInstance, m, true);
			List<KnowledgeValueUnchangeTrigger> knowledgeValueUnchangeTriggers = createKnowledgeValueUnchangeTriggers(componentInstance, m, true);
			if (periodicTrigger == null) {
				if (knowledgeChangeTriggers.isEmpty()&&knowledgeValueChangeTriggers.isEmpty()&&knowledgeValueUnchangeTriggers.isEmpty()) {
					throw new AnnotationProcessorException("No triggers were found.");
				}
			} else {
				componentProcess.getTriggers().add(periodicTrigger);
			}
			componentProcess.getTriggers().addAll(knowledgeChangeTriggers);
			componentProcess.getTriggers().addAll(knowledgeValueChangeTriggers);
			componentProcess.getTriggers().addAll(knowledgeValueUnchangeTriggers);
			componentProcess.setState(ModeState.IDLE);
				
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
				periodicTrigger.setPeriod(((PeriodicScheduling) a).value());
				periodicTrigger.setOffset(0);
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
	 * @param inComponentProcess indicates whether it is being called within the context of {@link #createComponentProcess} or {@link #createEnsembleDefinition}. 
	 */
	List<KnowledgeChangeTrigger> createKnowledgeChangeTriggers(Method method, boolean inComponentProcess) 
			throws AnnotationProcessorException, ParseException {
		List<KnowledgeChangeTrigger> knowledgeChangeTriggers = new ArrayList<>();
		Type[] parameterTypes = method.getParameterTypes();
		Annotation[][] allAnnotations = method.getParameterAnnotations();
		for (int i = 0; i < parameterTypes.length; i++) {
			TriggerOnChange t = getAnnotation(allAnnotations[i], TriggerOnChange.class);
			if (t != null) {
				Annotation directionAnnotation = getDirectionAnnotation(allAnnotations[i]);
				String path = getDirectionAnnotationValue(directionAnnotation);
				KnowledgeChangeTrigger trigger = factory.createKnowledgeChangeTrigger();
				trigger.setKnowledgePath(createKnowledgePath(path, inComponentProcess));
				knowledgeChangeTriggers.add(trigger);
			}
		}
		return knowledgeChangeTriggers;
	}

	
	List<KnowledgeValueChangeTrigger> createKnowledgeValueChangeTriggers(ComponentInstance componentInstance, Method method, boolean inComponentProcess) 
			throws AnnotationProcessorException, ParseException {
		List<KnowledgeValueChangeTrigger> knowledgeValueChangeTriggers = new ArrayList<>();
		Type[] parameterTypes = method.getParameterTypes();
		Annotation[][] allAnnotations = method.getParameterAnnotations();
		for (int i = 0; i < parameterTypes.length; i++) {
			TriggerOnValueChange t = getAnnotation(allAnnotations[i], TriggerOnValueChange.class);
			if (t != null) {
				Annotation directionAnnotation = getDirectionAnnotation(allAnnotations[i]);
				String path = getDirectionAnnotationValue(directionAnnotation);
				KnowledgeValueChangeTrigger trigger = factory.createKnowledgeValueChangeTrigger();
				trigger.setKnowledgePath(createKnowledgePath(path, inComponentProcess));
				for (String guard : t.guard()) {
					trigger.getCondition().add(guard);
				}
				knowledgeValueChangeTriggers.add(trigger);
			}
		}
		return knowledgeValueChangeTriggers;
	}
	
	List<KnowledgeValueUnchangeTrigger> createKnowledgeValueUnchangeTriggers(ComponentInstance componentInstance, Method method, boolean inComponentProcess) 
			throws AnnotationProcessorException, ParseException {
		List<KnowledgeValueUnchangeTrigger> knowledgeValueUnchangeTriggers = new ArrayList<>();
		Type[] parameterTypes = method.getParameterTypes();
		Annotation[][] allAnnotations = method.getParameterAnnotations();
		for (int i = 0; i < parameterTypes.length; i++) {
			TriggerOnValueUnchange t = getAnnotation(allAnnotations[i], TriggerOnValueUnchange.class);
			if (t != null) {
				Annotation directionAnnotation = getDirectionAnnotation(allAnnotations[i]);
				String path = getDirectionAnnotationValue(directionAnnotation);
				KnowledgeValueUnchangeTrigger trigger = factory.createKnowledgeValueUnchangeTrigger();
				trigger.setKnowledgePath(createKnowledgePath(path, inComponentProcess));
				for (String guard : t.guard()) {
					trigger.getCondition().add(guard);
				}
				knowledgeValueUnchangeTriggers.add(trigger);
			}
		}
		return knowledgeValueUnchangeTriggers;
	}

	void createTriggerTransition(ComponentInstance componentInstance, Method method) 
			throws AnnotationProcessorException, ParseException {
		Type[] parameterTypes = method.getParameterTypes();
		Annotation[][] allAnnotations = method.getParameterAnnotations();
		ComponentProcess toProcess = getProcess(componentInstance, method.getName(), null);
	
		for (int i = 0; i < parameterTypes.length; i++) {
			TriggerOnValueUnchange t = getAnnotation(allAnnotations[i], TriggerOnValueUnchange.class);
			if (t != null) {
				for (Trigger trigger : toProcess.getTriggers()) {
					if(trigger instanceof KnowledgeValueUnchangeTrigger){
						String[] from = t.from();
						for (String string : from) {
							System.out.println(string);
							if(string.equals("")){
								toProcess.setState(ModeState.RUNNING);
								((KnowledgeValueUnchangeTrigger)trigger).getFrom().add(toProcess);
								((KnowledgeValueUnchangeTrigger)trigger).setTo(toProcess);
							}else{
								if(toProcess.getState() != ModeState.RUNNING)
									toProcess.setState(ModeState.IDLE);
								ComponentProcess fromProcess = getProcess(componentInstance, string, toProcess);
								((KnowledgeValueUnchangeTrigger)trigger).getFrom().add(fromProcess);
								((KnowledgeValueUnchangeTrigger)trigger).setTo(toProcess);
							}
						}
					}
				}
			}
			TriggerOnValueChange tc = getAnnotation(allAnnotations[i], TriggerOnValueChange.class);
			if (tc != null) {
				for (Trigger trigger : toProcess.getTriggers()) {
					if(trigger instanceof KnowledgeValueChangeTrigger){
						String[] from = tc.from();
						for (String string : from) {
							if(string.equals("")){
								toProcess.setState(ModeState.RUNNING);
								((KnowledgeValueChangeTrigger)trigger).getFrom().add(toProcess);
								((KnowledgeValueChangeTrigger)trigger).setTo(toProcess);
							}else{
								if(toProcess.getState() != ModeState.RUNNING)
									toProcess.setState(ModeState.IDLE);
								ComponentProcess fromProcess = getProcess(componentInstance, string, toProcess);
								((KnowledgeValueChangeTrigger)trigger).getFrom().add(fromProcess);
								((KnowledgeValueChangeTrigger)trigger).setTo(toProcess);
							}
						}
					}
				}
			}
		}
	}

	
	
	private ComponentProcess getProcess(ComponentInstance componentInstance,
			String name, ComponentProcess toProcess) {
		for (ComponentProcess process : componentInstance.getComponentProcesses()) {
			if(process.getName().equals(name))
				return process;
		}
	
		return toProcess;
	} 
	

	/**
	 * Creator a list of {@link Parameter} from a method. 
	 * <p>
	 * If the method has no parameters, it throws an exception.
	 * </p>
	 * 
	 * @param method method to be parsed
	 * @param inComponentProcess indicates whether it is being called within the context of {@link #createComponentProcess createComponentProcess()} or {@link #createEnsembleDefinition createEnsembleDefinition()}.
	 */
	List<Parameter> createParameters(Method method, boolean inComponentProcess)
			throws AnnotationProcessorException, ParseException {
		List<Parameter> parameters = new ArrayList<>();
		Class<?>[] parameterTypes = method.getParameterTypes();
		Annotation[][] allAnnotations = method.getParameterAnnotations();
		for (int i = 0; i < parameterTypes.length; i++) {
			parameters.add(createParameter(parameterTypes[i], i, allAnnotations[i], inComponentProcess));
		}
		if (parameters.isEmpty()) {
 			throw new AnnotationProcessorException(
 					"The "+ (inComponentProcess ? "component" : "ensemble") + " process cannot have zero parameters.");
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
	 * @param inComponentProcess indicates whether it is being called within the context of {@link #createComponentProcess createComponentProcess()} or {@link #createEnsembleDefinition createEnsembleDefinition()}.
	 */
	Parameter createParameter(Class<?> type, int parameterIndex, Annotation[] parameterAnnotations, boolean inComponentProcess)
			throws AnnotationProcessorException, ParseException {
		Parameter parameter = factory.createParameter();
		try {
			Annotation directionAnnotation = getDirectionAnnotation(parameterAnnotations);
			parameter.setDirection(parameterAnnotationsToParameterDirections.get(directionAnnotation.annotationType()));
			String path = getDirectionAnnotationValue(directionAnnotation);
			parameter.setKnowledgePath(createKnowledgePath(path,inComponentProcess));
			parameter.setType(type);
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
	 * @param inComponentProcess indicates whether it is being called within the context of {@link #createComponentProcess createComponentProcess()} or {@link #createEnsembleDefinition createEnsembleDefinition()}.
	 */
	KnowledgePath createKnowledgePath(String path, boolean inComponentProcess) throws ParseException, AnnotationProcessorException {
		PNode pNode = PathParser.parse(path);
		return createKnowledgePath(pNode, inComponentProcess);
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
	 * @param inComponentProcess indicates whether it is being called within the context of {@link #createComponentProcess createComponentProcess()} or {@link #createEnsembleDefinition createEnsembleDefinition()}.
	 */
	KnowledgePath createKnowledgePath(PNode pNode, boolean inComponentProcess) throws AnnotationProcessorException {
		KnowledgePath knowledgePath = factory.createKnowledgePath();
		do {	
			Object nValue = pNode.value;
			if (nValue instanceof String) {
				// check if the first node in an ensemble path is not 'coord' or 'member':
				if (!inComponentProcess && knowledgePath.getNodes().isEmpty()) {
					throw new AnnotationProcessorException(
						"The path does not start with one of the '"
						+ EEnsembleParty.COORDINATOR.toString() + "' or '"
						+ EEnsembleParty.MEMBER.toString() + "' keywords."); 
				}
				// Check if this is a component identifier ("id") node.
				// In such case, this has to be the final node in the path:
				if ((nValue.equals(ComponentIdentifier.ID.toString()))
					&& ((inComponentProcess && knowledgePath.getNodes().isEmpty()) 
					|| (!inComponentProcess && (knowledgePath.getNodes().size() == 1)))) {
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
				if (!inComponentProcess && knowledgePath.getNodes().isEmpty())  {
					knowledgePath.getNodes().add(createMemberOrCoordinatorPathNode(ensembleKeyword));
				} else {
					PathNodeField pathNodeField = factory.createPathNodeField();
					pathNodeField.setName(ensembleKeyword.toString());
					knowledgePath.getNodes().add(pathNodeField);
				}
			}
			if (nValue instanceof PNode) {
				PathNodeMapKey pathNodeMapKey = factory.createPathNodeMapKey();
				pathNodeMapKey.setKeyPath(createKnowledgePath((PNode) nValue, inComponentProcess));
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
	 * Returns list of methods in the class definition which are annotated as
	 * DEECo processes.
	 */
	List<Method> getMethodsMarkedAsProcesses(Class<?> c) {
		List<Method> result = new ArrayList<Method>();
		if (c != null) {
			Method[] methods = c.getMethods();
			for (Method m : methods) {
				if (m.getAnnotation(Process.class) != null)
					result.add(m);
			}
		}
		return result;
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
	 * Retrieves the direction annotation, i.e. one of {Inout, In, Out} from an array of annotations.  
	 * <p>
	 * If more than one / none direction annotation is found, it throws an exception.
	 * </p>
	 */
	Annotation getDirectionAnnotation(Annotation[] annotations) throws AnnotationProcessorException {
		Annotation foundAnnotation = null;
		for (Annotation a : annotations) {
			if (parameterAnnotationsToParameterDirections.containsKey(a.annotationType())) {
				if (foundAnnotation == null) {
					foundAnnotation = a;
				} else {
					throw new AnnotationProcessorException("More than one direction annotation was found.");
				}
			}
		}
		if (foundAnnotation == null) {
			throw new AnnotationProcessorException("No direction annotation was found.");
		}
		return foundAnnotation;
	}
	
	/**
	 * Helper method to get the value of a direction annotation. 
	 */
	String getDirectionAnnotationValue(Annotation a) {
		if (a instanceof In) {
			return ((In) a).value();
		}
		if (a instanceof Out) {
			return ((Out) a).value();
		}
		if (a instanceof InOut) {
			return ((InOut) a).value();
		}
		Log.w("Invalid argument in getDirectionAnnotation()");
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
	
	
	boolean isStateSpaceModel(Class<?> clazz) {
		return clazz != null && clazz.isAnnotationPresent(StateSpaceModel.class);
	}
	
	/**
	 * Returns a {@link ChangeSet} containing the initial values of the knowledge of the parsed Component.
	 *  
	 * @param knowledge the object of a class annotated as DEECo component (@{@link Component}).
	 */
	ChangeSet extractInitialKnowledge(Object knowledge) {
		ChangeSet changeSet = new ChangeSet();
		
		// print a warning if the component definition contains non-public fields
		for (Field f : knowledge.getClass().getDeclaredFields()) {
			if (!Modifier.isPublic(f.getModifiers()) || Modifier.isStatic(f.getModifiers())) {
				Log.w("Non-public or static fields are ignored during the extraction of initial knowledge ("
						+ knowledge.getClass().getCanonicalName());
				break;
			}
		}
		for (Field f : knowledge.getClass().getFields()) {
			if (Modifier.isStatic(f.getModifiers()))
				continue;
			KnowledgePath knowledgePath = factory.createKnowledgePath();
			PathNodeField pathNodeField = factory.createPathNodeField();
			pathNodeField.setName(new String(f.getName()));
			knowledgePath.getNodes().add(pathNodeField);
			try {
				Object value = null;
				Annotation[] anns = f.getDeclaredAnnotations();
				for (int i = 0; i < anns.length; i++) {
					if(anns[i].annotationType().getName().contains("TimeStamp")){
						value = new TSParamHolder();
						((TSParamHolder)value).value = f.get(knowledge);
						((TSParamHolder)value).creationTime = currentTime;
					}
				}
				
				if(value == null)
					value = f.get(knowledge);
				changeSet.setValue(knowledgePath, value);
			} catch (IllegalAccessException e) {
				continue;
			}
		}
		return changeSet;
	}
	
	HashMap<KnowledgePath,Transitions> extractInitialKnowledgeTransitions(Object knowledge) {
		HashMap<KnowledgePath,Transitions> transitions = new HashMap<KnowledgePath,Transitions>();
		
		// print a warning if the component definition contains non-public fields
		for (Field f : knowledge.getClass().getDeclaredFields()) {
			if (!Modifier.isPublic(f.getModifiers()) || Modifier.isStatic(f.getModifiers())) {
				Log.w("Non-public or static fields are ignored during the extraction of initial knowledge ("
						+ knowledge.getClass().getCanonicalName());
				break;
			}
		}
		for (Field f : knowledge.getClass().getFields()) {
			if (Modifier.isStatic(f.getModifiers()))
				continue;
			KnowledgePath knowledgePath = factory.createKnowledgePath();
			PathNodeField pathNodeField = factory.createPathNodeField();
			pathNodeField.setName(new String(f.getName()));
			knowledgePath.getNodes().add(pathNodeField);
			Annotation[] anns = f.getDeclaredAnnotations();
			for (int i = 0; i < anns.length; i++) {
				if(anns[i].annotationType().getName().contains("Transitions")){
					Transitions intrans = (Transitions)anns[i];
					transitions.put(knowledgePath, intrans);
				}
			}
		}
		return transitions;
	}
	
	ChangeSet extractInitialKnowledgeWithInaccuracy(ChangeSet changeSet, List<StateSpaceModelDefinition> stateSpaceModels) {
		
		for (StateSpaceModelDefinition stateSpaceModel : stateSpaceModels) {
			
			for (KnowledgePath kp : stateSpaceModel.getInStates()) {
				for (KnowledgePath csKP : changeSet.getUpdatedReferences()) {
					if(kp.equals(csKP)){
						InaccuracyParamHolder inacc = new InaccuracyParamHolder();
						if(changeSet.getValue(csKP) instanceof TSParamHolder){
						TSParamHolder v = (TSParamHolder)changeSet.getValue(csKP);
						inacc.setWithTS(v);
						changeSet.setValue(csKP, inacc);
						}else 
							System.err.println("The value of "+kp+" do not have a timestamp........");
					}
				}
			}
		
		}
		return changeSet;
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
}
