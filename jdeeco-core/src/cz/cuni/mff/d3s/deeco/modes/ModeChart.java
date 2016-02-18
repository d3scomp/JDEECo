package cz.cuni.mff.d3s.deeco.modes;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;

/**
 * To be extended by class(es) belonging to the ModeSwitching plugin.
 * Put in the core project because it is referenced in the runtime ecore.  
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 */
public abstract class ModeChart {

	protected ComponentInstance component;
	protected Class<? extends DEECoMode> currentMode;
	protected Map<Class<? extends DEECoMode>, Map<Class<? extends DEECoMode>,
				List<ModeTransitionListener>>> transitionListeners;
		
	public ComponentInstance getComponent() {
		return component;
	}

	public void setComponent(ComponentInstance component) {
		this.component = component;
	}

	public Class<? extends DEECoMode> getCurrentMode(){
		return currentMode;
	}
	
	/**
	 * @return all the modes that are included in this mode chart
	 */
	public abstract Set<Class<? extends DEECoMode>> getModes();
		
	public abstract Class<? extends DEECoMode> switchMode();
	
}
