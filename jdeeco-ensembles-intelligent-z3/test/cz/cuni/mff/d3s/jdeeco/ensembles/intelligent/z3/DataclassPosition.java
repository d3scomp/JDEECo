package cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3;

public class DataclassPosition extends Position {

	public DataclassPosition(int x, int y) {
		this.x = x;
		this.y = y;
		this.id = ""+x+y;
	}
	
	public Integer x;	
	public Integer y;
}
