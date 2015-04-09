package cz.cuni.mff.d3s.deeco.runtime;

public class DEECoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new {@link DEECoException} with the specified detail <em>message</em>.
	 * The cause is not initialized, and may subsequently be initialized by a call to initCause.
	 * @param message is the detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
	 */
	public DEECoException(String message) {
		super(message);
	}
	
	/**
	 * Constructs a new {@link DEECoException} with the specified <em>cause</em> and a detail <em>message</em>
	 * of (cause == null ? null : cause.toString()) (which typically contains the class and detail message of cause).
	 * This constructor is useful for exceptions that are little more than wrappers for other throwables
	 * (for example, {@link java.security.PrivilegedActionException}).
	 * @param cause is the cause (which is saved for later retrieval by the {@link #getCause()} method).
	 * (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public DEECoException(Throwable cause)
	{
		super(cause);
	}
	
	/**
	 * <p>Constructs a new {@link DEECoException} with the specified detail <em>message</em> and <em>cause</em>.</p>
	 * <p>Note that the detail <em>message</em> associated with <em>cause</em> is not automatically incorporated
	 * in this exception's detail message.</p>
	 * @param message is the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
	 * @param cause is the cause (which is saved for later retrieval by the {@link #getCause()} method).
	 * (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public DEECoException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
