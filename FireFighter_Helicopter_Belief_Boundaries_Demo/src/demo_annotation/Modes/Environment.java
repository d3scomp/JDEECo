package demo_annotation.Modes;


import java.util.HashMap;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
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
	public Double eLGas = 0.0;    
	public Double eLBrake = 0.0;  
	public HashMap<String, Double> eHGass = new HashMap<String, Double>();
	public HashMap<String, Double> eHBrakes = new HashMap<String, Double>();

	@TimeStamp
 	public Double eFFSpeed = 0.0;
	@TimeStamp
 	public Double eFFPos = 0.0;

	public Double eLSpeed = 0.0;
 	public Double eLPos = 0.0;
	
	@TimeStamp
	public HashMap<String, Double> eHSpeeds = new HashMap<String, Double>();
	@TimeStamp
	public HashMap<String, Double> eHPoss = new HashMap<String, Double>();

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
			@In("eLGas") Double eLGas,
			@In("eLBrake") Double eLBrake,
			@In("eHGass") HashMap<String, Double> eHGass,
			@In("eHBrakes") HashMap<String, Double> eHBrakes,

			@InOut("eFFPos") TSParamHolder<Double> eFFPos,
			@InOut("eFFSpeed") TSParamHolder<Double> eFFSpeed,
			@InOut("eLPos") ParamHolder<Double> eLPos,
			@InOut("eLSpeed") ParamHolder<Double> eLSpeed,
			@InOut("eHPoss") HashMap<String, TSParamHolder<Double>> eHPoss,
			@InOut("eHSpeeds") HashMap<String, TSParamHolder<Double>> eHSpeeds
			){
	
		double currentTime = System.nanoTime()/SEC_NANOSECOND_FACTOR;
		double timePeriodInSeconds = TIMEPERIOD/SEC_MILISEC_FACTOR;
		
  		//------------------------ FireFighter ---------------------------------------------------------------------
		double ffAcceleration = Database.getAcceleration(eFFSpeed.value, eFFPos.value, Database.fTorques, eFFGas, eFFBrake,Database.fMass);
		eFFSpeed.value += ffAcceleration * timePeriodInSeconds; 
		eFFPos.value += eFFSpeed.value * timePeriodInSeconds;
  		//------------------------ Leader  ---------------------------------------------------------------------
		double lAcceleration = Database.getAcceleration(eLSpeed.value, eLPos.value, Database.fTorques, eLGas, eLBrake,Database.fMass);
		eLSpeed.value += lAcceleration * timePeriodInSeconds; 
		eLPos.value += eLSpeed.value * timePeriodInSeconds;
  		//------------------------ Helicopters  ---------------------------------------------------------------------
		for (String hName : eHPoss.keySet()) {
			double hAcceleration = Database.getAcceleration(eHSpeeds.get(hName).value, eHPoss.get(hName).value, Database.fTorques, eHGass.get(hName), eHBrakes.get(hName),Database.fMass);
			eHSpeeds.get(hName).value += hAcceleration * timePeriodInSeconds; 
			eHPoss.get(hName).value += eHSpeeds.get(hName).value * timePeriodInSeconds;
	 	}
		//--------------------------------------------------------------------------------------------------------
		System.out.println("=================================== statue ==========================================");
 		System.out.println("Speed FireFighter : "+eFFSpeed.value+", pos : "+eFFPos.value+"... time :"+currentTime);
		System.out.println("Speed Leader : "+eLSpeed.value+", pos : "+eLPos.value+"... time :"+currentTime);
		for (String hName : eHPoss.keySet()) {
			System.out.println("Speed Helicopter : "+eHSpeeds.get(hName).value+", pos : "+eHPoss.get(hName).value+"... time :"+currentTime);
		}
		System.out.println("==========================================================================================");
	}
}