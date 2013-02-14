package cz.cuni.mff.d3s.deeco.sde.utils;

public class StringUtils {
	public static final String SPACE = " ";
	public static final String FSLASH = "/";
	public static final String DBSLASH = "\\";
	
	
	
	
	public static String padRight(String s, int n) {
		return s + repeat(SPACE, n);
	}

	public static String padLeft(String s, int n) {
		return repeat(SPACE, n) + s; 
	}

	public static String repeat(String string, int times) {
		return new String((new char[times])).replace("\0", string);
	}
}
