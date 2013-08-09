package cz.cuni.mff.d3s.deeco.demo.cloud.candidates;

import java.util.Random;

import cz.cuni.mff.d3s.deeco.knowledge.Knowledge;
import cz.cuni.mff.d3s.spl.core.Data;
import cz.cuni.mff.d3s.spl.core.StatisticSnapshot;

public class CandidateSPLComponentOSLatencyData extends Knowledge implements Data {

	/** id field just for verbose */
	public String id1;
	/** id field just for verbose */
	public String id2;
	/** maximum latency */
	public Double max;
	/** minimum latency */
	public Double min;
	
	public CandidateSPLComponentOSLatencyData() {
	}
	
	public CandidateSPLComponentOSLatencyData(String id1, String id2, Double min, Double max) {
		this.id1 = id1;
		this.id2 = id2;
		this.min = min;
		this.max = max;
	}
	
	public Long generate() {
		Random rand = new Random();
		Long value = (long) (min + rand.nextDouble()*(max-min));
		System.out.print(value);
		return value;
	}
	
	/**
	 * Generates an array of samples with a random size and with random measures within the min-max interval.
	 * @return
	 */
	public long[] generateSamples(){
		Random rand = new Random();
		// between 5 and 10 samples
		int size = 5+rand.nextInt(5);
		long[] generatedSamples = new long[size];
		System.out.println("fromId/toId=" + id1+ "/" + id2);
		System.out.print("values=");
		for (int i = 0; i < generatedSamples.length; i++){
			generatedSamples[i] = generate();
			System.out.print(" ");
		}
		System.out.println("");

		return generatedSamples;
	}

	@Override
	public StatisticSnapshot getStatisticSnapshot() {
		return new LoadStatistics(generateSamples());
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
		public double mean;
		public long[] samples;
		
		public LoadStatistics(long[] samples) {
			this.samples = samples;
			long sum = 0;
			for (int i = 0; i < samples.length; i++)
				sum += samples[i];
			this.mean = sum/samples.length;
			System.out.println("mean="+this.mean);
		}

		@Override
		public double getArithmeticMean() {
			return mean;
		}

		@Override
		public long getSampleCount() {
			return samples.length;
		}

		@Override
		public long[] getSamples() {
			return samples;
		}
		
	}
}
