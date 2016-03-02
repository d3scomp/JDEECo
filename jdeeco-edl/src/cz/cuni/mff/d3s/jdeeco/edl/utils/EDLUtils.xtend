package cz.cuni.mff.d3s.jdeeco.edl.utils

import cz.cuni.mff.d3s.jdeeco.edl.model.edl.*

class EDLUtils {	
	def static String getKnowledgeType(ITypeResolutionContext ctx, QualifiedName name, TypeDefinition type, int position) {
		if (position >= name.prefix.length) {			
			var FieldDeclaration f = type.fields.findFirst[it.name.equals(name.name)]
			if (f != null) {
				return f.type.name.toLowerCase();
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
		
		"unknown"
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
					return role.type.name				
			}
			else {
				ctx.reportError("An element with this name was not found in the ensemble.", name, EdlPackage.Literals.QUALIFIED_NAME__NAME)
			}			 						
		}
		else {
			var role = ensemble.roles.findFirst[it.name.equals(name.prefix.findFirst[true])]
			if (role != null) {
				if(ctx.isKnownType(role.type)) {
					var roleType = ctx.getDataType(role.type)
					return getKnowledgeType(ctx, name, roleType, 1)
					
				}
				else {
					ctx.reportError("Could not resolve the name.", role.type, EdlPackage.Literals.QUALIFIED_NAME__PREFIX)
				}				
			}
			else {
				ctx.reportError("A role with this name was not found in the ensemble.", name, EdlPackage.Literals.QUALIFIED_NAME__PREFIX)
			}
		}
		
		"unknown"	
	}
	
	def static String getType(ITypeResolutionContext ctx, Query query, EnsembleDefinition ensemble) {		
		switch(query) {
		BoolLiteral:
			"bool"			
		NumericLiteral:
			"int"			
		StringLiteral:
			"string"
		FloatLiteral:
			"float"
		KnowledgeVariable: 
			{
				var QualifiedName name = query.path				 
				getKnowledgeType(ctx, name, ensemble)
			}
		LogicalOperator:
			{
				var String l = cz.cuni.mff.d3s.jdeeco.edl.utils.EDLUtils.getType(ctx, query.left, ensemble)
 				var String r = cz.cuni.mff.d3s.jdeeco.edl.utils.EDLUtils.getType(ctx, query.right, ensemble)
 				
 				if(!l.equals("bool"))
 					ctx.reportError("A parameter of a logical operator must be a logical value.", query, EdlPackage.Literals.LOGICAL_OPERATOR__LEFT)
 				
 				if(!r.equals("bool"))
 					ctx.reportError("A parameter of a logical operator must be a logical value.", query, EdlPackage.Literals.LOGICAL_OPERATOR__RIGHT)
 				
 				"bool"
 			} 			
		RelationOperator:
			{
				var String l = cz.cuni.mff.d3s.jdeeco.edl.utils.EDLUtils.getType(ctx, query.left, ensemble);
 				var String r = cz.cuni.mff.d3s.jdeeco.edl.utils.EDLUtils.getType(ctx, query.right, ensemble); 				
 				
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
 				
				"bool"
			}
		BinaryOperator:	
			{
				var String l = cz.cuni.mff.d3s.jdeeco.edl.utils.EDLUtils.getType(ctx, query.left, ensemble);
 				var String r = cz.cuni.mff.d3s.jdeeco.edl.utils.EDLUtils.getType(ctx, query.right, ensemble);
 				
 				if(!(l.equals("int") || l.equals("float")))
 					ctx.reportError("A parameter of a binary operator must be numeric.", query, EdlPackage.Literals.BINARY_OPERATOR__LEFT)
 				
 				
 				if(!(r.equals("int") || r.equals("float")))
 					ctx.reportError("A parameter of a binary operator must be numeric.", query, EdlPackage.Literals.BINARY_OPERATOR__RIGHT)
 				
 				
 				if(!l.equals(r))
 				{
 					ctx.reportError("Both parameters of a binary operator must be of the same numeric type.", query, EdlPackage.Literals.BINARY_OPERATOR__LEFT);
 				}
 				
				l		
			}
		AdditiveInverse:
			{
				var String inner = cz.cuni.mff.d3s.jdeeco.edl.utils.EDLUtils.getType(ctx, query.nested, ensemble);
				
				if(!inner.equals("int") && !inner.equals("float"))
					ctx.reportError("The nested expression of a additive inverse must be a numeric expression.", query, EdlPackage.Literals.ADDITIVE_INVERSE__NESTED)								
				inner
			}
		Negation:
			{
				var String inner = cz.cuni.mff.d3s.jdeeco.edl.utils.EDLUtils.getType(ctx, query.nested, ensemble);
				
				if(!inner.equals("bool"))
					ctx.reportError("The nested expression of a negation must be logical expression.", query, EdlPackage.Literals.NEGATION__NESTED)								
				"bool"
			}
		FunctionCall:
		// TODO: Check function calls, needs info on available functions and their typing.
			
			"goo"			
		default:
			"unknown"
			
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
		
		result + String.join(".", remainder)
	}
}