package cz.cuni.mff.d3s.jdeeco.simulation.demo;

@SuppressWarnings("serial")
public class MemberData {

	public Float temperature;

	public MemberData(Float temperature) {
		this.temperature = temperature;
	}

	@Override
	public String toString() {
		return "[temperature=" + temperature + "]";
	}
	
	

}
