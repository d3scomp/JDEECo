package cz.cuni.mff.d3s.deeco.performance;

import java.util.Calendar;

public class TimeStamp implements Comparable<TimeStamp>{
// time in nanoseconds
	public long release=0;
	public long start=0;
	public long finish=0;
	@Override
	public int compareTo(TimeStamp arg0) {
		// TODO Auto-generated method stub
		long result=finish - arg0.finish;
		return (result == 0 ? 0 : (result > 0 ? 1 : -1));
	}
}
