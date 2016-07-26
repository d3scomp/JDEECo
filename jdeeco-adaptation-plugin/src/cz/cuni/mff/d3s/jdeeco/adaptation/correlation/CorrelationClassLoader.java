package cz.cuni.mff.d3s.jdeeco.adaptation.correlation;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

public class CorrelationClassLoader extends ClassLoader {
	
	public CorrelationClassLoader(ClassLoader parent) {
        super(parent);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public Class loadClass(String name) throws ClassNotFoundException {
        
    	if(!name.startsWith("Correlation_"))
            return super.loadClass(name);
    	
        try {
            InputStream input = new FileInputStream(String.format("%s/%s.class",
            		CorrelationEnsembleFactory.CLASS_DIRECTORY, name));
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int data = input.read();

            while(data != -1){
                buffer.write(data);
                data = input.read();
            }

            input.close();

            byte[] classData = buffer.toByteArray();

            return defineClass(name, classData, 0, classData.length);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
