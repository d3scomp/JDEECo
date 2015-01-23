package cz.cuni.mff.d3s.deeco.security;

import java.util.Map;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityRole;

/**
 * Class used to verification of access when exchanging knowledge between a local and a remote component.
 * @author Ondřej Štumpf
 *
 */
public class RemoteSecurityChecker {
	
	/**
	 * Checks if the local component has a role which allows it to read remote's component data.
	 * @param localSecurityRole
	 * 			the role of the local component
	 * @param remoteSecurityRole
	 * 			the role required to read the data
	 * @param accessingKnowledgeManager
	 * 			the local knowledge manager
	 * @return true if the conditions are met
	 * @throws KnowledgeNotFoundException
	 */
	public boolean checkSecurity(SecurityRole localSecurityRole, RoleWithArguments remoteSecurityRole, ReadOnlyKnowledgeManager accessingKnowledgeManager) throws KnowledgeNotFoundException {		
		Map<String, Object> localRoleArguments = RoleHelper.readRoleArguments(null, localSecurityRole, accessingKnowledgeManager);
		boolean namesMatch = localSecurityRole.getRoleName().equals(remoteSecurityRole.roleName);
		
		return namesMatch && RoleHelper.roleArgumentsMatch(localRoleArguments, remoteSecurityRole.arguments);
	}
}
