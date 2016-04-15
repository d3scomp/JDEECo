package cz.cuni.mff.d3s.jdeeco.edl.utils

import cz.cuni.mff.d3s.jdeeco.edl.model.edl.*
import java.util.List
import java.util.Collections
import cz.cuni.mff.d3s.jdeeco.edl.PrimitiveTypes
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.impl.QualifiedNameImpl

class EDLUtils {
	def static String stripSet(String setType) {
		if (setType.startsWith("set"))
			setType.subSequence(4, setType.length-1).toString()
		else
			PrimitiveTypes.UNKNOWN;
	}
	
		
	def static boolean convertible(String src, String target) {
		if (src == null || target == null)
		 	return false
		
		if (src.equals(target))
			return true;
			
		if (target.startsWith("set")) {
			if (target.equals("set"))
				return true;
			
			var String subType = stripSet(target) 			
			
			if (convertible(src, subType)) { 
				return true;				
			}
		}
			
		return false;
	}
	
	def static String getKnowledgeType(ITypeResolutionContext ctx, QualifiedName name, TypeDefinition type, int position) {
		if (position >= name.prefix.length) {			
			var FieldDeclaration f = type.fields.findFirst[it.name.equals(name.name)]
			if (f != null) {
				return f.type.name;
			}
			else {
				ctx.reportError("The specified data type does not contain a field of this name.", name, EdlPackage.Literals.QUALIFIED_NAME__NAME)
			}			
		}
		else {
			var FieldDeclaration f = type.fields.findFirst[it.name.equals(name.prefix.get(position))]
			if (f != null) {
				if(ctx.isKnownType(f.type)) {
					var nestedType = ctx.getDataType(f.type)										
					return getKnowledgeType(ctx, name, nestedType, position+1)					
				}
				else {
					ctx.reportError("A data type with this name was not found in the package.", name, EdlPackage.Literals.QUALIFIED_NAME__PREFIX)
				}
			}
			else {
				ctx.reportError("The specified data type does not contain a field of this name.", name, EdlPackage.Literals.QUALIFIED_NAME__PREFIX)
			}
		}
		
		PrimitiveTypes.UNKNOWN;
	}
	
	def static String getKnowledgeType(ITypeResolutionContext ctx, QualifiedName name, EnsembleDefinition ensemble) {		
		if (name.prefix.length == 0) {
			if(ensemble.id.fieldName.equals(name.name))
				return ensemble.id.type.name
			
			 var alias = ensemble.aliases.findFirst[it.aliasId.equals(name.name)]
			 if (alias != null)
			 	return cz.cuni.mff.d3s.jdeeco.edl.utils.EDLUtils.getType(ctx, alias.aliasValue, ensemble)	
			 	
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
			var role = ensemble.roles.findFirst[it.name.equals(name.prefix.findFirst[true])]
			if (role != null) {
				if(ctx.isKnownType(role.type)) {
					var roleType = ctx.getDataType(role.type)
					
					if(role.cardinalityMax == 1)
						return getKnowledgeType(ctx, name, roleType, 1)
					else
						return "set<" + getKnowledgeType(ctx, name, roleType, 1) + ">"
				}
				else {
					ctx.reportError("Could not resolve the name: " + role.type, role.type, EdlPackage.Literals.QUALIFIED_NAME__PREFIX)
				}				
			}
			else {
				ctx.reportError("A role with this name was not found in the ensemble: " + name, name, EdlPackage.Literals.QUALIFIED_NAME__PREFIX)
			}
		}
		
		PrimitiveTypes.UNKNOWN;
	}

	def static String getType(ITypeResolutionContext ctx, Query query, EnsembleDefinition ensemble) {
		getType(ctx, query, ensemble, null)
	}
	
	def static String getType(ITypeResolutionContext ctx, Query query, EnsembleDefinition ensemble, RoleDefinition role) {		
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
					getKnowledgeType(ctx, name, ensemble)				
				}
				else {
					var parts = name.toParts();
					
					if (parts.findFirst[true].equals("it")) {
						getKnowledgeType(ctx, name, ctx.getDataType(role.type), 1)
					} 
					else if (parts.findFirst[true].equals(ensemble.id.fieldName)) {
						getKnowledgeType(ctx, name, ensemble)	
					}
					else {
						ctx.reportError("Only the ensemble id field and the it operator can be used in the where filter.", query, EdlPackage.Literals.KNOWLEDGE_VARIABLE__PATH)
					}
							
					""				
				}
			}
		LogicalOperator:
			{
				var String l = cz.cuni.mff.d3s.jdeeco.edl.utils.EDLUtils.getType(ctx, query.left, ensemble, role)
 				var String r = cz.cuni.mff.d3s.jdeeco.edl.utils.EDLUtils.getType(ctx, query.right, ensemble, role)
 				
 				if(!l.equals(PrimitiveTypes.BOOL))
 					ctx.reportError("A parameter of a logical operator must be a logical value.", query, EdlPackage.Literals.LOGICAL_OPERATOR__LEFT)
 				
 				if(!r.equals(PrimitiveTypes.BOOL))
 					ctx.reportError("A parameter of a logical operator must be a logical value.", query, EdlPackage.Literals.LOGICAL_OPERATOR__RIGHT)
 				
 				PrimitiveTypes.BOOL
 			} 			
		RelationOperator:
			{
				var String l = cz.cuni.mff.d3s.jdeeco.edl.utils.EDLUtils.getType(ctx, query.left, ensemble, role);
 				var String r = cz.cuni.mff.d3s.jdeeco.edl.utils.EDLUtils.getType(ctx, query.right, ensemble, role); 				
 				
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
				var String l = cz.cuni.mff.d3s.jdeeco.edl.utils.EDLUtils.getType(ctx, query.left, ensemble, role);
 				var String r = cz.cuni.mff.d3s.jdeeco.edl.utils.EDLUtils.getType(ctx, query.right, ensemble, role);
 				
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
				var String inner = cz.cuni.mff.d3s.jdeeco.edl.utils.EDLUtils.getType(ctx, query.nested, ensemble, role);
				
				if(!inner.equals(PrimitiveTypes.INT) && !inner.equals(PrimitiveTypes.FLOAT))
					ctx.reportError("The nested expression of a additive inverse must be a numeric expression.", query, EdlPackage.Literals.ADDITIVE_INVERSE__NESTED)								
				inner
			}
		Negation:
			{
				var String inner = cz.cuni.mff.d3s.jdeeco.edl.utils.EDLUtils.getType(ctx, query.nested, ensemble, role);
				
				if(!inner.equals(PrimitiveTypes.BOOL))
					ctx.reportError("The nested expression of a negation must be logical expression. Actual type: " + inner, query, EdlPackage.Literals.NEGATION__NESTED)								
				PrimitiveTypes.BOOL
			}
		FunctionCall:
			{
				if (ctx.functionRegistry.containsFunction(query.name)) {				
					val function = ctx.functionRegistry.getFunction(query.name)
					
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
							if(!convertible(getType(ctx, query.parameters.get(i), ensemble, role), formalParams.get(i))) {
							ctx.reportError("The parameter types do not correspond to the expected formal parameters.", query, EdlPackage.Literals.FUNCTION_CALL__PARAMETERS)
							}							
						}					
					}
					
					val returnType = ctx.functionRegistry.getFunctionReturnType(ctx, ensemble, query.name, query.parameters);
					
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
					
		default:
			PrimitiveTypes.UNKNOWN	
		}
	}
	
	def static String getAccessPath(ITypeResolutionContext ctx, QualifiedName name, EnsembleDefinition ensemble) {
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
			
			if (getKnowledgeType(ctx, firstName, ensemble).startsWith("set")
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
	
	def static String getJavaTypeName(String type) {
		switch type {
			case PrimitiveTypes.STRING:
				"String"
			case PrimitiveTypes.BOOL:
				"boolean"
			default:
				type
		}			
	}	
}