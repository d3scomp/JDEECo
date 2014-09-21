package demo_annotation.Modes;

import java.util.HashMap;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.Fun;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.State;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.StateSpaceModel;
import cz.cuni.mff.d3s.deeco.annotations.Model;
import cz.cuni.mff.d3s.deeco.annotations.TimeStamp;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.InaccuracyParamHolder;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import demo_annotation.test.Inaccuracy.VehicleModel;



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
	@TimeStamp
	public HashMap<String, Double>  hHFFPoss = new HashMap<String, Double>();
	@TimeStamp
	public HashMap<String, Double>  hHFFSpeeds = new HashMap<String, Double>();
	// # in front of the field name to recognize that I should compare with the field's value
	public String mID = null;
	//FIXME: change this kind of statement to accept "for" or any other normal command also
	public HashMap<String, String> search_In = new HashMap<String, String>();
	public HashMap<String, String> search_Out = new HashMap<String, String>();
	public Double switchSg = 0.0;
	public HashMap<String, Double> signal_In = new HashMap<String, Double>();
	public Double signal_Out = 0.0;
	public HashMap<String, Double> signal_Reply_In = new HashMap<String, Double>();
	public HashMap<String, Double> signal_Reply_Out = new HashMap<String, Double>();
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

	
	
	
	
	public Helicopter1(String name) {
		hID = name;
	}
	
	
	@State
	@Process
	public static void flyOff(
			@Out("hFFPos") InaccuracyParamHolder<Double> hFFPos
		){
		hFFPos.value = 0.0;
		//------------------------------
	}
	
	
	@State
	@Process
	public static void flyOn(
			@In("signal_In") HashMap<String, Double> signal_In,
			@Out("signal_Reply_Out") ParamHolder<HashMap<String, Double>> signal_Reply_Out, //do we use ParamHolder<Double> instead?
			@InOut("hold") ParamHolder<Double> hold
			){
		hold.value = System.nanoTime()/SEC_NANOSEC_FACTOR;

		//--------------during-----------------------
		for (String hID:signal_In.keySet()) {
			signal_Reply_Out.value.put(hID, signal_In.get(hID));
		}
	}
	
	
	@State
	@Process
	public static void initilizedSystem(
			@In("hID") Double hID,
			@InOut("hFFPos") InaccuracyParamHolder<Double> hFFPos,
			@InOut("hFFSpeed") InaccuracyParamHolder<Double> hFFSpeed,
			@InOut("hFFTargetPos") ParamHolder<Double> hFFTargetPos,
			@InOut("hFFTargetSpeed") ParamHolder<Double> hFFTargetSpeed,
			@InOut("hHPoss") InaccuracyParamHolder<HashMap<String, Double>> hHPoss,
			@InOut("hHSpeeds") InaccuracyParamHolder<HashMap<String, Double>> hHSpeeds,
			@InOut("hHFFPoss") InaccuracyParamHolder<HashMap<String, Double>> hHFFPoss,
			@InOut("hHFFSpeeds") InaccuracyParamHolder<HashMap<String, Double>> hHFFSpeeds,
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
		hHFFPoss.value.clear();
		hHFFSpeeds.value.clear();
		search_In.value.clear();
		search_Out.value.clear();
		switchSg.value = 0.0;
		signal_In.value.clear();
		signal_Out.value = 0.0;
		signal_Reply_Out.value.clear();
		searchID.value = 0.0; 
		protocolDuration.value = 0.0;

		//-------------during---------------------
		//********************************
		hold.value = System.nanoTime()/SEC_NANOSEC_FACTOR;
	}

	
	@State
	@Process
	public static void mediate(
			@Out("time_val") ParamHolder<Double> time_val,
			@Out("time_period") ParamHolder<Double> time_period,
			@InOut("time_last") ParamHolder<Double> time_last
			){
		time_last.value =  System.nanoTime()/SEC_NANOSEC_FACTOR;
		time_period.value = 0.0;
		time_val.value = time_last.value;

		//--------------during---------------------------
		time_val.value = System.nanoTime()/SEC_NANOSEC_FACTOR; // needed for sending messages to another 
	}

	
	@State
	@Process
	public static void organizeSearch(
			@In("hPos") Double hPos,
			@In("hSpeed") Double hSpeed,
			@InOut("hFFTargetPos") ParamHolder<Double> hFFTargetPos,
			@InOut("hFFTargetSpeed") ParamHolder<Double> hFFTargetSpeed,
			@In("signal_Reply_In") HashMap<String, Double> signal_Reply_In,
			@Out("searchID") ParamHolder<String> searchID,
			@InOut("signal_Out") ParamHolder<Double> signal_Out,
			@InOut("time_last") ParamHolder<Double> time_last,
			@InOut("time_period") ParamHolder<Double> time_period,
			@InOut("time_val") ParamHolder<Double> time_val,
			@InOut("hold") ParamHolder<Double> hold
			){
		hold.value = 0.0;
		time_last.value =  System.nanoTime()/SEC_NANOSEC_FACTOR;
		time_period.value = 0.0;
		time_val.value = time_last.value;
		signal_Out.value = time_val.value;

		//------------------during------------------------
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
		//TODO:
		
		
		searchID.value = getClosest(ffRange,objsRange);
		time_val.value = System.nanoTime()/SEC_NANOSEC_FACTOR;
		
		//***************************
		hold.value = System.nanoTime()/SEC_NANOSEC_FACTOR;
	}	

	
	@State
	@Process
	public static void toldToSearch(
			@In("hPos") Double hPos,
			@In("hSpeed") Double hSpeed,
			@InOut("hHFFPoss") ParamHolder<HashMap<String,InaccuracyParamHolder<Double>>> hHFFPoss,
			@InOut("hHFFSpeeds") ParamHolder<HashMap<String,InaccuracyParamHolder<Double>>> hHFFSpeeds,
			@InOut("hFFTargetPos") ParamHolder<Double> hFFTargetPos,
			@InOut("hFFTargetSpeed") ParamHolder<Double> hFFTargetSpeed,
			@InOut("hold") ParamHolder<Double> hold
			){
		
		//-------------during ---------------
		int key = -1;
		Double max = -1.0;
		for (int i = 0; i < hHFFPoss.value.size(); i++) {
			if(max < hHFFPoss.value.get(i).creationTime){
				max = hHFFPoss.value.get(i).creationTime;
				key = i;
			}
		}
		
		Range pos = new Range(hHFFPoss.value.get(key).minBoundary,hHFFPoss.value.get(key).maxBoundary);
		Range speed = new Range(hHFFSpeeds.value.get(key).minBoundary,hHFFSpeeds.value.get(key).maxBoundary);
		Target tr = computeTarget(hPos, pos, hSpeed, speed);
		hFFTargetPos.value = tr.pos;
		hFFTargetSpeed.value = tr.speed;
		//****************************
		hold.value = System.nanoTime()/SEC_NANOSEC_FACTOR;
		
	}

	
	@State
	@Process
	public static void initilizedSearch(
			@InOut("hFFTargetPos") ParamHolder<Double> hFFTargetPos,
			@InOut("hFFTargetSpeed") ParamHolder<Double> hFFTargetSpeed
			){
		//--------------during---------------------------
		hFFTargetPos.value = 0.0;
		hFFTargetSpeed.value = 0.0;
	}
	
	
	@State
	@Process
	public static void otherSearch(
			@InOut("hold") ParamHolder<Double> hold
			){
		
		hold.value = 0.0;
		//----------------during---------------------------
		
	}
	
	
	@State
	@Process
	public static void leadSearch(
			@In("hPos") Double hPos,
			@In("hSpeed") Double hSpeed,
			@In("hFFPos") InaccuracyParamHolder<Double> hFFPos,
			@In("hFFSpeed") InaccuracyParamHolder<Double> hFFSpeed,
			@InOut("hFFTargetPos") ParamHolder<Double> hFFTargetPos,
			@InOut("hFFTargetSpeed") ParamHolder<Double> hFFTargetSpeed
			){
		//--------------during---------------------------
		Range pos = new Range(hFFPos.minBoundary,hFFPos.maxBoundary);
		Range speed = new Range(hFFSpeed.minBoundary,hFFSpeed.maxBoundary);
		Target tr = computeTarget(hPos, pos, hSpeed, speed);
		hFFTargetPos.value = tr.pos;
		hFFTargetSpeed.value = tr.speed;
		
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

	
	public static Target computeTarget(Double xPos, Range yPos,Double xSpeed, Range ySpeed) {
		Target tr = new Target();
		
		if( xPos < yPos.min ) { tr.pos = yPos.min; tr.speed = ySpeed.min; }
		else if(xPos > yPos.max){ tr.pos = DESIRED_DISTANCE; tr.speed = DESIRED_SPEED;}
		else if( (xPos >= yPos.min) && (xPos <= yPos.max)) { tr.pos = yPos.max; tr.speed = ySpeed.max;}
		
		return tr;
	}
	
	
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
		
		public Range(double min,double max){
			this.min = min;
			this.max = max;
		}
}


class Target{
	double pos = 0;
	double speed = 0;
	
	public Target(){
		this.pos = 0;
		this.speed = 0;
	}
	
	public Target(double pos,double speed){
		this.pos = pos;
		this.speed = speed;
	}
}