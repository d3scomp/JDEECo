package cz.cuni.mff.d3s.deeco.security;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.model.runtime.api.AbsoluteSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.BlankSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityRole;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityRoleArgument;

/**
 * Helper class providing functionality for working with roles and their parameters.
 * @author Ondřej Štumpf
 */
public class RoleHelper {
	
	
	/**
	 * Checks if the arguments of the role contain such values that they correspond to the arguments
	 * of the security tag.
	 *
	 * @param roleArguments
	 *            the role arguments
	 * @param tagArguments
	 *            the tag arguments
	 * @return true, if successful
	 */
	public static boolean roleArgumentsMatch(Map<String, Object> roleArguments, Map<String, Object> tagArguments) {
		boolean match = true;
		
		// each of the tag parameters must be matched
		for (Entry<String, Object> entry : tagArguments.entrySet()) {			
			Object roleArgumentValue = roleArguments.get(entry.getKey());
			match = match && ((roleArgumentValue == null) || (entry.getValue() != null && entry.getValue().equals(roleArgumentValue)));
			
			if (!match) break;
		}
		
		return match;
	}
	
	/**
	 * Gets the transitive closure of the given roles.
	 *
	 * @param roles
	 *            the roles
	 * @return the transitive closure 
	 */
	public static List<SecurityRole> getTransitiveRoles(List<SecurityRole> roles) {
		List<SecurityRole> result = new LinkedList<>();
		result.addAll(roles);
		roles.forEach(role -> result.addAll(getTransitiveRoles(role.getConsistsOf())));		
		return result;
	}
	
	/**
	 * Reads role arguments values from the given knowledge manager.
	 *
	 * @param requiredRole
	 *            the role with the arguments
	 * @param knowledgeManager
	 *            the knowledge manager from which to read the data
	 * @return the map knowledge path -> value
	 * @throws KnowledgeNotFoundException
	 *             the knowledge not found exception
	 */
	public static Map<String, Object> readRoleArguments(SecurityRole requiredRole, ReadOnlyKnowledgeManager knowledgeManager) throws KnowledgeNotFoundException {
		Map<String, Object> arguments = new HashMap<>();
		
		// get the knowledge paths used in the parameters
		List<KnowledgePath> knowledgePaths = requiredRole.getArguments().stream()
				.filter(arg -> arg instanceof PathSecurityRoleArgument)
				.map(arg -> ((PathSecurityRoleArgument)arg).getKnowledgePath())
				.collect(Collectors.toList());
		
		// load the knowledge from the manager
		ValueSet argumentsValueSet = null;
		if (!knowledgePaths.isEmpty()) {
			argumentsValueSet = knowledgeManager.get(knowledgePaths);
		}
		
		for (SecurityRoleArgument argument : requiredRole.getArguments()) {
			if (argument instanceof PathSecurityRoleArgument) {
				// get the value from value set
				KnowledgePath argumentPath = ((PathSecurityRoleArgument)argument).getKnowledgePath();
				arguments.put(argument.getName(), argumentsValueSet.getValue(argumentPath));
			} else if (argument instanceof BlankSecurityRoleArgument) {
				// set null
				arguments.put(argument.getName(), null);
			} else if (argument instanceof AbsoluteSecurityRoleArgument) {
				// set the actual value of the parameter
				arguments.put(argument.getName(), ((AbsoluteSecurityRoleArgument)argument).getValue() );
			} else throw new KnowledgeNotFoundException();
		}
		return arguments;
	}
}
