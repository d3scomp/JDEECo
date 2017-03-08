package cz.cuni.mff.d3s.jdeeco.edl.generator;

import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EnsembleDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.FunctionCall;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.KnowledgeVariable;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.Query;
import cz.cuni.mff.d3s.jdeeco.edl.typing.ITypeInformationProvider;
import cz.cuni.mff.d3s.jdeeco.edl.utils.EDLUtils;
import cz.cuni.mff.d3s.jdeeco.edl.utils.ToStringVisitor;

public class CodeGeneratorVisitor extends ToStringVisitor {
	
	private EnsembleDefinition parentEnsemble;
	private ITypeInformationProvider typing;

	public CodeGeneratorVisitor(ITypeInformationProvider typing, EnsembleDefinition parentEnsemble) {
		
		this.parentEnsemble = parentEnsemble;
		this.typing = typing;
	}
	
	@Override
	public String visit(KnowledgeVariable query) {		
		return typing.getAccessPath(query.getPath(), parentEnsemble);
	}
	
	@Override
	public String visit(FunctionCall query) {
		StringBuilder builder = new StringBuilder();		
		
		builder.append("(" + 
		EDLUtils.getJavaTypeName(typing.getFunctionRegistry().getFunctionReturnType(typing, parentEnsemble, query.getName(), query.getParameters().toArray(new Query[]{}))) 
		+") new " + query.getName() + "().evaluate(");
		
		for (Query p : query.getParameters()) {
			builder.append(p.accept(this));
		}
		
		builder.append(")");
		return builder.toString();
	}
}
