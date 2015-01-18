package cz.cuni.mff.d3s.deeco.integrity;

import java.io.Serializable;

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;

/**
 * 
 * @author Ondřej Štumpf
 *
 */
public class RatingsChangeSet implements Serializable {

	private static final long serialVersionUID = 2415324270333544655L;

	private String authorComponentId, targetComponentId;
	private KnowledgePath knowledgePath;
	private PathRating rating;
	
	public RatingsChangeSet(String authorComponentId, String targetComponentId, KnowledgePath knowledgePath, PathRating rating) {
		this.authorComponentId = authorComponentId;
		this.targetComponentId = targetComponentId;
		this.knowledgePath = knowledgePath;
		this.rating = rating;
	}
	
	public String getAuthorComponentId() {
		return authorComponentId;
	}
	
	public String getTargetComponentId() {
		return targetComponentId;
	}
	
	public KnowledgePath getKnowledgePath() {
		return knowledgePath;
	}
	
	public PathRating getPathRating() {
		return rating;
	}
}
