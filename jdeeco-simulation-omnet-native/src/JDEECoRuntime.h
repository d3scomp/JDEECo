#ifndef JDEECORUNTIME_H
#define JDEECORUNTIME_H

#include <jni.h>

#include <vector>

class JDEECoRuntime {
public:
	jobject host;
	JavaVM *jvm;
	const char * id;
	double firstCallAt;

	JDEECoRuntime(jobject host, JavaVM *jvm, const char *id);

	// Runtime registry
	static JDEECoRuntime* findRuntime(const char *id);
	static JDEECoRuntime* findRuntime(JNIEnv *env, jstring id);
	static void addRuntime(JDEECoRuntime* runtime);
	static void clearAll();

private:
	// XXX: This should be a hash map. Having it in a vector will be too slow when we have many nodes.
	static std::vector<JDEECoRuntime *> jDEECoRuntimes;
};

#endif // JDEECORUNTIME_H
