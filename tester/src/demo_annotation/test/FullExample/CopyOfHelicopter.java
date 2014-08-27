package demo_annotation.test.FullExample;

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
import cz.cuni.mff.d3s.deeco.annotations.Transition;
import cz.cuni.mff.d3s.deeco.annotations.Transitions;
import cz.cuni.mff.d3s.deeco.annotations.TriggerOnTimeStampChange;
import cz.cuni.mff.d3s.deeco.annotations.TriggerOnTimeStampUnchange;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.InaccuracyParamHolder;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.ModeParamHolder;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.TSParamHolder;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import demo_annotation.test.Modes.Database;



@StateSpaceModel(models = {@Model( period = 100, state  = {"hFFSpeed","hFFPos"}, 
							result = @Fun(returnedIndex = {-1,0}, referenceModel = VehicleModel.class)),
							@Model( period = 100, state  = {"hHSpeed","hHPos"}, 
							result = @Fun(returnedIndex = {-1,0}, referenceModel = VehicleModel.class))})
@Component
public class CopyOfHelicopter {

	public String  hID;
	@TimeStamp
	public Double  hPos = 0.0;
	@TimeStamp
	public Double  hSpeed = 0.0;
	public Double  hGas = 0.0;
	public Double  hBrake = 0.0;
	public Double  hRangeDistance = 100.0; 
	
	@Transitions(map = {@Transition(from = "initilizedSystem", to = "mediate")//,
//				@Transition(from = "mediate", to = "organizeSearch"),
//				@Transition(from = "organizeSearch", to = "initilizedSystem")
	})	
	@TimeStamp
	public Double  hFFPos = 0.0;
	@TimeStamp
	public Double  hFFSpeed = 0.0;
	public Double  hFFTargetPos = 0.0;
	public Double  hFFTargetSpeed = 0.0;

	
	// In case loss of communication and FF did not communicate with the leader
	public String hHName = "";
	@TimeStamp
	public Double  hHPos = 0.0;
	@TimeStamp
	public Double  hHSpeed = 0.0;
	@TimeStamp
	public Double  hLFFPos = 0.0;
	@TimeStamp
	public Double  hLFFSpeed = 0.0;
	@TimeStamp
	public Double  hHFFPos = 0.0;
	@TimeStamp
	public Double  hHFFSpeed = 0.0;
	
	//would it work without timestamp???
//	@Transitions(map = {@Transition(from = "initilizedSystem", to = "toldToSearch"),
//			@Transition(from = "toldToSearch", to = "initilizedSystem")})	
	public String hSearch_In = "";
	public String hSearch_Out = "";
	
//	@Transitions(map = {@Transition(from = "leadSearch", to = "otherSearch"),
//			@Transition(from = "otherSearch", to = "leadSearch")})	
	public String hSearchID = null;  // it is local in the simulation
//	public Double protocolDuration = 0.0; //for the prediction

	
	
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

	
	public CopyOfHelicopter(String name, Double pos) {
		hID = name;
		hPos = pos; 
	}
	
	
	@Mode(init = true)
	@Process
	public static void initilizedSystem(
			@InOut("hFFPos") @TriggerOnTimeStampUnchange(equal = 0) ModeParamHolder<Double> hFFPos
			){
		System.out.println("init ......");
	}

	
	@Process
	public static void mediate(
			@InOut("hFFPos") @TriggerOnTimeStampChange ModeParamHolder<Double> hFFPos,
			@InOut("hFFSpeed") InaccuracyParamHolder<Double> hFFSpeed,
			@InOut("hLFFPos") TSParamHolder<Double> hLFFPos,
			@InOut("hLFFSpeed") TSParamHolder<Double> hLFFSpeed
			){
		System.out.println("mediate .....");
		hLFFPos.setWithTS(hFFPos.value, hFFPos.creationTime);
		hLFFSpeed.setWithTS(hFFSpeed.value, hLFFSpeed.creationTime);
	}

	
//	@Process
//	public static void organizeSearch(
//			@InOut("hFFPos") @TriggerOnValueUnchange(moreThan = 5000) ModeParamHolder<Double> hFFPos,
//			@InOut("hSearchID") ModeParamHolder<String> hSearchID
//			){
//		RangeValue ffRange = new RangeValue();
//		HashMap<String,RangeValue> objsRange = new HashMap<String,RangeValue>();
//		//TODO:
//		
//		
//		hSearchID.value = getClosest(ffRange,objsRange);
//	}	
//
//	
//	@Process
//	public static void toldToSearch(
//			@InOut("hSearch_In") @TriggerOnValueChange(notEqualStr = "") ModeParamHolder<Double> hSearch_In,
//			@In("hPos") Double hPos,
//			@In("hSpeed") Double hSpeed,
//			@InOut("hFFPos") InaccuracyParamHolder<Double> hFFPos,
//			@InOut("hHFFPos") InaccuracyParamHolder<Double> hHFFPos,
//			@InOut("hHFFSpeed") InaccuracyParamHolder<Double> hHFFSpeed,
//			@InOut("hFFTargetPos") InaccuracyParamHolder<Double> hFFTargetPos,
//			@InOut("hFFTargetSpeed") InaccuracyParamHolder<Double> hFFTargetSpeed
//			){
//		Double max = hFFPos.creationTime;
//		if(max < hHFFPos.creationTime){
//			max = hHFFPos.creationTime;
//		}
//		
//		RangeValue pos = new RangeValue(hHFFPos.minBoundary,hHFFPos.maxBoundary);
//		RangeValue speed = new RangeValue(hHFFSpeed.minBoundary,hHFFSpeed.maxBoundary);
//		TargetValue tr = computeTarget(hPos, pos, hSpeed, speed);
//		hFFTargetPos.value = tr.pos;
//		hFFTargetSpeed.value = tr.speed;
//	}
//
//	
//	@Mode(parent = "organizeSearch")
//	@Process
//	public static void otherSearch(
//			@InOut("hSearchID") @TriggerOnValueChange(notEqualStr = "#hID") ModeParamHolder<String> hSearchID,
//			@InOut("hSearch_Out") ParamHolder<String> hSearch_Out
//		){
//		hSearch_Out = hSearchID;
//	}
//	
//	
//	@Mode(init = true, parent = "organizeSearch")
//	@Process
//	public static void leadSearch(
//			@InOut("hSearchID") @TriggerOnValueChange(equalStr = "#hID") ModeParamHolder<String> hSearchID,
//			@In("hPos") Double hPos,
//			@In("hSpeed") Double hSpeed,
//			@In("hFFPos") InaccuracyParamHolder<Double> hFFPos,
//			@In("hFFSpeed") InaccuracyParamHolder<Double> hFFSpeed,
//			@InOut("hFFTargetPos") ParamHolder<Double> hFFTargetPos,
//			@InOut("hFFTargetSpeed") ParamHolder<Double> hFFTargetSpeed
//			){
//		//--------------during---------------------------
//		RangeValue pos = new RangeValue(hFFPos.minBoundary,hFFPos.maxBoundary);
//		RangeValue speed = new RangeValue(hFFSpeed.minBoundary,hFFSpeed.maxBoundary);
//		TargetValue tr = computeTarget(hPos, pos, hSpeed, speed);
//		hFFTargetPos.value = tr.pos;
//		hFFTargetSpeed.value = tr.speed;		
//	}
	
	
	
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
			@Out("hBrake") ParamHolder<Double> hBrake,

			@InOut("hIntegratorSpeedError") ParamHolder<Double> hIntegratorSpeedError,
			@InOut("hErrorWindup") ParamHolder<Double> hErrorWindup) {

		double currentTime = System.nanoTime()/SEC_NANOSEC_FACTOR;
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