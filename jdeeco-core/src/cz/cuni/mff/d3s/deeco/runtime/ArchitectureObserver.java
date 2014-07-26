package cz.cuni.mff.d3s.deeco.runtime;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;

/**
 * <p>
 * To be registered for callbacks in cases when updates to the architecture
 * model are needed.
 * </p>
 * <p>
 * The architecture model contains both local and remote component instances,
 * with references to the runtime model counterparts, when applicable (because
 * local component instances have runtime counterparts, remote do not have any).
 * </p>
 * <p>
 * It also contains the "established" ensemble instances with references to (i)
 * the ensemble definition and (ii) the architecture component instances that
 * took the role of coordinator and member in each individual case.
 * </p>
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 * 
 */
public interface ArchitectureObserver {

	/**
	 *  Called from EnsembleTask when a membership condition passes.
	 */
	void ensembleFormed(EnsembleDefinition e, ComponentInstance controller, String coordID, String memberID);
}
