package cz.cuni.mff.d3s.deeco.runners;

import cz.cuni.mff.d3s.deeco.runtime.DEECoException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;

public interface DEECoNodeFactory {

	public DEECoNode createNode(DEECoPlugin... nodeSpecificPlugins) throws DEECoException;

}
