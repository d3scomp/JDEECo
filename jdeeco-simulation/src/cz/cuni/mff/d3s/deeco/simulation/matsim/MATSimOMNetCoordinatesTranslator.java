package cz.cuni.mff.d3s.deeco.simulation.matsim;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.Node;
import org.matsim.core.utils.geometry.CoordImpl;

/**
 * Because the coordinates in MATSim are given in meters in large numbers, this
 * class takes care to translate coordinates from MATSim to OMNet, which are
 * much smaller.
 * 
 * 
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public class MATSimOMNetCoordinatesTranslator {

	private final double OMNetSizeX;
	private final double OMNetSizeY;
	private final Coord translationVector;

	public MATSimOMNetCoordinatesTranslator(Network network) {
		double maxX = 0, maxY = 0, minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
		double currentX, currentY;
		for (Node node : network.getNodes().values()) {
			currentX = node.getCoord().getX();
			currentY = node.getCoord().getY();
			if (currentX > maxX) {
				maxX = currentX;
			} else if (currentX < minX) {
				minX = currentX;
			}
			if (currentY > maxY) {
				maxY = currentY;
			} else if (currentY < minY) {
				minY = currentY;
			}
		}
		// Let's give 50m boundary
		minX -= 50.0;
		minY -= 50.0;
		maxX += 50.0;
		maxY += 50.0;
		// Calculate OMNet sizes
		this.OMNetSizeX = maxX - minX;
		this.OMNetSizeY = maxY - minY;
		this.translationVector = new CoordImpl(-minX, -minY);
	}

	public double getOMNetSizeX() {
		return OMNetSizeX;
	}

	public double getOMNetSizeY() {
		return OMNetSizeY;
	}

	public Coord fromMATSimToOMNet(Coord coord) {
		return new CoordImpl(coord.getX() + translationVector.getX(),
				coord.getY() + translationVector.getY());
	}

}
