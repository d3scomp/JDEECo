package demo_annotation.Modes;

import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.*;

import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.MetadataType;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.TSParamHolder;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;


@ComponentModes(modes = {@ModesInfo(initMode = "inactive", 
				allModes = {"inactive","mediatorConnected"})})
@StateSpaceModel(state  = {"hFFSpeed","hFFPos"}, 
				result = @Fun(returnedIndex = {-1,0}, referenceModel = VehicleModel.class))
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

	public Double ffMID = 0.0;
//	public Double ffActive = 0.0;
	public Double ffConn = 0.0;
	@ModeTransactions( 
		    transitions= {@ModeTransition(meta = MetadataType.EMPTY, 
		    								fromMode = "inactive" , toMode = "mediatorConnected" , 
		    								transitionCondition = "E? > 1"),
		    			  @ModeTransition(meta = MetadataType.EMPTY, 
		    								fromMode = "mediatorConnected" , toMode = "inactive" , 
		    								transitionCondition = "V? > 1")}
	) 	
	public Double[] ffHconn = {0.0,0.0,0.0};
	
	private static final double KP = 0.05;
	private static final double KI = 0.000228325;
	private static final double KT = 0.01;
	private static final double DISEREDSPEED = 50;
	private static final double TIMEPERIOD = 100;
	private static final double SEC_MILI_SEC_FACTOR = 1000;


	@Process
	@PeriodicSchedulingOnModeChange(value = 100, entry = {@Code(field = {@Field(name = "ffGas", value = "0.0"),
																		@Field(name = "ffBrake", value = "0.0"),
																		@Field(name = "ffIntegratorSpeedError", value = "0.0"),
																		@Field(name = "ffErrorWindup", value = "0.0"),
																		@Field(name = "ffMID", value = "0.0"),
																		@Field(name = "ffConn", value = "0.0"),
																		@Field(name = "ffHconn", value = "{0.0}")})}) //@Method("methodName", parms, return)???
	public static void inactive(
			@Out("ffPos") TSParamHolder<Double> ffPos,
			@Out("ffSpeed") TSParamHolder<Double> ffSpeed,

			@Out("ffGas") ParamHolder<Double> ffGas,
			@Out("ffBrake") ParamHolder<Double> ffBrake,

			@Out("ffIntegratorSpeedError") ParamHolder<Double> ffIntegratorSpeedError,
			@Out("ffErrorWindup") ParamHolder<Double> ffErrorWindup,

			@Out("ffMID") ParamHolder<Double> ffMID,

			@Out("ffConn") ParamHolder<Double> ffConn,
			@Out("ffHconn") ParamHolder<Double>[] ffHconn
			){
	}
	
	
//	@Process
//	@PeriodicSchedulingOnModeChange(value = 100)
//	public static void Active(
//			@Out("ffActive") ParamHolder<Double> ffActive
//			){
//		System.out.println("Active firefighter ... ");
//	}
//	
//	
//	
//	@Process
//	@PeriodicSchedulingOnModeChange(value = 100)
//	public static void Wait(
//			@Out("ffConn") ParamHolder<Double> ffConn,
//			@Out("ffHconn") ParamHolder<Double>[] ffHconn
//			){
//		
//	}
//
//	
//	@Process
//	@PeriodicSchedulingOnModeChange(value = 100)
//	public static void LeaderConnected(
//			@Out("ffConn") ParamHolder<Double> ffConn
//			){
//		
//	}

	@Process
	@PeriodicSchedulingOnModeChange(value = 100, entry = {@Code(field = {@Field(name = "ffMID", value = "0.0")})}, 
												 exit  = {@Code(field = {@Field(name = "ffMID", value = "0.0")})}) //@Method("methodName", parms, return)???
	public static void mediatorConnected(
			@InOut("ffMID") ParamHolder<Double> ffMID,
			@InOut("ffHconn") ParamHolder<Double>[] ffHconn
			){
		Double[] arr = {0.0,0.0,0.0};
		for (int i = 0; i< ffHconn.length; i++) {
			arr[i] = ffHconn[i].value;
		}
		ffMID.value = MIDValue(arr);
	}

	
	@Process
	@PeriodicScheduling((int)TIMEPERIOD)
	public static void FFPos_Out(
			@InOut("ffPos") TSParamHolder<Double> ffPos,
			@InOut("ffSpeed") TSParamHolder<Double> ffSpeed,

			@Out("ffGas") ParamHolder<Double> ffGas,
			@Out("ffBrake") ParamHolder<Double> ffBrake,

			@InOut("ffIntegratorSpeedError") ParamHolder<Double> ffIntegratorSpeedError,
			@InOut("ffErrorWindup") ParamHolder<Double> ffErrorWindup
			) {
		
		System.out.println(" - FireFighter : pos "+ffPos.value+", speed : "+ffSpeed.value+"   "+ffPos.creationTime);
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
		
	}

	//help methods ....
	private static double saturate(double val) {
		if (val > 1)
			val = 1;
		else if (val < -1)
			val = -1;
		return val;
	}	
	
	
	private static double MIDValue(Double[] ffHconn){
		if(ffHconn[0] > 0)
			return 1.0;
		else if(ffHconn[1] > 0)
			return 2.0;
		else if(ffHconn[2] > 0)
			return 3.0;
		
		return 0.0;
	}
}