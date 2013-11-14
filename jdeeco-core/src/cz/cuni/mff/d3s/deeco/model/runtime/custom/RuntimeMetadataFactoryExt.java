/**
 * 
 */
package cz.cuni.mff.d3s.deeco.model.runtime.custom;

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeChangeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeCoordinator;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMember;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PeriodicTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataFactoryImpl;

/**
 * Extension of the generated RuntimeMetadataFactoryImpl which is used instead of the generated factory. The class
 * allows keeping extensions to the meta-model classes in a separate package. In order for this class to be used, the genmodel must have 
 * dynamic templates enabled and a template has to be provided (located in directory /model/templates) that redefines the method {@link RuntimeMetadataFactoryImpl#init()} to instantiate this
 * class as the factory instead of the generated one. 
 * 
 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
 *
 */
public class RuntimeMetadataFactoryExt extends RuntimeMetadataFactoryImpl {

	/**
	 * 
	 */
	public RuntimeMetadataFactoryExt() {
		super();
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataFactoryImpl#createPeriodicTrigger()
	 */
	@Override
	public PeriodicTrigger createPeriodicTrigger() {
		return new PeriodicTriggerExt();
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataFactoryImpl#createKnowledgeChangeTrigger()
	 */
	@Override
	public KnowledgeChangeTrigger createKnowledgeChangeTrigger() {
		return new KnowledgeChangeTriggerExt();
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataFactoryImpl#createKnowledgePath()
	 */
	@Override
	public KnowledgePath createKnowledgePath() {
		return new KnowledgePathExt();
	}
	
	@Override
	public PathNodeField createPathNodeField() {
		return new PathNodeFieldExt();
	}

	@Override
	public PathNodeCoordinator createPathNodeCoordinator() {
		return new PathNodeCoordinatorExt();
	}
	
	@Override
	public PathNodeMember createPathNodeMember() {
		return new PathNodeMemberExt();
	}
	

	// TODO: We might have also toString() method implemented for the KnowledgePath
}
