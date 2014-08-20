package demo_annotation.test.HierchicalModes;


import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.*;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.InaccuracyParamHolder;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.TSParamHolder;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;


@StateSpaceModel(models = @Model( period = 100, state  = {"ffLSpeed","ffLPos"}, 
				result = @Fun(returnedIndex = {-1,0}, referenceModel = VehicleModel.class)))
@Component
public class FireFighter{

	public final static long serialVersionUID = 1L;
	public String ffName = "FF";
	@TimeStamp
	public Double ffPos = 0.0;
	@TimeStamp
	public Double ffSpeed = 0.0;
	public Double ffGas = 0.0;
	public Double ffBrake = 0.0;
	
	public Double ffIntegratorSpeedError = 0.0;
	public Double ffErrorWindup = 0.0;

	@TimeStamp
	public Double ffLPos = 0.0;
	@TimeStamp
	public Double ffLSpeed = 0.0;
	

	private static final double KP = 0.05;
	private static final double KI = 0.000228325;
	private static final double KT = 0.01;
	private static final double DISEREDSPEED = 50;
	private static final double TIMEPERIOD = 100;
	private static final double SEC_MILI_SEC_FACTOR = 1000;
	protected static final double SEC_NANOSECOND_FACTOR = 1000000000;
	
	
// 	@Process
//	public static void mediatorConnected(
//			@InOut("ffLPos") @TriggerOnValueUnchange(from = "leaderConnected", guard = "LH > 5 && LH < 15") InaccuracyParamHolder<Double> ffLPos
// 			){
////  		System.out.println("choose between the helicopters .... "+ffLPos.value+" ["+ffLPos.minBoundary+" , "+ffLPos.maxBoundary+" ] "+ffLPos.trans );
// 	}
//
//	
//// 	@Mode(init = true)
// 	@Process
//	public static void leaderConnected(
//			@InOut("ffLPos") @TriggerOnValueUnchange(guard = "LH < 1") InaccuracyParamHolder<Double> ffLPos
// 			){
//// 		double currentTime = System.nanoTime()/SEC_NANOSECOND_FACTOR;
////  		System.out.println("connected to leader .... "+ffLPos.value+" ["+ffLPos.minBoundary+" , "+ffLPos.maxBoundary+" ]  "+(currentTime-ffLPos.creationTime)+"  "+ffLPos.trans );
// 	}
// 	
// 	
// 	@Process
//	public static void nonWorking(
//			@InOut("ffLPos") @TriggerOnValueUnchange(from = "leaderConnected", guard = "LH > 6") InaccuracyParamHolder<Double> ffLPos
// 			){
////  		System.err.println("none working .... "+ffLPos.value+" ["+ffLPos.minBoundary+" , "+ffLPos.maxBoundary+" ] "+ffLPos.trans );
// 	}

 	
 	@Process
	@PeriodicScheduling((int)TIMEPERIOD)
	public static void speedControl(
			@InOut("ffPos") TSParamHolder<Double> ffPos,
			@InOut("ffSpeed") TSParamHolder<Double> ffSpeed,

			@InOut("ffGas") ParamHolder<Double> ffGas,
			@InOut("ffBrake") ParamHolder<Double> ffBrake,

			@InOut("ffIntegratorSpeedError") ParamHolder<Double> ffIntegratorSpeedError,
			@InOut("ffErrorWindup") ParamHolder<Double> ffErrorWindup
			) {
		
		double currentTime = System.nanoTime()/SEC_NANOSECOND_FACTOR;
		double timePeriodInSeconds = TIMEPERIOD / SEC_MILI_SEC_FACTOR;
		double speedError = DISEREDSPEED - ffSpeed.value; 
		ffIntegratorSpeedError.value += (KI * speedError + KT * ffErrorWindup.value) * timePeriodInSeconds;
		double pid = KP * speedError + ffIntegratorSpeedError.value;
		ffErrorWindup.value = saturate(pid) - pid;
		
		if (pid >= 0) {
			ffGas.value = pid;
			ffBrake.value = 0.0;
		} else {
			ffGas.value = 0.0;
			ffBrake.value = -pid;
		}
		
		
		double ffAcceleration = Database.getAcceleration(ffSpeed.value, ffPos.value, Database.fTorques, ffGas.value, ffBrake.value,Database.fMass);
		ffSpeed.value += ffAcceleration * timePeriodInSeconds; 
		ffPos.value += ffSpeed.value * timePeriodInSeconds;
		ffPos.creationTime = currentTime;
		ffSpeed.creationTime = currentTime;

		
//		System.err.println("=================================== FireFighter statue ==========================================");
// 		System.err.println("Speed FireFighter : "+ffSpeed.value+", pos : "+ffPos.value+"... current time :"+(currentTime-ffPos.creationTime));
// 		System.err.println("leader : pos : "+ffLPos.value+"... current time :"+(currentTime-ffLPos.creationTime));
//		System.err.println("=================================================================================================");
		
	}

	//help methods ....
	private static double saturate(double val) {
		if (val > 1)
			val = 1;
		else if (val < -1)
			val = -1;
		return val;
	}
}