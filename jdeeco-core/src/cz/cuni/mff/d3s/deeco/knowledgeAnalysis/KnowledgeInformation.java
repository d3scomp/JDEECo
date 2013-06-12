package cz.cuni.mff.d3s.deeco.knowledgeAnalysis;



import org.eclipse.emf.common.util.EList;

import knowledge.AbstractElement;
import knowledge.AbstractKnowledge;

public class KnowledgeInformation {
	
	protected AbstractElement writer;
	protected EList<AbstractElement> reader;
	protected int oldness;
	protected boolean visited;
	
	
	public KnowledgeInformation(AbstractElement writer, EList<AbstractElement> reader,int oldness,boolean visited) {
		// TODO Auto-generated constructor stub
		setWriter(writer);
		setReader(reader);
		setOldness(oldness);
		setVisited(visited);
	}
	
	public void setOldness(int oldness) {
		this.oldness = oldness;
	}
	
	public int getOldness() {
		return oldness;
	}
	
	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	
	public boolean isVisited() {
		return visited;
	}
	public EList<AbstractElement> getReader() {
		return reader;
	}
	
	public void setReader(EList<AbstractElement> reader) {
		this.reader = reader;
	}
	
	public void setWriter(AbstractElement writer) {
		this.writer = writer;
	}
	
	public AbstractElement getWriter() {
		return writer;
	}
	
}
