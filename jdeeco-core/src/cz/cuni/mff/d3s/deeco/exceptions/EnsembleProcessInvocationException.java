package cz.cuni.mff.d3s.deeco.exceptions;

/**
 * When process-invocation related error occurs.
 * This is specifically localized in the SchedulableEnsembleProcess
 * @author Julien
 *
 */
public class EnsembleProcessInvocationException extends Exception {
	public final static long serialVersionUID = 1L;

	public EnsembleProcessInvocationException(String message) {
		super(message);
	}
}
