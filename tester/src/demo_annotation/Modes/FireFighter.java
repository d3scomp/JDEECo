package demo_annotation.Modes;

import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.*;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.TSParamHolder;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;


@ComponentModes(modes = {@ModesInfo(initMode = "inactive", allModes = {"inactive","active"}),
						 @ModesInfo(parentMode = "active", initMode = "waitConnection", allModes = {"waitConnection","leaderConnected","mediatorConnected"})})
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
	@ModeTransactions( 
		    transitions= {@ModeTransition(fromMode = "inactive" , toMode = "active" , transitionCondition = "? == 1.0"),
		    			  @ModeTransition(fromMode = "active" , toMode = "inactive" , transitionCondition = "? == 0.0")}) 	
	public Double ffActive = 0.0;
	@ModeTransactions( 
		    transitions= {@ModeTransition(fromMode = "waitConnection" , toMode = "leaderConnected" , transitionCondition = "? > 0"),
		    			  @ModeTransition(fromMode = "leaderConnected" , toMode = "waitConnection" , transitionCondition = "? == 0")}) 	
	public Double ffConn = 0.0;
	
	//FIXME: should be another way to deal with that
	//Since it is array. I already used "E" to represent there is one element at least (instead of || ) satisfies the condition
	//and "V" for all the values in the array (instead of && )
	//*what if we had the conditions more detailed???
	@ModeTransactions( 
		    transitions= {@ModeTransition(fromMode = "waitConnection" , toMode = "mediatorConnected",	transitionCondition = "E? > 0"),
		    			  @ModeTransition(fromMode = "mediatorConnected" , toMode = "waitConnection",  transitionCondition = "V? == 0")}) 	
	public Double[] ffHconn = {0.0,0.0,0.0};
	public String modeName = "";
	
	private static final double KP = 0.05;
	private static final double KI = 0.000228325;
	private static final double KT = 0.01;
	private static final double DISEREDSPEED = 50;
	private static final double TIMEPERIOD = 100;
	protected static final double MODETIMEPERIOD = 100;
	protected static final double MODETIMEPERIOD_MIN = 100;
	protected static final double MODETIMEPERIOD_MAX = 500;
	private static final double SEC_MILI_SEC_FACTOR = 1000;

	
	
	
	@Mode
	@Process
	@PeriodicScheduling(value = (int) MODETIMEPERIOD)
	public static void inactive(
			@In("modeName") String modeName,
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
		ffGas.value = 0.0;
		ffBrake.value = 0.0;
		ffIntegratorSpeedError.value = 0.0;
		ffErrorWindup.value = 0.0;
		ffMID.value = 0.0;
		ffConn.value = 0.0;
		for (int i = 0; i < ffHconn.length; i++) {
			ffHconn[i].value = 0.0;
		} 
	}

	
	
//Maybe just delete those empty modes would be a good idea	
	
	@Mode
	@Process
	@PeriodicScheduling(value = (int) MODETIMEPERIOD)
	public static void active(){
		//FIXME: what I should put here???? ffPos = ffPos_In
	}
	
	
	
	@Mode
	@Process
	@PeriodicScheduling(value = (int) MODETIMEPERIOD)
	public static void waitConnection(){
		//FIXME: what I should put here????
	}

	
	
	@Mode
	@Process
	@PeriodicScheduling(value = (int) MODETIMEPERIOD)
	public static void leaderConnected(){
		//FIXME: what I should put here????
	}

	
	//[min,max] possible range for periodic scheduling of the process is what the guys suggested in their breakout_group in RELATE meeting. 
	//So, I think I should leave it for them then. Isn't it?? 
	@Mode
	@Process
	@PeriodicScheduling(value_min = (int) MODETIMEPERIOD_MIN, value_max = (int) MODETIMEPERIOD_MAX)
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

	
	
	//could be triggered by Aspectj code before starting any process except exit and itself
	@Entry
	public static void entry(
			@In("modeName") String modeName, //FIXME: should be deleted and triggered from the runtime
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
		
		switch (modeName) {
		
		case "inactive":
			ffGas.value = 0.0;
			ffBrake.value = 0.0;
			ffIntegratorSpeedError.value = 0.0;
			ffErrorWindup.value = 0.0;
			ffMID.value = 0.0;
			ffConn.value = 0.0;
			for (int i = 0; i < ffHconn.length; i++) {
				ffHconn[i].value = 0.0;
			} 
			break;
			
		case "mediatorConnected":
			ffMID.value = 0.0;
			
		default:
			break;
		}
		
	}
	

	//could be triggered by aspectj code after finishing any process except exit and itself
	@Exit
	public static void exit(
			@In("modeName") String modeName,//FIXME: should be deleted and triggered from the runtime
			@Out("ffMID") ParamHolder<Double> ffMID
			){
		
		switch (modeName) {
		
		case "mediatorConnected":
			ffMID.value = 0.0;
			break;

		default:
			break;
		}
		
	}
	
	
	//FIXME: "value_min = (int) MODETIMEPERIOD_MIN, value_max = (int) MODETIMEPERIOD_MAX" should be here
	@Process
	@PeriodicScheduling((int)TIMEPERIOD)
	public static void speedControl(
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