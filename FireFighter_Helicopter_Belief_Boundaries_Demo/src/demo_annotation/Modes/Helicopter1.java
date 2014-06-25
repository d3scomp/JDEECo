package demo_annotation.Modes;

import java.util.HashMap;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.ComponentModes;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.ModeTransactions;
import cz.cuni.mff.d3s.deeco.annotations.ModeTransition;
import cz.cuni.mff.d3s.deeco.annotations.ModesInfo;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicSchedulingOnActivateMode;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.TimeStamp;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.MetadataType;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.TSParamHolder;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

@ComponentModes(modes = {@ModesInfo(initMode = "flyOff", 
							allModes = {"flyOff","flyOn"}),
						 @ModesInfo(parentMode = "flyOn", initMode = "initilizedSystem", 
							allModes = {"initilizedSystem","mediate","organizeSearch","toldToSearch"}),
						 @ModesInfo(parentMode = "organizeSearch", initMode = "initilizedSearch",
							allModes = {"initilizedSearch","otherSearch", "leadSearch"})}
)
@Component
public class Helicopter1 {

	public String  hName;
	@TimeStamp
	public Double  hPos = 0.0;
	@TimeStamp
	public Double  hSpeed = 0.0;
	public Double  hGas = 0.0;
	public Double  hBrake = 0.0;
	public Boolean hSearch = false;
	public Double  hRangeDistance = 100.0; 

	@TimeStamp
	public Double  hFFPos = 0.0;
	@TimeStamp
	public Double  hFFSpeed = 0.0;
	public Boolean hFFConnected = false;
	public Boolean hFFLost = false;
	public Double  hFFTargetPos = 0.0;
	public Double  hFFTargetSpeed = 0.0;

	//in all inaccuracy condition there is a pair of data (? + number), which means :
	// 															? is the inaccurate value till this moment
	// 															number is the inaccuracy during the future period of time
	//Those two values should return the current inaccuracy plus the predicted inaccuracy
	//the zero value could be done as default and just use ? to represent the current inaccuracy value
	@ModeTransactions( 
		    transitions= {@ModeTransition(	meta = MetadataType.INACCURACY,
		    								fromMode = "mediate" , toMode = "organizeSearch" , 
		    								transitionCondition = "(? + 0) > 5000"),
		    			  @ModeTransition(	meta = MetadataType.INACCURACY,
		    			  					fromMode = "organizeSearch" , toMode = "mediate" , 
		    								transitionCondition = "(? + 0) <= 5000"),
		    			  @ModeTransition(	meta = MetadataType.INACCURACY,
		    			  					fromMode = "organizeSearch" , toMode = "initilizedSystem" , 
		    								transitionCondition = "(? + 5) > 10000")}
	) 		
	@TimeStamp
	public HashMap<String, Double>  hHPoss = new HashMap<String, Double>();
	@TimeStamp
	public HashMap<String, Double>  hHSpeeds = new HashMap<String, Double>();

	// # in front of the field name to recognize that I should compare with the field's value
	@ModeTransactions( 
		    transitions= {@ModeTransition(	fromMode = "initilizedSystem" , toMode = "mediate" , 
		    								transitionCondition = "? == #hName"),
		    			  @ModeTransition(	fromMode = "mediate" , toMode = "initilizedSystem" , 
		    								transitionCondition = "? != #hName"),
		    			  @ModeTransition(	fromMode = "organizeSearch" , toMode = "initilizedSystem" , 
		    								transitionCondition = "? != #hName")}
	) 	
	public String MID = "";
	
	@ModeTransactions( 
		    transitions= {@ModeTransition(	fromMode = "initilizedSystem" , toMode = "toldToSearch" , 
		    								transitionCondition = "E? == #hName"),
		    			  @ModeTransition(	fromMode = "toldToSearch" , toMode = "initilizedSystem" , 
		    								transitionCondition = "V? != #hName")}
	) 	
	public HashMap<String, String> search_In = new HashMap<String, String>();
	public HashMap<String, String> search_Out = new HashMap<String, String>();
	
	@ModeTransactions( 
		    transitions= {@ModeTransition(	fromMode = "flyOff" , toMode = "flyOn" , 
		    								transitionCondition = "? == 1.0"),
		    			  @ModeTransition(	fromMode = "flyOn" , toMode = "flyOff" , 
		    								transitionCondition = "? == 0.0")}
	) 
	public Double switchSg = 0.0;
	

	@ModeTransactions( 
		    transitions= {@ModeTransition(	fromMode = "initilizedSearch" , toMode = "leadSearch" , 
		    								transitionCondition = "? == #hName"),
		    			  @ModeTransition(	fromMode = "initilizedSearch" , toMode = "otherSearch" , 
		    								transitionCondition = "? != #hName"),
		    			  @ModeTransition(	fromMode = "leadSearch" , toMode = "otherSearch" , 
		    								transitionCondition = "? != #hName"),
		    			  @ModeTransition(	fromMode = "otherSearch" , toMode = "leadSearch" , 
		    								transitionCondition = "? == #hName")}
	) 	
	
	
	public HashMap<String, Double> signal_In = new HashMap<String, Double>();
	public HashMap<String, Double> signal_Reply_Out = new HashMap<String, Double>();
	
	
	private String searchID = "";  // it is local in the simulation
	
	
	protected static double hIntegratorError = 0.0;
	protected static double hErrorWindup = 0.0;

	protected static final double KP_D = 0.193;
	protected static final double KP_S = 0.01;
	protected static final double KI_S = 0.00002;
	protected static final double KT_S = 0.00001;
	protected static final double TIMEPERIOD = 100;
	protected static final double MODETIMEPERIOD = 100;
	protected static final double SEC_MILISEC_FACTOR = 1000;
	protected static final double SEC_MILI_SEC_FACTOR = 1000;
	protected static final double SEC_NANOSEC_FACTOR = 1000000000;
	protected static final double DESIRED_DISTANCE = 0;
	protected static final double DESIRED_SPEED = 0;

	public Helicopter1() {
		hName = "H1";
	}
	
	
	@Process
	@PeriodicSchedulingOnActivateMode(value = (int) MODETIMEPERIOD)
	public static void flyOff(){
		
	}
	
	@Process
	@PeriodicSchedulingOnActivateMode(value = (int) MODETIMEPERIOD)
	public static void flyOn(){
		
	}
	
	@Process
	@PeriodicSchedulingOnActivateMode(value = (int) MODETIMEPERIOD)
	public static void initilizedSystem(){
		
	}

	@Process
	@PeriodicSchedulingOnActivateMode(value = (int) MODETIMEPERIOD)
	public static void mediate(){
		
	}
	
	@Process
	@PeriodicSchedulingOnActivateMode(value = (int) MODETIMEPERIOD)
	public static void organizeSearch(){
		
	}
	
	@Process
	@PeriodicSchedulingOnActivateMode(value = (int) MODETIMEPERIOD)
	public static void toldToSearch(){
		
	}

	@Process
	@PeriodicSchedulingOnActivateMode(value = (int) MODETIMEPERIOD)
	public static void initilizedSearch(){
		
	}
	
	@Process
	@PeriodicSchedulingOnActivateMode(value = (int) MODETIMEPERIOD)
	public static void otherSearch(){
		
	}
	
	@Process
	@PeriodicSchedulingOnActivateMode(value = (int) MODETIMEPERIOD)
	public static void leadSearch(){
		
	}
	

	@Process
	@PeriodicScheduling((int) TIMEPERIOD)
	public static void speedControl(
			@In("hPos") Double hPos,
			@In("hSpeed") Double hSpeed,
			@In("hFFPos") Double hFFPos,
			@In("hFFSpeed") Double hFFSpeed,
			@In("hFFTargetPos") Double hFFTargetPos,
			@In("hFFTargetSpeed") Double hFFTargetSpeed,

			@Out("hGas") ParamHolder<Double> hGas,
			@Out("hBrake") ParamHolder<Double> hBrake,

			@InOut("hIntegratorSpeedError") ParamHolder<Double> hIntegratorSpeedError,
			@InOut("hErrorWindup") ParamHolder<Double> hErrorWindup) {

		double timePeriodInSeconds = TIMEPERIOD / SEC_MILISEC_FACTOR;
		double distanceError = -DESIRED_DISTANCE + hFFTargetPos - hFFPos;
		double pidDistance = KP_D * distanceError;
		double error = pidDistance + hFFTargetSpeed - hFFSpeed;
		hIntegratorError += (KI_S * error + KT_S * hErrorWindup.value) * timePeriodInSeconds;
		double pidSpeed = KP_S * error + hIntegratorError;
		hErrorWindup.value = saturate(pidSpeed) - pidSpeed;

		if (pidSpeed >= 0) {
			hGas.value = pidSpeed;
			hBrake.value = 0.0;
		} else {
			hGas.value = 0.0;
			hBrake.value = -pidSpeed;
		}
	}
	
	private static double saturate(double val) {
		if (val > 1) val = 1;
		else if (val < -1) val = -1;
		return val;
	}

	private static class Pedal{
		public Double gas;
		public Double brake;
	
		public Pedal(Double gas,Double brake) {
			this.gas = gas;
			this.brake = brake;
		}	
	}

	
	private static Boolean hasToMove(InaccuracyParamHolder<Double> hFFPos,double hPos, InaccuracyParamHolder<Double> hHPos, double hRangeDistance) {
		boolean result = false;
		double distH1 = 0.0;
		double distH2 = 0.0;
		//----------------------------------------- distance between Helicopter1 and FireFighter ---------------------------------------------------------------
		if(hFFPos.minBoundary > hPos){       distH1 = hFFPos.maxBoundary - hPos;
		}else if(hPos > hFFPos.maxBoundary){	distH1 = hPos - hFFPos.minBoundary;
		}else {						distH1 = Math.max(hFFPos.maxBoundary - hPos, hPos - hFFPos.minBoundary); }
		//----------------------------------------- distance between Helicopter2 and FireFighter ---------------------------------------------------------------
		if (hHPos.minBoundary > hFFPos.maxBoundary ){	    distH2 = hHPos.maxBoundary - hFFPos.minBoundary;
		}else if(hFFPos.minBoundary > hHPos.maxBoundary){	distH2 = hFFPos.maxBoundary - hHPos.minBoundary;
		}else{							distH2 = Math.max(hHPos.maxBoundary - hFFPos.minBoundary, hFFPos.maxBoundary - hHPos.minBoundary); }
		//------------------------------------------ compare between two distances ---------------------------------------------------------------
		if(hHPos.value == 0.0){ result = true;
		}else if((distH1 < distH2) && hPos != 0.0 && hHPos.value != 0.0 && distH1 < 2*hRangeDistance ){ result = true;
		}else if((distH1 > distH2)&& hPos != 0.0 && hHPos.value != 0.0 && distH2 < 2*hRangeDistance ){	result = false;
		}else if((distH1 < distH2)&& hPos != 0.0 && hHPos.value != 0.0){	result = true;
		}else result = false;
		//---------------------------------------------------------------------------------------------------------
		return result;
	}
	
	
	
//	@Process
//	@PeriodicScheduling((int) TIMEPERIOD)
//	public static void computeTarget(
//			@In("hPos") Double hPos,
//			@In("hSpeed") Double hSpeed, 
//			@In("hRangeDistance") Double hRangeDistance,
//	
//			@In("hHPos") ParamHolder<Double> hHPos, 
//			@In("hHSpeed") ParamHolder<Double> hHSpeed,
//			@In("hHConnected") Boolean hHConnected,
//
//			@Out("hGas") ParamHolder<Double> hGas,
//			@Out("hBrake") ParamHolder<Double> hBrake,
//
//			@InOut("hSearch") ParamHolder<Boolean> hSearch,
//
//			@InOut("hFFPos") ParamHolder<Double> hFFPos, 
//			@InOut("hFFSpeed") ParamHolder<Double> hFFSpeed,
//
//			@InOut("hFFTargetPos") ParamHolder<Double> hFFTargetPos,
//			@InOut("hFFTargetSpeed") ParamHolder<Double> hFFTargetSpeed,
//
//			@InOut("hHFFConnected") ParamHolder<Boolean> hHFFConnected
//			) {
//	
//		System.out.println(" - OffloadHelicopter : pos "+hPos+", speed "+hSpeed+".."+hFFTargetSpeed.value+".In the OffloadHelicopter : - firefighter : pos "+hFFPos.value+" ,  speed "+hFFSpeed.value+" , creation time "+hFFSpeed.creationTime);
//		if(hFFSpeed.creationTime != 0.0){
//			double inaccuracy = -1;
//			if (hFFTargetPos.value != 0.0) inaccuracy = Math.max( Math.abs(hFFPos.value  - hFFPos.minBoundary), Math.abs(hFFPos.maxBoundary - hFFPos.value)); 
//			if (inaccuracy <= THRESHOLD ) { hFFLost.value = false; } else { hFFConnected.value = false; hFFLost.value = true;}
//			hFFTargetPos.value = hPos; 
//			hFFTargetSpeed.value = DESIRED_SPEED;
//
//			if(!hFFConnected.value && inaccuracy >= 0 && hHConnected){
//				hSearch.value = hasToMove(hFFPos, hPos, hHPos, hRangeDistance);
//				
//				if(hSearch.value && !hHFFConnected.value){
//					if(       hFFPos.minBoundary > hPos){ hFFTargetPos.value = hFFPos.maxBoundary;   hFFTargetSpeed.value = hFFSpeed.maxBoundary;
//					} else if(hPos > hFFPos.maxBoundary){ hFFTargetPos.value = hPos+hRangeDistance;  hFFTargetSpeed.value = DESIRED_SPEED;
//					} else {					 		  hFFTargetPos.value = hFFPos.maxBoundary;	 hFFTargetSpeed.value = hFFSpeed.maxBoundary;   }											
//					System.err.println("Move.....  from : "+hPos+"  to "+hFFTargetPos.value+",   Min pos: "+hFFPos.minBoundary+"  Max pos:"+hFFPos.maxBoundary);
//					
//				}else {
//					hFFPos.setWithInaccuracy(0.0);
//					hFFSpeed.setWithInaccuracy(0.0);
//					hFFLost.value = false;
//				}
//			}
//		}else{
//			hFFTargetPos.value = hPos; 
//			hFFTargetSpeed.value = DESIRED_SPEED;
//		}
//		System.out.println("Target : "+hFFTargetPos.value+"   "+hFFTargetSpeed.value);
//		Pedal p = speedControl(hPos, hSpeed, hFFTargetPos.value, hFFTargetSpeed.value);
//		hGas.value = p.gas;
//		hBrake.value = p.brake;
//	}
//	
//
//	@Process
//	@PeriodicScheduling((int)TIMEPERIOD)
//	public static void speedControl( Double ffPos, Double ffSpeed, Double hFFTargetPos, Double hFFTargetSpeed ) {
//
//		Pedal result = null;
//		if (hFFTargetPos == 0.0){ result = new Pedal(0.0, 0.0);
//		} else {
//			double timePeriodInSeconds = TIMEPERIOD / SEC_MILISEC_FACTOR;
//			double distanceError = -DESIRED_DISTANCE + hFFTargetPos - ffPos;
//			double pidDistance = KP_D * distanceError;
//			double error = pidDistance + hFFTargetSpeed - ffSpeed;
//			hIntegratorError += (KI_S * error + KT_S * hErrorWindup) * timePeriodInSeconds;
//			double pidSpeed = KP_S * error + hIntegratorError;
//			hErrorWindup = saturate(pidSpeed) - pidSpeed;
//			
//			if (pidSpeed >= 0) { result = new Pedal(pidSpeed, 0.0);
//			} else { 			 result = new Pedal(0.0, -pidSpeed); }
//		}
//		
//		return result;
//	}

}