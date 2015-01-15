package cz.cuni.mff.d3s.deeco.integrity;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;

/**
 * 
 * @author Ondřej Štumpf
 *
 */
public class RatingsManagerImpl implements RatingsManager {
	private final ComponentInstance component;
	private final String id;

	public RatingsManagerImpl(String id, ComponentInstance component) {
		this.id = id;
		this.component = component;
	}
}
