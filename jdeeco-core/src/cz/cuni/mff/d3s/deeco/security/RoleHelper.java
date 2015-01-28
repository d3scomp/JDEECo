package cz.cuni.mff.d3s.deeco.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager;
import cz.cuni.mff.d3s.deeco.model.runtime.api.AbsoluteSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.BlankSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ContextKind;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityRole;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper;

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
			if (!roleArguments.containsKey(entry.getKey())) {
				match = false;
				break;
			}
			
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
		List<SecurityRole> result = new ArrayList<>();
		result.addAll(roles);
		roles.forEach(role -> result.addAll(getTransitiveRoles(role.getConsistsOf())));		
		roles.stream().filter(role -> role.getAliasRole() != null).forEach(role -> result.add(role.getAliasRole()));
		return result;
	}
	
	/**
	 * Reads role arguments values from the given knowledge manager.
	 *
	 * @param securedKnowledgePath
	 * 			  the absolute knowledge path that is being secured. This is only required when reading arguments of a security tag role, can be null otherwise. 	
	 * @param requiredRole
	 *            the role with the arguments
	 * @param knowledgeManager
	 *            the knowledge manager from which to read the data
	 * @return the map knowledge path -> value
	 * @throws KnowledgeNotFoundException
	 *            the knowledge not found exception
	 */
	public static Map<String, Object> readRoleArguments(KnowledgePath securedKnowledgePath, SecurityRole requiredRole, ReadOnlyKnowledgeManager knowledgeManager) throws KnowledgeNotFoundException {
		Map<String, Object> arguments = new HashMap<>();
		
		String authorOfTheKnowledge;
		if (securedKnowledgePath == null) {
			// reading arguments of component role, this can never point to another knowledge manager
			authorOfTheKnowledge = knowledgeManager.getId();
		} else {
			authorOfTheKnowledge = knowledgeManager.getAuthor(securedKnowledgePath);
		}
		
		// get the security role parameters and map it to the knowledge manager in which it should be evaluated
		Map<PathSecurityRoleArgument, ReadOnlyKnowledgeManager> knowledgePathsWithKnowledgeManagers = new HashMap<>();
		for (SecurityRoleArgument argument : requiredRole.getArguments()) {
			if (argument instanceof PathSecurityRoleArgument) {
				PathSecurityRoleArgument pathArgument = (PathSecurityRoleArgument)argument;
				
				if (pathArgument.getContextKind() == ContextKind.LOCAL) {
					knowledgePathsWithKnowledgeManagers.put(pathArgument, knowledgeManager);
				} else if (pathArgument.getContextKind() == ContextKind.SHADOW) {
					knowledgePathsWithKnowledgeManagers.put(pathArgument, getAuthorReplica(authorOfTheKnowledge, knowledgeManager));
				} else throw new IllegalArgumentException("Unknown ContextKind: " + pathArgument.getContextKind());
			} 
		}
				
		for (SecurityRoleArgument argument : requiredRole.getArguments()) {
			if (argument instanceof PathSecurityRoleArgument) {
				// get the value from value set
				KnowledgePath argumentPath = ((PathSecurityRoleArgument)argument).getKnowledgePath();
				ReadOnlyKnowledgeManager actualKnowledgeManager = knowledgePathsWithKnowledgeManagers.get((PathSecurityRoleArgument)argument);
				KnowledgePath absolutePath = KnowledgePathHelper.getAbsolutePath(argumentPath, actualKnowledgeManager);
				arguments.put(argument.getName(), actualKnowledgeManager.get(Arrays.asList(argumentPath)).getValue(absolutePath));
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

	/**
	 * Returns the replica with specified ID that shares the component instance with given knowledge manager
	 */
	private static ReadOnlyKnowledgeManager getAuthorReplica(String authorOfTheKnowledge, ReadOnlyKnowledgeManager knowledgeManager) throws KnowledgeNotFoundException {
		if (authorOfTheKnowledge.equals(knowledgeManager.getId())) return knowledgeManager;
		
		List<ReadOnlyKnowledgeManager> replicas = knowledgeManager.getComponent().getShadowKnowledgeManagerRegistry().getShadowKnowledgeManagers().stream()
			.filter(replica -> replica.getId().equals(authorOfTheKnowledge))
			.collect(Collectors.toList());
		
		if (replicas.size() != 1) 
			throw new KnowledgeNotFoundException();
		
		return replicas.get(0);
	}

}
