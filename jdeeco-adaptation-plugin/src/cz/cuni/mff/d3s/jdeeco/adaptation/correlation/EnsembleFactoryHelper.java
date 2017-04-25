/*******************************************************************************
 * Copyright 2017 Charles University in Prague
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *******************************************************************************/
package cz.cuni.mff.d3s.jdeeco.adaptation.correlation;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class EnsembleFactoryHelper {

	private boolean verbose;
	
	/**
	 * Once a new class is created it is stored here ready for further retrieval.
	 */
	private static Map<String, Class<?>> bufferedClasses = new HashMap<>();
	
	private CorrelationEnsembleFactory ensembleFactory;
	
	public EnsembleFactoryHelper(){
	}
	
	public EnsembleFactoryHelper withVerbosity(boolean verbosity){
		verbose = verbosity;
		return this;
	}
	
	public void setEnsembleFactory(CorrelationEnsembleFactory factory){
		if(factory == null){
			throw new NullPointerException(String.format("The %s argument is null.", "factory"));
		}
		this.ensembleFactory = factory;
	}
	
	/**
	 * Provides the ensemble definition for the knowledge fields of given name.
	 * The correlation of knowledge denoted by correlationSubject depends on the
	 * data filtering by the knowledge denoted by correlationFilter.
	 * @param correlationFilter Name of the knowledge field used for data filtering when calculating correlation.
	 * 		In our example this refers to "position".
	 * @param correlationSubject Name of the knowledge field used for the calculation of data correlation after
	 * 		the values has been filtered. In our example this refers to "temperature".
	 * @return A class that defines an ensemble for data exchange given by the specified knowledge fields.
	 * @throws Exception If there is a problem creating the ensemble class.
	 */
	public Class<?> getEnsembleDefinition(String correlationFilter, String correlationSubject) throws Exception {
		String className = composeClassName(correlationFilter, correlationSubject);
		Class<?> requestedClass;
		if(!bufferedClasses.containsKey(className)){
			requestedClass = ensembleFactory.createEnsembleDefinition(correlationFilter, correlationSubject);
			
			bufferedClasses.put(className, requestedClass);

			if(verbose){
				System.out.println(String.format("The %s ensemble created.", className));
				System.out.println(String.format("The %s ensemble buffered.", className));
			}
		}
		else {
			requestedClass = bufferedClasses.get(className);
			
			if(verbose){
				System.out.println(String.format("The %s ensemble fetched from buffer.", className));
			}
		}

		return requestedClass;
	}

	/**
	 * Compose the name of the ensemble class for the defined knowledge fields.
	 * @param correlationFilter Name of the knowledge field used for data filtering when calculating correlation.
	 * 		In our example this refers to "position".
	 * @param correlationSubject Name of the knowledge field used for the calculation of data correlation after
	 * 		the values has been filtered. In our example this refers to "temperature".
	 * @return The name of the ensemble class for the defined knowledge fields.
	 */
	public String composeClassName(String correlationFilter, String correlationSubject){
		return String.format("Correlation_%s2%s", correlationFilter, correlationSubject);
	}

	@SuppressWarnings("rawtypes")
	public void bufferEnsembleDefinition(String correlationFilter, String correlationSubject, Class ensembleDefinition){
		String ensembleName = composeClassName(correlationFilter, correlationSubject);
		// Buffer the modified class
		if(bufferedClasses.containsKey(ensembleName)){
			bufferedClasses.remove(ensembleName);
		}
		bufferedClasses.put(ensembleName, ensembleDefinition);
		
		if(verbose){
			System.out.println(String.format("The %s ensemble buffered.", ensembleName));
		}
	}
}
