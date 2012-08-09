JDEECo is a Java implementation of the DEECo component system. For further information, please visit [http://d3s.mff.cuni.cz/projects/components_and_services/deeco/](http://d3s.mff.cuni.cz/projects/components_and_services/deeco/).

#Setup 
##Requirements
To compile and run the JDEECo framework, the following software has to be available on your system:

* Java SDK >= 1.6.0 ([http://java.com/en/](http://java.com/en/))
* Apache River distributed tuple-space >= 2.2.0 ([http://river.apache.org](http://river.apache.org)) (downloaded and setup automatically by ant)

Additionally if you want to use Java PathFinder for model check of your application, you need to have Mercurial (http://mercurial.selenic.com/)
installed as ant run script will use it inorder to download and configure it automatically.

## Compilation and Deployment
To compile the project you will need to have Apache Ant (http://ant.apache.org/) installed on your machine (version 1.8.3 was used when writing the script).

Compilation is performed by simply executing `ant` command in the JDEECo folder.
All generated binaries together with zipped sources are placed in the `dist` folder. 
Additionally AppacheRiver 2.2 is downloaded, set up and all necessary scripts are generated.

To run the demo application perform the following two step:

* Start the Apache River by executing `ant start-ar` (starts Tuple Spaces service)
* Start the demo (using local knowledge repository)
  **Cloud demo: `ant cloud-local`
  **Convoy demo: `ant convoy-local`
* Start the demo (using tuple space knowledge repository) by executing `ant demo-ts`


