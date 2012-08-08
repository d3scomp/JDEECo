package cz.cuni.mff.d3s.deeco.path.grammar;

import java.io.Serializable;

import cz.cuni.mff.d3s.deeco.exceptions.KMException;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;

public class KnowledgePath implements Serializable {
	private PNode pathNode;
	private String evaluation;

	public KnowledgePath(String path) throws ParseException {
		this.pathNode = JDEECoParser.parse(path);
		this.evaluation = null;
	}

	public KnowledgePath(String path, String coord, String member)
			throws ParseException {
		this.pathNode = JDEECoParser.parse(path);
		this.evaluation = null;
	}
	
	
	public void appendKnowledgePath(String postfix, boolean reevaluate) throws ParseException {
		PNode newNode = JDEECoParser.parse(postfix);
		PNode last = pathNode;
		while (last.next != null)
			last = last.next;
		last.next = newNode;
		if (reevaluate) {
			evaluation = null;
		} else if (evaluation != null) {
			evaluation += PathGrammar.PATH_SEPARATOR + postfix;
		} 
	}
	
	public void prependKnowledgePath(String prefix) throws ParseException {
		prependKnowledgePath(prefix, false);
	}
	
	public void prependKnowledgePath(String prefix, boolean reevaluate) throws ParseException {
		PNode newNode = JDEECoParser.parse(prefix);
		PNode last = newNode;
		while (last.next != null)
			last = last.next;
		last.next = pathNode;
		pathNode = newNode;
		if (reevaluate) {
			evaluation = null;
		} else if (evaluation != null) {
			evaluation = prefix + PathGrammar.PATH_SEPARATOR + evaluation;
		}
	}
	
	public String getEvaluatedPath(KnowledgeManager km) {
		return getEvaluatedPath(km, null, null, null);
	}
	
	public String getEvaluatedPath(KnowledgeManager km, ISession session) {
		return getEvaluatedPath(km, null, null, session);
	}

	public String getEvaluatedPath(KnowledgeManager km, String coord,
			String member, ISession session) {
		if (evaluation == null || (coord != null && member != null)) {
			try {
				evaluation = evaluatePath(pathNode, km, coord, member, session);
			} catch (KMException kme) {
				System.out.println("Knowledge path evaluation error!");
			}
		}
		return evaluation;
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
			result = (String) km.getKnowledge(result, session);
		}
		if (pathNode.next != null)
			result += PathGrammar.PATH_SEPARATOR
					+ evaluatePath((PNode) pathNode.next, km, coord, member, session);
		return result;
	}
}
