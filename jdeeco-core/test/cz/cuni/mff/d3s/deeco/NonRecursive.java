package cz.cuni.mff.d3s.deeco;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *Used to Mark DynamicSuite as NonRecursive so that:<br>
 * If a Suite that's being run is marked as NonRecursive, it will only run classes<br>
 * within the same package.<br>
 * But If the  Suite being run is not marked as  NonRecursive,<br>
 * it ignores all NonRecursive annotations in Suites contained in subpackages of the package the<br>
 * Suite class belongs to.<br>
 * The rationale is to be able to test only Tests in a package when desired at the same time<br>
 * being able to test all tests in a project or in a package and subpackages when running when the Suite being run is<br>
 * not marked as NonRecursive.<br>
 * @author anjan
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface NonRecursive {
 
}