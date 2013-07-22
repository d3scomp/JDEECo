package cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.highload;

import java.util.List;
import java.util.Random;

public class LoadGenerator {

	Random rand = new Random();
	
	/**
	 * default constructor for SPL init
	 */
	public LoadGenerator() {
	}
	
	public Long generate() {
		return ((Integer) rand.nextInt(100)).longValue();
	}
}
