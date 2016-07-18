package cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3;

import cz.cuni.mff.d3s.deeco.annotations.Role;

public class DataclassRescuer extends Rescuer {
	
	public DataclassRescuer(String id, Integer pos) {
		this.id = id;
		this.pos = pos;
		this.isLeader = false;
		this.trainId = 0;
	}
}
