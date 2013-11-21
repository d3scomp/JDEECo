/**
 * Holds the classes responsible for processing of Java source files annotated with the internal DEECo DSL.
 * The {@link cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessor} parses the input file(s) and creates an EMF model that holds the runtime metadata instances with their references.
 * There are several cases where the input file(s) may be not admissible and it is found out at runtime only.
 * In such cases, an {@link cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException} with a corresponding message is thrown.      
 */
package cz.cuni.mff.d3s.deeco.annotations.processor;