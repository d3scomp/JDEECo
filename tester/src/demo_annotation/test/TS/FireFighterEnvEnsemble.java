package demo_annotation.test.TS;

import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.TSParamHolder;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

@Ensemble
@PeriodicScheduling(80)
public class FireFighterEnvEnsemble {

	public final static long serialVersionUID = 1L;
	
	@Membership
	public static boolean membership(
			@In("coord.ffPos") Double ffPos,
			@In("coord.ffSpeed") Double ffSpeed, 
			@In("coord.ffGas") Double ffGas,
			@In("coord.ffBrake") Double ffBrake,

			@In("member.eFFGas") Double eFFGas,
			@In("member.eFFBrake") Double eFFBrake,
			@In("member.eFFPos") Double eFFPos,
			@In("member.eFFSpeed") Double eFFSpeed) {
		return true;
	}

	@KnowledgeExchange
	public static void map(			
			@In("coord.ffGas") Double ffGas,
			@In("coord.ffBrake") Double ffBrake,
			@InOut("coord.ffPos") TSParamHolder<Double> ffPos,
			@InOut("coord.ffSpeed") TSParamHolder<Double> ffSpeed,

			@Out("member.eFFGas") ParamHolder<Double> eFFGas,
			@Out("member.eFFBrake") ParamHolder<Double> eFFBrake,
			@InOut("member.eFFPos") TSParamHolder<Double> eFFPos,
			@InOut("member.eFFSpeed") TSParamHolder<Double> eFFSpeed
			) {

		eFFGas.value = ffGas;
		eFFBrake.value = ffBrake;
		ffPos.setWithTS(eFFPos.value, eFFPos.creationTime);
		ffSpeed.setWithTS(eFFSpeed.value, eFFSpeed.creationTime);
	}
}