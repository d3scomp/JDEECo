package cz.cuni.mff.d3s.deeco.performance;

import java.util.HashMap;
import java.util.List;

import cz.cuni.mff.d3s.deeco.exceptions.KMException;
import cz.cuni.mff.d3s.deeco.invokable.ParameterizedMethod;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcess;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;

public class EnsembleKnowledgeDecomposer extends ProcessKnowledgeDecomposer {
	
	public EnsembleKnowledgeDecomposer(KnowledgeManager km, SchedulableEnsembleProcess sep,IPerformanceInfo pInfo) {
		// TODO Auto-generated constructor stub
		this.km=km;
		this.sp=sep;
		this.pInfo=pInfo;
		this.inOld=new HashMap<KnowledgeInfo, List<TimeStamp>>();
	}
	
	public void inEnsemble( TimeStamp time, ParameterizedMethod knowledgeExchange, String coord, String mem){
		init();
		String st="in";
		for (int i = 0; i < knowledgeExchange.in.size(); i++) {
			String ph=knowledgeExchange.in.get(i).kPath.getEvaluatedPath(km, coord, mem, null);
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
	

	
	public void outEnsemble( TimeStamp time, ParameterizedMethod knowledgeExchange, String coord, String mem){
		init();
		String st="out";
		for (int i = 0; i < knowledgeExchange.out.size(); i++) {
			String ph=knowledgeExchange.out.get(i).kPath.getEvaluatedPath(km, coord, mem, null);
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
	

	
	public void inOutEnsemble( TimeStamp time, ParameterizedMethod knowledgeExchange, String coord, String mem){
		init();
		String st="inOut";
		for (int i = 0; i < knowledgeExchange.inOut.size(); i++) {
			String ph=knowledgeExchange.inOut.get(i).kPath.getEvaluatedPath(km, coord, mem, null);
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
