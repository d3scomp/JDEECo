package cz.cuni.mff.d3s.deeco.model.runtime.custom;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl;

import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityRole;
import cz.cuni.mff.d3s.deeco.model.runtime.impl.SecurityRoleImpl;

public class SecurityRoleExt extends SecurityRoleImpl implements Serializable {

	private static final long serialVersionUID = -4748717283540770244L;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SecurityRoleExt other = (SecurityRoleExt) obj;
		
		if (roleName == null) {
			if (other.roleName != null)
				return false;
		} else if (!roleName.equals(other.roleName))
			return false;
		
		if (arguments == null) {
			if (other.arguments != null)
				return false;
		} else if (!arguments.equals(other.arguments))
			return false;
		
		if (consistsOf == null) {
			if (other.consistsOf != null)
				return false;
		} else if (!consistsOf.equals(other.consistsOf))
			return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 17;
		int result = 1;
		result = prime * result + ((roleName == null) ? 0 : roleName.hashCode());
		result = prime * result + ((arguments == null) ? 0 : arguments.hashCode());
		result = prime * result + ((consistsOf == null) ? 0 : consistsOf.hashCode());
		return result;
	}
	
	@Override
	public String toString() {
		return String.format("Security Role [name=%s,args=%s,consistsOf=%s]", roleName, 
				arguments.stream().map(arg -> arg.toString()).collect(Collectors.joining(",")),
				consistsOf.stream().map(p -> p.toString()).collect(Collectors.joining(",")));
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException {
		XMLResource res = new XMLResourceImpl();
		res.getContents().add(this);
		StringWriter sw = new StringWriter();
		res.save(sw, null);
		
		String encoded = sw.toString();
		out.writeUTF(encoded);
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {		
		XMLResource res = new XMLResourceImpl();			
		String encoded = in.readUTF();
		
		try {
			InputStream stream = new ByteArrayInputStream(encoded.getBytes());
			res.load(stream, null);
			SecurityRole role = (SecurityRole) res.getContents().get(0);
			
			setRoleName(role.getRoleName());
			getArguments().addAll(role.getArguments());
			getConsistsOf().addAll(role.getConsistsOf());
		} catch (Exception e) {
			throw e;
		}
	}
}
