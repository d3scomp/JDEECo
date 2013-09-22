package cz.cuni.mff.d3s.deeco.processor;

import static cz.cuni.mff.d3s.deeco.processor.TypeUtils.getClassFields;
import static cz.cuni.mff.d3s.deeco.processor.TypeUtils.getGenericElementType;
import static cz.cuni.mff.d3s.deeco.processor.TypeUtils.isKnowledge;
import static cz.cuni.mff.d3s.deeco.processor.TypeUtils.isList;
import static cz.cuni.mff.d3s.deeco.processor.TypeUtils.isMap;
import static cz.cuni.mff.d3s.deeco.processor.TypeUtils.isOutWrapper;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.exceptions.ParametersParseException;
import cz.cuni.mff.d3s.deeco.runtime.model.KnowledgeType;
import cz.cuni.mff.d3s.deeco.runtime.model.ListValueType;
import cz.cuni.mff.d3s.deeco.runtime.model.MapValueType;
import cz.cuni.mff.d3s.deeco.runtime.model.NestedKnowledgeDefinition;
import cz.cuni.mff.d3s.deeco.runtime.model.OutWrapperValueType;
import cz.cuni.mff.d3s.deeco.runtime.model.StructuredKnowledgeValueType;

public class ParameterKnowledgeTypeParser {

	public static KnowledgeType extractKnowledgeType(Type type)
			throws ParametersParseException {
		if (type == null) {
			throw new ParametersParseException(
					"Parsing Exception: Type is null");
		}
		final Class<?> clazz = TypeUtils.getClass(type);
		KnowledgeType result = null;
		if (isList(clazz))
			result = new ListValueType(
					extractKnowledgeType(getGenericElementType(type)[0]), clazz);
		else if (isMap(clazz))
			result = new MapValueType(
					extractKnowledgeType(getGenericElementType(type)[1]), clazz);
		else if (isOutWrapper(clazz))
			result = new OutWrapperValueType(
					extractKnowledgeType(getGenericElementType(type)[0]), clazz);
		else if (isKnowledge(clazz)) {
			Field[] fields = getClassFields(clazz);
			List<NestedKnowledgeDefinition> children = new LinkedList<>();
			for (Field f : fields)
				children.add(new NestedKnowledgeDefinition(f.getName(), false,
						extractKnowledgeType(f.getType())));
			result = new StructuredKnowledgeValueType(clazz, children);
		} else {

		}
		return result;
	}

}
