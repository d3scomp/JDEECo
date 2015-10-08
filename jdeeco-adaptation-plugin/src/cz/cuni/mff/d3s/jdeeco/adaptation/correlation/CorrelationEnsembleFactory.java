package cz.cuni.mff.d3s.jdeeco.adaptation.correlation;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.ParameterAnnotationsAttribute;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.LongMemberValue;
import javassist.bytecode.annotation.StringMemberValue;
import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;

/**
 * This class provides method for creating an ensemble definition at runtime.
 * Once the ensemble definition is created it i stored, so the next time it's
 * requested it is returned without being constructed again.
 *
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
@Ensemble
@PeriodicScheduling(period = 1000)
public class CorrelationEnsembleFactory {

	/**
	 * Once a new class is created it is stored here ready for further retrieval.
	 */
	private static Map<String, Class<?>> bufferedClasses = new HashMap<>();

	/**
	 * The period at which the ensemble is planned.
	 */
	private static final long schedulingPeriod = 250;

	/**
	 * Provides the ensemble definition for the knowledge fields of given name.
	 * The correlation of knowledge denoted by correlationSubject depends on the
	 * data filtering by the knowledge denoted by correlationFilter.
	 * @param correlationFilter Name of the knowledge field used for data filtering when calculating correlation.
	 * 		In our example this refers to "position".
	 * @param correlationSubject Name of the knowledge field used for the calculation of data correlation after
	 * 		the values has been filtered. In our example this refers to "temperature".
	 * @return A class that defines an ensemble for data exchange given by the specified knowledge fields.
	 * @throws Exception If there is a problem creating the ensemble class.
	 */
	public static Class<?> getEnsembleDefinition(String correlationFilter, String correlationSubject) throws Exception {
		String className = composeClassName(correlationFilter, correlationSubject);
		Class<?> requestedClass;
		if(!bufferedClasses.containsKey(className)){
			requestedClass = createEnsembleDefinition(correlationFilter, correlationSubject);
			bufferedClasses.put(className, requestedClass);
		}
		else {
			requestedClass = bufferedClasses.get(className);
		}

		return requestedClass;
	}

	@SuppressWarnings("rawtypes")
	public static void setEnsembleMembershipBoundary(String correlationFilter, String correlationSubject, double boundary) throws Exception {
		Class<?> requestedClass = CorrelationEnsembleFactory.getEnsembleDefinition(correlationFilter, correlationSubject);
		String className = requestedClass.getName();

		ClassPool classPool = ClassPool.getDefault();
		CtClass ensembleClass = classPool.getCtClass(className);
		ensembleClass.defrost();
		ClassFile classFile = ensembleClass.getClassFile();
		ConstPool constPool = classFile.getConstPool();

		// Remove the existing membership method
		MethodInfo oldMembershipMethod = classFile.getMethod("membership");
		if(oldMembershipMethod != null){
			List ensembleMethods = classFile.getMethods();
			ensembleMethods.remove(oldMembershipMethod);
		}

		// TODO: infer the types dynamically
		String methodBody = String.format(Locale.ENGLISH,
				"public static boolean membership(\n"
				+ "		cz.cuni.mff.d3s.jdeeco.adaptation.correlation.metadata.MetadataWrapper member%1$s,\n"
				+ "		cz.cuni.mff.d3s.jdeeco.adaptation.correlation.metadata.MetadataWrapper member%2$s,\n"
				+ "		cz.cuni.mff.d3s.jdeeco.adaptation.correlation.metadata.MetadataWrapper coord%1$s,\n"
				+ "		cz.cuni.mff.d3s.jdeeco.adaptation.correlation.metadata.MetadataWrapper coord%2$s) {\n"
				+ " final double %1$s_bound = %3$f;\n"
//				+ "	System.out.println(\"Membership for %1$s -> %2$s with %1$s_bound = %3$.1f\");\n"
				+ " return (!member%2$s.isOperational()\n"
				+ "		&& coord%2$s.isOperational()\n"
				+ "		&& cz.cuni.mff.d3s.jdeeco.adaptation.correlation.metadata.KnowledgeMetadataHolder.distance(\n"
				+ "			\"%1$s\", member%1$s.getValue(), coord%1$s.getValue()) < %1$s_bound);}",
					correlationFilter, correlationSubject, boundary);

//		System.out.println(methodBody);

		// Create new Membership method
		CtMethod membershipMethod = CtNewMethod.make(methodBody, ensembleClass);
		// Membership annotation for the membership method
		Annotation membershipAnnotation = new Annotation("cz.cuni.mff.d3s.deeco.annotations.Membership", constPool);
		AnnotationsAttribute membershipAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
		membershipAttribute.addAnnotation(membershipAnnotation);
		membershipMethod.getMethodInfo().addAttribute(membershipAttribute);
		// Membership parameters annotations
		ParameterAnnotationsAttribute membershipParamAnnotations = new ParameterAnnotationsAttribute(constPool,
				ParameterAnnotationsAttribute.visibleTag);
		Annotation[][] membershipParamAnnotationsInfo = new Annotation[4][1];
		membershipParamAnnotationsInfo[0][0] = new Annotation("cz.cuni.mff.d3s.deeco.annotations.In", constPool);
		membershipParamAnnotationsInfo[0][0].addMemberValue("value", new StringMemberValue(
				String.format("member.%s", correlationFilter), constPool));
		membershipParamAnnotationsInfo[1][0] = new Annotation("cz.cuni.mff.d3s.deeco.annotations.In", constPool);
		membershipParamAnnotationsInfo[1][0].addMemberValue("value", new StringMemberValue(
				String.format("member.%s", correlationSubject), constPool));
		membershipParamAnnotationsInfo[2][0] = new Annotation("cz.cuni.mff.d3s.deeco.annotations.In", constPool);
		membershipParamAnnotationsInfo[2][0].addMemberValue("value", new StringMemberValue(
				String.format("coord.%s", correlationFilter), constPool));
		membershipParamAnnotationsInfo[3][0] = new Annotation("cz.cuni.mff.d3s.deeco.annotations.In", constPool);
		membershipParamAnnotationsInfo[3][0].addMemberValue("value", new StringMemberValue(
				String.format("coord.%s", correlationSubject), constPool));
		membershipParamAnnotations.setAnnotations(membershipParamAnnotationsInfo);
		membershipMethod.getMethodInfo().addAttribute(membershipParamAnnotations);

		// Add the method into the ensemble class
		ensembleClass.addMethod(membershipMethod);

		// Buffer the modified class
		if(bufferedClasses.containsKey(className)){
			bufferedClasses.remove(className);
		}
		ensembleClass.writeFile();
		CorrelationClassLoader loader = new CorrelationClassLoader(CorrelationClassLoader.class.getClassLoader());
		Class loadedClass = loader.loadClass(className);
		bufferedClasses.put(className, loadedClass);
	}

	/**
	 * Compose the name of the ensemble class for the defined knowledge fields.
	 * @param correlationFilter Name of the knowledge field used for data filtering when calculating correlation.
	 * 		In our example this refers to "position".
	 * @param correlationSubject Name of the knowledge field used for the calculation of data correlation after
	 * 		the values has been filtered. In our example this refers to "temperature".
	 * @return The name of the ensemble class for the defined knowledge fields.
	 */
	public static String composeClassName(String correlationFilter, String correlationSubject){
		return String.format("Correlation_%s2%s", correlationFilter, correlationSubject);
	}

	/**
	 * Create the ensemble class definition at runtime with the help of
	 * <a href="http://www.csg.ci.i.u-tokyo.ac.jp/~chiba/javassist/">javassist</a>.
	 * @param correlationFilter Name of the knowledge field used for data filtering when calculating correlation.
	 * 		In our example this refers to "position".
	 * @param correlationSubject Name of the knowledge field used for the calculation of data correlation after
	 * 		the values has been filtered. In our example this refers to "temperature".
	 * @return The ensemble class definition for the knowledge fields of given names.
	 * @throws Exception If there is a problem creating the ensemble class.
	 */
	@SuppressWarnings("rawtypes")
	private static Class createEnsembleDefinition(String correlationFilter, String correlationSubject) throws Exception {

		// Create the class defining the ensemble
		ClassPool classPool = ClassPool.getDefault();
		CtClass ensembleClass = classPool.makeClass(composeClassName(correlationFilter, correlationSubject));

		ClassFile classFile = ensembleClass.getClassFile();
		ConstPool constPool = classFile.getConstPool();

		// Ensemble annotation for the class
		Annotation ensembleAnnotation = new Annotation("cz.cuni.mff.d3s.deeco.annotations.Ensemble", constPool);
		AnnotationsAttribute ensembleAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
		ensembleAttribute.addAnnotation(ensembleAnnotation);
		// Scheduling annotation for the class
		Annotation schedulingAnnotation = new Annotation("cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling", constPool);
		schedulingAnnotation.addMemberValue("period", new LongMemberValue(schedulingPeriod, constPool));
		ensembleAttribute.addAnnotation(schedulingAnnotation);
		// Add the class annotations
		classFile.addAttribute(ensembleAttribute);

		// Membership method
		CtMethod membershipMethod = CtNewMethod.make(String.format(
				"public static boolean membership("
				+ "		cz.cuni.mff.d3s.jdeeco.adaptation.correlation.metadata.MetadataWrapper member%1$s,"
				+ "		cz.cuni.mff.d3s.jdeeco.adaptation.correlation.metadata.MetadataWrapper member%2$s,"
				+ "		cz.cuni.mff.d3s.jdeeco.adaptation.correlation.metadata.MetadataWrapper coord%1$s,"
				+ "		cz.cuni.mff.d3s.jdeeco.adaptation.correlation.metadata.MetadataWrapper coord%2$s) {"
				+ " return (!member%2$s.isOperational()"
				+ "		&& coord%2$s.isOperational()"
				+ "		&& cz.cuni.mff.d3s.jdeeco.adaptation.correlation.metadata.KnowledgeMetadataHolder.classifyDistance("
				+ "			\"%1$s\", member%1$s.getValue(), coord%1$s.getValue())"
				+ "			 == cz.cuni.mff.d3s.jdeeco.adaptation.correlation.metadata.CorrelationLevel.DistanceClass.Close);}",
					correlationFilter, correlationSubject),
				ensembleClass);
		// Membership annotation for the membership method
		Annotation membershipAnnotation = new Annotation("cz.cuni.mff.d3s.deeco.annotations.Membership", constPool);
		AnnotationsAttribute membershipAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
		membershipAttribute.addAnnotation(membershipAnnotation);
		membershipMethod.getMethodInfo().addAttribute(membershipAttribute);
		// Membership parameters annotations
		ParameterAnnotationsAttribute membershipParamAnnotations = new ParameterAnnotationsAttribute(constPool,
				ParameterAnnotationsAttribute.visibleTag);
		Annotation[][] membershipParamAnnotationsInfo = new Annotation[4][1];
		membershipParamAnnotationsInfo[0][0] = new Annotation("cz.cuni.mff.d3s.deeco.annotations.In", constPool);
		membershipParamAnnotationsInfo[0][0].addMemberValue("value", new StringMemberValue(
				String.format("member.%s", correlationFilter), constPool));
		membershipParamAnnotationsInfo[1][0] = new Annotation("cz.cuni.mff.d3s.deeco.annotations.In", constPool);
		membershipParamAnnotationsInfo[1][0].addMemberValue("value", new StringMemberValue(
				String.format("member.%s", correlationSubject), constPool));
		membershipParamAnnotationsInfo[2][0] = new Annotation("cz.cuni.mff.d3s.deeco.annotations.In", constPool);
		membershipParamAnnotationsInfo[2][0].addMemberValue("value", new StringMemberValue(
				String.format("coord.%s", correlationFilter), constPool));
		membershipParamAnnotationsInfo[3][0] = new Annotation("cz.cuni.mff.d3s.deeco.annotations.In", constPool);
		membershipParamAnnotationsInfo[3][0].addMemberValue("value", new StringMemberValue(
				String.format("coord.%s", correlationSubject), constPool));
		membershipParamAnnotations.setAnnotations(membershipParamAnnotationsInfo);
		membershipMethod.getMethodInfo().addAttribute(membershipParamAnnotations);

		// Add the method into the ensemble class
		ensembleClass.addMethod(membershipMethod);

		// Map method
		CtMethod mapMethod = CtNewMethod.make(String.format(
				"public static void map("
				+ "		String memberId,"
				+ "		String coordId,"
				+ "		cz.cuni.mff.d3s.jdeeco.adaptation.correlation.metadata.MetadataWrapper coord%1$s,"
				+ "		cz.cuni.mff.d3s.deeco.task.ParamHolder member%1$s) {"
				+ " System.out.println(\"Knowledge injection \" + coordId + \" -> \" + memberId + \" %1$s \" + coord%1$s.getValue() + \" at \" +  coord%1$s.getTimestamp() + \" based on \" + \"%2$s\");"
				+ "	member%1$s.value = coord%1$s; "
				+ " ((cz.cuni.mff.d3s.jdeeco.adaptation.correlation.metadata.MetadataWrapper) member%1$s.value).malfunction();}",
					correlationSubject, correlationFilter),
				ensembleClass);
		// KnowledgeExchange annotation for the map method
		Annotation mapAnnotation = new Annotation("cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange", constPool);
		AnnotationsAttribute mapAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
		mapAttribute.addAnnotation(mapAnnotation);
		mapMethod.getMethodInfo().addAttribute(mapAttribute);
		// Map parameters annotations
		ParameterAnnotationsAttribute mapParamAnnotations = new ParameterAnnotationsAttribute(constPool,
				ParameterAnnotationsAttribute.visibleTag);
		Annotation[][] mapParamAnnotationsInfo = new Annotation[4][1];
		mapParamAnnotationsInfo[0][0] = new Annotation("cz.cuni.mff.d3s.deeco.annotations.In", constPool);
		mapParamAnnotationsInfo[0][0].addMemberValue("value", new StringMemberValue("member.id", constPool));
		mapParamAnnotationsInfo[1][0] = new Annotation("cz.cuni.mff.d3s.deeco.annotations.In", constPool);
		mapParamAnnotationsInfo[1][0].addMemberValue("value", new StringMemberValue("coord.id", constPool));
		mapParamAnnotationsInfo[2][0] = new Annotation("cz.cuni.mff.d3s.deeco.annotations.In", constPool);
		mapParamAnnotationsInfo[2][0].addMemberValue("value", new StringMemberValue(
				String.format("coord.%s", correlationSubject), constPool));
		mapParamAnnotationsInfo[3][0] = new Annotation("cz.cuni.mff.d3s.deeco.annotations.Out", constPool);
		mapParamAnnotationsInfo[3][0].addMemberValue("value", new StringMemberValue(
				String.format("member.%s", correlationSubject), constPool));
		mapParamAnnotations.setAnnotations(mapParamAnnotationsInfo);
		mapMethod.getMethodInfo().addAttribute(mapParamAnnotations);

		// Add the method into the ensemble class
		ensembleClass.addMethod(mapMethod);

		ensembleClass.writeFile();
		CorrelationClassLoader loader = new CorrelationClassLoader(CorrelationClassLoader.class.getClassLoader());
		return loader.loadClass(ensembleClass.getName());
	}

}