#include "JDEECoRuntime.h"

#include <limits>

#include <omnetpp.h>

JDEECoRuntime::JDEECoRuntime(jobject host, JavaVM *jvm, NodeId id) {
	this->jvm = jvm;
	this->host = host;
	this->id = id;
	this->firstCallAt = std::numeric_limits<double>::min();
}

JDEECoRuntime* JDEECoRuntime::findRuntime(NodeId id) {
	JDEECoRuntime *result = NULL;

	//std::cout << "findRuntime: " << id << " Begin" << std::endl;

	//std::cout << "findRuntime: jDEECoRuntimes=" << &jDEECoRuntimes << std::endl;

	for (std::vector<JDEECoRuntime *>::iterator it = jDEECoRuntimes.begin();
			it != jDEECoRuntimes.end(); ++it) {
		//std::cout << "findRuntime: runtime with id " << (*it)->id << std::endl;
		if ((*it)->id == id) {
			result = *it;
			break;
		}
	}

	//std::cout << "findRuntime: End" << std::endl;
	return result;
}

JDEECoRuntime* JDEECoRuntime::findRuntime(JNIEnv *env, NodeId id) {
	JDEECoRuntime *result = NULL;

	result = findRuntime(id);

	return result;
}

void JDEECoRuntime::addRuntime(JDEECoRuntime* runtime) {
	jDEECoRuntimes.push_back(runtime);
}

void JDEECoRuntime::clearAll() {
	// TODO: Memory leak here?
	jDEECoRuntimes.clear();
}

std::vector<JDEECoRuntime *> JDEECoRuntime::jDEECoRuntimes;
