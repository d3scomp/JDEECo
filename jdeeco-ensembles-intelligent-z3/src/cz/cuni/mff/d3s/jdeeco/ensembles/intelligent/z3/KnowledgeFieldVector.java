package cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3;

import javax.activation.UnsupportedDataTypeException;

import com.microsoft.z3.ArrayExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.Optimize;
import com.microsoft.z3.Sort;

import cz.cuni.mff.d3s.jdeeco.edl.PrimitiveTypes;

class KnowledgeFieldVector {
	private ArrayExpr values; // for individual components
	private ArrayExpr values_tmp;
	private Context ctx;
	private Optimize opt;
	private boolean closed;
	
	public KnowledgeFieldVector(Context ctx, Optimize opt, String dataContractName, String fieldName, String fieldType)
			throws UnsupportedDataTypeException {
		
		this.ctx = ctx;
		this.opt = opt;
		
		Sort sort = getSort(ctx, fieldType);
				
		values_tmp = ctx.mkArrayConst(dataContractName + "_" + fieldName + "_vals_tmp", ctx.mkIntSort(), sort);
		values = ctx.mkArrayConst(dataContractName + "_" + fieldName + "_vals", ctx.mkIntSort(), sort);
	}
	
	public void set(int componentIndex, Expr value) throws UnsupportedOperationException {
		if (closed) {
			throw new UnsupportedOperationException("Field vector already closed.");
		}
		
		values_tmp = ctx.mkStore(values_tmp, ctx.mkInt(componentIndex), value);
	}
	
	public void close() {
		opt.Add(ctx.mkEq(values, values_tmp));
		closed = true;
	}
	
	public Expr get(Expr componentIndex) {
		return ctx.mkSelect(values, componentIndex);
	}
	
	public static Sort getSort(Context ctx, String primitiveType) throws UnsupportedDataTypeException {
		if (PrimitiveTypes.BOOL.equals(primitiveType)) {
			return ctx.mkBoolSort();
		} else if (PrimitiveTypes.INT.equals(primitiveType)) {
			return ctx.mkIntSort();
		} else {
			throw new UnsupportedDataTypeException(primitiveType);
		}
	}
	
}