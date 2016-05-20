package cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3;

import cz.cuni.mff.d3s.deeco.annotations.Role;
import cz.cuni.mff.d3s.jdeeco.edl.BaseDataContract;

@Role
public class Rescuer extends BaseDataContract {
	
	public Integer pos;
	
	public Boolean isLeader;
	
	public Integer trainId;
	
	public Rescuer() {
		
	}
	
	public Rescuer(String id, Integer pos) {
		this.id = id;
		this.pos = pos;
		this.isLeader = false;
		this.trainId = 0;
	}
}
