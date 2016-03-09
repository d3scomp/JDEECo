package cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3;

import java.util.List;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;

import cz.cuni.mff.d3s.jdeeco.edl.model.edl.RoleDefinition;

class EnsembleAssignmentMatrix {
	private EnsembleRoleAssignmentMatrix[] assignmentMatrix;
	
	public static EnsembleAssignmentMatrix create(Context ctx, int maxEnsembleCount, List<RoleDefinition> roleList, int componentCount) {
		EnsembleRoleAssignmentMatrix[] assignments = new EnsembleRoleAssignmentMatrix[maxEnsembleCount];
		for (int i = 0; i < maxEnsembleCount; i++) {
			assignments[i] = EnsembleRoleAssignmentMatrix.create(ctx, i, roleList, componentCount);
		}
		
		return new EnsembleAssignmentMatrix(assignments);
	}
	
	public EnsembleAssignmentMatrix(EnsembleRoleAssignmentMatrix[] assignmentMatrix) {
		this.assignmentMatrix = assignmentMatrix;
	}
	
	public EnsembleRoleAssignmentMatrix get(int ensembleIndex) {
		return assignmentMatrix[ensembleIndex];
	}
	
	public ComponentAssignmentSet get(int ensembleIndex, int roleIndex) {
		return get(ensembleIndex).get(roleIndex);
	}
		
	public BoolExpr get(int ensembleIndex, int roleIndex, int componentIndex) {
		return get(ensembleIndex, roleIndex).get(componentIndex);
	}
}