package demo_annotation.Modes;

import cz.cuni.mff.d3s.deeco.annotations.*;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.MetadataType;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.TSParamHolder;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

@ComponentModes(modes = {@ModesInfo(initMode = "initilizedLeader", 
				                    allModes = {"initilizedLeader","alarmed"})})
@StateSpaceModel(state  = {"hFFSpeed","hFFPos"}, 
				 result = @Fun(returnedIndex = {-1,0}, referenceModel = VehicleModel.class))
@Component
public class Leader {

	public String lName;

	public Double lPos = 0.0;
	public Double lSpeed = 0.0;
	public Double lGas = 0.0;
	public Double lBrake = 0.0;
	public Double lAlarm = 0.0;
	
	public Double lIntegratorSpeedError = 0.0;
	public Double lErrorWindup = 0.0;

	
	@ModeTransactions( 
			    transitions= {@ModeTransition(meta = MetadataType.INACCURACY, 
			    								fromMode = "initilizedLeader" , toMode = "alarmed" , 
			    								transitionCondition = "? > 10000"),
			    			  @ModeTransition(meta = MetadataType.INACCURACY, 
			    								fromMode = "alarmed" , toMode = "initilizedLeader" , 
			    								transitionCondition = "? < 10000")}
	) 	
	@TimeStamp
	public Double lFFPos = 0.0;


	@TimeStamp
	public Double lFFSpeed = 0.0;

	protected static final double KP = 0.05;
	protected static final double KI = 0.000228325;
	protected static final double KT = 0.01;
	protected static final double TIMEPERIOD = 100;
	protected static final double SEC_MILI_SEC_FACTOR = 1000;

	public Leader() {
		lName = "Leader";
	}


	@Process
	@PeriodicSchedulingOnModeChange(value = 100)
	public static void initilizedLeader(
			@Out("lPos") ParamHolder<Double> lPos,
			@Out("lSpeed") ParamHolder<Double> lSpeed,

			@InOut("lFFPos") TSParamHolder<Double> lFFPos,
			@InOut("lFFSpeed") TSParamHolder<Double> lFFSpeed,
			@Out("lGas") ParamHolder<Double> lGas,
			@Out("lBrake") ParamHolder<Double> lBrake,
			@Out("lAlarm") ParamHolder<Double> lAlarm,

			@Out("lIntegratorSpeedError") ParamHolder<Double> lIntegratorSpeedError,
			@Out("lErrorWindup") ParamHolder<Double> lErrorWindup			
			){
		
		lPos.value = 0.0;
		lSpeed.value = 0.0;

		lFFPos.value = 0.0;
		lFFSpeed.value = 0.0;
		lGas.value = 0.0; 
		lBrake.value = 0.0;
		lAlarm.value = 0.0;

		lIntegratorSpeedError.value = 0.0;
		lErrorWindup.value = 0.0;
	}
	
	
//	//it will have a problem because there is no parameters
//	@Process
//	@PeriodicSchedulingOnModeChange(value = 100)
//	public static void ffConnected(
//			@In("lPos") Double lPos
//			){
//		System.out.println("The FireFighter is connected");
//	}
	
	
	@Process
	@PeriodicSchedulingOnModeChange(value = 100)
	public static void alarmed(
			@Out("lAlarm") ParamHolder<Double> lAlarm			
			){
		lAlarm.value = 1.0;
		System.out.println("Alarm .......");
	}
		
	
	@Process
	@PeriodicScheduling((int) TIMEPERIOD)
	public static void speedControl(
			@In("lPos") Double lPos,
			@In("lSpeed") Double lSpeed,

			@InOut("lFFPos") TSParamHolder<Double> lFFPos,
			@InOut("lFFSpeed") TSParamHolder<Double> lFFSpeed,
			@Out("lGas") ParamHolder<Double> lGas,
			@Out("lBrake") ParamHolder<Double> lBrake,

			@InOut("lIntegratorSpeedError") ParamHolder<Double> lIntegratorSpeedError,
			@InOut("lErrorWindup") ParamHolder<Double> lErrorWindup) {

		System.out.println(" - Leader : pos "+lPos+", speed "+lSpeed+"...In the leader : - firefighter : pos "+lFFPos.value+" ,  speed "+lFFSpeed.value);
		double timePeriodInSeconds = TIMEPERIOD / SEC_MILI_SEC_FACTOR;
		double speedError = 0.0;
		lIntegratorSpeedError.value += (KI * speedError + KT * lErrorWindup.value) * timePeriodInSeconds;
		double pid = KP * speedError + lIntegratorSpeedError.value;
		lErrorWindup.value = saturate(pid) - pid;

		if (pid >= 0) {
			lGas.value = pid;
			lBrake.value = 0.0;
		} else {
			lGas.value = 0.0;
			lBrake.value = -pid;
		}
	}

	
	private static double saturate(double val) {
		if (val > 1)
			val = 1;
		else if (val < -1)
			val = -1;
		return val;
	}

}

//the values should be numerical 
//@FieldModes(DefaultMode = InitilizedLeader.class, Meta = MetadataType.INACCURATE, 
//	Transitions= {@Tran(from = InitilizedLeader.class , to = FFConnected.class , con = "10000")},
//	StateSpace = @StateSpaceModel(state = {"hFFSpeed","hFFPos"}, result = @Fun(returnedIndex =0)))

//con = @Condition(var = @Variable(value = MetadataType.INACCURATE, boundary = Boundary.SUBSTRACT , predicteTime = 0.0),
//factor = "<" , limit = "1000")),
//or we can use simple string " ? < 10000" /  "(-,0) < 10000" where ? for the value and - for the substract of max-min inaccurate value [ min and ] max

//@Tran(from ="FFConnected", to="Alarmed", con = " ? > 10000 "),
//@Tran(from ="Alarmed", to="FFConnected", con = " ? <= 10000 "),
//@Tran(from ="InitilizedLeader", to="Alarmed", con = " ? > 10000 ")})
//timestamp - inaccuracy (min,max.substarct(default)+statespacemodel)
//@Process
//public static void init(		
//		@InOut("lFFPos")  ModeParamHolder<Double> lFFPos,
//		@InOut("lFFSpeed") @TriggerOnChange ParamHolder<Double> lFFSpeed
//		){
////	lFFPos.minBoundary = lFFPos.minBoundary + 1;
////	System.out.println("min --------------- "+lFFPos.minBoundary);
//	System.out.println("inaskljdnasjdnsa&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&   "+lFFPos.value);
//	
//}