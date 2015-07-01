package cz.cuni.mff.d3s.deeco.runtime;

public class PluginInitFailedException extends DEECoException {
	
	/**
	 * Generated UID.
	 */
	private static final long serialVersionUID = -3536788554359614927L;

	public PluginInitFailedException(final String msg) {
		super(msg);
	}

	public PluginInitFailedException(final Throwable cause) {
		super(cause);
	}
	
	public PluginInitFailedException(final String msg, final Throwable cause) {
		super(msg, cause);
	}
	
}
