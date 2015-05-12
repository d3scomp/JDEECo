package cz.cuni.mff.d3s.jdeeco.network.l2.strategy;

import cz.cuni.mff.d3s.deeco.annotations.Component;

@Component
public class DataSource {
	public String id;
	public Integer value = 42;
	
	public DataSource(String id) {
		this.id = id;
		this.value = 42;
	}
}