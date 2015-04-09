package cz.cuni.mff.d3s.deeco.logging;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class SimpleFormatter extends Formatter {

	private static final DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss.SSS");
	 
    protected void preFormat(StringBuilder builder, LogRecord record) {
        builder.append("[")
 	   		   .append(record.getLevel())
 	   		   .append("] [")
 	   		   .append(dateFormat.format(new Date(record.getMillis())))
        	   .append("] ")
        	   .append(formatMessage(record));
    }
    
    protected void thrownFormat(StringBuilder builder, LogRecord record) {
    	if(hasException(record)){
    		ByteArrayOutputStream stream = new ByteArrayOutputStream();
    		PrintStream printStream = new PrintStream(stream);
    		record.getThrown().printStackTrace(printStream);
    		builder.append("\t").append(stream.toString());
    	}
    }
    
    protected boolean hasException(LogRecord record){
    	return record.getThrown() != null;
    }
    
    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder();
        preFormat(builder, record);
        builder.append("\n");
        thrownFormat(builder, record);
        builder.append("\n");
        return builder.toString();
    }
 
    public String getHead(Handler h) {
        return super.getHead(h);
    }
 
    public String getTail(Handler h) {
        return super.getTail(h);
    }
}
