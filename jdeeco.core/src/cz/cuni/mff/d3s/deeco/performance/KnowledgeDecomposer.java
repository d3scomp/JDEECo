package cz.cuni.mff.d3s.deeco.performance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableComponentProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;

public class KnowledgeDecomposer {

	protected boolean checkHashMap(ArrayList arrPath, ArrayList arrValue, int j, boolean hm){
		if (arrValue.get(j) instanceof HashMap) {
			hm=true;
			Map<String, Object> hmValue=(Map<String, Object>) arrValue.get(j);
			String partPath=arrPath.get(j).toString();
			Iterator ks = hmValue.keySet().iterator();
			arrPath.remove(j);
			arrValue.remove(j);
			for (String key : hmValue.keySet()) {
				decomposeValuePath(hmValue, key, arrPath, arrValue, ks, partPath);
			}
		}else{
			String partPath=arrPath.get(j).toString();
			decomposeValuePath(arrValue.get(j), arrPath, arrValue, partPath);
		}
		return hm;
	}
	
	protected void  decomposeValuePath(Map<String, Object> hmValue,String key,  ArrayList arrPath, ArrayList arrValue,  Iterator ks, String partPath){
		Object nextKS = ks.next();
		if (hmValue.get(key) instanceof Object[] ) {
			Object [] array = (Object []) hmValue.get(key);
			if(array.length > 1){
				arrPath.add(partPath);
				arrValue.add(hmValue.get(key));
			}else{
				String newS=partPath.concat(".")+nextKS.toString();
				arrPath.add(newS);
				arrValue.add(array[0]);
			}
		}
	}


	protected void  decomposeValuePath(Object hmValue,  ArrayList arrPath, ArrayList arrValue,  String partPath){
//		Object nextKS = ks.next();
		if (hmValue instanceof Object[] ) {
			Object [] array = (Object []) hmValue;
			if(array.length > 1){
				arrPath.add(partPath);
				arrValue.add(hmValue);
			}else{
//				String newS=partPath.concat(".")+nextKS.toString();
				arrPath.add(partPath);
				arrValue.add(array[0]);
			}
		}
	}
	
	
	protected void addKnowInfo(ArrayList arrPath, ArrayList arrValue,SchedulableProcess scp, IPerformanceInfo pInfo,String st, TimeStamp time){
		boolean exist=false;
		for (int j = 0; j < arrPath.size(); j++) {
			for (int k = 0; k < scp.kInfoArr.size(); k++) {
				KnowledgeInfo arrElem = scp.kInfoArr.get(k);
				if(arrElem.knowPath == arrPath.get(j)){
					arrElem.value=arrValue.get(j);
					chooseAdd(scp, pInfo, arrElem, st,time);
					exist=true;
				}
			}
			if(!exist){
				KnowledgeInfo ki=new KnowledgeInfo();
				ki.knowPath=arrPath.get(j);
				ki.value=arrValue.get(j);
				chooseAdd(scp, pInfo, ki, st, time);
				scp.kInfoArr.add(ki);
			}
		}
	}
	
	
		
	protected void chooseAdd(SchedulableProcess scp, IPerformanceInfo pInfo,KnowledgeInfo arrElem,String st, TimeStamp time){
		//get ride of instance of
		if(scp instanceof SchedulableComponentProcess){
			SchedulableComponentProcess s = (SchedulableComponentProcess) scp;
			if(st.equals("in"))
				addProcessReadKnowledge(s, pInfo, arrElem, time);
			else if(st.equals("out"))
					addProcessWriteKnowledge(s, pInfo, arrElem, time);
			else if(st.equals("inOut")){
					addProcessReadKnowledge(s, pInfo, arrElem, time);
					addProcessWriteKnowledge(s, pInfo, arrElem, time);
			}
		}else if ( scp instanceof SchedulableEnsembleProcess){
					SchedulableEnsembleProcess e = (SchedulableEnsembleProcess) scp;
						if(st.equals("in"))
							addEnsembleReadKnowledge(e, pInfo, arrElem, time);
						else if(st.equals("out"))
								addEnsembleWriteKnowledge(e, pInfo, arrElem, time);
						else if(st.equals("inOut")){
								addEnsembleReadKnowledge(e, pInfo, arrElem, time);
								addEnsembleWriteKnowledge(e, pInfo, arrElem, time);
						}
			}
		}

	
	
	protected void addProcessReadKnowledge(SchedulableComponentProcess scp, IPerformanceInfo pInfo,KnowledgeInfo arrElem, TimeStamp time){
		arrElem.registerPerformanceInfo.processReader.put(scp, pInfo);
		arrElem.registerTimeStamp.processHistoryReader.put(scp, time);
	}
	protected void addEnsembleReadKnowledge(SchedulableEnsembleProcess sep, IPerformanceInfo pInfo,KnowledgeInfo arrElem, TimeStamp time){
		arrElem.registerPerformanceInfo.ensembleReader.put(sep, pInfo);
		arrElem.registerTimeStamp.ensembleHistoryReader.put(sep, time);
	}
	protected void addProcessWriteKnowledge(SchedulableComponentProcess scp, IPerformanceInfo pInfo,KnowledgeInfo arrElem, TimeStamp time){
		arrElem.registerPerformanceInfo.processWriter.put(scp, pInfo);
		arrElem.registerTimeStamp.processHistoryWriter.put(scp, time);
	}
	protected void addEnsembleWriteKnowledge(SchedulableEnsembleProcess sep, IPerformanceInfo pInfo,KnowledgeInfo arrElem, TimeStamp time){
		arrElem.registerPerformanceInfo.ensembleWriter.put(sep, pInfo);
		arrElem.registerTimeStamp.ensembleHistoryWriter.put(sep, time);
	}

	
}
