package cz.cuni.mff.d3s.jdeeco.edl.typing

import cz.cuni.mff.d3s.jdeeco.edl.typing.ITypeInformationProvider
import cz.cuni.mff.d3s.jdeeco.edl.PrimitiveTypes
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.*
import cz.cuni.mff.d3s.jdeeco.edl.ContextSymbols
import cz.cuni.mff.d3s.jdeeco.edl.utils.EDLUtils
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.impl.QualifiedNameImpl
import cz.cuni.mff.d3s.jdeeco.edl.functions.IFunctionRegistry
import java.util.Map
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.QualifiedName
import java.util.HashMap
import cz.cuni.mff.d3s.jdeeco.edl.validation.IErrorReportingService

class DefaultTypeInformationProvider implements ITypeInformationProvider {
	
	IErrorReportingService ctx;
	
	Map<String, TypeDefinition> dataTypes;
	
	IFunctionRegistry registry;
	
	new(IErrorReportingService ctx, EdlDocument document, IFunctionRegistry registry) {
		this.ctx = ctx
		this.registry = registry;
		dataTypes = new HashMap();
		
		for (DataContractDefinition d : document.dataContracts) {
			if (!dataTypes.containsKey(d.name)) {
				dataTypes.put(d.name, d);
			} 
			else {
				ctx.reportError("Duplicate data type definition: " + d.name, d, EdlPackage.Literals.TYPE_DEFINITION__NAME)
			}							
		}				
		
		for (TypeDefinition d : document.knowledgeTypes) {
			if (!dataTypes.containsKey(d.name)) {
				dataTypes.put(d.name, d);
			} 
			else {
				ctx.reportError("Duplicate data type definition: " + d.name, d, EdlPackage.Literals.TYPE_DEFINITION__NAME)
			}							
		}
	}
	
	def String getKnowledgeType(QualifiedName name, TypeDefinition type, int position) {
		if (type == null)
			return PrimitiveTypes.UNKNOWN
		
		if (position >= name.prefix.length) {			
			var FieldDeclaration f = type.fields.findFirst[it.name.equals(name.name)]
			if (f != null) {
				return f.type.name;
			}
			else {
				ctx.reportError("The specified data type " + type.name + " does not contain a field of this name: " + name.name, name, EdlPackage.Literals.QUALIFIED_NAME__NAME)
			}			
		}
		else {
			var FieldDeclaration f = type.fields.findFirst[it.name.equals(name.prefix.get(position))]
			if (f != null) {
				if(isKnownType(f.type)) {
					var nestedType = getDataType(f.type)										
					return getKnowledgeType(name, nestedType, position+1)					
				}
				else {
					if (PrimitiveTypes.isPrimitiveType(f.type.name))					
						ctx.reportError("This type is primitive and does not have any fields: " + f.type.name, name, EdlPackage.Literals.QUALIFIED_NAME__PREFIX)
				}
			}
			else {
				ctx.reportError("The specified data type "+ type.name +" does not contain a field of this name: " + name.prefix.get(position), name, EdlPackage.Literals.QUALIFIED_NAME__PREFIX)
			}
		}
		
		PrimitiveTypes.UNKNOWN;
	}
	
	override String getKnowledgeType(QualifiedName name, EnsembleDefinition ensemble) {		
		if (name.prefix.length == 0) {
			if(ensemble.id.fieldName.equals(name.name))
				return ensemble.id.type.name
			
			 var alias = ensemble.aliases.findFirst[it.aliasId.equals(name.name)]
			 if (alias != null)
			 	return getType(alias.aliasValue, ensemble)	
			 	
			 var role = ensemble.roles.findFirst[it.name.equals(name.name)]
			 if (role != null) {									
					if(role.cardinalityMax == 1)
						return role.type.name
					else
						return "set<" + role.type.name + ">";
						//ctx.reportError("Accessing a role with non-unique cardinality directly is not allowed.", name, EdlPackage.Literals.QUALIFIED_NAME__NAME) 				
			}
			else {
				ctx.reportError("An element with this name was not found in the ensemble: " + name, name, EdlPackage.Literals.QUALIFIED_NAME__NAME)
			}			 						
		}
		else {
			
			if(ensemble.id.fieldName.equals(name.prefix.findFirst[true])) {
				if(isKnownType(ensemble.id.type)) {
					return getKnowledgeType(name, getDataType(ensemble.id.type), 1);
				}
				else {
					// Reporting for this is done elsewhere, regardless of whether the id fields are used or not
					return PrimitiveTypes.UNKNOWN
				}	
			}
				
			
			var role = ensemble.roles.findFirst[it.name.equals(name.prefix.findFirst[true])]
			if (role != null) {
				if(isKnownType(role.type)) {
					var roleType = getDataType(role.type)
					
					if(role.cardinalityMax == 1)
						return getKnowledgeType(name, roleType, 1)
					else
						return "set<" + getKnowledgeType(name, roleType, 1) + ">"
				}
				else {
					// Reporting for this is done elsewhere, regardless of whether the id fields are used or not
					//ctx.reportError("Could not resolve the type name: " + role.type, role.type, EdlPackage.Literals.QUALIFIED_NAME__PREFIX)
				}				
			}
			else {
				ctx.reportError("A role with this name was not found in the ensemble: " + name.prefix.get(0), name, EdlPackage.Literals.QUALIFIED_NAME__PREFIX)
			}
		}
		
		PrimitiveTypes.UNKNOWN;
	}

	override String getType(Query query, EnsembleDefinition ensemble) {
		getType(query, ensemble, null)
	}
	
	override String getType(Query query, EnsembleDefinition ensemble, RoleDefinition role) {		
		switch(query) {
		BoolLiteral:
			PrimitiveTypes.BOOL			
		NumericLiteral:
			PrimitiveTypes.INT	
		StringLiteral:
			PrimitiveTypes.STRING
		FloatLiteral:
			PrimitiveTypes.FLOAT
		KnowledgeVariable: 
			{
				var QualifiedName name = query.path
				if(role == null) {									 
					getKnowledgeType(name, ensemble)				
				}
				else {
					var parts = name.toParts();
					
					if (parts.findFirst[true].equals(ContextSymbols.WHERE_IT_SYMBOL)) {
						getKnowledgeType(name, getDataType(role.type), 1)
					} 
					else if (parts.findFirst[true].equals(ensemble.id.fieldName)) {
						getKnowledgeType(name, ensemble)	
					}
					else {
						ctx.reportError("Only the ensemble id field and the it operator can be used in this context.", query, EdlPackage.Literals.KNOWLEDGE_VARIABLE__PATH)
						PrimitiveTypes.UNKNOWN
					}				
				}
			}
		LogicalOperator:
			{
				var String l = getType(query.left, ensemble, role)
 				var String r = getType(query.right, ensemble, role)
 				
 				if(!l.equals(PrimitiveTypes.BOOL))
 					ctx.reportError("A parameter of a logical operator must be a logical value.", query, EdlPackage.Literals.LOGICAL_OPERATOR__LEFT)
 				
 				if(!r.equals(PrimitiveTypes.BOOL))
 					ctx.reportError("A parameter of a logical operator must be a logical value.", query, EdlPackage.Literals.LOGICAL_OPERATOR__RIGHT)
 				
 				PrimitiveTypes.BOOL
 			} 			
		RelationOperator:
			{
				var String l = getType(query.left, ensemble, role);
 				var String r = getType(query.right, ensemble, role); 				
 				
 				if(!l.equals(r))
 				{
 					ctx.reportError("Both parameters of a relation must be of the same type.", query, EdlPackage.Literals.RELATION_OPERATOR__LEFT);
 				}
 				
 				if(query.type.equals(RelationOperatorType.EQUALITY) || query.type.equals(RelationOperatorType.NON_EQUALITY)) {
 					if (!(query.left instanceof EquitableQuery))
 						ctx.reportError("Parameters of this type of relation must be equitable.", query, EdlPackage.Literals.RELATION_OPERATOR__LEFT);
 				}
 				else {
 					if (!(query.left instanceof ComparableQuery))
 						ctx.reportError("Parameters of this type of relation must be comparable.", query, EdlPackage.Literals.RELATION_OPERATOR__LEFT);
 				}
 				
				PrimitiveTypes.BOOL
			}
		BinaryOperator:	
			{
				var String l = getType(query.left, ensemble, role);
 				var String r = getType(query.right, ensemble, role);
 				
 				if(!(l.equals(PrimitiveTypes.INT) || l.equals(PrimitiveTypes.FLOAT)))
 					ctx.reportError("A parameter of a binary operator must be numeric.", query, EdlPackage.Literals.BINARY_OPERATOR__LEFT)
 				
 				
 				if(!(r.equals(PrimitiveTypes.INT) || r.equals(PrimitiveTypes.FLOAT)))
 					ctx.reportError("A parameter of a binary operator must be numeric.", query, EdlPackage.Literals.BINARY_OPERATOR__RIGHT)
 				
 				
 				if(!l.equals(r))
 				{
 					ctx.reportError("Both parameters of a binary operator must be of the same numeric type.", query, EdlPackage.Literals.BINARY_OPERATOR__LEFT);
 				}
 				
				l		
			}
		AdditiveInverse:
			{
				var String inner = getType(query.nested, ensemble, role);
				
				if(!inner.equals(PrimitiveTypes.INT) && !inner.equals(PrimitiveTypes.FLOAT))
					ctx.reportError("The nested expression of a additive inverse must be a numeric expression.", query, EdlPackage.Literals.ADDITIVE_INVERSE__NESTED)								
				inner
			}
		Negation:
			{
				var String inner = getType(query.nested, ensemble, role);
				
				if(!inner.equals(PrimitiveTypes.BOOL))
					ctx.reportError("The nested expression of a negation must be logical expression. Actual type: " + inner, query, EdlPackage.Literals.NEGATION__NESTED)								
				PrimitiveTypes.BOOL
			}
		FunctionCall:
			{
				if (registry.containsFunction(query.name)) {				
					val function = registry.getFunction(query.name)
					
					val formalParams = function.parameterTypes;
					
					if (formalParams == null) {
						ctx.reportError("The function reports null as its parameters. Until this is fixed in the function definition, the function cannot be used.", 
							query, EdlPackage.Literals.FUNCTION_CALL__NAME);
							
						return PrimitiveTypes.UNKNOWN;
					}
					
					if (formalParams.length != query.parameters.length)
						ctx.reportError("Incorrect number of parameters. Expected: " + formalParams.length + " Actual:" + query.parameters.length, 
							query, EdlPackage.Literals.FUNCTION_CALL__PARAMETERS)
					else {					
						for (var int i = 0; i < formalParams.length; i++) {
							if(!EDLUtils.convertible(getType(query.parameters.get(i), ensemble, role), formalParams.get(i))) {
							ctx.reportError("The parameter types do not correspond to the expected formal parameters.", query, EdlPackage.Literals.FUNCTION_CALL__PARAMETERS)
							}							
						}					
					}
					
					val returnType = registry.getFunctionReturnType(this, ensemble, query.name, query.parameters);
					
					if (returnType != null) {
						returnType;
						
					}
					else {
						ctx.reportError("Return type of a function must not be null. Until this is fixed, the function cannot be used.", query, EdlPackage.Literals.FUNCTION_CALL__NAME);
						PrimitiveTypes.UNKNOWN;
					}	
				} 
				else {
					ctx.reportError("Unknown function name: " + query.name, query, EdlPackage.Literals.FUNCTION_CALL__NAME);
					PrimitiveTypes.UNKNOWN;
				}
			}
			
		Sum:
			{
				if (role != null) {
					ctx.reportError("Aggregation functions cannot be used in role filters.", role, EdlPackage.Literals.ROLE_DEFINITION__WHERE_FILTER)
					return PrimitiveTypes.UNKNOWN;					
				} else {				
				
					if (!ensemble.roles.exists[it.name.equals(query.collection.toString())]) {
						ctx.reportError("Aggregation functions can be used only over existing ensemble roles.", query, EdlPackage.Literals.AGGREGATION__COLLECTION);
						PrimitiveTypes.UNKNOWN
					} else {
						var RoleDefinition roleDef = ensemble.roles.findFirst[it.name.equals(query.collection.toString())];
						if (!getType(query.item, ensemble, roleDef).equals(PrimitiveTypes.INT)) {
							ctx.reportError("Sum only accepts integer items.", query, EdlPackage.Literals.AGGREGATION__COLLECTION);
							PrimitiveTypes.UNKNOWN						
						}
						else {
							PrimitiveTypes.INT
						}			
					}					
				}			
			}
					
		default:
			PrimitiveTypes.UNKNOWN	
		}
	}
	
	override String getAccessPath(QualifiedName name, EnsembleDefinition ensemble) {
		var parts = name.toParts();
		var String result;
		
		val first = parts.findFirst[true]
		
		if(ensemble.id.fieldName.equals(first))
			result = ensemble.id.fieldName
		
		var alias = ensemble.aliases.findFirst[it.aliasId.equals(first)]
		if (alias != null)
		 	result = alias.aliasId + "()"	
		 	
		var role = ensemble.roles.findFirst[it.name.equals(first)]
		if (role != null)									
			result = role.name
			
		var remainder = parts.drop(1)	
		
		if (remainder.length > 0) {
			var firstName = new QualifiedNameImpl() {};
			firstName.name = first;			
			
			if (getKnowledgeType(firstName, ensemble).startsWith("set")
			) {
				result + ".stream().map(x -> x." + String.join(".", remainder) + ").collect(Collectors.toList())";
			}
			else {
				result + "." +String.join(".", remainder)
			}
		}
		else {
			result
		}
			
	}
	
	override isKnownType(QualifiedName name) {
		return dataTypes.containsKey(name.name);
	}
	
	override getDataType(QualifiedName name) {
		return dataTypes.get(name.name);
	}
	
	override getFunctionRegistry() {
		return registry;
	}	
}