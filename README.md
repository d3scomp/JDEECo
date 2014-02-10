JDEECo is a Java implementation of the DEECo component system. For further information, please visit [http://d3s.mff.cuni.cz/projects/components_and_services/deeco/](http://d3s.mff.cuni.cz/projects/components_and_services/deeco/).

##Requirements
To compile and run the JDEECo framework, the following software has to be available on your system:

* Java SDK >= 1.7.0 ([http://java.com/en/](http://java.com/en/))

## Compilation and Deployment
To compile the project you will need to have Apache Maven (http://maven.apache.org/) installed on your machine (version 3.1.1 was used when writing the script).

#### There are two ways you can compile the sources using maven:
 * `default` - produces both simple jar and the OSGi bundle for jDEECo core library and demos
 * `OSGi` - produces OSGi bundle for jDEECo core
 * `core` - produces the simple jar for jDEECo

The `default` compilation is performed by simply executing `mvn clean install` command in the `jdeeco-parent` subfolder.
In order to compile sources to have OSGi bundle of jDEECo on outcome `mvn clean install` command in the `jdeeco-core-osgi` subfolder. To get just the simple jar one needs to run `mvn clean install` in the `jdeeco-core`.
All generated binaries along with dependencies needed to run jdeeco are placed in the `dist` folder.

## Concepts
A new node called `jdeeco-concepts` is available now. It contains projects that currently cannot be run or implemented(in terms of containig syntax sugar cutrrenly not supported by jdeeco) in jdeeco. Those projects are for experimenting and coining ideas that would be nice to have in future releases only.

## Demos
Demo projects for jdeeco can be found in `jdeeco-demos` folder. They can be built either by running `mvn clean install` in jdeeco-demos or via running the same command from `jdeeco-parent` (this will eventually generate all the binaries). In both cases binaries `cloud.jar` and `firefighters.jar` will appear in `dist` folder along with an ant script to run them. There are 5 different demos out of those two demo binaries

#### Cloud demos
* ant LocalLauncherCloud
* ant LocalLauncherDynamicCloud
* ant TSLauncherCloud

#### Firefighters demos
* ant FFLauncher
* ant FFHexacopterLauncher

## Tutorial
A jDEECo tutorial featuring a simple convoy example can be found as a separate project at https://github.com/d3scomp/JDEECo-Convoy-Tutorial. 

## Eclipse integration
All of the projects in the repository are the Eclipse projects. As such they can be easily imported to the Eclipse workspace.
