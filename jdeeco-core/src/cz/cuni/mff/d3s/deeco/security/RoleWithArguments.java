package cz.cuni.mff.d3s.deeco.security;

import java.util.Map;

/**
 * Holder for role name and its argument values.
 * @author Ondřej Štumpf
 *
 */
public final class RoleWithArguments {
	public final String roleName;
	public final Map<String, Object> arguments;
	
	/**
	 * Constructs new instance with the given role name and argument values.
	 * @param roleName
	 * @param arguments
	 */
	public RoleWithArguments(String roleName, Map<String, Object> arguments) {
		this.roleName = roleName;
		this.arguments = arguments;
	}
}
