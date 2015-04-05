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

import cz.cuni.mff.d3s.deeco.model.runtime.api.WildcardSecurityTag;
import cz.cuni.mff.d3s.deeco.model.runtime.impl.WildcardSecurityTagImpl;

/**
 * 
 * @author Ondřej Štumpf
 *
 */
public class WildcardSecurityTagExt extends WildcardSecurityTagImpl implements Serializable {

	private static final long serialVersionUID = 8882348465110736564L;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WildcardSecurityTagExt other = (WildcardSecurityTagExt) obj;
		
		if (accessRights == null) {
			if (other.accessRights != null)
				return false;
		} else if (!accessRights.equals(other.accessRights))
			return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 71;
		int result = 1;
		result = prime * result + ((accessRights == null) ? 0 : accessRights.hashCode());
		return result;
	}
	
	@Override
	public String toString() {
		return String.format("Wildcard Security Tag [access=%s]", accessRights);
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
			WildcardSecurityTag tag = (WildcardSecurityTag) res.getContents().get(0);
			
			setAccessRights(tag.getAccessRights());
		} catch (Exception e) {
			throw e;
		}
	}
}
