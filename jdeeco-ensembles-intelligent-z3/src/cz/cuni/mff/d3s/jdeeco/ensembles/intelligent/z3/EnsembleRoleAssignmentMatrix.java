package cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3;

import java.util.List;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.Optimize;

import cz.cuni.mff.d3s.jdeeco.edl.model.edl.RoleDefinition;

class EnsembleRoleAssignmentMatrix {
	private ComponentAssignmentSet[] assignmentMatrix;
	
	private BoolExpr ensembleExists;
	
	public static EnsembleRoleAssignmentMatrix create(Context ctx, Optimize opt, int ensembleIndex, List<RoleDefinition> roleList, 
			DataContainer dataContainer) {
		ComponentAssignmentSet[] assignments = new ComponentAssignmentSet[roleList.size()];
		for (int i = 0; i < roleList.size(); i++) {
			RoleDefinition roleDef = roleList.get(i);
			int componentCount = dataContainer.get(roleDef.getType().toString()).getNumInstances();
			assignments[i] = ComponentAssignmentSet.create(ctx, opt, ensembleIndex, roleDef.getName(), componentCount);
		}
		
		return new EnsembleRoleAssignmentMatrix(ctx, assignments, ensembleIndex);
	}
	
	public EnsembleRoleAssignmentMatrix(Context ctx, ComponentAssignmentSet[] assignmentMatrix, int ensembleIndex) {
		this.assignmentMatrix = assignmentMatrix;
		this.ensembleExists = ctx.mkBoolConst("ensemble_exists_" + ensembleIndex);
	}
	
	public void createCounters(int ensembleIndex) {
		for (int j = 0; j < assignmentMatrix.length; j++) {
			get(j).createCounter(ensembleIndex, j);
		}
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
	
	public IntExpr getAssignedCount(int roleIndex) {
		return get(roleIndex).getAssignedCount();
	}
	
	public BoolExpr ensembleExists() {
		return ensembleExists;
	}
}