package demo_annotation.test.HierchicalModes;

import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.InaccuracyParamHolder;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.TSParamHolder;


@Ensemble
@PeriodicScheduling(80)
public class FireFighterLeaderEnsemble {

	@Membership
	public static boolean membership(
			@In("coord.ffPos") Double ffPos,
			@In("coord.ffSpeed") Double ffSpeed,
			@In("coord.ffLPos") Double ffLPos,
			@In("coord.ffLSpeed") Double ffLSpeed,
			
			@In("member.lPos") Double lPos,
			@In("member.lSpeed") Double lSpeed,
			@In("member.lFFPos") Double lFFPos,
			@In("member.lFFSpeed") Double lFFSpeed
 		){
		if(lPos < 50)
			return true;
		return false;
	}
	
	@KnowledgeExchange
	public static void map(
			@InOut("coord.ffPos") TSParamHolder<Double> ffPos,
			@InOut("coord.ffSpeed") TSParamHolder<Double> ffSpeed,
			@InOut("coord.ffLPos") InaccuracyParamHolder<Double> ffLPos,
			@InOut("coord.ffLSpeed") InaccuracyParamHolder<Double> ffLSpeed,
			
			@InOut("member.lPos") TSParamHolder<Double> lPos,
			@InOut("member.lSpeed") TSParamHolder<Double> lSpeed,
			@InOut("member.lFFPos") InaccuracyParamHolder<Double> lFFPos,
			@InOut("member.lFFSpeed") InaccuracyParamHolder<Double> lFFSpeed
	) {
		lFFPos.setWithTS(ffPos);
		lFFSpeed.setWithTS(ffSpeed);
		ffLPos.setWithTS(lPos);
		ffLSpeed.setWithTS(lSpeed);
	}
}