package cz.cuni.mff.d3s.deeco;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;

/**
 *TestSuite execute all test classes from the package of  <br>
 * classes that are annotated to run with this class<br>
 * It loads all classes in the same package as the setup class and
 * loads classes from sub packages recursively.
 * If a sub-package contains a class that's marked to be run with this class<br>
 * it only loads that class so that the marked class can in turn load and execute<br>
 * test classes in its package and its subpackages.<br><br>
 * To cut it short: This class loads and executes all test classes in its package and its
 * subpackages recursively.<br<br>
 * Example usage:Just create an empty class and add <code>@RunWith</code> annotation<br>
 *
 *  <code>@RunWith(DynamicSuite.class)<br>
 * public class BatchTest {<br>
 * //no methods or body needed.<br>
 * }</code>
 * <br><br>
 * To test only classes in a package and ignore test classes in subpackages mark the class with @NonRecursive.
 * To test all test classes in a project mark a class at a project's  root test  package  with <code>@RunWith(DynamicSuite.class)</code>
 * and it will run all test classes in the project.
 * The only catch so far is that all the methods of all test classes gets run under
 * this the setup class and the results are all grouped under that class , at least from what I can see in Netbeans.
 *
 *
 * @author anjan
 */
public class DynamicSuite extends Suite {
 
    static boolean isEntrySuite=true;
    public DynamicSuite(Class<?> setupClass) throws InitializationError {
 
        super(setupClass, getTestClasses(setupClass,setupClass.isAnnotationPresent(NonRecursive.class)));
 
            //this will run after the initial setup has been done;
            //If a  Class is marked as NonRecursive and its the entry point of tests
            // it won't load test classes from subpackages.
            //but if the Class is marked as NonRecursive and its not the entry point
           // of tests the annotation will be ignored and all classes from subpackages of the containing
          //package will be loaded
            isEntrySuite=false;
 
    }
 
    public static Class<?>[] getTestClasses(Class<?> setupClass ,boolean nonRecursive) {
 
        List<Class<?>> classes = null;
 
        String packageName = setupClass.getPackage().getName();
        String path = setupClass.getProtectionDomain().getCodeSource().getLocation().getFile();
        path = path + packageName.replace('.', File.separatorChar);
 
        File dir = new File(path);
 
        try {
            classes = findTestClasses(dir, setupClass,nonRecursive);
        } catch (Exception ex) {
            throw new RuntimeException("Problem loading classes from package[" + packageName + "]", ex);
        }
        return classes.toArray(new Class[classes.size()]);
    }
 
    /**
     * recursively finds Classes in test package <br>
     * if it finds a class annotated with @RubWith(DynamicSuite.class)
     * if loads that class and ignores all classes and sub-packages in that package
     * because the loaded class is responsible for loading classes under ite hierarchy
     * @param dir   The root directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    private static List<Class<?>> findTestClasses(File dir, Class<?> setupClass, boolean nonRecursive) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (!dir.exists()) {
            return classes;
        }
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
               //only load classes in subpackage if the setup class isn't marked as NonRecursive
                // or its marked as NonRecursive but its not the entry point class
                if(!nonRecursive ||(nonRecursive && !isEntrySuite))
                classes.addAll(findTestClasses(file, setupClass,nonRecursive));
            }
            else if (file.getName().endsWith(".class")) {
 
                String className = file.getName().substring(0, file.getName().length() - 6);
 
                if (!className.equals(DynamicSuite.class.getSimpleName())) {
 
                    String packageDirPath = file.getPath();
                    packageDirPath = packageDirPath.substring(packageDirPath.indexOf("classes"));
                    String pkg = packageDirPath.replace(File.separatorChar, '.').substring(8).replace("." + file.getName(), "");
                    Class<?> cls = Class.forName(pkg + '.' + className);
                    if (cls.isAnnotationPresent(RunWith.class)) {
                        RunWith ann = (RunWith) cls.getAnnotation(RunWith.class);
 
                        if (ann.value().equals(DynamicSuite.class)) {
                           //ignore the class being run, otherwise the program will crash with StackOverflowError
                            if (!cls.equals(setupClass)) {
                                classes = new ArrayList<>();
                                classes.add(cls);
                                break;
                            }
                        } else {
                            classes.add(cls);
                        }
 
                    } else {
                    	for (Method m: cls.getMethods()) {
                    		if (m.isAnnotationPresent(Test.class)) {
                    			classes.add(cls);
                    			break;
                    		}
                    	}                        
                    }
 
                }
            }
        }
 
        return classes;
    }
 
}