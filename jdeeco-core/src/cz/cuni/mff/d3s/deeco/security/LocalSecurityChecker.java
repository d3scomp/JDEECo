package cz.cuni.mff.d3s.deeco.security;

import static cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper.getAbsoluteStrippedPath;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerContainer;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.AccessRights;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleController;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeSecurityTag;
import cz.cuni.mff.d3s.deeco.model.runtime.api.LocalKnowledgeTag;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterKind;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityRole;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityTag;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper.KnowledgePathAndRoot;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper;
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
	
	/** Reference to the corresponding {@link ModelSecurityValidator}. */
	ModelSecurityValidator modelSecurityValidator;
	
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
		this.modelSecurityValidator = new ModelSecurityValidator();
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
		
		Collection<Parameter> formalParamsOfMembership = ensembleController.getEnsembleDefinition().getMembership().getParameters();
		Collection<Parameter> formalParamsOfExchange = ensembleController.getEnsembleDefinition().getKnowledgeExchange().getParameters();
		
		List<KnowledgePath> membershipInputPaths = formalParamsOfMembership.stream()
				.filter(parameter -> parameter.getKind() == ParameterKind.IN || parameter.getKind() == ParameterKind.INOUT)
				.map(Parameter::getKnowledgePath)
				.collect(Collectors.toList());
		List<KnowledgePath> exchangeInputPaths = formalParamsOfExchange.stream()
				.filter(parameter -> parameter.getKind() == ParameterKind.IN || parameter.getKind() == ParameterKind.INOUT)
				.map(Parameter::getKnowledgePath)
				.collect(Collectors.toList());
		
		if (kmContainer.hasReplica(shadowKnowledgeManager.getId())) {
			// if the shadow knowledge manager belongs to a remote component, security is already guaranteed with data encryption in DefaultKnowledgeDataManager
			canAccess = knowledgePresent(localRole, membershipInputPaths, shadowKnowledgeManager) 
					 && knowledgePresent(localRole, exchangeInputPaths, shadowKnowledgeManager);
		} else {						
			canAccess = knowledgePresent(localRole, membershipInputPaths, shadowKnowledgeManager) 
					 && knowledgePresent(localRole, exchangeInputPaths, shadowKnowledgeManager) 
					 && canAccessKnowledge(localRole, formalParamsOfMembership, shadowKnowledgeManager) 
					 && canAccessKnowledge(localRole, formalParamsOfExchange, shadowKnowledgeManager);					
		}		
		
		if (canAccess) {
			// validate that knowledge will not be compromised (i.e. moved to a path with lesser security)		
			Set<String> compromitationErrors = modelSecurityValidator.validate(localRole, ensembleController.getEnsembleDefinition().getKnowledgeExchange(), 
					ensembleController.getComponentInstance(), shadowKnowledgeManager);
			
			if (!compromitationErrors.isEmpty()) {
				Log.e(compromitationErrors.stream().collect(Collectors.joining(", ")));
			}
			
			return compromitationErrors.isEmpty();
		} else {
			return false;
		}		
	}

	/**
	 * Checks if the set of knowledge paths is secured in such way which enables the local component to
	 * access them.
	 * @param localRole
	 * 			the role of the local component (member/coord)
	 * @param parameters
	 * 			the method parameters
	 * @param shadowKnowledgeManager
	 * 			the shadow knowledge manager
	 * @return true if the local component has such roles that allow it to access the knowledge
	 */
	private boolean canAccessKnowledge(PathRoot localRole, Collection<Parameter> parameters, ReadOnlyKnowledgeManager shadowKnowledgeManager) {
		KnowledgeManager localKnowledgeManager = ensembleController.getComponentInstance().getKnowledgeManager();
		
		boolean canAccessAll = true;
		
		// verify each of the paths
		for (Parameter parameter : parameters) {					
			Map<SecurityTag, ReadOnlyKnowledgeManager> securityTagManager = new HashMap<>();
			SecurityTagCollection securityTagCollection = SecurityTagCollection.getSecurityTags(localRole, parameter.getKnowledgePath(), localKnowledgeManager, shadowKnowledgeManager, securityTagManager);
			
			boolean canAccessPath = false;
			for (List<SecurityTag> securityTags : securityTagCollection) {
				boolean canAccessAllConditions = true;
				for (SecurityTag securityTag : securityTags) {
					if (securityTag instanceof LocalKnowledgeTag) {
						continue;
					}
					canAccessAllConditions = canAccessAllConditions && canAccessTag(localRole, parameter, (KnowledgeSecurityTag) securityTag, localKnowledgeManager, shadowKnowledgeManager, securityTagManager);
				}
				canAccessPath = canAccessPath || canAccessAllConditions;
				
				if (canAccessPath) break; // exit the loop if the condition is already satisfied to improve performance
			}
			canAccessAll = canAccessAll && (securityTagCollection.isEmpty() || canAccessPath);
		}
		
		return canAccessAll;
	}
	
	/**
	 * Checks if such knowledge is present in the knowledge manager and therefore if it makes sense to validate security.
	 */
	private boolean knowledgePresent(PathRoot localRole, Collection<KnowledgePath> paths, ReadOnlyKnowledgeManager shadowKnowledgeManager) {		
		KnowledgeManager localKnowledgeManager = ensembleController.getComponentInstance().getKnowledgeManager();
		
		try {
			Collection<KnowledgePath> localPaths = new LinkedList<KnowledgePath>();
			Collection<KnowledgePath> shadowPaths = new LinkedList<KnowledgePath>();
			
			for (KnowledgePath path : paths) {
				KnowledgePathAndRoot absoluteKnowledgePathAndRoot;
				
				if (localRole == PathRoot.COORDINATOR) {
					absoluteKnowledgePathAndRoot = getAbsoluteStrippedPath(path, localKnowledgeManager, shadowKnowledgeManager);
				} else {
					absoluteKnowledgePathAndRoot = getAbsoluteStrippedPath(path, shadowKnowledgeManager, localKnowledgeManager);				
				}
			
				if (absoluteKnowledgePathAndRoot.root == localRole) {
					localPaths.add(absoluteKnowledgePathAndRoot.knowledgePath);					
				} else {
					shadowPaths.add(absoluteKnowledgePathAndRoot.knowledgePath);					
				}			
			}
		
			localKnowledgeManager.get(localPaths);	
			shadowKnowledgeManager.get(shadowPaths);	
			
			return true;
		} catch (KnowledgeNotFoundException e) {
			return false;
		}		
	}

	/**
	 * Checks whether the local component has role to access the given security tag.
	 */
	private boolean canAccessTag(PathRoot localRole, Parameter securedParameter, KnowledgeSecurityTag securityTag, ReadOnlyKnowledgeManager localKnowledgeManager, ReadOnlyKnowledgeManager shadowKnowledgeManager, 
			Map<SecurityTag, ReadOnlyKnowledgeManager> securityTagManager) {
		if (!accessRightsMatched(securedParameter, securityTag)) {
			return false;
		}
		
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
		
		// read the data from the security tag
		String tagName = null;
		Map<String, Object> tagArguments = null;
		try {
			KnowledgePathAndRoot pathAndRoot;
			if (localRole == PathRoot.COORDINATOR) {
				pathAndRoot = KnowledgePathHelper.getAbsoluteStrippedPath(securedParameter.getKnowledgePath(), localKnowledgeManager, shadowKnowledgeManager);
			} else {
				pathAndRoot = KnowledgePathHelper.getAbsoluteStrippedPath(securedParameter.getKnowledgePath(), shadowKnowledgeManager, localKnowledgeManager);
			}
			
			tagName = securityTag.getRequiredRole().getRoleName();
			tagArguments = RoleHelper.readRoleArguments(pathAndRoot.knowledgePath, securityTag.getRequiredRole(), protectingKnowledgeManager);
		} catch (KnowledgeNotFoundException e) {	
			return false;
		}
		
		// try each of the roles
		for (SecurityRole role : localRoles) {
			try {
				String roleName = role.getRoleName();
				Map<String, Object> roleArguments = RoleHelper.readRoleArguments(null, role, accessingKnowledgeManager);
				
				canAccessTag = canAccessTag || (roleName.equals(tagName) && RoleHelper.roleArgumentsMatch(roleArguments, tagArguments));
			} catch (KnowledgeNotFoundException e) { 
				// do nothing
			}	
		}
		return canAccessTag;
	}

	/** 
	 * Checks if the parameter kind does not violate security access rights 
	 */
	private boolean accessRightsMatched(Parameter securedParameter, KnowledgeSecurityTag securityTag) {
		if (securityTag.getAccessRights() == AccessRights.READ_WRITE) {
			return true;
		} else if (securityTag.getAccessRights() == AccessRights.READ && securedParameter.getKind() == ParameterKind.IN) {
			return true;
		} else if (securityTag.getAccessRights() == AccessRights.WRITE && securedParameter.getKind() == ParameterKind.OUT) {
			return true;
		}
		return false;
	}
	
}
