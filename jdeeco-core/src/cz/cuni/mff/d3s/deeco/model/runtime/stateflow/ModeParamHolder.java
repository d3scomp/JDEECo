package cz.cuni.mff.d3s.deeco.model.runtime.stateflow;

import java.util.ArrayList;

import delete.Modes;



public class ModeParamHolder<T> extends InaccuracyParamHolder<T> {
	
	public final static long serialVersionUID = 1L;
	public Modes lastMode = null;
	public MetadataType meta = MetadataType.EMPTY;
	public ArrayList<ComponentModeTransition> transitions = new ArrayList<ComponentModeTransition>();

	public ModeParamHolder() {
		super();
	}
	
}
