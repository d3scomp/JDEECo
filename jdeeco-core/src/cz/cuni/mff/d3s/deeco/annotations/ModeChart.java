package cz.cuni.mff.d3s.deeco.annotations;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;

public abstract class ModeChart {

	protected ComponentInstance component;
	protected Class<? extends DEECoMode> currentMode;
		
	public ComponentInstance getComponent() {
		return component;
	}

	public void setComponent(ComponentInstance component) {
		this.component = component;
	}
	
	public abstract Class<? extends DEECoMode> findSetAndReturnCurrentMode();
	
}
