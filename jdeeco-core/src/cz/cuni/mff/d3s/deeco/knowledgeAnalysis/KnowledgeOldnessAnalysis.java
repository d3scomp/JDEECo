package cz.cuni.mff.d3s.deeco.knowledgeAnalysis;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import knowledge.AbstractElement;
import knowledge.AbstractKnowledge;
import knowledge.DEECoModel;
import knowledge.Ensemble;
import knowledge.KnowledgeElement;
import knowledge.KnowledgePackage;
import knowledge.Leaf;
import knowledge.Process;
import knowledge.xtext.KnowDEECoStandaloneSetup;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;


/*
 * *******************************IMPORTANT***************************
 * I have to change the search way from not visited node search back and visit all not visited nodes
 * in the path to the visited node or till we discover a node written by process
 * 
 */

public class KnowledgeOldnessAnalysis {

	
	protected DEECoModel dmodel;
	protected TreeIterator<EObject> dm;
	protected LinkedList<EObject> list=new LinkedList<EObject>();
	protected LinkedHashMap<AbstractKnowledge, KnowledgeInformation> knowHashMap;
	
	
	/*
	 * Constructor
	 */
	public KnowledgeOldnessAnalysis() {
		KnowDEECoStandaloneSetup.doSetup();
		KnowledgePackage dmmp=KnowledgePackage.eINSTANCE;
		
		ResourceSet resourceSet=new ResourceSetImpl();
		URI fileURI= URI.createFileURI(new File("example2.knowdeeco").getAbsolutePath());
		Resource resource=resourceSet.getResource(fileURI, true);
	
		EObject eobj = resource.getContents().get(0);
		dmodel = (DEECoModel) eobj;
		dm = dmodel.eAllContents();
		createList();
		knowHashMap=new LinkedHashMap<AbstractKnowledge, KnowledgeInformation>();
		System.out.println("constructer");
		init();

	}
	
	
	/*
	 * Initialization 
	 */
	protected void init(){
		System.out.println("init...");
		for (int i = 0; i < list.size(); i++) {
			EObject type = list.get(i);
			if (type instanceof Leaf) {
				AbstractElement writer=((Leaf) type).getWriter();
				EList<AbstractElement> reader = ((Leaf) type).getReader();
				if(writer != null){
						if (writer instanceof Process){
							knowHashMap.put((Leaf) type,new KnowledgeInformation(writer,reader,2*writer.getPeriod(),true));
						}else {
							knowHashMap.put(((Leaf) type), new KnowledgeInformation(writer,reader,0,false));;
						}
				}
			}
			
		}
	}
	
	
	/*
	 * visiting all knowledge nodes
	 */
	public void visitAllNodes(){
		
		while(!isAllNodesVisited()){
			Iterator<EObject> itr=list.iterator();
			while(itr.hasNext()) {
				EObject nextKnow=itr.next();
				if (isLeaf(nextKnow) && knowHashMap.containsKey((Leaf)nextKnow)){
					Leaf leaf=(Leaf)nextKnow;
					KnowledgeInformation leafInfo=knowHashMap.get(leaf);
					knowledgeOldnessCalc(leaf);
				}
			}
		}
	}
	
	
	
	///////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////ALGORITHM///////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////
	protected void knowledgeOldnessCalc(Leaf leafNode){
		int oldness = 0;
		KnowledgeInformation leaf = knowHashMap.get(leafNode);
		AbstractElement eWriter=leaf.getWriter();
		boolean visited=leaf.isVisited();
		if(!visited){
			if (eWriter instanceof Ensemble) {
					oldness+=2*eWriter.getPeriod() + oldnessOfPath(eWriter);
					leaf.setOldness(oldness);
					leaf.setVisited(true);
				}
		}
	}
	

	/*
	 * values in previous knowledge in the path
	 */
	protected int oldnessOfPath(AbstractElement eWriter) {
		int val=-1;
		int max=-1;
		Iterator<EObject> itr = list.iterator();
		while (itr.hasNext()) {
			EObject typeObj =itr.next();
			if(typeObj instanceof AbstractKnowledge){
				AbstractKnowledge type=(AbstractKnowledge) typeObj;
				EList<AbstractElement> eReaders = type.getReader();
				if(eReaders.contains(eWriter)){
					if(type instanceof Leaf){
						if (knowHashMap.containsKey(type)) 
							val=knowHashMap.get(type).getOldness();
						else 
							val=0; // in case of constant
					}else 
						if(type instanceof KnowledgeElement){
								val=maxChildrenVal(type);
						}
					}
					if(max<val)
						max=val;
				}
		}
		return max; 
	}

	
	/*
	 * choose the maximum value of the children nodes
	 */
	protected int maxChildrenVal(AbstractKnowledge goal) {
		int val=-1;
		int max=-1;
		Iterator<EObject> itr=goal.eAllContents();
		while (itr.hasNext()) {
			AbstractKnowledge type = (AbstractKnowledge) itr.next();
			if(type instanceof Leaf)
				val=knowHashMap.get(type).getOldness();
			if(max<val)
				max=val;
			
		}
		return max;
	}
	
	
	/*
	 * check if all nodes ( leafs not constant-written by some process or ensemble ) are visited
	 */
	public boolean isAllNodesVisited(){
		Iterator<EObject> itr=list.iterator();
		while (itr.hasNext()) {
			EObject var=itr.next();
			if (isLeaf(var)){
					if(knowHashMap.get(var).isVisited()){
						 continue;
					}else return false; 
			}
		}
		return true;
		
	}

	/*
	 * save all nodes in list to create iterator using it
	 */
	protected void createList(){
		while (dm.hasNext()) {
			list.add(dm.next());
		}
	}
	
	/*
	 * making sure that the node is a leaf and it's saved in HashMap ( not constant value )
	 */
	public boolean isLeaf(EObject nextKnow){
		if (nextKnow instanceof Leaf && knowHashMap.containsKey((Leaf)nextKnow)){
			return true;
			}
		return false;
	}
	
	/*
	 * printing all the nodes
	 */
	public void printAllElements(){
		System.out.println("====================================================================");
		dm=dmodel.eAllContents();
		System.out.println(dm.hasNext());
		while (dm.hasNext()) {
			EObject nextKnow=dm.next();
			if (nextKnow instanceof Leaf && knowHashMap.containsKey((Leaf)nextKnow))
			System.out.println(nextKnow+"        "+knowHashMap.get((Leaf)nextKnow).getOldness()+" visited:"+knowHashMap.get((Leaf)nextKnow).isVisited());
		}
	}
	
}
