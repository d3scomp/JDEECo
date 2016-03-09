package cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3;

import java.util.List;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;

import cz.cuni.mff.d3s.jdeeco.edl.model.edl.RoleDefinition;

class EnsembleRoleAssignmentMatrix {
	private ComponentAssignmentSet[] assignmentMatrix;
	
	public static EnsembleRoleAssignmentMatrix create(Context ctx, int ensembleIndex, List<RoleDefinition> roleList, int componentCount) {
		ComponentAssignmentSet[] assignments = new ComponentAssignmentSet[roleList.size()];
		for (int i = 0; i < roleList.size(); i++) {
			assignments[i] = ComponentAssignmentSet.create(ctx, ensembleIndex, roleList.get(i).getName(), componentCount);
		}
		
		return new EnsembleRoleAssignmentMatrix(assignments);
	}
	
	public EnsembleRoleAssignmentMatrix(ComponentAssignmentSet[] assignmentMatrix) {
		this.assignmentMatrix = assignmentMatrix;
	}
	
	public int getRoleCount() {
		return assignmentMatrix.length;
	}
	
	public ComponentAssignmentSet get(int roleIndex) {
		return assignmentMatrix[roleIndex];
	}
	
	public BoolExpr get(int roleIndex, int componentIndex) {
		return get(roleIndex).get(componentIndex);
	}
}