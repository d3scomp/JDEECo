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
import cz.cuni.mff.d3s.deeco.exceptions.AnnotationParsingException;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
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

public class AnnotationProcessor {

	private static final RuntimeMetadataFactory factory = RuntimeMetadataFactory.eINSTANCE;
	private static final Map<Class<? extends Annotation>, ParameterDirection> parameterAnnotationsToParameterDirections = Collections
			.unmodifiableMap(new HashMap<Class<? extends Annotation>, ParameterDirection>() {
				{
					put(InOut.class, ParameterDirection.INOUT);
					put(In.class, ParameterDirection.IN);
					put(Out.class, ParameterDirection.OUT);
				}
			});
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
		componentInstance.setKnowledgeManager(null);
		componentInstance.setOtherKnowledgeManagersAccess(null);

		List<Method> methods = getMethodsMarkedAsProcesses(clazz);
		try {
			for (Method m : methods) {
				ComponentProcess componentProcess = factory
						.createComponentProcess();
				componentInstance.getComponentProcesses().add(componentProcess);
				componentProcess.setComponentInstance(componentInstance);
				componentProcess.setMethod(m);
				componentProcess.setName(m.getName());

				for (Parameter p : getParameters(m)) {
					componentProcess.getParameters().add(p);
				}

				SchedulingSpecification schedulingSpecification = factory
						.createSchedulingSpecification();
				componentProcess
						.setSchedulingSpecification(schedulingSpecification);
				schedulingSpecification.setPeriod(getPeriodInMilliSeconds(m));

				/*
				 * if (true) { KnowledgeChangeTrigger trigger = factory
				 * .createKnowledgeChangeTrigger();
				 * schedulingSpecification.getTriggers().add(trigger); //
				 * trigger.setKnowledgePath(value); }
				 */
			} // end: for every Method
			model.getComponentInstances().add(componentInstance);
		} catch (AnnotationParsingException | ParseException e) {
			e.printStackTrace();
		}
	}

	private void parseEnsembleClassAndUpdateRuntimeModel(Class clazz) {
		// TODO(IG)
	}

	private boolean isComponentDefinition(Class<?> clazz) {
		return clazz != null && clazz.isAnnotationPresent(Component.class);
	}

	private boolean isEnsembleDefinition(Class<?> clazz) {
		return clazz != null && clazz.isAnnotationPresent(Ensemble.class);
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
	 * TODO(IG)
	 */
	private long getPeriodInMilliSeconds(Method m) {
		for (Annotation a : m.getAnnotations()) {
			if (a instanceof PeriodicScheduling) {
				return ((PeriodicScheduling) a).value();
			}
		}
		return 1000; // default period
	}

	/*
	 * Constructs and returns a list of Parameters (eObjects)
	 */
	private List<Parameter> getParameters(Method method)
			throws AnnotationParsingException, ParseException {
		List<Parameter> parameters = new ArrayList<Parameter>();
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

			parameters.add(param);
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
		Set<Class<? extends Annotation>> directionAnnotationClasses = parameterAnnotationsToParameterDirections.keySet();
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

}
