package cz.cuni.mff.d3s.deeco.invokable.parameters;

import java.io.Serializable;
import java.lang.reflect.Type;

import cz.cuni.mff.d3s.deeco.exceptions.ComponentEnsembleParseException;
import cz.cuni.mff.d3s.deeco.invokable.ParameterTypeParser;
import cz.cuni.mff.d3s.deeco.invokable.TypeDescription;

public class GenericParameter implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 25138716843224106L;
	
	public final TypeDescription type;
	public final Integer index;
	
	public GenericParameter(Type type, Integer index) throws ComponentEnsembleParseException {
		super();
		// index equal to -1 is a different case when the knowledge exchange with group identifiers
		// in the knowledge paths is obtained implicitly from the membership selectors
		if (index != -1)
			this.type = ParameterTypeParser.parse(type);
		else this.type = null;
		this.index = index;
	}
}
