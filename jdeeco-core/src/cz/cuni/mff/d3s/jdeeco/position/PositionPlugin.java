package cz.cuni.mff.d3s.jdeeco.position;

import java.util.Collections;
import java.util.List;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;

public class PositionPlugin implements DEECoPlugin, PositionProvider {
	private Position staticPosition;
	private PositionProvider provider;
	
	public PositionPlugin(double x, double y) {
		this(new Position(x, y, 0));
	}

	public PositionPlugin(double x, double y, double z) {
		this(new Position(x, y, z));
	}

	public PositionPlugin(Position initialPosition) {
		staticPosition = initialPosition;
	}

	public void setProvider(PositionProvider positionProvider) {
		if (provider != null) {
			throw new UnsupportedOperationException("Poisiton provider is already set, cannot add another one.");
		}
		
		provider = positionProvider;
	}

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
		// nothing to do here
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
