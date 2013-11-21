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

import cz.cuni.mff.d3s.deeco.annotations.AnnotationProxy;
import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.IValuedAnnotation;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.TriggerOnChange;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.ComponentIdentifier;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.EEnsembleParty;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.PNode;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.ParseException;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.PathParser;
import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.CloningKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.CloningKnowledgeManagerContainer;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.CloningKnowledgeManagerContainer;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeUpdateException;
import cz.cuni.mff.d3s.deeco.knowledge.ShadowKnowledgeManagerRegistryImpl;
import cz.cuni.mff.d3s.deeco.knowledge.ShadowKnowledgeManagerRegistry;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Condition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleController;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Exchange;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeChangeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterDirection;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNode;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeComponentId;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeCoordinator;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMapKey;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMember;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PeriodicTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;

/**
 * Common gateway for processing of Java source code classes with DEECo
 * annotations.
 * 
 * "Processing" means parsing the file, creating an eCore subgraph out of it,
 * attaching the subgraph to the top-level container, the "runtimeMetadata"
 * model of the application (which is provided in every <code>process</code>
 * method). The processor is parameterized by an EMF factory, which has to be
 * passed through the constructor.
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 * 
 */
public class AnnotationProcessor {
	
	private static final Map<Class<? extends Annotation>, ParameterDirection> parameterAnnotationsToParameterDirections = Collections
			.unmodifiableMap(new HashMap<Class<? extends Annotation>, ParameterDirection>() {
				private static final long serialVersionUID = 1L;
				{
					put(InOut.class, ParameterDirection.INOUT);
					put(In.class, ParameterDirection.IN);
					put(Out.class, ParameterDirection.OUT);
				}
			});
	private RuntimeMetadataFactory factory;
	private RuntimeMetadata model;
	
	public AnnotationProcessor(RuntimeMetadataFactory factory) {
		this.factory = factory;
	}
		
	private void setModel(RuntimeMetadata model) throws AnnotationParsingException {
		if (model == null) {
			throw new AnnotationParsingException("Provided model cannot be null.");
		}
		this.model = model;
	}

	/**
	 * Processing of a single file.
	 * 
	 * @param model
	 *            EMF model to be updated
	 * @param obj
	 *            object to be processed
	 * @throws AnnotationParsingException
	 */
	public void process(RuntimeMetadata model, Object obj) throws AnnotationParsingException {
		setModel(model);
		processObject(obj); 
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
	 * @throws AnnotationParsingException
	 */
	public void process(RuntimeMetadata model, Object obj, Object... objs) throws AnnotationParsingException {
		setModel(model);
		processObject(obj); 
		for (Object o: objs) {
			processObject(o);
		}
	}
	
	/**
	 * Batch processing of multiple inputs provided as a list of objects.
	 * 
	 * @param model
	 *            EMF model to be updated
	 * @param objs
	 *            rest of objects to be processed, if any
	 * @throws AnnotationParsingException
	 */
	public void process(RuntimeMetadata model, List<Object> objs) throws AnnotationParsingException {
		setModel(model);
		if (objs == null) {
			throw new AnnotationParsingException("Provide an initialized object or a non-empty list of objects.");
		}
		if (objs.isEmpty()) {
			throw new AnnotationParsingException("Cannot process an empty list.");
		}
		for (Object o: objs) {
			processObject(o);
		}
	}

	void processObject(Object obj) throws AnnotationParsingException {
		if (obj == null) {
			throw new AnnotationParsingException("Provided object(s) cannot be null.");
		}
		Class<?> clazz = obj.getClass();
		boolean isC = isComponentDefinition(clazz);
		boolean isE = isEnsembleDefinition(clazz);
		if (isC && isE) {
			throw new AnnotationParsingException("Class: "
					+ clazz.getCanonicalName()
					+ "->Both @Component or @Ensemble annotation found.");
		}
		if (isComponentDefinition(clazz)) {
			ComponentInstance ci = createComponentInstance(obj);
			model.getComponentInstances().add(ci);
			// Create ensemble controllers for all the already-processed ensemble definitions
			for (EnsembleDefinition ed: model.getEnsembleDefinitions()) {
				EnsembleController ec = factory.createEnsembleController();
				ec.setComponentInstance(ci);
				ec.setEnsembleDefinition(ed);
				ci.getEnsembleControllers().add(ec);
			}
			return;
		} if (isEnsembleDefinition(clazz)) {
			EnsembleDefinition ed = createEnsembleDefinition(obj);
			model.getEnsembleDefinitions().add(ed);
			// Create ensemble controllers for all the already-processed component instance definitions
			for (ComponentInstance ci: model.getComponentInstances()) {
				EnsembleController ec = factory.createEnsembleController();
				ec.setComponentInstance(ci);
				ec.setEnsembleDefinition(ed);
				ci.getEnsembleControllers().add(ec);
			}
			return;
		} 
		throw new AnnotationParsingException("Class: "
				+ clazz.getCanonicalName()
				+ "->No @Component or @Ensemble annotation found.");
	}

	ComponentInstance createComponentInstance(Object obj) throws AnnotationParsingException {
		Class<?> clazz = obj.getClass();
		ComponentInstance componentInstance = factory.createComponentInstance();
		componentInstance.setName(clazz.getCanonicalName());
		
		//TODO Below should be the id of the component passed instead of "String"
		KnowledgeManager km = new CloningKnowledgeManager(clazz.getSimpleName());
		try {
		km.update(extractInitialKnowledge(obj));
		componentInstance.setKnowledgeManager(km);	
		List<Method> methods = getMethodsMarkedAsProcesses(clazz);
			for (Method m : methods) {
				int modifier = m.getModifiers();
				if (Modifier.isPublic(modifier) && Modifier.isStatic(modifier)) {
					componentInstance.getComponentProcesses().add(
							createComponentProcess(componentInstance, m));
				} else {
					throw new AnnotationParsingException(
							"Method "+ m.getName()+ " annotated as @Process should be public and static.");
				}
			}
		} catch (KnowledgeUpdateException | AnnotationParsingException
				| ParseException e) {
			String msg = "Component: " + componentInstance.getName() + "->"
					+ e.getMessage();
			throw new AnnotationParsingException(msg, e);
		}
		return componentInstance;
	}
	
	EnsembleDefinition createEnsembleDefinition(Object obj) throws AnnotationParsingException {
		Class<?> clazz = obj.getClass();
		EnsembleDefinition ensembleDefinition = factory.createEnsembleDefinition();
		ensembleDefinition.setName(clazz.getCanonicalName());
		try {
			Condition condition = createCondition(clazz);
			ensembleDefinition.setMembership(condition);
			Exchange exchange = createExchange(clazz);
			ensembleDefinition.setKnowledgeExchange(exchange);
			PeriodicTrigger periodicEnsembleTrigger = createPeriodicTrigger(clazz);
			List<KnowledgeChangeTrigger> exchangeKChangeTriggers = createKnowledgeChangeTriggers(exchange.getMethod(), false);
			List<KnowledgeChangeTrigger> conditionKChangeTriggers = createKnowledgeChangeTriggers(condition.getMethod(), false);
			if (periodicEnsembleTrigger == null) {
				if (exchangeKChangeTriggers.isEmpty() && conditionKChangeTriggers.isEmpty()) {
					throw new AnnotationParsingException("No triggers were found.");
				}
			} else {
				ensembleDefinition.getTriggers().add(periodicEnsembleTrigger);
			}
			ensembleDefinition.getTriggers().addAll(exchangeKChangeTriggers);
			ensembleDefinition.getTriggers().addAll(conditionKChangeTriggers);
		} catch (AnnotationParsingException | ParseException e) {
			String msg = "EnsembleDefinition: " + ensembleDefinition.getName() + "->" + e.getMessage();
			throw new AnnotationParsingException(msg, e);
		}
		return ensembleDefinition;
	}
	
	Condition createCondition(Class<?> clazz) throws AnnotationParsingException, ParseException{
		Method m = getAnnotatedMethodInEnsemble(clazz, Membership.class);
		Condition condition = factory.createCondition();
		condition.setMethod(m);
		condition.getParameters().addAll(createParameters(m, false));
		return condition;
	}
	
	Exchange createExchange(Class<?> clazz) throws AnnotationParsingException,ParseException {
		Method m = getAnnotatedMethodInEnsemble(clazz, KnowledgeExchange.class);
		Exchange exchange = factory.createExchange();
		exchange.setMethod(m);
		exchange.getParameters().addAll(createParameters(m, false));
		return exchange;
	}

	ComponentProcess createComponentProcess(ComponentInstance componentInstance, Method m) throws AnnotationParsingException, ParseException {
		ComponentProcess componentProcess = factory.createComponentProcess();
		try {
			componentProcess.setComponentInstance(componentInstance);
			componentProcess.setMethod(m);
			componentProcess.setName(m.getName());
			PeriodicTrigger periodicTrigger = createPeriodicTrigger(m);
			List<KnowledgeChangeTrigger> knowledgeChangeTriggers = createKnowledgeChangeTriggers(m, true);
			if (periodicTrigger == null) {
				if (knowledgeChangeTriggers.isEmpty()) {
					throw new AnnotationParsingException("No triggers were found.");
				}
			} else {
				componentProcess.getTriggers().add(periodicTrigger);
			}
			componentProcess.getTriggers().addAll(knowledgeChangeTriggers);
			componentProcess.getParameters().addAll(createParameters(m, true));			
		} catch (AnnotationParsingException e) {
			String msg = "Process: "+componentProcess.getName()+"->"+e.getMessage();
			throw new AnnotationParsingException(msg, e);
		}
		return componentProcess;
	}
	
	/**
	 * Extracts the period from a method or a class and returns it as an
	 * instance of {@link PeriodicTrigger}. If no {@link PeriodicScheduling}
	 * annotation is found, returns <code>null</code>.
	 * 
	 * @param o
	 *            either method or class (both Method and Class can hold a @
	 *            {@link PeriodicTrigger})
	 * @return periodic trigger or <code>null</code> if no period is associated
	 *         with the class/method.
	 * @throws AnnotationParsingException
	 */
	PeriodicTrigger createPeriodicTrigger(Object o)
			throws AnnotationParsingException {
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
				PeriodicTrigger periodicTrigger = factory.createPeriodicTrigger();
				periodicTrigger.setPeriod(((PeriodicScheduling) a).value());
				return periodicTrigger;
			}
		}
		return null;
	}

	List<KnowledgeChangeTrigger> createKnowledgeChangeTriggers(Method method, boolean inComponentProcess) 
			throws AnnotationParsingException, ParseException {
		List<KnowledgeChangeTrigger> knowledgeChangeTriggers = new ArrayList<>();
		Type[] parameterTypes = method.getParameterTypes();
		Annotation[][] allAnnotations = method.getParameterAnnotations();
		for (int i = 0; i < parameterTypes.length; i++) {
			TriggerOnChange t = getAnnotation(allAnnotations[i], TriggerOnChange.class);
			if (t != null) {
				Annotation directionAnnotation = getDirectionAnnotation(i, allAnnotations[i]);
				String path = (String) getAnnotationValue(directionAnnotation);
				KnowledgeChangeTrigger trigger = factory.createKnowledgeChangeTrigger();
				trigger.setKnowledgePath(createKnowledgePath(path, inComponentProcess));
				knowledgeChangeTriggers.add(trigger);
			}
		}
		return knowledgeChangeTriggers;
	}

	List<Parameter> createParameters(Method method, boolean inComponentProcess)
			throws AnnotationParsingException, ParseException {
		List<Parameter> parameters = new ArrayList<>();
		Type[] parameterTypes = method.getParameterTypes();
		Annotation[][] allAnnotations = method.getParameterAnnotations();
		for (int i = 0; i < parameterTypes.length; i++) {
			parameters.add(createParameter(i, allAnnotations[i], inComponentProcess));
		}
		return parameters;
	}
	
	/**
	 * Creates and returns a parameter for a method.
	 * 
	 * @param parameterIndex
	 *            order of the parameter in the method declaration - just for
	 *            exception printing.
	 * @param parameterAnnotations
	 *            list of annotations extracted from method signature
	 * @return the created Parameter object
	 * @throws AnnotationParsingException
	 * @throws ParseException
	 */
	Parameter createParameter(int parameterIndex, Annotation[] parameterAnnotations, boolean inComponentProcess)
			throws AnnotationParsingException, ParseException {
		Annotation directionAnnotation = getDirectionAnnotation(parameterIndex, parameterAnnotations);
		Parameter parameter = factory.createParameter();
		parameter.setDirection(parameterAnnotationsToParameterDirections.get(directionAnnotation.annotationType()));
		String path = (String) getAnnotationValue(directionAnnotation);
		parameter.setKnowledgePath(createKnowledgePath(path,inComponentProcess));
		return parameter;
	}
	
	KnowledgePath createKnowledgePath(String path, boolean inComponentProcess) throws ParseException, AnnotationParsingException {
		PNode pNode = PathParser.parse(path);
		return createKnowledgePath(pNode, inComponentProcess);
	}
	
	KnowledgePath createKnowledgePath(PNode pNode, boolean inComponentProcess) throws AnnotationParsingException {
		KnowledgePath knowledgePath = factory.createKnowledgePath();
		do {			
			Object nValue = pNode.value;
			if (nValue instanceof String) {
				PathNodeField pathNodeField = factory.createPathNodeField();
				pathNodeField.setName((String) nValue);
				knowledgePath.getNodes().add(pathNodeField);
			}
			if (nValue instanceof ComponentIdentifier) {
				if (inComponentProcess) {
					if (knowledgePath.getNodes().isEmpty()) {
						PathNodeComponentId idField = factory.createPathNodeComponentId();
						knowledgePath.getNodes().add(idField);
					} else {
						PathNodeField pathNodeField = factory.createPathNodeField();
						pathNodeField.setName(ComponentIdentifier.ID.toString());
						knowledgePath.getNodes().add(pathNodeField);	
					}
				} else {
					if (knowledgePath.getNodes().isEmpty()) {
						throw new AnnotationParsingException(
								"'"+ComponentIdentifier.ID.toString()+"' cannot be used in the beginning of a path in an ensemble definition.");
					} else {
						PathNode first = knowledgePath.getNodes().get(0);
						if (!((first instanceof PathNodeMember) || (first instanceof PathNodeCoordinator))) {
							PathNodeComponentId idField = factory.createPathNodeComponentId();
							knowledgePath.getNodes().add(idField);
						} else {
							PathNodeField pathNodeField = factory.createPathNodeField();
							pathNodeField.setName(ComponentIdentifier.ID.toString());
							knowledgePath.getNodes().add(pathNodeField);
						}
					}
					
				}
			}
			if (nValue instanceof EEnsembleParty) {
				EEnsembleParty ensembleKeyword = (EEnsembleParty) nValue;
				if (inComponentProcess) {
					throw new AnnotationParsingException(
							"Cannot use the '" + ensembleKeyword + "' in a component process.");
				} else {
					if (knowledgePath.getNodes().isEmpty()) {
						knowledgePath.getNodes().add(createMemberOrCoordinatorPathNode(ensembleKeyword));						
					} else { // not in the beginning, add it as PathNodeField
						PathNodeField pathNodeField = factory.createPathNodeField();
						pathNodeField.setName(ensembleKeyword.toString());
						knowledgePath.getNodes().add(pathNodeField);
					}
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

	PathNode createMemberOrCoordinatorPathNode(EEnsembleParty keyword)
			throws AnnotationParsingException {
		switch (keyword) {
			case COORDINATOR:
				return factory.createPathNodeCoordinator();
			case MEMBER:
				return factory.createPathNodeMember();
			default:
				throw new AnnotationParsingException(
						"Invalid identifier: 'coord' or 'member' keyword expected.");
		}
	}
	
	/**
	 * Returns list of methods in the class definition which are annotated as
	 * DEECo processes.
	 * 
	 * @param c
	 *            class definition being parsed
	 * @return list of methods
	 */
	List<Method> getMethodsMarkedAsProcesses(Class<?> c) {
		List<Method> result = new ArrayList<Method>();
		if (c != null) {
			Method[] methods = c.getDeclaredMethods();
			for (Method m : methods) {
				if (m.getAnnotation(Process.class) != null)
					result.add(m);
			}
		}
		return result;
	}
	
	Method getMethodMarkedAsMembership(Class<?> clazz,
			Class<Condition> class1) {
		return null;
	}
	
	Method getAnnotatedMethodInEnsemble(Class<?> clazz,
			Class<? extends Annotation> annotationClass)
			throws AnnotationParsingException {
		Method foundMethod = null;
		Method[] methods = clazz.getDeclaredMethods();
		for (Method m : methods) {
			if (m.getAnnotation(annotationClass) != null) {
				int modifier = m.getModifiers();
				if (Modifier.isPublic(modifier) && Modifier.isStatic(modifier)){
					if (foundMethod == null) {
						foundMethod = m;
					} else {
						throw new AnnotationParsingException(
								"More than one instance of @"
										+ annotationClass.getSimpleName()
										+ " annotation was found");
					}
				} else {
					throw new AnnotationParsingException("Method "+ m.getName() + 
							" annotated as @" + annotationClass.getSimpleName()
							+ " should be public and static.");
				}
			}
		}
		if (foundMethod == null) {
			throw new AnnotationParsingException("No @"
					+ annotationClass.getSimpleName()
					+ " annotation was found");
		}
		return foundMethod;
	}

	/**
	 * Retrieves the first instance of a direction annotation, i.e. one of
	 * {Inout, In, Out}. If more than one / none direction annotation is found,
	 * it throws an exception.
	 * 
	 * @param parameterIndex
	 *            order of the parameter in the method declaration - just for
	 *            exception printing.
	 * @param annotations
	 *            list of annotations to search into
	 * @return found annotation instance
	 * @throws AnnotationParsingException
	 */
	Annotation getDirectionAnnotation(int parameterIndex,
			Annotation[] annotations) throws AnnotationParsingException {
		Annotation foundAnnotation = null;
		for (Annotation a : annotations) {
			if (parameterAnnotationsToParameterDirections.containsKey(a.annotationType())) {
				if (foundAnnotation == null) {
					foundAnnotation = a;
				} else {
					throw new AnnotationParsingException("Parameter: "+ (parameterIndex+1) +". More than one direction annotation was found.");
				}
			}
		}
		if (foundAnnotation == null) {
			throw new AnnotationParsingException("Parameter: "+ (parameterIndex+1) +". No direction annotation was found.");
		}
		return foundAnnotation;
	}

	Object getAnnotationValue(Annotation annotation) {
		IValuedAnnotation valuedAnnotation = (IValuedAnnotation) AnnotationProxy
				.implement(IValuedAnnotation.class, annotation);
		return valuedAnnotation.value();
	}

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
	
	ChangeSet extractInitialKnowledge(Object knowledge) {
		ChangeSet changeSet = new ChangeSet();
		for (Field f : knowledge.getClass().getFields()) {
			KnowledgePath knowledgePath = factory.createKnowledgePath();
			PathNodeField pathNodeField = factory.createPathNodeField();
			pathNodeField.setName(new String(f.getName()));
			knowledgePath.getNodes().add(pathNodeField);
			try {
				changeSet.setValue(knowledgePath, f.get(knowledge));
			} catch (IllegalAccessException e) {
				continue;
			}
		}
		return changeSet;
	}

	boolean isComponentDefinition(Class<?> clazz) {
		return clazz != null && clazz.isAnnotationPresent(Component.class);
	}

	boolean isEnsembleDefinition(Class<?> clazz) {
		return clazz != null && clazz.isAnnotationPresent(Ensemble.class);
	}

}
