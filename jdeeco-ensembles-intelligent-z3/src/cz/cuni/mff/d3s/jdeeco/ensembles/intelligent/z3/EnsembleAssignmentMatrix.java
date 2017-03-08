package cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3;

import java.util.List;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.Optimize;

import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EnsembleDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.RoleDefinition;

class EnsembleAssignmentMatrix {
	private EnsembleDefinition ensembleDefinition;
	private EnsembleRoleAssignmentMatrix[] assignmentMatrix;
	
	public static EnsembleAssignmentMatrix create(Context ctx, Optimize opt, int maxEnsembleCount, EnsembleDefinition ensembleDefinition, 
			DataContainer dataContainer) {
		EnsembleRoleAssignmentMatrix[] assignments = new EnsembleRoleAssignmentMatrix[maxEnsembleCount];
		for (int i = 0; i < maxEnsembleCount; i++) {
			assignments[i] = EnsembleRoleAssignmentMatrix.create(ctx, opt, i, ensembleDefinition, dataContainer);
		}
		
		return new EnsembleAssignmentMatrix(ensembleDefinition, assignments);
	}
	
	public EnsembleAssignmentMatrix(EnsembleDefinition ensembleDefinition, EnsembleRoleAssignmentMatrix[] assignmentMatrix) {
		this.ensembleDefinition = ensembleDefinition;
		this.assignmentMatrix = assignmentMatrix;
	}
	
	public void createCounters() {		
		for (int i = 0; i < assignmentMatrix.length; i++) {
			get(i).createCounters(i);
		}
	}
	
	public EnsembleDefinition getEnsembleDefinition() {
		return ensembleDefinition;
	}
	
	public int getMaxEnsembleCount() {
		return assignmentMatrix.length;
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
	
	public IntExpr getAssignedCount(int ensembleIndex, int roleIndex) {
		return get(ensembleIndex, roleIndex).getAssignedCount();
	}
	
	public BoolExpr ensembleExists(int ensembleIndex) {
		return get(ensembleIndex).ensembleExists();
	}
}