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

import cz.cuni.mff.d3s.deeco.model.runtime.api.PathSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.impl.PathSecurityRoleArgumentImpl;

/**
 * 
 * @author Ondřej Štumpf
 *
 */
public class PathSecurityRoleArgumentExt extends PathSecurityRoleArgumentImpl implements Serializable {

	private static final long serialVersionUID = 5680133272326377414L;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PathSecurityRoleArgumentExt other = (PathSecurityRoleArgumentExt) obj;
		
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;		
		
		if (knowledgePath == null) {
			if (other.knowledgePath != null)
				return false;
		} else if (!knowledgePath.equals(other.knowledgePath))
			return false;	
		
		if (contextKind == null) {
			if (other.contextKind != null)
				return false;
		} else if (!contextKind.equals(other.contextKind))
			return false;	
		
		
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 17;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((knowledgePath == null) ? 0 : knowledgePath.hashCode());
		result = prime * result + ((contextKind == null) ? 0 : contextKind.hashCode());
		return result;
	}
	
	@Override
	public String toString() {
		return String.format("Path Security Role Argument [name=%s,path=%s,kind=%s]", name, knowledgePath,contextKind);
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
			PathSecurityRoleArgument argument = (PathSecurityRoleArgument) res.getContents().get(0);
			
			setName(argument.getName());
			setKnowledgePath(argument.getKnowledgePath());	
			setContextKind(argument.getContextKind());
		} catch (Exception e) {
			throw e;
		}
	}
}
