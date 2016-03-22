package cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3;

import javax.activation.UnsupportedDataTypeException;

import com.microsoft.z3.ArrayExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.Sort;

import cz.cuni.mff.d3s.jdeeco.edl.PrimitiveTypes;

class KnowledgeFieldVector {
	private ArrayExpr values; // for individual components
	private Context ctx;
	
	public KnowledgeFieldVector(Context ctx, String dataContractName, String fieldName, String fieldType)
			throws UnsupportedDataTypeException {
		
		this.ctx = ctx;
		
		Sort sort;
		if (PrimitiveTypes.BOOL.equals(fieldType)) {
			sort = ctx.mkBoolSort();
		} else if (PrimitiveTypes.INT.equals(fieldType)) {
			sort = ctx.mkIntSort();
		} else {
			throw new UnsupportedDataTypeException(fieldType);
		}
		
		values = ctx.mkArrayConst(dataContractName + "_" + fieldName + "_vals", ctx.mkIntSort(), sort);
	}
	
	public void set(int componentIndex, Expr value) {
		ctx.mkStore(values, ctx.mkInt(componentIndex), value);
	}
	
	public Expr get(Expr componentIndex) {
		return ctx.mkSelect(values, componentIndex);
	}
	
}