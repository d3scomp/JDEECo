package cz.cuni.mff.d3s.deeco.security;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.model.runtime.api.AbsoluteSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityRole;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;
import cz.cuni.mff.d3s.deeco.network.KnowledgeSecurityAnnotation;

/**
 * 
 * @author Ondřej Štumpf
 *
 */
public class RemoteSecurityCheckerTest {

	RemoteSecurityChecker target;
	
	@Before
	public void setUp() {
		target = new RemoteSecurityChecker();
	}
	
	@Test
	public void checkSecurityTest() throws KnowledgeNotFoundException {
		// given annotation arguments are prepared
		Map<String, Object> annotationArguments = new HashMap<>();
		annotationArguments.put("x", 123);
		
		// given role is prepared
		SecurityRole role = createRole("role");
		AbsoluteSecurityRoleArgument argument = RuntimeMetadataFactory.eINSTANCE.createAbsoluteSecurityRoleArgument();
		argument.setName("x");
		argument.setValue(123);
		role.getArguments().add(argument);
		
		KnowledgeSecurityAnnotation annotation = new KnowledgeSecurityAnnotation("role", annotationArguments);
				
		// when checkSecurity is called()
		boolean result = target.checkSecurity(role, annotation, null);
		
		// then true is returned
		assertTrue(result);
	}
	
	private SecurityRole createRole(String name) {
		SecurityRole role = RuntimeMetadataFactory.eINSTANCE.createSecurityRole();		
		role.setRoleName(name);		
		return role;
	}
		
}
