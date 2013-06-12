package cz.cuni.mff.d3s.deeco.performance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.exceptions.KMException;
import cz.cuni.mff.d3s.deeco.invokable.ParameterizedMethod;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableComponentProcess;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;

public class ComponentKnowledgeDecomoser extends KnowledgeDecomposer{

	public void inProcess(SchedulableComponentProcess scp, IPerformanceInfo pInfo, TimeStamp time, ParameterizedMethod process, KnowledgeManager km){
		for (int i = 0; i < process.in.size(); i++) {
			String ph=process.in.get(i).kPath.getEvaluatedPath(km, null, null, null);
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
				addKnowInfo(arrPath, arrValue, scp, pInfo,st,time);
				
			} catch (KMException e) {
				System.err.println(e.getMessage());
			}
		}		
	}
	
	public void outProcess(SchedulableComponentProcess scp, IPerformanceInfo pInfo, TimeStamp time, ParameterizedMethod process, KnowledgeManager km){
		for (int i = 0; i < process.out.size(); i++) {
			String ph=process.out.get(i).kPath.getEvaluatedPath(km, null, null, null);
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
				addKnowInfo(arrPath, arrValue, scp, pInfo,st,time);

			} catch (KMException e) {
				System.err.println(e.getMessage());
			}
		}		
	}
	
	public void inOutProcess(SchedulableComponentProcess scp, IPerformanceInfo pInfo, TimeStamp time, ParameterizedMethod process, KnowledgeManager km){
		for (int i = 0; i < process.inOut.size(); i++) {
			String ph=process.inOut.get(i).kPath.getEvaluatedPath(km, null, null, null);
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
				addKnowInfo(arrPath, arrValue, scp, pInfo,st,time);

			} catch (KMException e) {
				System.err.println(e.getMessage());
			}
		}		
	}

	
}



//if (kph instanceof Map) {
//Map<String, Object> map = (Map<String, Object>)kph;
//for (String key : map.keySet()) {
//	if (map.get(key) instanceof Object[]) {
//		Object [] array = (Object []) map.get(key);
//		if (array.length == 1) {
//			System.out.println("Value is: " + array[0]);
//		}
//	}
//}
//}

