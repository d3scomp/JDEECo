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
import cz.cuni.mff.d3s.deeco.runtime.Runtime;

public class ProcessParametersDecomposer extends ProcessKnowledgeDecomposer{


	public ProcessParametersDecomposer(KnowledgeManager km, SchedulableComponentProcess scp, IPerformanceInfo pInfo){
		this.km=km;
		this.sp=scp;
		this.pInfo=pInfo;
	}
	
	
	public void inProcess(TimeStamp time, ParameterizedMethod process){
		init();
		String st="in";
		for (int i = 0; i < process.in.size(); i++) {
			String ph=process.in.get(i).kPath.getEvaluatedPath(km, null, null, null);
			try {
				Object kph = km.getKnowledge(ph);
				init();
				arrPath.add(ph);
				arrValue.add(kph);
				boolean hm=true;
				while (hm) {
					hm=false;
					for (int j = 0; j < arrValue.size(); j++) {
						hm=checkHashMap(j, hm);
					}
				}

			} catch (KMException e) {
				System.err.println(e.getMessage());
			}
		}
		addKnowInfo(st, time);

	}
	
	public void outProcess(TimeStamp time, ParameterizedMethod process){
		init();
		String st="out";
		for (int i = 0; i < process.out.size(); i++) {
			String ph=process.out.get(i).kPath.getEvaluatedPath(km, null, null, null);
			try {
				Object kph = km.getKnowledge(ph);
				arrPath.add(ph);
				arrValue.add(kph);
				boolean hm=true;
				while (hm) {
					hm=false;
					for (int j = 0; j < arrValue.size(); j++) {
						hm=checkHashMap(j, hm);
					}
				}

			} catch (KMException e) {
				System.err.println(e.getMessage());
			}
		}
		addKnowInfo(st, time);

	}
	
	public void inOutProcess(TimeStamp time, ParameterizedMethod process){
		init();
		String st="inOut";
		for (int i = 0; i < process.inOut.size(); i++) {
			String ph=process.inOut.get(i).kPath.getEvaluatedPath(km, null, null, null);
			try {
				Object kph = km.getKnowledge(ph);
				arrPath.add(ph);
				arrValue.add(kph);
				boolean hm=true;
				while (hm) {
					hm=false;
					for (int j = 0; j < arrValue.size(); j++) {
						hm=checkHashMap(j, hm);
					}
				}

			} catch (KMException e) {
				System.err.println(e.getMessage());
			}
		}
		addKnowInfo(st, time);

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

