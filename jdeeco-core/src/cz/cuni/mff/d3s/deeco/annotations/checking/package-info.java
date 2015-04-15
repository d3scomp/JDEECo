/**
 * This package contains classes that help the {@link cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessor}
 * to check the processed components and ensembles. When a component/an ensemble is processed
 * in the {@link cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessor}, a series of
 * validators is applied to the processed instance. If any of the validators finds an error
 * in the component instance/ensemble definition, an exception is thrown.
 * 
 * A generic component+ensemble validator is represented by the {@link cz.cuni.mff.d3s.deeco.annotations.checking.AnnotationChecker}
 * interface. The list of the actual validators that are currently used in the {@link AnnotationProcessor}
 * is represented by the {@link cz.cuni.mff.d3s.deeco.annotations.checking.AnnotationChecker#standardCheckers}
 * variable. Of course, the validation can be altered, or turned of by manually setting the checkers list
 * to the {@link cz.cuni.mff.d3s.deeco.annotations.checking.AnnotationChecker} constructor.
 * 
 * Currently, following validator clases are present in the package:
 * 
 * 1) {@link cz.cuni.mff.d3s.deeco.annotations.checking.RolesAnnotationChecker} class that checks that
 * components correctly implement roles, which the components declare to be impelenting.
 * A role is here a simple class that works similarly to an interface. If a component implements a role,
 * it must contain all fields that are present in the role class, with respective fields.
 * The {@link cz.cuni.mff.d3s.deeco.annotations.checking.RolesAnnotationChecker} also checks that ensembles
 * that declare coordinator and member roles, use only fields that are present in the respective role classes.
 * For instance, if an ensemble declares, that the coordinator component must implement a "Vehicle" role class,
 * only fields from the "Vehicle" for each knowledge path 'coord.<<X>>', the <<X>> part must be actually
 * valid for the "Vehicle" role. This way it is ensured that the ensemble membership and knowledge exchange
 * will always work with knowledge paths, that surely exist.
 * 
 * 2) {@link cz.cuni.mff.d3s.deeco.annotations.checking.ComponentProcessChecker} class that checks component
 * processes. A component process is considered invalid, if it uses knowledge paths that do not correspond
 * to any field in the component class. For instance if a process has an @@In("x") parameter, but no field "x"
 * is present in the component class, the component is invalid. The component is also invalid, if the field "x"
 * is present in the class, but the type of the member variable is different than the type of the process
 * parameter.
 * 
 * Additionaly, the package contains some helper classes and interfaces:
 * 
 * 1) {@link cz.cuni.mff.d3s.deeco.annotations.checking.TypeComparer} (implemented by
 * {@link cz.cuni.mff.d3s.deeco.annotations.checking.GenericTypeComparer}) that compares types when searching
 * for implementation in a role class or when comparing a method parameter type to the implemented field type.
 * 
 * 2) {@link cz.cuni.mff.d3s.deeco.annotations.checking.KnowledgePathChecker} (implemented by
 * {@link cz.cuni.mff.d3s.deeco.annotations.checking.KnowledgePathCheckerImpl}) that checks whether knowledge
 * paths exist in respective component classes.
 * 
 * 3) {@link cz.cuni.mff.d3s.deeco.annotations.checking.ParameterKnowledgePathExtractor} that is used to
 * extract information about parameters from component processes, ensemble memberships and knowledge exchanges.
 * 
 * @see cz.cuni.mff.d3s.deeco.annotations.checking.AnnotationChecker
 * @see cz.cuni.mff.d3s.deeco.annotations.checking.RolesAnnotationChecker
 * @see cz.cuni.mff.d3s.deeco.annotations.checking.ComponentProcessChecker
 * 
 * @author Zbyněk Jiráček
 *
 */
package cz.cuni.mff.d3s.deeco.annotations.checking;