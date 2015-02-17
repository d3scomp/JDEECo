#include "JDEECoRuntime.h"

#include <omnetpp.h>

JDEECoRuntime::JDEECoRuntime(jobject host, JavaVM *jvm, const char *id) {
	this->jvm = jvm;
	this->host = host;
	this->id = id;
	this->firstCallAt = -1.0;
}

JDEECoRuntime* JDEECoRuntime::findRuntime(const char *id) {
	JDEECoRuntime *result = NULL;

	//std::cout << "findRuntime: " << id << " Begin" << std::endl;

	//std::cout << "findRuntime: jDEECoRuntimes=" << &jDEECoRuntimes << std::endl;

	for (std::vector<JDEECoRuntime *>::iterator it = jDEECoRuntimes.begin();
			it != jDEECoRuntimes.end(); ++it) {
		//std::cout << "findRuntime: runtime with id " << (*it)->id << std::endl;
		if (opp_strcmp((*it)->id, id) == 0) {
			result = *it;
			break;
		}
	}

	//std::cout << "findRuntime: End" << std::endl;
	return result;
}

JDEECoRuntime* JDEECoRuntime::findRuntime(JNIEnv *env, jstring id) {
	JDEECoRuntime *result = NULL;
	const char *cstring = env->GetStringUTFChars(id, 0);

	result = findRuntime(cstring);

	env->ReleaseStringUTFChars(id, cstring);
	return result;
}

void JDEECoRuntime::addRuntime(JDEECoRuntime* runtime) {
	jDEECoRuntimes.push_back(runtime);
}

void JDEECoRuntime::clear() {
	jDEECoRuntimes.clear();
}

std::vector<JDEECoRuntime *> JDEECoRuntime::jDEECoRuntimes;
