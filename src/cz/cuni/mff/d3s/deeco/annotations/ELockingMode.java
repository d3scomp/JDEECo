package cz.cuni.mff.d3s.deeco.annotations;

/**
 * Enum type describing locking methods when invoking process.
 * 
 * WEAK - Locking is disabled during a process invocation
 * STRONG - Locking is enabled during the process invocation
 * 
 * @author Michal Kit
 *
 */
public enum ELockingMode {
	WEAK, STRONG
}
