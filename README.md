JDEECo is a Java implementation of the DEECo domponent system. For further information, please visit [http://d3s.mff.cuni.cz/projects/components_and_services/deeco/](http://d3s.mff.cuni.cz/projects/components_and_services/deeco/).

#Setup 
##Requirements
To compile and run the JDEECo framework, the following software has to be available on your system:

* Java SDK >= 1.6.0 ([http://java.com/en/](http://java.com/en/))
* Apache River distributed tuple-space >= 2.2.0 ([http://river.apache.org](http://river.apache.org))

## Compilation and Deployment
To compile the project you will need to have Apache Ant (http://ant.apache.org/) installed on your machine (version used was 1.8.3).

Compilation is performed by simply executing `ant` command in the JDEECo folder.
All generated binaries together with zipped sources are placed in the `dist` folder. 
Additionally AppacheRiver 2.2 is downloaded, set up and all necessary scripts are generated.

To run the demo application simply execute `ant demo`, which should automatically start all necessary services and the main application.

At the moment support for the Windows machine is provided, however shell scripts will be provided shortly.

