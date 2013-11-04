package cz.cuni.mff.d3s.deeco.annotations.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.javatuples.Pair;

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
import cz.cuni.mff.d3s.deeco.exceptions.AnnotationParsingException;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeChangeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterDirection;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SchedulingSpecification;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;
import cz.cuni.mff.d3s.deeco.path.grammar.PNode;
import cz.cuni.mff.d3s.deeco.path.grammar.ParseException;
import cz.cuni.mff.d3s.deeco.path.grammar.PathParser;

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

	public void process(Class<?> clazz) {
		if (isComponentDefinition(clazz)) {
			parseComponentClassAndUpdateRuntimeModel(clazz);
		} else if (isEnsembleDefinition(clazz)) {
			parseEnsembleClassAndUpdateRuntimeModel(clazz);
		} else {
			try {
				throw new AnnotationParsingException(
						"No @Component or @Ensemble annotation found");
			} catch (AnnotationParsingException e) {
				e.printStackTrace();
			}
		}
	}

	private void parseComponentClassAndUpdateRuntimeModel(Class<?> clazz) {
		ComponentInstance componentInstance = factory.createComponentInstance();
		componentInstance.setName(clazz.getCanonicalName());
		componentInstance.setKnowledgeManager(null); // FIXME
		componentInstance.setOtherKnowledgeManagersAccess(null); // FIXME

		List<Method> methods = getMethodsMarkedAsProcesses(clazz);
		ComponentProcess componentProcess;
		SchedulingSpecification schedulingSpecification;
		KnowledgeChangeTrigger trigger;
		Parameter param;
		Boolean hasTriggeredAnnotation;
		try {
			for (Method m : methods) {
				componentProcess = factory.createComponentProcess();
				componentInstance.getComponentProcesses().add(componentProcess);
				componentProcess.setComponentInstance(componentInstance);
				componentProcess.setMethod(m);
				componentProcess.setName(m.getName());

				schedulingSpecification = factory
						.createSchedulingSpecification();
				componentProcess
						.setSchedulingSpecification(schedulingSpecification);
				schedulingSpecification.setPeriod(getPeriodInMilliSeconds(m));

				for (Pair<Parameter, Boolean> p : getParameters(m)) {
					param = p.getValue0();
					hasTriggeredAnnotation = p.getValue1();
					componentProcess.getParameters().add(param);
					if (hasTriggeredAnnotation) {
						trigger = factory.createKnowledgeChangeTrigger();
						trigger.setKnowledgePath(EcoreUtil.copy(param
								.getKnowledgePath()));
						schedulingSpecification.getTriggers().add(trigger);
					}
				}
			}
			model.getComponentInstances().add(componentInstance);
		} catch (AnnotationParsingException | ParseException e) {
			e.printStackTrace();
		}
	}

	private void parseEnsembleClassAndUpdateRuntimeModel(Class<?> clazz) {
		// TODO:IG
	}

	/**
	 * Returns list of methods in the class definition which are annotated as
	 * DEECo processes.
	 * 
	 * @param c
	 *            class definition being parsed
	 * @return list of methods
	 */
	private List<Method> getMethodsMarkedAsProcesses(Class<?> c) {
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
	 * Extracts the period from a method. If no {@link PeriodicScheduling}
	 * annotation is found, returns the default period (1000 msec).
	 * 
	 * @param m
	 *            method to be processed
	 * @return period in msec
	 */
	private long getPeriodInMilliSeconds(Method m) {
		for (Annotation a : m.getAnnotations()) {
			if (a instanceof PeriodicScheduling) {
				return ((PeriodicScheduling) a).value();
			}
		}
		return 1000;
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
	private List<Pair<Parameter, Boolean>> getParameters(Method method)
			throws AnnotationParsingException, ParseException {
		List<Pair<Parameter, Boolean>> parameters = new ArrayList<Pair<Parameter, Boolean>>();
		Type[] parameterTypes = method.getParameterTypes();
		Annotation[][] allAnnotations = method.getParameterAnnotations();
		Pair<Annotation, Class<?>> annotationPair;
		Parameter param;
		KnowledgePath knowledgePath;
		PathNodeField pathNodeField;
		for (int i = 0; i < parameterTypes.length; i++) {
			annotationPair = getFirstDirectionAnnotationFromParameter(allAnnotations[i]);
			if (annotationPair == null) {
				throw new AnnotationParsingException(
						"No direction annotation was found for a parameter in method: "
								+ method);
			}
			param = factory.createParameter();
			param.setDirection(parameterAnnotationsToParameterDirections
					.get(annotationPair.getValue1()));
			knowledgePath = factory.createKnowledgePath();
			param.setKnowledgePath(knowledgePath);
			String path = (String) getAnnotationValue(annotationPair
					.getValue0());
			PNode pNode = PathParser.parse(path);
			do {
				pathNodeField = factory.createPathNodeField();
				pathNodeField.setName((String) pNode.value);
				knowledgePath.getNodes().add(pathNodeField);
				pNode = pNode.next;
			} while (!(pNode == null));
			parameters.add(new Pair<Parameter, Boolean>(param,
					hasTriggeredAnnotation(allAnnotations[i],
							TriggerOnChange.class)));
		}
		return parameters;
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
	private Pair<Annotation, Class<?>> getFirstDirectionAnnotationFromParameter(
			Annotation[] annotations) throws AnnotationParsingException {
		Set<Class<? extends Annotation>> directionAnnotationClasses = parameterAnnotationsToParameterDirections
				.keySet();
		for (Class<?> directionAnnotationClass : directionAnnotationClasses) {
			for (Annotation a : annotations) {
				if (directionAnnotationClass.isInstance(a)) {
					return new Pair<Annotation, Class<?>>(a,
							directionAnnotationClass);
				}
			}
		}
		return null;
	}

	private Object getAnnotationValue(Annotation annotation) {
		IValuedAnnotation valuedAnnotation = (IValuedAnnotation) AnnotationProxy
				.implement(IValuedAnnotation.class, annotation);
		return valuedAnnotation.value();
	}
	
	private boolean hasTriggeredAnnotation(Annotation[] annotations,
			Class<?> triggeringAnnotationClass) {
		for (Annotation a : annotations) {
			if (triggeringAnnotationClass.isInstance(a)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isComponentDefinition(Class<?> clazz) {
		return clazz != null && clazz.isAnnotationPresent(Component.class);
	}

	private boolean isEnsembleDefinition(Class<?> clazz) {
		return clazz != null && clazz.isAnnotationPresent(Ensemble.class);
	}

}
