package cz.cuni.mff.d3s.jdeeco.publishing;

import java.util.concurrent.SynchronousQueue;

import org.nustaq.serialization.FSTConfiguration;

import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.network.KnowledgeData;

/**
 * The purpose of this plugin is to diagnose knowledge size
 * 
 * It works like knowledge publisher. Just instead of pulishing it prints detailed knowledge size 
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class KnowledgeSizeSampler extends DefaultKnowledgePublisher {
	static FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();
	
	@Override
	public void at(long time, Object triger) {
		for(KnowledgeData data : getLocalKnowledgeData()) {
			System.out.println("Knowledge data size report:");
			System.out.println("> Complete knowledge data: " + conf.asByteArray(data).length + " bytes:");		
			System.out.println(">> Authors: " + conf.asByteArray(data.getAuthors()).length + " bytes");
			System.out.println(">> Metadata: " + conf.asByteArray(data.getMetaData()).length + " bytes");
			System.out.println(">> RoleClasses: " + conf.asByteArray(data.getRoleClasses()).length + " bytes");
			System.out.println(">> Security: " + conf.asByteArray(data.getSecuritySet()).length + " bytes");
			ValueSet knowledge = data.getKnowledge();
			System.out.println(">> Knowledge: " + conf.asByteArray(knowledge).length + " bytes:");
			
			long knowledgeTotal = 0;
			for(KnowledgePath path: knowledge.getKnowledgePaths()) {
				long pathLength = conf.asByteArray(path).length;
				long valueLength = conf.asByteArray(knowledge.getValue(path)).length;
				System.out.println(">>> Path: " + path.toString() + " " + pathLength + " bytes");
				System.out.println(">>> Data: " + valueLength + " bytes");
				knowledgeTotal += pathLength;
				knowledgeTotal += valueLength;
			}
			System.out.println(">>> Computed total: " + knowledgeTotal + " bytes");
		}
	}
}
