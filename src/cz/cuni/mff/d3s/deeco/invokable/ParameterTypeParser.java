package cz.cuni.mff.d3s.deeco.invokable;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;

import cz.cuni.mff.d3s.deeco.exceptions.ComponentEnsembleParseException;
import cz.cuni.mff.d3s.deeco.knowledge.TypeUtils;

public class ParameterTypeParser {

	public static ParameterType parse(Type type) throws ComponentEnsembleParseException {
		if (type != null) {
			ParameterType result = new ParameterType();
			result.clazz = TypeUtils.getClass(type);
			if (TypeUtils.isGenericType(type)) {
				Type[] parametricTypes = TypeUtils.getGenericElementType(type);
				result.parametricTypes = new ParameterType[parametricTypes.length];
				for (int i = 0; i < parametricTypes.length; i++) {
					result.parametricTypes[i] = parse(parametricTypes[i]);
				}
			} else if (TypeUtils.isKnowledge(result.clazz)) {
				result.knowledgeStructure = new HashMap<String, ParameterType>();
				Field [] fields = TypeUtils.getClassFields(result.clazz);
				if (fields == null)
					throw new ComponentEnsembleParseException("Parsing exception: Empty knowledge structure");
				for (Field f : fields)  {
					result.knowledgeStructure.put(f.getName(), parse(f.getGenericType()));
				}
			}
			return result;
		}
		throw new ComponentEnsembleParseException("Parsing Exception: Type is null");
	}

}
