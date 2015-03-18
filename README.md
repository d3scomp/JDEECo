JDEECo is a Java implementation of the DEECo component system. For further information, please visit [http://d3s.mff.cuni.cz/projects/components_and_services/deeco/](http://d3s.mff.cuni.cz/projects/components_and_services/deeco/).

##Requirements
To compile and run the JDEECo framework, the following software has to be available on your system:

* Java SDK >= 1.8.0 ([http://java.com/en/](http://java.com/en/))

## Compilation and Deployment
To compile the project you will need to have Apache Maven (http://maven.apache.org/) installed on your machine (version 3.1.1 was used when writing the script).

#### There are two ways you can compile the sources using maven:
 * `default` - produces both simple jar and the OSGi bundle for jDEECo core library and demos
 * `OSGi` - produces OSGi bundle for jDEECo core
 * `core` - produces the simple jar for jDEECo

The `default` compilation is performed by simply executing `mvn clean install` command in the `jdeeco-parent` subfolder.
In order to compile sources to have OSGi bundle of jDEECo on outcome `mvn clean install` command in the `jdeeco-core-osgi` subfolder. To get just the simple jar one needs to run `mvn clean install` in the `jdeeco-core`.
All generated binaries along with dependencies needed to run jdeeco are placed in the `dist` folder.

## Tutorial
A jDEECo tutorial featuring a simple convoy example can be found as a separate project at https://github.com/d3scomp/JDEECo-Convoy-Tutorial. 

## Project structure

####Binaries
All binaries for jDEECo can be found in directory `dist`, which is created automatically in the root folder. Even if a specific part of jDEECo is built binaries will appear there (also could be found in projects `target` folder).

#### Build All
The universal build script is situated in `jdeeco-parent`. Running this script builds jDEECo in all available configurations along with demos.

#### OSGI support
To build jDEECo as an OSGi bundle, you run `mvn clean install` in `jdeeco-core-osgi`.

#### Core
To build jDEECo core you need to go to `jdeeco-core` folder and run `mvn clean install`. This will build jDEECo along with tests and produce the jDEECo jar.

#### Demos
Demo projects for jdeeco can be found in `jdeeco-demos` folder. There are two projects: the cloud case study based one, and one concerning firefighters case study. They can be built either by running `mvn clean install` in jdeeco-demos or via running the same command from `jdeeco-parent` (this will eventually generate all the binaries, as mentioned before). In both cases binaries `cloud.jar` and `firefighters.jar` will appear in `dist` folder along with an ant script to run them. There are 5 different demos out of those two projects

##### Cloud demos
* ant LocalLauncherCloud
* ant LocalLauncherDynamicCloud
* ant TSLauncherCloud

###### Firefighters demos
* ant FFLauncher
* ant FFHexacopterLauncher

## Eclipse integration
All of the projects in the repository are the Eclipse projects. As such they can be easily imported to the Eclipse workspace.

In the project there are included custom formatters. One of them colors the logging output that goes into the Eclipse console using ANSI escape sequences. In order to be able to see the output correctly this plugin ([eclipse-plugin-ansi-in-console](http://mihai-nita.net/2013/06/03/eclipse-plugin-ansi-in-console/)) needs to be installed into your Eclipse.

#### CDEECo
Apart from Java version of DEECo, there is its C++ realization that can be found under: ([http://github.com/d3scomp/CDEECo](http://github.com/d3scomp/CDEECo))
