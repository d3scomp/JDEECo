package demo_annotation.test.FullExample;

import java.util.HashMap;
import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.Model;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.Mode;
import cz.cuni.mff.d3s.deeco.annotations.Sets;
import cz.cuni.mff.d3s.deeco.annotations.Exclusive;
import cz.cuni.mff.d3s.deeco.annotations.Parallel;
import cz.cuni.mff.d3s.deeco.annotations.StateSpaceModel;
import cz.cuni.mff.d3s.deeco.annotations.TimeStamp;
import cz.cuni.mff.d3s.deeco.annotations.TriggerOnTimeStampChange;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.InaccuracyParamHolder;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.TSParamHolder;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;




@StateSpaceModel( models = {@Model( period = 100, state  = {"hFFPos","hFFSpeed"}, referenceModel = VehicleModel.class)})
@Sets(parallel = @Parallel(modes = {"SelfSearch","NoSelfSearch"}),
		exclusive = @Exclusive(parent = "NoSelfSearch", modes = {"NoSearch","RequestSearch"}))
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
//  public Double protocolDuration = 0.0; //for the prediction


	protected static double hIntegratorError = 0.0;
	protected static double hErrorWindup = 0.0;
	protected static final double KP_D = 1.0;//0.193;
	protected static final double KP_S = 1.0;//0.01;
	protected static final double KI_S = 0.01;//00002;
	protected static final double KT_S = 0.01;//00001;
	protected static final double TIMEPERIOD = 100;
	protected static final double SEC_MILISEC_FACTOR = 1000;
	protected static final double SEC_NANOSEC_FACTOR = 1000000000;
	protected static final double DESIRED_DISTANCE = 0;
	protected static final double DESIRED_SPEED = 0;

	
	public Helicopter(String name, Double pos) {
		hID = name;
		hPos = pos; 
		hFFTargetPos = pos;
	}
	
	@Mode(set = "NoSearch", guard = "organizeSearch && V > 0 && LH > 10")
	@Process
	@PeriodicScheduling((int) TIMEPERIOD)
	public static void initilizedSystem(
			@In("hPos") Double hPos,
			@In("hSpeed") Double hSpeed,			
			@InOut("hFFPos") InaccuracyParamHolder<Double> hFFPos,
			@Out("hFFTargetPos") ParamHolder<Double> hFFTargetPos,
			@Out("hFFTargetSpeed") ParamHolder<Double> hFFTargetSpeed
			){
		System.out.println("init ...... "+hFFPos.value+" "+hFFPos.creationTime+"  ["+hFFPos.minBoundary+" "+hFFPos.maxBoundary+"]... hpos "+hPos+" hspeed "+hSpeed);
		hFFPos.setWithTS(0.0, 0.0);
		hFFPos.minBoundary = 0.0;
		hFFPos.maxBoundary = 0.0;
		hFFTargetPos.value = hPos;
		hFFTargetSpeed.value = hSpeed;
	}

//I can mediate for another
	@Mode(set = "RequestSearch", guard = "initilizedSystem && V == hID")
	@Process
	public static void toldToSearch(
			@In("hSearch_In") @TriggerOnTimeStampChange String hSearch_In,
			@In("hPos") Double hPos,
			@In("hSpeed") Double hSpeed,			
			@InOut("hFFPos") InaccuracyParamHolder<Double> hFFPos,
			@InOut("hFFSpeed") InaccuracyParamHolder<Double> hFFSpeed,
			@Out("hFFTargetPos") ParamHolder<Double> hFFTargetPos,
			@Out("hFFTargetSpeed") ParamHolder<Double> hFFTargetSpeed
			){
		System.out.println("toldToSearch ...... "+hSearch_In);
		TargetValue t = computeTarget(hPos, new RangeValue(hFFPos.minBoundary, hFFPos.maxBoundary), hSpeed, new RangeValue(hFFSpeed.minBoundary, hFFSpeed.maxBoundary),hPos, hSpeed);
		hFFTargetPos.value = t.pos;
		hFFTargetSpeed.value = hSpeed;
	}

	@Mode(set = "NoSearch", guard = {"V > 0","initilizedSystem && V > 0","organizeSearch && LH <= 5"})
	@Process
	public static void mediate(
			@InOut("hFFPos") @TriggerOnTimeStampChange InaccuracyParamHolder<Double> hFFPos,
			@InOut("hFFSpeed") InaccuracyParamHolder<Double> hFFSpeed,
			@In("hPos") Double hPos,
			@In("hSpeed") Double hSpeed,			
			@Out("hFFTargetPos") ParamHolder<Double> hFFTargetPos,
			@Out("hFFTargetSpeed") ParamHolder<Double> hFFTargetSpeed
			){
		System.out.println("mediate ..... "+hFFPos.value+" .... ["+hFFPos.minBoundary+" , "+hFFPos.maxBoundary+"]");
		hFFTargetPos.value = hPos;
		hFFTargetSpeed.value = hSpeed;
	}

	@Mode(set = "NoSearch", guard = "V > 0 && LH > 5")
	@Process
	public static void otherSearch(
			@In("hID") String hID,
			@In("hPos") Double hPos,
			@In("hSpeed") Double hSpeed,			
			@InOut("hFFPos") InaccuracyParamHolder<Double> hFFPos,
			@InOut("hFFSpeed") InaccuracyParamHolder<Double> hFFSpeed,
			@InOut("hSearchID") @TriggerOnTimeStampChange ParamHolder<String> hSearchID,
			@Out("hFFTargetPos") ParamHolder<Double> hFFTargetPos,
			@Out("hFFTargetSpeed") ParamHolder<Double> hFFTargetSpeed,
			@InOut("hSearch_Out") ParamHolder<String> hSearch_Out
		){
		RangeValue ffRange = new RangeValue(hFFPos.minBoundary, hFFPos.maxBoundary);
		HashMap<String,RangeValue> objsRange = new HashMap<String,RangeValue>();
		//TODO: add all other Hs
		objsRange.put("H1", new RangeValue(hPos,hPos));
		hSearchID.value = getClosest(ffRange,objsRange);

		if(hSearchID.value != hID){
			hFFTargetPos.value = hPos;
			hFFTargetSpeed.value = hSpeed;
			hSearch_Out.value = hSearchID.value;
			System.err.println("otherSearch ...["+hFFPos.minBoundary+","+hFFPos.maxBoundary+"] ... target = "+hFFTargetPos.value+" , "+hFFTargetSpeed.value);
		}
	}
	
	
	@Mode(set = "SelfSearch", guard = "V > 0 && LH > 5")
	@Process
	public static void leadSearch(
			@In("hID") String hID,
			@In("hPos") Double hPos,
			@In("hSpeed") Double hSpeed,
			@InOut("hFFPos") InaccuracyParamHolder<Double> hFFPos,
			@InOut("hFFSpeed") InaccuracyParamHolder<Double> hFFSpeed,
			@Out("hFFTargetPos") ParamHolder<Double> hFFTargetPos,
			@Out("hFFTargetSpeed") ParamHolder<Double> hFFTargetSpeed,
			@InOut("hSearchID") ParamHolder<String> hSearchID
			){

		RangeValue ffRange = new RangeValue(hFFPos.minBoundary, hFFPos.maxBoundary);
		HashMap<String,RangeValue> objsRange = new HashMap<String,RangeValue>();
		//TODO: add all other Hs
		objsRange.put("H1", new RangeValue(hPos,hPos));
		hSearchID.value = getClosest(ffRange,objsRange);

		
		if(hSearchID.value == hID){
			double currentTime = System.nanoTime()/SEC_NANOSEC_FACTOR;
			RangeValue r1 = new RangeValue();
			RangeValue r2 = new RangeValue();
			r1.min = hFFPos.minBoundary;
			r1.max = hFFPos.maxBoundary;
			r2.min = hFFSpeed.minBoundary;
			r2.max = hFFSpeed.maxBoundary;
			TargetValue result = new TargetValue();
			result = computeTarget(hFFPos.value, r1, hFFSpeed.value, r2, hPos, hSpeed);
			hFFTargetPos.value = result.pos;
			hFFTargetSpeed.value = hSpeed;
			System.err.println("leadSearch ... ["+hFFPos.minBoundary+","+hFFPos.maxBoundary+"] "+hFFPos.creationTime+" - "+currentTime+" ... target = "+hFFTargetPos.value+" , "+hFFTargetSpeed.value+"   ---- "+hPos+"  ,  "+hSpeed);
		}
	}
	

	//
	//-----------------------------------------------------------------------------------------------------------------------------
	//
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
//		System.out.println(hPos.value+"   "+hSpeed.value+" ....  "+hFFTargetPos+"   "+hFFTargetSpeed);
		double currentTime = System.nanoTime()/SEC_NANOSEC_FACTOR;
		double timePeriodInSeconds = TIMEPERIOD / SEC_MILISEC_FACTOR;
		double distanceError = -DESIRED_DISTANCE + hFFTargetPos - hPos.value;
		double pidDistance = KP_D * distanceError;
		double error = pidDistance + hFFTargetSpeed - hSpeed.value;
		hIntegratorError += (KI_S * error + KT_S * hErrorWindup) * timePeriodInSeconds;
		double pidSpeed = KP_S * error + hIntegratorError;
		hErrorWindup = saturate(pidSpeed) - pidSpeed;

		if (saturate(pidSpeed) >= 0) {
			hGas.value = saturate(pidSpeed);
			hBrake.value = 0.0;
		} else {
			hGas.value = 0.0;
			hBrake.value = -saturate(pidSpeed);
		}
		
		double hAcceleration = Database.getAcceleration(hSpeed.value, hPos.value, Database.fTorques, hGas.value, hBrake.value,Database.fMass);
		hSpeed.value += hAcceleration * timePeriodInSeconds; 
		hPos.value += hSpeed.value * timePeriodInSeconds;
		hPos.creationTime = currentTime;
		hSpeed.creationTime = currentTime;
//		System.out.println("hPos : "+hPos.value+"   target:"+hFFTargetPos+" ,  hSpeed : "+hSpeed.value+"   target:"+hFFTargetSpeed);

	}	

	
	public static TargetValue computeTarget(Double xPos, RangeValue yPos,Double xSpeed, RangeValue ySpeed, Double hPos, Double hSpeed) {
		TargetValue tr = new TargetValue();
		
		if( xPos < yPos.min ) { tr.pos = yPos.min; tr.speed = hSpeed; }
		else if(xPos > yPos.max){ tr.pos = hPos; tr.speed = hSpeed;}
		else if( (xPos >= yPos.min) && (xPos <= yPos.max)) { tr.pos = yPos.max; tr.speed = hSpeed;}
		
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