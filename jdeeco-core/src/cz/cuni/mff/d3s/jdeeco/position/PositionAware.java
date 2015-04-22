package cz.cuni.mff.d3s.jdeeco.position;

import java.util.Collections;
import java.util.List;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;

public class PositionAware implements DEECoPlugin, PositionProvider {
	private final Position inital;
	private PositionProvider provider;
	
	public PositionAware(double x, double y) {
		this(new Position(x, y, 0));
	}

	public PositionAware(double x, double y, double z) {
		this(new Position(x, y, z));
	}

	public PositionAware(Position initialPosition) {
		inital = initialPosition;
	}

	public void setProvider(PositionProvider positionProvider) {
		if (provider != null) {
			throw new UnsupportedOperationException("POisiton provider is already set, sannot add another one.");
		}
		
		provider = positionProvider;
	}

	public Position getInitialPosition() {
		return inital;
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
			return inital;
		}
		
		Position fromProvider = provider.getPosition();
		
		if(fromProvider == null) {
			return inital;
		}
		
		return fromProvider;
	}
}
