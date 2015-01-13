package cz.cuni.mff.d3s.deeco.security;

import java.util.Map;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityRole;
import cz.cuni.mff.d3s.deeco.network.KnowledgeSecurityAnnotation;

/**
 * 
 * @author Ondřej Štumpf
 *
 */
public class RemoteSecurityChecker {
	public boolean checkSecurity(SecurityRole securityRole, KnowledgeSecurityAnnotation securityAnnotation, ReadOnlyKnowledgeManager accessingKnowledgeManager) throws KnowledgeNotFoundException {		
		Map<String, Object> roleArguments = RoleHelper.readRoleArguments(securityRole, accessingKnowledgeManager);
		boolean namesMatch = securityRole.getRoleName().equals(securityAnnotation.getRoleName());
		
		return namesMatch && RoleHelper.roleArgumentsMatch(roleArguments, securityAnnotation.getRoleArguments());
	}
}
