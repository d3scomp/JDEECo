package cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3;

import com.microsoft.z3.ArrayExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.Optimize;

class ComponentAssignmentSet {
	// for each component a boolean expression determining whether the component is
	// in the ensemble (in the particular role)
	//private BoolExpr[] assignments;
	private ArrayExpr assignments;
	private int length;
	
	private Context ctx;
	private Optimize opt;
	
	private IntExpr assignedCount;
	
	public static ComponentAssignmentSet create(Context ctx, Optimize opt, int ensembleIndex, String roleName, int componentCount) {		
		ArrayExpr assignments = ctx.mkArrayConst("assignment_e" + ensembleIndex + "_" + roleName, ctx.mkIntSort(), ctx.mkBoolSort());		
		return new ComponentAssignmentSet(ctx, opt, assignments, componentCount);
	}
	
	public ComponentAssignmentSet(Context ctx, Optimize opt, ArrayExpr assignment, int length) {
		this.ctx = ctx;
		this.opt = opt;
		this.assignments = assignment;
		this.length = length;
	}
	
	// assignment counters for each rescuer and train: int[#trains][#rescuers]
	//  assignmentTempCounters[T][0] = 0 if not assignments[T][0], otherwise 1
	//  assignmentTempCounters[T][R] = assignmentTempCounters[T][R-1], if not assignments[T][R], otherwise it's +1
	// therefore assignmentTempCounters[T][#rescuers-1] = number of rescuers for train T
	public void createCounter(int ensembleIndex, int roleIndex) {
		ArrayExpr tempCounts = ctx.mkArrayConst("_tmp_ensemble_assignment_count_e" + ensembleIndex + "_r" + roleIndex, ctx.getIntSort(), ctx.getIntSort());
		
		BoolExpr firstInSet = get(0);
		opt.Add(ctx.mkImplies(firstInSet, ctx.mkEq(ctx.mkSelect(tempCounts, ctx.mkInt(0)), ctx.mkInt(1))));
		opt.Add(ctx.mkImplies(ctx.mkNot(firstInSet), ctx.mkEq(ctx.mkSelect(tempCounts, ctx.mkInt(0)), ctx.mkInt(0))));
		for (int j = 1; j < getLength(); j++) {
			BoolExpr isInSet = get(j);
			IntExpr current = (IntExpr) ctx.mkSelect(tempCounts, ctx.mkInt(j));
			IntExpr prev = (IntExpr) ctx.mkSelect(tempCounts, ctx.mkInt(j-1));
			opt.Add(ctx.mkImplies(isInSet, 
					ctx.mkEq(current, ctx.mkAdd(prev, ctx.mkInt(1)))));
			opt.Add(ctx.mkImplies(ctx.mkNot(isInSet),
					ctx.mkEq(current, prev)));
		}
		
		assignedCount = (IntExpr) ctx.mkSelect(tempCounts, ctx.mkInt(getLength()-1));
	}
	
	public int getLength() {
		return length;
	}
	
	public BoolExpr get(int componentIndex) {
		return (BoolExpr) ctx.mkSelect(assignments, ctx.mkInt(componentIndex));
	}
	
	public ArrayExpr getAssignedSet() {
		return assignments;
	}
	
	public IntExpr getAssignedCount() {
		return assignedCount;
	}
}