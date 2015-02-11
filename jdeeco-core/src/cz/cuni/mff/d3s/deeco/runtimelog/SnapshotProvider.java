package cz.cuni.mff.d3s.deeco.runtimelog;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;

/**
 * The interface for {@link ComponentInstance}s that are able to provide
 * a snapshot of theirs knowledge.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public interface SnapshotProvider
{
	/**
	 * Provides a snapshot of the {@link ComponentInstance}s knowledge.
	 * @return The {@link RuntimeLogRecord} containing the knowledge snapshot.
	 */
	public RuntimeLogRecord getSnapshot();
}
