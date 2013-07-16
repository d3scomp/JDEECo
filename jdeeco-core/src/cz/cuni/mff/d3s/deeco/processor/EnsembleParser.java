package cz.cuni.mff.d3s.deeco.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.ensemble.Ensemble;
import cz.cuni.mff.d3s.deeco.invokable.ParameterizedMethod;
import cz.cuni.mff.d3s.deeco.invokable.ParameterizedSelectorMethod;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcess;
import cz.cuni.mff.d3s.deeco.invokable.memberships.AbstractMembershipMethod;
import cz.cuni.mff.d3s.deeco.invokable.memberships.MemberMembershipMethod;
import cz.cuni.mff.d3s.deeco.invokable.memberships.MembersMembershipMethod;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.path.grammar.EEnsembleParty;
import cz.cuni.mff.d3s.deeco.path.grammar.ParseException;
import cz.cuni.mff.d3s.deeco.path.grammar.PathGrammar;
import cz.cuni.mff.d3s.deeco.scheduling.ProcessPeriodicSchedule;
import cz.cuni.mff.d3s.deeco.scheduling.ProcessSchedule;

public class EnsembleParser {

	/**
	 * Static function used to extract {@link SchedulableEnsembleProcess}
	 * instance from the class definition
	 * 
	 * @param c
	 *            class to be parsed for extraction
	 * @param km
	 *            {@link KnowledgeManager} instance that is used for knowledge
	 *            repository communication
	 * @return a {@link SchedulableEnsembleProcess} instance extracted from the
	 *         class definition
	 */
	public static SchedulableEnsembleProcess extractEnsembleProcess(Class<?> c)
			throws ParseException {
		// TODO: put names into the exception strings

		if (!isEnsembleDefinition(c)) {
			throw new ParseException("The class " + c.getName()
					+ " is not an ensemble definition.");
		}

		assert (c != null);
		// check methodEnsMembership
		final Method methodEnsMembership = AnnotationHelper.getAnnotatedMethod(
				c, Membership.class);
		// check methodEnsMembership existence
		if (methodEnsMembership == null) {
			throw new ParseException(
					"The ensemble definition does not define a membership function");
		}
		// check methodEnsMembership ParameterizedMethod existence
		ParameterizedMethod pm = ParserHelper
				.extractParametrizedMethod(methodEnsMembership);

		if (pm == null) {
			throw new ParseException(
					"Malformed membership function definition.");
		}
		// check methodEnsMembership return types
		Class<?> returnType = methodEnsMembership.getReturnType();
		if (!returnType.equals(Boolean.class)){
			throw new ParseException(
					"The return type must be always the Boolean primitive java type");
		}
		// check methodEnsMembership parameter annotations on multiplicity (=1) and value
		EEnsembleParty ensembleIdentifier = null;
		Annotation[][] annotations = methodEnsMembership.getParameterAnnotations();
		int i = 0;
		while (ensembleIdentifier == null && i < annotations.length){
			// there must one and only one annotation above each membership parameter at this time
			if (annotations[i].length == 1){
				//TODO: from now, just simply initialize the ensembleIdentifier, by priority
				if (ensembleIdentifier == null){
					Annotation a = annotations[i][0];
					// warning: it is a startsWith for the member(s) keyword!
					if (a.annotationType().equals(In.class) && ((In) a).value().startsWith(PathGrammar.MEMBERS)){
						ensembleIdentifier = EEnsembleParty.MEMBERS;
						// TODO: there should be as many Members annotations in MembersArray as types of members path in the parameters
						// TODO: check the annotation deeper: if a path is for getting IDS, the type should be a list of strings ?
					}else if (a.annotationType().equals(In.class) && ((In) a).value().startsWith(PathGrammar.MEMBER)){
						ensembleIdentifier = EEnsembleParty.MEMBER;		
						// TODO: check the annotation deeper: if a path is for getting the id, the type should be a string ?
					}
				}
			}else if (annotations[i].length == 0){
				throw new ParseException(
						"A membership parameter has not any in/inOut/out annotation");
			}else{
				throw new ParseException(
						"A membership parameter has more than one in/inOut/out annotation");
			}
			i++;
		}
		// instantiate the membership according to the returnType class and the EEnsembleParty identifier
		AbstractMembershipMethod<?> membership = null;
		if (ensembleIdentifier.equals(EEnsembleParty.MEMBER)){
			membership = new MemberMembershipMethod(pm);
		}else if (ensembleIdentifier.equals(EEnsembleParty.MEMBERS)){
			// basic tests for the selector method have already been passed
			// TODO: additional treatment for the param.sel.meth?!
			// we create the selector method for the membership
			pm = ParserHelper.extractParametrizedSelectorMethod(methodEnsMembership);
			membership = new MembersMembershipMethod((ParameterizedSelectorMethod) pm);
		}
		// knowledge exchange parsing
		final Method knowledgeExchangeMethod = AnnotationHelper
				.getAnnotatedMethod(c, KnowledgeExchange.class);

		if (knowledgeExchangeMethod == null) {
			throw new ParseException(
					"The ensemble definition does not define a knowledge exchange function");
		}

		ParameterizedMethod knowledgeExchange = null;
		if (ensembleIdentifier.equals(EEnsembleParty.MEMBER)){
			knowledgeExchange = ParserHelper.extractParametrizedMethod(knowledgeExchangeMethod);
		}else if (ensembleIdentifier.equals(EEnsembleParty.MEMBERS)){
			// the membership selectors poopulate explicitly the knowledge exchange selectors
			knowledgeExchange = ParserHelper.extractImplicitParametrizedSelectorMethod(knowledgeExchangeMethod, ((ParameterizedSelectorMethod) pm).selectors);
			//TODO: additional treatment for kno.exch.param.Method
		}
		
		if (knowledgeExchange == null) {
			throw new ParseException(
					"Malformed knowledge exchange function definition.");
		}

		// Look up scheduling
		ProcessSchedule scheduling = null;

		final ProcessSchedule periodicSchedule = ScheduleHelper
				.getPeriodicSchedule(AnnotationHelper.getAnnotation(
						PeriodicScheduling.class, knowledgeExchangeMethod.getAnnotations()));
		if (periodicSchedule != null) {
			scheduling = periodicSchedule;
		}

		if (scheduling == null) {
			// not periodic
			final ProcessSchedule triggeredSchedule = ScheduleHelper
					.getTriggeredSchedule(
							methodEnsMembership.getParameterAnnotations(),
							membership.method.in, membership.method.inOut);

			if (triggeredSchedule != null) {
				scheduling = triggeredSchedule;
			}
		}

		if (scheduling == null) {
			// No scheduling specified by annotations, using defaults
			scheduling = new ProcessPeriodicSchedule();
		}
		
		return new SchedulableEnsembleProcess(null, scheduling, membership,
					knowledgeExchange, null);
	}

	public static boolean isEnsembleDefinition(Class<?> clazz) {
		return clazz != null && Ensemble.class.isAssignableFrom(clazz);
	}
}
