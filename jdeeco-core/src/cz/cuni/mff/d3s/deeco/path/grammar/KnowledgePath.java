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

	public String getEvaluatedPath(KnowledgeManager km, String coord, String member, ISession session) {
		try {
			return evaluatePath(pathNode, km, coord, member, null, session); 
		} catch (KMException kme) {
			Log.e("Knowledge path evaluation error",kme);
			return null;
		}
	}
	
	/**
	 * applies to a single node with no relation to any other node
	 * can not be applied to a coordinator !
	 * if you want to evaluate a coordinator, use the {@link getEvaluatedPath} with an input coord and member
	 * @param km
	 * @param node
	 * @param session
	 * @return
	 */
	public String getEvaluatedPath(KnowledgeManager km, String node, ISession session) {
		try {
			return evaluatePath(pathNode, km, null, node, (String) pathNode.next.value, session); 
		} catch (KMException kme) {
			Log.e("Isolated Knowledge path evaluation error",kme);
			return null;
		}
	}
	
	public String getNaiveEvaluatedPath() {
		try {
			return evaluateNaivePath(pathNode); 
		} catch (KMException kme) {
			Log.e("Isolated Knowledge path evaluation error",kme);
			return null;
		}
	}
	
	/**
	 * provide the information if the type of path is candidate-based for the caller
	 * it will mean the caller will have to deal with an array of paths as multiple candidates
	 * are processed as input
	 * @param pathNode
	 * @return
	 */
	public Boolean isCandidateEnsemblePath() {
		return (pathNode.value instanceof EEnsembleParty && EEnsembleParty.CANDIDATES.equals(pathNode.value));
	}
	
	/**
	 * check if the path is of the form "members.identifier.*" against the supplied identifier
	 * this test function asserts if the path is well identified for a given group member identifier
	 * @param identifier
	 * @return
	 */
	public Boolean hasGroupId(String groupId){
		return (EEnsembleParty.MEMBERS.equals(pathNode.value) && groupId.equals(pathNode.next.value));
	}
	
	public String getGroupIdentifier(){
		if (EEnsembleParty.MEMBERS.equals(pathNode.value) && pathNode.next.value instanceof String)
			return (String) pathNode.next.value;
		return null;
	}

	// the node can be either a member or candidate (it does not matter to the semantics)
	private String evaluatePath(PNode pathNode, KnowledgeManager km,
			String coord, String node, String identifier, ISession session) throws KMException {
		String result = "";
		// if the pathnode value is the identifier, we do not consider it in the evaluated path
		if (pathNode.value instanceof String && 
				(identifier == null || (identifier != null && !identifier.equals(pathNode.value)))) {// identifier
			result = ((String) pathNode.value);
		} else if (pathNode.value instanceof String && identifier != null && identifier.equals(pathNode.value)){
			result = "";
		} else if (pathNode.value instanceof EEnsembleParty) { // ensemble party
			if (EEnsembleParty.COORDINATOR.equals(pathNode.value)){
				result = coord;
			// TODO: track this case
			}/*else if (EEnsembleParty.MEMBERS.equals(pathNode.value) && coord != null){
				throw new KMException("The members path can not be evaluated against any coordinator in the evaluatePath function");
			
			}*/
			else{
				result = node;
			}
		} else {// expression
			// TODO: check in case of the members if it does not screw up the process because of the identifier
			result = evaluatePath((PNode) pathNode.value, km, coord, node, identifier, session);
			Object o = km.getKnowledge(result, session);
			if (o instanceof Object [] && ((Object []) o).length == 1)
				result = (String) ((Object []) o)[0];
			else
				result = (String) o;
		}
		
		if (pathNode.next != null){
			// the same for the identifier case, we do not consider the path separator as we do not include the identifier
			if (!(pathNode.value instanceof String) || identifier == null || !identifier.equals(pathNode.value)){
				result += PathGrammar.PATH_SEPARATOR;
			}
			result += evaluatePath((PNode) pathNode.next, km, coord, node, identifier, session);
		}
		return result;
	}
	
	private String evaluateNaivePath(PNode pathNode) throws KMException {
		return pathNode.value + (pathNode.next != null ? evaluateNaivePath(pathNode.next) : "");
	}
	
	/*private String evaluatePath(PNode pathNode, KnowledgeManager km,
			String node, ISession session) throws KMException {
		return evaluatePath(pathNode, km, null, node, null, session);
	}*/
	
	public String toString() {
		return pathNode.toString();
	}
}
