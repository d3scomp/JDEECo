package cz.cuni.mff.d3s.deeco.knowledge;

import java.lang.reflect.Field;

import cz.cuni.mff.d3s.deeco.exceptions.KMException;
import cz.cuni.mff.d3s.deeco.exceptions.KMNotExistentException;
import cz.cuni.mff.d3s.deeco.exceptions.KnowledgeRepositoryException;
import cz.cuni.mff.d3s.deeco.exceptions.UnavailableEntryException;
import cz.cuni.mff.d3s.deeco.scheduling.IKnowledgeChangeListener;

public class RepositoryKnowledgeManagerHelper {

	private KnowledgeRepository kr;
	private KnowledgeManager km;

	public RepositoryKnowledgeManagerHelper(KnowledgeRepository kr,
			KnowledgeManager km) {
		this.kr = kr;
		this.km = km;
	}

	public ISession createSession() {
		return kr.createSession();
	}

	public boolean putFlat(String knowledgePath, Object newValue,
			ISession session) throws KnowledgeRepositoryException {
		Object currentValue = null;
		putStructure(knowledgePath, null, session);
		try {
			currentValue = kr.get(knowledgePath, session);
			if ((newValue != null && !newValue.equals(currentValue))
					|| (currentValue != null && !currentValue.equals(newValue))) {
				kr.take(knowledgePath, session);
				kr.put(knowledgePath, newValue, session);
				return true;
			}
			return false;
		} catch (UnavailableEntryException uee) {
			System.out.println("Unavailable entry: " + knowledgePath);
			kr.put(knowledgePath, newValue, session);
			return true;
		}
	}

	public Object getFlat(boolean withdrawal, String knowledgePath,
			ISession session) throws UnavailableEntryException,
			KnowledgeRepositoryException {
		return (withdrawal) ? kr.take(knowledgePath, session) : kr.get(
				knowledgePath, session);
	}

	public Class<?> getStructure(boolean withdrawal, String knowledgePath,
			ISession session) throws KnowledgeRepositoryException {
		String tempPath = KPBuilder.appendToRoot(knowledgePath,
				ConstantKeys.CLASS_ID);
		Object result = null;
		try {
			result = getFlat(withdrawal, tempPath, session);
		} catch (UnavailableEntryException uee) {
		}
		return (result != null) ? (Class<?>) result : null;

	}

	public void putStructure(String knowledgePath, Class<?> newClass,
			ISession session) throws KnowledgeRepositoryException {
		Object oldClass;
		String structurePath = KPBuilder.appendToRoot(knowledgePath,
				ConstantKeys.CLASS_ID);
		try {
			oldClass = kr.get(structurePath, session);
			if ((newClass != null && !newClass.equals(oldClass))
					|| (oldClass != null && !oldClass.equals(newClass))) {
				kr.take(structurePath, session);

				String tempPath;
				for (Field f : ((Class<?>) oldClass).getFields()) {
					tempPath = KPBuilder.appendToRoot(knowledgePath,
							f.getName());
					try {
						km.takeKnowledge(tempPath, session);
					} catch (KMNotExistentException kmnee) {
					} catch (KMException kmae) {
						throw new KnowledgeRepositoryException(
								"Knowledge repository error!");
					}
				}

				if (newClass != null)
					kr.put(structurePath, newClass, session);
			}
		} catch (UnavailableEntryException uee) {
			if (newClass != null)
				kr.put(structurePath, newClass, session);
		}
	}

	public boolean listenForChange(IKnowledgeChangeListener listener) {
		return kr.listenForChange(listener);
	}
}
