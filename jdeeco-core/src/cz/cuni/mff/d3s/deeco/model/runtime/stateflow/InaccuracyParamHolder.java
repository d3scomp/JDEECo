package cz.cuni.mff.d3s.deeco.model.runtime.stateflow;


public class InaccuracyParamHolder<T> extends TSParamHolder<T> {
	
	public final static long serialVersionUID = 1L;
	public T minBoundary;
	public T maxBoundary;


	public InaccuracyParamHolder() {
		super();
	}

	
	public InaccuracyParamHolder(InaccuracyParamHolder<T> inacc) {
		setWithInaccuracy(inacc);
	}

	public InaccuracyParamHolder(T min, T max) {
		this.minBoundary = min;
		this.maxBoundary = max;
	}

	public void setWithTS(TSParamHolder<T> e){
		super.setWithTS(e.value,e.creationTime);
		this.minBoundary = e.value;
		this.maxBoundary = e.value;
	}
	
	
	public void setWithInaccuracy(InaccuracyParamHolder<T> e){
		super.setWithTS(e.value,e.creationTime);
		this.minBoundary = e.minBoundary;
		this.maxBoundary = e.maxBoundary;
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
		if (!(obj instanceof InaccuracyParamHolder<?>))
			return false;
		if ((obj instanceof InaccuracyParamHolder<?>))
			return false;
		InaccuracyParamHolder<?> other = (InaccuracyParamHolder<?>) obj;
		boolean a= value == null ? other.value == null : value.equals(other.value);
		boolean b= creationTime == null ? other.creationTime == null : creationTime.equals(other.creationTime);
		boolean c= minBoundary == null ? other.minBoundary == null : minBoundary.equals(other.minBoundary);
		boolean d= maxBoundary == null ? other.maxBoundary == null : maxBoundary.equals(other.maxBoundary);
		return a && b ;
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
