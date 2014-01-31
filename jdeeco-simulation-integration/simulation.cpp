#include "simulation.h"

#include <stdarg.h>
#include <stdio.h>
#include <string.h>
#include <cmath>

#include <vector>
#include <algorithm>

#include "jDEECoModule.h"
#include "cMessage.h"

#include "opp_ctype.h"
#include "args.h"
#include "distrib.h"
#include "cconfigoption.h"
#include "inifilereader.h"
#include "sectionbasedconfig.h"
#include "appreg.h"
#include "cmodule.h"
#include "fsutils.h"
#include "fnamelisttokenizer.h"
#include "stringutil.h"
#include "fileutil.h"
#include "intxtypes.h"
#include "startup.h"

#include <iostream>
#include <fstream>

USING_NAMESPACE;

Register_GlobalConfigOption(CFGID_LOAD_LIBS, "load-libs", CFG_FILENAMES, "",
		"A space-separated list of dynamic libraries to be loaded on startup. The libraries should be given without the `.dll' or `.so' suffix -- that will be automatically appended.");
Register_GlobalConfigOption(CFGID_CONFIGURATION_CLASS, "configuration-class",
		CFG_STRING, "",
		"Part of the Envir plugin mechanism: selects the class from which all configuration information will be obtained. This option lets you replace omnetpp.ini with some other implementation, e.g. database input. The simulation program still has to bootstrap from an omnetpp.ini (which contains the configuration-class setting). The class should implement the cConfigurationEx interface.");
Register_GlobalConfigOption(CFGID_USER_INTERFACE, "user-interface", CFG_STRING,
		"",
		"Selects the user interface to be started. Possible values are Cmdenv and Tkenv. This option is normally left empty, as it is more convenient to specify the user interface via a command-line option or the IDE's Run and Debug dialogs. New user interfaces can be defined by subclassing cRunnableEnvir.");

class jDEECoRuntime {
public:
	jobject host;
	JavaVM *jvm;
	const char * id;
	double firstCallAt;

	jDEECoRuntime(jobject host, JavaVM *jvm, const char *id) {
		this->jvm = jvm;
		this->host = host;
		this->id = id;
		this->firstCallAt = -1.0;
	}
};

std::vector<jDEECoRuntime *> jDEECoRuntimes;
std::vector<jDEECoModule *> jDEECoModules;
std::ofstream logger;

// helper macro
#define CREATE_BY_CLASSNAME(var,classname,baseclass,description) \
     baseclass *var ## _tmp = (baseclass *) createOne(classname); \
     var = dynamic_cast<baseclass *>(var ## _tmp); \
     if (!var) \
         throw cRuntimeError("Class \"%s\" is not subclassed from " #baseclass, (const char *)classname);

static void verifyIntTypes() {
#define VERIFY(t,size) if (sizeof(t)!=size) {printf("INTERNAL ERROR: sizeof(%s)!=%d, please check typedefs in include/inttypes.h, and report this bug!\n\n", #t, size); abort();}
	VERIFY(int8, 1);
	VERIFY(int16, 2);
	VERIFY(int32, 4);
	VERIFY(int64, 8);

	VERIFY(uint8, 1);
	VERIFY(uint16, 2);
	VERIFY(uint32, 4);
	VERIFY(uint64, 8);
#undef VERIFY

#define LL  INT64_PRINTF_FORMAT
	char buf[32];
	int64 a = 1, b = 2;
	sprintf(buf, "%"LL"d %"LL"d", a, b);
	if (strcmp(buf, "1 2") != 0) {
		printf(
				"INTERNAL ERROR: INT64_PRINTF_FORMAT incorrectly defined in include/inttypes.h, please report this bug!\n\n");
		abort();
	}
#undef LL
}

void simulate(const char * envName, const char * confFile) {
	cStaticFlag dummy;
	//
	// SETUP
	//
	cSimulation *simulationobject = NULL;
	cRunnableEnvir *app = NULL;
	SectionBasedConfiguration *bootconfig = NULL;
	cConfigurationEx *configobject = NULL;
	try {
		// construct global lists
		ExecuteOnStartup::executeAll();

		// verify definitions of int64, int32, etc.
		verifyIntTypes();

		//
		// First, load the ini file. It might contain the name of the user interface
		// to instantiate.
		//

		InifileReader *inifile = new InifileReader();
		if (fileExists(confFile))
			inifile->readFile(confFile);

		// activate [General] section so that we can read global settings from it
		bootconfig = new SectionBasedConfiguration();
		bootconfig->setConfigurationReader(inifile);
		bootconfig->activateConfig("General", 0);

		//load libs
		std::vector<std::string> libs = bootconfig->getAsFilenames(
				CFGID_LOAD_LIBS);
		for (int k = 0; k < (int) libs.size(); k++) {
			::printf("Loading %s ...\n", libs[k].c_str());
			loadExtensionLibrary(libs[k].c_str());
		}

		//
		// Create custom configuration object, if needed.
		//
		std::string configclass = bootconfig->getAsString(
				CFGID_CONFIGURATION_CLASS);
		if (configclass.empty()) {
			configobject = bootconfig;
		} else {
			// create custom configuration object
			CREATE_BY_CLASSNAME(configobject, configclass.c_str(),
					cConfigurationEx, "configuration");
			configobject->initializeFrom(bootconfig);
			configobject->activateConfig("General", 0);
			delete bootconfig;
			bootconfig = NULL;

			// load libs from this config as well
			std::vector<std::string> libs = configobject->getAsFilenames(
					CFGID_LOAD_LIBS);
			for (int k = 0; k < (int) libs.size(); k++)
				loadExtensionLibrary(libs[k].c_str());
		}

		// validate the configuration, but make sure we don't report cmdenv-* keys
		// as errors if Cmdenv is absent; same for Tkenv.
		std::string ignorablekeys;
		if (omnetapps.getInstance()->lookup("Cmdenv") == NULL)
			ignorablekeys += " cmdenv-*";
		if (omnetapps.getInstance()->lookup("Tkenv") == NULL)
			ignorablekeys += " tkenv-*";
		configobject->validate(ignorablekeys.c_str());

		//
		// Choose and set up user interface (EnvirBase subclass). Everything else
		// will be done by the user interface class.
		//
		const char * appname = envName;
		if (appname == NULL || opp_strcmp(appname, "") == 0)
			appname = configobject->getAsString(CFGID_USER_INTERFACE).c_str();
		cOmnetAppRegistration *appreg = NULL;
		if (!(appname == NULL || opp_strcmp(appname, "") == 0)) {
			// look up specified user interface
			appreg =
					static_cast<cOmnetAppRegistration *>(omnetapps.getInstance()->lookup(
							appname));
			if (!appreg) {
				::printf(
						"\n"
								"User interface '%s' not found (not linked in or loaded dynamically).\n"
								"Available ones are:\n", appname);
				cRegistrationList *a = omnetapps.getInstance();
				for (int i = 0; i < a->size(); i++)
					::printf("  %s : %s\n", a->get(i)->getName(),
							a->get(i)->info().c_str());

				throw cRuntimeError("Could not start user interface '%s'",
						appname);
			}
		} else {
			// user interface not explicitly selected: pick one from what we have
			appreg = cOmnetAppRegistration::chooseBest();
			if (!appreg)
				throw cRuntimeError(
						"No user interface (Cmdenv, Tkenv, etc.) found");
		}

		//
		// Create interface object.
		//
		::printf("Setting up %s...\n", appreg->getName());
		app = appreg->createOne();
	} catch (std::exception& e) {
		::fprintf(stderr, "\n<!> Error during startup: %s.\n", e.what());
		if (app) {
			delete app;
			app = NULL;
		} else {
			delete bootconfig;
		}
	}

	//
	// RUN
	//
	try {
		if (app) {
			simulationobject = new cSimulation("simulation", app);
			cSimulation::setActiveSimulation(simulationobject);
			app->run(0, NULL, configobject);
		}
	} catch (std::exception& e) {
		::fprintf(stderr, "\n<!> %s.\n", e.what());
	}

	//
	// SHUTDOWN
	//
	cSimulation::setActiveSimulation(NULL);
	delete simulationobject;  // will delete app as well

	componentTypes.clear();
	nedFunctions.clear();
	classes.clear();
	enums.clear();
	classDescriptors.clear();
	configOptions.clear();
	omnetapps.clear();
	cSimulation::clearLoadedNedFiles();
}

JNIEXPORT jint JNICALL _JNI_OnLoad(JavaVM *vm, void *reserved) {
	return JNI_VERSION_1_6;
}

JNIEXPORT jdouble JNICALL _Java_cz_cuni_mff_d3s_deeco_simulation_Simulation_nativeGetCurrentTime(
		JNIEnv *env, jobject jsimulation) {
	if (cSimulation::getActiveSimulation() == NULL)
		return -1;
	else
		return cSimulation::getActiveSimulation()->getSimTime().dbl();
}

JNIEXPORT void JNICALL _Java_cz_cuni_mff_d3s_deeco_simulation_Simulation_nativeRegister(
		JNIEnv *env, jobject jsimulation, jobject object, jstring id) {
	const char * cstring = env->GetStringUTFChars(id, 0);
	for (std::vector<jDEECoRuntime *>::iterator it = jDEECoRuntimes.begin();
			it != jDEECoRuntimes.end(); ++it) {
		if (opp_strcmp((*it)->id, cstring) == 0) {
			return;
		}
	}
	JavaVM *jvm;
	jint status = env->GetJavaVM(&jvm);
	if (JNI_OK == status) {
		jDEECoRuntimes.push_back(
				new jDEECoRuntime(env->NewGlobalRef(object), jvm, cstring));
		//::printf("JVM successfully retrieved\n");
	} else {
		::printf("JVM could not be retrieved!\n");
	}
}

JNIEXPORT void JNICALL _Java_cz_cuni_mff_d3s_deeco_simulation_Simulation_nativeSendPacket(
		JNIEnv *env, jobject jsimulation, jstring id, jbyteArray packet,
		jstring recipient) {
	const char * cstring = env->GetStringUTFChars(id, 0);
	for (std::vector<jDEECoModule *>::iterator it = jDEECoModules.begin();
			it != jDEECoModules.end(); ++it) {
		if (opp_strcmp((*it)->jDEECoGetModuleId(), cstring) == 0) {
			int length = env->GetArrayLength(packet);
			signed char* buffer = new signed char[length];
			env->GetByteArrayRegion(packet, 0, length,
					reinterpret_cast<jbyte*>(buffer));
			JDEECoPacket *jPacket = new JDEECoPacket(JDEECO_DATA_MESSAGE);
			//Setting data
			jPacket->setDataArraySize(length);
			for (int i = 0; i < length; i++)
				jPacket->setData(i, buffer[i]);
			const char * cRecipient = env->GetStringUTFChars(recipient, 0);
			(*it)->jDEECoSendPacket(jPacket, cRecipient);
			break;
		}
	}
	env->ReleaseStringUTFChars(id, cstring);
}

JNIEXPORT void JNICALL _Java_cz_cuni_mff_d3s_deeco_simulation_Simulation_nativeRun(
		JNIEnv *env, jobject jsimulation, jstring environment, jstring confFile) {
	logger.open ("c-log.txt");
	const char * cEnv = env->GetStringUTFChars(environment, 0);
	const char * cConfFile = env->GetStringUTFChars(confFile, 0);
	simulate(cEnv, cConfFile);
	jDEECoModules.clear();
	jDEECoRuntimes.clear();
	env->ReleaseStringUTFChars(environment, cEnv);
	env->ReleaseStringUTFChars(environment, cConfFile);
	logger.close();
}

JNIEXPORT void JNICALL _Java_cz_cuni_mff_d3s_deeco_simulation_Simulation_nativeCallAt(
		JNIEnv *env, jobject jsimulation, jdouble absoluteTime, jstring id) {
	//printf("Before callback registration\n");
	const char * cstring = env->GetStringUTFChars(id, 0);
	if (cSimulation::getActiveSimulation() != NULL) {
		for (std::vector<jDEECoModule *>::iterator it = jDEECoModules.begin();
				it != jDEECoModules.end(); ++it) {
			if (opp_strcmp((*it)->jDEECoGetModuleId(), cstring) == 0) {
				//::printf("Will be called at %f\n", absoluteTime);
				(*it)->jDEECoCallAt(absoluteTime);
				break;
			}
		}
	} else {
		for (std::vector<jDEECoRuntime *>::iterator it = jDEECoRuntimes.begin();
				it != jDEECoRuntimes.end(); ++it) {
			if (opp_strcmp((*it)->id, cstring) == 0) {
				(*it)->firstCallAt = absoluteTime;
				//callbacks at 0 time are not allowed
				//::printf("Will be called first at %f\n", absoluteTime);
				break;
			}
		}
	}
	env->ReleaseStringUTFChars(id, cstring);
}

JNIEXPORT jboolean JNICALL _Java_cz_cuni_mff_d3s_deeco_simulation_Simulation_nativeIsPositionInfoAvailable
  (JNIEnv *env, jobject jsimulation, jstring id) {
	const char * cstring = env->GetStringUTFChars(id, 0);
	jboolean result = 0;
	for (std::vector<jDEECoModule *>::iterator it = jDEECoModules.begin();
		it != jDEECoModules.end(); ++it) {
		if (opp_strcmp((*it)->jDEECoGetModuleId(), cstring) == 0) {
			result = (*it)->jDEECoIsPositionInfoAvailable();
			break;
		}
	}
	env->ReleaseStringUTFChars(id, cstring);
	return result;
}

JNIEXPORT jdouble JNICALL _Java_cz_cuni_mff_d3s_deeco_simulation_Simulation_nativeGetPositionX
  (JNIEnv *env, jobject jsimulation, jstring id) {
	const char * cstring = env->GetStringUTFChars(id, 0);
	jdouble result = 0;
	for (std::vector<jDEECoModule *>::iterator it = jDEECoModules.begin();
		it != jDEECoModules.end(); ++it) {
		if (opp_strcmp((*it)->jDEECoGetModuleId(), cstring) == 0) {
			result = (*it)->jDEECoGetPositionX();
			break;
		}
	}
	env->ReleaseStringUTFChars(id, cstring);
	return result;
}

JNIEXPORT jdouble JNICALL _Java_cz_cuni_mff_d3s_deeco_simulation_Simulation_nativeGetPositionY
  (JNIEnv *env, jobject jsimulation, jstring id) {
	const char * cstring = env->GetStringUTFChars(id, 0);
	jdouble result = 0;
	for (std::vector<jDEECoModule *>::iterator it = jDEECoModules.begin();
		it != jDEECoModules.end(); ++it) {
		if (opp_strcmp((*it)->jDEECoGetModuleId(), cstring) == 0) {
			result = (*it)->jDEECoGetPositionY();
			break;
		}
	}
	env->ReleaseStringUTFChars(id, cstring);
	return result;
}

JNIEXPORT jdouble JNICALL _Java_cz_cuni_mff_d3s_deeco_simulation_Simulation_nativeGetPositionZ
(JNIEnv *env, jobject jsimulation, jstring id) {
	const char * cstring = env->GetStringUTFChars(id, 0);
	jdouble result = 0;
	for (std::vector<jDEECoModule *>::iterator it = jDEECoModules.begin();
		it != jDEECoModules.end(); ++it) {
		if (opp_strcmp((*it)->jDEECoGetModuleId(), cstring) == 0) {
			result = (*it)->jDEECoGetPositionZ();
			break;
		}
	}
	env->ReleaseStringUTFChars(id, cstring);
	return result;
}

DLLEXPORT_OR_IMPORT void jDEECoModule::jDEECoCallAt(double absoluteTime) {
	//::printf("Adding callback at: %f for %f\n", simTime().dbl(), absoluteTime);
	if (currentCallAtTime != absoluteTime && simTime().dbl() < absoluteTime) {
		currentCallAtTime = absoluteTime;
		cMessage *msg = new cMessage(JDEECO_TIMER_MESSAGE);
		currentCallAtMessage = msg;
		jDEECoScheduleAt(absoluteTime, msg);
	}
}

DLLEXPORT_OR_IMPORT void jDEECoModule::jDEECoOnHandleMessage(cMessage *msg) {
	if (jvm != NULL && host != NULL) {
		JavaVM *cJvm = (JavaVM *) jvm;
		JNIEnv *env;
		cJvm->AttachCurrentThread((void **) &env, NULL);
		jclass cls = env->GetObjectClass((jobject) host);
		jmethodID mid;
		if (opp_strcmp(msg->getName(), JDEECO_TIMER_MESSAGE) == 0) {
			logger << "1: At callback at: " << simTime().dbl() << " for " << currentCallAtTime << "\n";
			// compare in nanos
			if (((long) round(simTime().dbl() * 1000000)) == ((long) round(currentCallAtTime * 1000000))) {
				logger << "2: At callback at: " << simTime().dbl() << " for " << currentCallAtTime << "\n";
				mid = env->GetMethodID(cls, "at", "(D)V");
				if (mid == 0)
					return;
				logger << "3: At callback at: " << simTime().dbl() << " for " << currentCallAtTime << "\n";
				env->CallVoidMethod((jobject) host, mid, currentCallAtTime);
			} else {
				//Ignore the message as it is not valid any longer.
			}
		} else if (opp_strcmp(msg->getName(), JDEECO_DATA_MESSAGE) == 0) {
			mid = env->GetMethodID(cls, "packetReceived", "([B)V");
			if (mid == 0)
				return;
			//::printf("Data message\n");
			JDEECoPacket *jPacket = check_and_cast<JDEECoPacket *>(msg);
			signed char* buffer = new signed char[jPacket->getDataArraySize()];
			for (unsigned int i = 0; i < jPacket->getDataArraySize(); i++)
				buffer[i] = jPacket->getData(i);
			jbyteArray jArray = env->NewByteArray(jPacket->getDataArraySize());
			env->SetByteArrayRegion(jArray, 0, jPacket->getDataArraySize(),
					(jbyte *) buffer);
			env->CallVoidMethod((jobject) host, mid, jArray);
		}
	}
}

DLLEXPORT_OR_IMPORT void jDEECoModule::jDEECoInitialize() {
	::printf("Initializing jDEECo module: %s\n", jDEECoGetModuleId());
	if (std::find(jDEECoModules.begin(), jDEECoModules.end(), this)
			== jDEECoModules.end()) {
		for (std::vector<jDEECoRuntime *>::iterator it = jDEECoRuntimes.begin();
				it != jDEECoRuntimes.end(); ++it) {
			if (opp_strcmp((*it)->id, jDEECoGetModuleId()) == 0) {
				host = (*it)->host;
				jvm = (*it)->jvm;
				//::printf("First callAt time: %f\n", (*it)->firstCallAt);
				if ((*it)->firstCallAt >= 0)
					jDEECoCallAt((*it)->firstCallAt);
				jDEECoModules.push_back(this);
				::printf("Module registered\n");
				break;
			}
		}
	}
}
