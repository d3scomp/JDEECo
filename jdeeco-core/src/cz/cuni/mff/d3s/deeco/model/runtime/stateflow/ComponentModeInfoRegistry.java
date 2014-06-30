package cz.cuni.mff.d3s.deeco.model.runtime.stateflow;

import java.util.ArrayList;
import cz.cuni.mff.d3s.deeco.annotations.ModesInfo;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;


public class ComponentModeInfoRegistry {
	
	public ArrayList<ComponentModeInfo> modeInfos = new ArrayList<ComponentModeInfo>(); 
	public ArrayList<ComponentMode> modes = new ArrayList<ComponentMode>(); 
	public ComponentInstance componentInstance = null; 
	
	
	
	public void registerComponentMode(ModesInfo modeInfo){
		
//		ComponentModeInfo cmContainer = new ComponentModeInfo();
//		System.out.println(modeInfo.initMode().getSimpleName());
//		System.out.println(modeInfo.parentMode().getSimpleName());
//		for (ComponentMode cparent : modes) {
//			if(cparent.name.equals(modeInfo.parentMode().getSimpleName())){
//				cmContainer.parentMode = cparent;
//			}
//		}
//		
//		for (Class<?> mode : modeInfo.allModes()) {
//				try {
//					ComponentMode cm = (ComponentMode) mode.newInstance();
//					cm.name = mode.getSimpleName();
//					System.out.println(mode.getName());
//					if(modeInfo.initMode().getSimpleName().equals(mode.getSimpleName())){
//						cmContainer.initMode = cm;
//					}
//					modes.add(cm);
//				} catch (IllegalAccessException | InstantiationException e) {
//					System.err.println("Fail in generating new mode ..... "+e.getMessage());
//				}
//			}
//		modeInfos.add(cmContainer);
		
	}
	
	private boolean isExist(ComponentMode cm) {
		for (ComponentMode mode : modes) {
			if(mode.name.equals(cm.name))
				return true;
		}
		return false;
	}

	public ComponentMode getComponentMode(int index){
		return modes.get(index);
	}
	
	public ComponentModeInfo getComponentModeInfo(int index){
		return modeInfos.get(index);
	}
	
	
	public ComponentInstance getComponentInstance() {
		return componentInstance;
	}
	
	public void setComponentInstance(ComponentInstance componentInstance) {
		this.componentInstance = componentInstance;
	}

}
