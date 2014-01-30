/**
 * 
 */
package cz.cuni.mff.d3s.deeco.model.runtime.custom;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNode;
import cz.cuni.mff.d3s.deeco.model.runtime.impl.KnowledgePathImpl;

/**
 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
 *
 */
public class KnowledgePathExt extends KnowledgePathImpl implements Serializable {

	/**
	 * 
	 */
	public KnowledgePathExt() {
		super();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object that) {
		if (that instanceof KnowledgePath) {
			List<PathNode> thatNodes = new LinkedList<>(((KnowledgePath) that).getNodes());
			List<PathNode> thisNodes = new LinkedList<>(nodes);
			return thisNodes.equals(thatNodes);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int code = 0;
		for (PathNode node : getNodes()) {
			code ^= node.hashCode();
		}
		return code;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();

		Iterator<PathNode> nodesIter = getNodes().iterator();
		
		PathNode node;
		if (nodesIter.hasNext()) {
			node = nodesIter.next();
			out.append(node.toString());
		}
		
		while (nodesIter.hasNext()) {
			node = nodesIter.next();
			out.append('.');
			out.append(node.toString());
		}
		
		return out.toString();
	}
}
