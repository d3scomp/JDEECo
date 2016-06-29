package cz.cuni.mff.d3s.jdeeco.edl;

public class PrimitiveTypes {
	public static final String INT = "int";
	public static final String STRING = "string";
	public static final String BOOL = "bool";
	public static final String FLOAT = "double";
	public static final String UNKNOWN = "unknown";
	
	public static boolean isPrimitiveType(String typeName) {
		return typeName.equals(INT) 
				|| typeName.equals(STRING)
				|| typeName.equals(BOOL)
				|| typeName.equals(FLOAT);
	}
}
