# Guide to the Ensemble Definition Language (EDL)

## Compiling
### Prerequisites
To compile the EDL packages, it is required to install the following packages into your Eclipse instance:
 - Eclipse SDK (for the Plugin Development Environment)
 - EMF - Eclipse Modeling Framework SDK
 - Xtext Complete SDK 
 
### Build workflow
The EDL consists of four projects - the core EDL language project (cz.cuni.mff.d3s.jdeeco.edl), the model project describing the structure of the EDL documents (cz.cuni.mff.d3s.jdeeco.edl.model), the GUI project containing the graphical editor (cz.cuni.mff.d3s.jdeeco.edl.ui), and the test project (cz.cuni.mff.d3s.jdeeco.edl.tests). When working with EDL, it is highly recommended to use the Eclipse in the **Modeling perspective**. The recommended workflow for the first build and when changing language features is as follows:
1. Generate model code - run the edl.genmodel model generation (model folder) from the edl.model project
2. Generate grammar artifacts - run the MWE workflow GenerateEDL.mwe2 either directly or by running EDL.xtext (Generate Xtext artifacts), both located in cz.cuni.mff.d3s.jdeeco.edl. Note that when running this step for the first time, you will be prompted to download the ANTLR parser, which might take a while to complete.
3. Export and reinstall EDL plugins, if necessary
 
## Exporting and installing the plugins
Some changes may require that you export the EDL plugins and install them in your Eclipse instance. This operation requires that the Eclipse SDK is installed. To export the plugins, hit **File -> Export** and select **Plug-in Development/Deployable plug-ins and fragments**. Select all EDL projects except the test project, pick a directory and make sure "Package plug-ins as individual JAR archives" is checked. Hit finish and locate the folder. Finally, copy all three JAR files to the plugins folder in your Eclipse installation folder. Upon next restart, Eclipse should automatically detect the new plugins and install them.

## Using the plugins
After installing the plugins, when creating an x.edl file in any Java project you should be prompted to add Xtext nature to this project. Doing so activates the EDL plugins for this project, including the GUI editor. Note that any EDL project must have the model and core EDL projects on the path, as well as the core JDEECo project. To enable the creation of ensembles, the project must also have the cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3 project and the Z3 libraries on the path. By default, EDL uses the standard approach of having a src folder and a src-gen folder, with files intended for modification being generated in the src folder, and other files in src-gen. The src-gen folder must be marked as a java source folder **(Build path -> Use as source folder)**. The folders used for generation can be overridden in the EDL properties of the project.   