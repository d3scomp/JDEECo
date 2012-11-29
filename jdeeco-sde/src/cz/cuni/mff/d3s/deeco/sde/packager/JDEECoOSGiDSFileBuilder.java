package cz.cuni.mff.d3s.deeco.sde.packager;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class JDEECoOSGiDSFileBuilder {
	
	public static File buildDeclarativeServiceXML(File where,
			List<String> classNames, String bundleSymbolicName) {
		if (classNames == null || classNames.size() == 0) {
			return null;
		}
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.newDocument();
			Element component = document.createElement("component");
			component.setAttribute("name", bundleSymbolicName);
			Element implementation = document.createElement("implementation");
			implementation
					.setAttribute("class",
							"cz.cuni.mff.d3s.deeco.sde.provider.OSGiBundleDEECoObjectProvider");
			component.appendChild(implementation);
			Element service = document.createElement("service");
			Element provide = document.createElement("provide");
			provide.setAttribute("interface",
					"cz.cuni.mff.d3s.deeco.provider.AbstractDEECoObjectProvider");
			service.appendChild(provide);
			component.appendChild(service);
			Element property = document.createElement("property");
			String listOfClasses = "";
			for (String className : classNames) {
				listOfClasses += className + ",";
			}
			if (!listOfClasses.equals(""))
				listOfClasses = listOfClasses.substring(0, listOfClasses.length()-1);
			property.setAttribute("name", "components");
			property.setAttribute("value", listOfClasses);
			component.appendChild(property);
			document.appendChild(component);

			File f = new File(where.getAbsolutePath() + "\\OSGI-INF");
			FileOutputStream fos = null;
			try {
				f.mkdirs();
				f = new File(f.getAbsolutePath() + "\\ds.xml");
				f.createNewFile();
				fos = new FileOutputStream(f);
				Source source = new DOMSource(document);
				Result result = new StreamResult(fos);
				Transformer transformer = TransformerFactory.newInstance()
						.newTransformer();
				transformer.transform(source, result);
			} finally {
				if (fos != null)
					fos.close();
			}
			return f.getParentFile();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}	
}
