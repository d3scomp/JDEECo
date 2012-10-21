package cz.cuni.mff.d3s.deeco.sde.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableComponentProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcess;
import cz.cuni.mff.d3s.deeco.knowledge.ComponentKnowledge;
import cz.cuni.mff.d3s.deeco.runtime.IEnsembleComponentInformer;

public class Printer {

	public static String printComponentProcess(SchedulableComponentProcess scp) {
		return printComponentProcess(scp, 0);
	}

	public static String printComponentProcess(SchedulableComponentProcess scp,
			int indentSize) {
		if (scp == null)
			return null;
		return StringUtils.padLeft(scp.getProcessMethod().getDeclaringClass()
				+ ": " + scp.getProcessMethod().getName() + "\n", indentSize);
	}

	public static String printEnsemble(SchedulableEnsembleProcess sep) {
		return printEnsemble(sep, 0);
	}

	public static String printEnsemble(SchedulableEnsembleProcess sep,
			int indentSize) {
		if (sep == null)
			return null;
		return StringUtils.padLeft(sep.getMembershipMethod()
				.getDeclaringClass().toString()
				+ "\n", indentSize);
	}

	public static String printComponentKnowledge(Object ck) {
		return printComponentKnowledge(ck, 0);
	}

	public static String printComponentKnowledge(Object ck, int indentSize) {
		String result = "Knowledge: " + retrieveComponentId(ck) + "\n";
		result += printKnowledge(ck, indentSize + 1);
		return result;
	}

	public static String printComponentInfo(
			List<SchedulableComponentProcess> processes, Object ck) {
		String cId = retrieveComponentId(ck);
		String result = "Component: ";
		if (cId == null)
			result += "Not found.\n";
		else {
			result += printComponentKnowledge(ck, 1) + "\n";
			result += StringUtils.padLeft("Processes:\n", 1);
			for (SchedulableComponentProcess scp : processes)
				result += printComponentProcess(scp, 2);
		}
		return result;
	}

	public static String printComponentsInfo(IEnsembleComponentInformer infoProvider) {
		return printComponentsInfo(infoProvider,
				infoProvider.getComponentsIds());
	}

	public static String printComponentsInfo(IEnsembleComponentInformer infoProvider,
			List<String> componentsIds) {
		String result = "";
		if (componentsIds != null && !componentsIds.isEmpty()) {
			for (String cId : componentsIds) {
				result += printComponentInfo(
						infoProvider.getComponentProcesses(cId),
						infoProvider.getComponentKnowledge(cId));
				result += "\n" + StringUtils.repeat("-", 40) + "\n";
			}
			return result;
		}
		return "Components not found.";
	}

	public static String printKnowledgesInfo(IEnsembleComponentInformer infoProvider) {
		return printKnowledgesInfo(infoProvider,
				infoProvider.getComponentsIds());
	}

	public static String printKnowledgesInfo(IEnsembleComponentInformer infoProvider,
			List<String> componentsIds) {
		String result = "";
		if (componentsIds != null && !componentsIds.isEmpty()) {
			for (String cId : componentsIds) {
				result += printComponentKnowledge(infoProvider
						.getComponentKnowledge(cId));
				result += "\n" + StringUtils.repeat("-", 40) + "\n";
			}
			return result;
		}
		return "Components not found.";
	}

	public static String printEnsembles(
			List<SchedulableEnsembleProcess> ensembleProcesses) {
		String result = "";
		if (ensembleProcesses != null && !ensembleProcesses.isEmpty()) {
			for (SchedulableEnsembleProcess sep : ensembleProcesses) {
				result += printEnsemble(sep) + "\n";
			}
			return result;
		}
		return "Ensembles not found.";
	}

	private static String printKnowledge(Object ck, int indentSize) {
		Object value;
		String result = "";
		if (ck instanceof Map) {
			Map<String, ?> mCK = (Map<String, ?>) ck;
			Iterator<String> keyIterator = mCK.keySet().iterator();
			String key;
			while (keyIterator.hasNext()) {
				key = keyIterator.next();
				value = mCK.get(key);
				result += processSingleNode(key, value, indentSize) + "\n";
			}
		} else if (ck instanceof ComponentKnowledge) {
			for (Field f : ck.getClass().getFields()) {
				try {
					value = f.get(ck);
					result += processSingleNode(f.getName(), value, indentSize)
							+ "\n";
				} catch (IllegalAccessException e) {
					continue;
				}
			}
		} else
			return "Wrong knowledge structure.";
		return result;
	}

	private static String processSingleNode(String name, Object value,
			int indentSize) {
		String result = StringUtils.padLeft(name, indentSize) + ": ";
		if (value == null) {
			result += "null\n";
		} else if (value instanceof Map) {
			result += "\n" + printKnowledge(value, indentSize + 2);
		} else {
			result += "(" + value.getClass() + ") ";
			if (value instanceof Object[])
				result += Arrays.toString((Object[]) value);
			else
				result += value.toString();
		}
		return result;
	}

	private static String retrieveComponentId(Object ck) {
		if (ck != null) {
			if (ck instanceof Map) {
				Map<String, ?> mCK = (Map<String, ?>) ck;
				if (mCK.containsKey("id")) {
					Object[] aId = (Object[]) mCK.get("id");
					if (aId.length == 1)
						return (String) aId[0];
				}
			} else if (ck instanceof ComponentKnowledge) {
				return ((ComponentKnowledge) ck).id;
			}
		}
		return null;
	}

}
