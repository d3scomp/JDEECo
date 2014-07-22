package demo_annotation.Modes;

import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.TSParamHolder;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;


@Ensemble
@PeriodicScheduling(80)
public class FireFighterLeaderEnsemble {

	@Membership
	public static boolean membership(
			@In("coord.ffPos") Double ffPos,
			@In("coord.ffSpeed") Double ffSpeed,
			
			@In("member.lPos") Double lPos,
			@In("member.lSpeed") Double lSpeed,
			@In("member.lFFPos") Double lFFPos,
			@In("member.lFFSpeed") Double lFFSpeed
 		){
			return true;
	}
	
	@KnowledgeExchange
	public static void map(
			@InOut("coord.ffPos") TSParamHolder<Double> ffPos,
			@InOut("coord.ffSpeed") TSParamHolder<Double> ffSpeed,
			
			@InOut("member.lFFPos") TSParamHolder<Double> lFFPos,
			@InOut("member.lFFSpeed") TSParamHolder<Double> lFFSpeed
	) {
		lFFPos.setWithTS(ffPos.value,ffPos.creationTime);
		lFFSpeed.setWithTS(ffSpeed.value, ffSpeed.creationTime);
	}
}