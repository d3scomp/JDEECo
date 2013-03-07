JDEECo is a Java implementation of the DEECo component system. For further information, please visit [http://d3s.mff.cuni.cz/projects/components_and_services/deeco/](http://d3s.mff.cuni.cz/projects/components_and_services/deeco/).

#Setup 
##Requirements
To compile and run the JDEECo framework, the following software has to be available on your system:

* Java SDK >= 1.6.0 ([http://java.com/en/](http://java.com/en/))

## Compilation and Deployment
To compile the project you will need to have Apache Maven (http://maven.apache.org/) installed on your machine (version 3.04 was used when writing the script).

Compilation is performed by simply executing `mvn clean install` command in the jdeeco-parent subfolder.
All generated binaries together with zipped sources are placed in the `dist` folder in the root of the project. 
Additionally AppacheRiver 2.2 and Java PathFinder distribution are downloaded, set up and all required scripts are generated.

To simplify the execution of demo applications `ant` script is provided, that allows for single command launch.
If there is no Apache Ant installation on the machine, please download and configure one from the following link:
http://ant.apache.org/


To run one of demo applications perform open command window in the `dist` directory and execute one of the following commands:

* Start the demo (using local knowledge repository):
 * Cloud demo: `ant cloud-local`
 * Convoy demo: `ant convoy-local`
* Start the demo (using tuple space knowledge repository):
First, run `ant start-ar` in a separate console and wait for it to start (approx 10sec). Specifically, wait until the following is printed (in arbitrary order):
```
[java] INFO: ClassServer started [[D:\documents\skola\projects\deeco\JDEECo\dist\apache-river\lib\, D:\documents\skola\projects\deeco\JDEECo\dist\apache-river\lib-dl\], port 8080]
[java] INFO: Mahalo started: com.sun.jini.mahalo.TransientMahaloImpl@19ae493
[java] INFO: started Reggie: cc519f68-6182-4b17-8578-28cae0563484, [cz.cuni.mff.d3s.deeco.lookupGroup], jini://10.10.16.127/
[java] INFO: Outrigger server started: com.sun.jini.outrigger.OutriggerServerImpl@1267610

```
Then run the following in a separate console:
 * *Cloud demo*: `ant cloud-ts`
 * *Convoy demo*: `ant convoy-ts`

To run cloud demo under the Java PathFinder (only local knowledge repository supported) issue the following ant command: `ant jpf-cloud`.

## Tutorial
A jDEECo tutorial featuring a simple convoy example can be found as a separate project at https://github.com/d3scomp/JDEECo-Convoy-Tutorial. 

## Eclipse integration
All of the projects in the repository are the Eclipse projects. As such they can be easily imported to the Eclipse workspace.

In order to work with jDEECo sources in the Eclipse IDE a proper target platform needs to be configured. For that purpose both `core` and `sde` projects contain the `targetDefinition.target` files, which points to the required update sites containing necessary plugins.
To set them active, click the `Set as Target Platform` link (top left corner) in the overview tab of the targetDefinition.target.
