package cz.cuni.mff.d3s.deeco.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNode;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeComponentId;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeCoordinator;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMapKey;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMember;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityTag;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper.PathRoot;

/**
 * 
 * @author Ondřej Štumpf
 *
 */
public class SecurityTagCollection extends ArrayList<List<SecurityTag>> {

	private static final long serialVersionUID = -246404371591456723L;

	public static SecurityTagCollection getSecurityTags(KnowledgePath knowledgePath, ReadOnlyKnowledgeManager knowledgeManager) {
		SecurityTagCollection result = new SecurityTagCollection();
		Iterator<PathNode> iterator = knowledgePath.getNodes().iterator();
		if (!iterator.hasNext()) {
			throw new IllegalArgumentException("The knowledge path contains no nodes.");
		}
		
		PathNode firstNode = iterator.next();
		if (!(firstNode instanceof PathNodeField) && !(firstNode instanceof PathNodeComponentId)) {
			throw new IllegalArgumentException("The knowledge path must refer to a field.");
		}
		
		List<SecurityTag> tags = (firstNode instanceof PathNodeField) ? knowledgeManager.getSecurityTags((PathNodeField)firstNode) : null;
		if (tags == null) {
			tags = new LinkedList<>();
		}
		
		result.addAll(tags.stream().map(tag -> Arrays.asList(tag)).collect(Collectors.toList()));
		
		while (iterator.hasNext()) {
			PathNode node = iterator.next();
			if (node instanceof PathNodeMapKey) {
				KnowledgePath innerPath = ((PathNodeMapKey)node).getKeyPath();
				SecurityTagCollection innerTags = SecurityTagCollection.getSecurityTags(innerPath, knowledgeManager);
				result = result.mergeWith(innerTags);
			}
		}
		
		return result;
	}
	
	public static SecurityTagCollection getSecurityTags(PathRoot localRole, KnowledgePath knowledgePath, ReadOnlyKnowledgeManager localKnowledgeManager, 
			ReadOnlyKnowledgeManager shadowKnowledgeManager, Map<SecurityTag, ReadOnlyKnowledgeManager> securityTagManager) { 
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
		
		List<SecurityTag> tags = (secondNode instanceof PathNodeField) ? relevantKnowledgeManager.getSecurityTags((PathNodeField)secondNode) : null;
		if (tags == null) {
			tags = new LinkedList<>();
		}
		
		if (securityTagManager != null) {
			tags.stream().forEach(tag -> securityTagManager.put(tag, relevantKnowledgeManager));
		}
		
		result.addAll(tags.stream().map(tag -> Arrays.asList(tag)).collect(Collectors.toList()));
		
		while (iterator.hasNext()) {
			PathNode node = iterator.next();
			if (node instanceof PathNodeMapKey) {
				KnowledgePath innerPath = ((PathNodeMapKey)node).getKeyPath();
				SecurityTagCollection innerTags = getSecurityTags(localRole, innerPath, localKnowledgeManager, shadowKnowledgeManager, securityTagManager);
				result = result.mergeWith(innerTags);
			}
		}
		
		return result;
	}
	
	public SecurityTagCollection mergeWith(SecurityTagCollection other) {
		SecurityTagCollection result = new SecurityTagCollection();
		
		if (this.isEmpty()) {
			result.addAll(other);
		} else {
			for (List<SecurityTag> parentList : this) {
				if (other.isEmpty()) {
					result.add(parentList);
				} else {
					for (List<SecurityTag> innerList : other) {
						List<SecurityTag> parentClone = new ArrayList<>();
						parentClone.addAll(parentList);
						parentClone.addAll(innerList);
						result.add(parentClone);
					}	 
				}
			}
		}
		
		return result; 	
	}
}
