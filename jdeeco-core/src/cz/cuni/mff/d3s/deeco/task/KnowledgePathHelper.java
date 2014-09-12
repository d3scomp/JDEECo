/**
 * 
 */
package cz.cuni.mff.d3s.deeco.task;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNode;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeComponentId;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeCoordinator;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMapKey;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMember;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;


/**
 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 *
 */
public class KnowledgePathHelper {

	public enum PathRoot {
		COORDINATOR, MEMBER
	}
	
	public static class KnowledgePathAndRoot {
		public KnowledgePath knowledgePath;
		public PathRoot root;

		public KnowledgePathAndRoot() {
		}
		
		public KnowledgePathAndRoot(KnowledgePath knowledgePath, PathRoot root) {
			super();
			this.knowledgePath = knowledgePath;
			this.root = root;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			
			if (obj == null) {
				return false;
			}
			
			if (!(obj instanceof KnowledgePathAndRoot)) {
				return false;
			}
			
			KnowledgePathAndRoot other = (KnowledgePathAndRoot) obj;
			if (knowledgePath == null) {
				if (other.knowledgePath != null) {
					return false;
				}
			} else if (!knowledgePath.equals(other.knowledgePath)) {
				return false;
			}
			if (root != other.root) {
				return false;
			}
			return true;
		}
	}
	
	/**
	 * Returns an absolute path (i.e. path is a sequence of only {@link PathNodeField}) without the member/coordinator prefix.
	 * It resolves indexes to maps and replaces indexing in a map with the {@link PathNodeField}.  
	 * 
	 * @param path input knowledge path.
	 * @param coordKnowledgeManager knowledge manager to be used when coord prefix is used in the map index
	 * @param memberKnowledgeManager knowledge manager to be used when member prefix is used in the map index
	 * @return structure containing the absolute path with the member/coordinator prefix stripped and the root prefix. <code>null</code> if there is not member/coordinator prefix in the input path.
	 * @throws KnowledgeNotFoundException 
	 */
	public static KnowledgePathAndRoot getAbsoluteStrippedPath(KnowledgePath path, ReadOnlyKnowledgeManager coordKnowledgeManager, ReadOnlyKnowledgeManager memberKnowledgeManager)
			throws KnowledgeNotFoundException {
		RuntimeMetadataFactory factory = RuntimeMetadataFactory.eINSTANCE;
		for (int i=0; i<path.getNodes().size();i++) {
			PathNode pn = path.getNodes().get(i);
			if (pn instanceof PathNodeMapKey) {
				KnowledgePath innerPath = ((PathNodeMapKey) pn).getKeyPath();
				KnowledgePathAndRoot innerPathAndRoot = getAbsoluteStrippedPath(innerPath, coordKnowledgeManager,memberKnowledgeManager);
				PathNode first = innerPathAndRoot.knowledgePath.getNodes().get(0);
				String value;
				ReadOnlyKnowledgeManager km = (innerPathAndRoot.root.equals(PathRoot.COORDINATOR)) ? coordKnowledgeManager: memberKnowledgeManager;
				if (first instanceof PathNodeComponentId) {
					value = km.getId();
				}
				else {
					ValueSet vs = km.get(Arrays.asList(innerPathAndRoot.knowledgePath));
					value = vs.getValue(innerPathAndRoot.knowledgePath).toString();
				}
				PathNodeField resField = factory.createPathNodeField();
				resField.setName(value);
				path.getNodes().remove(i);
				path.getNodes().add(i,resField);
			}
		}
		return getStrippedPath(path);
	}

	/**
	 * Returns an absolute path (i.e. path is a sequence of only {@link PathNodeField}).
	 * It resolves indexes to maps and replaces indexing in a map with the {@link PathNodeField}.  
	 * 
	 * @param path input knowledge path.
	 * @param knowledgeManager knowledge manager to be used for resolving.
	 * @return absolute knowledge path.
	 * @throws KnowledgeNotFoundException 
	 */
	public static KnowledgePath getAbsolutePath(KnowledgePath path, ReadOnlyKnowledgeManager knowledgeManager) 
			throws KnowledgeNotFoundException {
		RuntimeMetadataFactory factory = RuntimeMetadataFactory.eINSTANCE;
		for (int i=0; i<path.getNodes().size();i++) {
			PathNode pn = path.getNodes().get(i);
			if (pn instanceof PathNodeMapKey) {
				KnowledgePath innerPath = ((PathNodeMapKey) pn).getKeyPath();
				innerPath = getAbsolutePath(innerPath, knowledgeManager);
				PathNode first = innerPath.getNodes().get(0);
				String value;
				if (first instanceof PathNodeComponentId) {
					value = knowledgeManager.getId();
				} else {
					ValueSet vs = knowledgeManager.get(Arrays.asList(innerPath));
					value = vs.getValue(innerPath).toString();
				}
				PathNodeField resField = factory.createPathNodeField();
				resField.setName(value);
				path.getNodes().remove(i);
				path.getNodes().add(i,resField);
			}
		}
		return path;
	}

	/**
	 * Returns a stripped path (i.e. path without member/coordinator prefix. It further checks that the input path is absolute (i.e. is a sequence of only {@link PathNodeField}).
	 * 
	 * @param absolutePath input knowledge path.
	 * @return structure containing the path with the member/coordinator prefix stripped and the root prefix. <code>null</code> if there is not member/coordinator prefix in the input path or if the input path is not absolute.
	 */
	protected static KnowledgePathAndRoot getStrippedPath(KnowledgePath absolutePath) {
		Collection<PathNode> origPathNodes = absolutePath.getNodes();
		
		if (origPathNodes.isEmpty()) {
			return null;
		}

		Iterator<PathNode> pathNodeIter = origPathNodes.iterator();
		PathNode firstPathNode = pathNodeIter.next();

		if (!(firstPathNode instanceof PathNodeCoordinator) && !(firstPathNode instanceof PathNodeMember)) {
			return null;
		}

		KnowledgePathAndRoot result = new KnowledgePathAndRoot();
		if (firstPathNode instanceof PathNodeCoordinator) {
			result.root = PathRoot.COORDINATOR;
		} else {
			result.root = PathRoot.MEMBER;
		}

		RuntimeMetadataFactory factory = RuntimeMetadataFactory.eINSTANCE;

		result.knowledgePath = factory.createKnowledgePath();
		List<PathNode> newPathNodes = result.knowledgePath.getNodes();

		while (pathNodeIter.hasNext()) {
			PathNode origNode = pathNodeIter.next();

			if (origNode instanceof PathNodeField) {
				PathNodeField newNode = factory.createPathNodeField();
				newNode.setName(((PathNodeField) origNode).getName());

				newPathNodes.add(newNode);
			} else if (origNode instanceof PathNodeComponentId) {
				PathNodeComponentId newComponentId = factory.createPathNodeComponentId();
				newPathNodes.add(newComponentId);
			} else {
				return null;
			}
		}
		
		return result; 
	}
	
}
