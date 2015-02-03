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

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeSecurityTag;
import cz.cuni.mff.d3s.deeco.model.runtime.impl.KnowledgeSecurityTagImpl;

/**
 * 
 * @author Ondřej Štumpf
 *
 */
public class KnowledgeSecurityTagExt extends KnowledgeSecurityTagImpl implements Serializable {

	private static final long serialVersionUID = 8972443112574780902L;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KnowledgeSecurityTagExt other = (KnowledgeSecurityTagExt) obj;
		
		if (requiredRole == null) {
			if (other.requiredRole != null)
				return false;
		} else if (!requiredRole.equals(other.requiredRole))
			return false;
				
		if (accessRights == null) {
			if (other.accessRights != null)
				return false;
		} else if (!accessRights.equals(other.accessRights))
			return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 61;
		int result = 1;
		result = prime * result + ((requiredRole == null) ? 0 : requiredRole.hashCode());
		result = prime * result + ((accessRights == null) ? 0 : accessRights.hashCode());
		return result;
	}
	
	@Override
	public String toString() {
		return String.format("Knowledge Security Tag [role=%s, access=%s]", requiredRole, accessRights);
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
			KnowledgeSecurityTag tag = (KnowledgeSecurityTag) res.getContents().get(0);
			
			setRequiredRole(tag.getRequiredRole());
			setAccessRights(tag.getAccessRights());
		} catch (Exception e) {
			throw e;
		}
	}
}
