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
import cz.cuni.mff.d3s.deeco.model.runtime.api.BlankSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityRole;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityRoleArgument;

/**
 * 
 * @author Ondřej Štumpf
 */
public class RoleHelper {
	
	public static boolean roleArgumentsMatch(Map<String, Object> roleArguments, Map<String, Object> tagArguments) {
		boolean match = true;
		
		for (Entry<String, Object> entry : tagArguments.entrySet()) {
			if (roleArguments.containsKey(entry.getKey())) {
				Object roleArgumentValue = roleArguments.get(entry.getKey());
				match = match && ((roleArgumentValue == null) || (entry.getValue() != null && entry.getValue().equals(roleArgumentValue)));
			} else {
				match = false;				
			}
			
			if (!match) break;
		}
		
		return match;
	}
	
	public static List<SecurityRole> getTransitiveRoles(List<SecurityRole> roles) {
		List<SecurityRole> result = new LinkedList<>();
		result.addAll(roles);
		roles.forEach(role -> result.addAll(getTransitiveRoles(role.getConsistsOf())));		
		return result;
	}
	
	public static Map<String, Object> readRoleArguments(SecurityRole requiredRole, ReadOnlyKnowledgeManager knowledgeManager) throws KnowledgeNotFoundException {
		Map<String, Object> arguments = new HashMap<>();
		List<KnowledgePath> knowledgePaths = requiredRole.getArguments().stream()
				.filter(arg -> arg instanceof PathSecurityRoleArgument)
				.map(arg -> ((PathSecurityRoleArgument)arg).getKnowledgePath())
				.collect(Collectors.toList());
		
		ValueSet argumentsValueSet = null;
		if (!knowledgePaths.isEmpty()) {
			argumentsValueSet = knowledgeManager.get(knowledgePaths);
		}
		
		for (SecurityRoleArgument argument : requiredRole.getArguments()) {
			if (argument instanceof PathSecurityRoleArgument) {
				KnowledgePath argumentPath = ((PathSecurityRoleArgument)argument).getKnowledgePath();
				arguments.put(argument.getName(), argumentsValueSet.getValue(argumentPath));
			} else if (argument instanceof BlankSecurityRoleArgument) {
				arguments.put(argument.getName(), null);
			} else throw new KnowledgeNotFoundException();
		}
		return arguments;
	}
}
