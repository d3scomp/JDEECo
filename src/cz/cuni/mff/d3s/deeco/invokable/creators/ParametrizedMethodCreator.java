package cz.cuni.mff.d3s.deeco.invokable.creators;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.DEECoIn;
import cz.cuni.mff.d3s.deeco.annotations.DEECoInOut;
import cz.cuni.mff.d3s.deeco.annotations.DEECoOut;
import cz.cuni.mff.d3s.deeco.exceptions.ComponentEnsembleParseException;
import cz.cuni.mff.d3s.deeco.invokable.Parameter;
import cz.cuni.mff.d3s.deeco.invokable.ParameterizedMethod;
import cz.cuni.mff.d3s.deeco.path.grammar.ParseException;
import cz.cuni.mff.d3s.deeco.processor.MethodDescription;
import cz.cuni.mff.d3s.deeco.processor.ParserHelper;

/**
 * 
 * Class used to create instance of {@link ParametrizedMethod}
 *  
 * @author alf
 *
 */
public class ParametrizedMethodCreator implements Serializable {

	private static final long serialVersionUID = -3047202665570775291L;
	
	public final List<Parameter> in;
	public final List<Parameter> inOut;
	public final List<Parameter> out;
	
	public final MethodDescription methodDesc;

	public ParametrizedMethodCreator(List<Parameter> in, List<Parameter> inOut,
			List<Parameter> out, MethodDescription methodDesc) {
		super();
		this.in = in;
		this.inOut = inOut;
		this.out = out;
		this.methodDesc = methodDesc;
	}
	
	public ParameterizedMethod extract() {
		return new ParameterizedMethod(this);
	}
	
	
	public static ParametrizedMethodCreator extractParametrizedMethodCreator(Method method) {
		return extractParametrizedMethodCreator(method, null);
	}

	public static synchronized ParametrizedMethodCreator extractParametrizedMethodCreator(Method method, String root) {
		try {
			if (method != null) {
				List<Parameter> in = ParserHelper.getParameters(method, DEECoIn.class, root);
				List<Parameter> out = ParserHelper.getParameters(method, DEECoOut.class, root);
				List<Parameter> inOut = ParserHelper.getParameters(method, DEECoInOut.class, root);
				return new ParametrizedMethodCreator(in, inOut, out, new MethodDescription(method));
			}
		} catch (ComponentEnsembleParseException pe) {
		} catch (ParseException pe) {
		}
		return null;
	}

}
