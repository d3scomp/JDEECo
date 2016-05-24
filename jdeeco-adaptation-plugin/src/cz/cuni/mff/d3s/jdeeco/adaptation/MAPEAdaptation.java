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
	 * The provided value indicates the applicability of the adaptation.
	 * False means no applicability of the adaptation, True expresses
	 * the adaptation being suitable in the current situation.
	 * 
	 * @return A suitability of the adaptation.
	 */
	boolean analyze();
	
	/**
	 * Plan and prepare the adaptation changes.
	 */
	void plan();
	
	/**
	 * Apply the adaptation.
	 */
	void execute();
}
