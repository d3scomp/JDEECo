/**
 * 
 */
package cz.cuni.mff.d3s.deeco.model.runtime.custom;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Iterator;

import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl;

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNode;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.impl.KnowledgePathImpl;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;

/**
 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
 *
 */
public class KnowledgePathExt extends KnowledgePathImpl implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public KnowledgePathExt() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object that) {
		if (that instanceof KnowledgePath) {
			Iterator<PathNode> thatNodesIterator = ((KnowledgePath) that).getNodes().iterator();
			Iterator<PathNode> thisNodesIterator = nodes.iterator();

			while (thatNodesIterator.hasNext() && thisNodesIterator.hasNext()) {
				PathNode thisNode = thisNodesIterator.next();
				PathNode thatNode = thatNodesIterator.next();

				if (!thisNode.equals(thatNode)) {
					return false;
				}
			}

			return (!thatNodesIterator.hasNext()) && (!thisNodesIterator.hasNext());
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

	/*
	 * (non-Javadoc)
	 * 
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

	public static KnowledgePath createKnowledgePath(String... knowledgePathNodes) {
		RuntimeMetadataFactory factory = RuntimeMetadataFactory.eINSTANCE;
		KnowledgePath knowledgePath = factory.createKnowledgePath();

		for (String nodeName : knowledgePathNodes) {
			PathNode pathNode;

			if ("<C>".equals(nodeName)) {
				pathNode = factory.createPathNodeCoordinator();
			} else if ("<M>".equals(nodeName)) {
				pathNode = factory.createPathNodeMember();
			} else {
				pathNode = createPathNodeField(nodeName);
			}

			knowledgePath.getNodes().add(pathNode);
		}

		return knowledgePath;
	}

	public static PathNodeField createPathNodeField(String name) {
		RuntimeMetadataFactory factory = RuntimeMetadataFactory.eINSTANCE;
		PathNodeField pn = factory.createPathNodeField();
		pn.setName(new String(name));
		return pn;
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		XMLResource res = new XMLResourceImpl();
		res.getContents().add(this);
		StringWriter sw = new StringWriter();
		res.save(sw, null);

		String encoded = sw.toString();
		out.writeUTF(encoded);
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		XMLResource res = new XMLResourceImpl();
		String encoded = in.readUTF();

		try {
			InputStream stream = new ByteArrayInputStream(encoded.getBytes());
			res.load(stream, null);
			KnowledgePath kp = (KnowledgePath) res.getContents().get(0);

			getNodes().addAll(kp.getNodes());
		} catch (Exception e) {
			throw e;
		}
	}
}
