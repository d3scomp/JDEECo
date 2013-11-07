package cz.cuni.mff.d3s.deeco.model.runtime;

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeChangeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterDirection;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNode;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PeriodicTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;

/**
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public class RuntimeModelHelper {

	public static KnowledgePath createKnowledgePath(
			String... knowledgePathNodes) {
		RuntimeMetadataFactory factory = RuntimeMetadataFactory.eINSTANCE;
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

	public static KnowledgeChangeTrigger createKnowledgeChangeTrigger(
			String... knowledgePathNodes) {
		RuntimeMetadataFactory factory = RuntimeMetadataFactory.eINSTANCE;
		KnowledgeChangeTrigger trigger = factory.createKnowledgeChangeTrigger();

		trigger.setKnowledgePath(RuntimeModelHelper
				.createKnowledgePath(knowledgePathNodes));

		return trigger;
	}
	
	public static Parameter createParameter(ParameterDirection direction,
			String... knowledgePathNodes) {
		RuntimeMetadataFactory factory = RuntimeMetadataFactory.eINSTANCE;
		Parameter param = factory.createParameter();

		param.setDirection(direction);
		param.setKnowledgePath(RuntimeModelHelper
				.createKnowledgePath(knowledgePathNodes));

		return param;
	}

	public static PeriodicTrigger createPeriodicTrigger(long period) {
		RuntimeMetadataFactory factory = RuntimeMetadataFactory.eINSTANCE;
		PeriodicTrigger trigger = factory.createPeriodicTrigger();

		trigger.setPeriod(period);

		return trigger;
	}

}
