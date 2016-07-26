package cz.cuni.mff.d3s.deeco.runtime;

public class PluginStartupFailedException extends DEECoException {

	/**
	 * Generated UID.
	 */
	private static final long serialVersionUID = -3536788554359614927L;

	public PluginStartupFailedException(final String msg) {
		super(msg);
	}

	public PluginStartupFailedException(final Throwable cause) {
		super(cause);
	}

	public PluginStartupFailedException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

}
