package cz.cuni.mff.d3s.deeco.model.runtime.stateflow;

import java.util.ArrayList;

public class ComponentModeInfo {
	
	public ComponentMode parentMode = null;
	public ComponentMode initMode = null;
	public ArrayList<ComponentMode> allModes = new ArrayList<ComponentMode>(); 
	
}
