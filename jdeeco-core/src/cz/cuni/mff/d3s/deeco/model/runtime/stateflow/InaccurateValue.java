package cz.cuni.mff.d3s.deeco.model.runtime.stateflow;

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;

public class InaccurateValue {

	public KnowledgePath path;
	public Number value;
	public Number minBoundary;
	public Number maxBoundary;
	public Double creationTime;
	
	
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
