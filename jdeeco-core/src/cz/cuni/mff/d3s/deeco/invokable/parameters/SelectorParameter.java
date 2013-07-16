package cz.cuni.mff.d3s.deeco.invokable.parameters;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.exceptions.ComponentEnsembleParseException;

/**
 * A selector with an index equal to -1 is implicit, otherwise it is explicit.
 * Explicit means the selector is explicitly defined inline, as part of the parametrized method parameters.
 * This has effect on the processing of parameters when getting the method parameter values, as
 * the selector is not part of the arguments, it must be considered only for the method parameters.
 *  
 * @author Julien
 *
 */
public class SelectorParameter extends GenericParameter{

	public final String groupId;
	public List<Boolean> groupSelection;
	public final List<Parameter> groupIn;
	public final List<Parameter> groupInOut;
	public final List<Parameter> groupOut;
	
	public SelectorParameter(String groupId, List<Parameter> groupIn,
			List<Parameter> groupInOut, List<Parameter> groupOut, Type type, Integer index)
			throws ComponentEnsembleParseException {
		super(type, index);
		this.groupSelection = new ArrayList<Boolean>();
		this.groupId = groupId;
		this.groupIn = groupIn;
		this.groupInOut = groupInOut;
		this.groupOut = groupOut;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6755176485665800619L;

	
}
