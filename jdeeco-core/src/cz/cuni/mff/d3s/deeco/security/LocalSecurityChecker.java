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
 * 
 * @author Ondřej Štumpf
 */
public class LocalSecurityChecker {
	
	/**
	 * Reference to the corresponding {@link KnowledgeManagerContainer} 
	 */
	KnowledgeManagerContainer kmContainer;
	
	/**
	 * Reference to the corresponding {@link EnsembleController} 
	 */
	EnsembleController ensembleController;
	
	public LocalSecurityChecker(EnsembleController ensembleController, KnowledgeManagerContainer kmContainer) {
		this.ensembleController = ensembleController;
		this.kmContainer = kmContainer;		
	}
	
	public boolean checkSecurity(PathRoot localRole, ReadOnlyKnowledgeManager shadowKnowledgeManager) throws TaskInvocationException {
		if (kmContainer.hasReplica(shadowKnowledgeManager.getId())) {
			// if the shadow knowledge manager belongs to a remote component, security is already guaranteed with data encryption in DefaultKnowledgeDataManager
			return true;
		} else {			
			Collection<Parameter> formalParamsOfMembership = ensembleController.getEnsembleDefinition().getMembership().getParameters();
			Collection<Parameter> formalParamsOfExchange = ensembleController.getEnsembleDefinition().getKnowledgeExchange().getParameters();
			
			Collection<KnowledgePath> knowledgePathsFromMembership = formalParamsOfMembership.stream().map(param -> param.getKnowledgePath()).collect(Collectors.toList());
			Collection<KnowledgePath> knowledgePathsFromExchange = formalParamsOfExchange.stream().map(param -> param.getKnowledgePath()).collect(Collectors.toList());
			
			boolean canAccess = canAccessKnowledge(localRole, knowledgePathsFromMembership, shadowKnowledgeManager) 
							 && canAccessKnowledge(localRole, knowledgePathsFromExchange, shadowKnowledgeManager);
			
			List<String> compromitationErrors = ModelSecurityValidator.validate(localRole, ensembleController.getEnsembleDefinition().getKnowledgeExchange(), 
					ensembleController.getComponentInstance(), shadowKnowledgeManager);
			if (!compromitationErrors.isEmpty()) {
				Log.e("Knowledge exchange would result into data compromise: " + compromitationErrors.stream().collect(Collectors.joining(", ")));
			}
			
			return canAccess && compromitationErrors.isEmpty();
		}		
	}

	private boolean canAccessKnowledge(PathRoot localRole, Collection<KnowledgePath> paths, ReadOnlyKnowledgeManager shadowKnowledgeManager) {
		// shadowKnowledgeManager is actually local and therefore has associated knowledge security tags
		KnowledgeManager localKnowledgeManager = ensembleController.getComponentInstance().getKnowledgeManager();
		
		boolean canAccessAll = true;
		
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
		
		List<SecurityRole> localRoles = RoleHelper.getTransitiveRoles(accessingKnowledgeManager.getComponent().getRoles());
		boolean canAccessTag = false;
		
		for (SecurityRole role : localRoles) {
			try {
				String roleName = role.getRoleName();
				Map<String, Object> roleArguments = RoleHelper.readRoleArguments(role, accessingKnowledgeManager);
				
				String tagName = securityTag.getRequiredRole().getRoleName();
				Map<String, Object> tagArguments = RoleHelper.readRoleArguments(role, protectingKnowledgeManager);
				
				canAccessTag = canAccessTag || (roleName.equals(tagName) && RoleHelper.roleArgumentsMatch(roleArguments, tagArguments));
			} catch (KnowledgeNotFoundException e) { 
				
			}	
		}
		return canAccessTag;
	}
	
}
