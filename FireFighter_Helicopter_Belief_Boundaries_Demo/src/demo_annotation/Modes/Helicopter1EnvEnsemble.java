package demo_annotation.Modes;


import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

//NOT MODIFIED YET
@Ensemble
public class Helicopter1EnvEnsemble {

	@Membership
	public static boolean membership(
			@In("coord.hPos") Double hPos,
			@In("coord.hSpeed") Double hSpeed,
			@In("coord.hGas") Double hGas,
			@In("coord.hBrake") Double hBrake,
			@In("coord.hHFFConnected") Boolean hHFFConnected,
				
			@In("member.eHGas") Double eHGas,
			@In("member.eHBrake") Double eHBrake,
			@In("member.eHPos") Double eHPos,
			@In("member.eHSpeed") Double eHSpeed
		){
			return true;
	}
	
	@KnowledgeExchange
	@PeriodicScheduling(50)
	public static void map(
			@Out("coord.hPos")  ParamHolder<Double> hPos,
			@Out("coord.hSpeed")  ParamHolder<Double> hSpeed,
			@In("coord.hGas") Double hGas,
			@In("coord.hBrake") Double hBrake,
		
			@Out("member.eHGas")  ParamHolder<Double> eHGas,
			@Out("member.eHBrake")  ParamHolder<Double> eHBrake,
			@In("member.eHPos") Double eHPos,
			@In("member.eHSpeed") Double eHSpeed
	) {
	
		eHGas.value = hGas;
		eHBrake.value = hBrake;
		hPos.value = eHPos;
		hSpeed.value = eHSpeed;
	}
}