package demo_annotation.Modes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import org.mvel2.MVEL;

public class trys {
	public static void main(String[] args) {
//		ComposedCondition cond = new ComposedCondition("V > 100 && (V < 40 || V >= #H)");
//		ComposedCondition cond2 = new ComposedCondition("(V > 100 && V < 40) || V >= #H");
		
		Collection<String> strs= new ArrayList<String>();
		strs.add("A");
		strs.add("A");
		System.out.println("size : "+strs.size());
		
		Map<String, Object> context = new java.util.HashMap<String, Object>();
		String name = "A";
		int g = 10;
		context.put("T", 1);
		context.put("F", 5);
		context.put("g", g);
		context.put(name, 0);
		
		System.out.println(MVEL.eval("g > 0 && ( F < 10 || ( F < 10 && T > 0 ) )", context));
		System.out.println(MVEL.eval("T > 0+A", context));
	}
}
