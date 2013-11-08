package cz.cuni.mff.d3s.deeco.annotations.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
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
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.TriggerOnChange;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.EEnsembleParty;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.PNode;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.ParseException;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.PathParser;
import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.CloningKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerContainer;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerViewImpl;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagersView;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Invocable;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeChangeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterDirection;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNode;
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
 * model of the application (which is provided through the constructor).
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
	private static final RuntimeMetadataFactory factory = RuntimeMetadataFactory.eINSTANCE;

	private RuntimeMetadata model;

	public AnnotationProcessor(RuntimeMetadata model) {
		this.model = model;
	}

	public void process(Object obj) {
		Class<?> clazz = obj.getClass();
		try {
			if (isComponentDefinition(clazz)) {
				model.getComponentInstances().add(createComponentInstance(obj));
			} else if (isEnsembleDefinition(clazz)) {
				model.getEnsembleDefinitions().add(createEnsembleDefinition(obj));
			} else {
				throw new AnnotationParsingException("No @Component or @Ensemble annotation found.");
			}
		}catch (AnnotationParsingException e) {
			Log.e(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	ComponentInstance createComponentInstance(Object obj) throws AnnotationParsingException {
		Class<?> clazz = obj.getClass();
		ComponentInstance componentInstance = factory.createComponentInstance();
		componentInstance.setName(clazz.getCanonicalName());
//		KnowledgeManagerContainer container = new CloningKnowledgeManagerView();
//		KnowledgeManager km = container.createLocal();
//		km.update(processInitialKnowledge(obj));
//		componentInstance.setKnowledgeManager(km);	
//		KnowledgeManagersView view = new KnowledgeManagerViewImpl(km, container);
//		componentInstance.setOtherKnowledgeManagersAccess(view);
		List<Method> methods = getMethodsMarkedAsProcesses(clazz);
		try {
			for (Method m : methods) {
				int modifier = m.getModifiers();
				if (Modifier.isPublic(modifier) && Modifier.isStatic(modifier)) {
					componentInstance.getComponentProcesses().add(
							createComponentProcess(componentInstance, m));
				}
			}
		} catch (AnnotationParsingException e) {
			String msg = "Component: "+componentInstance.getName()+"->"+e.getMessage();
			throw new AnnotationParsingException(msg, e);
		}
		return componentInstance;
	}
	
	EnsembleDefinition createEnsembleDefinition(Object obj) {
		// TODO:IG
		return null;
	}

	ComponentProcess createComponentProcess(
			ComponentInstance componentInstance, Method m) throws AnnotationParsingException {
		ComponentProcess componentProcess;
		componentProcess = factory.createComponentProcess();
		try {
			componentProcess.setComponentInstance(componentInstance);
			componentProcess.setMethod(m);
			componentProcess.setName(m.getName());
			PeriodicTrigger periodicTrigger = createPeriodicTrigger(m);
			if (periodicTrigger!=null) {
				componentProcess.getTriggers().add(periodicTrigger);	
			}
			componentProcess.getTriggers().addAll(createKnowledgeChangeTriggers(m));
			componentProcess.getParameters().addAll(createParameters(m));			
		} catch (AnnotationParsingException | ParseException e) {
			String msg = "Process: "+componentProcess.getName()+"->"+e.getMessage();
			throw new AnnotationParsingException(msg, e);
		}
		return componentProcess;
	}

	/**
	 * Extracts the period from a method and returns it as an instance of
	 * {@link PeriodicTrigger}. If no {@link PeriodicScheduling} annotation is
	 * found, returns <code>null</code>.
	 * 
	 * @param m
	 *            method to be processed
	 * @return periodic trigger or <code>null</code> if no period is associated
	 *         with the method.
	 * @throws AnnotationParsingException
	 */
	PeriodicTrigger createPeriodicTrigger(Method m)
			throws AnnotationParsingException {
		for (Annotation a : m.getAnnotations()) {
			if (a instanceof PeriodicScheduling) {
				PeriodicTrigger periodicTrigger = factory
						.createPeriodicTrigger();
				periodicTrigger.setPeriod(((PeriodicScheduling) a).value());
				return periodicTrigger;
			}
		}
		return null;
	}

	List<KnowledgeChangeTrigger> createKnowledgeChangeTriggers(Method method) throws AnnotationParsingException, ParseException {
		List<KnowledgeChangeTrigger> knowledgeChangeTriggers = new ArrayList<>();
		Type[] parameterTypes = method.getParameterTypes();
		Annotation[][] allAnnotations = method.getParameterAnnotations();
		for (int i = 0; i < parameterTypes.length; i++) {
			TriggerOnChange t = getAnnotation(allAnnotations[i], TriggerOnChange.class);
			if (t != null) {
				Annotation directionAnnotation = getDirectionAnnotation(i, allAnnotations[i]);
				String path = (String) getAnnotationValue(directionAnnotation);
				KnowledgeChangeTrigger trigger = factory.createKnowledgeChangeTrigger();
				trigger.setKnowledgePath(createKnowledgePath(path));
				knowledgeChangeTriggers.add(trigger);
			}
		}
		return knowledgeChangeTriggers;
	}

	List<Parameter> createParameters(Method method)
			throws AnnotationParsingException, ParseException {
		List<Parameter> parameters = new ArrayList<>();
		Type[] parameterTypes = method.getParameterTypes();
		Annotation[][] allAnnotations = method.getParameterAnnotations();
		for (int i = 0; i < parameterTypes.length; i++) {
			parameters.add(createParameter(i,parameterTypes[i], allAnnotations[i]));
		}
		return parameters;
	}

	/**
	 * Extracts the Parameters from a method, paired with a flag signaling
	 * whether they have any triggered annotation associated.
	 * 
	 * @param method
	 *            method to be processed
	 * @return list of Tuples of <eObject:Parameter, flag:Boolean>
	 * @throws AnnotationParsingException
	 * @throws ParseException
	 */
	Parameter createParameter(int parameterIndex, Type parameterType,
			Annotation[] parameterAnnotations)
			throws AnnotationParsingException, ParseException {
		Annotation directionAnnotation = getDirectionAnnotation(
				parameterIndex, parameterAnnotations);
		Parameter parameter = factory.createParameter();
		parameter.setDirection(parameterAnnotationsToParameterDirections
				.get(directionAnnotation.annotationType()));
		String path = (String) getAnnotationValue(directionAnnotation);
		parameter.setKnowledgePath(createKnowledgePath(path));
		return parameter;
	}
	
	KnowledgePath createKnowledgePath(String path) throws ParseException, AnnotationParsingException {
		PNode pNode = PathParser.parse(path);
		return createKnowledgePath(pNode);
	}
	
	KnowledgePath createKnowledgePath(PNode pNode) throws AnnotationParsingException {
		KnowledgePath knowledgePath = factory.createKnowledgePath();
		do {			
			Object nValue = pNode.value;
			if (nValue instanceof String) {
				PathNodeField pathNodeField = factory.createPathNodeField();
				pathNodeField.setName((String) nValue);
				knowledgePath.getNodes().add(pathNodeField);
			}
			if (nValue instanceof EEnsembleParty) {
				EEnsembleParty ensembleKeyword = (EEnsembleParty) nValue;				
				knowledgePath.getNodes().add(createMemberOrCoordinatorPathNode(ensembleKeyword));
			}
			if (nValue instanceof PNode) {
				PathNodeMapKey pathNodeMapKey = factory.createPathNodeMapKey();
				pathNodeMapKey.setKeyPath(createKnowledgePath((PNode) nValue));
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
			Method[] methods = c.getMethods();
			for (Method m : methods) {
				if (m.getAnnotation(Process.class) != null)
					result.add(m);
			}
		}
		return result;
	}

	/**
	 * Retrieves the first instance of a direction annotation, i.e. one of
	 * {Inout, In, Out} (*in this order*), in an array of annotations for a
	 * parameter, paired with its annotation class
	 * 
	 * @param annotations
	 *            array of annotation instances where the search is performed
	 * @return found annotation instance or null if the search fails
	 */
	Annotation getDirectionAnnotation(int parameterIndex,
			Annotation[] annotations) throws AnnotationParsingException {
		Annotation foundAnnotation = null;
		for (Annotation a : annotations) {
			if (parameterAnnotationsToParameterDirections.containsKey(a.annotationType())) {
				if (foundAnnotation == null) {
					foundAnnotation = a;
				} else {
					throw new AnnotationParsingException("Parameter: "+ parameterIndex +". More than one direction annotation was found.");
				}
			}
		}
		if (foundAnnotation == null) {
			throw new AnnotationParsingException("Parameter: "+ parameterIndex +". More than one direction annotation was found.");
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
	
	ChangeSet processInitialKnowledge(Object knowledge) {
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
