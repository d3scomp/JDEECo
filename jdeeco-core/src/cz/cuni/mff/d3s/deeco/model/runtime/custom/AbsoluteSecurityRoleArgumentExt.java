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

import cz.cuni.mff.d3s.deeco.model.runtime.api.AbsoluteSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.impl.AbsoluteSecurityRoleArgumentImpl;

/**
 * 
 * @author Ondřej Štumpf
 *
 */
public class AbsoluteSecurityRoleArgumentExt extends AbsoluteSecurityRoleArgumentImpl implements Serializable {

	private static final long serialVersionUID = 4072094973100189590L;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbsoluteSecurityRoleArgumentExt other = (AbsoluteSecurityRoleArgumentExt) obj;
		
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;		
		
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;	
		
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 17;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}
	
	@Override
	public String toString() {
		return String.format("Absolute Security Role Argument [name=%s,value=%s]", name, value);
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
			AbsoluteSecurityRoleArgument argument = (AbsoluteSecurityRoleArgument) res.getContents().get(0);
			
			setName(argument.getName());
			setValue(argument.getValue());
		} catch (Exception e) {
			throw e;
		}
	}
}
