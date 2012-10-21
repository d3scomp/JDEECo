package cz.cuni.mff.d3s.deeco.sde.utils;

public class StringUtils {
	public static String padRight(String s, int n) {
		return s + repeat(" ", n);
	}

	public static String padLeft(String s, int n) {
		return repeat(" ", n) + s; 
	}

	public static String repeat(String string, int times) {
		return new String((new char[times])).replace("\0", string);
	}
}
