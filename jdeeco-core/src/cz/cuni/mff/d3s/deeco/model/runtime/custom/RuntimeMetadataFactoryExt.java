/**
 * 
 */
package cz.cuni.mff.d3s.deeco.model.runtime.custom;

import cz.cuni.mff.d3s.deeco.model.runtime.api.AbsoluteSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.BlankSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeChangeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeSecurityTag;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeComponentId;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeCoordinator;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMapKey;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMember;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityRole;
import cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger;
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
	public TimeTrigger createTimeTrigger() {
		return new TimeTriggerExt();
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
	
	@Override
	public PathNodeComponentId createPathNodeComponentId() {
		return new PathNodeComponentIdExt();
	}
	
	@Override
	public PathNodeMapKey createPathNodeMapKey() {
		return new PathNodeMapKeyExt();
	}
	
	@Override
	public KnowledgeSecurityTag createKnowledgeSecurityTag() {
		return new KnowledgeSecurityTagExt();
	}

	@Override
	public SecurityRole createSecurityRole() {
		return new SecurityRoleExt();
	}
	
	@Override
	public BlankSecurityRoleArgument createBlankSecurityRoleArgument() {
		return new BlankSecurityRoleArgumentExt();
	}
	
	@Override
	public PathSecurityRoleArgument createPathSecurityRoleArgument() {
		return new PathSecurityRoleArgumentExt();
	}
	
	@Override
	public AbsoluteSecurityRoleArgument createAbsoluteSecurityRoleArgument() {
		return new AbsoluteSecurityRoleArgumentExt();
	}
	
	
	// TODO: We might have also toString() method implemented for the KnowledgePath
}
