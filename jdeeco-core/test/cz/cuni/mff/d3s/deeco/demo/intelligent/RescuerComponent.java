package cz.cuni.mff.d3s.deeco.demo.intelligent;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.PlaysRole;

@Component
@PlaysRole(Rescuer.class)
public class RescuerComponent {

	public String id;
	
	public Integer pos;
	
	public Integer trainId;
	
	public RescuerComponent(String id, Integer pos) {
		this.id = id;
		this.pos = pos;
	}

}
