#include "cz_cuni_mff_d3s_deeco_simulation_omnet_OMNetSimulation.h"

#include <stdarg.h>
#include <stdio.h>
#include <string.h>
#include <cmath>

#include <vector>
#include <algorithm>

#include "JDEECoModule.h"
#include "cmessage.h"

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

USING_NAMESPACE;

Register_GlobalConfigOption(CFGID_LOAD_LIBS, "load-libs", CFG_FILENAMES, "",
		"A space-separated list of dynamic libraries to be loaded on startup. The libraries should be given without the `.dll' or `.so' suffix -- that will be automatically appended.");
Register_GlobalConfigOption(CFGID_CONFIGURATION_CLASS, "configuration-class",
		CFG_STRING, "",
		"Part of the Envir plugin mechanism: selects the class from which all configuration information will be obtained. This option lets you replace omnetpp.ini with some other implementation, e.g. database input. The simulation program still has to bootstrap from an omnetpp.ini (which contains the configuration-class setting). The class should implement the cConfigurationEx interface.");
Register_GlobalConfigOption(CFGID_USER_INTERFACE, "user-interface", CFG_STRING,
		"",
		"Selects the user interface to be started. Possible values are Cmdenv and Tkenv. This option is normally left empty, as it is more convenient to specify the user interface via a command-line option or the IDE's Run and Debug dialogs. New user interfaces can be defined by subclassing cRunnableEnvir.");

class JDEECoRuntime {
public:
	jobject host;
	JavaVM *jvm;
	const char * id;
	double firstCallAt;

	JDEECoRuntime(jobject host, JavaVM *jvm, const char *id) {
		this->jvm = jvm;
		this->host = host;
		this->id = id;
		this->firstCallAt = -1.0;
	}
};

// XXX: This should be a hash map. Having it in a vector will be too slow when we have many nodes.
std::vector<JDEECoRuntime *> jDEECoRuntimes;
std::vector<JDEECoModule *> jDEECoModules;

static JDEECoRuntime* findRuntime(const char *id) {
	JDEECoRuntime *result = NULL;

	for (std::vector<JDEECoRuntime *>::iterator it = jDEECoRuntimes.begin();
			it != jDEECoRuntimes.end(); ++it) {
		if (opp_strcmp((*it)->id, id) == 0) {
			result = *it;
			break;
		}
	}

	return result;
}

static JDEECoRuntime* findRuntime(JNIEnv *env, jstring id) {
	JDEECoRuntime *result = NULL;
	const char *cstring = env->GetStringUTFChars(id, 0);

	result = findRuntime(cstring);

	env->ReleaseStringUTFChars(id, cstring);
	return result;
}

static JDEECoModule* findModule(JNIEnv *env, jstring id) {
	JDEECoModule *result = NULL;
	const char *cstring = env->GetStringUTFChars(id, 0);

	for (std::vector<JDEECoModule *>::iterator it = jDEECoModules.begin();
			it != jDEECoModules.end(); ++it) {
		if (opp_strcmp((*it)->getModuleId(), cstring) == 0) {
			result = *it;
			break;
		}
	}

	env->ReleaseStringUTFChars(id, cstring);
	return result;
}

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

JNIEXPORT jdouble JNICALL Java_cz_cuni_mff_d3s_deeco_simulation_omnet_OMNetSimulation_nativeGetCurrentTime(
		JNIEnv *env, jobject jsimulation) {
	//std::cout << "nativeGetCurrentTime: Begin" << std::endl;
	jdouble result = -1.0;
	if (cSimulation::getActiveSimulation() != NULL)
		result = cSimulation::getActiveSimulation()->getSimTime().dbl();
	//std::cout << "nativeGetCurrentTime: Time is " << result << std::endl;
	//std::cout << "nativeGetCurrentTime: End" << std::endl;
	return result;
}

JNIEXPORT void JNICALL Java_cz_cuni_mff_d3s_deeco_simulation_omnet_OMNetSimulation_nativeRegister(
		JNIEnv *env, jobject jsimulation, jobject object, jstring id) {
	//std::cout << "nativeRegister: Begin" << std::endl;

	if (findRuntime(env, id) != NULL) {
		return;
	}

	JavaVM *jvm;
	jint status = env->GetJavaVM(&jvm);
	if (JNI_OK == status) {
		const char *cstring = env->GetStringUTFChars(id, 0);

		jDEECoRuntimes.push_back(
				new JDEECoRuntime(env->NewGlobalRef(object), jvm, cstring));

		env->ReleaseStringUTFChars(id, cstring);
		//std::cout << "nativeRegister: JDEECo runtime created for " << cstring << std::endl;
	} else {
		//std::cout << "nativeRegister: JVM could not be retrieved" << std::endl;
	}
	//std::cout << "nativeRegister: End" << std::endl;
}

JNIEXPORT void JNICALL Java_cz_cuni_mff_d3s_deeco_simulation_omnet_OMNetSimulation_nativeSendPacket(
		JNIEnv *env, jobject jsimulation, jstring id, jbyteArray packet,
		jstring recipient) {
	//std::cout << "nativeSendPacket: Begin" << std::endl;
	JDEECoModule *module = findModule(env, id);

	if (module != NULL) {
		int length = env->GetArrayLength(packet);
		jbyte *buffer = env->GetByteArrayElements(packet, 0);
		JDEECoPacket *jPacket = new JDEECoPacket(JDEECO_DATA_MESSAGE);
		//Setting data
		jPacket->setDataArraySize(length);
		for (int i = 0; i < length; i++)
			jPacket->setData(i, buffer[i]);
		const char *cRecipient = env->GetStringUTFChars(recipient, 0);
		EV << "OMNET++ ("<< simTime() <<") : " << module->getModuleId() << " sending packet with ID = " << jPacket->getId() << endl;
		module->sendPacket(jPacket, cRecipient);
		env->ReleaseByteArrayElements(packet, buffer, JNI_ABORT);
		env->ReleaseStringUTFChars(recipient, cRecipient);
		env->DeleteLocalRef(packet);
	}
	//std::cout << "nativeSendPacket: End" << std::endl;
}

JNIEXPORT void JNICALL Java_cz_cuni_mff_d3s_deeco_simulation_omnet_OMNetSimulation_nativeRun(
		JNIEnv *env, jobject jsimulation, jstring environment, jstring confFile) {
	//std::cout << "nativeRun: Begin" << std::endl;
	const char * cEnv = env->GetStringUTFChars(environment, 0);
	const char * cConfFile = env->GetStringUTFChars(confFile, 0);
	simulate(cEnv, cConfFile);
	jDEECoModules.clear();
	jDEECoRuntimes.clear();
	env->ReleaseStringUTFChars(environment, cEnv);
	env->ReleaseStringUTFChars(confFile, cConfFile);
	//std::cout << "nativeRun: End" << std::endl;
}

JNIEXPORT void JNICALL Java_cz_cuni_mff_d3s_deeco_simulation_omnet_OMNetSimulation_nativeCallAt(
		JNIEnv *env, jobject jsimulation, jdouble absoluteTime, jstring id) {
	//std::cout << "nativeCallAt: Begin" << std::endl;
	if (cSimulation::getActiveSimulation() != NULL) {
		JDEECoModule *module = findModule(env, id);

		if (module != NULL) {
			//std::cout << "nativeCallAt: " << (*it)->getModuleId() << " will be called at " << absoluteTime << std::endl;
			module->callAt(absoluteTime);
		}
	} else {
		JDEECoRuntime *runtime = findRuntime(env, id);

		if (runtime != NULL) {
			//std::cout << "nativeCallAt: " << (*it)->id << " will be first called at " << absoluteTime << std::endl;
			runtime->firstCallAt = absoluteTime;
		}
	}
	//std::cout << "nativeCallAt: End" << std::endl;
}

JNIEXPORT jboolean JNICALL Java_cz_cuni_mff_d3s_deeco_simulation_omnet_OMNetSimulation_nativeIsPositionInfoAvailable
  (JNIEnv *env, jobject jsimulation, jstring id) {
	//std::cout << "nativeIsPositionInfoAvailable: Begin" << std::endl;
	jboolean result = JNI_FALSE;
	JDEECoModule *module = findModule(env, id);

	if (module != NULL) {
		result = module->isPositionInfoAvailable() ? JNI_TRUE : JNI_FALSE;
	}

	//std::cout << "nativeIsPositionInfoAvailable: End" << std::endl;
	return result;
}

// XXX: These three methods should be replaced by one method that returns a triplet (X,Y,Z)

JNIEXPORT jdouble JNICALL Java_cz_cuni_mff_d3s_deeco_simulation_omnet_OMNetSimulation_nativeGetPositionX
  (JNIEnv *env, jobject jsimulation, jstring id) {
	//std::cout << "nativeGetPositionX: Begin" << std::endl;
	jdouble result = 0;
	JDEECoModule *module = findModule(env, id);

	if (module != NULL) {
		result = module->getPositionX();
	}

	//std::cout << "nativeGetPositionX: End" << std::endl;
	return result;
}

JNIEXPORT jdouble JNICALL Java_cz_cuni_mff_d3s_deeco_simulation_omnet_OMNetSimulation_nativeGetPositionY
  (JNIEnv *env, jobject jsimulation, jstring id) {
	//std::cout << "nativeGetPositionY: Begin" << std::endl;
	jdouble result = 0;
	JDEECoModule *module = findModule(env, id);

	if (module != NULL) {
		result = module->getPositionY();
	}

	//std::cout << "nativeGetPositionY: End" << std::endl;
	return result;
}

JNIEXPORT jdouble JNICALL Java_cz_cuni_mff_d3s_deeco_simulation_omnet_OMNetSimulation_nativeGetPositionZ
(JNIEnv *env, jobject jsimulation, jstring id) {
	//std::cout << "nativeGetPositionZ: Begin" << std::endl;
	jdouble result = 0;
	JDEECoModule *module = findModule(env, id);

	if (module != NULL) {
		result = module->getPositionZ();
	}

	//std::cout << "nativeGetPositionZ: End" << std::endl;
	return result;
}

JNIEXPORT void JNICALL Java_cz_cuni_mff_d3s_deeco_simulation_omnet_OMNetSimulation_nativeSetPosition
(JNIEnv *env, jobject jsimulation, jstring id, jdouble valX, jdouble valY, jdouble valZ) {
	JDEECoModule *module = findModule(env, id);

	if (module != NULL) {
		module->setPosition(valX, valY, valZ);
	}
}

DLLEXPORT_OR_IMPORT void JDEECoModule::callAt(double absoluteTime) {
	//std::cout << "jDEECoCallAt: " << this->jDEECoGetModuleId() << " Begin" << std::endl;
	if (currentCallAtTime != absoluteTime && simTime().dbl() < absoluteTime) {
		currentCallAtTime = absoluteTime;
		cMessage *msg = new cMessage(JDEECO_TIMER_MESSAGE);
		currentCallAtMessage = msg;
		scheduleAt(absoluteTime, msg);
		//std::cout << "jDEECoCallAt: " << this->jDEECoGetModuleId() << " Callback added: " << this->jDEECoGetModuleId() << " for " << absoluteTime << std::endl;
	}
	//std::cout << "jDEECoCallAt: " << this->jDEECoGetModuleId() << " End" << std::endl;
}

DLLEXPORT_OR_IMPORT void JDEECoModule::onHandleMessage(cMessage *msg, double rssi) {
	//std::cout << "jDEECoOnHandleMessage: " << this->jDEECoGetModuleId() << " Begin" << std::endl;
	JDEECoRuntime* runtime = findRuntime(getModuleId());

	if (runtime != NULL) {
		JNIEnv *env;
		//std::cout << "jDEECoOnHandleMessage: " << this->jDEECoGetModuleId() << " Before getting the environment" << std::endl;
		runtime->jvm->AttachCurrentThread((void **) &env, NULL);
		//std::cout << "jDEECoOnHandleMessage: " << this->jDEECoGetModuleId() << " Before getting jobject class" << std::endl;
		jclass cls = env->GetObjectClass(runtime->host);
		jmethodID mid;
		if (opp_strcmp(msg->getName(), JDEECO_TIMER_MESSAGE) == 0) {
			// compare in nanos
			if (((long) round(simTime().dbl() * 1000000)) == ((long) round(currentCallAtTime * 1000000))) {
				//std::cout << "jDEECoOnHandleMessage: " << this->jDEECoGetModuleId() << " Before getting the \"at\" method reference" << std::endl;
				mid = env->GetMethodID(cls, "at", "(D)V");
				if (mid == 0)
					return;
				//std::cout << "jDEECoOnHandleMessage: " << this->jDEECoGetModuleId() << " Before calling the \"at\" method" << std::endl;
				env->CallVoidMethod(runtime->host, mid, currentCallAtTime);
			} else {
				//Ignore the message as it is not valid any longer.
			}
		} else if (opp_strcmp(msg->getName(), JDEECO_DATA_MESSAGE) == 0) {
			//std::cout << "jDEECoOnHandleMessage: " << this->jDEECoGetModuleId() << " Before getting the \"packetRecived\" method reference" << std::endl;
			EV << "OMNET++ ("<< simTime() <<") : " << getModuleId() << " received packet with ID = " << msg->getId() << endl;
			mid = env->GetMethodID(cls, "packetReceived", "([BD)V");
			if (mid == 0)
				return;
			JDEECoPacket *jPacket = check_and_cast<JDEECoPacket *>(msg);
			jbyte *buffer = new jbyte[jPacket->getDataArraySize()];
			for (unsigned int i = 0; i < jPacket->getDataArraySize(); i++)
				buffer[i] = jPacket->getData(i);
			jbyteArray jArray = env->NewByteArray(jPacket->getDataArraySize());
			if (jArray == NULL) {
				std::cout << "onHandleMessage: " << this->getModuleId() << " Cannot create new ByteArray object! Out of memory problem?" << std::endl;
				return;
			}
			env->SetByteArrayRegion(jArray, 0, jPacket->getDataArraySize(),
					buffer);
			//std::cout << "jDEECoOnHandleMessage: " << this->jDEECoGetModuleId() << " Before calling the \"packetRecived\" method" << std::endl;
			env->CallVoidMethod(runtime->host, mid, jArray, rssi);
			//std::cout << "jDEECoOnHandleMessage: " << this->jDEECoGetModuleId() << " After calling the \"packetRecived\" method" << std::endl;
			env->DeleteLocalRef(jArray);
			//std::cout << "jDEECoOnHandleMessage: " << this->jDEECoGetModuleId() << " After deleting the array reference" << std::endl;
			delete [] buffer;
		}
		env->DeleteLocalRef(cls);
	}
	//std::cout << "jDEECoOnHandleMessage: " << this->jDEECoGetModuleId() << " End" << std::endl;
}

DLLEXPORT_OR_IMPORT void JDEECoModule::initialize() {
	//std::cout << "jDEECoInitialize: " << this->jDEECoGetModuleId() << " Begin" << std::endl;
	//std::cout << "jDEECoInitialize: " << this->jDEECoGetModuleId() << " Initializing jDEECo module: " << this->jDEECoGetModuleId() << std::endl;
	if (!initialized) {
		JDEECoRuntime *runtime = findRuntime(getModuleId());

		assert(runtime != NULL);

		if (runtime->firstCallAt >= 0)
			callAt(runtime->firstCallAt);

		jDEECoModules.push_back(this);
	}

	initialized = true;
	//std::cout << "jDEECoInitialize: " << this->jDEECoGetModuleId() << " End" << std::endl;
}
