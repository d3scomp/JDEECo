package cz.cuni.mff.d3s.deeco.model.runtime.stateflow;

import cz.cuni.mff.d3s.deeco.task.ParamHolder;


public class TSParamHolder<T> extends ParamHolder<T> {
	
	public final static long serialVersionUID = 1L;
	public Double creationTime = 0.0;
	

	public TSParamHolder() {
		super();
	}

	public TSParamHolder(TSParamHolder<T> v) {
		this.value = v.value;
		this.creationTime = v.creationTime;
	}

	public TSParamHolder(T value, Double creationTime) {
		super(value);
		this.creationTime = creationTime;
	}
	
	public void setWithTS(T value, Double creationTime){
		this.value = value;
		this.creationTime = creationTime;
	}

	public Double getCreationTime() {
		return creationTime;
	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof TSParamHolder<?>)){
			return this.value == (T)obj;
		}if ((obj instanceof TSParamHolder<?>)){
			TSParamHolder<?> other = (TSParamHolder<?>) obj;
			boolean a= value == null ? other.value == null : value.equals(other.value);
			boolean b= creationTime == null ? other.creationTime == null : creationTime.equals(other.creationTime);
			return a && b;
			}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return value == null ? 0 : value.hashCode();
	}

}
