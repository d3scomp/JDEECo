JDEECo is a Java implementation of the DEECo component system. For further information, please visit [http://d3s.mff.cuni.cz/projects/components_and_services/deeco/](http://d3s.mff.cuni.cz/projects/components_and_services/deeco/).

##Requirements
To compile and run the JDEECo framework, the following software has to be available on your system:

* Java SDK >= 1.7.0 ([http://java.com/en/](http://java.com/en/))

## Compilation and Deployment
To compile the project you will need to have Apache Maven (http://maven.apache.org/) installed on your machine (version 3.04 was used when writing the script).

#### There are two ways you can compile the sources using maven:
 * `default` - produces simple jars for jDEECo core library
 * `OSGi` - produces OSGi bundles for jDEECo core

The `default` compilation is performed by simply executing `mvn clean install` command in the jdeeco-parent subfolder.
In order to compile sources to have OSGi bundle of jDEECo on outcome `mvn clean install` command in the jdeeco-core-osgi subfolder. A new folder named `target` will be created in jdeeco-core-osgi containing all the binaries needed.
All generated binaries together with zipped sources are placed in the `dist` folder in the root of the project. 

// TODO: Add demo applications

## Tutorial
A jDEECo tutorial featuring a simple convoy example can be found as a separate project at https://github.com/d3scomp/JDEECo-Convoy-Tutorial. 

## Eclipse integration
All of the projects in the repository are the Eclipse projects. As such they can be easily imported to the Eclipse workspace.

To set up logging in Eclipse, you have to add the 'config' folder to the classpath (through Run Configurations).
