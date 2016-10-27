package cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3;

import com.microsoft.z3.Expr;
import com.microsoft.z3.FuncInterp.Entry;
import com.microsoft.z3.Model;
import com.microsoft.z3.enumerations.Z3_lbool;

class ComponentAssignmentResults {

	Entry[] entries;
	int length;
	
	public ComponentAssignmentResults(Model m, ComponentAssignmentSet assignments) {
		entries = m.getFuncInterp(assignments.getAssignedSet().getFuncDecl()).getEntries();
		length = assignments.getLength();
	}
	
	public int getLength() {
		return length;
	}
	
	public boolean[] getAssignedIndicators() {
		
		boolean[] result = new boolean[getLength()];
		
		for (int c = 0; c < getLength(); c++) {
			Expr v1 = null;
			for (Entry entry : entries) {
				if (entry.getArgs()[0].toString().equals(Integer.toString(c))) {
					v1 = entry.getValue();
				}
			}
			
			if (v1 != null && v1.getBoolValue() == Z3_lbool.Z3_L_TRUE) {
				result[c] = true;
			}
		}
		
		return result;
	}
	
	public int[] getAssignedIndices() {
		int[] result = new int[getLength()];
		int numTrue = 0;
		for (Entry entry : entries) {
			if (entry.getValue().getBoolValue() == Z3_lbool.Z3_L_TRUE) {
				int index = Integer.parseInt(entry.getArgs()[0].toString());
				if (index < result.length) {
					result[numTrue] = Integer.parseInt(entry.getArgs()[0].toString());
					numTrue++;
				}
			}
		}
		
		int[] result2 = new int[numTrue];
		for (int i = 0; i < numTrue; i++) {
			result2[i] = result[i];
		}
		
		return result2;
	}

}
