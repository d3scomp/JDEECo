package cz.cuni.mff.d3s.deeco.invokable.parameters;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.exceptions.ComponentEnsembleParseException;

/**
 * A selector parameter is a parameter which identifies a group of parameters within a process method. 
 * This is the root of the parameters identified by its identifier.
 * If its index equals to -1, then the selector is implicitly defined in the method. It is logically retrieved from
 * the membership method, entry point of declaration. 
 * Otherwise it is explicitly defined in the method.
 * By explicitly, one means the selector is explicitly defined inline, as part of the parametrized method parameters.
 *  
 * @author Julien Malvot
 *
 */
public class SelectorParameter extends GenericParameter{
	
	private static final long serialVersionUID = 6755176485665800619L;
	
	/** Identifier of the selector supplied by the selector annotation. 
	 * Each sub parameter in groupIn/groupInOut/groupOut has a knowledge path with the groupId explicitly declared as follow : "members.groupId.*"
	 * where the star is the relative knowledge path of the component to be selected */
	public final String groupId;
	/** List of booleans defining the selections.
	 * (each item is by default true as a selected item) 
	 * */
	public List<Boolean> groupSelection;
	/** group of input parameters, similar to input parameters of the abstract membership method, 
	 * but owned by the selector parameter */
	public final List<Parameter> groupIn;
	/** group of input/output parameters, similar to input/output parameters of the abstract membership method, 
	 * but owned by the selector parameter */
	public final List<Parameter> groupInOut;
	/** group of output parameters, similar to output parameters of the abstract membership method, 
	 * but owned by the selector parameter */
	public final List<Parameter> groupOut;
	
	/**
	 * 
	 * @param groupId identifier of the selector
	 * @param groupIn identified input parameters
	 * @param groupInOut identified input/output parameters
	 * @param groupOut identified output parameters
	 * @param type type of the parameter
	 * @param index index of the parameter
	 * @throws ComponentEnsembleParseException exception when the parsing fails
	 */
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
}
