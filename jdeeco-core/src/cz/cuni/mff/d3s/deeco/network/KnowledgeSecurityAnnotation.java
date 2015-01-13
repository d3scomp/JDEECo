package cz.cuni.mff.d3s.deeco.network;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Ondřej Štumpf
 *
 */
public class KnowledgeSecurityAnnotation implements Serializable {

	private static final long serialVersionUID = -7479104269351895942L;

	private final String roleName;
	private final Map<String, Object> roleArguments;
		
	public KnowledgeSecurityAnnotation(String roleName, Map<String, Object> roleArguments) {
		if (roleArguments == null) {
			roleArguments = new HashMap<>();
		}
		this.roleArguments = roleArguments;
		this.roleName = roleName;
	}
	
	public String getRoleName() {
		return roleName;	
	}
	
	public Map<String, Object> getRoleArguments() {
		return roleArguments;
	}
	
	@Override
	public int hashCode() {
		final int prime = 37;
		int result = 1;
		result = prime * result + ((roleName == null) ? 0 : roleName.hashCode());
		result = prime * result + ((roleArguments == null) ? 0 : roleArguments.hashCode());
		
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		
		if (obj instanceof KnowledgeSecurityAnnotation) {
			KnowledgeSecurityAnnotation other = (KnowledgeSecurityAnnotation) obj;
			
			boolean eq = true;
			if (roleArguments == null) {
				eq = eq && other.roleArguments == null;
			} else {
				eq = eq && roleArguments.equals(other.roleArguments);
			}
			
			if (roleName == null) {
				eq = eq && other.roleName == null;
			} else {
				eq = eq && roleName.equals(other.roleName);
			}
			
			return eq;
 		} else {
			return false;
		}
	}
}
