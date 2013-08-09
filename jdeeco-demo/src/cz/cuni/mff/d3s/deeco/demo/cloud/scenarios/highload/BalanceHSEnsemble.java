package cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.highload;

import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.ENetworkId;
import cz.cuni.mff.d3s.deeco.ensemble.Ensemble;
import cz.cuni.mff.d3s.deeco.knowledge.OutWrapper;
import cz.cuni.mff.d3s.spl.core.Formula;
import cz.cuni.mff.d3s.spl.core.Result;
import cz.cuni.mff.d3s.spl.core.impl.FormulaFactory;

/**
 * The Balance Ensemble for the Highload Scenario (HS).
 * 
 * Whenever the application singleton gets deployed, and the scp component running it
 * gets overloaded, the ensemble balances the load by spawning a new scp component.
 * It detaches the overloaded node and attaches the singleton to the spawned one.
 * The load gets compared via the SPL API inside the ScpHSComponentOSLoadData object
 * from the scp component knowledge being evaluated.
 * 
 * @author Julien Malvot
 * 
 */
public class BalanceHSEnsemble extends Ensemble {
	
	private static final long serialVersionUID = 1L;
	
	@Membership
	public static Boolean membership(
			// AppComponent coordinator
			@In("coord.id") String cId,
			@In("coord.onScpId") String cOnScpId,
			@In("coord.isDeployed") Boolean cIsDeployed,
			// ScpComponent member
			@In("member.id") String mId,
			@In("member.osLoadData") ScpHSComponentOSLoadData osLoadData
			) { 
		if (cIsDeployed && cOnScpId.equals(mId)){
			// high load for higher than 50%
			Formula<Result> belowHighLoad = FormulaFactory.createLessThanConstFormula("load", 0.5);
			// bind the scp data source with the load variable
			belowHighLoad.bind("load", osLoadData);
			boolean isBelowHighload = (belowHighLoad.evaluate() == Result.TRUE);
			System.out.println("Load evaluation = " + (isBelowHighload ? "normal load" : "high load"));
			// evaluate the formula
			if (!isBelowHighload){
				// the load is too high, need to ask the Zimory Platform to start a new VM with more power
				return true;
			}
			// the load is ok, no need of anything
		}
		return false;
	}
	
	@KnowledgeExchange
	@PeriodicScheduling(1000)
	public static void map(
			// AppComponent coordinator
			@In("coord.id") String cId,
			@Out("coord.onScpId") OutWrapper<String> cOnScpId,
			// AppComponent member
			@In("member.id") String mId,
			@In("member.machineId") String machineId,
			@In("member.networkId") ENetworkId networkId,
			@InOut("member.onAppIds") OutWrapper<List<String>> onAppIds) {
		// id of the new spawned ScpComponent
		String newScpId = "IMTZimory"+mId;
		// attach the new ScpComponent to the AppComponent
		System.out.println(newScpId + " attaching " + cId);
		cOnScpId.value = newScpId;
		// detach the ScpComponent from the AppComponent
		onAppIds.value.remove(cId);
		System.out.println(mId + " detaching " + cId);
		// spawn a new component into the runtime
		ScpHSComponent newScpComponent = new ScpHSComponent(newScpId, networkId);
		newScpComponent.machineId = machineId;
		newScpComponent.onAppIds.add(cId);
		// latencies generated between the node and the others
		// but this would mean altering the knowledge of all scp components to add a new latency with this node
		// to be implemented..
		//LatencyGenerator.generate(newScpComponent, LocalLauncherHSNoJPF.scpComponents, 80, false);
		// the spawn includes 
		try {
			LocalLauncherHSNoJPF.dynamicRuntime.registerComponent(newScpComponent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(newScpId + " spawned");
	}
}
