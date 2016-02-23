package cz.cuni.mff.d3s.jdeeco.edl;

import org.eclipse.xtext.common.services.DefaultTerminalConverters;
import org.eclipse.xtext.conversion.IValueConverter;
import org.eclipse.xtext.conversion.ValueConverter;
import org.eclipse.xtext.conversion.ValueConverterException;
import org.eclipse.xtext.nodemodel.INode;

public class EDLValueConverter extends DefaultTerminalConverters {
	
	@ValueConverter(rule ="Cardinality")
	public IValueConverter<Integer> Cardinality() {
		return new IValueConverter<Integer>() {			
			@Override
			public Integer toValue(String string, INode node) {				
	                if (string.isEmpty())
	                    throw new ValueConverterException("Couldn't convert empty string to int", node, null);
	                else if ("*".equals(string.trim()))
	                    return -1;
	                try {
	                    return Integer.parseInt(string);
	                } catch (NumberFormatException e) {
	                    throw new ValueConverterException("Couldn't convert '"+string+"' to int", node, e);
	                }
				
			}

			@Override
			public String toString(Integer value) {
			 	return ((value == -1) ? "*" : Integer.toString(value));
			}
		};
	}
}
