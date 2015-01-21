package cz.cuni.mff.d3s.deeco.security;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerContainer;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleController;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeSecurityTag;
import cz.cuni.mff.d3s.deeco.model.runtime.api.LocalKnowledgeTag;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityRole;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityTag;
import cz.cuni.mff.d3s.deeco.task.TaskInvocationException;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper.PathRoot;

/**
 * Performs security checks before knowledge exchange between two local components.
 * @author Ondřej Štumpf
 */
public class LocalSecurityChecker {
	
	/** Reference to the corresponding {@link KnowledgeManagerContainer}. */
	KnowledgeManagerContainer kmContainer;
	
	/** Reference to the corresponding {@link EnsembleController}. */
	EnsembleController ensembleController;
	
	/**
	 * Instantiates a new local security checker.
	 *
	 * @param ensembleController
	 *            the ensemble controller
	 * @param kmContainer
	 *            the km container
	 */
	public LocalSecurityChecker(EnsembleController ensembleController, KnowledgeManagerContainer kmContainer) {
		this.ensembleController = ensembleController;
		this.kmContainer = kmContainer;		
	}
	
	/**
	 * Checks if all knowledge paths used in the membership and knowledge
	 * exchange are secured in such way which enables the local component to
	 * access them.
	 *
	 * @param localRole
	 *            the role of the local component (member/coord)
	 * @param shadowKnowledgeManager
	 *            the shadow knowledge manager
	 * @return true if the local component has such roles that allow it to access the knowledge
	 * @throws TaskInvocationException
	 *             the task invocation exception
	 */
	public boolean checkSecurity(PathRoot localRole, ReadOnlyKnowledgeManager shadowKnowledgeManager) throws TaskInvocationException {
		boolean canAccess;
		
		if (kmContainer.hasReplica(shadowKnowledgeManager.getId())) {
			// if the shadow knowledge manager belongs to a remote component, security is already guaranteed with data encryption in DefaultKnowledgeDataManager
			canAccess = true;
		} else {			
			Collection<Parameter> formalParamsOfMembership = ensembleController.getEnsembleDefinition().getMembership().getParameters();
			Collection<Parameter> formalParamsOfExchange = ensembleController.getEnsembleDefinition().getKnowledgeExchange().getParameters();
			
			Collection<KnowledgePath> knowledgePathsFromMembership = formalParamsOfMembership.stream().map(param -> param.getKnowledgePath()).collect(Collectors.toList());
			Collection<KnowledgePath> knowledgePathsFromExchange = formalParamsOfExchange.stream().map(param -> param.getKnowledgePath()).collect(Collectors.toList());
			
			canAccess = canAccessKnowledge(localRole, knowledgePathsFromMembership, shadowKnowledgeManager) 
							 && canAccessKnowledge(localRole, knowledgePathsFromExchange, shadowKnowledgeManager);					
		}		
		
		// validate that knowledge will not be compromised (i.e. moved to a path with lesser security)		
		List<String> compromitationErrors = ModelSecurityValidator.validate(localRole, ensembleController.getEnsembleDefinition().getKnowledgeExchange(), 
				ensembleController.getComponentInstance(), shadowKnowledgeManager);
		if (!compromitationErrors.isEmpty()) {
			Log.e("Knowledge exchange would result into data compromise: " + compromitationErrors.stream().collect(Collectors.joining(", ")));
		}
				
		return canAccess && compromitationErrors.isEmpty();
	}

	/**
	 * Checks if the set of knowledge paths is secured in such way which enables the local component to
	 * access them.
	 * @param localRole
	 * 			the role of the local component (member/coord)
	 * @param paths
	 * 			the knowledge paths
	 * @param shadowKnowledgeManager
	 * 			the shadow knowledge manager
	 * @return true if the local component has such roles that allow it to access the knowledge
	 */
	private boolean canAccessKnowledge(PathRoot localRole, Collection<KnowledgePath> paths, ReadOnlyKnowledgeManager shadowKnowledgeManager) {
		KnowledgeManager localKnowledgeManager = ensembleController.getComponentInstance().getKnowledgeManager();
		
		boolean canAccessAll = true;
		
		// verify each of the paths
		for (KnowledgePath kp : paths) {		
			Map<SecurityTag, ReadOnlyKnowledgeManager> securityTagManager = new HashMap<>();
			SecurityTagCollection securityTagCollection = SecurityTagCollection.getSecurityTags(localRole, kp, localKnowledgeManager, shadowKnowledgeManager, securityTagManager);
			
			boolean canAccessPath = false;
			for (List<SecurityTag> securityTags : securityTagCollection) {
				boolean canAccessAllConditions = true;
				for (SecurityTag securityTag : securityTags) {
					if (securityTag instanceof LocalKnowledgeTag) {
						continue;
					}
					canAccessAllConditions = canAccessAllConditions && canAccessTag((KnowledgeSecurityTag) securityTag, localKnowledgeManager, shadowKnowledgeManager, securityTagManager);
				}
				canAccessPath = canAccessPath || canAccessAllConditions;
				
				if (canAccessPath) break; // exit the loop if the condition is already satisfied to improve performance
			}
			canAccessAll = canAccessAll && (securityTagCollection.isEmpty() || canAccessPath);
		}
		
		return canAccessAll;
	}
	
	/**
	 * Checks whether the local component has role to access the given security tag.
	 */
	private boolean canAccessTag(KnowledgeSecurityTag securityTag, ReadOnlyKnowledgeManager localKnowledgeManager, ReadOnlyKnowledgeManager shadowKnowledgeManager, 
			Map<SecurityTag, ReadOnlyKnowledgeManager> securityTagManager) {
		ReadOnlyKnowledgeManager accessingKnowledgeManager, protectingKnowledgeManager;
		
		if (securityTagManager.get(securityTag) == localKnowledgeManager) {
			protectingKnowledgeManager = localKnowledgeManager;
			accessingKnowledgeManager = shadowKnowledgeManager;
		} else {
			protectingKnowledgeManager = shadowKnowledgeManager;
			accessingKnowledgeManager = localKnowledgeManager;
		}
		
		// get the roles transitive closure
		List<SecurityRole> localRoles = RoleHelper.getTransitiveRoles(accessingKnowledgeManager.getComponent().getRoles());
		boolean canAccessTag = false;
		
		// try each of the roles
		for (SecurityRole role : localRoles) {
			try {
				String roleName = role.getRoleName();
				Map<String, Object> roleArguments = RoleHelper.readRoleArguments(role, accessingKnowledgeManager);
				
				String tagName = securityTag.getRequiredRole().getRoleName();
				Map<String, Object> tagArguments = RoleHelper.readRoleArguments(role, protectingKnowledgeManager);
				
				canAccessTag = canAccessTag || (roleName.equals(tagName) && RoleHelper.roleArgumentsMatch(roleArguments, tagArguments));
			} catch (KnowledgeNotFoundException e) { 
				// do nothing
			}	
		}
		return canAccessTag;
	}
	
}
