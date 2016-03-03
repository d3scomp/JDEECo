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
	private IntExpr createCounter(ComponentAssignmentSet assignments, int length, int ensembleIndex, int roleIndex) {
		ArrayExpr tempCounts = ctx.mkArrayConst("_tmp_ensemble_assignment_count_e" + ensembleIndex + "_r" + roleIndex, ctx.getIntSort(), ctx.getIntSort());
		
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
	        
			int[] positions = new int[rescuers.length];
			
	        EnsembleDefinition ensembleDefinition = ensemblesDefinition.getEnsembles().get(0);	        
	        
	        List<RoleDefinition> roles = ensembleDefinition.getRoles();
	        int minCardinalitiesSum = 0;
	        for (RoleDefinition roleDefinition : roles) {
	        	minCardinalitiesSum += roleDefinition.getCardinalityMin();
	        }
	        
	        int maxEnsembleCount = positions.length / Math.max(1, minCardinalitiesSum);
			
			/*// positions of rescuers: int[#rescuers]
			IntNum[] positionExprs = new IntNum[positions.length];
			for (int i = 0; i < positions.length; i++) {
				positionExprs[i] = ctx.mkInt(positions[i]);
			}*/
			
			// assignments for each rescuer and train: bool[#trains][#rescuers]
			//ArrayExpr[] assignments = createAssignmentMatrix_("rescuer", maxEnsembleCount);
			EnsembleAssignmentMatrix assignments = EnsembleAssignmentMatrix.create(ctx, maxEnsembleCount, roles, positions.length);
			
			// existence of individual ensembles
			BoolExpr[] ensembleExists = new BoolExpr[maxEnsembleCount];
			for (int i = 0; i < maxEnsembleCount; i++) {
				ensembleExists[i] = ctx.mkBoolConst("ensemble_exists_" + i);
			}
	
			// indexed by (ensemble, role)
			IntExpr[][] assignmentCounts = new IntExpr[maxEnsembleCount][];
			for (int i = 0; i < maxEnsembleCount; i++) {
				assignmentCounts[i] = new IntExpr[assignments.get(i).getRoleCount()];
				for (int j = 0; j < assignmentCounts[i].length; j++) {
					assignmentCounts[i][j] = createCounter(assignments.get(i, j), positions.length, i, j);
				}
			}
			
			// number of rescuers is within cardinality conditions
			for (int i = 0; i < maxEnsembleCount; i++) {
				for (int j = 0; j < assignments.get(i).getRoleCount(); j++) {
					RoleDefinition roleDefinition = roles.get(j);
					BoolExpr le = ctx.mkLe(assignmentCounts[i][j], ctx.mkInt(roleDefinition.getCardinalityMax()));
					BoolExpr ge = ctx.mkGe(assignmentCounts[i][j], ctx.mkInt(roleDefinition.getCardinalityMin()));
					BoolExpr cardinalityOk = ctx.mkAnd(le, ge);
					opt.Add(ctx.mkImplies(ensembleExists[i], cardinalityOk));
					BoolExpr ensembleEmpty = ctx.mkEq(assignmentCounts[i][j], ctx.mkInt(0));
					opt.Add(ctx.mkImplies(ctx.mkNot(ensembleExists[i]), ensembleEmpty));
				}
			}

			// nonexistence of an ensemble implies nonexistence of the following ensembles (use consecutive number set from 1)
			for (int i = 1; i < maxEnsembleCount; i++) {
				opt.Add(ctx.mkImplies(ctx.mkNot(ensembleExists[i-1]), ctx.mkNot(ensembleExists[i])));
			}
			
			// assignment to one <ensemble,role> implies nonassignment to the others
			for (int c = 0; c < positions.length; c++) {
				List<BoolExpr> assigned = new ArrayList<BoolExpr>();
				for (int e = 0; e < maxEnsembleCount; e++) {
					for (int r = 0; r < assignments.get(e).getRoleCount(); r++) {
						assigned.add(assignments.get(e, r, c));
					}
				}
							
				opt.Add(ctx.mkOr(assigned.toArray(new BoolExpr [assigned.size()])));
				for (int j = 0; j < assigned.size(); j++) {
					BoolExpr[] assignedOthers = new BoolExpr[assigned.size()-1];
					for (int k = 0; k < assigned.size(); k++) {
						if (k < j)
							assignedOthers[k] = assigned.get(k);
						else if (k > j)
							assignedOthers[k-1] = assigned.get(k);
					}
					
					opt.Add(ctx.mkImplies(assigned.get(j), ctx.mkNot(ctx.mkOr(assignedOthers))));
				}
			}
								
			Status status = opt.Check();
			if (status == Status.SATISFIABLE) {
				Model m = opt.getModel();
				//System.out.println("Solver: " + opt);
				//System.out.println("Model = " + m);
				for (int e = 0; e < maxEnsembleCount; e++) {
					System.out.println("train " + e + ":");
					for (int r = 0; r < assignments.get(e).getRoleCount(); r++) {
						System.out.print("  - " + roles.get(r).getName() + ": [");
						for (int c = 0; c < positions.length; c++) {
							System.out.print(m.getConstInterp(assignments.get(e, r, c)).getBoolValue() + " ");
						}
						
						System.out.println("]");
					}
				}
				
				// create ensembles
				List<EnsembleInstance> result = new ArrayList<>();
				for (int e = 0; e < maxEnsembleCount; e++) {
					Expr exists = m.getConstInterp(ensembleExists[e]);
					if (exists.getBoolValue() == Z3_lbool.Z3_L_FALSE)
						continue;
					
					IntelligentEnsemble ie = new IntelligentEnsemble(e + 1);
					ie.rescuers = new ArrayList<>();
					for (int c = 0; c < positions.length; c++) {
						Expr v1 = m.getConstInterp(assignments.get(e, 0, c));
						Expr v2 = m.getConstInterp(assignments.get(e, 1, c));
						if (v1.getBoolValue() == Z3_lbool.Z3_L_TRUE) {
							ie.leader = rescuers[c];
						}
						if (v2.getBoolValue() == Z3_lbool.Z3_L_TRUE) {
							ie.rescuers.add(rescuers[c]);
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
