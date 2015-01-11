package cz.cuni.mff.d3s.deeco.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNode;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeComponentId;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeCoordinator;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMapKey;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMember;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityRole;
import cz.cuni.mff.d3s.deeco.task.TaskInvocationException;
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
	
	Map<KnowledgeSecurityTag, ReadOnlyKnowledgeManager> securityTagManager;
	
	public SecurityChecker(EnsembleController ensembleController, KnowledgeManagerContainer kmContainer) {
		this.ensembleController = ensembleController;
		this.kmContainer = kmContainer;
		this.securityTagManager = new HashMap<>();
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
			
			return canAccessKnowledge(localRole, knowledgePathsFromMembership, shadowKnowledgeManager) 
				&& canAccessKnowledge(localRole, knowledgePathsFromExchange, shadowKnowledgeManager);
		}		
	}

	private boolean canAccessKnowledge(PathRoot localRole, Collection<KnowledgePath> paths, ReadOnlyKnowledgeManager shadowKnowledgeManager) {
		// shadowKnowledgeManager is actually local and therefore has associated knowledge security tags
		KnowledgeManager localKnowledgeManager = ensembleController.getComponentInstance().getKnowledgeManager();
		
		boolean canAccessAll = true;
		
		for (KnowledgePath kp : paths) {		
			SecurityTagCollection securityTagCollection = getSecurityTagsFor(localRole, kp, localKnowledgeManager, shadowKnowledgeManager);
			
			boolean canAccessPath = false;
			for (List<KnowledgeSecurityTag> securityTags : securityTagCollection) {
				boolean canAccessAllConditions = true;
				for (KnowledgeSecurityTag securityTag : securityTags) {
					canAccessAllConditions = canAccessAllConditions && canAccessTag(securityTag, localKnowledgeManager, shadowKnowledgeManager);
				}
				canAccessPath = canAccessPath || canAccessAllConditions;
				
				if (canAccessPath) break; // exit the loop if the condition is already satisfied to improve performance
			}
			canAccessAll = canAccessAll && (securityTagCollection.isEmpty() || canAccessPath);
		}
		
		return canAccessAll;
	}

	public SecurityTagCollection getSecurityTagsFor(PathRoot localRole, KnowledgePath knowledgePath, ReadOnlyKnowledgeManager localKnowledgeManager, ReadOnlyKnowledgeManager shadowKnowledgeManager) { 
		SecurityTagCollection result = new SecurityTagCollection();
		
		Iterator<PathNode> iterator = knowledgePath.getNodes().iterator();
		if (!iterator.hasNext()) {
			throw new IllegalArgumentException("The knowledge path contains no nodes.");
		}
		PathNode firstNode = iterator.next();
		
		ReadOnlyKnowledgeManager relevantKnowledgeManager;
		if (firstNode instanceof PathNodeCoordinator) {
			if (localRole == PathRoot.COORDINATOR) {
				relevantKnowledgeManager = localKnowledgeManager;
			} else {
				relevantKnowledgeManager = shadowKnowledgeManager;
			}
		} else if (firstNode instanceof PathNodeMember) {
			if (localRole == PathRoot.COORDINATOR) {
				relevantKnowledgeManager = shadowKnowledgeManager;
			} else {
				relevantKnowledgeManager = localKnowledgeManager;
			}
		} else {
			throw new IllegalArgumentException("The knowledge path must start with member/coordinator.");
		}
		
		PathNode secondNode = iterator.next();
		if (!(secondNode instanceof PathNodeField) && !(secondNode instanceof PathNodeComponentId)) {
			throw new IllegalArgumentException("The knowledge path must refer to a field.");
		}
		
		List<KnowledgeSecurityTag> tags = (secondNode instanceof PathNodeField) ? relevantKnowledgeManager.getSecurityTags((PathNodeField)secondNode) : null;
		if (tags == null) {
			tags = new LinkedList<>();
		}
		
		tags.stream().forEach(tag -> securityTagManager.put(tag, relevantKnowledgeManager));
		result.addAll(tags.stream().map(tag -> Arrays.asList(tag)).collect(Collectors.toList()));
		
		while (iterator.hasNext()) {
			PathNode node = iterator.next();
			if (node instanceof PathNodeMapKey) {
				KnowledgePath innerPath = ((PathNodeMapKey)node).getKeyPath();
				SecurityTagCollection innerTags = getSecurityTagsFor(localRole, innerPath, localKnowledgeManager, shadowKnowledgeManager);
				result = result.mergeWith(innerTags);
			}
		}
		
		return result;
	}
	
	private boolean canAccessTag(KnowledgeSecurityTag securityTag, ReadOnlyKnowledgeManager localKnowledgeManager, ReadOnlyKnowledgeManager shadowKnowledgeManager) {
		ReadOnlyKnowledgeManager accessingKnowledgeManager, protectingKnowledgeManager;
		
		if (securityTagManager.get(securityTag) == localKnowledgeManager) {
			protectingKnowledgeManager = localKnowledgeManager;
			accessingKnowledgeManager = shadowKnowledgeManager;
		} else {
			protectingKnowledgeManager = shadowKnowledgeManager;
			accessingKnowledgeManager = localKnowledgeManager;
		}
		
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
	
	
}
