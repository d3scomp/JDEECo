package cz.cuni.mff.d3s.deeco.ensembles.z3;

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

public class Z3IntelligentEnsembleFactory implements EnsembleFactory {

	private EdlDocument ensemblesDefinition;
	
	public Z3IntelligentEnsembleFactory(EdlDocument ensemblesDefinition) {
		this.ensemblesDefinition = ensemblesDefinition;
	}

	@Override
	public Collection<EnsembleInstance> createInstances(KnowledgeContainer container) throws EnsembleFormationException {
		try {
long time_milis = System.currentTimeMillis();

			Collection<Rescuer> rescuersUnsorted = container.getTrackedKnowledgeForRole(Rescuer.class);
			Rescuer[] rescuers = new Rescuer[rescuersUnsorted.size()];
			for (Rescuer rescuer : rescuersUnsorted) {
				rescuers[Integer.parseInt(rescuer.id)-1] = rescuer;
			}
			
			HashMap<String, String> cfg = new HashMap<String, String>();
	        cfg.put("model", "true");
	        Context ctx = new Context(cfg);

	        EnsembleDefinition ensembleDefinition = ensemblesDefinition.getEnsembles().get(0);	        
	        
	        RoleDefinition roleDefinition = ensembleDefinition.getRoles().get(0);
	        
			int[] positions = new int[rescuers.length];
			int maxEnsembleCount = positions.length / Math.max(1, roleDefinition.getCardinalityMin());
			
			// positions of rescuers: int[#rescuers]
			IntNum[] positionExprs = new IntNum[positions.length];
			for (int i = 0; i < positions.length; i++) {
				positionExprs[i] = ctx.mkInt(positions[i]);
			}
			
			// assignments for each rescuer and train: bool[#trains][#rescuers]
			ArrayExpr[] assignments = new ArrayExpr[maxEnsembleCount];
			for (int i = 0; i < maxEnsembleCount; i++) {
				assignments[i] = ctx.mkArrayConst("train_" + i, ctx.getIntSort(), ctx.getBoolSort());
			}
			
			// existence of individual ensembles
			BoolExpr[] ensembleExists = new BoolExpr[maxEnsembleCount];
			for (int i = 0; i < maxEnsembleCount; i++) {
				ensembleExists[i] = ctx.mkBoolConst("ensemble_exists_" + i);
			}
	
			Optimize opt = ctx.mkOptimize();
	
			// assignment counters for each rescuer and train: int[#trains][#rescuers]
			//  assignmentTempCounters[T][0] = 0 if not assignments[T][0], otherwise 1
			//  assignmentTempCounters[T][R] = assignmentTempCounters[T][R-1], if not assignments[T][R], otherwise it's +1
			// therefore assignmentTempCounters[T][#rescuers-1] = number of rescuers for train T
			ArrayExpr[] assignmentTempCounts = new ArrayExpr[maxEnsembleCount];
			for (int i = 0; i < maxEnsembleCount; i++) {
				assignmentTempCounts[i] = ctx.mkArrayConst("train_temp_count_" + i, ctx.getIntSort(), ctx.getIntSort());
				
				BoolExpr firstInSet = (BoolExpr) ctx.mkSelect(assignments[i], ctx.mkInt(0));
				opt.Add(ctx.mkImplies(firstInSet, ctx.mkEq(ctx.mkSelect(assignmentTempCounts[i], ctx.mkInt(0)), ctx.mkInt(1))));
				opt.Add(ctx.mkImplies(ctx.mkNot(firstInSet), ctx.mkEq(ctx.mkSelect(assignmentTempCounts[i], ctx.mkInt(0)), ctx.mkInt(0))));
				for (int j = 1; j < positions.length; j++) {
					BoolExpr isInSet = (BoolExpr) ctx.mkSelect(assignments[i], ctx.mkInt(j));
					IntExpr current = (IntExpr) ctx.mkSelect(assignmentTempCounts[i], ctx.mkInt(j));
					IntExpr prev = (IntExpr) ctx.mkSelect(assignmentTempCounts[i], ctx.mkInt(j-1));
					opt.Add(ctx.mkImplies(isInSet, 
							ctx.mkEq(current, ctx.mkAdd(prev, ctx.mkInt(1)))));
					opt.Add(ctx.mkImplies(ctx.mkNot(isInSet),
							ctx.mkEq(current, prev)));
				}
			}
			
			// number of rescuers is 3
			for (int i = 0; i < maxEnsembleCount; i++) {
				ArithExpr rescuerCount = (ArithExpr) ctx.mkSelect(assignmentTempCounts[i], ctx.mkInt(positions.length-1));
				BoolExpr le = ctx.mkLe(rescuerCount, ctx.mkInt(roleDefinition.getCardinalityMax())); //!!!!!!!
				BoolExpr ge = ctx.mkGe(rescuerCount, ctx.mkInt(roleDefinition.getCardinalityMin())); //!!!!!!!
				BoolExpr cardinalityOk = ctx.mkAnd(le, ge);
				opt.Add(ctx.mkImplies(ensembleExists[i], cardinalityOk));
				BoolExpr ensembleEmpty = ctx.mkEq(rescuerCount, ctx.mkInt(0));
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
					assigned[j] = ctx.mkSetMembership(posExpr, assignments[j]);
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
				for (int i = 0; i < maxEnsembleCount; i++)
					System.out.println("train " + i + ": " + m.getFuncInterp(assignments[i].getFuncDecl()));
				for (int i = 0; i < maxEnsembleCount; i++)
					System.out.println("train " + i + " temp counts: " + m.getFuncInterp(assignmentTempCounts[i].getFuncDecl()));
				
				// create ensembles
				List<EnsembleInstance> result = new ArrayList<>();
				for (int i = 0; i < maxEnsembleCount; i++) {
					IntelligentEnsemble ie = new IntelligentEnsemble(i + 1);
					ie.members = new ArrayList<>();
					FuncInterp fi = m.getFuncInterp(assignments[i].getFuncDecl());
					Entry[] entries = fi.getEntries();
					for (int j = 0; j < fi.getNumEntries(); j++) {
						Expr[] a = entries[j].getArgs();
						Expr v = entries[j].getValue();
						if (v.getBoolValue() == Z3_lbool.Z3_L_TRUE) {
							int componentId = Integer.parseInt(a[0].toString());
							ie.members.add(rescuers[componentId]);
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
