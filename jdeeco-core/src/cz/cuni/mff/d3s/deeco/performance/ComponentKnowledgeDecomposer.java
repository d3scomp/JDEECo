package cz.cuni.mff.d3s.deeco.performance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;


public class ComponentKnowledgeDecomposer  extends KnowledgeDecomposer {
	
	String cid;
	Object knowledge;
	
	
	public ComponentKnowledgeDecomposer(String cid, Object knowledge, KnowledgeManager km){
		this.cid=cid;
		this.knowledge=knowledge;
		this.km=km;
		
	}

	public void decompose(){
		init();
		arrPath.add(cid);
		arrValue.add(knowledge);
		boolean hm=true;
		while (hm) {
			hm=false;
			for (int j = 0; j < arrValue.size(); j++) {
				hm=checkHashMap(j, hm);
			}
		}
		addKnowInfo(cid);
	}
	
	public void filter(List<String> result){

	    boolean exist=false;
	    for (int i = km.arrayKnow.size()-1 ; i >= 0 ; i--) {
	    	exist=false;
	    	KnowledgeInfo currentKnow = km.arrayKnow.get(i);
			for (int j = result.size()-1; j >= 0; j--) {
				if(currentKnow.knowPath.startsWith(result.get(j)))
					exist=true;
		}
			
			if(!exist){
				km.arrayKnow.remove(currentKnow);
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

