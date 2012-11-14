package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.Arrays;
import java.util.List;

import cz.cuni.mff.d3s.deeco.exceptions.KMException;
import cz.cuni.mff.d3s.deeco.exceptions.KMNotExistentException;
import cz.cuni.mff.d3s.deeco.exceptions.KRExceptionAccessError;
import cz.cuni.mff.d3s.deeco.exceptions.KRExceptionUnavailableEntry;
import cz.cuni.mff.d3s.deeco.scheduling.IKnowledgeChangeListener;

public class RepositoryKnowledgeManagerHelper {

	private final KnowledgeRepository kr;
	private final KnowledgeManager km;

	public RepositoryKnowledgeManagerHelper(KnowledgeRepository kr,
			KnowledgeManager km) {
		this.kr = kr;
		this.km = km;
	}

	public ISession createSession() {
		return kr.createSession();
	}

	public void putFlat(String knowledgePath, Object newValue,
			ISession session, boolean modify) throws KRExceptionAccessError {
		Object currentValue = null;
		putStructure(knowledgePath, null, session, modify);
		if (modify) {
			try {
				currentValue = kr.get(knowledgePath, session);
				if ((newValue != null || currentValue != null)
						&& !Arrays.deepEquals((Object[]) currentValue,
								new Object[] { newValue })) {
					kr.take(knowledgePath, session);
					kr.put(knowledgePath, newValue, session);
				}
			} catch (KRExceptionUnavailableEntry uee) {
				kr.put(knowledgePath, newValue, session);
			}
		} else {
			kr.put(knowledgePath, newValue, session);
		}
	}

	public Object getFlat(boolean withdrawal, String knowledgePath,
			ISession session) throws KRExceptionUnavailableEntry,
			KRExceptionAccessError {
		return (withdrawal) ? kr.take(knowledgePath, session) : kr.get(
				knowledgePath, session);
	}

	public Object[] getStructure(boolean withdrawal, String knowledgePath,
			ISession session) throws KRExceptionAccessError {
		String tempPath = KPBuilder.appendToRoot(knowledgePath,
				ConstantKeys.STRUCTURE_ID);
		try {
			Object[] result = (Object[]) getFlat(withdrawal, tempPath, session);
			if (result.length == 1)
				return (Object[]) result[0];
			else
				return null;
		} catch (KRExceptionUnavailableEntry uee) {
			return null;
		}
	}

	public Object[] putStructure(String knowledgePath, Object value,
			ISession session, boolean modify) throws KRExceptionAccessError {
		Object[] oldStructure, newStructure = StructureHelper
				.getStructureFromObject(value);
		boolean store = value != null && newStructure != null;
		String structurePath = KPBuilder.appendToRoot(knowledgePath,
				ConstantKeys.STRUCTURE_ID);
		try {
			if (modify) {
				Object[] tObjects = (Object[]) kr.get(structurePath, session);
				oldStructure = (Object[]) tObjects[0];
				if ((newStructure != null || oldStructure != null)
						&& !Arrays.deepEquals(oldStructure, newStructure)) {
					kr.take(structurePath, session);
					String tempPath;
					List<?> nsList = Arrays.asList(newStructure);
					for (Object s : oldStructure) {
						if (nsList.contains(s))
							continue;
						tempPath = KPBuilder.appendToRoot(knowledgePath,
								(String) s);
						try {
							km.takeKnowledge(tempPath, session);
						} catch (KMNotExistentException kmnee) {
						} catch (KMException kmae) {
							throw new KRExceptionAccessError(
									"Knowledge repository error!");
						}
					}
					if (store)
						kr.put(structurePath, newStructure, session);
				}
			} else if (store)
				kr.put(structurePath, newStructure, session);
		} catch (KRExceptionUnavailableEntry uee) {
			if (store)
				kr.put(structurePath, newStructure, session);
		}
		return newStructure;
	}

	public boolean registerListener(IKnowledgeChangeListener listener) {
		return kr.registerListener(listener);
	}
	
	public boolean unregisterListener(IKnowledgeChangeListener listener) {
		return kr.unregisterListener(listener);
	}
	
	public void setListenersActive(boolean on) {
		kr.setListenersActive(on);	
	}
}
