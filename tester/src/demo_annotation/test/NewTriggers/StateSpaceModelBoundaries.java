package demo_annotation.test.NewTriggers;
//
//import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
//import org.apache.commons.math3.exception.DimensionMismatchException;
//import org.apache.commons.math3.exception.MaxCountExceededException;
//import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
//import org.apache.commons.math3.ode.FirstOrderIntegrator;
//import org.apache.commons.math3.ode.nonstiff.MidpointIntegrator;
//
//
//
public class StateSpaceModelBoundaries<T> {
//	
//	public ModelInterface modelName = null;
//	public InaccurateValueDefinition<T>[] in;
//	public InaccurateValueDefinition<T>[] inDerv;
//	
//	private static double lastTime;
//
//	protected static final double SEC_NANOSECOND_FACTOR = 1000000000;
//	
//	public StateSpaceModelBoundaries(ModelInterface modelName, Double[] in, Double[] inDerv, Double creationTime) {
//		initBoundaries(this.in, in , creationTime);
//		initBoundaries(this.inDerv, inDerv, creationTime);
//		this.modelName = modelName;
//		computeBeliefBoundaries();
//	}
//	
//
//	public void computeBeliefBoundaries() {
//
//		double currentTime = System.nanoTime() / SEC_NANOSECOND_FACTOR;
//		double[] boundaries = new double[1];
//		double 	startTime = startTime();
//		double dt = lastTime - startTime;
//		// ---------------------- knowledge evaluation --------------------------------
//		ModelInterface m;
//		try {
//			m = (ModelInterface) modelName.newInstance();
//			inDerv[0] = modelName.getModelBoundaries(in);
//		} catch (IllegalAccessException | InstantiationException e) {
//			e.printStackTrace();
//		}
//		
//		FirstOrderIntegrator integrator = new MidpointIntegrator(1);
//		integrator.setMaxEvaluations((int) dt);
//		FirstOrderDifferentialEquations f = new Derivation(); 
//		//-----------------------  Boundaries    ----------------------------------
//		for (int i = 0; i < in.length; i++) {
//			computeBoundary(boundaries, i, f, integrator, startTime, currentTime);
//		}
//	
//	}
//
//	private void computeBoundary(double[] boundaries, int i, FirstOrderDifferentialEquations f,
//			FirstOrderIntegrator integrator, double startTime, double currentTime) {
//		
//		double[] minBoundaries = new double[1];
//		minBoundaries[0] = boundaries[0];
//		integrator.integrate(f, startTime, minBoundaries, currentTime, minBoundaries);
//		inDerv[i].minBoundary = inDerv[i].minBoundary.doubleValue() + minBoundaries[0];
//		boundaries[0] = minBoundaries[0];
//		
//		double[] maxBoundaries = new double[1];
//		maxBoundaries[0] = boundaries[1];
//		integrator.integrate(f, startTime, maxBoundaries, currentTime, maxBoundaries);
//		inDerv[i].maxBoundary = inDerv[i].maxBoundary.doubleValue() + maxBoundaries[0];
//		boundaries[1] = maxBoundaries[0];
//	}
//
//	private void resetBoundaries(){
//		for (int i = 0; i < in.length; i++) {
//			in[i].minBoundary = in[i].value; 
//			in[i].maxBoundary = in[i].value; 
//		}
//	}
//	
//	private Double startTime(){
//		
//		Double startTime;
//		if (in[0].creationTime <= lastTime) {
//			startTime = lastTime;
//		} else {
//			startTime = in[0].creationTime;
//			resetBoundaries();
//		}
//		return startTime;
//	}
//	
//	private static class Derivation implements FirstOrderDifferentialEquations {
//
//		@Override
//		public int getDimension() {
//			return 1;
//		}
//
//		@Override
//		public void computeDerivatives(double t, double[] y, double[] yDot)
//				throws MaxCountExceededException, DimensionMismatchException {
//			int params = 1;
//			int order = 1;
//			DerivativeStructure x = new DerivativeStructure(params, order, 0,
//					y[0]);
//			DerivativeStructure f = x.divide(t);
//			yDot[0] = f.getValue();
//		}
//	}
//	
//	
//	private void initBoundaries(InaccurateValueDefinition<T>[] inArr, Double[] arr,  Double creationTime){
//		for (int i = 0; i < arr.length; i++) {
//				inArr[i].value = arr[i];
//				inArr[i].minBoundary = arr[i];
//				inArr[i].maxBoundary = arr[i];
//				inArr[i].creationTime = creationTime;
//		}
//	}
//
}
