package cz.cuni.mff.d3s.deeco.model.runtime;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Process;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeChangeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterDirection;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SchedulingSpecification;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;

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
	public Process process;
	public SchedulingSpecification schedulingSpecification;
	public KnowledgeChangeTrigger trigger;
	public Parameter paramIn, paramOut, paramInOut;
	
	private static int processMethodCallCounter;
	
	public static class ProcessParameterType {
		public int value;

		public ProcessParameterType(int value) {
			this.value = value;
		}
	}

	public static void processMethod(ProcessParameterType in, ProcessParameterType out, ProcessParameterType inOut) {
		processMethodCallCounter++;
		
		out.value = in.value + 1;
		inOut.value = inOut.value + out.value;
	}
	
	public static void resetProcessMethodCallCounter() {
		processMethodCallCounter = 0;
	}
	
	public static int getProcessMethodCallCounter() {
		return processMethodCallCounter;
	}
	
	public void setKnowledgeManager(KnowledgeManager knowledgeManager) {
		componentInstance.setKnowledgeManager(knowledgeManager);
	}
	
	public SampleRuntimeModel() throws Exception {
		RuntimeMetadataFactory factory = RuntimeMetadataFactory.eINSTANCE; 

		// Construct the top-level container
		model = factory.createRuntimeMetadata();
		
		// Construct a component instance
		componentInstance = factory.createComponentInstance();
		model.getComponentInstances().add(componentInstance);
		componentInstance.setId("sample component instance");		
		componentInstance.setKnowledgeManager(null); // TODO: add a knowledge manager with some knowledge
		
		// Construct a process
		process = factory.createProcess();
		componentInstance.getProcesses().add(process);
		process.setName("aProcess");
		process.setMethod(SampleRuntimeModel.class.getMethod("processMethod", ProcessParameterType.class, ProcessParameterType.class, ProcessParameterType.class));
		process.setComponentInstance(componentInstance);
		
		// Construct the process parameters (3 in total .. IN - "level1.in", OUT - "level1.out", INOUT - "level1.inout")
		PathNodeField pathNode;
		KnowledgePath inKnowledgePath = factory.createKnowledgePath();
		KnowledgePath outKnowledgePath = factory.createKnowledgePath();
		KnowledgePath inOutKnowledgePath = factory.createKnowledgePath();
		pathNode = factory.createPathNodeField();
		
		pathNode.setName("level1");
		inKnowledgePath.getNodes().add(pathNode);
		outKnowledgePath.getNodes().add(pathNode);
		inOutKnowledgePath.getNodes().add(pathNode);

		paramIn = factory.createParameter();
		process.getParameters().add(paramIn);
		paramIn.setDirection(ParameterDirection.IN);
		pathNode.setName("in");
		inKnowledgePath.getNodes().add(pathNode);
		paramIn.setKnowledgePath(inKnowledgePath);
		
		paramOut = factory.createParameter();
		process.getParameters().add(paramOut);
		paramOut.setDirection(ParameterDirection.OUT);
		pathNode.setName("out");
		outKnowledgePath.getNodes().add(pathNode);
		paramOut.setKnowledgePath(inKnowledgePath);
		
		paramInOut = factory.createParameter();
		process.getParameters().add(paramInOut);
		paramInOut.setDirection(ParameterDirection.INOUT);
		pathNode.setName("inout");
		inOutKnowledgePath.getNodes().add(pathNode);
		paramInOut.setKnowledgePath(inKnowledgePath);
		
		// Construct scheduling specification for the process
		schedulingSpecification = factory.createSchedulingSpecification();
		schedulingSpecification.setPeriod(10); // FIXME, what does this number mean?
		process.setSchedulingSpecification(schedulingSpecification);
		
		// Construct a trigger for the process
		trigger = factory.createKnowledgeChangeTrigger();
		schedulingSpecification.getTriggers().add(trigger);
	
		KnowledgePath triggerKnowledgePath = factory.createKnowledgePath();		
		pathNode = factory.createPathNodeField();
		pathNode.setName("level1");
		triggerKnowledgePath.getNodes().add(pathNode);
	
		pathNode = factory.createPathNodeField();
		pathNode.setName("trigger");
		triggerKnowledgePath.getNodes().add(pathNode);
		
		trigger.setKnowledgePath(triggerKnowledgePath);
	}
}
