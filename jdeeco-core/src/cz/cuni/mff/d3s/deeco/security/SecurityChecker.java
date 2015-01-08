package cz.cuni.mff.d3s.deeco.security;

import static cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper.getAbsoluteStrippedPath;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerContainer;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleController;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeSecurityTag;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityRole;
import cz.cuni.mff.d3s.deeco.task.TaskInvocationException;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper.KnowledgePathAndRoot;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper.PathRoot;

/**
 * 
 * @author Ondřej Štumpf
 */
public class SecurityChecker {
	
	/**
	 * Reference to the corresponding {@link KnowledgeManagerContainer} 
	 */
	KnowledgeManagerContainer kmContainer;
	
	/**
	 * Reference to the corresponding {@link EnsembleController} 
	 */
	EnsembleController ensembleController;
	
	public SecurityChecker(EnsembleController ensembleController, KnowledgeManagerContainer kmContainer) {
		this.ensembleController = ensembleController;
		this.kmContainer = kmContainer;
	}
	
	public boolean checkSecurity(PathRoot localRole, ReadOnlyKnowledgeManager shadowKnowledgeManager) throws TaskInvocationException {
		if (kmContainer.hasReplica(shadowKnowledgeManager.getId())) {
			// if the shadow knowledge manager belongs to a remote component, security is already guaranteed with data encryption in DefaultKnowledgeDataManager
			return true;
		} else {			
			// shadowKnowledgeManager is actually local and therefore has associated knowledge security tags
			KnowledgeManager localKnowledgeManager = ensembleController.getComponentInstance().getKnowledgeManager();
			
			Collection<Parameter> formalParamsOfMembership = ensembleController.getEnsembleDefinition().getMembership().getParameters();
			Collection<Parameter> formalParamsOfExchange = ensembleController.getEnsembleDefinition().getKnowledgeExchange().getParameters();
			
			Collection<KnowledgePath> shadowPathsFromMembership = new LinkedList<>();
			Collection<KnowledgePath> localPathsFromMembership = new LinkedList<>();
			Collection<KnowledgePath> shadowPathsFromExchange = new LinkedList<>();
			Collection<KnowledgePath> localPathsFromExchange = new LinkedList<>();
			
			getPathsFrom(localRole, formalParamsOfMembership, shadowKnowledgeManager, localPathsFromMembership, shadowPathsFromMembership);
			getPathsFrom(localRole, formalParamsOfExchange, shadowKnowledgeManager, localPathsFromExchange, shadowPathsFromExchange);
			
			return canAccessKnowledge(shadowPathsFromMembership, shadowKnowledgeManager, localKnowledgeManager) 
					&& canAccessKnowledge(shadowPathsFromExchange, shadowKnowledgeManager, localKnowledgeManager) 
					&& canAccessKnowledge(localPathsFromMembership, localKnowledgeManager, shadowKnowledgeManager) 
					&& canAccessKnowledge(localPathsFromExchange, localKnowledgeManager, shadowKnowledgeManager);
		}		
	}

	private boolean canAccessKnowledge(Collection<KnowledgePath> paths, ReadOnlyKnowledgeManager protectingKnowledgeManager, ReadOnlyKnowledgeManager accessingKnowledgeManager) {
		boolean canAccessAll = true;
		
		for (KnowledgePath kp : paths) {
			Collection<KnowledgeSecurityTag> securityTags = protectingKnowledgeManager.getSecurityTagsFor(kp);
			boolean canAccessPath = false;
			for (KnowledgeSecurityTag securityTag : securityTags) {
				canAccessPath = canAccessPath || canAccessTag(securityTag, protectingKnowledgeManager, accessingKnowledgeManager);
			}
			canAccessAll = canAccessAll && (securityTags.isEmpty() || canAccessPath);
		}
		
		return canAccessAll;
	}

	private boolean canAccessTag(KnowledgeSecurityTag securityTag, ReadOnlyKnowledgeManager protectingKnowledgeManager, ReadOnlyKnowledgeManager accessingKnowledgeManager) {
		
		Collection<SecurityRole> localRoles = accessingKnowledgeManager.getComponent().getRoles();
		boolean canAccessTag = false;
		
		for (SecurityRole role : localRoles) {
			try {
				String roleName = role.getRoleName();
				ValueSet roleArgumentsValueSet = accessingKnowledgeManager.get(role.getArguments());
				List<Object> roleArguments = role.getArguments().stream().map(path -> roleArgumentsValueSet.getValue(path)).collect(Collectors.toList());
				
				String tagName = securityTag.getRoleName();
				ValueSet tagArgumentsValueSet = protectingKnowledgeManager.get(securityTag.getArguments());
				List<Object> tagArguments = securityTag.getArguments().stream().map(path -> tagArgumentsValueSet.getValue(path)).collect(Collectors.toList());
				
				canAccessTag = canAccessTag || (roleName.equals(tagName) && roleArguments.equals(tagArguments));
			} catch (KnowledgeNotFoundException e) { 
				
			}	
		}
		return canAccessTag;
	}
	
	protected void getPathsFrom(PathRoot localRole, Collection<Parameter> formalParams, 
			ReadOnlyKnowledgeManager shadowKnowledgeManager, Collection<KnowledgePath> localPaths, Collection<KnowledgePath> shadowPaths) throws TaskInvocationException {
		KnowledgeManager localKnowledgeManager = ensembleController.getComponentInstance().getKnowledgeManager();
		
		for (Parameter formalParam : formalParams) {
			KnowledgePathAndRoot absoluteKnowledgePathAndRoot;

			// FIXME: The call to getAbsoluteStrippedPath is in theory wrong, because this way we are not obtaining the
			// knowledge within one transaction. But fortunately this is not a problem with the single 
			// threaded scheduler we have at the moment, because once the invoke method starts there is no other
			// activity whatsoever in the system.	
			try {
				if (localRole == PathRoot.COORDINATOR) {
					absoluteKnowledgePathAndRoot = getAbsoluteStrippedPath(formalParam.getKnowledgePath(), localKnowledgeManager, shadowKnowledgeManager);
				} else {
					absoluteKnowledgePathAndRoot = getAbsoluteStrippedPath(formalParam.getKnowledgePath(), shadowKnowledgeManager, localKnowledgeManager);				
				}
			} catch (KnowledgeNotFoundException e) {
				absoluteKnowledgePathAndRoot = null;
			}
			
			if (absoluteKnowledgePathAndRoot == null) {
				throw new TaskInvocationException("Member/Coordinator prefix required for membership and knowledge exchange paths.");
			} if (absoluteKnowledgePathAndRoot.root == localRole) {
				localPaths.add(absoluteKnowledgePathAndRoot.knowledgePath);
			} else {
				shadowPaths.add(absoluteKnowledgePathAndRoot.knowledgePath);
			}				
			
		}
	}
}
