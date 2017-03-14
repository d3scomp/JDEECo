package cz.cuni.mff.d3s.jdeeco.adaptation.correlation;

import java.util.List;
import java.util.Locale;

import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.metaadaptation.correlation.CorrelationLevel;
import cz.cuni.mff.d3s.metaadaptation.correlation.CorrelationMetadataWrapper;
import cz.cuni.mff.d3s.metaadaptation.correlation.EnsembleFactoryHelper;
import cz.cuni.mff.d3s.metaadaptation.correlation.KnowledgeMetadataHolder;
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
import javassist.bytecode.annotation.BooleanMemberValue;
import javassist.bytecode.annotation.LongMemberValue;
import javassist.bytecode.annotation.StringMemberValue;

/**
 * This class provides method for creating an ensemble definition at runtime.
 * Once the ensemble definition is created it i stored, so the next time it's
 * requested it is returned without being constructed again.
 *
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public class CorrelationEnsembleFactory implements cz.cuni.mff.d3s.metaadaptation.correlation.EnsembleFactory {

	/**
	 * The output directory for generated classes.
	 */
	static final String CLASS_DIRECTORY = "target/correlation-classes" + System.currentTimeMillis();
	
	/**
	 * The period at which the ensemble is planned.
	 */
	private static final long schedulingPeriod = 250;
	
	private boolean verbose = false;
	
	private final EnsembleFactoryHelper ensembleFactoryHelper;
	
	public CorrelationEnsembleFactory(){
		ensembleFactoryHelper = new EnsembleFactoryHelper();
		ensembleFactoryHelper.setEnsembleFactory(this);
	}
	
	public CorrelationEnsembleFactory withVerbosity(boolean verbosity){
		verbose = verbosity;
		ensembleFactoryHelper.withVerbosity(verbosity);
		return this;
	}

	@SuppressWarnings("rawtypes")
	public Class setEnsembleMembershipBoundary(String correlationFilter, String correlationSubject, double boundary) throws Exception {
		Class<?> requestedClass = ensembleFactoryHelper.getEnsembleDefinition(correlationFilter, correlationSubject);
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

		final String wrapperClass = CorrelationMetadataWrapper.class.getCanonicalName();
		final String holderClass = KnowledgeMetadataHolder.class.getCanonicalName();
		
		String methodBody = String.format(Locale.ENGLISH,
				"public static boolean membership(\n"
				+ "		" + wrapperClass + " member%1$s,\n"
				+ "		" + wrapperClass + " member%2$s,\n"
				+ "		" + wrapperClass + " coord%1$s,\n"
				+ "		" + wrapperClass + " coord%2$s) {\n"
				+ " final double %1$s_bound = %3$f;\n"
//				+ "	System.out.println(\"Membership for %1$s -> %2$s with %1$s_bound = %3$.1f\");\n"
				+ " return (!member%2$s.isOperational()\n"
				+ "		&& coord%2$s.isOperational()\n"
				+ "		&& " + holderClass + ".distance(\n"
				+ "			\"%1$s\", member%1$s.getValue(), coord%1$s.getValue()) < %1$s_bound);}",
					correlationFilter, correlationSubject, boundary);

		if(verbose){
			System.out.println(methodBody);
		}

		final String membershipClass = Membership.class.getCanonicalName();
		final String inClass = In.class.getCanonicalName();
		
		// Create new Membership method
		CtMethod membershipMethod = CtNewMethod.make(methodBody, ensembleClass);
		// Membership annotation for the membership method
		Annotation membershipAnnotation = new Annotation(membershipClass, constPool);
		AnnotationsAttribute membershipAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
		membershipAttribute.addAnnotation(membershipAnnotation);
		membershipMethod.getMethodInfo().addAttribute(membershipAttribute);
		// Membership parameters annotations
		ParameterAnnotationsAttribute membershipParamAnnotations = new ParameterAnnotationsAttribute(constPool,
				ParameterAnnotationsAttribute.visibleTag);
		Annotation[][] membershipParamAnnotationsInfo = new Annotation[4][1];
		membershipParamAnnotationsInfo[0][0] = new Annotation(inClass, constPool);
		membershipParamAnnotationsInfo[0][0].addMemberValue("value", new StringMemberValue(
				String.format("member.%s", correlationFilter), constPool));
		membershipParamAnnotationsInfo[1][0] = new Annotation(inClass, constPool);
		membershipParamAnnotationsInfo[1][0].addMemberValue("value", new StringMemberValue(
				String.format("member.%s", correlationSubject), constPool));
		membershipParamAnnotationsInfo[2][0] = new Annotation(inClass, constPool);
		membershipParamAnnotationsInfo[2][0].addMemberValue("value", new StringMemberValue(
				String.format("coord.%s", correlationFilter), constPool));
		membershipParamAnnotationsInfo[3][0] = new Annotation(inClass, constPool);
		membershipParamAnnotationsInfo[3][0].addMemberValue("value", new StringMemberValue(
				String.format("coord.%s", correlationSubject), constPool));
		membershipParamAnnotations.setAnnotations(membershipParamAnnotationsInfo);
		membershipMethod.getMethodInfo().addAttribute(membershipParamAnnotations);

		// Add the method into the ensemble class
		ensembleClass.addMethod(membershipMethod);

		ensembleClass.writeFile(CLASS_DIRECTORY);
		CorrelationClassLoader loader = new CorrelationClassLoader(CorrelationClassLoader.class.getClassLoader());
		Class loadedClass = loader.loadClass(className);
		return loadedClass;
	}

	/**
	 * Create the ensemble class definition at runtime with the help of
	 * <a href="http://www.csg.ci.i.u-tokyo.ac.jp/~chiba/javassist/">javassist</a>.
	 * @param correlationFilter Name of the knowledge field used for data filtering when calculating correlation.
	 * 		In our example this refers to "position".
	 * @param correlationSubject Name of the knowledge field used for the calculation of data correlation after
	 * 		the values has been filtered. In our example this refers to "temperature". 
	 * @param enableLogging Specifies whether the generated ensemble will be logged in the runtime log.
	 * @return The ensemble class definition for the knowledge fields of given names.
	 * @throws Exception If there is a problem creating the ensemble class.
	 */
	@SuppressWarnings("rawtypes")
	public Class createEnsembleDefinition(String correlationFilter, String correlationSubject) throws Exception {

		// Create the class defining the ensemble
		ClassPool classPool = ClassPool.getDefault();
		CtClass ensembleClass = classPool.makeClass(
				ensembleFactoryHelper.composeClassName(correlationFilter, correlationSubject));

		ClassFile classFile = ensembleClass.getClassFile();
		ConstPool constPool = classFile.getConstPool();

		final String ensembleClassName = Ensemble.class.getCanonicalName();		
		final String schedulingClassName = PeriodicScheduling.class.getCanonicalName();
		
		// Ensemble annotation for the class
		Annotation ensembleAnnotation = new Annotation(ensembleClassName, constPool);
		ensembleAnnotation.addMemberValue("enableLogging", new BooleanMemberValue (verbose, constPool));
				
		AnnotationsAttribute ensembleAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
		ensembleAttribute.addAnnotation(ensembleAnnotation);
		// Scheduling annotation for the class
		Annotation schedulingAnnotation = new Annotation(schedulingClassName, constPool);
		schedulingAnnotation.addMemberValue("period", new LongMemberValue(schedulingPeriod, constPool));
		ensembleAttribute.addAnnotation(schedulingAnnotation);
		// Add the class annotations
		classFile.addAttribute(ensembleAttribute);

		final String wrapperClass = CorrelationMetadataWrapper.class.getCanonicalName();
		final String holderClass = KnowledgeMetadataHolder.class.getCanonicalName();
		final String distanceClose = String.format("%s.%s",
				CorrelationLevel.DistanceClass.class.getCanonicalName(),
				CorrelationLevel.DistanceClass.Close.toString());
		
		// Membership method
		CtMethod membershipMethod = CtNewMethod.make(String.format(
				"public static boolean membership("
				+ "		" + wrapperClass + " member%1$s,"
				+ "		" + wrapperClass + " member%2$s,"
				+ "		" + wrapperClass + " coord%1$s,"
				+ "		" + wrapperClass + " coord%2$s) {"
				+ " return (!member%2$s.isOperational()"
				+ "		&& coord%2$s.isOperational()"
				+ "		&& " + holderClass + ".classifyDistance("
				+ "			\"%1$s\", member%1$s.getValue(), coord%1$s.getValue())"
				+ "			 == " + distanceClose + ");}",
					correlationFilter, correlationSubject),
				ensembleClass);

		final String membershipClass = Membership.class.getCanonicalName();
		final String inClass = In.class.getCanonicalName();
		
		// Membership annotation for the membership method
		Annotation membershipAnnotation = new Annotation(membershipClass, constPool);
		AnnotationsAttribute membershipAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
		membershipAttribute.addAnnotation(membershipAnnotation);
		membershipMethod.getMethodInfo().addAttribute(membershipAttribute);
		// Membership parameters annotations
		ParameterAnnotationsAttribute membershipParamAnnotations = new ParameterAnnotationsAttribute(constPool,
				ParameterAnnotationsAttribute.visibleTag);
		Annotation[][] membershipParamAnnotationsInfo = new Annotation[4][1];
		membershipParamAnnotationsInfo[0][0] = new Annotation(inClass, constPool);
		membershipParamAnnotationsInfo[0][0].addMemberValue("value", new StringMemberValue(
				String.format("member.%s", correlationFilter), constPool));
		membershipParamAnnotationsInfo[1][0] = new Annotation(inClass, constPool);
		membershipParamAnnotationsInfo[1][0].addMemberValue("value", new StringMemberValue(
				String.format("member.%s", correlationSubject), constPool));
		membershipParamAnnotationsInfo[2][0] = new Annotation(inClass, constPool);
		membershipParamAnnotationsInfo[2][0].addMemberValue("value", new StringMemberValue(
				String.format("coord.%s", correlationFilter), constPool));
		membershipParamAnnotationsInfo[3][0] = new Annotation(inClass, constPool);
		membershipParamAnnotationsInfo[3][0].addMemberValue("value", new StringMemberValue(
				String.format("coord.%s", correlationSubject), constPool));
		membershipParamAnnotations.setAnnotations(membershipParamAnnotationsInfo);
		membershipMethod.getMethodInfo().addAttribute(membershipParamAnnotations);

		// Add the method into the ensemble class
		ensembleClass.addMethod(membershipMethod);

		final String paramHolderClass = ParamHolder.class.getCanonicalName();
		
		// Map method
		CtMethod mapMethod = CtNewMethod.make(String.format(
				"public static void map("
				+ "		String memberId,"
				+ "		String coordId,"
				+ "		" + wrapperClass + " coord%1$s,"
				+ "		" + paramHolderClass + " member%1$s) {"
//				+ " System.out.println(\"Knowledge injection \" + coordId + \" -> \" + memberId + \" %1$s \" + coord%1$s.getValue() + \" at \" +  coord%1$s.getTimestamp() + \" based on \" + \"%2$s\");"
				+ "	member%1$s.value = coord%1$s; "
				+ " ((" + wrapperClass + ") member%1$s.value).malfunction();}",
					correlationSubject, correlationFilter),
				ensembleClass);
		
		final String knowledgeExchangeClass = KnowledgeExchange.class.getCanonicalName();
		final String outClass = Out.class.getCanonicalName();
		
		// KnowledgeExchange annotation for the map method
		Annotation mapAnnotation = new Annotation(knowledgeExchangeClass, constPool);
		AnnotationsAttribute mapAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
		mapAttribute.addAnnotation(mapAnnotation);
		mapMethod.getMethodInfo().addAttribute(mapAttribute);
		// Map parameters annotations
		ParameterAnnotationsAttribute mapParamAnnotations = new ParameterAnnotationsAttribute(constPool,
				ParameterAnnotationsAttribute.visibleTag);
		Annotation[][] mapParamAnnotationsInfo = new Annotation[4][1];
		mapParamAnnotationsInfo[0][0] = new Annotation(inClass, constPool);
		mapParamAnnotationsInfo[0][0].addMemberValue("value", new StringMemberValue("member.id", constPool));
		mapParamAnnotationsInfo[1][0] = new Annotation(inClass, constPool);
		mapParamAnnotationsInfo[1][0].addMemberValue("value", new StringMemberValue("coord.id", constPool));
		mapParamAnnotationsInfo[2][0] = new Annotation(inClass, constPool);
		mapParamAnnotationsInfo[2][0].addMemberValue("value", new StringMemberValue(
				String.format("coord.%s", correlationSubject), constPool));
		mapParamAnnotationsInfo[3][0] = new Annotation(outClass, constPool);
		mapParamAnnotationsInfo[3][0].addMemberValue("value", new StringMemberValue(
				String.format("member.%s", correlationSubject), constPool));
		mapParamAnnotations.setAnnotations(mapParamAnnotationsInfo);
		mapMethod.getMethodInfo().addAttribute(mapParamAnnotations);

		// Add the method into the ensemble class
		ensembleClass.addMethod(mapMethod);
		
		ensembleClass.writeFile(CLASS_DIRECTORY);
		CorrelationClassLoader loader = new CorrelationClassLoader(CorrelationClassLoader.class.getClassLoader());
		return loader.loadClass(ensembleClass.getName());
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.metaadaptation.correlation.EnsembleFactory#getHelper()
	 */
	@Override
	public EnsembleFactoryHelper getHelper() {
		return ensembleFactoryHelper;
	}

}