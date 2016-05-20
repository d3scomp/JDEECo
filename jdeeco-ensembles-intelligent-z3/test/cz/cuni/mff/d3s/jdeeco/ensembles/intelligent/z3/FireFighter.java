package cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3;

import cz.cuni.mff.d3s.deeco.annotations.Role;
import cz.cuni.mff.d3s.jdeeco.edl.BaseDataContract;

@Role
public class FireFighter extends BaseDataContract {	
	
	public Integer trainId;
	
	public Integer pos;
	
	public FireFighter() {
		
	}
	
	public FireFighter(String id, Integer pos) {
		this.id = id;
		this.pos = pos;
		this.trainId = 0;
	}
}
