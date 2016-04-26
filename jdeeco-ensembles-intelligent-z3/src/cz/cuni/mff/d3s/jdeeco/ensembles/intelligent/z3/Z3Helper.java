package cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3;

import com.microsoft.z3.ArrayExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.Optimize;

public class Z3Helper {

	Context ctx;
	Optimize opt;
	
	public Z3Helper(Context ctx, Optimize opt) {
		this.ctx = ctx;
		this.opt = opt;
	}
	
	private BoolExpr get(ArrayExpr set, int index) {
		return (BoolExpr) ctx.mkSelect(set, ctx.mkInt(index));
	}
	
	public IntExpr getSetSize(ArrayExpr set, String name, int length) {
		ArrayExpr tempCounts = ctx.mkArrayConst("_tmp_counter_" + name, ctx.getIntSort(), ctx.getIntSort());
		
		if (length == 0) {
			return ctx.mkInt(0);
		}
		
		BoolExpr firstInSet = get(set, 0);
		opt.Add(ctx.mkImplies(firstInSet, ctx.mkEq(ctx.mkSelect(tempCounts, ctx.mkInt(0)), ctx.mkInt(1))));
		opt.Add(ctx.mkImplies(ctx.mkNot(firstInSet), ctx.mkEq(ctx.mkSelect(tempCounts, ctx.mkInt(0)), ctx.mkInt(0))));
		for (int j = 1; j < length; j++) {
			BoolExpr isInSet = get(set, j);
			IntExpr current = (IntExpr) ctx.mkSelect(tempCounts, ctx.mkInt(j));
			IntExpr prev = (IntExpr) ctx.mkSelect(tempCounts, ctx.mkInt(j-1));
			opt.Add(ctx.mkImplies(isInSet, 
					ctx.mkEq(current, ctx.mkAdd(prev, ctx.mkInt(1)))));
			opt.Add(ctx.mkImplies(ctx.mkNot(isInSet),
					ctx.mkEq(current, prev)));
		}
		
		return (IntExpr) ctx.mkSelect(tempCounts, ctx.mkInt(length-1));
	}

}
