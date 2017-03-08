/*******************************************************************************
 * Copyright 2015 Charles University in Prague
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
package cz.cuni.mff.d3s.deeco.annotations.processor;

import java.lang.annotation.Annotation;

import cz.cuni.mff.d3s.deeco.annotations.CorrelationData;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.metaadaptation.correlation.KnowledgeMetadataHolder;
import cz.cuni.mff.d3s.metaadaptation.correlation.metric.Metric;

/**
 * Processes the annotations related to the correlation plugin.
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 * @see AnnotationProcessor
 */
public class CorrelationAwareAnnotationProcessorExtension extends AnnotationProcessorExtensionPoint {

	@Override
	public void onComponentKnowledgeCreation(String fieldName, Annotation unknownAnnotation) {
		if (unknownAnnotation instanceof CorrelationData) {
			CorrelationData correlationDataAnnotation = (CorrelationData) unknownAnnotation; 
			double boundary = correlationDataAnnotation.boundary();
			double confidence = correlationDataAnnotation.confidence();
			Metric metric = null;
			try {
				metric = correlationDataAnnotation.metric().newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				Log.e("Could not instantiate metric class for field " + fieldName,e);
			}
			// metadata are only considered the first time for each field 
			if (!KnowledgeMetadataHolder.containsLabel(fieldName)) {
				KnowledgeMetadataHolder.setBoundAndMetric(fieldName, boundary, metric, confidence);
			}
		}
	}

	
}
