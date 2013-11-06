package cz.cuni.mff.d3s.deeco.model.runtime;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagersView;
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
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PeriodicTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
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
        public PeriodicTrigger processPeriodicTrigger;
        public KnowledgeChangeTrigger processKnowledgeChangeTrigger;
        public Parameter processParamIn, processParamOut, processParamInOut;
        
        public EnsembleDefinition ensembleDefinition;
        public Condition membershipCondition;
        public Exchange knowledgeExchange;
        public PeriodicTrigger ensemblePeriodicTrigger;
        public KnowledgeChangeTrigger ensembleKnowledgeChangeTrigger;
        public Parameter membershipParamCoord, membershipParamMember;
        public Parameter exchangeParamCoordIn, exchangeParamCoordOut, exchangeParamCoordInOut;
        public Parameter exchangeParamMemberIn, exchangeParamMemberOut, exchangeParamMemberInOut;        
        
        private static int processMethodCallCounter;
        private static int membershipMethodCallCounter;
        private static int exchangeMethodCallCounter;

        private RuntimeMetadataFactory factory;
        
        public static void processMethod(Integer in, ParamHolder<Integer> out, ParamHolder<Integer> inOut) {
                processMethodCallCounter++;
                
                out.value = in + 1;
                inOut.value = inOut.value + out.value;
        }
        
        public static void resetProcessMethodCallCounter() {
                processMethodCallCounter = 0;
        }
        
        public static int getProcessMethodCallCounter() {
                return processMethodCallCounter;
        }
        
        public static boolean membershipMethod(Integer coordIn, Integer memberIn) {
                membershipMethodCallCounter++;

                return coordIn.equals(memberIn);
        }
        
        public static void resetMembershipMethodCallCounter() {
                membershipMethodCallCounter = 0;
        }
        
        public static int getMembershipMethodCallCounter() {
                return membershipMethodCallCounter;
        }
        
        public static void exchangeMethod(Integer coordIn, ParamHolder<Integer> coordOut, ParamHolder<Integer> coordInOut,
                        Integer memberIn, ParamHolder<Integer> memberOut, ParamHolder<Integer> memberInOut) {
                exchangeMethodCallCounter++;
                
                coordOut.value = memberIn;
                memberOut.value = coordIn;
                
                Integer xchng = coordInOut.value;
                coordInOut.value = memberInOut.value;
                memberInOut.value = xchng;
        }
        
        public static void resetExchangeMethodCallCounter() {
                processMethodCallCounter = 0;
        }
        
        public static int getExchangeMethodCallCounter() {
                return exchangeMethodCallCounter;
        }
        
        public void setKnowledgeManager(KnowledgeManager knowledgeManager) {
                componentInstance.setKnowledgeManager(knowledgeManager);
        }
        
        public void setOtherKnowledgeManagersAccess(KnowledgeManagersView knowledgeManagersView) {
                componentInstance.setOtherKnowledgeManagersAccess(knowledgeManagersView);
        }
        
        private KnowledgePath createKnowledgePath(String... knowledgePathNodes) {
                KnowledgePath knowledgePath = factory.createKnowledgePath();
                                
                for (String nodeName : knowledgePathNodes) {
                        PathNode pathNode;
                        
                        if ("<C>".equals(nodeName)) {
                                pathNode = factory.createPathNodeCoordinator();
                        } else if ("<M>".equals(nodeName)) {
                                pathNode = factory.createPathNodeMember();
                        } else {
                                PathNodeField pathNodeField = factory.createPathNodeField();                
                                pathNodeField.setName(nodeName);
                                pathNode = pathNodeField;
                        }
                        
                        knowledgePath.getNodes().add(pathNode);
                }
                
                return knowledgePath;
        }
        
        private Parameter createParameter(ParameterDirection direction, String... knowledgePathNodes) {
                Parameter param = factory.createParameter();
                
                param.setDirection(direction);
                param.setKnowledgePath(createKnowledgePath(knowledgePathNodes));
                
                return param;
        }
        
        private PeriodicTrigger createPeriodicTrigger(long period) {
                PeriodicTrigger trigger = factory.createPeriodicTrigger();
                
                trigger.setPeriod(period);

                return trigger;
        }
        
        private KnowledgeChangeTrigger createKnowledgeChangeTrigger(String... knowledgePathNodes) {
                KnowledgeChangeTrigger trigger = factory.createKnowledgeChangeTrigger();
                
                trigger.setKnowledgePath(createKnowledgePath(knowledgePathNodes));

                return trigger;
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
                process.setMethod(SampleRuntimeModel.class.getMethod("processMethod", Integer.class, ParamHolder.class, ParamHolder.class));
//                process.setComponentInstance(componentInstance);
                processParamIn = createParameter(ParameterDirection.IN, "level1", "in");
                processParamOut = createParameter(ParameterDirection.OUT, "level1", "out");
                processParamInOut = createParameter(ParameterDirection.INOUT, "level1", "inout");
                process.getParameters().add(processParamIn);
                process.getParameters().add(processParamOut);
                process.getParameters().add(processParamInOut);
                
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
                membershipCondition.setMethod(SampleRuntimeModel.class.getMethod("membershipMethod", Integer.class, Integer.class));
                membershipParamCoord = createParameter(ParameterDirection.IN,  "<C>", "level1", "out");
                membershipParamMember = createParameter(ParameterDirection.IN,  "<M>", "level1", "out");
                membershipCondition.getParameters().add(membershipParamCoord);
                membershipCondition.getParameters().add(membershipParamMember);
                
                // Construct a knowledge exchange 
                knowledgeExchange = factory.createExchange();
                ensembleDefinition.setKnowledgeExchange(knowledgeExchange);
                knowledgeExchange.setMethod(SampleRuntimeModel.class.getMethod("exchangeMethod",  Integer.class, ParamHolder.class, ParamHolder.class, Integer.class, ParamHolder.class, ParamHolder.class));
                exchangeParamCoordIn = createParameter(ParameterDirection.IN,  "<C>", "level1", "out");
                exchangeParamCoordOut = createParameter(ParameterDirection.OUT,  "<C>", "level1", "trigger");
                exchangeParamCoordInOut = createParameter(ParameterDirection.INOUT,  "<C>", "level1", "in");
                exchangeParamMemberIn = createParameter(ParameterDirection.IN,  "<M>", "level1", "out");
                exchangeParamMemberOut = createParameter(ParameterDirection.OUT,  "<M>", "level1", "trigger");
                exchangeParamMemberInOut = createParameter(ParameterDirection.INOUT,  "<M>", "level1", "in");
                knowledgeExchange.getParameters().add(exchangeParamCoordIn);
                knowledgeExchange.getParameters().add(exchangeParamCoordOut);
                knowledgeExchange.getParameters().add(exchangeParamCoordInOut);
                knowledgeExchange.getParameters().add(exchangeParamMemberIn);
                knowledgeExchange.getParameters().add(exchangeParamMemberOut);
                knowledgeExchange.getParameters().add(exchangeParamMemberInOut);
                
                // Constructe an ensemble controller
                ensembleController = factory.createEnsembleController();
                componentInstance.getEnsembleControllers().add(ensembleController);
                ensembleController.setEnsembleDefinition(ensembleDefinition);
        }
}