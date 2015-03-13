/**
 * 
 */
package cz.cuni.mff.d3s.deeco.task;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.pathparser.ComponentIdentifier;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.EEnsembleParty;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.PNode;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.ParseException;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.PathOrigin;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.PathParser;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
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

	
	/**
	 * Creator of a {@link KnowledgePath} from a String.  
	 *  
	 * @param path string to be parsed into knowledge path
	 * @param pathOrigin indicates whether it is being called within the context of {@link #createComponentProcess}, {@link #createEnsembleDefinition} or {@link #addSecurityTags(Class, KnowledgeManager, ChangeSet)} .
	 */
	public static KnowledgePath createKnowledgePath(String path, PathOrigin pathOrigin) throws ParseException, AnnotationProcessorException {
		PNode pNode = PathParser.parse(path);
		return createKnowledgePath(pNode, pathOrigin);
	}
	
	/**
	 * Creator of {@link KnowledgePath} from a {@link PNode}.
	 * <p>
	 * It throws an exception in the following two cases:
	 * <ul>
	 * <li>When it is called in the context of an ensemble process and a (sub-)path does not start with one of the <code>coord</code> or <code>member</code> keywords.</li>
	 * <li>When a (sub-)path contains the <code>id</code> keyword and it is <b>not</b> in the last position in the (sub-)path.</li>
	 * </ul>
	 * </p>
	 * 
	 * @param pNode head of the list as retrieved by the {@link PathParser#parse} method.
	 * @param pathOrigin indicates whether it is being called within the context of {@link #createComponentProcess}, {@link #createEnsembleDefinition} or {@link #addSecurityTags(Class, KnowledgeManager, ChangeSet)} .
	 */
	static KnowledgePath createKnowledgePath(PNode pNode, PathOrigin pathOrigin) throws AnnotationProcessorException {
		RuntimeMetadataFactory factory = RuntimeMetadataFactory.eINSTANCE;
		KnowledgePath knowledgePath = factory.createKnowledgePath();
		do {	
			Object nValue = pNode.value;
			if (nValue instanceof String) {
				// check if the first node in an ensemble path is not 'coord' or 'member':
				if (pathOrigin == PathOrigin.ENSEMBLE && knowledgePath.getNodes().isEmpty()) {
					throw new AnnotationProcessorException(
						"The path does not start with one of the '"
						+ EEnsembleParty.COORDINATOR.toString() + "' or '"
						+ EEnsembleParty.MEMBER.toString() + "' keywords."); 
				}
				// Check if this is a component identifier ("id") node.
				// In such case, this has to be the final node in the path:
				if ((nValue.equals(ComponentIdentifier.ID.toString()))
					&& (((pathOrigin == PathOrigin.COMPONENT || pathOrigin == PathOrigin.SECURITY_ANNOTATION || pathOrigin == PathOrigin.RATING_PROCESS) && knowledgePath.getNodes().isEmpty()) 
					|| (pathOrigin == PathOrigin.ENSEMBLE && (knowledgePath.getNodes().size() == 1)))) {
						PathNodeComponentId idField = factory.createPathNodeComponentId();
						knowledgePath.getNodes().add(idField); 
						if (pNode.next!=null) {
							throw new AnnotationProcessorException(
									"A component identifier cannot be followed by any other fields in a path.");
						} 
						return knowledgePath;
				} 
				PathNodeField pathNodeField = factory.createPathNodeField();
				pathNodeField.setName((String) nValue);
				knowledgePath.getNodes().add(pathNodeField);
			}
			if (nValue instanceof EEnsembleParty) {
				EEnsembleParty ensembleKeyword = (EEnsembleParty) nValue;
				if (pathOrigin == PathOrigin.ENSEMBLE && knowledgePath.getNodes().isEmpty())  {
					knowledgePath.getNodes().add(createMemberOrCoordinatorPathNode(ensembleKeyword));
				} else {
					PathNodeField pathNodeField = factory.createPathNodeField();
					pathNodeField.setName(ensembleKeyword.toString());
					knowledgePath.getNodes().add(pathNodeField);
				}
			}
			if (nValue instanceof PNode) {
				PathNodeMapKey pathNodeMapKey = factory.createPathNodeMapKey();
				pathNodeMapKey.setKeyPath(createKnowledgePath((PNode) nValue, pathOrigin));
				knowledgePath.getNodes().add(pathNodeMapKey);
			}
			pNode = pNode.next;
		} while (!(pNode == null));
		return knowledgePath;
	}
	
	/**
	 * Simple creator of either {@link PathNodeCoordinator} or {@link PathNodeMember} object.  
	 */
	static PathNode createMemberOrCoordinatorPathNode(EEnsembleParty keyword) throws AnnotationProcessorException {
		RuntimeMetadataFactory factory = RuntimeMetadataFactory.eINSTANCE;
		switch (keyword) {
			case COORDINATOR:
				return factory.createPathNodeCoordinator();
			case MEMBER:
				return factory.createPathNodeMember();
			default:
				throw new AnnotationProcessorException(
						"Invalid identifier: 'coord' or 'member' keyword expected.");
		}
	}

	
	
	
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
		
		KnowledgePath modifiablePath = cloneKnowledgePath(path);
		
		for (int i=0; i<modifiablePath.getNodes().size();i++) {
			PathNode pn = modifiablePath.getNodes().get(i);
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
				modifiablePath.getNodes().remove(i);
				modifiablePath.getNodes().add(i,resField);
			}
		}
		return getStrippedPath(modifiablePath);
	}

	/**
	 * Creates a deep clone of the knowledge path
	 * @param path to clone
	 * @return the deep copy
	 */
	public static KnowledgePath cloneKnowledgePath(KnowledgePath path) {
		RuntimeMetadataFactory factory = RuntimeMetadataFactory.eINSTANCE;
		
		KnowledgePath modifiablePath = factory.createKnowledgePath();
		for (PathNode node : path.getNodes()) {
			if (node instanceof PathNodeField) {
				PathNodeField copy = factory.createPathNodeField();
				copy.setName(((PathNodeField)node).getName());
				modifiablePath.getNodes().add(copy);
			} else if (node instanceof PathNodeMapKey) {
				PathNodeMapKey copy = factory.createPathNodeMapKey();
				copy.setKeyPath(cloneKnowledgePath(((PathNodeMapKey)node).getKeyPath()));
				modifiablePath.getNodes().add(copy);
			} else if (node instanceof PathNodeComponentId) {
				PathNodeComponentId copy = factory.createPathNodeComponentId();
				modifiablePath.getNodes().add(copy);
			} else if (node instanceof PathNodeCoordinator) {
				PathNodeCoordinator copy = factory.createPathNodeCoordinator();
				modifiablePath.getNodes().add(copy);
			} else if (node instanceof PathNodeMember) {
				PathNodeMember copy = factory.createPathNodeMember();
				modifiablePath.getNodes().add(copy);
			} else {
				throw new IllegalArgumentException("Unknown node type "+node.getClass());
			}
		}
		
		return modifiablePath;
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
		
		KnowledgePath modifiablePath = cloneKnowledgePath(path);
		
		for (int i=0; i<modifiablePath.getNodes().size();i++) {
			PathNode pn = modifiablePath.getNodes().get(i);
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
				modifiablePath.getNodes().remove(i);
				modifiablePath.getNodes().add(i,resField);
			}
		}
		return modifiablePath;
	}

	/**
	 * Checks whether the given knowledge path is absolute (i.e. consists only of {@link PathNodeField} or {@link PathNodeComponentId}).
	 * @param path input knowledge path.
	 * @return true if the path is absolute, false otherwise
	 */
	public static boolean isAbsolutePath(KnowledgePath path) {
		return path.getNodes().stream().allMatch(node -> (node instanceof PathNodeField) || (node instanceof PathNodeComponentId));
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
