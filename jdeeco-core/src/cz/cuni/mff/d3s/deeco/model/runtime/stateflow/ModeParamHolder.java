package cz.cuni.mff.d3s.deeco.model.runtime.stateflow;

import java.util.ArrayList;
import java.util.List;
import cz.cuni.mff.d3s.deeco.model.runtime.api.TransitionDefinition;



public class ModeParamHolder<T> extends InaccuracyParamHolder<T> {
	
	public final static long serialVersionUID = 1L;
	public List<TransitionDefinition> trans = new ArrayList<TransitionDefinition>();

	public ModeParamHolder() {
		super();
	}
	
	public ModeParamHolder(ModeParamHolder<T> mode) {
		setWithMode(mode);
	}
	
	public ModeParamHolder(T min, T max) {
		super(min,max);
	}
	
	public ModeParamHolder(InaccuracyParamHolder inacc) {
		setWithInaccuracy(inacc);
	}
	
	public void setWithMode(ModeParamHolder<T> v){
		this.value = v.value;
		this.creationTime = v.creationTime;
		this.maxBoundary = v.maxBoundary;
		this.minBoundary = v.minBoundary;
		this.trans.clear();
		this.trans.addAll(v.trans);
	}
	
}
