package cz.cuni.mff.d3s.deeco.path.grammar;

import java.io.Serializable;

import cz.cuni.mff.d3s.deeco.exceptions.KMException;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.logging.Log;

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
			Log.e("Knowledge path evaluation error",kme);
			return null;
		}
	}
	
	/**
	 * getEvaluatedPath for candidate extension support
	 * CAUTION: Applies only to candidate-rooted paths
	 * 
	 * @param km
	 * @param coord
	 * @param candidates
	 * @param session
	 * @return the set of paths with the root candidate ids
	 * 
	 * @author Julien Malvot
	 */
	/*public String[] getEvaluatedPaths(KnowledgeManager km, String coord,
			String[] candidates, ISession session) {
		// verification for candidate-rooted paths appliance 
		if (!isCandidateEnsemblePath()){
			Log.e("getEvaluatedCandidatePaths is exclusively for a set of candidates");
			return null;
		}
		try {
			String[] candidatePaths = new String[candidates.length];
			// evaluate the path for each candidate
			for (int i = 0; i < candidates.length; i++)
				candidatePaths[i] = evaluatePath(pathNode, km, coord, candidates[i], session);
			return candidatePaths; 
		} catch (KMException kme) {
			Log.e("Knowledge path evaluation error",kme);
			return null;
		}
	}*/
	
	/**
	 * provide the information if the type of path is candidate-based for the caller
	 * it will mean the caller will have to deal with an array of paths as multiple candidates
	 * are processed as input
	 * @param pathNode
	 * @return
	 */
	public Boolean isCandidateEnsemblePath() {
		return (pathNode.value instanceof EEnsembleParty && EEnsembleParty.CANDIDATE.equals(pathNode.value));
	}

	// the node can be either a member or candidate (it does not matter to the semantics
	private String evaluatePath(PNode pathNode, KnowledgeManager km,
			String coord, String node, ISession session) throws KMException {
		String result = "";
		if (pathNode.value instanceof String) {// identifier
			result = ((String) pathNode.value);
		} else if (pathNode.value instanceof EEnsembleParty) {
			if (EEnsembleParty.COORDINATOR.equals(pathNode.value))
				result = coord;
			else
				result = node;
		} else {// expression
			result = evaluatePath((PNode) pathNode.value, km, coord, node, session);
			Object o = km.getKnowledge(result, session);
			if (o instanceof Object [] && ((Object []) o).length == 1)
				result = (String) ((Object []) o)[0];
			else
				result = (String) o;
		}
		if (pathNode.next != null)
			result += PathGrammar.PATH_SEPARATOR
					+ evaluatePath((PNode) pathNode.next, km, coord, node, session);
		return result;
	}
	
	public String toString() {
		return pathNode.toString();
	}
}
