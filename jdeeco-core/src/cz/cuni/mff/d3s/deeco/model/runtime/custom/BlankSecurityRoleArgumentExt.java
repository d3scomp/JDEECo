package cz.cuni.mff.d3s.deeco.model.runtime.custom;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StringWriter;

import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl;

import cz.cuni.mff.d3s.deeco.model.runtime.api.BlankSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.impl.BlankSecurityRoleArgumentImpl;

public class BlankSecurityRoleArgumentExt extends BlankSecurityRoleArgumentImpl implements Serializable {

	private static final long serialVersionUID = 1559162721313167821L;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlankSecurityRoleArgumentExt other = (BlankSecurityRoleArgumentExt) obj;
		
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;		
		
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 17;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	
	@Override
	public String toString() {
		return String.format("Blank Security Role Argument [name=%s]", name);
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
			BlankSecurityRoleArgument argument = (BlankSecurityRoleArgument) res.getContents().get(0);
			
			name = argument.getName();
		} catch (Exception e) {
			throw e;
		}
	}
}
