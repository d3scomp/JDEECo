package cz.cuni.mff.d3s.deeco.runtime;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.w3c.dom.ranges.RangeException;

import cz.cuni.mff.d3s.deeco.knowledge.Component;

/**
 * RandomNetworkDistanceMiddlewareEntry provides the user with a middleware entry
 * represented here as a class containing a static field. 
 * It provides an access via a singleton to the integer-based distance matrix supplied by the abstract class
 * AbstractMiddlewareEntry.
 * updateDistanceMatrix updates the distance matrix with help of a random generator which grants a symetric distance
 * from one node to another.
 * @author Julien Malvot
 * @see AbstractMiddlewareEntry
 */
public class RandomNetworkDistanceMiddlewareEntry extends AbstractMiddlewareEntry<Integer> {
	
	private static RandomNetworkDistanceMiddlewareEntry middlewareEntry;
	
	/**
	 * getMiddlewareEntry returns a singleton of the RandomNetworkDistanceMiddlewareEntry instance.
	 * This returns a non-initialized instance if it has been called for the first time.
	 * Use updateDistanceMatrix with an input set of components for filling in the associated distance matrix
	 * @return middlewareEntry
	 * @author Julien Malvot
	 */
	public static RandomNetworkDistanceMiddlewareEntry getMiddlewareEntry(){
		if (middlewareEntry == null){
			middlewareEntry = new RandomNetworkDistanceMiddlewareEntry();
		}
		return middlewareEntry;
	}
	

    /** 
     * updateDistanceMatrix updates the distance matrix with help of a random generator which grants a symetric distance
     * from one node to another.
     * @param components The input list of components to compute the distance from.
     * @author Julien Malvot
     */
	@Override
	public void updateDistanceMatrix(List<Component> components) {
		Random rand = new Random();
		distanceMatrix = new Integer[components.size()][components.size()];
		for (int i = 0; i < components.size(); i++) {
			// generate the list of distances from one component to the others
			for (int j = 0; j <= i; j++){
				// we do the triangle of the matrix and its symetric
				distanceMatrix[i][j] = rand.nextInt(2);
				distanceMatrix[j][i] = distanceMatrix[i][j];
			}
		}
	}
}
