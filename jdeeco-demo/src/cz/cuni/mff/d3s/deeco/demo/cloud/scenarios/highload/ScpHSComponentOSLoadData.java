package cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.highload;

import java.util.Random;

import cz.cuni.mff.d3s.deeco.knowledge.Knowledge;
import cz.cuni.mff.d3s.spl.core.Data;
import cz.cuni.mff.d3s.spl.core.StatisticSnapshot;

/**
 * The Science Cloud Platform (Scp) Component Operating System Load Data for the Highload Scenario (HS).
 * 
 * The LoadData object performs the load generation for a scp component. evaluates
 * In the membership, the loads are compared with a condition involving a max upper bound via the SPL API.
 * 
 * @author Julien Malvot
 * 
 */
public class ScpHSComponentOSLoadData extends Knowledge implements Data {
	
	public Double max;
	
	public ScpHSComponentOSLoadData() {
	}
	
	public ScpHSComponentOSLoadData(Double max) {
		this.max = max;
	}
	
	public Double generate() {
		Random rand = new Random();
		Double value = rand.nextDouble()*max;
		System.out.println("load = " + value);
		return value;
	}

	@Override
	public StatisticSnapshot getStatisticSnapshot() {
		return new LoadStatistics(generate());
	}

	@Override
	public void addValue(long when, long value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void __clear() {
		/* Do nothing. */
	}
	
	private class LoadStatistics implements StatisticSnapshot {
		private double mean;
		
		public LoadStatistics(double load) {
			mean = load;
		}

		@Override
		public double getArithmeticMean() {
			return mean;
		}

		@Override
		public long getSampleCount() {
			return 1000; // FIXME
		}

		@Override
		public long[] getSamples() {
			throw new UnsupportedOperationException("Original samples not available.");
		}
		
	}
}
