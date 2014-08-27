package demo_annotation.Modes;

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
public class FireFighterHelicopterEnsembledfds {

	@Membership
	public static boolean membership(
			@In("coord.ffPos") Double ffPos,
			@In("coord.ffSpeed") Double ffSpeed,
			@In("coord.ffHPoss") Double ffHPoss,
			@In("coord.ffHSpeeds") Double ffHSpeeds,
			
			@In("member.hPos") Double hPos,
			@In("member.hSpeed") Double hSpeed,
			@In("member.hFFPos") Double hFFPos,
			@In("member.hFFSpeed") Double hFFSpeed
 		){
		if(ffPos < 100)
			return true;
		return false;
	}
	
	@KnowledgeExchange
	public static void map(
			@InOut("coord.ffPos") TSParamHolder<Double> ffPos,
			@InOut("coord.ffSpeed") TSParamHolder<Double> ffSpeed,
			@InOut("coord.ffHPos") InaccuracyParamHolder<Double> ffHPos,
			@InOut("coord.ffHSpeed") InaccuracyParamHolder<Double> ffHSpeed,
						
			@InOut("member.hPos") TSParamHolder<Double> hPos,
			@InOut("member.hSpeed") TSParamHolder<Double> hSpeed,
			@InOut("member.hFFPos") InaccuracyParamHolder<Double> hFFPos,
			@InOut("member.hFFSpeed") InaccuracyParamHolder<Double> hFFSpeed
	) {
		ffHPos.setWithTS(hPos);
		ffHSpeed.setWithTS(hSpeed);
		hFFPos.setWithTS(ffPos);
		hFFSpeed.setWithTS(ffSpeed);
	}
}