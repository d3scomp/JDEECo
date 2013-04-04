package cz.cuni.mff.d3s.deeco.invokable;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.exceptions.ComponentEnsembleParseException;
import cz.cuni.mff.d3s.deeco.knowledge.TypeUtils;

public class ParameterTypeParser {

	public static TypeDescription parse(Type type) throws ComponentEnsembleParseException {
		if (type == null) {
			throw new ComponentEnsembleParseException("Parsing Exception: Type is null");
		}
		
		final Class<?> resultClazz = TypeUtils.getClass(type);
		TypeDescription[] resultParametricTypes = null;
		Map<String, TypeDescription> resultKnowledgeStructure = null;
			
		if (TypeUtils.isGenericType(type)) {
			Type[] parametricTypes = TypeUtils.getGenericElementType(type);
			
			resultParametricTypes = new TypeDescription[parametricTypes.length]; 
			for (int i = 0; i < parametricTypes.length; i++) {
				resultParametricTypes[i] = parse(parametricTypes[i]);
			}
		} else if (TypeUtils.isKnowledge(resultClazz)) {
			resultKnowledgeStructure = new HashMap<String, TypeDescription>();
			Field [] fields = TypeUtils.getClassFields(resultClazz);
			if (fields == null)
				throw new ComponentEnsembleParseException("Parsing exception: Empty knowledge structure");
			for (Field f : fields)  {
				resultKnowledgeStructure.put(f.getName(), parse(f.getGenericType()));
			}
		}
		
		return new TypeDescription(resultClazz, resultParametricTypes, resultKnowledgeStructure);
	}

}
