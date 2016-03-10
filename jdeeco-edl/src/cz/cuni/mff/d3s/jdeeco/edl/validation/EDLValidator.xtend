/*
 * generated by Xtext
 */
package cz.cuni.mff.d3s.jdeeco.edl.validation

import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EnsembleDefinition
import org.eclipse.xtext.validation.Check
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.*
import java.util.Map
import java.util.HashMap
import java.util.Set
import java.util.HashSet
import com.google.inject.Inject
import cz.cuni.mff.d3s.jdeeco.edl.IFunctionRegistry
import cz.cuni.mff.d3s.jdeeco.edl.utils.ITypeResolutionContext
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.QualifiedName
import cz.cuni.mff.d3s.jdeeco.edl.utils.EDLUtils
import org.eclipse.emf.ecore.EStructuralFeature
import org.eclipse.emf.ecore.EObject
import cz.cuni.mff.d3s.jdeeco.edl.PrimitiveTypes

/**
 * Custom validation rules. 
 *
 * see http://www.eclipse.org/Xtext/documentation.html#validation
 */
class EDLValidator extends AbstractEDLValidator implements ITypeResolutionContext {	
	
	Map<String, TypeDefinition> dataTypes;
	Set<String> ensembleNames
	
	@Inject
	IFunctionRegistry registry;
	
	@Check
	def generateDocumentInfo(EdlDocument document) {
			
		dataTypes = new HashMap();
		ensembleNames = new HashSet();
		
		
		for (DataContractDefinition d : document.dataContracts) {
			if (!dataTypes.containsKey(d.name)) {
				dataTypes.put(d.name, d);
			} 
			else {
				error("Duplicate data type definition.", d, EdlPackage.Literals.TYPE_DEFINITION__NAME)
			}							
		}				
		
		for (TypeDefinition d : document.knowledgeTypes) {
			if (!dataTypes.containsKey(d.name)) {
				dataTypes.put(d.name, d);
			} 
			else {
				error("Duplicate data type definition.", d, EdlPackage.Literals.TYPE_DEFINITION__NAME)
			}							
		}
		
		for (EnsembleDefinition d : document.ensembles) {
			if (!ensembleNames.contains(d.name)) {
				ensembleNames.add(d.name);
			} 
			else {
				error("Duplicate ensemble definition.", d, EdlPackage.Literals.ENSEMBLE_DEFINITION__NAME)
			}							
		}
	}	
	
	@Check
	def validateType(TypeDefinition contract) {				
		for (FieldDeclaration d : contract.fields) {			
			switch (d.type.name) {
				case PrimitiveTypes.INT,
				case PrimitiveTypes.STRING,
				case PrimitiveTypes.FLOAT,
				case PrimitiveTypes.BOOL:
					{}
				default:
					if(!dataTypes.containsKey(d.type.name)) {
						error("Field type must be either a primitive type or an existing knowledge type.", d, EdlPackage.Literals.FIELD_DECLARATION__TYPE)						
					}
					else {
						if (dataTypes.get(d.type.name) instanceof DataContractDefinition) {
							error("Field type must be a knowledge type, not a data contract.", d, EdlPackage.Literals.FIELD_DECLARATION__TYPE)
						}
					}
			}
		}
	}
	
	@Check
	def validateEnsembleDefinition(EnsembleDefinition ensemble) {
		if (ensemble.fitness != null) {
			val type = EDLUtils.getType(this, ensemble.fitness, ensemble)
			if (!type.equals(PrimitiveTypes.INT))
				error("Fitness function must be a numeric expression.", ensemble.fitness, EdlPackage.Literals.ENSEMBLE_DEFINITION__FITNESS)
		}
		
		for (Query c : ensemble.constraints) {
			val type = EDLUtils.getType(this, c, ensemble)
			
			if (!type.equals(PrimitiveTypes.BOOL)) {
				error("Constraint must be a logical expression. - " + type, ensemble, EdlPackage.Literals.ENSEMBLE_DEFINITION__CONSTRAINTS, ensemble.constraints.indexOf(c))
			}
		}	
		
		for (AliasDefinition a : ensemble.aliases) {
			EDLUtils.getType(this, a.aliasValue, ensemble)
		}
		
		for (ExchangeRule rule : ensemble.exchangeRules) {
			var queryType = EDLUtils.getType(this, rule.query, ensemble)
			var fieldType = EDLUtils.getKnowledgeType(this, rule.field, ensemble)
			
			if (!queryType.equals(fieldType)) {
				error("Invalid assignment - field and query types do not correspond.", rule, EdlPackage.Literals.EXCHANGE_RULE__FIELD)
			}
		}
		
		for (RoleDefinition roleDefinition : ensemble.roles) {
			if (dataTypes.containsKey(roleDefinition.type.name)) {
				if (!(dataTypes.get(roleDefinition.type.name) instanceof DataContractDefinition))
					error("The type is present, but is not a data contract.", roleDefinition, EdlPackage.Literals.CHILD_DEFINITION__TYPE)				
			}			
			else
				error("This data contract is not present in the package.", roleDefinition, EdlPackage.Literals.CHILD_DEFINITION__TYPE)
		}
		
		EDLUtils.getType(this, ensemble.id.value, ensemble)		
	}
	
	override isKnownType(QualifiedName name) {
		return dataTypes.containsKey(name.name);
	}
	
	override getDataType(QualifiedName name) {
		return dataTypes.get(name.name);
	}
	
	override reportError(String message, EObject source, EStructuralFeature feature) {
		error(message, source, feature)
	}
	
	override functionRegistry() {
		return registry
	}
	
}
