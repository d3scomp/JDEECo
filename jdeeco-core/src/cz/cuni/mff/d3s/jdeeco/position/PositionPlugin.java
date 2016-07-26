package cz.cuni.mff.d3s.jdeeco.position;

import java.util.Collections;
import java.util.List;

import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;

/**
 * Position providing plug-in
 * 
 * This plug-in can work in two modes. In static mode the position is set in constructor and can be updated by explicit
 * call to setStaticPosition. In provided mode the position is determined by PositionProvider registered with this
 * plug-in. In order to avoid misunderstanding the provider can be set only once.
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class PositionPlugin implements DEECoPlugin, PositionProvider {
	private DEECoContainer container;
	private Position staticPosition;
	private PositionProvider provider = null;

	public PositionPlugin(double x, double y) {
		this(new Position(x, y, 0));
	}

	public PositionPlugin(double x, double y, double z) {
		this(new Position(x, y, z));
	}

	public PositionPlugin(Position initialPosition) {
		staticPosition = initialPosition;
	}

	/**
	 * Sets position provider
	 * 
	 * Once set the provider cannot be removed or replaced and provides the current position of the node.
	 * 
	 * @param positionProvider
	 */
	public void setProvider(PositionProvider positionProvider) {
		if (provider != null) {
			throw new UnsupportedOperationException("Position provider is already set, cannot add another one.");
		}
		provider = positionProvider;
		Log.i(container.getId() + ": " + this.getClass().getSimpleName() + " now using "
				+ provider.getClass().getSimpleName() + " as node position provider.");
	}

	/**
	 * Gets static position of the node
	 * 
	 * Static position is not provided by the provider
	 * 
	 * @return Static node position
	 */
	public Position getStaticPosition() {
		return staticPosition;
	}

	public void setStaticPosition(Position position) {
		staticPosition = position;
	}

	public void setStaticPosition(double x, double y) {
		staticPosition = new Position(x, y, 0);
	}

	public void setStaticPosition(double x, double y, double z) {
		staticPosition = new Position(x, y, z);
	}

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return Collections.emptyList();
	}

	@Override
	public void init(DEECoContainer container) {
		this.container = container;
	}

	@Override
	public Position getPosition() {
		if (provider == null) {
			return staticPosition;
		} else {
			return provider.getPosition();
		}
	}
}
