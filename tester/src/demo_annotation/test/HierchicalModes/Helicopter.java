package demo_annotation.test.HierchicalModes;

import java.util.HashMap;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.Fun;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.Mode;
import cz.cuni.mff.d3s.deeco.annotations.Model;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.StateSpaceModel;
import cz.cuni.mff.d3s.deeco.annotations.TimeStamp;
import cz.cuni.mff.d3s.deeco.annotations.TriggerOnValueChange;
import cz.cuni.mff.d3s.deeco.annotations.TriggerOnValueUnchange;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.InaccuracyParamHolder;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.TSParamHolder;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import demo_annotation.test.Modes.Database;



@StateSpaceModel(models = {@Model( period = 100, state  = {"hFFSpeed","hFFPos"}, 
							result = @Fun(returnedIndex = {-1,0}, referenceModel = VehicleModel.class))})
@Component
public class Helicopter {

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
	
	public String hSearch_In = "";
	public String hSearch_Out = "";
	public String hSearchID = "";  // it is local in the simulation
////public Double protocolDuration = 0.0; //for the prediction


	protected static double hIntegratorError = 0.0;
	protected static double hErrorWindup = 0.0;
	protected static final double KP_D = 0.193;
	protected static final double KP_S = 0.01;
	protected static final double KI_S = 0.00002;
	protected static final double KT_S = 0.00001;
	protected static final double TIMEPERIOD = 100;
	protected static final double SEC_MILISEC_FACTOR = 1000;
	protected static final double SEC_NANOSEC_FACTOR = 1000000000;
	protected static final double DESIRED_DISTANCE = 0;
	protected static final double DESIRED_SPEED = 0;

	
	public Helicopter(String name, Double pos) {
		hID = name;
		hPos = pos; 
	}
	

	@Process
	public static void initilizedSystem(
			@InOut("hFFPos") @TriggerOnValueUnchange(from = "organizeSearch", guard = "V > 0 && LH > 10") InaccuracyParamHolder<Double> hFFPos
			){
		System.out.println("init ...... "+hFFPos.value+" "+hFFPos.creationTime+"  ["+hFFPos.minBoundary+" "+hFFPos.maxBoundary+"]...");
		hFFPos.value = 0.0;
		hFFPos.minBoundary = 0.0;
		hFFPos.maxBoundary = 0.0;
	}


	@Process
	public static void toldToSearch(
			@In("hSearch_In") @TriggerOnValueChange(from = "initilizedSystem", guard = "V == hID") String hSearch_In,
			@In("hFFPos") Double hFFPos,
			@In("hFFSpeed") Double hFFSpeed,
			@Out("hFFTargetPos") ParamHolder<Double> hFFTargetPos,
			@Out("hFFTargetSpeed") ParamHolder<Double> hFFTargetSpeed
			){
		System.out.println("toldToSearch ...... "+hSearch_In);
		hFFTargetPos.value = hFFPos;
		hFFTargetSpeed.value = hFFSpeed;
	}

	
	@Process
	public static void mediate(
			@InOut("hFFPos") @TriggerOnValueChange(from = {"","initilizedSystem","organizeSearch"}, guard = {"V > 0","V > 0","LH <= 5"}) InaccuracyParamHolder<Double> hFFPos,
			@InOut("hFFSpeed") InaccuracyParamHolder<Double> hFFSpeed
			){
		System.out.println("mediate ..... "+hFFPos.value+" .... ["+hFFPos.minBoundary+" , "+hFFPos.maxBoundary+"]");
	}

	
	@Process
	public static void organizeSearch(
			@InOut("hFFPos") @TriggerOnValueUnchange(from = "mediate", guard = "LH > 5") InaccuracyParamHolder<Double> hFFPos,
			@InOut("hSearchID") ParamHolder<String> hSearchID
			){
		System.out.println("organize ..... "+hFFPos.value+" .... ["+hFFPos.minBoundary+" , "+hFFPos.maxBoundary+"]");
		RangeValue ffRange = new RangeValue();
		HashMap<String,RangeValue> objsRange = new HashMap<String,RangeValue>();
		//TODO:
		
		
		hSearchID.value = "H1";//getClosest(ffRange,objsRange);
	}	

	
	@Mode(parent = "organizeSearch")
	@Process
	public static void otherSearch(
			@In("hSearchID") @TriggerOnValueChange(guard = "V != hID") String hSearchID,
			@InOut("hSearch_Out") ParamHolder<String> hSearch_Out
		){
		System.err.println("otherSearch ...");
		hSearch_Out.value = hSearchID;
	}
	
	
	@Mode(parent = "organizeSearch")
	@Process
	public static void leadSearch(
			// It can't put handle simple string for comparing. We should pass variable or numbers.
			@In("hSearchID") @TriggerOnValueChange(guard = "V == hID") String hSearchID
			){
		System.err.println("leadSearch ...");
	}
	

	@Process
	@PeriodicScheduling((int) TIMEPERIOD)
	public static void speedControl(
			@InOut("hPos") TSParamHolder<Double> hPos,
			@InOut("hSpeed") TSParamHolder<Double> hSpeed,
			@In("hFFPos") Double hFFPos,
			@In("hFFSpeed") Double hFFSpeed,
			@In("hFFTargetPos") Double hFFTargetPos,
			@In("hFFTargetSpeed") Double hFFTargetSpeed,

			@Out("hGas") ParamHolder<Double> hGas,
			@Out("hBrake") ParamHolder<Double> hBrake
			) {

		double currentTime = System.nanoTime()/SEC_NANOSEC_FACTOR;
		double timePeriodInSeconds = TIMEPERIOD / SEC_MILISEC_FACTOR;
		double distanceError = -DESIRED_DISTANCE + hFFTargetPos - hFFPos;
		double pidDistance = KP_D * distanceError;
		double error = pidDistance + hFFTargetSpeed - hFFSpeed;
		hIntegratorError += (KI_S * error + KT_S * hErrorWindup) * timePeriodInSeconds;
		double pidSpeed = KP_S * error + hIntegratorError;
		hErrorWindup = saturate(pidSpeed) - pidSpeed;

		if (pidSpeed >= 0) {
			hGas.value = pidSpeed;
			hBrake.value = 0.0;
		} else {
			hGas.value = 0.0;
			hBrake.value = -pidSpeed;
		}
		
		
		double hAcceleration = Database.getAcceleration(hSpeed.value, hPos.value, Database.hTorques, hGas.value, hBrake.value,Database.hMass);
		hSpeed.value += hAcceleration * timePeriodInSeconds; 
		hPos.value += hSpeed.value * timePeriodInSeconds;
		hPos.creationTime = currentTime;
		hSpeed.creationTime = currentTime;

	}	

	
	public static TargetValue computeTarget(Double xPos, RangeValue yPos,Double xSpeed, RangeValue ySpeed) {
		TargetValue tr = new TargetValue();
		
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

	
	private static String getClosest(RangeValue ffRange, HashMap<String,RangeValue> objsRange) {
		HashMap<String,RangeValue> closestObjsRange = new HashMap<String,RangeValue>(); 
		String schID = null;
		for (String key: objsRange.keySet()) {
			RangeValue resultRange = getComposedInaccuracy(ffRange, objsRange.get(key));
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

	private static RangeValue getComposedInaccuracy(RangeValue range1, RangeValue range2){
		double[] val = {0.0,0.0,0.0,0.0};
		val[0] = Math.abs(range2.max - range1.max);
		val[1] = Math.abs(range2.max - range1.min);
		val[2] = Math.abs(range2.min - range1.max);
		val[3] = Math.abs(range2.max - range1.min);
		
		RangeValue result = new RangeValue();
		result.min = val[0];
		result.max = val[0];
		
		for (int i = 1; i < val.length; i++) {
			if(result.min > val[i]) result.min = val[i];
			if(result.max < val[i]) result.max = val[i];
		}
		
		return result;
		
	}

}


class RangeValue{
		double min = 0;
		double max = 0;
		
		public RangeValue(){
			min = 0;
			max = 0;
		}
		
		public RangeValue(double min,double max){
			this.min = min;
			this.max = max;
		}
}


class TargetValue{
	double pos = 0;
	double speed = 0;
	
	public TargetValue(){
		this.pos = 0;
		this.speed = 0;
	}
	
	public TargetValue(double pos,double speed){
		this.pos = pos;
		this.speed = speed;
	}
}