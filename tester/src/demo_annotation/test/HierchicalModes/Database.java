package demo_annotation.test.HierchicalModes;

import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;

public class Database {
	
	protected static final ArrayList<Double> driverSpeed = new ArrayList<Double>();
	protected static final ArrayList<Double> routeSlops = new ArrayList<Double>();
	protected static final ArrayList<Double> fTorques = new ArrayList<Double>();
	protected static final ArrayList<Double> lTorques = new ArrayList<Double>();
	protected static final ArrayList<Double> hTorques = new ArrayList<Double>();
	protected static final ArrayList<Double> positionSeries = new ArrayList<Double>();
	protected static final ArrayList<Double> speedSeries = new ArrayList<Double>();
	protected static final ArrayList<Double> rotationSeries = new ArrayList<Double>();
	protected static final ArrayList<Double> helicopterSpeedSeries = new ArrayList<Double>();
	protected static final ArrayList<Double> helicopterArea = new ArrayList<Double>();
	protected static final double lMass = 1000;
	protected static final double fMass = 1000;
	protected static final double hMass = 5;
	protected static final double r_density = 1000; // depends on the temperature of air, altitude, and humidity. 
	protected static final double C_L = 0.5;
	protected static final double C_D = 0.00005;
	protected static final double r = 0.5;
	protected static final double c = 0.2;
	protected static final double g = 9.80665;

	static{
		positionSeries.add(-10000.0);
		positionSeries.add(0.0);
		positionSeries.add(1000.0);
		positionSeries.add(2000.0);
		positionSeries.add(3000.0);
		positionSeries.add(4000.0);
		positionSeries.add(5000.0);
		positionSeries.add(6000.0);
		positionSeries.add(7000.0);
		positionSeries.add(8000.0);
		positionSeries.add(9000.0);
		positionSeries.add(10000.0);
		positionSeries.add(100000.0);

		driverSpeed.add(0.0);
		driverSpeed.add(90.0);
		driverSpeed.add(90.0);
		driverSpeed.add(90.0);
		driverSpeed.add(/*150*/90.0);
		driverSpeed.add(/*170*/90.0);
		driverSpeed.add(90.0);
		driverSpeed.add(90.0);
		driverSpeed.add(90.0);
		driverSpeed.add(90.0);
		driverSpeed.add(90.0);
		driverSpeed.add(90.0);
		driverSpeed.add(90.0);

		routeSlops.add(0.0);
		routeSlops.add(0.0);
		routeSlops.add(0.0);
		routeSlops.add(/*Math.PI/60*/0.0);
		routeSlops.add(/*Math.PI/30*/0.0);
		routeSlops.add(/*Math.PI/20*/0.0);
		routeSlops.add(/*Math.PI/15*/0.0);
		routeSlops.add(0.0);
		routeSlops.add(0.0);
		routeSlops.add(/*-Math.PI/18*/0.0);
		routeSlops.add(/*-Math.PI/36*/0.0);
		routeSlops.add(0.0);
		routeSlops.add(0.0);

		
		speedSeries.add(-100000.0);
		speedSeries.add(0.0);
		speedSeries.add(8.0);
		speedSeries.add(20.0);
		speedSeries.add(28.0);
		speedSeries.add(40.0);
		speedSeries.add(60.0);
		speedSeries.add(80.0);
		speedSeries.add(100.0);
		speedSeries.add(120.0);
		speedSeries.add(140.0);
		speedSeries.add(160.0);
		speedSeries.add(180.0);
		speedSeries.add(200.0);
		speedSeries.add(220.0);

		helicopterSpeedSeries.add(-100000.0);
		helicopterSpeedSeries.add(0.0);
		helicopterSpeedSeries.add(8.0);
		helicopterSpeedSeries.add(20.0);
		helicopterSpeedSeries.add(28.0);
		helicopterSpeedSeries.add(40.0);
		helicopterSpeedSeries.add(60.0);
		helicopterSpeedSeries.add(80.0);
		helicopterSpeedSeries.add(100.0);
		helicopterSpeedSeries.add(120.0);
		helicopterSpeedSeries.add(160.0);
		helicopterSpeedSeries.add(200.0);
		helicopterSpeedSeries.add(300.0);
		helicopterSpeedSeries.add(400.0);
		helicopterSpeedSeries.add(100000.0);

		rotationSeries.add(2*Math.PI/60);
		rotationSeries.add(2*Math.PI/60);
		rotationSeries.add(12*Math.PI/60);
		rotationSeries.add(25*Math.PI/60);
		rotationSeries.add(32*Math.PI/60);
		rotationSeries.add(40*Math.PI/60);
		rotationSeries.add(49*Math.PI/60);
		rotationSeries.add(58*Math.PI/60);
		rotationSeries.add(65*Math.PI/60);
		rotationSeries.add(73*Math.PI/60);
		rotationSeries.add(80*Math.PI/60);
		rotationSeries.add(85*Math.PI/60);
		rotationSeries.add(90*Math.PI/60);
		rotationSeries.add(95*Math.PI/60);
		rotationSeries.add(96*Math.PI/60);
		
		helicopterArea.add(1.600);
		helicopterArea.add(1.616);
		helicopterArea.add(1.640);
		helicopterArea.add(1.656);
		helicopterArea.add(1.720);
		helicopterArea.add(1.760);
		helicopterArea.add(1.800);
		helicopterArea.add(1.840);
		helicopterArea.add(1.880);
		helicopterArea.add(1.920);
		helicopterArea.add(2.000);
		helicopterArea.add(2.400);
		helicopterArea.add(2.800);
		helicopterArea.add(3.200);
		helicopterArea.add(3.500);

		lTorques.add(0.0);
		lTorques.add(165.0);
		lTorques.add(180.0);
		lTorques.add(180.0);
		lTorques.add(170.0);
		lTorques.add(170.0);
		lTorques.add(150.0);
		lTorques.add(115.0);
		lTorques.add(97.0);
		lTorques.add(80.0);
		lTorques.add(70.0);
		lTorques.add(60.0);
		lTorques.add(50.0);
		lTorques.add(40.0);
		lTorques.add(1.0);

		fTorques.addAll(lTorques);
		hTorques.addAll(lTorques);

	}
	
	public static Double getAcceleration(Double speed, Double pos, ArrayList<Double> torques,Double gas, Double brake, Double mass){
		double FEng = gas * getValue(speedSeries, torques, speed) / 0.005;
		double FResistance = brake * 10000;
		double FEngResistance = 0.0005 * speed;
		double FHill = Math.sin(getValue(positionSeries, routeSlops, pos)) * g * mass;
		double FFinal = FEng - FResistance - FEngResistance - FHill;
		double Acceleration = FFinal / mass;
		
		return Acceleration;
	}
	
	
//	public static Double getHelicopterAcceleration(Double speed, Double pos, ArrayList<Double> rotations,Double gas, Double brake, Double mass){
//		double w = gas * getValue(helicopterSpeedSeries, rotations, speed);
//		double A = brake * getValue(helicopterSpeedSeries, helicopterArea, speed);
//		double FL = (r_density / 2) * C_L * Math.pow( w * r ,2) * 4 * r * c;
//		double FResistance = (r_density / 2) * C_D * Math.pow(speed, 2) * A;
//		double FFinal = FL - FResistance;
//		double Acceleration = FFinal / mass;
//
//		return Acceleration;
//	}

	public static Double getValue(ArrayList<Double> x, ArrayList<Double> y, Double key){
		double a[] = ArrayUtils.toPrimitive(x.toArray(new Double[x.size()]));
		double b[] = ArrayUtils.toPrimitive(y.toArray(new Double[y.size()]));
		UnivariateInterpolator interpolator = new LinearInterpolator();//Spline interpolation more accurate
		UnivariateFunction function = interpolator.interpolate(a,b);
		if(key < 0.0) key = 0.0;
		if(key > a[x.size()-1]) key = a[x.size()-1];
		double value = function.value(key);
		return value;
	}
}