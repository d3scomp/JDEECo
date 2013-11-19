package cz.cuni.mff.d3s.deeco.task;

/**
 * Provide a holder for INOUT and OUT process parameters.
 * 
 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
 *
 */
public class ParamHolder<T> {
	// FIXME TB: The class should be rather is some other package, which would contain the API 
	// for the component/ensemble developer.
	
	public T value;

	public ParamHolder() {
		super();
	}

	public ParamHolder(T value) {
		super();
		this.value = value;
	}
}
