package cz.cuni.mff.d3s.deeco.knowledgeAnalysis;

import java.io.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Vector;

import knowledge.AbstractElement;
import knowledge.AbstractKnowledge;
import knowledge.DEECoModel;
import knowledge.Ensemble;
import knowledge.KnowledgePackage;
import knowledge.Process;
import knowledge.xtext.KnowDEECoStandaloneSetup;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

public class KnowledgeOldnessAnalysisMain {

		
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		KnowledgeOldnessAnalysis know=new KnowledgeOldnessAnalysis();
		know.visitAllNodes();
		know.printAllElements();
//		know.print(know.knowHashMap.keySet());
//		know.print(know.knowHashMap.values());
	}	
}
