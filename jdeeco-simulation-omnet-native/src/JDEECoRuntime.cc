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
	std::unordered_map<int, JDEECoRuntime *>::iterator it = jDEECoRuntimes.find(id);

	if(it == jDEECoRuntimes.end()) {
		return NULL;
	} else {
		return it->second;
	}
}

JDEECoRuntime* JDEECoRuntime::findRuntime(JNIEnv *env, NodeId id) {
	JDEECoRuntime *result = NULL;

	result = findRuntime(id);

	return result;
}

void JDEECoRuntime::addRuntime(JDEECoRuntime* runtime) {
	jDEECoRuntimes[runtime->id] = runtime;
}

void JDEECoRuntime::clearAll() {
	// TODO: Memory leak here?
	jDEECoRuntimes.clear();
}

std::unordered_map<int, JDEECoRuntime *> JDEECoRuntime::jDEECoRuntimes;
