package knowledgeAnalysis

import knowledge.xtext.KnowDEECoStandaloneSetup
import knowledge.KnowledgePackage
import org.eclipse.emf.common.util.URI
import java.io.File
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import knowledge.DEECoModel
import knowledge.Leaf

class KnowledgeByViliam {
	def static void main(String[] args) {
		val tool = new KnowledgeByViliam
		tool.start
	}
	
	def start() {
		KnowDEECoStandaloneSetup::doSetup
		KnowledgePackage::eINSTANCE.knowledgeFactory
		
		val resourceSet = new ResourceSetImpl
		val fileURI = URI::createFileURI(new File("example2.knowdeeco").getAbsolutePath())
		val resource = resourceSet.getResource(fileURI, true)
	
		val dmodel = resource.contents.head as DEECoModel
		
		val leaves = dmodel.knowledges.filter(typeof(Leaf))
		
		val types = leaves.map[type]
		
		println(leaves)
		println(types)
	}
}