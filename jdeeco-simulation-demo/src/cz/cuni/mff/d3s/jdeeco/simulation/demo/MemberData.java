package cz.cuni.mff.d3s.jdeeco.simulation.demo;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MemberData implements Serializable {

	public Float temperature;

	public MemberData(Float temperature) {
		this.temperature = temperature;
	}

	@Override
	public String toString() {
		return "[temperature=" + temperature + "]";
	}
	
	

}
