package cz.cuni.mff.d3s.deeco.performance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableComponentProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgePathHelper;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;

public class KnowledgeDecomposer {

	protected ArrayList arrPath=new ArrayList();
	protected ArrayList arrValue=new ArrayList();
	protected KnowledgeManager km=null;
	
	
	public void init(){
		arrPath.clear();
		arrValue.clear();
	}

	
	public static ArrayList<KnowledgeInfo> getComponentKnowledgeArray(String cid, KnowledgeManager km){
		ArrayList<KnowledgeInfo> filteredArrayKnow=new ArrayList<KnowledgeInfo>();
		for (int i = 0; i < km.arrayKnow.size(); i++) {
			if(km.arrayKnow.get(i).getKnowPath().contains(cid))
				filteredArrayKnow.add(km.arrayKnow.get(i));
		}
		return filteredArrayKnow;
	}

	public static void addKnowledgeInfo(KnowledgeInfo kinfo, KnowledgeManager km){
		km.arrayKnow.add(kinfo);
	}
	
	
	protected boolean checkHashMap(int j, boolean hm){
		String partPath = arrPath.get(j).toString();
		if (arrValue.get(j) instanceof  Map ) {
			hm=true;
			HashMap<String, Object> hmValue=(HashMap<String, Object>) arrValue.get(j);
			Iterator ks = hmValue.keySet().iterator();
			Iterator<Object> kv = hmValue.values().iterator();
			arrPath.remove(j);
			arrValue.remove(j);
			while (ks.hasNext()) {
				Object kElem = ks.next();
				Object kVal = kv.next();
				decomposeValuePath(kElem, kVal, arrPath, arrValue, partPath);
			}
		}else{
			decomposeValueArray(arrValue.get(j), arrPath, arrValue, partPath);
		}
		return hm;
	}
	
	
	
	protected void addKnowInfo(String cid){
		boolean exist=false;
		for (int j = 0; j < arrPath.size(); j++) {
			for (KnowledgeInfo arrElem : km.arrayKnow) {
				if(arrElem.getKnowPath() == arrPath.get(j)){
					arrElem.setCurrentValue(arrValue.get(j));
					long creatTime = System.nanoTime();
					TimeStamp t=new TimeStamp();
					t.release=creatTime;
					t.start=creatTime;
					t.finish=creatTime;
					arrElem.setValueCreationTimeStamp(t);
					arrElem.setValueCurrentTimeStamp(t);
					exist=true;
				}
			}
			
			
			if(!exist){
				KnowledgeInfo ki=new KnowledgeInfo();
				ki.setKnowPath(arrPath.get(j).toString());
				ki.setCurrentValue(arrValue.get(j));
				long creatTime = System.nanoTime();
				TimeStamp t=new TimeStamp();
				t.release=creatTime;
				t.start=creatTime;
				t.finish=creatTime;
				ki.setValueCurrentTimeStamp(t);
				ki.setValueCreationTimeStamp(t);
				km.arrayKnow.add(ki);
			}
			exist=false;
		}

	}


	protected void  decomposeValuePath(Object kElem, Object kVal, ArrayList arrPath, ArrayList arrValue, String partPath){
		if(kVal instanceof Map){
			String newS=KnowledgePathHelper.appendToRoot(partPath,kElem.toString());
			arrPath.add(newS);
			arrValue.add(kVal);
		}else{
			if (kVal instanceof Object[] ) {
				Object [] array = (Object [])kVal;
				if(array.length > 1){
					arrPath.add(partPath);
					arrValue.add(kVal);
				}else{
					String newS=KnowledgePathHelper.appendToRoot(partPath,kElem.toString());
					arrPath.add(newS);
					arrValue.add(array[0]);
				}
			} 
		}

	}
	
	
	protected void  decomposeValueArray(Object hmValue,  ArrayList arrPath, ArrayList arrValue,  String partPath){
		if (hmValue instanceof Object[] ) {
			Object [] array = (Object []) hmValue;
			if(array.length > 1){
				arrPath.add(partPath);
				arrValue.add(hmValue);
			}else{
				arrPath.add(partPath);
				arrValue.add(array[0]);
			}
		}
	}
	
}
