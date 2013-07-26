package cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.deployment;

import java.util.Random;

import cz.cuni.mff.d3s.deeco.knowledge.Knowledge;
import cz.cuni.mff.d3s.spl.core.Data;
import cz.cuni.mff.d3s.spl.core.StatisticSnapshot;

public class ScpDSComponentOSLatencyData extends Knowledge implements Data {
	
	/** id of one extremity of the latency link */	
	public String id1;
	/** id of the other extremity of the latency link */
	public String id2;
	/** latency value retained after generation */
	public Long cache;
	/** min possible latency value as a constraint */
	public Long min;
	/** max possible latency value */
	public Long max;
	
	public ScpDSComponentOSLatencyData() {
	}
	
	public ScpDSComponentOSLatencyData(String id1, String id2, Long min, Long max) {
		this.id1 = id1;
		this.id2 = id2;
		this.min = min;
		this.max = max;
	}
	
	long nextLong(Random rng, long n) {
		   // error checking and 2^x checking removed for simplicity.
	   long bits, val;
	   do {
	      bits = (rng.nextLong() << 1) >>> 1;
	      val = bits % n;
	   } while (bits-val+(n-1) < 0L);
	   return val;
	}
	
	public Long generate() {
		Random rand = new Random();
		cache = min+nextLong(rand, max-min);
		System.out.println(id1 + " - " + id2 + " - new value = " + cache);
		return cache;
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
		
		public LoadStatistics(Long load) {
			//mean = loads.;
		}

		@Override
		public double getArithmeticMean() {
			return mean;
		}

		@Override
		public long getSampleCount() {
			return 1;
		}

		@Override
		public long[] getSamples() {
			throw new UnsupportedOperationException("Original samples not available.");
		}
		
	}
	
	/**
	 * override the equals method to compare components from their cache
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ScpDSComponentOSLatencyData){
			ScpDSComponentOSLatencyData toCompare = (ScpDSComponentOSLatencyData) obj;
			// same source id and destination id means equal latency data
	        return (this.id1.equals(toCompare.id1) && this.id2.equals(toCompare.id2));
	    }
	    return false;
	}
	
	/**
	 * need for component comparison
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}
}

