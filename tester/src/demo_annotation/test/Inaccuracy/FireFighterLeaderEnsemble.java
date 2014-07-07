package demo_annotation.test.Inaccuracy;

import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
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
			@In("coord.ffHold") Boolean ffHold,
			
			@In("member.lPos") Double lPos,
			@In("member.lSpeed") Double lSpeed,
			@In("member.lFFPos") Double lFFPos,
			@In("member.lFFSpeed") Double lFFSpeed,
			@In("member.lFFHold") Boolean lFFHold
 		){
			return true;
	}
	
	@KnowledgeExchange
	public static void map(
			@InOut("coord.ffPos") TSParamHolder<Double> ffPos,
			@InOut("coord.ffSpeed") TSParamHolder<Double> ffSpeed,
			@In("coord.ffHold") Boolean ffHold,
			
			@InOut("member.lFFPos") TSParamHolder<Double> lFFPos,
			@InOut("member.lFFSpeed") TSParamHolder<Double> lFFSpeed,
			@Out("member.lFFHold") ParamHolder<Boolean> lFFHold
	) {
		lFFPos.setWithTS(ffPos.value,ffPos.creationTime);
		lFFSpeed.setWithTS(ffSpeed.value, ffSpeed.creationTime);
		lFFHold.value = ffHold;
	}
}