package cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.eclipse.emf.common.util.EList;

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.ArrayExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.FuncInterp;
import com.microsoft.z3.FuncInterp.Entry;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.IntNum;
import com.microsoft.z3.Model;
import com.microsoft.z3.Optimize;
import com.microsoft.z3.Status;
import com.microsoft.z3.enumerations.Z3_lbool;

import cz.cuni.mff.d3s.deeco.ensembles.EnsembleFactory;
import cz.cuni.mff.d3s.deeco.ensembles.EnsembleFormationException;
import cz.cuni.mff.d3s.deeco.ensembles.EnsembleInstance;
import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainer;
import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainerException;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EdlDocument;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EnsembleDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.RoleDefinition;
import cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3.IntelligentEnsemble;
import cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3.Rescuer;

class ComponentAssignmentSet {
	// for each component a boolean expression determining whether the component is
	// in the ensemble (in the particular role)
	private BoolExpr[] assignmentSet;
	
	public static ComponentAssignmentSet create(Context ctx, int ensembleIndex, String roleName, int componentCount) {
		BoolExpr[] assignments = new BoolExpr[componentCount];
		for (int j = 0; j < componentCount; j++) {
			assignments[j] = ctx.mkBoolConst("assignment_" + roleName + "_e" + ensembleIndex + "_c" + j);
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

class EnsembleRoleAssignmentMatrix {
	private ComponentAssignmentSet[] assignmentMatrix;
	
	public static EnsembleRoleAssignmentMatrix create(Context ctx, int ensembleIndex, RoleDefinition roleDefinition, int componentCount) {
		ComponentAssignmentSet[] assignments = new ComponentAssignmentSet[1];
		assignments[0] = ComponentAssignmentSet.create(ctx, ensembleIndex, roleDefinition.getName(), componentCount);
		
		return new EnsembleRoleAssignmentMatrix(assignments);
	}
	
	public EnsembleRoleAssignmentMatrix(ComponentAssignmentSet[] assignmentMatrix) {
		this.assignmentMatrix = assignmentMatrix;
	}
	
	public BoolExpr get(int componentIndex) {
		return assignmentMatrix[0].get(componentIndex);
	}
}

class EnsembleAssignmentMatrix {
	private EnsembleRoleAssignmentMatrix[] assignmentMatrix;
	
	public static EnsembleAssignmentMatrix create(Context ctx, int maxEnsembleCount, List<RoleDefinition> roleList, int componentCount) {
		EnsembleRoleAssignmentMatrix[] assignments = new EnsembleRoleAssignmentMatrix[maxEnsembleCount];
		for (int i = 0; i < maxEnsembleCount; i++) {
			assignments[i] = EnsembleRoleAssignmentMatrix.create(ctx, i, roleList.get(0), componentCount);
		}
		
		return new EnsembleAssignmentMatrix(assignments);
	}
	
	public EnsembleAssignmentMatrix(EnsembleRoleAssignmentMatrix[] assignmentMatrix) {
		this.assignmentMatrix = assignmentMatrix;
	}
	
	public EnsembleRoleAssignmentMatrix get(int ensembleIndex) {
		return assignmentMatrix[ensembleIndex];
	}
		
	public BoolExpr get(int ensembleIndex, int componentIndex) {
		return get(ensembleIndex).get(componentIndex);
	}
}

public class Z3IntelligentEnsembleFactory implements EnsembleFactory {

	private EdlDocument ensemblesDefinition;
	private Context ctx;
	private Optimize opt;
	
	public Z3IntelligentEnsembleFactory(EdlDocument ensemblesDefinition) {
		this.ensemblesDefinition = ensemblesDefinition;
	}
	
	private void initConfiguration() {
		HashMap<String, String> cfg = new HashMap<String, String>();
        cfg.put("model", "true");
        ctx = new Context(cfg);
        opt = ctx.mkOptimize();
	}
		
	// assignment counters for each rescuer and train: int[#trains][#rescuers]
	//  assignmentTempCounters[T][0] = 0 if not assignments[T][0], otherwise 1
	//  assignmentTempCounters[T][R] = assignmentTempCounters[T][R-1], if not assignments[T][R], otherwise it's +1
	// therefore assignmentTempCounters[T][#rescuers-1] = number of rescuers for train T
	private IntExpr createCounter(EnsembleRoleAssignmentMatrix assignments, int length, int index) {
		ArrayExpr tempCounts = ctx.mkArrayConst("_tmp_ensemble_assignment_count_" + index, ctx.getIntSort(), ctx.getIntSort());
		
		BoolExpr firstInSet = assignments.get(0);
		opt.Add(ctx.mkImplies(firstInSet, ctx.mkEq(ctx.mkSelect(tempCounts, ctx.mkInt(0)), ctx.mkInt(1))));
		opt.Add(ctx.mkImplies(ctx.mkNot(firstInSet), ctx.mkEq(ctx.mkSelect(tempCounts, ctx.mkInt(0)), ctx.mkInt(0))));
		for (int j = 1; j < length; j++) {
			BoolExpr isInSet = assignments.get(j);
			IntExpr current = (IntExpr) ctx.mkSelect(tempCounts, ctx.mkInt(j));
			IntExpr prev = (IntExpr) ctx.mkSelect(tempCounts, ctx.mkInt(j-1));
			opt.Add(ctx.mkImplies(isInSet, 
					ctx.mkEq(current, ctx.mkAdd(prev, ctx.mkInt(1)))));
			opt.Add(ctx.mkImplies(ctx.mkNot(isInSet),
					ctx.mkEq(current, prev)));
		}
		
		IntExpr assignedCount = (IntExpr) ctx.mkSelect(tempCounts, ctx.mkInt(length-1));
		return assignedCount;
	}

	@Override
	public Collection<EnsembleInstance> createInstances(KnowledgeContainer container) throws EnsembleFormationException {
		try {
long time_milis = System.currentTimeMillis();

			initConfiguration();

			Collection<Rescuer> rescuersUnsorted = container.getTrackedKnowledgeForRole(Rescuer.class);
			Rescuer[] rescuers = new Rescuer[rescuersUnsorted.size()];
			for (Rescuer rescuer : rescuersUnsorted) {
				rescuers[Integer.parseInt(rescuer.id)-1] = rescuer;
			}
	        
	        EnsembleDefinition ensembleDefinition = ensemblesDefinition.getEnsembles().get(0);	        
	        
	        RoleDefinition roleDefinition = ensembleDefinition.getRoles().get(0);
	        
			int[] positions = new int[rescuers.length];
			int maxEnsembleCount = positions.length / Math.max(1, roleDefinition.getCardinalityMin());
			
			/*// positions of rescuers: int[#rescuers]
			IntNum[] positionExprs = new IntNum[positions.length];
			for (int i = 0; i < positions.length; i++) {
				positionExprs[i] = ctx.mkInt(positions[i]);
			}*/
			
			// assignments for each rescuer and train: bool[#trains][#rescuers]
			//ArrayExpr[] assignments = createAssignmentMatrix_("rescuer", maxEnsembleCount);
			EnsembleAssignmentMatrix assignments = EnsembleAssignmentMatrix.create(ctx, maxEnsembleCount, ensembleDefinition.getRoles(), positions.length);
			
			// existence of individual ensembles
			BoolExpr[] ensembleExists = new BoolExpr[maxEnsembleCount];
			for (int i = 0; i < maxEnsembleCount; i++) {
				ensembleExists[i] = ctx.mkBoolConst("ensemble_exists_" + i);
			}
	
			IntExpr[] assignmentCounts = new IntExpr[maxEnsembleCount];
			for (int i = 0; i < maxEnsembleCount; i++) {
				assignmentCounts[i] = createCounter(assignments.get(i), positions.length, i);
			}
			
			// number of rescuers is within cardinality conditions
			for (int i = 0; i < maxEnsembleCount; i++) {
				BoolExpr le = ctx.mkLe(assignmentCounts[i], ctx.mkInt(roleDefinition.getCardinalityMax()));
				BoolExpr ge = ctx.mkGe(assignmentCounts[i], ctx.mkInt(roleDefinition.getCardinalityMin()));
				BoolExpr cardinalityOk = ctx.mkAnd(le, ge);
				opt.Add(ctx.mkImplies(ensembleExists[i], cardinalityOk));
				BoolExpr ensembleEmpty = ctx.mkEq(assignmentCounts[i], ctx.mkInt(0));
				opt.Add(ctx.mkImplies(ctx.mkNot(ensembleExists[i]), ensembleEmpty));
			}

			// nonexistence of an ensemble implies nonexistence of the following ensembles (use consecutive number set from 1)
			for (int i = 1; i < maxEnsembleCount; i++) {
				opt.Add(ctx.mkImplies(ctx.mkNot(ensembleExists[i-1]), ctx.mkNot(ensembleExists[i])));
			}
			
			// assignment to one train implies nonassignment to the others
			for (int i = 0; i < positions.length; i++) {
				IntNum posExpr = ctx.mkInt(i);
				BoolExpr[] assigned = new BoolExpr[maxEnsembleCount];
				for (int j = 0; j < maxEnsembleCount; j++) {
					assigned[j] = assignments.get(j, i);
				}
							
				opt.Add(ctx.mkOr(assigned));
				for (int j = 0; j < maxEnsembleCount; j++) {
					BoolExpr[] assignedOthers = new BoolExpr[maxEnsembleCount-1];
					for (int k = 0; k < maxEnsembleCount; k++) {
						if (k < j)
							assignedOthers[k] = assigned[k];
						else if (k > j)
							assignedOthers[k-1] = assigned[k];
					}
					
					opt.Add(ctx.mkImplies(assigned[j], ctx.mkNot(ctx.mkOr(assignedOthers))));
				}
			}
								
			Status status = opt.Check();
			if (status == Status.SATISFIABLE) {
				Model m = opt.getModel();
				for (int i = 0; i < maxEnsembleCount; i++) {
					System.out.print("train " + i + ": [");
					for (int j = 0; j < positions.length; j++) {
						System.out.print(m.getConstInterp(assignments.get(i, j)).getBoolValue() + " ");
					}
					
					System.out.println("]");
				}
				
				// create ensembles
				List<EnsembleInstance> result = new ArrayList<>();
				for (int i = 0; i < maxEnsembleCount; i++) {
					IntelligentEnsemble ie = new IntelligentEnsemble(i + 1);
					ie.members = new ArrayList<>();
					for (int j = 0; j < positions.length; j++) {
						Expr v = m.getConstInterp(assignments.get(i, j));
						if (v.getBoolValue() == Z3_lbool.Z3_L_TRUE) {
							ie.members.add(rescuers[j]);
						}
					}
					
					result.add(ie);
				}
				
				System.out.println("Time taken (ms): " + (System.currentTimeMillis() - time_milis));
				return result;
				
			} else {
				System.out.println("Unsat :-(");
				System.out.println("Time taken (ms): " + (System.currentTimeMillis() - time_milis));
				return Collections.emptyList();
			}
			
		} catch (KnowledgeContainerException e) {
			throw new EnsembleFormationException(e);
		}
	}

	@Override
	public int getSchedulingOffset() {
		return 0;
	}

	@Override
	public int getSchedulingPeriod() {
		return 1000;
	}

}
