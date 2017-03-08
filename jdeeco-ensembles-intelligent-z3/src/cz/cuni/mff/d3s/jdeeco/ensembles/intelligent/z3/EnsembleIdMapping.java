package cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;

import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainer;
import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainerException;
import cz.cuni.mff.d3s.jdeeco.edl.BaseDataContract;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EnsembleDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.QualifiedName;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.TypeDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.typing.IDataTypeContext;

class EnsembleIdMapping {
		
	private BaseDataContract mapping[];	
	private Class<? extends BaseDataContract> roleClass;
	
	@SuppressWarnings("unchecked")
	public EnsembleIdMapping(String packageName, EnsembleDefinition ensembleDefinition, IDataTypeContext typeResolution, KnowledgeContainer container) 
				throws ReflectiveOperationException, KnowledgeContainerException {		
		
		TypeDefinition contract = typeResolution.getDataType(ensembleDefinition.getId().getType());		
		roleClass = (Class<? extends BaseDataContract>) Class.forName(
				packageName + "." + contract.getName());
		
		// TODO Should this be untracked, i.e. are id classes readonly?
		Collection<? extends BaseDataContract> instances = container.getUntrackedKnowledgeForRole(roleClass);
		
		mapping = new BaseDataContract[instances.size()];
		
		int i = 0;
		for(BaseDataContract c : instances) {
			mapping[i] = c;
			++i;
		}
	}

	public BaseDataContract getIdClass(int localEnsembleIndex) {
		return mapping[localEnsembleIndex];				
	}
	
	public Expr getFieldExpression(Context ctx, int localEnsembleIndex, QualifiedName path) throws ReflectiveOperationException, SecurityException {
		List<String> parts = path.toParts();
		Class currentClass = roleClass;
		Field field = null;
		Object value = mapping[localEnsembleIndex];
		
		// First part of the path is the id name 
		for(int i = 1; i < parts.size(); ++i) {
			field = currentClass.getField(parts.get(i));			
			currentClass = field.getType();			
			value = field.get(value);
		}
		
		if (field == null) {
			// The id class itself is used, use its index
			return ctx.mkInt(localEnsembleIndex);
		} else {
			if (value == null)
				throw new FieldNotSetException(path.toString());
			
			if (field.getType().equals(Boolean.class)) {
				return ctx.mkBool((Boolean)value);
			} else {
				// Must be an integer
				return ctx.mkInt((Integer)value);
			}
		}
	}
}
