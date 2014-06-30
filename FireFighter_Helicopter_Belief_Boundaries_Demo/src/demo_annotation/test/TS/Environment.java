package demo_annotation.test.TS;


import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.TimeStamp;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.TSParamHolder;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;


@Component
public class Environment {

	public String eName = "E";
 	public Double eFFGas = 0.0;    
	public Double eFFBrake = 0.0;  
 
	@TimeStamp
 	public Double eFFSpeed = 0.0;
	@TimeStamp
 	public Double eFFPos = 0.0;
	
	protected static final double TIMEPERIOD = 100;
	protected static final double SEC_NANOSECOND_FACTOR = 1000000000;
	protected static final double SEC_MILISEC_FACTOR = 1000;
	
	
	
	public Environment() {
	}	
	
	@Process 
	@PeriodicScheduling((int)TIMEPERIOD)
	public static void environmentResponse(
			@In("eFFGas") Double eFFGas,
			@In("eFFBrake") Double eFFBrake,

			@InOut("eFFPos") TSParamHolder<Double> eFFPos,
			@InOut("eFFSpeed") TSParamHolder<Double> eFFSpeed
			){
	
		double currentTime = System.nanoTime()/SEC_NANOSECOND_FACTOR;
		double timePeriodInSeconds = TIMEPERIOD/SEC_MILISEC_FACTOR;
		
  		//------------------------ FireFighter ---------------------------------------------------------------------
		double ffAcceleration = Database.getAcceleration(eFFSpeed.value, eFFPos.value, Database.fTorques, eFFGas, eFFBrake,Database.fMass);
		eFFSpeed.value += ffAcceleration * timePeriodInSeconds; 
		eFFPos.value += eFFSpeed.value * timePeriodInSeconds;
		//--------------------------------------------------------------------------------------------------------
		System.out.println("=================================== statue ==========================================");
 		System.out.println("Speed FireFighter : "+eFFSpeed.value+", pos : "+eFFPos.value+"... time :"+currentTime);
		System.out.println("==========================================================================================");
	}
}