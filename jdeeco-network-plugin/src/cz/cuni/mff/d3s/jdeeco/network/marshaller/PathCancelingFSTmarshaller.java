package cz.cuni.mff.d3s.jdeeco.network.marshaller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.KnowledgePathExt;
import cz.cuni.mff.d3s.deeco.network.KnowledgeData;
import cz.cuni.mff.d3s.deeco.network.KnowledgeMetaData;

/**
 * This knowledge marshaler improves standard FST marshaler by removing model objects
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class PathCancelingFSTmarshaller extends FSTMarshaller {
	static class CompressedKnowledgeData implements Serializable {
		private static final long serialVersionUID = 1L;

		static class CompresedValueSet implements Serializable {
			private static final long serialVersionUID = 1L;
			private Map<String, Object> values = new HashMap<>();

			public CompresedValueSet(ValueSet valueSet) {
				for (KnowledgePath path : valueSet.getKnowledgePaths()) {
					values.put(path.toString(), valueSet.getValue(path));
				}
			}

			public ValueSet toValueSet() {
				ValueSet valueSet = new ValueSet();
				for (Entry<String, Object> valueEntry : values.entrySet()) {
					KnowledgePath path = KnowledgePathExt.createKnowledgePath(valueEntry.getKey());
					valueSet.setValue(path, valueEntry.getValue());
				}
				return valueSet;
			}
		}

		private CompresedValueSet knowledge;
		private CompresedValueSet securitySet;
		private CompresedValueSet authors;
		private List<String> roleClasses;
		private KnowledgeMetaData metaData;

		public CompressedKnowledgeData(KnowledgeData knowledgeData) {
			this.knowledge = new CompresedValueSet(knowledgeData.getKnowledge());
			this.securitySet = new CompresedValueSet(knowledgeData.getSecuritySet());
			this.authors = new CompresedValueSet(knowledgeData.getAuthors());
			this.roleClasses = knowledgeData.getRoleClasses();
			this.metaData = knowledgeData.getMetaData();
		}

		public KnowledgeData toKnowledgeData() {
			return new KnowledgeData(knowledge.toValueSet(), securitySet.toValueSet(), authors.toValueSet(),
					roleClasses, metaData);
		}
	}

	@Override
	public byte[] marshall(Object data) throws Exception {
		KnowledgeData knowledgeData = (KnowledgeData) data;
		CompressedKnowledgeData compresedKnowledgeData = new CompressedKnowledgeData(knowledgeData);
//		System.out.println("Standatd: " + super.marshall(knowledgeData).length + " bytes");
//		System.out.println("Compresed: " + super.marshall(compresedKnowledgeData).length + " bytes");
		return super.marshall(compresedKnowledgeData);
	}

	@Override
	public Object unmashall(byte[] data) throws Exception {
		CompressedKnowledgeData compresedKnowledgeData = (CompressedKnowledgeData) super.unmashall(data);
		return compresedKnowledgeData.toKnowledgeData();
	}
}
