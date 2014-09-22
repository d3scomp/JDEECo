package demo_annotation.Modes;

import cz.cuni.mff.d3s.deeco.annotations.*;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.MetadataType;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.TSParamHolder;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import demo_annotation.test.Inaccuracy.VehicleModel;



//define all the component modes with its tree structure (modes have submodes - that will help in defining the )
@ComponentModes(modes = {@Modes(initMode = "initilizedLeader", allModes = {"initilizedLeader","FFConnected","alarmed"})})
//the state space model have the in states and the out states are the index of in states or -1 to use the model and calculate the derived value
@StateSpaceModel(models = @Model(periodicScheduling = @PeriodicScheduling(100), triggerField = "lFFHold",
								 state  = {"lFFPos","lFFSpeed"}, result = @Fun(returnedIndex = {1,-1}, referenceModel = VehicleModel.class)))
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
		    transitions= {@ModeTransition(meta = MetadataType.INACCURACY, fromMode = "initilizedLeader" , toMode = "alarmed", transitionCondition = "(? + 0) > 10000"), 
		    								// MODETIMEPERIOD = 200 .... could be added to enter the periodic time 
		    								//for the mode. I should try it as code to know if it could work or not.
		    			  @ModeTransition(meta = MetadataType.INACCURACY, fromMode = "initilizedLeader" , toMode = "ffConnected", transitionCondition = "(? + 0) <= 10000"),
		    			  @ModeTransition(meta = MetadataType.INACCURACY, fromMode = "ffConnected" , toMode = "alarmed", transitionCondition = "(? + 0) > 10000"),
		    			  @ModeTransition(meta = MetadataType.INACCURACY, fromMode = "alarmed" , toMode = "ffConnected", transitionCondition = "(? + 0) <= 10000")}) 	
	@TimeStamp
	public Double lFFPos = 0.0;


	@TimeStamp
	public Double lFFSpeed = 0.0;

	protected static final double KP = 0.05;
	protected static final double KI = 0.000228325;
	protected static final double KT = 0.01;
	protected static final double TIMEPERIOD = 100;
	protected static final double MODETIMEPERIOD = 100;
	protected static final double SEC_MILI_SEC_FACTOR = 1000;

	public Leader() {
		lName = "Leader";
	}


	@Mode
	@Process
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
	
	
	//it will have a problem because there is no parameters in the process
	@Mode
	@Process
	public static void ffConnected(
			@In("lPos") Double lPos
			){
		System.out.println("The FireFighter is connected");
	}
	
	
	@Mode
	@Process
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