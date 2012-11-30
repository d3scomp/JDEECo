package cz.cuni.mff.d3s.deeco.path.grammar;

import java.io.Serializable;

import cz.cuni.mff.d3s.deeco.exceptions.KMException;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;

public class KnowledgePath implements Serializable {
	
	private static final long serialVersionUID = -6173052910579323995L;
	
	private final PNode pathNode;

	public KnowledgePath(String path) throws ParseException {
		this.pathNode = PathParser.parse(path);
	}

	public String getEvaluatedPath(KnowledgeManager km, String coord,
			String member, ISession session) {
		try {
			return evaluatePath(pathNode, km, coord, member, session); 
		} catch (KMException kme) {
			System.out.println("Knowledge path evaluation error!");
			return null;
		}

	}

	private String evaluatePath(PNode pathNode, KnowledgeManager km,
			String coord, String member, ISession session) throws KMException {
		String result = "";
		if (pathNode.value instanceof String) {// identifier
			result = ((String) pathNode.value);
		} else if (pathNode.value instanceof EEnsembleParty) {
			if (EEnsembleParty.COORDINATOR.equals(pathNode.value))
				result = coord;
			else
				result = member;
		} else {// expression
			result = evaluatePath((PNode) pathNode.value, km, coord, member, session);
			Object o = km.getKnowledge(result, session);
			if (o instanceof Object [] && ((Object []) o).length == 1)
				result = (String) ((Object []) o)[0];
			else
				result = (String) o;
		}
		if (pathNode.next != null)
			result += PathGrammar.PATH_SEPARATOR
					+ evaluatePath((PNode) pathNode.next, km, coord, member, session);
		return result;
	}
}
