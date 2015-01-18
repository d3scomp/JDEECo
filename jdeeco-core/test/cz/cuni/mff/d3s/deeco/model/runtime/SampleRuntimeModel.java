package cz.cuni.mff.d3s.deeco.model.runtime;

import static cz.cuni.mff.d3s.deeco.model.runtime.RuntimeModelHelper.*;
import cz.cuni.mff.d3s.deeco.integrity.PathRating;
import cz.cuni.mff.d3s.deeco.integrity.RatingsHolder;
import cz.cuni.mff.d3s.deeco.integrity.ReadonlyRatingsHolder;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.ShadowKnowledgeManagerRegistry;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Condition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleController;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Exchange;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeChangeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterKind;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RatingsProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

/**
 * A helper class which constructs a sample RuntimeModel to be used in testing. For convenience, it references
 * the key entities of the model by public fields of the class. Note that the model does not contain the knowledge managers associated
 * with the component instance. They have to be supplied from outside.
 * 
 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 *
 */
public class SampleRuntimeModel {

	public RuntimeMetadata model;
	public ComponentInstance componentInstance;
	public EnsembleController ensembleController;
	public ComponentProcess process;
	public TimeTrigger processPeriodicTrigger;
	public KnowledgeChangeTrigger processKnowledgeChangeTrigger;
	public Parameter processParamIn, processParamOut, processParamInOut, processParamRating;
	public RatingsProcess ratingsProcess;
	
	public EnsembleDefinition ensembleDefinition;
	public Condition membershipCondition;
	public Exchange knowledgeExchange;
	public TimeTrigger ensemblePeriodicTrigger;
	public KnowledgeChangeTrigger ensembleKnowledgeChangeTrigger;
	
	// It is assumed that xxxParamCoord and xxxParamMember are equal and that membershipParamXXX and exchangeParamXXXIn are equal
	public Parameter membershipParamCoord, membershipParamMember, membershipParamCoordRating, membershipParamMemberRating;
	public Parameter exchangeParamCoordIn, exchangeParamCoordOut, exchangeParamCoordInOut, exchangeParamCoordRating, exchangeParamCoordReadonlyRating;
	public Parameter exchangeParamMemberIn, exchangeParamMemberOut, exchangeParamMemberInOut, exchangeParamMemberRating;
	public Parameter ratingParamIn, ratingParamReadonly, ratingParamModifiable;
	
	private static int processMethodCallCounter;
	private static int membershipMethodCallCounter;
	private static int exchangeMethodCallCounter;
	private static long membershipRatingSum, exchangeRatingSum;
	
	private RuntimeMetadataFactory factory;
	
	public static void processMethod(Integer in, ParamHolder<Integer> out, ParamHolder<Integer> inOut, ReadonlyRatingsHolder rating) {
		processMethodCallCounter++;
		
		out.value = in + 1;
		inOut.value = inOut.value + out.value + (int)rating.getRatings(PathRating.OK);
	}
	
	public static void resetProcessMethodCallCounter() {
		processMethodCallCounter = 0;
	}
	
	public static int getProcessMethodCallCounter() {
		return processMethodCallCounter;
	}
	
	public static boolean membershipMethod(Integer coordIn, Integer memberIn, ReadonlyRatingsHolder coordRating, ReadonlyRatingsHolder memberRating) {
		membershipMethodCallCounter++;
		membershipRatingSum = coordRating.getRatings(PathRating.OK) + memberRating.getRatings(PathRating.OK);
		
		return coordIn.equals(memberIn);
	}
	
	public static void resetMembershipMethodCallCounter() {
		membershipMethodCallCounter = 0;
	}
	
	public static int getMembershipMethodCallCounter() {
		return membershipMethodCallCounter;
	}
	
	public static long getMembershipRatingSum() {
		return membershipRatingSum;
	}
	
	public static long getExchangeRatingSum() {
		return exchangeRatingSum;
	}
	
	public static void exchangeMethod(Integer coordIn, ParamHolder<Integer> coordOut, ParamHolder<Integer> coordInOut,
			Integer memberIn, ParamHolder<Integer> memberOut, ParamHolder<Integer> memberInOut, 
			ReadonlyRatingsHolder coordRating, ReadonlyRatingsHolder memberRating) {
		exchangeMethodCallCounter++;
		exchangeRatingSum = coordRating.getRatings(PathRating.OUT_OF_RANGE) + memberRating.getRatings(PathRating.OK);
		
		coordOut.value = memberIn;
		memberOut.value = coordIn;

		if (coordInOut.value > 0) {
			coordInOut.value = 1;
		} else {
			coordInOut.value = 0;
		}

		if (memberInOut.value > 0) {
			memberInOut.value = 1;
		} else {
			memberInOut.value = 0;
		}
	}
	
	public static void ratingProcess(Integer in, ReadonlyRatingsHolder readonlyHolder, RatingsHolder holder) {
		if (in > 5) {
			holder.setMyRating(PathRating.OK);
		} else {
			holder.setMyRating(PathRating.OUT_OF_RANGE);
		}
	}
	
	public static void resetExchangeMethodCallCounter() {
		exchangeMethodCallCounter = 0;
	}
	
	public static int getExchangeMethodCallCounter() {
		return exchangeMethodCallCounter;
	}
	
	public void setKnowledgeManager(KnowledgeManager knowledgeManager) {
		componentInstance.setKnowledgeManager(knowledgeManager);
	}
	
	public void setOtherKnowledgeManagersAccess(ShadowKnowledgeManagerRegistry shadowKnowledgeManagerRegistry) {
		componentInstance.setShadowKnowledgeManagerRegistry(shadowKnowledgeManagerRegistry);
	}
	
	public SampleRuntimeModel() throws Exception {
		factory = RuntimeMetadataFactory.eINSTANCE; 

		// Construct the top-level container
		model = factory.createRuntimeMetadata();
		
		// Construct a component instance
		componentInstance = factory.createComponentInstance();
		model.getComponentInstances().add(componentInstance);
		componentInstance.setName("sample component instance");		
		
		// Construct a process
		process = factory.createComponentProcess();
		componentInstance.getComponentProcesses().add(process);
		process.setName("aProcess");
		process.setMethod(SampleRuntimeModel.class.getMethod("processMethod", Integer.class, ParamHolder.class, ParamHolder.class, ReadonlyRatingsHolder.class));
//		process.setComponentInstance(componentInstance);
		processParamIn = createParameter(ParameterKind.IN, "level1", "in");
		processParamOut = createParameter(ParameterKind.OUT, "level1", "out");
		processParamInOut = createParameter(ParameterKind.INOUT, "level1", "inout");
		processParamRating = createParameter(ParameterKind.RATING, "level1", "rating");
		process.getParameters().add(processParamIn);
		process.getParameters().add(processParamOut);
		process.getParameters().add(processParamInOut);
		process.getParameters().add(processParamRating);
		
		// construct a rating process
		ratingsProcess = factory.createRatingsProcess();
		componentInstance.setRatingsProcess(ratingsProcess);
		ratingsProcess.setMethod(SampleRuntimeModel.class.getMethod("ratingProcess", Integer.class, ReadonlyRatingsHolder.class, RatingsHolder.class));
		ratingParamIn = createParameter(ParameterKind.IN, "level1", "in");
		ratingParamReadonly = createParameter(ParameterKind.RATING, "level1", "new_rating");
		ratingParamReadonly.setType(ReadonlyRatingsHolder.class);
		ratingParamModifiable = createParameter(ParameterKind.RATING, "level1", "rating");
		ratingParamModifiable.setType(RatingsHolder.class);
		ratingsProcess.getParameters().add(ratingParamIn);
		ratingsProcess.getParameters().add(ratingParamReadonly);
		ratingsProcess.getParameters().add(ratingParamModifiable);
		
		// Construct a scheduling specification for the process
		processPeriodicTrigger = createPeriodicTrigger(10);
		processKnowledgeChangeTrigger = createKnowledgeChangeTrigger("level1", "trigger");
		process.getTriggers().add(processPeriodicTrigger);
		process.getTriggers().add(processKnowledgeChangeTrigger);
		
		// Construct an ensemble definition
		ensembleDefinition = factory.createEnsembleDefinition();
		model.getEnsembleDefinitions().add(ensembleDefinition);
		ensembleDefinition.setName("sample ensemble definition");
		
		// Construct a scheduling specification for the ensemble
		ensemblePeriodicTrigger = createPeriodicTrigger(10);
		ensembleKnowledgeChangeTrigger = createKnowledgeChangeTrigger("<C>", "level1", "out");
		ensembleDefinition.getTriggers().add(ensemblePeriodicTrigger);
		ensembleDefinition.getTriggers().add(ensembleKnowledgeChangeTrigger);

		// Construct a membership condition
		membershipCondition = factory.createCondition();
		ensembleDefinition.setMembership(membershipCondition);
		membershipCondition.setMethod(SampleRuntimeModel.class.getMethod("membershipMethod", Integer.class, Integer.class, ReadonlyRatingsHolder.class, ReadonlyRatingsHolder.class));
		membershipParamCoord = createParameter(ParameterKind.IN,  "<C>", "level1", "out");
		membershipParamMember = createParameter(ParameterKind.IN,  "<M>", "level1", "out");
		membershipParamCoordRating = createParameter(ParameterKind.RATING,  "<C>", "level1", "rating");
		membershipParamMemberRating = createParameter(ParameterKind.RATING,  "<M>", "level1", "rating");
		membershipCondition.getParameters().add(membershipParamCoord);
		membershipCondition.getParameters().add(membershipParamMember);
		membershipCondition.getParameters().add(membershipParamCoordRating);
		membershipCondition.getParameters().add(membershipParamMemberRating);
		
		// Construct a knowledge exchange 
		knowledgeExchange = factory.createExchange();
		ensembleDefinition.setKnowledgeExchange(knowledgeExchange);
		knowledgeExchange.setMethod(SampleRuntimeModel.class.getMethod("exchangeMethod",  Integer.class, ParamHolder.class, ParamHolder.class, Integer.class, ParamHolder.class, ParamHolder.class, ReadonlyRatingsHolder.class, ReadonlyRatingsHolder.class));
		exchangeParamCoordIn = createParameter(ParameterKind.IN,  "<C>", "level1", "out");
		exchangeParamCoordOut = createParameter(ParameterKind.OUT,  "<C>", "level1", "trigger");
		exchangeParamCoordInOut = createParameter(ParameterKind.INOUT,  "<C>", "level1", "in");
		exchangeParamMemberIn = createParameter(ParameterKind.IN,  "<M>", "level1", "out");
		exchangeParamMemberOut = createParameter(ParameterKind.OUT,  "<M>", "level1", "trigger");
		exchangeParamMemberInOut = createParameter(ParameterKind.INOUT,  "<M>", "level1", "in");
		exchangeParamCoordRating = createParameter(ParameterKind.RATING,  "<C>", "level1", "new_rating");		
		exchangeParamMemberRating = createParameter(ParameterKind.RATING,  "<M>", "level1", "rating");
		knowledgeExchange.getParameters().add(exchangeParamCoordIn);
		knowledgeExchange.getParameters().add(exchangeParamCoordOut);
		knowledgeExchange.getParameters().add(exchangeParamCoordInOut);
		knowledgeExchange.getParameters().add(exchangeParamMemberIn);
		knowledgeExchange.getParameters().add(exchangeParamMemberOut);
		knowledgeExchange.getParameters().add(exchangeParamMemberInOut);
		knowledgeExchange.getParameters().add(exchangeParamCoordRating);
		knowledgeExchange.getParameters().add(exchangeParamMemberRating);
		
		// Constructe an ensemble controller
		ensembleController = factory.createEnsembleController();
		componentInstance.getEnsembleControllers().add(ensembleController);
		ensembleController.setEnsembleDefinition(ensembleDefinition);
	}
}
