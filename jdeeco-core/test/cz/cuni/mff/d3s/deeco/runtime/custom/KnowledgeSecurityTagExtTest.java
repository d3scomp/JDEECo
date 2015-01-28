package cz.cuni.mff.d3s.deeco.runtime.custom;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.d3s.deeco.model.runtime.RuntimeModelHelper;
import cz.cuni.mff.d3s.deeco.model.runtime.api.AbsoluteSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.BlankSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeSecurityTag;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityRole;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;
import cz.cuni.mff.d3s.deeco.network.Serializer;

/**
 * 
 * @author Ondřej Štumpf
 *
 */
public class KnowledgeSecurityTagExtTest {

	private RuntimeMetadataFactory factory;
	
	@Before
	public void setUp() {
		factory = RuntimeMetadataFactoryExt.eINSTANCE;
	}
	
	@Test
	public void serializationTest() throws IOException, ClassNotFoundException {
		// given a tag is prepared
		KnowledgeSecurityTag originalTag = createTag();
		
		// when the tag is serialized
		byte[] serialization = Serializer.serialize(originalTag);
		
		// when the tag is deserialized
		KnowledgeSecurityTag deserializedTag = (KnowledgeSecurityTag)Serializer.deserialize(serialization);
		
		// then the objects are equal
		assertEquals(originalTag.toString(), deserializedTag.toString());
		assertEquals(originalTag, deserializedTag);		
		assertNotSame(originalTag, deserializedTag);
	}
	
	private KnowledgeSecurityTag createTag() {
		KnowledgeSecurityTag tag = factory.createKnowledgeSecurityTag();
		SecurityRole role = factory.createSecurityRole();
		role.setRoleName("role");
		
		AbsoluteSecurityRoleArgument absoluteArgument = factory.createAbsoluteSecurityRoleArgument();
		absoluteArgument.setName("absolute");
		absoluteArgument.setValue(123);
		
		PathSecurityRoleArgument pathArgument = factory.createPathSecurityRoleArgument();
		pathArgument.setName("absolute");
		pathArgument.setKnowledgePath(RuntimeModelHelper.createKnowledgePath("level1", "level2"));
		
		BlankSecurityRoleArgument blankArgument = factory.createBlankSecurityRoleArgument();
		blankArgument.setName("blank");
		
		role.getArguments().add(absoluteArgument);
		role.getArguments().add(pathArgument);
		role.getArguments().add(blankArgument);
		
		SecurityRole parentRole = factory.createSecurityRole();
		parentRole.setRoleName("parentRole");
		
		AbsoluteSecurityRoleArgument parentAbsoluteArgument = factory.createAbsoluteSecurityRoleArgument();
		parentAbsoluteArgument.setName("absoluteParent");
		parentAbsoluteArgument.setValue(1234);
		
		parentRole.getArguments().add(parentAbsoluteArgument);
		
		role.getConsistsOf().add(parentRole);
		tag.setRequiredRole(role);
		return tag;
	}
	
}
