package cz.cuni.mff.d3s.deeco.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import cz.cuni.mff.d3s.deeco.knowledge.Component;

/**
 * 
 * @author Julien Malvot
 *
 * @param <T> The type of distance to store (e.g. physical distance with Double, network distance with Integer)
 */
public abstract class AbstractMiddlewareEntry<T> {

	protected T[][] distanceMatrix;
	
	public T getDistance(Integer fromIdIndex, Integer toIdIndex) {
		return distanceMatrix[fromIdIndex][toIdIndex];
	}
	
	public String[] distanceMatrixToString(){
		String[] matStr = new String[distanceMatrix.length+2];
		matStr[0] = "[src]\\[dest]";
		matStr[1] = " ";
		for (int i = 0; i < distanceMatrix.length; i++) {
			matStr[i+2] = i + " ";
			for (int j = 0; j < distanceMatrix[i].length; j++) {
				if (i == 0) 
					matStr[1] += " " + j;
				matStr[i+2] += distanceMatrix[i][j] + " ";
			}
		}
		return matStr;
	}
	
	abstract public void updateDistanceMatrix(List<Component> components);
}
