package cz.cuni.mff.d3s.jdeeco.edl.typing;

import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EnsembleDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.QualifiedName;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.Query;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.RoleDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.TypeDefinition;

public interface ITypeInformationProvider extends IDataTypeContext {	
	String getKnowledgeType(QualifiedName name, EnsembleDefinition ensemble);
	String getType(Query query, EnsembleDefinition ensemble);
	String getType(Query query, EnsembleDefinition ensemble, RoleDefinition role);
	String getAccessPath(QualifiedName name, EnsembleDefinition ensemble);	
}
