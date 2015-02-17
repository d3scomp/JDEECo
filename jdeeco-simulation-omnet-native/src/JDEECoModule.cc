#include "JDEECoModule.h"

#include <assert.h>

#include "JDEECoRuntime.h"

void JDEECoModule::callAt(double absoluteTime) {
	//std::cout << "jDEECoCallAt: " << this->getModuleId() << " Begin" << std::endl;
	if (currentCallAtTime != absoluteTime && simTime().dbl() < absoluteTime) {
		currentCallAtTime = absoluteTime;
		cMessage *msg = new cMessage(JDEECO_TIMER_MESSAGE);
		currentCallAtMessage = msg;
		registerCallbackAt(absoluteTime, msg);
		//std::cout << "jDEECoCallAt: " << this->getModuleId() << " Callback added: " << this->getModuleId() << " for " << absoluteTime << std::endl;
	}
	//std::cout << "jDEECoCallAt: " << this->jDEECoGetModuleId() << " End" << std::endl;
}

void JDEECoModule::onHandleMessage(cMessage *msg, double rssi) {
	//std::cout << "jDEECoOnHandleMessage: " << this->jDEECoGetModuleId() << " Begin" << std::endl;
	//std::cout << "jDEECoOnHandleMessage" << std::endl;
	JDEECoRuntime* runtime = JDEECoRuntime::findRuntime(getModuleId());
	if (runtime != NULL) {
		JNIEnv *env;
		//std::cout << "jDEECoOnHandleMessage: " << this->getModuleId() << " Before getting the environment" << std::endl;
		runtime->jvm->AttachCurrentThread((void **) &env, NULL);
		//std::cout << "jDEECoOnHandleMessage: " << this->getModuleId() << " Before getting jobject class" << std::endl;
		jclass cls = env->GetObjectClass(runtime->host);
		jmethodID mid;
		if (opp_strcmp(msg->getName(), JDEECO_TIMER_MESSAGE) == 0) {
			// compare in nanos
			if (((long) round(simTime().dbl() * 1000000)) == ((long) round(currentCallAtTime * 1000000))) {
				//std::cout << "jDEECoOnHandleMessage: " << this->getModuleId() << " Before getting the \"at\" method reference" << std::endl;
				mid = env->GetMethodID(cls, "at", "(D)V");
				if (mid == 0)
					return;
				//std::cout << "jDEECoOnHandleMessage: " << this->getModuleId() << " Before calling the \"at\" method" << std::endl;
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
			//std::cout << "jDEECoOnHandleMessage: " << this->getModuleId() << " Before calling the \"packetRecived\" method" << std::endl;
			env->CallVoidMethod(runtime->host, mid, jArray, rssi);
			//std::cout << "jDEECoOnHandleMessage: " << this->getModuleId() << " After calling the \"packetRecived\" method" << std::endl;
			env->DeleteLocalRef(jArray);
			//std::cout << "jDEECoOnHandleMessage: " << this->getModuleId() << " After deleting the array reference" << std::endl;
			delete [] buffer;
		}
		env->DeleteLocalRef(cls);
	}
	//std::cout << "jDEECoOnHandleMessage: " << this->jDEECoGetModuleId() << " End" << std::endl;
}

void JDEECoModule::initialize() {
	std::cout << "initialize: " << this->getModuleId() << " Begin" << std::endl;
	std::cout << "initialize: " << this->getModuleId() << " Initializing jDEECo module: " << this->getModuleId() << std::endl;
	if (!initialized) {
		JDEECoRuntime *runtime = JDEECoRuntime::findRuntime(getModuleId());

		assert(runtime != NULL);

		if (runtime->firstCallAt >= 0) {
			std::cout << "adding the first callback: " << this->getModuleId() << std::endl;
			callAt(runtime->firstCallAt);
		}

		jDEECoModules.push_back(this);
	}

	initialized = true;
	std::cout << "initialize: " << this->getModuleId() << " End" << std::endl;
}

JDEECoModule* JDEECoModule::findModule(JNIEnv *env, jstring id) {
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

void JDEECoModule::clear() {
	jDEECoModules.clear();
}

std::vector<JDEECoModule*> JDEECoModule::jDEECoModules;
