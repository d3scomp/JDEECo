package cz.cuni.mff.d3s.deeco.integrity;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;

/**
 * 
 * @author Ondřej Štumpf
 *
 */
public class RatingsManagerFactoryImpl {
	
	public RatingsManager create(String id, ComponentInstance component) {
		return new RatingsManagerImpl(id, component);
	}
	
}
