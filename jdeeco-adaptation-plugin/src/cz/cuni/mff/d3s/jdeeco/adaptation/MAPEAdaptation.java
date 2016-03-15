package cz.cuni.mff.d3s.jdeeco.adaptation;

/**
 * Interface for adaptation manager to control individual adaptations.
 */
public interface MAPEAdaptation {

	/**
	 * Monitor the context or inner state.
	 */
	void monitor();
	
	/**
	 * Analyze the monitored data.
	 * The provided value indicates the applicability of the adaptation,
	 * and is supposed to be within the interval of [0, 1]. 0 means
	 * no applicability of the adaptation, 1 expresses the adaptation
	 * being the most suitable in the current situation.
	 * 
	 * @return A suitability of the adaptation in the range of [0, 1].
	 */
	double analyze();
	
	/**
	 * Plan and prepare the adaptation changes.
	 */
	void plan();
	
	/**
	 * Apply the adaptation.
	 */
	void execute();
}
