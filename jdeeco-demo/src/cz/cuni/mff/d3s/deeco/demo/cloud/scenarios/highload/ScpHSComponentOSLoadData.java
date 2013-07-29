package cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.highload;

import java.util.Random;

import cz.cuni.mff.d3s.deeco.knowledge.Knowledge;
import cz.cuni.mff.d3s.spl.core.Data;
import cz.cuni.mff.d3s.spl.core.StatisticSnapshot;

public class ScpHSComponentOSLoadData extends Knowledge implements Data {
	
	public Double max;
	
	public ScpHSComponentOSLoadData() {
	}
	
	public ScpHSComponentOSLoadData(Double max) {
		this.max = max;
	}
	
	/*long nextLong(Random rng, long n) {
		   // error checking and 2^x checking removed for simplicity.
		   long bits, val;
		   do {
		      bits = (rng.nextLong() << 1) >>> 1;
		      val = bits % n;
		   } while (bits-val+(n-1) < 0L);
		   return val;
		}*/
	
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
