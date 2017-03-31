package cz.cuni.mff.d3s.deeco.modes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;

/**
 * To be extended by class(es) belonging to the ModeSwitching plugin.
 * Put in the core project because it is referenced in the runtime ecore.  
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public abstract class DEECoModeChart {

	protected final ComponentInstance component;
	
	protected Set<DEECoMode> modes;
	protected DEECoMode currentMode;
	protected Set<DEECoTransition> transitions;
	
	protected Map<DEECoTransition, List<DEECoTransitionListener>> transitionListeners;
		
	public DEECoModeChart(ComponentInstance component){
		if (component == null){
			throw new IllegalArgumentException(
				String.format("The \"%s\" argument is null.", "component"));
		}
		
		this.component = component;
		modes = new HashSet<>();
		transitions = new HashSet<>();
		transitionListeners = new HashMap<>();
	}
	
	
	public ComponentInstance getComponent() {
		return component;
	}

	/*public void setComponent(ComponentInstance component) {
		this.component = component;
	}*/

	public DEECoMode getCurrentMode(){
		return currentMode;
	}
	
//	public void addTransitionListener(DEECoTransition transition,
//			ModeTransitionListener transitionListener){
//		if (transition == null) throw new IllegalArgumentException(
//				String.format("The \"%s\" argument is null.", "transition"));
//		if (transitionListener == null) throw new IllegalArgumentException(
//				String.format("The \"%s\" argument is null.", "transitionListener"));
//		
//		if(!transitionListeners.containsKey(transition)){
//			transitionListeners.put(transition, new ArrayList<>());
//		}
//		transitionListeners.get(transition).add(transitionListener);
//	}
	
	/**
	 * @return all the modes that are included in this mode chart
	 */
	public Set<DEECoMode> getModes(){
		return modes;
	}

	public Set<DEECoTransition> getTransitions(){
		return transitions;
	}
	
	public Map<DEECoTransition, List<DEECoTransitionListener>> getTransitionListeners(){
		return transitionListeners;
	}
		
	public abstract DEECoMode switchMode();
		
}
