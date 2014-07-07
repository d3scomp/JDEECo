package cz.cuni.mff.d3s.deeco.model.runtime.stateflow;

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;

public class InaccurateValueDefinition<T> {

	public KnowledgePath path;
	public Number value;
	public Number minBoundary;
	public Number maxBoundary;
	public Double creationTime;

	
	
	public InaccurateValueDefinition() {
	}
	
	public InaccurateValueDefinition(Number value){
		this.value = value;
	}
	
	public InaccurateValueDefinition(KnowledgePath kp, TSParamHolder<T> value){
		this.path = kp;
		TSParamHolder<T> obj = new TSParamHolder<T>(value);
		this.value = (Number) obj.value;
		this.minBoundary = (Number) obj.value;
		this.maxBoundary = (Number) obj.value;
		this.creationTime = obj.creationTime;
	}
	
	public InaccurateValueDefinition(InaccurateValueDefinition<T> value){
		this.path = value.path;
		this.value = value.value;
		this.minBoundary = value.minBoundary;
		this.maxBoundary = value.maxBoundary;
		this.creationTime = value.creationTime;
	}
	
	
	public void init(){
		this.minBoundary = this.value;
		this.maxBoundary = this.value;		
	}

	public void setPath(KnowledgePath path) {
		this.path = path;
	}
	
	public void setCreationTime(Double creationTime) {
		this.creationTime = creationTime;
	}
	
	public void setValue(Number value) {
		this.value = value;
	}
	
	public void setMaxBoundary(Number maxBoundary) {
		this.maxBoundary = maxBoundary;
	}
	
	public void setMinBoundary(Number minBoundary) {
		this.minBoundary = minBoundary;
	}
	
	public KnowledgePath getPath() {
		return path;
	}
	
	public Number getValue() {
		return value;
	}
	
	public Double getCreationTime() {
		return creationTime;
	}
	
	public Number getMaxBoundary() {
		return maxBoundary;
	}
	
	public Number getMinBoundary() {
		return minBoundary;
	}

}
