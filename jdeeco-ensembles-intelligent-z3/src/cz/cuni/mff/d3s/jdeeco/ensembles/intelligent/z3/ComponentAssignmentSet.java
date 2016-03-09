package cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;

class ComponentAssignmentSet {
	// for each component a boolean expression determining whether the component is
	// in the ensemble (in the particular role)
	private BoolExpr[] assignmentSet;
	
	public static ComponentAssignmentSet create(Context ctx, int ensembleIndex, String roleName, int componentCount) {
		BoolExpr[] assignments = new BoolExpr[componentCount];
		for (int j = 0; j < componentCount; j++) {
			assignments[j] = ctx.mkBoolConst("assignment_e" + ensembleIndex + "_" + roleName + "_c" + j);
		}
		
		return new ComponentAssignmentSet(assignments);
	}
	
	public ComponentAssignmentSet(BoolExpr[] assignment) {
		this.assignmentSet = assignment;
	}
	
	public BoolExpr get(int componentIndex) {
		return assignmentSet[componentIndex];
	}
}