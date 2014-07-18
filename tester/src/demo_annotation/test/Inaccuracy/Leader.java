package demo_annotation.test.Inaccuracy;

import cz.cuni.mff.d3s.deeco.annotations.*;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.InaccuracyParamHolder;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

//The order of the attributes matters. Start with the higher differential The index -1 shou
@StateSpaceModel(models = @Model( period = 100, state  = {"lFFSpeed","lFFPos"}, 
								 result = @Fun(returnedIndex = {-1,0}, referenceModel = VehicleModel.class)))
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

	
	@TimeStamp
	public Double lFFPos = 0.0;
	@TimeStamp
	public Double lFFSpeed = 0.0;
	public Boolean lFFHold = false;

	protected static final double KP = 0.05;
	protected static final double KI = 0.000228325;
	protected static final double KT = 0.01;
	protected static final double TIMEPERIOD = 100;
	protected static final double MODETIMEPERIOD = 100;
	protected static final double SEC_MILI_SEC_FACTOR = 1000;
	protected static final double SEC_NANOSECOND_FACTOR = 1000000000;
	protected static final double SEC_MILISEC_FACTOR = 1000;

	public Leader() {
		lName = "Leader";
	}


	
	@Process
	@PeriodicScheduling(value = (int) TIMEPERIOD)
	public static void speedControl(
			@InOut("lPos") ParamHolder<Double> lPos,
			@InOut("lSpeed") ParamHolder<Double> lSpeed,

			@InOut("lFFPos") InaccuracyParamHolder<Double> lFFPos,
			@InOut("lFFSpeed") InaccuracyParamHolder<Double> lFFSpeed,
			@InOut("lFFHold") ParamHolder<Boolean> lFFHold,
			@InOut("lGas") ParamHolder<Double> lGas,
			@InOut("lBrake") ParamHolder<Double> lBrake,

			@InOut("lIntegratorSpeedError") ParamHolder<Double> lIntegratorSpeedError,
			@InOut("lErrorWindup") ParamHolder<Double> lErrorWindup) {

		double currentTime = System.nanoTime()/SEC_NANOSECOND_FACTOR;
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
		
		
		
		double lAcceleration = Database.getAcceleration(lSpeed.value, lPos.value, Database.fTorques, lGas.value, lBrake.value,Database.fMass);
		lSpeed.value += lAcceleration * timePeriodInSeconds; 
		lPos.value += lSpeed.value * timePeriodInSeconds;

		System.out.println("=================================== Leader statue ==========================================");
 		System.out.println("Speed Leader : "+lSpeed.value+", pos : "+lPos.value+"... time :"+currentTime);
 		System.out.println("Speed Leader_FireFighter : "+lFFSpeed.value+", pos : "+lFFPos.value+"... time :"+lFFPos.creationTime);
 		System.out.println("Inaccuracy Leader_FireFighter : pos : "+lFFPos.value+" E ["+lFFPos.minBoundary+" , "+lFFPos.maxBoundary+"] ... time :"+lFFPos.creationTime+" ... current time: "+currentTime+" ...  dt : "+(currentTime-lFFPos.creationTime));
		System.out.println("============================================================================================");
		lFFHold.value = !lFFHold.value;

	}

	
	private static double saturate(double val) {
		if (val > 1)
			val = 1;
		else if (val < -1)
			val = -1;
		return val;
	}

}



//the state space model have the in states and the out states are the index of in states or -1 to use the model and calculate the derived value


//
//

//double lFFAccelerationMin = Database.getAcceleration(lFFSpeed.value, lFFPos.value, Database.fTorques, 0.0, 1.0,Database.fMass);
//lFFSpeed.value += lFFAccelerationMin * timePeriodInSeconds; 
//lFFPos.value += lFFSpeed.value * timePeriodInSeconds;
//
//double lFFAccelerationMax = Database.getAcceleration(lFFSpeed.value, lFFPos.value, Database.fTorques, 1.0, 0.0,Database.fMass);
//lFFSpeed.value += lFFAccelerationMax * timePeriodInSeconds; 
//lFFPos.value += lFFSpeed.value * timePeriodInSeconds;


//@Process
//@PeriodicScheduling(value = (int) MODETIMEPERIOD)
//public static void initilizedLeader(
//		@Out("lPos") ParamHolder<Double> lPos,
//		@Out("lSpeed") ParamHolder<Double> lSpeed,
//
//		@InOut("lFFPos") TSParamHolder<Double> lFFPos,
//		@InOut("lFFSpeed") TSParamHolder<Double> lFFSpeed,
//		@Out("lGas") ParamHolder<Double> lGas,
//		@Out("lBrake") ParamHolder<Double> lBrake,
//		@Out("lAlarm") ParamHolder<Double> lAlarm,
//
//		@Out("lIntegratorSpeedError") ParamHolder<Double> lIntegratorSpeedError,
//		@Out("lErrorWindup") ParamHolder<Double> lErrorWindup			
//		){
//	
//	lPos.value = 0.0;
//	lSpeed.value = 0.0;
//
//	lFFPos.value = 0.0;
//	lFFSpeed.value = 0.0;
//	lGas.value = 0.0; 
//	lBrake.value = 0.0;
//	lAlarm.value = 0.0;
//
//	lIntegratorSpeedError.value = 0.0;
//	lErrorWindup.value = 0.0;
//}
//
//
//@Process
//@PeriodicScheduling(value = (int) MODETIMEPERIOD)
//public static void ffConnected(
//		@In("lPos") Double lPos
//		){
//	System.out.println("The FireFighter is connected");
//}
//
//
//@Process
//@PeriodicScheduling(value = (int) MODETIMEPERIOD)
//public static void alarmed(
//		@Out("lAlarm") ParamHolder<Double> lAlarm			
//		){
//	lAlarm.value = 1.0;
//	System.out.println("Alarm .......");
//}
	
