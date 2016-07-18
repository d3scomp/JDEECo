package cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3;

import cz.cuni.mff.d3s.jdeeco.edl.PrimitiveTypes;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EnsembleDefinition;

public class Extensions {
	public static boolean hasDataContractBoundId(EnsembleDefinition ensemble) {
		return !ensemble.getId().getType().toString().equals(PrimitiveTypes.INT);
	}
}
