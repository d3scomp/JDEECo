package cz.cuni.mff.d3s.deeco.modes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.search.StateSpaceSearch;

/**
 * To be extended by class(es) belonging to the ModeSwitching plugin.
 * Put in the core project because it is referenced in the runtime ecore.  
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public abstract class ModeChart {

	protected ComponentInstance component;
	protected Class<? extends DEECoMode> currentMode;
	protected Map<Class<? extends DEECoMode>, Map<Class<? extends DEECoMode>,
				List<ModeTransitionListener>>> transitionListeners;
	/**
	 * Unless set to null this search engine enables non-deterministic mode switching.
	 */
	protected StateSpaceSearch stateSpaceSearch = null;
		
	public ComponentInstance getComponent() {
		return component;
	}

	public void setComponent(ComponentInstance component) {
		this.component = component;
	}

	public Class<? extends DEECoMode> getCurrentMode(){
		return currentMode;
	}
	
	public void setStateSpaceSearch(StateSpaceSearch stateSpaceSearch){
		this.stateSpaceSearch = stateSpaceSearch;
	}
	
	public StateSpaceSearch getStateSpaceSearch(){
		return stateSpaceSearch;
	}
	
	public void addTransitionListener(Class<? extends DEECoMode> from,
			Class<? extends DEECoMode> to,
			ModeTransitionListener transitionListener){
		if (from == null) throw new IllegalArgumentException(
				String.format("The \"%s\" argument is null.", "from"));
		if (to == null) throw new IllegalArgumentException(
				String.format("The \"%s\" argument is null.", "to"));
		if (transitionListener == null) throw new IllegalArgumentException(
				String.format("The \"%s\" argument is null.", "transitionListener"));
		
		if(!transitionListeners.containsKey(from)){
			transitionListeners.put(from, new HashMap<>());
		}
		Map<Class<? extends DEECoMode>, List<ModeTransitionListener>> fromTransitions =
				transitionListeners.get(from);
		if(!fromTransitions.containsKey(to)){
			fromTransitions.put(to, new ArrayList<>());
		}
		List<ModeTransitionListener> transition = fromTransitions.get(to);
		transition.add(transitionListener);
	}
	
	/**
	 * @return all the modes that are included in this mode chart
	 */
	public abstract Set<Class<? extends DEECoMode>> getModes();
		
	public abstract Class<? extends DEECoMode> switchMode();
		
}
