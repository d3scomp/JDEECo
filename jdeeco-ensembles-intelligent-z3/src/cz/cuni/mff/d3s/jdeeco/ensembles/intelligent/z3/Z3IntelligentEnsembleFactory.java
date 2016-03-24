package cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.Model;
import com.microsoft.z3.Optimize;
import com.microsoft.z3.Status;
import com.microsoft.z3.enumerations.Z3_lbool;

import cz.cuni.mff.d3s.deeco.ensembles.EnsembleFactory;
import cz.cuni.mff.d3s.deeco.ensembles.EnsembleFormationException;
import cz.cuni.mff.d3s.deeco.ensembles.EnsembleInstance;
import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainer;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EdlDocument;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EnsembleDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.RoleDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.utils.ITypeResolutionContext;
import cz.cuni.mff.d3s.jdeeco.edl.utils.SimpleTypeResolutionContext;

public class Z3IntelligentEnsembleFactory implements EnsembleFactory {

	private EdlDocument edlDocument;
	private ITypeResolutionContext typeResolution;
	private Context ctx;
	private Optimize opt;
	
	public Z3IntelligentEnsembleFactory(EdlDocument edlDocument) {
		this.edlDocument = edlDocument;
		this.typeResolution = new SimpleTypeResolutionContext(edlDocument);
	}
	
	private void initConfiguration() {
		HashMap<String, String> cfg = new HashMap<String, String>();
        cfg.put("model", "true");
        ctx = new Context(cfg);
        opt = ctx.mkOptimize();
	}

	private void printModel(Model m, EnsembleAssignmentMatrix assignments, List<RoleDefinition> roles,
			DataContainer dataContainer) {
		for (int e = 0; e < assignments.getMaxEnsembleCount(); e++) {
			if (m.getConstInterp(assignments.get(e).ensembleExists()).getBoolValue() == Z3_lbool.Z3_L_TRUE) {			
				System.out.println("ensemble " + e + ":");
				for (int r = 0; r < assignments.get(e).getRoleCount(); r++) {
					RoleDefinition role = roles.get(r);
					System.out.print("  - " + role.getName() + ": [");
					String dataContractName = role.getType().toString();
					//for (int c = 0; c < dataContainer.getNumInstances(dataContractName); c++) {
					//	System.out.print(m.getConstInterp(assignments.get(e, r, c)).getBoolValue() + " ");
					//}
					ComponentAssignmentResults assignmentResults = assignments.get(e, r).getResults(m);
					for (int componentIndex : assignmentResults.getAssignedIndices()) {
						System.out.print(componentIndex + " ");
					}
				
					System.out.println("]");
				}
			} else {
				System.out.println("ensemble " + e + " not instantiated.");
			}
		}
	}
	
	private List<EnsembleInstance> createEnsembles(Model m, EnsembleAssignmentMatrix assignments, EnsembleDefinition ensembleDefinition,
			DataContainer dataContainer, String packageName) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InstantiationException, ClassNotFoundException {
		int maxEnsembleCount = assignments.getMaxEnsembleCount();
		
		// create ensembles
		List<EnsembleInstance> result = new ArrayList<>();
		List<RoleDefinition> roles = ensembleDefinition.getRoles();
		Class<?> ensembleClass = Class.forName(packageName + "." + ensembleDefinition.getName());
		for (int e = 0; e < maxEnsembleCount; e++) {
			Expr exists = m.getConstInterp(assignments.ensembleExists(e));
			if (exists.getBoolValue() == Z3_lbool.Z3_L_FALSE)
				continue;
			
			// TODO
			//EnsembleInstance ie = (EnsembleInstance) ensembleClass.getConstructor(int.class).
			EnsembleInstance ie = new PendolinoEnsemble(e + 1);

			for (int r = 0; r < assignments.get(e).getRoleCount(); r++) {
				RoleDefinition role = roles.get(r);
				Field roleField = ensembleClass.getField(role.getName());
				List<Object> list = new ArrayList<>();
				if (role.getCardinalityMax() > 1) {
					roleField.set(ie, list);
				}
				
				String dataContractName = role.getType().toString();
				ComponentAssignmentResults assignmentResults = assignments.get(e, r).getResults(m);
				boolean[] indicators = assignmentResults.getAssignedIndicators();
				for (int c = 0; c < dataContainer.getNumInstances(dataContractName); c++) {
					if (indicators[c]) {
						Object instance = dataContainer.getInstance(dataContractName, c);
						if (role.getCardinalityMax() == 1) {
							roleField.set(ie, instance);
						} else {
							list.add(instance);
						}
					}
				}
				
				result.add(ie);
			}
		}
		
		return result;
	}

	@Override
	public Collection<EnsembleInstance> createInstances(KnowledgeContainer container) throws EnsembleFormationException {
		try {
			long time_milis = System.currentTimeMillis();

			initConfiguration();

			DataContainer dataContainer = new DataContainer(ctx, opt, edlDocument.getPackage().toString(), edlDocument, container);
			
	        EnsembleDefinition ensembleDefinition = edlDocument.getEnsembles().get(0);	        
	        List<RoleDefinition> roles = ensembleDefinition.getRoles();
	        int maxEnsembleCount = dataContainer.getMaxEnsembleCount(ensembleDefinition);
						
			EnsembleAssignmentMatrix assignments = EnsembleAssignmentMatrix.create(
					ctx, opt, maxEnsembleCount, ensembleDefinition, dataContainer);
	
			assignments.createCounters();
			
			// number of rescuers is within cardinality conditions
			for (int i = 0; i < maxEnsembleCount; i++) {
				for (int j = 0; j < assignments.get(i).getRoleCount(); j++) {
					RoleDefinition roleDefinition = roles.get(j);
					BoolExpr le = ctx.mkLe(assignments.getAssignedCount(i, j), ctx.mkInt(roleDefinition.getCardinalityMax()));
					BoolExpr ge = ctx.mkGe(assignments.getAssignedCount(i, j), ctx.mkInt(roleDefinition.getCardinalityMin()));
					BoolExpr cardinalityOk = ctx.mkAnd(le, ge);
					opt.Add(ctx.mkImplies(assignments.ensembleExists(i), cardinalityOk));
					BoolExpr ensembleEmpty = ctx.mkEq(assignments.getAssignedCount(i, j), ctx.mkInt(0));
					opt.Add(ctx.mkImplies(ctx.mkNot(assignments.ensembleExists(i)), ensembleEmpty));
				}
			}

			// nonexistence of an ensemble implies nonexistence of the following ensembles (use consecutive number set from 1)
			for (int i = 1; i < maxEnsembleCount; i++) {
				opt.Add(ctx.mkImplies(ctx.mkNot(assignments.ensembleExists(i-1)), ctx.mkNot(assignments.ensembleExists(i))));
			}
			
			// assignment to one <ensemble,role> implies nonassignment to the others
			for (DataContractInstancesContainer dataContract : dataContainer.getAllDataContracts()) {
				for (int c = 0; c < dataContract.getNumInstances(); c++) {
					List<BoolExpr> assigned = new ArrayList<BoolExpr>();
					for (int e = 0; e < maxEnsembleCount; e++) {
						for (int r = 0; r < assignments.get(e).getRoleCount(); r++) {
							if (roles.get(r).getType().toString().equals(dataContract.getName()))
								assigned.add(assignments.get(e, r, c));
						}
					}
								
					opt.Add(ctx.mkOr(assigned.toArray(new BoolExpr [assigned.size()])));
					for (int j = 0; j < assigned.size(); j++) {
						BoolExpr[] assignedOthers = new BoolExpr[assigned.size()-1];
						for (int k = 0; k < assigned.size(); k++) {
							if (k < j)
								assignedOthers[k] = assigned.get(k);
							else if (k > j)
								assignedOthers[k-1] = assigned.get(k);
						}
						
						opt.Add(ctx.mkImplies(assigned.get(j), ctx.mkNot(ctx.mkOr(assignedOthers))));
					}
				}
			}
			
			for (int e = 0; e < assignments.getMaxEnsembleCount(); e++) {
				new ConstraintParser(ctx, opt, dataContainer, assignments.get(e)).parseConstraints();
			}
								
			Status status = opt.Check();
			System.out.println("Solver: " + opt);
			if (status == Status.SATISFIABLE) {
				//System.out.println("Model = " + opt.getModel());
				printModel(opt.getModel(), assignments, roles, dataContainer);
				
				List<EnsembleInstance> result = createEnsembles(opt.getModel(), assignments, ensembleDefinition, 
						dataContainer, edlDocument.getPackage().toString());
				System.out.println("Time taken (ms): " + (System.currentTimeMillis() - time_milis));
				return result;
				
			} else {
				System.out.println("Unsat :-(");
				System.out.println("Time taken (ms): " + (System.currentTimeMillis() - time_milis));
				return Collections.emptyList();
			}
			
		} catch (Exception e) {
			throw new EnsembleFormationException(e);
		}
	}

	@Override
	public int getSchedulingOffset() {
		return 0;
	}

	@Override
	public int getSchedulingPeriod() {
		return 1000;
	}

}
