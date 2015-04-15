# Omnet++ integration for jDEECo
This project is meant to provide the integration between jDEECo (Java) and the OMNET++ simulation framework (C++).
JNI is used in the implementation and as an output of the project a shared library is produced that provides native API for communicating with the simulation.

## Versions and modules
Currently core Omnet++ and two modules called inet and mixim are used together with jDEECo. These are the versions of these libraries intended to be used with this manual:

- Omnet++ 4.6
- inet 2.5
- mixim 2.3

## Usage of jDEECo with Omnet++
As Omnet++ and it's modules are native libraries it is tricky to use them in Java. In order to make integration more simple a set of pre-build libraries for Fedora Linux and Windows are available as Maven dependencies.

### Native libraries and ned files
Currently these libraries are packed for 64bit release and 64bit debug flavors. The artifacts are located in the following maven repository [repo](https://gitlab.d3s.mff.cuni.cz:8443/nexus/). In order to use Omnet++ libraries native libraries and "ned" files are needed in the project. These two can be provided by the following Maven dependencies:

	<dependency>
		<groupId>cz.cuni.mff.d3s.jdeeco</groupId>
		<artifactId>cz.cuni.mff.d3s.jdeeco.simulation.omnet-native</artifactId>
		<version>2.0.0</version>
		<classifier>natives-common-ned</classifier>
	</dependency>
	<dependency>
		<groupId>cz.cuni.mff.d3s.jdeeco</groupId>
		<artifactId>cz.cuni.mff.d3s.jdeeco.simulation.omnet-native</artifactId>
		<version>2.0.0</version>
		<classifier>natives-linux64-debug</classifier>
	</dependency>
	
The dependency packages are distinguished by classifiers. The jar `natives-common-ned` provides platform independent description of network devices, while the `natives-linux64-debug` provides native libraries for linux with debug enabled. The native libraries can be altered with different version such as `natives-windows64-release`. Maven profiles selected by OS family are the prefered way for managing these platform specific packages.

### Unpacking dependencies
As the native libraries as well as ned files cannot be referenced from inside jar packages, these need to be extracted. The extraction is handled by Maven plugin which extracts the jar in to the project directory. The plugin can be used like this:

	<plugin>
		<groupId>com.googlecode.mavennatives</groupId>
		<artifactId>maven-nativedependencies-plugin</artifactId>
		<version>0.0.7</version>
		<executions>
			<execution>
				<configuration>
					<nativesTargetDir>.</nativesTargetDir>
					<separateDirs>false</separateDirs>
				</configuration>
				<id>unpacknatives</id>
				<phase>generate-resources</phase>
				<goals>
					<goal>copy</goal>
				</goals>
			</execution>
		</executions>
	</plugin>
	
### Referencing native libraries
Once the native libraries are in the place the java.library.path variable needs to be adjusted to point into the project root directory, so that the Java native interface can find the libraries in question. Apart from the initial library load it is also needed to make sure that the system can load the dependencies. These are also unpacked into the project root directory, but are loaded by the OS, not Java. On Windows no action is needed as the current directory is searched for dependencies, while on the Linux the LD_LIBRARY_PATH environment variable needs to be set to point to the project root directory.

### Project specific setup and example project
There is an example project which can be used as inspiration for future projects. It is located on the [d3s gitlab](https://gitlab.d3s.mff.cuni.cz/matena/omnetjdeecotest) Apart from Java sources, which show how to initialize simple Omnet++ based simulation, the model directory and omnetpp.ini are the files of special interest. The model directory describes network interfaces to be used for the simulation, while the omnetpp.ini describes the simulation properties. The omnetpp.ini is also important for loading the simulation environment libraries. These are loaded by this line: 'load-libs = oppcmdenvd'
It is necessary to remove the "d" suffix when the release libraries are being used. Otherwise the runtime will try to dlopen nonexistent library. Even when the debug library would be present in the release environment the simulation would not run as mixing the debug and release libraries is not allowed. 

## Compiling native libraries
Preparing native libraries is a complicated process. It is even more complex as we want the resulting libraries to be 64bit. The process is also a bit different on Windows and on Linux. First we will describe the common parts then the compilation process will be described for Windows and Linux separately.

### Common steps
During the process these versions as Omnet++ and modules were used:

- Omnet++ 4.6
- inet 2.5
- mixim 2.3

Appart from the Omnet++ related libraries these tools are required:

- Java development kit 1.8
- Maven
- tcl/tk 8.6
- patch
- mingw64 (only on windows)

Common steps are:

1. Create directory for the libraries (path to it should not contain spaces)
1. Unpack omnet++ (choose package based on your platform) to that directory (dir/omnetpp-4.6)
1. Unpack inet to that directory (dir/inet)
1. Unpack mixim to that directory (dir/mixim)
1. As the mixim 2.3 is not compatible with inet 2.5 it is necessary to apply a patch that solves the incompatibility. It is located in patches directory of this project and is called "mixim-2.3-with-inet-2.5.patch". Apply the patch to mixm directory
1. Continue with platform specific steps mentioned below 

### Linux steps
1. Go to the omnetpp-4.6 directory
1. Issue `. setenv` to setup environment variables (use this for every shell you will use)
1. Issue command `./configure`
1. Fix possible configure errors by installing appropriate packages (On Fedora it was necessary to install development versions of a few packages and tcl/tk libraries)
1. Make sure the optional xml support was found by configure (it is not reported as error, but we need it)
1. Issue `make -j5 MODE=debug` (assuming you want to compile the sources on 4 CPUs)
1. Issue `make -j5 MODE=release` (assuming you want to compile the sources on 4 CPUs)
1. Go to inet directory
1. Issue `make makefiles`
1. Issue `make -j5 MODE=debug` (assuming you want to compile the sources on 4 CPUs)
1. Issue `make -j5 MODE=release` (assuming you want to compile the sources on 4 CPUs)
1. Go to mixim directory
1. Issue `makefiles-using-inet`
1. Issue `make -j5 MODE=debug` (assuming you want to compile the sources on 4 CPUs)
1. Issue `make -j5 MODE=release` (assuming you want to compile the sources on 4 CPUs)
1. Now switch to the cz.cuni.mff.d3s.jdeeco.simulation.omnet-native project root
1. Fix JAVA_HOME path in MakefileL
1. Issue `make -f MakefileL makefiles`
1. Issue `make -f MakefileL MODE=debug`
1. Issue `make -f MakefileL MODE=release`
1. Issue `make install`
1. All the necessary libraries should have been compiled and copied to the libs/{debug|release} in the project root
1. Issue `mvn install` to create packages
1. Linux build does not produce the ned files which are needed, either produce them on windows, or grab all the *.ned files forom inet and mixim directories while keeping the directory structure. 

### Windows steps
1. Go to the omnetpp-4.6 directory
1. Unpack the mingw64 to the tools/win32/mingw64 (next to the mingw32)
1. As Omnet++ and inet does not support windows 64bit builds a patches called `omnetpp-4.6-win.patch` and `inet-2.5-win.patch` needs to be applied to respective directories
1. Run `mingwenv.cmd` in the omnetpp-4.6 root
1. Issue command `./configure`
1. Fix possible configure errors, it might be necessary to tune up configure.user and get tcl/tk libraries somewhere
1. Make sure the optional xml support was found by configure (it is not reported as error, but we need it)
1. Issue `make MODE=debug` (NOTE: parallel build does not work 100% properly on windows)
1. Issue `make MODE=release` (NOTE: parallel build does not work 100% properly on windows)
1. Go to inet directory
1. Issue `make makefiles`
1. Issue `make MODE=debug` (NOTE: parallel build does not work 100% properly on windows)
1. Issue `make MODE=release` (NOTE: parallel build does not work 100% properly on windows)
1. Go to mixim directory
1. Issue `makefiles-using-inet`
1. Issue `make MODE=debug` (NOTE: parallel build does not work 100% properly on windows)
1. Issue `make MODE=release` (NOTE: parallel build does not work 100% properly on windows)
1. Now switch to the cz.cuni.mff.d3s.jdeeco.simulation.omnet-native project root
1. Fix JAVA_HOME path in MakefileL
1. Issue `make -f Makefile makefiles`
1. Issue `make -f Makefile MODE=debug` (NOTE: parallel build does not work 100% properly on windows)
1. Replace `-loppcommond` with `-loppcommon` in the Makefile
1. Issue `make -f Makefile MODE=release` (NOTE: parallel build does not work 100% properly on windows)
1. Issue `make install`
1. All the necessary libraries and network interface descriptions should have been compiled and copied to the libs/{debug|release|ned} in the project root
1. Issue `mvn install` to create packages


### Deployment
1. Switch to the cz.cuni.mff.d3s.jdeeco.simulation.omnet-native project root
1. Fix `omnet` property in the pom.xml
1. Issue `mvn deploy` in the root of the cz.cuni.mff.d3s.jdeeco.simulation.omnet-native project to deploy packages to the Maven repository
1. It might happen that the deployment fails due to the pom file already in repository. This might happen when jar's for different architectures are deployed in separate steps. Just delete the pom from the repository and repeat deployment.





## Now out-dated, but possibly still relevant parts

#### Building the project (WINDOWS):
* In order to build and use the library the OMNET++ distribution needs to be rebuild with slightly modified configuration. The modified config.user file is in the "omnet-config" directory and needs to be placed in the root directory of the OMNET++ installation. Specifically it directs building process to statically link some of the libraries that may conflict with other libraries in the system. To rebuild the OMNET++ please follow the process described in the manual.
* Another part is to configure properly the eclipse project. The project requires includes that are provided by the OMNET++ and as such in the Project Properties > C\C++ General > Paths and Symbols > Includes please add path to the "include" directory in your OMNET++ installation.
* Similarly the project needs to be linked with the OMNET++ libraries so in the Project Properties > C\C++ General > Paths and Symbols > Library Paths please point to the "bin" directory of the OMNET++ project. Alternatively, if you did not rebuild the OMNET++ distribution (see the first bullet) you can use libraries given in the project. For that you need to set "Library paths" to project location ("."). In the "Libraries" tab please add following entries "oppsim", "oppenvir" and "oppcommon".

#### Usage in OMNET++:
In order to connect the OMNET++ simulation with jDEECo, one needs to create a simple module that inherits (pure C++ inheritance, not in NED file) from the jDEECoModule class. Appropriate headers files can be found in the project so an inclusion pointing to the project location is necessary. Also the reference to the integration library is necessary and it needs to be set in the "Library paths" tab in the Project Properties > C\C++ General > Paths and Symbols to point also to this project location.
Because OMNET++ relies on the Makemake, setting up particular libraries reference is different than previously. For that one needs to open the Project Properties > OMNt++ > Makemake > [configuration] > Options > Link > More and in the "Additional libraries to link" put "integration". Also in the Project Properties > OMNt++ > Makemake > [configuration] > Options > Target tab please select shared library. This shared library will be loaded when jDEECo is run. However, this step should be done at the end of the development of the network topology.

#### Usage with jDEECo:
To use the developed (in OMNet++) network topology one needs to provide all necessary libraries (i.e. main library + library used in the project such as iNET, Mixims, ....). All of them should be compiled in the relase mode so the correct libraries are loaded. Additionally, in the omnetpp.ini file please update the "load-libs" entry, which is the (white space separted) list of additional libraries that needs to be loaded for the simulation.
