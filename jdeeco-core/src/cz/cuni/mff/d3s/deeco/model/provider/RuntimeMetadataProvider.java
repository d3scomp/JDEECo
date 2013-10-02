package cz.cuni.mff.d3s.deeco.model.provider;

import java.io.Serializable;

import cz.cuni.mff.d3s.deeco.model.RuntimeMetadata;

public class RuntimeMetadataProvider implements Serializable {

	private static final long serialVersionUID = 1L;
	
	protected RuntimeMetadata runtimeMetadata;
	
	public RuntimeMetadataProvider() {
		this.runtimeMetadata = new RuntimeMetadata();
	}

	public RuntimeMetadata getRuntimeMetadata() {
		return runtimeMetadata;
	}	
}
