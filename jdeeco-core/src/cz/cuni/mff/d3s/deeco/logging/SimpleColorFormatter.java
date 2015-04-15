package cz.cuni.mff.d3s.deeco.logging;

import java.util.logging.LogRecord;

public class SimpleColorFormatter extends SimpleFormatter {

	private enum LogColor{
		
		/**
		 * ANSI_RESET
		 */
		DEFAULT("\u001B[0m"),
		/**
		 * ANSI_BLACK
		 */
		BLACK("\u001B[30m"),
		/**
		 * ANSI_RED
		 */
		RED("\u001B[91m"),
		/**
		 * ANSI_GREEN
		 */
		GREEN("\u001B[32m"),
		/**
		 * ANSI_YELLOW
		 */
		YELLOW("\u001B[33m"),
		/**
		 * ANSI_BLUE
		 */
		BLUE("\u001B[34m"),
		/**
		 * ANSI_PURPLE
		 */
		PURPLE("\u001B[35m"),
		/**
		 * ANSI_CYAN
		 */
		CYAN("\u001B[36m"),
		/**
		 * ANSI_WHITE
		 */
		WHITE("\u001B[37m");
		
		public final String mark;
		
		private LogColor(String mark){
			this.mark = mark;
		}
	}

	public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder();
        
        if(record.getLevel().equals(CustomLevel.DEBUG)){
        	builder.append(LogColor.CYAN.mark);
        } else if(record.getLevel().equals(CustomLevel.INFO)){
        	builder.append(LogColor.GREEN.mark);
        } else if(record.getLevel().equals(CustomLevel.WARNING)){
        	builder.append(LogColor.YELLOW.mark);
        } else if(record.getLevel().equals(CustomLevel.ERROR)){
        	builder.append(LogColor.RED.mark);
        }
        
        preFormat(builder, record);

        if(hasException(record)){
    		builder.append("\n")
        		   .append(LogColor.RED.mark);
        	thrownFormat(builder, record);
        }
        
        builder.append(LogColor.DEFAULT.mark).append("\n");
        
        
        return builder.toString();
    }

}
