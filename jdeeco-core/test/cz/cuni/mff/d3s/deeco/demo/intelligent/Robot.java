package cz.cuni.mff.d3s.deeco.demo.intelligent;

import cz.cuni.mff.d3s.deeco.annotations.Component;

@Component
public class Robot {
	public String id;
	
	
	public Robot(String name) {
		this.id = name;
	}
	
	public String getName() {
		return id;		
	}
}
