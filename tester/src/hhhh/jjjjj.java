package hhhh;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.nonstiff.MidpointIntegrator;

public class jjjjj {

	public static void main(String[] args) {
		System.err.println("jjj");
		MidpointIntegrator integrator = new MidpointIntegrator(1);
		FirstOrderDifferentialEquations f = new Derivation();
		double[] v = new double[25];
		v[0] = 1;
		v[1] = 1;
		v[2] = 1;
		v[3] = 1;
	
		integrator.integrate(f, 0, v, 2, v);
		System.out.println(v[0]+"  "+v[1]+"  "+v[2]);
	}

	private static class Derivation implements FirstOrderDifferentialEquations {

		@Override
		public int getDimension() {
			return 25;
		}

		@Override
		public void computeDerivatives(double t, double[] y, double[] yDot)
				throws MaxCountExceededException, DimensionMismatchException {
			for (int i = 0; i < y.length-1; i++) {
				yDot[i] = y[i+1] ;
				System.out.println(yDot[i]);
//				yDot[i] = y[i] + y[i+1]*t;
//				System.out.println(yDot[i]+" = "+y[i]+" + "+y[i+1]+" * "+t);
			}
		}
	}
}
