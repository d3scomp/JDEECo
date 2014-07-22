package demo_annotation.Modes;

import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

@Ensemble
@PeriodicScheduling(80)
public class LeaderEnvEnsemble {

	@Membership
	public static boolean membership(
			@In("coord.lPos") Double lPos,
			@In("coord.lSpeed") Double lSpeed, 
			@In("coord.lGas") Double lGas,
			@In("coord.lBrake") Double lBrake,

			@In("member.eLGas") Double eLGas,
			@In("member.eLBrake") Double eLBrake,
			@In("member.eLPos") Double eLPos,
			@In("member.eLSpeed") Double eLSpeed) {
		return true;
	}

	@KnowledgeExchange
	public static void map(
			@In("coord.lGas") Double lGas,
			@In("coord.lBrake") Double lBrake,
			@Out("coord.lPos") ParamHolder<Double> lPos,
			@Out("coord.lSpeed") ParamHolder<Double> lSpeed,

			@Out("member.eLGas") ParamHolder<Double> eLGas,
			@Out("member.eLBrake") ParamHolder<Double> eLBrake,
			@In("member.eLPos") Double eLPos,
			@In("member.eLSpeed") Double eLSpeed) {

		eLGas.value = lGas;
		eLBrake.value = lBrake;
		lPos.value = eLPos;
		lSpeed.value = eLSpeed;
	}

}
