#include "OMNeTJDEECoAPI.h"

#include <omnetpp.h>

#include "JDEECoRuntime.h"
#include "JDEECoModule.h"
#include "simulation.h"

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
	std::cout << "nativeRegister: Begin" << std::endl;

	if (JDEECoRuntime::findRuntime(env, id) != NULL) {
		return;
	}

	JavaVM *jvm;
	jint status = env->GetJavaVM(&jvm);
	if (JNI_OK == status) {
		const char *cstring = env->GetStringUTFChars(id, 0);

		JDEECoRuntime::addRuntime(new JDEECoRuntime(env->NewGlobalRef(object), jvm, cstring));

		JDEECoRuntime::findRuntime(env, id);

//  XXX: If simulation is repeated and runtimes are re-registered, then this should be called somewhere in the cleanup
//	env->ReleaseStringUTFChars(id, cstring);
		std::cout << "nativeRegister: JDEECo runtime created for " << cstring << std::endl;
	} else {
		std::cout << "nativeRegister: JVM could not be retrieved" << std::endl;
	}
	std::cout << "nativeRegister: End" << std::endl;
}

JNIEXPORT void JNICALL Java_cz_cuni_mff_d3s_deeco_simulation_omnet_OMNetSimulation_nativeSendPacket(
		JNIEnv *env, jobject jsimulation, jstring id, jbyteArray packet,
		jstring recipient) {
	//std::cout << "nativeSendPacket: Begin" << std::endl;
	JDEECoModule *module = JDEECoModule::findModule(env, id);

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
	std::cout << "nativeRun: Begin" << std::endl;
	const char * cEnv = env->GetStringUTFChars(environment, 0);
	const char * cConfFile = env->GetStringUTFChars(confFile, 0);
    std::cout << "nativeRun: simulate" << std::endl;
	simulate(cEnv, cConfFile);
    std::cout << "nativeRun: clearing runtimes" << std::endl;
	JDEECoModule::clear();
	JDEECoRuntime::clear();
	env->ReleaseStringUTFChars(environment, cEnv);
	env->ReleaseStringUTFChars(confFile, cConfFile);
	std::cout << "nativeRun: End" << std::endl;
}

JNIEXPORT void JNICALL Java_cz_cuni_mff_d3s_deeco_simulation_omnet_OMNetSimulation_nativeCallAt(
		JNIEnv *env, jobject jsimulation, jdouble absoluteTime, jstring id) {
	//std::cout << "nativeCallAt: Begin" << std::endl;
	if (cSimulation::getActiveSimulation() != NULL) {
		JDEECoModule *module = JDEECoModule::findModule(env, id);

		if (module != NULL) {
			//std::cout << "nativeCallAt: " << (*it)->getModuleId() << " will be called at " << absoluteTime << std::endl;
			module->callAt(absoluteTime);
		}
	} else {
		JDEECoRuntime *runtime = JDEECoRuntime::findRuntime(env, id);

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
	JDEECoModule *module = JDEECoModule::findModule(env, id);

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
	JDEECoModule *module = JDEECoModule::findModule(env, id);

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
	JDEECoModule *module = JDEECoModule::findModule(env, id);

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
	JDEECoModule *module = JDEECoModule::findModule(env, id);

	if (module != NULL) {
		result = module->getPositionZ();
	}

	//std::cout << "nativeGetPositionZ: End" << std::endl;
	return result;
}

JNIEXPORT void JNICALL Java_cz_cuni_mff_d3s_deeco_simulation_omnet_OMNetSimulation_nativeSetPosition
(JNIEnv *env, jobject jsimulation, jstring id, jdouble valX, jdouble valY, jdouble valZ) {
	JDEECoModule *module = JDEECoModule::findModule(env, id);

	if (module != NULL) {
		module->setPosition(valX, valY, valZ);
	}
}
