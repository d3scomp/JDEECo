package demo.broadcast.ranging;

import java.util.Arrays;
import java.util.List;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.runtime.PluginInitFailedException;
import cz.cuni.mff.d3s.jdeeco.position.Position;
import cz.cuni.mff.d3s.jdeeco.position.PositionPlugin;
import cz.cuni.mff.d3s.jdeeco.position.PositionProvider;

public class CustomPosition implements DEECoPlugin, PositionProvider {
	private Position position;
	
	@Override
	public Position getPosition() {
		return position;
	}
	
	public void setPosition(Position position) {
		this.position = position;
	}

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return Arrays.asList(PositionPlugin.class);
	}

	@Override
	public void init(DEECoContainer container) throws PluginInitFailedException {
		PositionPlugin positionPlugin = container.getPluginInstance(PositionPlugin.class);
		position = positionPlugin.getStaticPosition();
		positionPlugin.setProvider(this);
	}
}
