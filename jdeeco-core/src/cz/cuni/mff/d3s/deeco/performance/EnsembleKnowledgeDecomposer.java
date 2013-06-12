package cz.cuni.mff.d3s.deeco.performance;

import java.util.ArrayList;

import cz.cuni.mff.d3s.deeco.exceptions.KMException;
import cz.cuni.mff.d3s.deeco.invokable.ParameterizedMethod;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableComponentProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcess;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;

public class EnsembleKnowledgeDecomposer extends KnowledgeDecomposer{

	public void inEnsemble(SchedulableEnsembleProcess sep, IPerformanceInfo pInfo, TimeStamp time, ParameterizedMethod knowledgeExchange, KnowledgeManager km, String coord, String mem){
		for (int i = 0; i < knowledgeExchange.in.size(); i++) {
			String ph=knowledgeExchange.in.get(i).kPath.getEvaluatedPath(km, coord, mem, null);
			String st="in";
			try {
				Object kph = km.getKnowledge(ph);
				ArrayList arrPath=new ArrayList();
				ArrayList arrValue=new ArrayList();
				arrPath.add(ph);
				arrValue.add(kph);
				boolean hm=true;
				while (hm) {
					hm=false;
					for (int j = 0; j < arrValue.size(); j++) {
						hm=checkHashMap(arrPath, arrValue, j, hm);
					}
				}
				addKnowInfo(arrPath, arrValue, sep, pInfo,st,time);
				
			} catch (KMException e) {
				System.err.println(e.getMessage());
			}
		}		
	}
	

	
	public void outEnsemble(SchedulableEnsembleProcess sep, IPerformanceInfo pInfo, TimeStamp time, ParameterizedMethod knowledgeExchange, KnowledgeManager km, String coord, String mem){
		for (int i = 0; i < knowledgeExchange.out.size(); i++) {
			String ph=knowledgeExchange.out.get(i).kPath.getEvaluatedPath(km, coord, mem, null);
			String st="out";
			try {
				Object kph = km.getKnowledge(ph);
				ArrayList arrPath=new ArrayList();
				ArrayList arrValue=new ArrayList();
				arrPath.add(ph);
				arrValue.add(kph);
				boolean hm=true;
				
				while (hm) {
					hm=false;
					for (int j = 0; j < arrValue.size(); j++) {
						hm=checkHashMap(arrPath, arrValue, j, hm);
					}
				}
				
				addKnowInfo(arrPath, arrValue, sep, pInfo,st,time);

			} catch (KMException e) {
				System.err.println(e.getMessage());
			}
		}		
	}
	

	
	public void inOutEnsemble(SchedulableEnsembleProcess sep, IPerformanceInfo pInfo, TimeStamp time, ParameterizedMethod knowledgeExchange, KnowledgeManager km, String coord, String mem){
		for (int i = 0; i < knowledgeExchange.inOut.size(); i++) {
			String ph=knowledgeExchange.inOut.get(i).kPath.getEvaluatedPath(km, coord, mem, null);
			String st="inOut";
			try {
				Object kph = km.getKnowledge(ph);
				ArrayList arrPath=new ArrayList();
				ArrayList arrValue=new ArrayList();
				arrPath.add(ph);
				arrValue.add(kph);
				boolean hm=true;
				
				while (hm) {
					hm=false;
					for (int j = 0; j < arrValue.size(); j++) {
						hm=checkHashMap(arrPath, arrValue, j, hm);
					}
				}
				
				addKnowInfo(arrPath, arrValue, sep, pInfo,st,time);

			} catch (KMException e) {
				System.err.println(e.getMessage());
			}
		}		
	}

	
}
