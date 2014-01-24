This project is meant to provide the integration between jDEECo (Java) and the OMNET++ simulation framework (C++).
JNI is used in the implementation and as an output of the project a shared library is produced that provides native API for communicating with the simulation.

#### Building the project (WINDOWS):
* In oreder to build and use the library the OMNET++ distribution needs to be rebuild with slightly modified configuration. The modified config.user file is in the "omnet-config" directory and needs to be placed in the root directory of the OMNET++ installation. Specifically it directs building process to statically link some of the libraries that may conflict with other libraries in the system. To rebuild the OMNET++ please follow the process described in the manual.
* Another part is to configure properly the eclipse project. The project requires includes that are provided by the OMNET++ and as such in the Project Properties > C\C++ General > Paths and Symbols > Includes please add path to the "include" directory in your OMNET++ installation.
* Similarly the project needs to be linked with the OMNET++ libraries so in the Project Properties > C\C++ General > Paths and Symbols > Library Paths please point to the "bin" directory of the OMNET++ project. Alternatively, if you did not rebuild the OMNET++ distribution (see the first bullet) you can use libraries given in the project. For that you need to set "Library paths" to project location ("."). In the "Libraries" tab please add following entries "oppsim", "oppenvir" and "oppcommon".


