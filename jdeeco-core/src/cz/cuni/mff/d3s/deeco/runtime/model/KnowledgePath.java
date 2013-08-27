package cz.cuni.mff.d3s.deeco.runtime.model;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.exceptions.KMException;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.path.grammar.PathGrammar;

public class KnowledgePath {

	private List<PathNode> nodes;

	public KnowledgePath(List<PathNode> nodes) {
		super();
		this.nodes = nodes;
	}

	public KnowledgePath() {
		this(new LinkedList<PathNode>());
	}

	public List<PathNode> getNodes() {
		return nodes;
	}
	
	public void addNode(PathNode node) {
		nodes.add(node);
	}
	
	public void join(KnowledgePath knowledgePath) {
		nodes.addAll(knowledgePath.getNodes());
	}

	public String getEvaluatedPath(KnowledgeManager km, String coord,
			String member, String prepend, ISession session) {
		try {
			if (prepend != null && !prepend.equals(""))
				return prepend + PathGrammar.PATH_SEPARATOR + evaluate(this, km, coord, member, session);
			else
				return evaluate(this, km, coord, member, session);
		} catch (KMException kme) {
			Log.e("Knowledge path evaluation error", kme);
			return null;
		}

	}

	private String evaluate(KnowledgePath kp, KnowledgeManager km,
			String coord, String member, ISession session) throws KMException {
		StringBuilder builder = new StringBuilder();
		for (PathNode pn : kp.getNodes()) {
			if (pn instanceof PathNodeField) {
				PathNodeField pnf = (PathNodeField) pn;
				if (PathGrammar.COORD.equals(pnf.getName()))
					builder.append(coord);
				else if (PathGrammar.MEMBER.equals(pnf.getName()))
					builder.append(member);
				else
					builder.append(pnf.getName());
			} else {
				PathNodeMapKey pnmk = (PathNodeMapKey) pn;
				String evaluation = evaluate(pnmk.getKeyPath(), km, coord,
						member, session);
				Object o = km.getKnowledge(evaluation, session);
				if (o instanceof Object[] && ((Object[]) o).length == 1)
					builder.append((String) ((Object[]) o)[0]);
				else
					builder.append((String) o);
			}
			builder.append(PathGrammar.PATH_SEPARATOR);
		}
		builder.replace(builder.length() - 1, builder.length(), "");
		return builder.toString();
	}
}
