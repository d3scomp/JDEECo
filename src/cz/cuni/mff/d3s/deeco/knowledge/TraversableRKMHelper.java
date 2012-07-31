package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.exceptions.KMException;
import cz.cuni.mff.d3s.deeco.exceptions.KnowledgeRepositoryException;
import cz.cuni.mff.d3s.deeco.exceptions.UnavailableEntryException;

public class TraversableRKMHelper {

	private RepositoryKnowledgeManagerHelper rkmh;
	private RepositoryKnowledgeManager rkm;
	
	public TraversableRKMHelper(RepositoryKnowledgeManagerHelper rkmh, RepositoryKnowledgeManager rkm) {
		this.rkmh = rkmh;
		this.rkm = rkm;
	}

	public void putTraversable(String knowledgePath, Object value,
			Class<?> structure, ISession session)
			throws KnowledgeRepositoryException, KMException {
		Map<String, Object> traversable = null;
		Set<String> keys = null;
		if (value != null) {
			rkmh.putStructure(knowledgePath, structure, session);
			traversable = KMHelper.translateToMap(value);
			keys = traversable.keySet();
		} else {
			rkmh.putStructure(knowledgePath, null, session);
		}
		List<String> toRemove = getRedundantKeys(knowledgePath, keys, session);
		rkmh.putFlat(KPBuilder.appendToRoot(knowledgePath,
				ConstantKeys.TRAVERSABLE_KEYS_ID),
				(keys != null) ? keys.toArray(new String[keys.size()]) : null,
				session);
		if (value != null) {
			Set<Map.Entry<String, Object>> entries = traversable.entrySet();
			for (Map.Entry<String, Object> entry : entries) {
				rkm.putKnowledge(
						KPBuilder.appendToRoot(knowledgePath, entry.getKey()),
						entry.getValue(), session);
			}
		}
		removeRedundantTraversable(knowledgePath, toRemove, session);
	}

	public Object getTraversable(boolean withdrawal, String knowledgePath,
			ISession session) throws UnavailableEntryException,
			KnowledgeRepositoryException, InstantiationException,
			IllegalAccessException, KMException {
		Object tResult = null;
		Class<?> resultClass = rkmh.getStructure(withdrawal, knowledgePath,
				session);
		if (resultClass != null) {
			String[] currentKeys = null;
			Object currentElement;
			tResult = KMHelper.getInstance(resultClass);
			currentKeys = (String[]) rkmh.getFlat(withdrawal, KPBuilder
					.appendToRoot(knowledgePath,
							ConstantKeys.TRAVERSABLE_KEYS_ID), session);
			if (tResult instanceof Collection)
				Arrays.sort(currentKeys);
			for (String k : currentKeys) {
				currentElement = (withdrawal) ? rkm.takeKnowledge(
						KPBuilder.appendToRoot(knowledgePath, k), session)
						: rkm.getKnowledge(
								KPBuilder.appendToRoot(knowledgePath, k),
								session);
				KMHelper.addElementToTraversable(currentElement, tResult, k);
			}
		}
		return tResult;
	}

	private List<String> getRedundantKeys(String knowledgePath,
			Set<String> keys, ISession session)
			throws KnowledgeRepositoryException {
		String[] currentKeys = null;
		List<String> listOfCurrentKeys = null;
		try {
			currentKeys = (String[]) rkmh.getFlat(false, KPBuilder
					.appendToRoot(knowledgePath,
							ConstantKeys.TRAVERSABLE_KEYS_ID), session);
		} catch (UnavailableEntryException uee) {
		}
		if (currentKeys != null) {
			listOfCurrentKeys = new ArrayList<String>(
					Arrays.asList(currentKeys));
			if (listOfCurrentKeys != null) {
				if (keys != null)
					listOfCurrentKeys.removeAll(keys);
			}
		}
		return listOfCurrentKeys;
	}

	private void removeRedundantTraversable(String knowledgePath,
			List<String> keys, ISession session)
			throws KnowledgeRepositoryException {
		if (keys != null)
			for (String s : keys)
				try {
					rkmh.getFlat(true,
							KPBuilder.appendToRoot(knowledgePath, s), session);
				} catch (UnavailableEntryException e) {
					e.printStackTrace();
				}
	}
}
