package demo_annotation.Modes;

import java.util.HashMap;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.ComponentModes;
import cz.cuni.mff.d3s.deeco.annotations.Entry;
import cz.cuni.mff.d3s.deeco.annotations.Exit;
import cz.cuni.mff.d3s.deeco.annotations.Fun;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.Mode;
import cz.cuni.mff.d3s.deeco.annotations.ModeTransactions;
import cz.cuni.mff.d3s.deeco.annotations.ModeTransition;
import cz.cuni.mff.d3s.deeco.annotations.ModesInfo;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.StateSpaceModel;
import cz.cuni.mff.d3s.deeco.annotations.Model;
import cz.cuni.mff.d3s.deeco.annotations.TimeStamp;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.InaccuracyParamHolder;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.MetadataType;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.TSParamHolder;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

@ComponentModes(modes = {@ModesInfo(initMode = "flyOff", allModes = {"flyOff","flyOn"}),
						 @ModesInfo(parentMode = "flyOn", initMode = "initilizedSystem", allModes = {"initilizedSystem","mediate","organizeSearch","toldToSearch"}),
						 @ModesInfo(parentMode = "organizeSearch", initMode = "initilizedSearch", allModes = {"initilizedSearch","otherSearch", "leadSearch"})})
@StateSpaceModel(models= {@Model(state  = {"hFFSpeed","hFFPos"}, 
				 				 result = @Fun(returnedIndex = {-1,0}, referenceModel = VehicleModel.class)),
				 		 @Model(state  = {"hHSpeed","hHPos"}, 
				 		 	    result = @Fun(returnedIndex = {-1,0}, referenceModel = VehicleModel.class))})
@Component
public class Helicopter1 {

	public String  hID;
	@TimeStamp
	public Double  hPos = 0.0;
	@TimeStamp
	public Double  hSpeed = 0.0;
	public Double  hGas = 0.0;
	public Double  hBrake = 0.0;
	public Double  hRangeDistance = 100.0; 
	//in all inaccuracy condition there is a pair of data (? + number), which means :
	// 															? is the inaccurate value till this moment
	// 															number is the inaccuracy during the future period of time
	//Those two values should return the current inaccuracy plus the predicted inaccuracy
	//the zero value could be done as default and just use ? to represent the current inaccuracy value
	@ModeTransactions( 
		    transitions= {@ModeTransition(meta = MetadataType.INACCURACY,fromMode = "mediate", toMode = "organizeSearch",transitionCondition = "(? + 0) > 5000"),
		    			  @ModeTransition(meta = MetadataType.INACCURACY,fromMode = "organizeSearch", toMode = "mediate",transitionCondition = "(? + 0) <= 5000"),
		    			  @ModeTransition(meta = MetadataType.INACCURACY,fromMode = "organizeSearch", toMode = "initilizedSystem",transitionCondition = "(? + #protocolDuration) > 10000")}) 		
	@TimeStamp
	public Double  hFFPos = 0.0;
	@TimeStamp
	public Double  hFFSpeed = 0.0;
	public Double  hFFTargetPos = 0.0;
	public Double  hFFTargetSpeed = 0.0;
	@TimeStamp
	public HashMap<String, Double>  hHPoss = new HashMap<String, Double>();
	@TimeStamp
	public HashMap<String, Double>  hHSpeeds = new HashMap<String, Double>();
	// # in front of the field name to recognize that I should compare with the field's value
	@ModeTransactions( 
		    transitions= {@ModeTransition(fromMode = "initilizedSystem", toMode = "mediate", transitionCondition = "? == #hID"),
		    			  @ModeTransition(fromMode = "mediate", toMode = "initilizedSystem", transitionCondition = "? != #hID && ? != null "),
		    			  @ModeTransition(fromMode = "organizeSearch", toMode = "initilizedSystem", transitionCondition = "? != #hID && ? != null ")}) 	
	public String mID = null;
	//FIXME: change this kind of statement to accept "for" or any other normal command also
	@ModeTransactions( 
		    transitions= {@ModeTransition(fromMode = "initilizedSystem", toMode = "toldToSearch", transitionCondition = "E? == #hID"),
		    			  @ModeTransition(fromMode = "toldToSearch", toMode = "initilizedSystem", transitionCondition = "V? != #hID")}) 	
	public HashMap<String, String> search_In = new HashMap<String, String>();
	public HashMap<String, String> search_Out = new HashMap<String, String>();
	@ModeTransactions( 
		    transitions= {@ModeTransition(fromMode = "flyOff", toMode = "flyOn", transitionCondition = "? == 1.0"),
		    			  @ModeTransition(fromMode = "flyOn", toMode = "flyOff", transitionCondition = "? == 0.0")}) 
	public Double switchSg = 0.0;
	public HashMap<String, Double> signal_In = new HashMap<String, Double>();
	public Double signal_Out = 0.0;
	public HashMap<String, Double> signal_Reply_In = new HashMap<String, Double>();
	public HashMap<String, Double> signal_Reply_Out = new HashMap<String, Double>();
	@ModeTransactions( 
		    transitions= {@ModeTransition(fromMode = "initilizedSearch", toMode = "leadSearch",	transitionCondition = "? == #hID"),
		    			  @ModeTransition(fromMode = "initilizedSearch", toMode = "otherSearch", transitionCondition = "? != #hID"),
		    			  @ModeTransition(fromMode = "leadSearch", toMode = "otherSearch", transitionCondition = "? != #hID"),
		    			  @ModeTransition(fromMode = "otherSearch", toMode = "leadSearch", transitionCondition = "? == #hID")}) 	
	public String searchID = null;  // it is local in the simulation
	public Double time_last = 0.0;
	public Double time_val = 0.0;
	public Double time_period = 0.0;
	public Double protocolDuration = 0.0;
	public Double hold = 0.0;
	
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
		hID = "H1";
	}
	
	
	@Mode
	@Process
	@PeriodicScheduling(value = (int) MODETIMEPERIOD)
	public static void flyOff(){
		//FIXME: what should I put inside??
	}
	
	
	@Mode
	@Process
	@PeriodicScheduling(value = (int) MODETIMEPERIOD)
	public static void flyOn(
			@In("signal_In") HashMap<String, Double> signal_In,
			@Out("signal_Reply_Out") ParamHolder<HashMap<String, Double>> signal_Reply_Out //do we use ParamHolder<Double> instead?
			){
		for (String hID:signal_In.keySet()) {
			signal_Reply_Out.value.put(hID, signal_In.get(hID));
		}
	}
	
	
	@Mode
	@Process
	@PeriodicScheduling(value = (int) MODETIMEPERIOD)
	public static void initilizedSystem(){
		//FIXME: what should I put inside??
	}

	
	@Mode
	@Process
	@PeriodicScheduling(value = (int) MODETIMEPERIOD)
	public static void mediate(
			@Out("time_val") ParamHolder<Double> time_val
			){
		time_val.value = System.nanoTime()/SEC_NANOSEC_FACTOR; // needed for sending messages to another 
	}
	
	
	@Mode
	@Process
	@PeriodicScheduling(value = (int) MODETIMEPERIOD)
	public static void organizeSearch(
			@In("signal_Reply_In") HashMap<String, Double> signal_Reply_In,
			@Out("searchID") ParamHolder<String> searchID,
			@InOut("signal_Out") ParamHolder<Double> signal_Out,
			@InOut("time_last") ParamHolder<Double> time_last,
			@InOut("time_period") ParamHolder<Double> time_period,
			@InOut("time_val") ParamHolder<Double> time_val
			){
		boolean cond = false;
		for (String key : signal_Reply_In.keySet()) {
			if(key.equals(signal_Out))
				cond = true;
		}
		
		if(cond && (time_period.value < (time_val.value - time_last.value))){
			time_period.value = time_val.value - time_last.value;
		}else if(!cond){
			signal_Out.value = time_val.value;
			time_last.value = signal_Out.value;
		}
		
		Range ffRange = new Range();
		HashMap<String,Range> objsRange = new HashMap<String,Range>();
		
		
		searchID.value = getClosest(ffRange,objsRange);
		time_val.value = System.nanoTime()/SEC_NANOSEC_FACTOR;
	}	

	@Mode
	@Process
	@PeriodicScheduling(value = (int) MODETIMEPERIOD)
	public static void toldToSearch(){
		
	}

	@Mode
	@Process
	@PeriodicScheduling(value = (int) MODETIMEPERIOD)
	public static void initilizedSearch(){
		
	}
	
	@Mode
	@Process
	@PeriodicScheduling(value = (int) MODETIMEPERIOD)
	public static void otherSearch(){
		
	}
	
	@Mode
	@Process
	@PeriodicScheduling(value = (int) MODETIMEPERIOD)
	public static void leadSearch(){
		
	}
	
	//all of them "InOut" because if we were in one mode all the parameters will be set to null which are not related to this mode and that is wrong
	@Entry
	public static void entry(
			@In("hID") Double hID,
			@In("modeName") String modeName, //FIXME: should be deleted and triggered from the runtime
			@InOut("hFFPos") InaccuracyParamHolder<Double> hFFPos,
			@InOut("hFFSpeed") InaccuracyParamHolder<Double> hFFSpeed,
			@InOut("hFFTargetPos") ParamHolder<Double> hFFTargetPos,
			@InOut("hFFTargetSpeed") ParamHolder<Double> hFFTargetSpeed,
			@InOut("hHPoss") InaccuracyParamHolder<HashMap<String, Double>> hHPoss,
			@InOut("hHSpeeds") InaccuracyParamHolder<HashMap<String, Double>> hHSpeeds,
			@InOut("search_In") ParamHolder<HashMap<String, String>> search_In,
			@InOut("search_Out") ParamHolder<HashMap<String, String>> search_Out,
			@InOut("switchSg") ParamHolder<Double> switchSg,
			@InOut("signal_In") ParamHolder<HashMap<String, Double>> signal_In,
			@InOut("signal_Out") ParamHolder<Double> signal_Out,
			@InOut("signal_Reply_Out") ParamHolder<HashMap<String, Double>> signal_Reply_Out,
			@InOut("searchID") ParamHolder<Double> searchID,
			@InOut("protocolDuration") ParamHolder<Double> protocolDuration,
			@InOut("time_last") ParamHolder<Double> time_last,
			@InOut("time_period") ParamHolder<Double> time_period,
			@InOut("time_val") ParamHolder<Double> time_val,
			@InOut("hold") ParamHolder<Double> hold
			){
		
		switch(modeName){
		case "flyOff":
			hFFPos.value = 0.0;
			break;
		case "flyOn":
			hold.value = System.nanoTime()/SEC_NANOSEC_FACTOR;
			break;
		case "organizeSearch":
			hold.value = 0.0;
			time_last.value =  System.nanoTime()/SEC_NANOSEC_FACTOR;
			time_period.value = 0.0;
			time_val.value = time_last.value;
			signal_Out.value = time_val.value;
			break;
		case "initilizedSystem":
			hold.value = 0.0;
			hFFPos.value = 0.0;
			hFFPos.creationTime = 0.0;
			hFFPos.minBoundary = 0.0;
			hFFPos.maxBoundary = 0.0;
			hFFSpeed.value = 0.0;
			hFFSpeed.creationTime = 0.0;
			hFFSpeed.minBoundary = 0.0;
			hFFSpeed.maxBoundary = 0.0;
			hFFTargetPos.value = 0.0;
			hFFTargetSpeed.value = 0.0;
			hHPoss.value.clear();
			hHSpeeds.value.clear();
			search_In.value.clear();
			search_Out.value.clear();
			switchSg.value = 0.0;
			signal_In.value.clear();
			signal_Out.value = 0.0;
			signal_Reply_Out.value.clear();
			searchID.value = 0.0; 
			protocolDuration.value = 0.0;
			break;
		case "mediate":
			time_last.value =  System.nanoTime()/SEC_NANOSEC_FACTOR;
			time_period.value = 0.0;
			time_val.value = time_last.value;
			break;
		case "toldToSearch":
			hold.value = 0.0;
			break;
		default:
			break;
		}
	}


	@Exit
	public static void exit(
			@In("modeName") String modeName, //FIXME: should be deleted and triggered from the runtime
			@InOut("hold") ParamHolder<Double> hold
			){
		switch(modeName){
		case "flyOff":
			break;
		case "organizeSearch":
			hold.value = System.nanoTime()/SEC_NANOSEC_FACTOR;
			break;
		case "initilizedSystem":
			hold.value = System.nanoTime()/SEC_NANOSEC_FACTOR;
 		    break;
		case "toldToSearch":
			 hold.value = System.nanoTime()/SEC_NANOSEC_FACTOR;
			 break;
		default:
			break;
		}
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
	
	
	
//	@Process
//	@PeriodicScheduling((int) TIMEPERIOD)
//	public static void minInaccuracyFFControl(
//			@In("hold") Double hold,
//			@InOut("hFFPos") InaccuracyParamHolder<Double> hFFPos,
//			@InOut("hFFSpeed") InaccuracyParamHolder<Double> hFFSpeed
//			){
//		double timePeriodInSeconds = TIMEPERIOD/SEC_MILISEC_FACTOR;
//		if(hold == 0.0){
//			hFFSpeed.minBoundary = hFFSpeed.value;
//			hFFPos.minBoundary = hFFPos.value;
//		}else{
//			double ffAcceleration = Database.getAcceleration(hFFSpeed.minBoundary, hFFPos.minBoundary, Database.fTorques, 0.0, 1.0,Database.fMass);
//			hFFSpeed.minBoundary += ffAcceleration * timePeriodInSeconds; 
//			hFFPos.minBoundary += hFFSpeed.minBoundary * timePeriodInSeconds;
//		}
//	}	
//	
//	
//	
//	@Process
//	@PeriodicScheduling((int) TIMEPERIOD)
//	public static void maxInaccuracyFFControl(
//			@In("hold") Double hold,
//			@InOut("hFFPos") InaccuracyParamHolder<Double> hFFPos,
//			@InOut("hFFSpeed") InaccuracyParamHolder<Double> hFFSpeed
//			){
//		double timePeriodInSeconds = TIMEPERIOD/SEC_MILISEC_FACTOR;
//		if(hold == 0.0){
//			hFFSpeed.maxBoundary = hFFSpeed.value;
//			hFFPos.maxBoundary = hFFPos.value;
//		}else{
//			double ffAcceleration = Database.getAcceleration(hFFSpeed.maxBoundary, hFFPos.maxBoundary, Database.fTorques, 1.0, 0.0,Database.fMass);
//			hFFSpeed.maxBoundary += ffAcceleration * timePeriodInSeconds; 
//			hFFPos.maxBoundary += hFFSpeed.maxBoundary * timePeriodInSeconds;
//		}
//	}	
//	
//	@Process
//	@PeriodicScheduling((int) TIMEPERIOD)
//	public static void minInaccuracyHControl(
//			@In("hold") Double hold,
//			@InOut("hHPos") InaccuracyParamHolder<Double> hFFPos,
//			@InOut("hHSpeed") InaccuracyParamHolder<Double> hFFSpeed
//			){
//		double timePeriodInSeconds = TIMEPERIOD/SEC_MILISEC_FACTOR;
//		if(hold == 0.0){
//			hFFSpeed.minBoundary = hFFSpeed.value;
//			hFFPos.minBoundary = hFFPos.value;
//		}else{
//			double ffAcceleration = Database.getAcceleration(hFFSpeed.minBoundary, hFFPos.minBoundary, Database.fTorques, 0.0, 1.0,Database.fMass);
//			hFFSpeed.minBoundary += ffAcceleration * timePeriodInSeconds; 
//			hFFPos.minBoundary += hFFSpeed.minBoundary * timePeriodInSeconds;
//		}
//	}	
	
	
	
	@Process
	@PeriodicScheduling((int) TIMEPERIOD)
	public static void maxInaccuracyHControl(
			@In("hold") Double hold,
			@InOut("hFFPos") InaccuracyParamHolder<Double> hFFPos,
			@InOut("hFFSpeed") InaccuracyParamHolder<Double> hFFSpeed
			){
		double timePeriodInSeconds = TIMEPERIOD/SEC_MILISEC_FACTOR;
		if(hold == 0.0){
			hFFSpeed.maxBoundary = hFFSpeed.value;
			hFFPos.maxBoundary = hFFPos.value;
		}else{
			double ffAcceleration = Database.getAcceleration(hFFSpeed.maxBoundary, hFFPos.maxBoundary, Database.fTorques, 1.0, 0.0,Database.fMass);
			hFFSpeed.maxBoundary += ffAcceleration * timePeriodInSeconds; 
			hFFPos.maxBoundary += hFFSpeed.maxBoundary * timePeriodInSeconds;
		}
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

	
	
	
	
	//==================================================================================================================================================
	//help functions .......
	private static double saturate(double val) {
		if (val > 1) val = 1;
		else if (val < -1) val = -1;
		return val;
	}

	
	private static String getClosest(Range ffRange, HashMap<String,Range> objsRange) {
		HashMap<String,Range> closestObjsRange = new HashMap<String,Range>(); 
		String schID = null;
		for (String key: objsRange.keySet()) {
			Range resultRange = getComposedInaccuracy(ffRange, objsRange.get(key));
			closestObjsRange.put(key, resultRange);
			schID = key;
		}
		Double inacc_min = closestObjsRange.get(schID).min;
		for (String key: closestObjsRange.keySet()) {
			if(inacc_min > closestObjsRange.get(key).min){
				inacc_min = closestObjsRange.get(key).min;
				schID = key;
			}
		}
		
		return schID;
	}

	private static Range getComposedInaccuracy(Range range1, Range range2){
		double[] val = {0.0,0.0,0.0,0.0};
		val[0] = Math.abs(range2.max - range1.max);
		val[1] = Math.abs(range2.max - range1.min);
		val[2] = Math.abs(range2.min - range1.max);
		val[3] = Math.abs(range2.max - range1.min);
		
		Range result = new Range();
		result.min = val[0];
		result.max = val[0];
		
		for (int i = 1; i < val.length; i++) {
			if(result.min > val[i]) result.min = val[i];
			if(result.max < val[i]) result.max = val[i];
		}
		
		return result;
		
	}

}


class Range{
		double min = 0;
		double max = 0;
		
		public Range(){
			min = 0;
			max = 0;
		}
		
		public Range(int min,int max){
			this.min = min;
			this.max = max;
		}
}